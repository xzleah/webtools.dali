/*******************************************************************************
 *  Copyright (c) 2011  Oracle. All rights reserved.
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0, which accompanies this distribution
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jaxb.core.tests.internal.context.java;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAttribute;
import org.eclipse.jpt.common.core.utility.jdt.AnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.Member;
import org.eclipse.jpt.common.core.utility.jdt.ModifiedDeclaration;
import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.common.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.jaxb.core.MappingKeys;
import org.eclipse.jpt.jaxb.core.context.JaxbPersistentAttribute;
import org.eclipse.jpt.jaxb.core.context.JaxbPersistentClass;
import org.eclipse.jpt.jaxb.core.context.XmlAttributeMapping;
import org.eclipse.jpt.jaxb.core.context.XmlElementRef;
import org.eclipse.jpt.jaxb.core.context.XmlElementRefMapping;
import org.eclipse.jpt.jaxb.core.resource.java.JAXB;
import org.eclipse.jpt.jaxb.core.resource.java.XmlAttributeAnnotation;
import org.eclipse.jpt.jaxb.core.resource.java.XmlElementRefAnnotation;
import org.eclipse.jpt.jaxb.core.resource.java.XmlElementWrapperAnnotation;
import org.eclipse.jpt.jaxb.core.resource.java.XmlJavaTypeAdapterAnnotation;
import org.eclipse.jpt.jaxb.core.tests.internal.context.JaxbContextModelTestCase;


public class GenericJavaXmlElementRefMappingTests
		extends JaxbContextModelTestCase {
	
	public GenericJavaXmlElementRefMappingTests(String name) {
		super(name);
	}
	
	
	private ICompilationUnit createTypeWithXmlElementRef() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JAXB.XML_TYPE, JAXB.XML_ELEMENT_REF);
			}
			
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@XmlType");
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@XmlElementRef");
			}
		});
	}
	
	
	public void testModifyName() throws Exception {
		createTypeWithXmlElementRef();

		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementRefMapping xmlElementRefMapping = (XmlElementRefMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		XmlElementRef xmlElementRef = xmlElementRefMapping.getXmlElementRef();
		JavaResourceAttribute resourceAttribute = xmlElementRefMapping.getPersistentAttribute().getJavaResourceAttribute();
		
		assertNull(xmlElementRef.getQName().getSpecifiedName());
		assertEquals("id", xmlElementRef.getQName().getDefaultName());
		assertEquals("id", xmlElementRef.getQName().getName());

		xmlElementRef.getQName().setSpecifiedName("foo");
		XmlElementRefAnnotation xmlElementRefAnnotation = (XmlElementRefAnnotation) resourceAttribute.getAnnotation(XmlElementRefAnnotation.ANNOTATION_NAME);
		assertEquals("foo", xmlElementRefAnnotation.getName());
		assertEquals("foo", xmlElementRef.getQName().getSpecifiedName());
		assertEquals("id", xmlElementRef.getQName().getDefaultName());
		assertEquals("foo", xmlElementRef.getQName().getName());

		xmlElementRef.getQName().setSpecifiedName(null);
		xmlElementRefAnnotation = (XmlElementRefAnnotation) resourceAttribute.getAnnotation(XmlElementRefAnnotation.ANNOTATION_NAME);
		assertNull(xmlElementRefAnnotation.getName());
		assertNull(xmlElementRef.getQName().getSpecifiedName());
	}

	public void testUpdateName() throws Exception {
		createTypeWithXmlElementRef();

		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementRefMapping xmlElementRefMapping = (XmlElementRefMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		XmlElementRef xmlElementRef = xmlElementRefMapping.getXmlElementRef();
		JavaResourceAttribute resourceAttribute = xmlElementRefMapping.getPersistentAttribute().getJavaResourceAttribute();

		assertNull(xmlElementRef.getQName().getSpecifiedName());


		//add a Name member value pair
		AnnotatedElement annotatedElement = this.annotatedElement(resourceAttribute);
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				GenericJavaXmlElementRefMappingTests.this.addXmlElementRefMemberValuePair(declaration, JAXB.XML_ELEMENT_REF__NAME, "foo");
			}
		});
		assertEquals("foo", xmlElementRef.getQName().getName());

		//remove the Name member value pair
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				NormalAnnotation xmlElementRefAnnotation = (NormalAnnotation) GenericJavaXmlElementRefMappingTests.this.getXmlElementRefAnnotation(declaration);
				GenericJavaXmlElementRefMappingTests.this.values(xmlElementRefAnnotation).remove(0);
			}
		});
		assertNull(xmlElementRef.getQName().getSpecifiedName());
	}

	public void testModifyNamespace() throws Exception {
		createTypeWithXmlElementRef();

		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementRefMapping xmlElementRefMapping = (XmlElementRefMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		XmlElementRef xmlElementRef = xmlElementRefMapping.getXmlElementRef();
		JavaResourceAttribute resourceAttribute = xmlElementRefMapping.getPersistentAttribute().getJavaResourceAttribute();

		assertNull(xmlElementRef.getQName().getSpecifiedNamespace());

		xmlElementRef.getQName().setSpecifiedNamespace("foo");
		XmlElementRefAnnotation xmlElementRefAnnotation = (XmlElementRefAnnotation) resourceAttribute.getAnnotation(XmlElementRefAnnotation.ANNOTATION_NAME);
		assertEquals("foo", xmlElementRefAnnotation.getNamespace());
		assertEquals("foo", xmlElementRef.getQName().getNamespace());

		xmlElementRef.getQName().setSpecifiedNamespace(null);
		xmlElementRefAnnotation = (XmlElementRefAnnotation) resourceAttribute.getAnnotation(XmlElementRefAnnotation.ANNOTATION_NAME);
		assertNull(xmlElementRefAnnotation.getNamespace());
		assertNull(xmlElementRef.getQName().getSpecifiedNamespace());
	}

	public void testUpdateNamespace() throws Exception {
		createTypeWithXmlElementRef();

		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementRefMapping xmlElementRefMapping = (XmlElementRefMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		XmlElementRef xmlElementRef = xmlElementRefMapping.getXmlElementRef();
		JavaResourceAttribute resourceAttribute = xmlElementRefMapping.getPersistentAttribute().getJavaResourceAttribute();

		assertNull(xmlElementRef.getQName().getSpecifiedNamespace());


		//add a namespace member value pair
		AnnotatedElement annotatedElement = this.annotatedElement(resourceAttribute);
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				GenericJavaXmlElementRefMappingTests.this.addXmlElementRefMemberValuePair(declaration, JAXB.XML_ELEMENT_REF__NAMESPACE, "foo");
			}
		});
		assertEquals("foo", xmlElementRef.getQName().getNamespace());

		//remove the namespace member value pair
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				NormalAnnotation xmlElementRefAnnotation = (NormalAnnotation) GenericJavaXmlElementRefMappingTests.this.getXmlElementRefAnnotation(declaration);
				GenericJavaXmlElementRefMappingTests.this.values(xmlElementRefAnnotation).remove(0);
			}
		});
		assertNull(xmlElementRef.getQName().getSpecifiedNamespace());
	}

	public void testModifyRequired() throws Exception {
		createTypeWithXmlElementRef();

		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementRefMapping xmlElementRefMapping = (XmlElementRefMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		XmlElementRef xmlElementRef = xmlElementRefMapping.getXmlElementRef();
		JavaResourceAttribute resourceAttribute = xmlElementRefMapping.getPersistentAttribute().getJavaResourceAttribute();

		assertNull(xmlElementRef.getSpecifiedRequired());
		assertEquals(false, xmlElementRef.isDefaultRequired());
		assertEquals(false, xmlElementRef.isRequired());

		xmlElementRef.setSpecifiedRequired(Boolean.TRUE);
		XmlElementRefAnnotation xmlElementRefAnnotation = (XmlElementRefAnnotation) resourceAttribute.getAnnotation(XmlElementRefAnnotation.ANNOTATION_NAME);
		assertEquals(Boolean.TRUE, xmlElementRefAnnotation.getRequired());
		assertEquals(Boolean.TRUE, xmlElementRef.getSpecifiedRequired());
		assertEquals(false, xmlElementRef.isDefaultRequired());
		assertEquals(true, xmlElementRef.isRequired());

		xmlElementRef.setSpecifiedRequired(null);
		xmlElementRefAnnotation = (XmlElementRefAnnotation) resourceAttribute.getAnnotation(XmlElementRefAnnotation.ANNOTATION_NAME);
		assertNull(xmlElementRefAnnotation.getName());
		assertNull(xmlElementRef.getSpecifiedRequired());
		assertEquals(false, xmlElementRef.isDefaultRequired());
		assertEquals(false, xmlElementRef.isRequired());
	}

	public void testUpdateRequired() throws Exception {
		createTypeWithXmlElementRef();

		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementRefMapping xmlElementRefMapping = (XmlElementRefMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		XmlElementRef xmlElementRef = xmlElementRefMapping.getXmlElementRef();
		JavaResourceAttribute resourceAttribute = xmlElementRefMapping.getPersistentAttribute().getJavaResourceAttribute();

		assertNull(xmlElementRef.getSpecifiedRequired());
		assertEquals(false, xmlElementRef.isDefaultRequired());
		assertEquals(false, xmlElementRef.isRequired());


		//add a required member value pair
		AnnotatedElement annotatedElement = this.annotatedElement(resourceAttribute);
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				GenericJavaXmlElementRefMappingTests.this.addXmlElementRefMemberValuePair(declaration, JAXB.XML_ELEMENT_REF__REQUIRED, true);
			}
		});
		assertEquals(Boolean.TRUE, xmlElementRef.getSpecifiedRequired());
		assertEquals(false, xmlElementRef.isDefaultRequired());
		assertEquals(true, xmlElementRef.isRequired());

		//remove the required member value pair
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				NormalAnnotation xmlElementRefAnnotation = (NormalAnnotation) GenericJavaXmlElementRefMappingTests.this.getXmlElementRefAnnotation(declaration);
				GenericJavaXmlElementRefMappingTests.this.values(xmlElementRefAnnotation).remove(0);
			}
		});
		assertNull(xmlElementRef.getSpecifiedRequired());
		assertEquals(false, xmlElementRef.isDefaultRequired());
		assertEquals(false, xmlElementRef.isRequired());
	}

	public void testModifyType() throws Exception {
		createTypeWithXmlElementRef();
		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementRefMapping xmlElementRefMapping = (XmlElementRefMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		XmlElementRef xmlElementRef = xmlElementRefMapping.getXmlElementRef();
		
		assertNull(xmlElementRef.getSpecifiedType());
		assertEquals("int", xmlElementRef.getType());
		assertEquals("int", xmlElementRef.getDefaultType());
		
		xmlElementRef.setSpecifiedType("Foo");
		assertEquals("Foo", xmlElementRef.getSpecifiedType());
		assertEquals("Foo", xmlElementRef.getType());
		
		xmlElementRef.setSpecifiedType(null);
		assertNull(xmlElementRef.getSpecifiedType());
		assertEquals("int", xmlElementRef.getType());
	}
	
	public void testUpdateType() throws Exception {
		createTypeWithXmlElementRef();
		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementRefMapping xmlElementRefMapping = (XmlElementRefMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		XmlElementRef xmlElementRef = xmlElementRefMapping.getXmlElementRef();
		JavaResourceAttribute resourceAttribute = xmlElementRefMapping.getPersistentAttribute().getJavaResourceAttribute();
		
		assertNull(xmlElementRef.getSpecifiedType());
		assertEquals("int", xmlElementRef.getDefaultType());
		assertEquals("int", xmlElementRef.getType());
		
		//add a Type member value pair
		AnnotatedElement annotatedElement = this.annotatedElement(resourceAttribute);
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				GenericJavaXmlElementRefMappingTests.this.addXmlElementRefTypeMemberValuePair(declaration, JAXB.XML_ELEMENT_REF__TYPE, "Foo");
			}
		});
		assertEquals("Foo", xmlElementRef.getSpecifiedType());
		assertEquals("Foo", xmlElementRef.getType());
		
		//remove the Type member value pair
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				NormalAnnotation xmlElementRefAnnotation = (NormalAnnotation) GenericJavaXmlElementRefMappingTests.this.getXmlElementRefAnnotation(declaration);
				GenericJavaXmlElementRefMappingTests.this.values(xmlElementRefAnnotation).remove(0);
			}
		});
		assertNull(xmlElementRef.getSpecifiedType());
		assertEquals("int", xmlElementRef.getType());
	}

	public void testChangeMappingType() throws Exception {
		createTypeWithXmlElementRef();

		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		JaxbPersistentAttribute persistentAttribute = CollectionTools.get(persistentClass.getAttributes(), 0);
		XmlElementRefMapping xmlElementRefMapping = (XmlElementRefMapping) persistentAttribute.getMapping();
		JavaResourceAttribute resourceAttribute = xmlElementRefMapping.getPersistentAttribute().getJavaResourceAttribute();

		assertNotNull(xmlElementRefMapping);
		assertNotNull(resourceAttribute.getAnnotation(XmlElementRefAnnotation.ANNOTATION_NAME));

		persistentAttribute.setMappingKey(MappingKeys.XML_ATTRIBUTE_ATTRIBUTE_MAPPING_KEY);
		XmlAttributeMapping xmlAttributeMapping = (XmlAttributeMapping) persistentAttribute.getMapping();
		assertNotNull(xmlAttributeMapping);
		assertNull(resourceAttribute.getAnnotation(XmlElementRefAnnotation.ANNOTATION_NAME));
		assertNotNull(resourceAttribute.getAnnotation(XmlAttributeAnnotation.ANNOTATION_NAME));


		persistentAttribute.setMappingKey(MappingKeys.XML_ELEMENT_REF_ATTRIBUTE_MAPPING_KEY);
		xmlElementRefMapping = (XmlElementRefMapping) persistentAttribute.getMapping();
		assertNotNull(xmlElementRefMapping);
		assertNotNull(resourceAttribute.getAnnotation(XmlElementRefAnnotation.ANNOTATION_NAME));
		assertNull(resourceAttribute.getAnnotation(XmlAttributeAnnotation.ANNOTATION_NAME));
	}

	public void testModifyXmlJavaTypeAdapter() throws Exception {
		createTypeWithXmlElementRef();

		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		JaxbPersistentAttribute persistentAttribute = CollectionTools.get(persistentClass.getAttributes(), 0);
		XmlElementRefMapping xmlElementRefMapping = (XmlElementRefMapping) persistentAttribute.getMapping();
		JavaResourceAttribute resourceAttribute = xmlElementRefMapping.getPersistentAttribute().getJavaResourceAttribute();

		XmlJavaTypeAdapterAnnotation xmlJavaTypeAdapterAnnotation = (XmlJavaTypeAdapterAnnotation) resourceAttribute.getAnnotation(0, XmlJavaTypeAdapterAnnotation.ANNOTATION_NAME);
		assertNull(xmlElementRefMapping.getXmlJavaTypeAdapter());
		assertNull(xmlJavaTypeAdapterAnnotation);

		xmlElementRefMapping.addXmlJavaTypeAdapter();
		xmlJavaTypeAdapterAnnotation = (XmlJavaTypeAdapterAnnotation) resourceAttribute.getAnnotation(0, XmlJavaTypeAdapterAnnotation.ANNOTATION_NAME);
		assertNotNull(xmlElementRefMapping.getXmlJavaTypeAdapter());
		assertNotNull(xmlJavaTypeAdapterAnnotation);

		xmlElementRefMapping.removeXmlJavaTypeAdapter();
		xmlJavaTypeAdapterAnnotation = (XmlJavaTypeAdapterAnnotation) resourceAttribute.getAnnotation(0, XmlJavaTypeAdapterAnnotation.ANNOTATION_NAME);
		assertNull(xmlElementRefMapping.getXmlJavaTypeAdapter());
		assertNull(xmlJavaTypeAdapterAnnotation);
	}

	public void testUpdateXmlJavaTypeAdapter() throws Exception {
		createTypeWithXmlElementRef();

		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		JaxbPersistentAttribute persistentAttribute = CollectionTools.get(persistentClass.getAttributes(), 0);
		XmlElementRefMapping xmlElementRefMapping = (XmlElementRefMapping) persistentAttribute.getMapping();
		JavaResourceAttribute resourceAttribute = xmlElementRefMapping.getPersistentAttribute().getJavaResourceAttribute();

		XmlJavaTypeAdapterAnnotation xmlJavaTypeAdapterAnnotation = (XmlJavaTypeAdapterAnnotation) resourceAttribute.getAnnotation(0, XmlJavaTypeAdapterAnnotation.ANNOTATION_NAME);
		assertNull(xmlElementRefMapping.getXmlJavaTypeAdapter());
		assertNull(xmlJavaTypeAdapterAnnotation);


		//add an XmlJavaTypeAdapter annotation
		AnnotatedElement annotatedElement = this.annotatedElement(resourceAttribute);
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				GenericJavaXmlElementRefMappingTests.this.addMarkerAnnotation(declaration.getDeclaration(), XmlJavaTypeAdapterAnnotation.ANNOTATION_NAME);
			}
		});
		xmlJavaTypeAdapterAnnotation = (XmlJavaTypeAdapterAnnotation) resourceAttribute.getAnnotation(0, XmlJavaTypeAdapterAnnotation.ANNOTATION_NAME);
		assertNotNull(xmlElementRefMapping.getXmlJavaTypeAdapter());
		assertNotNull(xmlJavaTypeAdapterAnnotation);

		//remove the XmlJavaTypeAdapter annotation
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				GenericJavaXmlElementRefMappingTests.this.removeAnnotation(declaration, XmlJavaTypeAdapterAnnotation.ANNOTATION_NAME);
			}
		});
		xmlJavaTypeAdapterAnnotation = (XmlJavaTypeAdapterAnnotation) resourceAttribute.getAnnotation(0, XmlJavaTypeAdapterAnnotation.ANNOTATION_NAME);
		assertNull(xmlElementRefMapping.getXmlJavaTypeAdapter());
		assertNull(xmlJavaTypeAdapterAnnotation);
	}

	protected void addXmlElementRefMemberValuePair(ModifiedDeclaration declaration, String name, String value) {
		this.addMemberValuePair((MarkerAnnotation) this.getXmlElementRefAnnotation(declaration), name, value);
	}

	protected void addXmlElementRefMemberValuePair(ModifiedDeclaration declaration, String name, boolean value) {
		this.addMemberValuePair((MarkerAnnotation) this.getXmlElementRefAnnotation(declaration), name, value);
	}

	protected void addXmlElementRefTypeMemberValuePair(ModifiedDeclaration declaration, String name, String typeName) {
		this.addMemberValuePair(
			(MarkerAnnotation) this.getXmlElementRefAnnotation(declaration), 
			name, 
			this.newTypeLiteral(declaration.getAst(), typeName));
	}

	protected Annotation getXmlElementRefAnnotation(ModifiedDeclaration declaration) {
		return declaration.getAnnotationNamed(XmlElementRefAnnotation.ANNOTATION_NAME);
	}


	public void testModifyXmlElementWrapper() throws Exception {
		createTypeWithXmlElementRef();

		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		JaxbPersistentAttribute persistentAttribute = CollectionTools.get(persistentClass.getAttributes(), 0);
		XmlElementRefMapping xmlElementRefMapping = (XmlElementRefMapping) persistentAttribute.getMapping();
		JavaResourceAttribute resourceAttribute = xmlElementRefMapping.getPersistentAttribute().getJavaResourceAttribute();

		XmlElementWrapperAnnotation xmlElementWrapperAnnotation = (XmlElementWrapperAnnotation) resourceAttribute.getAnnotation(JAXB.XML_ELEMENT_WRAPPER);
		assertNull(xmlElementRefMapping.getXmlElementWrapper());
		assertNull(xmlElementWrapperAnnotation);
		
		xmlElementRefMapping.addXmlElementWrapper();
		xmlElementWrapperAnnotation = (XmlElementWrapperAnnotation) resourceAttribute.getAnnotation(JAXB.XML_ELEMENT_WRAPPER);
		assertNotNull(xmlElementRefMapping.getXmlElementWrapper());
		assertNotNull(xmlElementWrapperAnnotation);
		
		xmlElementRefMapping.removeXmlElementWrapper();
		xmlElementWrapperAnnotation = (XmlElementWrapperAnnotation) resourceAttribute.getAnnotation(JAXB.XML_ELEMENT_WRAPPER);
	}
	
	public void testUpdateXmlElementRefWrapper() throws Exception {
		createTypeWithXmlElementRef();

		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		JaxbPersistentAttribute persistentAttribute = CollectionTools.get(persistentClass.getAttributes(), 0);
		XmlElementRefMapping xmlElementRefMapping = (XmlElementRefMapping) persistentAttribute.getMapping();
		JavaResourceAttribute resourceAttribute = xmlElementRefMapping.getPersistentAttribute().getJavaResourceAttribute();

		XmlElementWrapperAnnotation xmlElementWrapperAnnotation = (XmlElementWrapperAnnotation) resourceAttribute.getAnnotation(JAXB.XML_ELEMENT_WRAPPER);
		assertNull(xmlElementRefMapping.getXmlElementWrapper());
		assertNull(xmlElementWrapperAnnotation);
		
		//add an XmlElementWrapper annotation
		AnnotatedElement annotatedElement = this.annotatedElement(resourceAttribute);
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				GenericJavaXmlElementRefMappingTests.this.addMarkerAnnotation(declaration.getDeclaration(), JAXB.XML_ELEMENT_WRAPPER);
			}
		});
		xmlElementWrapperAnnotation = (XmlElementWrapperAnnotation) resourceAttribute.getAnnotation(JAXB.XML_ELEMENT_WRAPPER);
		assertNotNull(xmlElementRefMapping.getXmlElementWrapper());
		assertNotNull(xmlElementWrapperAnnotation);
		
		//remove the XmlElementWrapper annotation
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				GenericJavaXmlElementRefMappingTests.this.removeAnnotation(declaration, JAXB.XML_ELEMENT_WRAPPER);
			}
		});
		xmlElementWrapperAnnotation = (XmlElementWrapperAnnotation) resourceAttribute.getAnnotation(JAXB.XML_ELEMENT_WRAPPER);
		assertNull(xmlElementRefMapping.getXmlElementWrapper());
		assertNull(xmlElementWrapperAnnotation);
	}
}
