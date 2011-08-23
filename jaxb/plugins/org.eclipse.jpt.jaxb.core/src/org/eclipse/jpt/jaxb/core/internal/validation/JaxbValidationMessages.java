/*******************************************************************************
 * Copyright (c) 2010, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jaxb.core.internal.validation;

@SuppressWarnings("nls")
public interface JaxbValidationMessages {
	
	// bundle name
	String BUNDLE_NAME = "jaxb_validation";
	
	// validation on project
	
	String NO_JAXB_PROJECT = "NO_JAXB_PROJECT"; 
	String PROJECT_INVALID_LIBRARY_PROVIDER = "PROJECT_INVALID_LIBRARY_PROVIDER";
	String PROJECT_UNRESOLVED_SCHEMA = "PROJECT_UNRESOLVED_SCHEMA";
	
	
	// validation on package
	
	String PACKAGE_NO_SCHEMA_FOR_NAMESPACE = "PACKAGE_NO_SCHEMA_FOR_NAMESPACE";
	String PACKAGE_XML_JAVA_TYPE_ADAPTER_TYPE_NOT_SPECIFIED = "PACKAGE_XML_JAVA_TYPE_ADAPTER_TYPE_NOT_SPECIFIED";
	
	String XML_SCHEMA__MISMATCHED_ATTRIBUTE_FORM_DEFAULT = "XML_SCHEMA__MISMATCHED_ATTRIBUTE_FORM_DEFAULT";
	String XML_SCHEMA__MISMATCHED_ELEMENT_FORM_DEFAULT = "XML_SCHEMA__MISMATCHED_ELEMENT_FORM_DEFAULT";
	
	
	// validation on type
	
	String XML_TYPE_UNMATCHING_NAMESPACE_FOR_ANONYMOUS_TYPE = "XML_TYPE_UNMATCHING_NAMESPACE_FOR_ANONYMOUS_TYPE";
	
	String XML_ROOT_ELEMENT_TYPE_CONFLICTS_WITH_XML_TYPE = "XML_ROOT_ELEMENT_TYPE_CONFLICTS_WITH_XML_TYPE";
	
	
	// validation on attribute
	
	String ATTRIBUTE_MAPPING__UNSUPPORTED_ANNOTATION = "ATTRIBUTE_MAPPING__UNSUPPORTED_ANNOTATION";
	String ATTRIBUTE_MAPPING_XML_JAVA_TYPE_ADAPTER_TYPE_NOT_DEFINED = "ATTRIBUTE_MAPPING_XML_JAVA_TYPE_ADAPTER_TYPE_NOT_DEFINED";
	String XML_ELEMENT_WRAPPER_DEFINED_ON_NON_ARRAY_NON_COLLECTION = "XML_ELEMENT_WRAPPER_DEFINED_ON_NON_ARRAY_NON_COLLECTION";
	String XML_ELEMENT__UNSPECIFIED_TYPE = "XML_ELEMENT__UNSPECIFIED_TYPE";
	String XML_ELEMENT__ILLEGAL_TYPE = "XML_ELEMENT__ILLEGAL_TYPE";
	String XML_ELEMENT__UNSPECIFIED_ELEMENT_NAME = "XML_ELEMENT__UNSPECIFIED_ELEMENT_NAME";
	String XML_ELEMENTS__DUPLICATE_XML_ELEMENT_TYPE = "XML_ELEMENTS__DUPLICATE_XML_ELEMENT_TYPE";
	String XML_ELEMENTS__DUPLICATE_XML_ELEMENT_QNAME = "XML_ELEMENTS__DUPLICATE_XML_ELEMENT_QNAME";
	String XML_IDREF__TYPE_DOES_NOT_CONTAIN_XML_ID = "XML_IDREF__TYPE_DOES_NOT_CONTAIN_XML_ID";
	String XML_LIST_DEFINED_ON_NON_ARRAY_NON_COLLECTION = "XML_LIST_DEFINED_ON_NON_ARRAY_NON_COLLECTION";
	String MULTIPLE_XML_ANY_ATTRIBUTE_MAPPINGS_DEFINED = "MULTIPLE_XML_ANY_ATTRIBUTE_MAPPINGS_DEFINED";
	String MULTIPLE_XML_ANY_ELEMENT_MAPPINGS_DEFINED = "MULTIPLE_XML_ANY_ELEMENT_MAPPINGS_DEFINED";
	String MULTIPLE_XML_VALUE_MAPPINGS_DEFINED = "MULTIPLE_XML_VALUE_MAPPINGS_DEFINED";
	
	String XML_VALUE_MAPPING_WITH_NON_XML_ATTRIBUTE_MAPPING_DEFINED = "XML_VALUE_MAPPING_WITH_NON_XML_ATTRIBUTE_MAPPING_DEFINED";
	String XML_ANY_ATTRIBUTE_MAPPING_DEFINED_ON_NON_MAP = "XML_ANY_ATTRIBUTE_MAPPING_DEFINED_ON_NON_MAP";
	String XML_ID_DEFINED_ON_NON_STRING = "XML_ID_DEFINED_ON_NON_STRING";
	String MULTIPLE_XML_IDS_DEFINED = "MULTIPLE_XML_IDS_DEFINED";
	
	
	// general validation
	
	String QNAME__MISSING_NAME = "QNAME__MISSING_NAME";
	String QNAME__UNRESOLVED_COMPONENT = "QNAME__UNRESOLVED_COMPONENT";
}
