/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.tests.internal.context.java;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.common.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.core.context.PersistentAttribute;
import org.eclipse.jpt.core.context.TemporalConverter;
import org.eclipse.jpt.core.context.TemporalType;
import org.eclipse.jpt.core.context.VersionMapping;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.core.resource.java.TemporalAnnotation;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkConvert;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkMutable;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkVersionMapping;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLink;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLinkConvertAnnotation;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLinkMutableAnnotation;
import org.eclipse.jpt.eclipselink.core.tests.internal.context.EclipseLinkContextModelTestCase;

@SuppressWarnings("nls")
public class EclipseLinkJavaVersionMappingTests extends EclipseLinkContextModelTestCase
{
	
	private ICompilationUnit createTestEntityWithVersionMapping() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.VERSION);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Version").append(CR);
			}
		});
	}

	
	private ICompilationUnit createTestEntityWithConvert() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.VERSION, EclipseLink.CONVERT);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Version").append(CR);
				sb.append("@Convert(\"class-instance\")").append(CR);
			}
		});
	}
	
	private ICompilationUnit createTestEntityWithMutableVersion() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.VERSION, EclipseLink.MUTABLE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Version").append(CR);
				sb.append("@Mutable").append(CR);
			}
		});
	}
	
	private ICompilationUnit createTestEntityWithMutableVersionDate() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.VERSION, EclipseLink.MUTABLE, "java.util.Date");
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Version").append(CR);
				sb.append("    @Mutable").append(CR);
				sb.append("    private Date myDate;").append(CR);
				sb.append(CR);
				sb.append("    ");
			}
		});
	}
	
	public EclipseLinkJavaVersionMappingTests(String name) {
		super(name);
	}

	public void testGetConvert() throws Exception {
		createTestEntityWithVersionMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		attributeResource.addAnnotation(EclipseLinkConvertAnnotation.ANNOTATION_NAME);
		getJpaProject().synchronizeContextModel();
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		VersionMapping versionMapping = (VersionMapping) persistentAttribute.getMapping();

		assertEquals(EclipseLinkConvert.class, versionMapping.getConverter().getType());
	}
	
	public void testGetConvert2() throws Exception {
		createTestEntityWithConvert();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		VersionMapping versionMapping = (VersionMapping) persistentAttribute.getMapping();

		assertEquals(EclipseLinkConvert.class, versionMapping.getConverter().getType());
		assertEquals(EclipseLinkConvert.CLASS_INSTANCE_CONVERTER, ((EclipseLinkConvert) versionMapping.getConverter()).getConverterName());
	}

	public void testSetConvert() throws Exception {
		createTestEntityWithVersionMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		VersionMapping versionMapping = (VersionMapping) persistentAttribute.getMapping();
		assertNull(versionMapping.getConverter().getType());
		
		versionMapping.setConverter(TemporalConverter.class);
		((TemporalConverter) versionMapping.getConverter()).setTemporalType(TemporalType.TIME);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		TemporalAnnotation temporal = (TemporalAnnotation) attributeResource.getAnnotation(TemporalAnnotation.ANNOTATION_NAME);
		
		assertEquals(org.eclipse.jpt.core.resource.java.TemporalType.TIME, temporal.getValue());
		
		versionMapping.setConverter(null);
		assertNull(attributeResource.getAnnotation(TemporalAnnotation.ANNOTATION_NAME));
	}
	
	public void testGetConvertUpdatesFromResourceModelChange() throws Exception {
		createTestEntityWithVersionMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		VersionMapping versionMapping = (VersionMapping) persistentAttribute.getMapping();

		assertNull(versionMapping.getConverter().getType());
		
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkConvertAnnotation convert = (EclipseLinkConvertAnnotation) attributeResource.addAnnotation(EclipseLinkConvertAnnotation.ANNOTATION_NAME);
		convert.setValue("foo");
		getJpaProject().synchronizeContextModel();
		
		assertEquals(EclipseLinkConvert.class, versionMapping.getConverter().getType());
		assertEquals("foo", ((EclipseLinkConvert) versionMapping.getConverter()).getConverterName());
		
		attributeResource.removeAnnotation(EclipseLinkConvertAnnotation.ANNOTATION_NAME);
		getJpaProject().synchronizeContextModel();
		
		assertNull(versionMapping.getConverter().getType());
		assertFalse(versionMapping.isDefault());
		assertSame(versionMapping, persistentAttribute.getMapping());
	}
	
	public void testGetSpecifiedMutable() throws Exception {
		createTestEntityWithMutableVersion();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		EclipseLinkVersionMapping versionMapping = (EclipseLinkVersionMapping) persistentAttribute.getMapping();
		EclipseLinkMutable mutable = versionMapping.getMutable();
		assertEquals(Boolean.TRUE, mutable.getSpecifiedMutable());
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkMutableAnnotation mutableAnnotation = (EclipseLinkMutableAnnotation) attributeResource.getAnnotation(EclipseLinkMutableAnnotation.ANNOTATION_NAME);
		mutableAnnotation.setValue(Boolean.TRUE);
		getJpaProject().synchronizeContextModel();
		
		assertEquals(Boolean.TRUE, mutable.getSpecifiedMutable());

		mutableAnnotation.setValue(null);
		getJpaProject().synchronizeContextModel();
		assertEquals(Boolean.TRUE, mutable.getSpecifiedMutable());

		mutableAnnotation.setValue(Boolean.FALSE);
		getJpaProject().synchronizeContextModel();
		assertEquals(Boolean.FALSE, mutable.getSpecifiedMutable());
		
		attributeResource.removeAnnotation(EclipseLinkMutableAnnotation.ANNOTATION_NAME);
		getJpaProject().synchronizeContextModel();
		assertEquals(null, mutable.getSpecifiedMutable());
		
		attributeResource.addAnnotation(EclipseLinkMutableAnnotation.ANNOTATION_NAME);
		getJpaProject().synchronizeContextModel();
		assertEquals(Boolean.TRUE, mutable.getSpecifiedMutable());
	}
	
	public void testSetSpecifiedMutable() throws Exception {
		createTestEntityWithMutableVersion();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		EclipseLinkVersionMapping versionMapping = (EclipseLinkVersionMapping) persistentAttribute.getMapping();
		EclipseLinkMutable mutable = versionMapping.getMutable();
		assertEquals(Boolean.TRUE, mutable.getSpecifiedMutable());
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkMutableAnnotation mutableAnnotation = (EclipseLinkMutableAnnotation) attributeResource.getAnnotation(EclipseLinkMutableAnnotation.ANNOTATION_NAME);
		assertEquals(null, mutableAnnotation.getValue());
		
		mutable.setSpecifiedMutable(Boolean.TRUE);	
		assertEquals(null, mutableAnnotation.getValue());

		mutable.setSpecifiedMutable(null);
		mutableAnnotation = (EclipseLinkMutableAnnotation) attributeResource.getAnnotation(EclipseLinkMutableAnnotation.ANNOTATION_NAME);
		assertEquals(null, mutableAnnotation);
		
		mutable.setSpecifiedMutable(Boolean.FALSE);	
		mutableAnnotation = (EclipseLinkMutableAnnotation) attributeResource.getAnnotation(EclipseLinkMutableAnnotation.ANNOTATION_NAME);
		assertEquals(Boolean.FALSE, mutableAnnotation.getValue());
		
		mutable.setSpecifiedMutable(Boolean.TRUE);	
		assertEquals(Boolean.TRUE, mutableAnnotation.getValue());
	}
	
	public void testIsDefaultMutable() throws Exception {
		createTestEntityWithMutableVersion();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		EclipseLinkVersionMapping versionMapping = (EclipseLinkVersionMapping) persistentAttribute.getMapping();
		EclipseLinkMutable mutable = versionMapping.getMutable();
		assertTrue(mutable.isDefaultMutable());
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		attributeResource.removeAnnotation(EclipseLinkMutableAnnotation.ANNOTATION_NAME);
		assertTrue(mutable.isDefaultMutable());
		
		mutable.setSpecifiedMutable(Boolean.FALSE);	
		assertTrue(mutable.isDefaultMutable());
		
		//set mutable default to false in the persistence unit properties, verify default in java still true since this is not a Date/Calendar
		(getPersistenceUnit()).getOptions().setTemporalMutable(Boolean.FALSE);
		assertTrue(mutable.isDefaultMutable());
	}
	
	public void testIsDefaultMutableForDate() throws Exception {
		createTestEntityWithMutableVersionDate();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		EclipseLinkVersionMapping versionMapping = (EclipseLinkVersionMapping) persistentAttribute.getMapping();
		EclipseLinkMutable mutable = versionMapping.getMutable();
		assertFalse(mutable.isDefaultMutable());
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		attributeResource.removeAnnotation(EclipseLinkMutableAnnotation.ANNOTATION_NAME);
		assertFalse(mutable.isDefaultMutable());
		
		mutable.setSpecifiedMutable(Boolean.TRUE);	
		assertFalse(mutable.isDefaultMutable());
		
		//set mutable default to false in the persistence unit properties, verify default in java still true since this is not a Date/Calendar
		(getPersistenceUnit()).getOptions().setTemporalMutable(Boolean.TRUE);
		assertTrue(mutable.isDefaultMutable());
		
		(getPersistenceUnit()).getOptions().setTemporalMutable(Boolean.FALSE);
		assertFalse(mutable.isDefaultMutable());
		
		(getPersistenceUnit()).getOptions().setTemporalMutable(null);
		assertFalse(mutable.isDefaultMutable());
	}
	
	public void testIsMutable() throws Exception {
		createTestEntityWithMutableVersion();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		EclipseLinkVersionMapping versionMapping = (EclipseLinkVersionMapping) persistentAttribute.getMapping();
		EclipseLinkMutable mutable = versionMapping.getMutable();
		assertTrue(mutable.isMutable());
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		attributeResource.removeAnnotation(EclipseLinkMutableAnnotation.ANNOTATION_NAME);
		assertTrue(mutable.isMutable());
		
		mutable.setSpecifiedMutable(Boolean.TRUE);	
		assertTrue(mutable.isMutable());
	}
}
