/*******************************************************************************
 *  Copyright (c) 2012  Oracle. All rights reserved.
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0, which accompanies this distribution
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jaxb.eclipselink.core.tests.internal.context.oxm;

import org.eclipse.jpt.common.core.resource.xml.JptXmlResource;
import org.eclipse.jpt.jaxb.eclipselink.core.context.ELJaxbContextRoot;
import org.eclipse.jpt.jaxb.eclipselink.core.context.ELXmlAccessOrder;
import org.eclipse.jpt.jaxb.eclipselink.core.context.ELXmlAccessType;
import org.eclipse.jpt.jaxb.eclipselink.core.context.oxm.OxmFile;
import org.eclipse.jpt.jaxb.eclipselink.core.context.oxm.OxmXmlBindings;
import org.eclipse.jpt.jaxb.eclipselink.core.resource.oxm.EXmlAccessOrder;
import org.eclipse.jpt.jaxb.eclipselink.core.resource.oxm.EXmlAccessType;
import org.eclipse.jpt.jaxb.eclipselink.core.resource.oxm.EXmlBindings;

public class OxmXmlBindingsTests
		extends OxmContextModelTestCase {
	
	public OxmXmlBindingsTests(String name) {
		super(name);
	}
	
	
	protected void addOxmFile(String fileName, String packageName) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(CR);
		sb.append("<xml-bindings").append(CR);
		sb.append("    version=\"2.4\"").append(CR);
		sb.append("    xmlns=\"http://www.eclipse.org/eclipselink/xsds/persistence/oxm\"").append(CR);
		sb.append("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"").append(CR);
		sb.append("    xsi:schemaLocation=\"http://www.eclipse.org/eclipselink/xsds/persistence/oxm http://www.eclipse.org/eclipselink/xsds/eclipselink_oxm_2_4.xsd\"").append(CR);
		sb.append("    package-name=\"").append(packageName).append("\"").append(CR);
		sb.append("    xml-accessor-type=\"PUBLIC_MEMBER\"").append(CR);
		sb.append("    />").append(CR);
		addOxmFile(fileName, sb);
	}
	
	public void testUpdateAccessType() throws Exception {
		addOxmFile("oxm.xml", "test.oxm");
		ELJaxbContextRoot root = (ELJaxbContextRoot) getJaxbProject().getContextRoot();
		OxmFile oxmFile = root.getOxmFile("test.oxm");
		OxmXmlBindings xmlBindings = oxmFile.getXmlBindings();
		JptXmlResource oxmResource = oxmFile.getOxmResource();
		EXmlBindings eXmlBindings = (EXmlBindings) oxmResource.getRootObject();
		
		// the value is not exactly specified, but since the attribute has a default value, it's "specified"
		assertEquals(EXmlAccessType.PUBLIC_MEMBER, eXmlBindings.getXmlAccessorType());
		assertEquals(ELXmlAccessType.PUBLIC_MEMBER, xmlBindings.getSpecifiedAccessType());
		assertEquals(ELXmlAccessType.PUBLIC_MEMBER, xmlBindings.getAccessType());
		
		eXmlBindings.setXmlAccessorType(EXmlAccessType.FIELD);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-accessor-type=\"FIELD\"", true);
		assertEquals(EXmlAccessType.FIELD, eXmlBindings.getXmlAccessorType());
		assertEquals(ELXmlAccessType.FIELD, xmlBindings.getSpecifiedAccessType());
		assertEquals(ELXmlAccessType.FIELD, xmlBindings.getAccessType());
		
		eXmlBindings.setXmlAccessorType(EXmlAccessType.NONE);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-accessor-type=\"NONE\"", true);
		assertEquals(EXmlAccessType.NONE, eXmlBindings.getXmlAccessorType());
		assertEquals(ELXmlAccessType.NONE, xmlBindings.getSpecifiedAccessType());
		assertEquals(ELXmlAccessType.NONE, xmlBindings.getAccessType());
		
		eXmlBindings.setXmlAccessorType(EXmlAccessType.PROPERTY);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-accessor-type=\"PROPERTY\"", true);
		assertEquals(EXmlAccessType.PROPERTY, eXmlBindings.getXmlAccessorType());
		assertEquals(ELXmlAccessType.PROPERTY, xmlBindings.getSpecifiedAccessType());
		assertEquals(ELXmlAccessType.PROPERTY, xmlBindings.getAccessType());
		
		eXmlBindings.setXmlAccessorType(null);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-accessor-type=", false);
		assertNull(eXmlBindings.getXmlAccessorType());
		assertNull(xmlBindings.getSpecifiedAccessType());
		assertEquals(ELXmlAccessType.PUBLIC_MEMBER, xmlBindings.getAccessType());
	}
	
	public void testModifyAccessType() throws Exception {
		addOxmFile("oxm.xml", "test.oxm");
		ELJaxbContextRoot root = (ELJaxbContextRoot) getJaxbProject().getContextRoot();
		OxmFile oxmFile = root.getOxmFile("test.oxm");
		OxmXmlBindings xmlBindings = oxmFile.getXmlBindings();
		JptXmlResource oxmResource = oxmFile.getOxmResource();
		EXmlBindings eXmlBindings = (EXmlBindings) oxmResource.getRootObject();
		
		// the value is not exactly specified, but since the attribute has a default value, it's "specified"
		assertEquals(EXmlAccessType.PUBLIC_MEMBER, eXmlBindings.getXmlAccessorType());
		assertEquals(ELXmlAccessType.PUBLIC_MEMBER, xmlBindings.getSpecifiedAccessType());
		assertEquals(ELXmlAccessType.PUBLIC_MEMBER, xmlBindings.getAccessType());
		
		xmlBindings.setSpecifiedAccessType(ELXmlAccessType.FIELD);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-accessor-type=\"FIELD\"", true);
		assertEquals(EXmlAccessType.FIELD, eXmlBindings.getXmlAccessorType());
		assertEquals(ELXmlAccessType.FIELD, xmlBindings.getSpecifiedAccessType());
		assertEquals(ELXmlAccessType.FIELD, xmlBindings.getAccessType());
		
		xmlBindings.setSpecifiedAccessType(ELXmlAccessType.NONE);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-accessor-type=\"NONE\"", true);
		assertEquals(EXmlAccessType.NONE, eXmlBindings.getXmlAccessorType());
		assertEquals(ELXmlAccessType.NONE, xmlBindings.getSpecifiedAccessType());
		assertEquals(ELXmlAccessType.NONE, xmlBindings.getAccessType());
		
		xmlBindings.setSpecifiedAccessType(ELXmlAccessType.PROPERTY);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-accessor-type=\"PROPERTY\"", true);
		assertEquals(EXmlAccessType.PROPERTY, eXmlBindings.getXmlAccessorType());
		assertEquals(ELXmlAccessType.PROPERTY, xmlBindings.getSpecifiedAccessType());
		assertEquals(ELXmlAccessType.PROPERTY, xmlBindings.getAccessType());
		
		xmlBindings.setSpecifiedAccessType(null);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-accessor-type=", false);
		assertNull(eXmlBindings.getXmlAccessorType());
		assertNull(xmlBindings.getSpecifiedAccessType());
		assertEquals(ELXmlAccessType.PUBLIC_MEMBER, xmlBindings.getAccessType());
	}
	
	public void testUpdateAccessOrder() throws Exception {
		addOxmFile("oxm.xml", "test.oxm");
		ELJaxbContextRoot root = (ELJaxbContextRoot) getJaxbProject().getContextRoot();
		OxmFile oxmFile = root.getOxmFile("test.oxm");
		OxmXmlBindings xmlBindings = oxmFile.getXmlBindings();
		JptXmlResource oxmResource = oxmFile.getOxmResource();
		EXmlBindings eXmlBindings = (EXmlBindings) oxmResource.getRootObject();
		
		// the value is not exactly specified, but since the attribute has a default value, it's "specified"
		assertEquals(EXmlAccessOrder.UNDEFINED, eXmlBindings.getXmlAccessorOrder());
		assertEquals(ELXmlAccessOrder.UNDEFINED, xmlBindings.getSpecifiedAccessOrder());
		assertEquals(ELXmlAccessOrder.UNDEFINED, xmlBindings.getAccessOrder());
		
		eXmlBindings.setXmlAccessorOrder(EXmlAccessOrder.ALPHABETICAL);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-accessor-order=\"ALPHABETICAL\"", true);
		assertEquals(EXmlAccessOrder.ALPHABETICAL, eXmlBindings.getXmlAccessorOrder());
		assertEquals(ELXmlAccessOrder.ALPHABETICAL, xmlBindings.getSpecifiedAccessOrder());
		assertEquals(ELXmlAccessOrder.ALPHABETICAL, xmlBindings.getAccessOrder());
		
		eXmlBindings.setXmlAccessorOrder(EXmlAccessOrder.UNDEFINED);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-accessor-order=\"UNDEFINED\"", true);
		assertEquals(EXmlAccessOrder.UNDEFINED, eXmlBindings.getXmlAccessorOrder());
		assertEquals(ELXmlAccessOrder.UNDEFINED, xmlBindings.getSpecifiedAccessOrder());
		assertEquals(ELXmlAccessOrder.UNDEFINED, xmlBindings.getAccessOrder());
		
		eXmlBindings.setXmlAccessorOrder(null);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-accessor-order=", false);
		assertNull(eXmlBindings.getXmlAccessorOrder());
		assertNull(xmlBindings.getSpecifiedAccessOrder());
		assertEquals(ELXmlAccessOrder.UNDEFINED, xmlBindings.getAccessOrder());
	}
	
	public void testModifyAccessOrder() throws Exception {
		addOxmFile("oxm.xml", "test.oxm");
		ELJaxbContextRoot root = (ELJaxbContextRoot) getJaxbProject().getContextRoot();
		OxmFile oxmFile = root.getOxmFile("test.oxm");
		OxmXmlBindings xmlBindings = oxmFile.getXmlBindings();
		JptXmlResource oxmResource = oxmFile.getOxmResource();
		EXmlBindings eXmlBindings = (EXmlBindings) oxmResource.getRootObject();
		
		// the value is not exactly specified, but since the attribute has a default value, it's "specified"
		assertEquals(EXmlAccessOrder.UNDEFINED, eXmlBindings.getXmlAccessorOrder());
		assertEquals(ELXmlAccessOrder.UNDEFINED, xmlBindings.getSpecifiedAccessOrder());
		assertEquals(ELXmlAccessOrder.UNDEFINED, xmlBindings.getAccessOrder());
		
		xmlBindings.setSpecifiedAccessOrder(ELXmlAccessOrder.ALPHABETICAL);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-accessor-order=\"ALPHABETICAL\"", true);
		assertEquals(EXmlAccessOrder.ALPHABETICAL, eXmlBindings.getXmlAccessorOrder());
		assertEquals(ELXmlAccessOrder.ALPHABETICAL, xmlBindings.getSpecifiedAccessOrder());
		assertEquals(ELXmlAccessOrder.ALPHABETICAL, xmlBindings.getAccessOrder());
		
		xmlBindings.setSpecifiedAccessOrder(ELXmlAccessOrder.UNDEFINED);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-accessor-order=\"UNDEFINED\"", true);
		assertEquals(EXmlAccessOrder.UNDEFINED, eXmlBindings.getXmlAccessorOrder());
		assertEquals(ELXmlAccessOrder.UNDEFINED, xmlBindings.getSpecifiedAccessOrder());
		assertEquals(ELXmlAccessOrder.UNDEFINED, xmlBindings.getAccessOrder());
		
		xmlBindings.setSpecifiedAccessOrder(null);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-accessor-order=", false);
		assertNull(eXmlBindings.getXmlAccessorOrder());
		assertNull(xmlBindings.getSpecifiedAccessOrder());
		assertEquals(ELXmlAccessOrder.UNDEFINED, xmlBindings.getAccessOrder());
	}
	
	public void testUpdateXmlMappingMetadataComplete() throws Exception {
		addOxmFile("oxm.xml", "test.oxm");
		ELJaxbContextRoot root = (ELJaxbContextRoot) getJaxbProject().getContextRoot();
		OxmFile oxmFile = root.getOxmFile("test.oxm");
		OxmXmlBindings xmlBindings = oxmFile.getXmlBindings();
		JptXmlResource oxmResource = oxmFile.getOxmResource();
		EXmlBindings eXmlBindings = (EXmlBindings) oxmResource.getRootObject();
		
		// the value is not exactly specified, but since the attribute has a default value, it's "specified"
		assertEquals(Boolean.FALSE, eXmlBindings.getXmlMappingMetadataComplete());
		assertFalse(xmlBindings.isXmlMappingMetadataComplete());
		
		eXmlBindings.setXmlMappingMetadataComplete(Boolean.TRUE);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-mapping-metadata-complete=\"true\"", true);
		assertEquals(Boolean.TRUE, eXmlBindings.getXmlMappingMetadataComplete());
		assertTrue(xmlBindings.isXmlMappingMetadataComplete());
		
		eXmlBindings.setXmlMappingMetadataComplete(null);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-mapping-metadata-complete=", false);
		assertNull(eXmlBindings.getXmlMappingMetadataComplete());
		assertFalse(xmlBindings.isXmlMappingMetadataComplete());
	}
	
	public void testModifyXmlMappingMetadataComplete() throws Exception {
		addOxmFile("oxm.xml", "test.oxm");
		ELJaxbContextRoot root = (ELJaxbContextRoot) getJaxbProject().getContextRoot();
		OxmFile oxmFile = root.getOxmFile("test.oxm");
		OxmXmlBindings xmlBindings = oxmFile.getXmlBindings();
		JptXmlResource oxmResource = oxmFile.getOxmResource();
		EXmlBindings eXmlBindings = (EXmlBindings) oxmResource.getRootObject();
		
		// the value is not exactly specified, but since the attribute has a default value, it's "specified"
		assertEquals(Boolean.FALSE, eXmlBindings.getXmlMappingMetadataComplete());
		assertFalse(xmlBindings.isXmlMappingMetadataComplete());
		
		xmlBindings.setXmlMappingMetadataComplete(true);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-mapping-metadata-complete=\"true\"", true);
		assertEquals(Boolean.TRUE, eXmlBindings.getXmlMappingMetadataComplete());
		assertTrue(xmlBindings.isXmlMappingMetadataComplete());
		
		xmlBindings.setXmlMappingMetadataComplete(false);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "xml-mapping-metadata-complete=", false);
		assertNull(eXmlBindings.getXmlMappingMetadataComplete());
		assertFalse(xmlBindings.isXmlMappingMetadataComplete());
	}
	
	public void testUpdatePackageName() throws Exception {
		addOxmFile("oxm.xml", "test.oxm");
		ELJaxbContextRoot root = (ELJaxbContextRoot) getJaxbProject().getContextRoot();
		OxmFile oxmFile = root.getOxmFile("test.oxm");
		OxmXmlBindings xmlBindings = oxmFile.getXmlBindings();
		JptXmlResource oxmResource = oxmFile.getOxmResource();
		EXmlBindings eXmlBindings = (EXmlBindings) oxmResource.getRootObject();
		
		assertEquals("test.oxm", eXmlBindings.getPackageName());
		assertEquals("test.oxm", xmlBindings.getPackageName());
		
		eXmlBindings.setPackageName("foo");
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "package-name=\"foo\"", true);
		assertEquals("foo", eXmlBindings.getPackageName());
		assertEquals("foo", xmlBindings.getPackageName());
		
		eXmlBindings.setPackageName(null);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "package-name=", false);
		assertNull(eXmlBindings.getPackageName());
		assertNull(xmlBindings.getPackageName());
	}
	
	public void testModifyPackageName() throws Exception {
		addOxmFile("oxm.xml", "test.oxm");
		ELJaxbContextRoot root = (ELJaxbContextRoot) getJaxbProject().getContextRoot();
		OxmFile oxmFile = root.getOxmFile("test.oxm");
		OxmXmlBindings xmlBindings = oxmFile.getXmlBindings();
		JptXmlResource oxmResource = oxmFile.getOxmResource();
		EXmlBindings eXmlBindings = (EXmlBindings) oxmResource.getRootObject();
		
		assertEquals("test.oxm", eXmlBindings.getPackageName());
		assertEquals("test.oxm", xmlBindings.getPackageName());
		
		xmlBindings.setPackageName("foo");
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "package-name=\"foo\"", true);
		assertEquals("foo", eXmlBindings.getPackageName());
		assertEquals("foo", xmlBindings.getPackageName());
		
		xmlBindings.setPackageName(null);
		oxmResource.save();
		
		assertFileContentsContains("oxm.xml", "package-name=", false);
		assertNull(eXmlBindings.getPackageName());
		assertNull(xmlBindings.getPackageName());
	}
}