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
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAttribute;
import org.eclipse.jpt.common.core.utility.jdt.AnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.Member;
import org.eclipse.jpt.common.core.utility.jdt.ModifiedDeclaration;
import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.common.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.jaxb.core.context.JaxbPersistentClass;
import org.eclipse.jpt.jaxb.core.context.XmlElement;
import org.eclipse.jpt.jaxb.core.context.XmlElementsMapping;
import org.eclipse.jpt.jaxb.core.resource.java.JAXB;
import org.eclipse.jpt.jaxb.core.resource.java.XmlElementAnnotation;
import org.eclipse.jpt.jaxb.core.resource.java.XmlElementWrapperAnnotation;
import org.eclipse.jpt.jaxb.core.resource.java.XmlElementsAnnotation;
import org.eclipse.jpt.jaxb.core.resource.java.XmlIDREFAnnotation;
import org.eclipse.jpt.jaxb.core.resource.java.XmlJavaTypeAdapterAnnotation;
import org.eclipse.jpt.jaxb.core.tests.internal.context.JaxbContextModelTestCase;


public class GenericJavaXmlElementsMappingTests
		extends JaxbContextModelTestCase {
	
	public GenericJavaXmlElementsMappingTests(String name) {
		super(name);
	}
	
	
	private ICompilationUnit createTypeWithXmlElements() throws Exception {
		return this.createTestType(
				new DefaultAnnotationWriter() {
					
					@Override
					public Iterator<String> imports() {
						return new ArrayIterator<String>(JAXB.XML_TYPE, JAXB.XML_ELEMENTS);
					}
					
					@Override
					public void appendTypeAnnotationTo(StringBuilder sb) {
						sb.append("@XmlType");
					}
					
					@Override
					public void appendIdFieldAnnotationTo(StringBuilder sb) {
						sb.append("@XmlElements");
					}
				});
	}
	
	
	// ***** XmlElements *****
	
	protected NormalAnnotation newXmlElementAnnotation(AST ast, String name) {
		NormalAnnotation annotation = newNormalAnnotation(ast, JAXB.XML_ELEMENT);
		addMemberValuePair(annotation, JAXB.XML_ELEMENT__NAME, name);
		return annotation;
	}
	
	protected void addXmlElement(ModifiedDeclaration declaration, int index, String name) {
		NormalAnnotation arrayElement = newXmlElementAnnotation(declaration.getAst(), name);
		addArrayElement(declaration, JAXB.XML_ELEMENTS, index, JAXB.XML_ELEMENTS__VALUE, arrayElement);		
	}
	
	protected void moveXmlElement(ModifiedDeclaration declaration, int targetIndex, int sourceIndex) {
		moveArrayElement((NormalAnnotation) declaration.getAnnotationNamed(JAXB.XML_ELEMENTS), JAXB.XML_ELEMENTS__VALUE, targetIndex, sourceIndex);
	}
	
	protected void removeXmlElement(ModifiedDeclaration declaration, int index) {
		removeArrayElement((NormalAnnotation) declaration.getAnnotationNamed(JAXB.XML_ELEMENTS), JAXB.XML_ELEMENTS__VALUE, index);
	}
	
	public void testSyncXmlElements() throws Exception {
		createTypeWithXmlElements();
		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementsMapping mapping = (XmlElementsMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		JavaResourceAttribute resourceAttribute = mapping.getPersistentAttribute().getJavaResourceAttribute();
		
		Iterable<XmlElement> xmlElements = mapping.getXmlElements();
		assertTrue(CollectionTools.isEmpty(xmlElements));
		assertEquals(0, mapping.getXmlElementsSize());
		
		//add 2 XmlElement annotations
		AnnotatedElement annotatedElement = annotatedElement(resourceAttribute);
		annotatedElement.edit(
				new Member.Editor() {
					
					public void edit(ModifiedDeclaration declaration) {
						GenericJavaXmlElementsMappingTests.this.addXmlElement(declaration, 0, "foo");
						GenericJavaXmlElementsMappingTests.this.addXmlElement(declaration, 1, "bar");
					}
				});
		
		xmlElements = mapping.getXmlElements();
		
		assertFalse(CollectionTools.isEmpty(mapping.getXmlElements()));
		assertEquals(2, mapping.getXmlElementsSize());
		assertEquals("foo", CollectionTools.get(xmlElements, 0).getQName().getName());
		assertEquals("bar", CollectionTools.get(xmlElements, 1).getQName().getName());
		
		// switch XmlElement annotations
		annotatedElement.edit(
				new Member.Editor() {
					
					public void edit(ModifiedDeclaration declaration) {
						GenericJavaXmlElementsMappingTests.this.moveXmlElement(declaration, 0, 1);
					}
				});
		
		xmlElements = mapping.getXmlElements();
		
		assertFalse(CollectionTools.isEmpty(mapping.getXmlElements()));
		assertEquals(2, mapping.getXmlElementsSize());
		assertEquals("bar", CollectionTools.get(xmlElements, 0).getQName().getName());
		assertEquals("foo", CollectionTools.get(xmlElements, 1).getQName().getName());
		
		// remove XmlElement annotations
		annotatedElement.edit(
				new Member.Editor() {
					
					public void edit(ModifiedDeclaration declaration) {
						GenericJavaXmlElementsMappingTests.this.removeXmlElement(declaration, 1);
						GenericJavaXmlElementsMappingTests.this.removeXmlElement(declaration, 0);
					}
				});
		
		xmlElements = mapping.getXmlElements();
		
		assertTrue(CollectionTools.isEmpty(xmlElements));
		assertEquals(0, mapping.getXmlElementsSize());
	}

	public void testModifyXmlElements() throws Exception {
		createTypeWithXmlElements();
		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementsMapping mapping = (XmlElementsMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		JavaResourceAttribute resourceAttribute = mapping.getPersistentAttribute().getJavaResourceAttribute();
		XmlElementsAnnotation xmlElementsAnnotation = (XmlElementsAnnotation) resourceAttribute.getAnnotation(JAXB.XML_ELEMENTS);
		
		Iterable<XmlElementAnnotation> xmlElementAnnotations = xmlElementsAnnotation.getXmlElements();
		
		assertEquals(0, xmlElementsAnnotation.getXmlElementsSize());
		assertEquals(0, mapping.getXmlElementsSize());
		
		mapping.addXmlElement(0).getQName().setSpecifiedName("foo");
		mapping.addXmlElement(1).getQName().setSpecifiedName("baz");
		mapping.addXmlElement(1).getQName().setSpecifiedName("bar");
		
		xmlElementAnnotations = xmlElementsAnnotation.getXmlElements();
		
		assertEquals(3, xmlElementsAnnotation.getXmlElementsSize());
		assertEquals(3, mapping.getXmlElementsSize());
		assertEquals("foo", CollectionTools.get(xmlElementAnnotations, 0).getName());
		assertEquals("bar", CollectionTools.get(xmlElementAnnotations, 1).getName());
		assertEquals("baz", CollectionTools.get(xmlElementAnnotations, 2).getName());
		
		mapping.moveXmlElement(1, 2);
		
		xmlElementAnnotations = xmlElementsAnnotation.getXmlElements();
		
		assertEquals(3, xmlElementsAnnotation.getXmlElementsSize());
		assertEquals(3, mapping.getXmlElementsSize());
		assertEquals("foo", CollectionTools.get(xmlElementAnnotations, 0).getName());
		assertEquals("baz", CollectionTools.get(xmlElementAnnotations, 1).getName());
		assertEquals("bar", CollectionTools.get(xmlElementAnnotations, 2).getName());
		
		mapping.removeXmlElement(2);
		mapping.removeXmlElement(0);
		mapping.removeXmlElement(0);
		
		assertEquals(0, xmlElementsAnnotation.getXmlElementsSize());
		assertEquals(0, mapping.getXmlElementsSize());
	}
	
	public void testModifyXmlJavaTypeAdapter() throws Exception {
		createTypeWithXmlElements();
		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementsMapping mapping = (XmlElementsMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		JavaResourceAttribute resourceAttribute = mapping.getPersistentAttribute().getJavaResourceAttribute();
		
		XmlJavaTypeAdapterAnnotation xmlJavaTypeAdapterAnnotation = (XmlJavaTypeAdapterAnnotation) resourceAttribute.getAnnotation(0, JAXB.XML_JAVA_TYPE_ADAPTER);
		
		assertNull(mapping.getXmlJavaTypeAdapter());
		assertNull(xmlJavaTypeAdapterAnnotation);
		
		mapping.addXmlJavaTypeAdapter();
		xmlJavaTypeAdapterAnnotation = (XmlJavaTypeAdapterAnnotation) resourceAttribute.getAnnotation(0, JAXB.XML_JAVA_TYPE_ADAPTER);
		assertNotNull(mapping.getXmlJavaTypeAdapter());
		assertNotNull(xmlJavaTypeAdapterAnnotation);
		
		mapping.removeXmlJavaTypeAdapter();
		xmlJavaTypeAdapterAnnotation = (XmlJavaTypeAdapterAnnotation) resourceAttribute.getAnnotation(0, JAXB.XML_JAVA_TYPE_ADAPTER);
		
		assertNull(mapping.getXmlJavaTypeAdapter());
		assertNull(xmlJavaTypeAdapterAnnotation);
	}
	
	public void testUpdateXmlJavaTypeAdapter() throws Exception {
		createTypeWithXmlElements();
		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementsMapping mapping = (XmlElementsMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		JavaResourceAttribute resourceAttribute = mapping.getPersistentAttribute().getJavaResourceAttribute();
		
		XmlJavaTypeAdapterAnnotation xmlJavaTypeAdapterAnnotation = (XmlJavaTypeAdapterAnnotation) resourceAttribute.getAnnotation(0, JAXB.XML_JAVA_TYPE_ADAPTER);
		
		assertNull(mapping.getXmlJavaTypeAdapter());
		assertNull(xmlJavaTypeAdapterAnnotation);
		
		AnnotatedElement annotatedElement = annotatedElement(resourceAttribute);
		annotatedElement.edit(
				new Member.Editor() {
					public void edit(ModifiedDeclaration declaration) {
						GenericJavaXmlElementsMappingTests.this.addMarkerAnnotation(declaration.getDeclaration(), JAXB.XML_JAVA_TYPE_ADAPTER);
					}
				});
		xmlJavaTypeAdapterAnnotation = (XmlJavaTypeAdapterAnnotation) resourceAttribute.getAnnotation(0, JAXB.XML_JAVA_TYPE_ADAPTER);
		
		assertNotNull(mapping.getXmlJavaTypeAdapter());
		assertNotNull(xmlJavaTypeAdapterAnnotation);
		
		annotatedElement.edit(
				new Member.Editor() {
					public void edit(ModifiedDeclaration declaration) {
						GenericJavaXmlElementsMappingTests.this.removeAnnotation(declaration, JAXB.XML_JAVA_TYPE_ADAPTER);
					}
				});
		xmlJavaTypeAdapterAnnotation = (XmlJavaTypeAdapterAnnotation) resourceAttribute.getAnnotation(0, JAXB.XML_JAVA_TYPE_ADAPTER);
		
		assertNull(mapping.getXmlJavaTypeAdapter());
		assertNull(xmlJavaTypeAdapterAnnotation);
	}
	
	public void testModifyXmlElementWrapper() throws Exception {
		createTypeWithXmlElements();
		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementsMapping mapping = (XmlElementsMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		JavaResourceAttribute resourceAttribute = mapping.getPersistentAttribute().getJavaResourceAttribute();
		
		XmlElementWrapperAnnotation xmlElementWrapperAnnotation = (XmlElementWrapperAnnotation) resourceAttribute.getAnnotation(JAXB.XML_ELEMENT_WRAPPER);
		
		assertNull(mapping.getXmlElementWrapper());
		assertNull(xmlElementWrapperAnnotation);
		
		mapping.addXmlElementWrapper();
		xmlElementWrapperAnnotation = (XmlElementWrapperAnnotation) resourceAttribute.getAnnotation(JAXB.XML_ELEMENT_WRAPPER);
		
		assertNotNull(mapping.getXmlElementWrapper());
		assertNotNull(xmlElementWrapperAnnotation);
		
		mapping.removeXmlElementWrapper();
		xmlElementWrapperAnnotation = (XmlElementWrapperAnnotation) resourceAttribute.getAnnotation(JAXB.XML_ELEMENT_WRAPPER);
		
		assertNull(mapping.getXmlElementWrapper());
		assertNull(xmlElementWrapperAnnotation);
	}

	public void testUpdateXmlElementWrapper() throws Exception {
		createTypeWithXmlElements();
		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementsMapping mapping = (XmlElementsMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		JavaResourceAttribute resourceAttribute = mapping.getPersistentAttribute().getJavaResourceAttribute();
		
		XmlElementWrapperAnnotation xmlElementWrapperAnnotation = (XmlElementWrapperAnnotation) resourceAttribute.getAnnotation(JAXB.XML_ELEMENT_WRAPPER);
		
		assertNull(mapping.getXmlElementWrapper());
		assertNull(xmlElementWrapperAnnotation);
		
		AnnotatedElement annotatedElement = annotatedElement(resourceAttribute);
		annotatedElement.edit(
				new Member.Editor() {
					public void edit(ModifiedDeclaration declaration) {
						GenericJavaXmlElementsMappingTests.this.addMarkerAnnotation(declaration.getDeclaration(), JAXB.XML_ELEMENT_WRAPPER);
					}
				});
		xmlElementWrapperAnnotation = (XmlElementWrapperAnnotation) resourceAttribute.getAnnotation(JAXB.XML_ELEMENT_WRAPPER);
		
		assertNotNull(mapping.getXmlElementWrapper());
		assertNotNull(xmlElementWrapperAnnotation);
		
		annotatedElement.edit(new Member.Editor() {
			public void edit(
					ModifiedDeclaration declaration) {
						GenericJavaXmlElementsMappingTests.this.removeAnnotation(declaration, JAXB.XML_ELEMENT_WRAPPER);
					}
				});
		xmlElementWrapperAnnotation = (XmlElementWrapperAnnotation) resourceAttribute.getAnnotation(JAXB.XML_ELEMENT_WRAPPER);
		
		assertNull(mapping.getXmlElementWrapper());
		assertNull(xmlElementWrapperAnnotation);
	}

	public void testModifyXmlIDREF() throws Exception {
		createTypeWithXmlElements();
		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementsMapping mapping = (XmlElementsMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		JavaResourceAttribute resourceAttribute = mapping.getPersistentAttribute().getJavaResourceAttribute();
		
		XmlIDREFAnnotation xmlIDREFAnnotation = (XmlIDREFAnnotation) resourceAttribute.getAnnotation(JAXB.XML_IDREF);
		
		assertNull(mapping.getXmlIDREF());
		assertNull(xmlIDREFAnnotation);
		
		mapping.addXmlIDREF();
		xmlIDREFAnnotation = (XmlIDREFAnnotation) resourceAttribute.getAnnotation(JAXB.XML_IDREF);
		
		assertNotNull(mapping.getXmlIDREF());
		assertNotNull(xmlIDREFAnnotation);
		
		mapping.removeXmlIDREF();
		xmlIDREFAnnotation = (XmlIDREFAnnotation) resourceAttribute.getAnnotation(JAXB.XML_IDREF);
		
		assertNull(mapping.getXmlIDREF());
		assertNull(xmlIDREFAnnotation);
	}
	
	public void testUpdateXmlIDREF() throws Exception {
		createTypeWithXmlElements();
		JaxbPersistentClass persistentClass = CollectionTools.get(getContextRoot().getPersistentClasses(), 0);
		XmlElementsMapping mapping = (XmlElementsMapping) CollectionTools.get(persistentClass.getAttributes(), 0).getMapping();
		JavaResourceAttribute resourceAttribute = mapping.getPersistentAttribute().getJavaResourceAttribute();
		
		XmlIDREFAnnotation xmlIDREFAnnotation = (XmlIDREFAnnotation) resourceAttribute.getAnnotation(JAXB.XML_IDREF);
		
		assertNull(mapping.getXmlIDREF());
		assertNull(xmlIDREFAnnotation);
		
		AnnotatedElement annotatedElement = annotatedElement(resourceAttribute);
		annotatedElement.edit(
				new Member.Editor() {
					public void edit(ModifiedDeclaration declaration) {
						GenericJavaXmlElementsMappingTests.this.addMarkerAnnotation(declaration.getDeclaration(), JAXB.XML_IDREF);
					}
				});
		xmlIDREFAnnotation = (XmlIDREFAnnotation) resourceAttribute.getAnnotation(JAXB.XML_IDREF);
		
		assertNotNull(mapping.getXmlIDREF());
		assertNotNull(xmlIDREFAnnotation);
		
		annotatedElement.edit(
				new Member.Editor() {
					public void edit(ModifiedDeclaration declaration) {
						GenericJavaXmlElementsMappingTests.this.removeAnnotation(declaration, JAXB.XML_IDREF);
					}
				});
		xmlIDREFAnnotation = (XmlIDREFAnnotation) resourceAttribute.getAnnotation(JAXB.XML_IDREF);
		
		assertNull(mapping.getXmlIDREF());
		assertNull(xmlIDREFAnnotation);
	}
}
