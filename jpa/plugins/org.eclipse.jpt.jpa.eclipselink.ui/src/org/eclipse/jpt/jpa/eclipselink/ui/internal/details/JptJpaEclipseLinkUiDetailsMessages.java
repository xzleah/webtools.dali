/*******************************************************************************
 * Copyright (c) 2008, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.ui.internal.details;

import org.eclipse.osgi.util.NLS;

/**
 * Localized messages used by Dali JPA ElipseLink UI details composites.
 */
public class JptJpaEclipseLinkUiDetailsMessages {

	private static final String BUNDLE_NAME = "jpt_jpa_eclipselink_ui_details"; //$NON-NLS-1$
	private static final Class<?> BUNDLE_CLASS = JptJpaEclipseLinkUiDetailsMessages.class;
	static {
		NLS.initializeMessages(BUNDLE_NAME, BUNDLE_CLASS);
	}

	public static String ECLIPSELINK_BASIC_COLLECTION_MAPPING_UI_PROVIDER_LABEL;
	public static String ECLIPSELINK_BASIC_COLLECTION_MAPPING_UI_PROVIDER_LINK_LABEL;
	
	public static String ECLIPSELINK_BASIC_MAP_MAPPING_UI_PROVIDER_LABEL;
	public static String ECLIPSELINK_BASIC_MAP_MAPPING_UI_PROVIDER_LINK_LABEL;
	
	public static String ECLIPSELINK_TRANSFORMATION_MAPPING_UI_PROVIDER_LABEL;
	public static String ECLIPSELINK_TRANSFORMATION_MAPPING_UI_PROVIDER_LINK_LABEL;

	public static String ECLIPSELINK_VARIABLE_ONE_TO_ONE_MAPPING_UI_PROVIDER_LABEL;
	public static String ECLIPSELINK_VARIABLE_ONE_TO_ONE_MAPPING_UI_PROVIDER_LINK_LABEL;

	public static String ECLIPSELINK_CACHE_TYPE_COMPOSITE_LABEL;
	public static String ECLIPSELINK_CACHE_TYPE_COMPOSITE_FULL;
	public static String ECLIPSELINK_CACHE_TYPE_COMPOSITE_WEAK;
	public static String ECLIPSELINK_CACHE_TYPE_COMPOSITE_SOFT;
	public static String ECLIPSELINK_CACHE_TYPE_COMPOSITE_SOFT_WEAK;
	public static String ECLIPSELINK_CACHE_TYPE_COMPOSITE_HARD_WEAK;
	public static String ECLIPSELINK_CACHE_TYPE_COMPOSITE_CACHE;
	public static String ECLIPSELINK_CACHE_TYPE_COMPOSITE_NONE;
	public static String ECLIPSELINK_TYPE_MAPPING_COMPOSITE_ADVANCED;
	public static String ECLIPSELINK_TYPE_MAPPING_COMPOSITE_CACHING;
	public static String ECLIPSELINK_TYPE_MAPPING_COMPOSITE_CONVERTERS;
	public static String ECLIPSELINK_TYPE_MAPPING_COMPOSITE_MULTITENANCY;
	
	public static String ECLIPSELINK_CACHE_SIZE_COMPOSITE_SIZE;

	public static String ECLIPSELINK_CACHE_COORDINATION_TYPE_COMPOSITE_LABEL;
	public static String ECLIPSELINK_CACHE_COORDINATION_TYPE_COMPOSITE_SEND_OBJECT_CHANGES;
	public static String ECLIPSELINK_CACHE_COORDINATION_TYPE_COMPOSITE_INVALIDATE_CHANGED_OBJECTS;
	public static String ECLIPSELINK_CACHE_COORDINATION_TYPE_COMPOSITE_SEND_NEW_OBJECTS_WITH_CHANGES;
	public static String ECLIPSELINK_CACHE_COORDINATION_TYPE_COMPOSITE_NONE;

	public static String ECLIPSELINK_CACHING_COMPOSITE_SHARED_LABEL_DEFAULT;
	public static String ECLIPSELINK_CACHING_COMPOSITE_SHARED_LABEL;
	public static String ECLIPSELINK_CACHING_COMPOSITE_ADVANCED;
	public static String ECLIPSELINK_ALWAYS_REFRESH_COMPOSITE_ALWAYS_REFRESH_DEFAULT;
	public static String ECLIPSELINK_ALWAYS_REFRESH_COMPOSITE_ALWAYS_REFRESH_LABEL;
	public static String ECLIPSELINK_REFRESH_ONLY_IF_NEWER_COMPOSITE_REFRESH_ONLY_IF_NEWER_DEFAULT;
	public static String ECLIPSELINK_REFRESH_ONLY_IF_NEWER_COMPOSITE_REFRESH_ONLY_IF_NEWER_LABEL;
	public static String ECLIPSELINK_DISABLE_HITS_COMPOSITE_DISABLE_HITS_DEFAULT;
	public static String ECLIPSELINK_DISABLE_HITS_COMPOSITE_DISABLE_HITS_LABEL;
	
	public static String ECLIPSELINK_CHANGE_TRACKING_COMPOSITE_LABEL;
	public static String ECLIPSELINK_CHANGE_TRACKING_COMPOSITE_ATTRIBUTE;
	public static String ECLIPSELINK_CHANGE_TRACKING_COMPOSITE_OBJECT;
	public static String ECLIPSELINK_CHANGE_TRACKING_COMPOSITE_DEFERRED;
	public static String ECLIPSELINK_CHANGE_TRACKING_COMPOSITE_AUTO;
	
	public static String ECLIPSELINK_CONVERT_COMPOSITE_CONVERTER_NAME_LABEL;

	public static String ECLIPSELINK_CONVERTER_COMPOSITE_NAME_TEXT_LABEL;
	public static String ECLIPSELINK_CONVERTER_COMPOSITE_CLASS_LABEL;

	public static String ECLIPSELINK_CONVERTERS_COMPOSITE_CUSTOM_CONVERTER;
	public static String ECLIPSELINK_CONVERTERS_COMPOSITE_OBJECT_TYPE_CONVERTER;
	public static String ECLIPSELINK_CONVERTERS_COMPOSITE_STRUCT_CONVERTER;
	public static String ECLIPSELINK_CONVERTERS_COMPOSITE_TYPE_CONVERTER;
	public static String ECLIPSELINK_CONVERTERS_COMPOSITE_MAX_CONVERTERS_ERROR_MESSAGE;
	
	public static String ECLIPSELINK_CUSTOMIZER_COMPOSITE_CLASS_LABEL;
		
	public static String TYPE_SECTION_CONVERTED;
	
	public static String ECLIPSELINK_EXISTENCE_CHECKING_COMPOSITE_LABEL;
	public static String ECLIPSELINK_EXISTENCE_CHECKING_COMPOSITE_CHECK_CACHE;
	public static String ECLIPSELINK_EXISTENCE_CHECKING_COMPOSITE_CHECK_DATABASE;
	public static String ECLIPSELINK_EXISTENCE_CHECKING_COMPOSITE_ASSUME_EXISTENCE;
	public static String ECLIPSELINK_EXISTENCE_CHECKING_COMPOSITE_ASSUME_NON_EXISTENCE;
	
	public static String ECLIPSELINK_EXPIRY_COMPOSITE_EXPIRY_SECTION;
	public static String ECLIPSELINK_EXPIRY_COMPOSITE_NO_EXPIRY;
	public static String ECLIPSELINK_EXPIRY_COMPOSITE_TIME_TO_LIVE_EXPIRY;
	public static String ECLIPSELINK_EXPIRY_COMPOSITE_TIME_TO_LIVE_EXPIRY_EXPIRE_AFTER;
	public static String ECLIPSELINK_EXPIRY_COMPOSITE_TIME_TO_LIVE_EXPIRY_MILLISECONDS;
	public static String ECLIPSELINK_EXPIRY_COMPOSITE_DAILY_EXPIRY;
	public static String ECLIPSELINK_EXPIRY_COMPOSITE_TIME_OF_DAY_EXPIRY_EXPIRE_AT;
	
	public static String ECLIPSELINK_JOIN_FETCHCOMPOSITE_LABEL;
	public static String ECLIPSELINK_JOIN_FETCH_COMPOSITE_INNER;
	public static String ECLIPSELINK_JOIN_FETCH_COMPOSITE_OUTER;	
	
	public static String ECLIPSELINK_MUTABLE_COMPOSITE_MUTABLE_LABEL;
	public static String ECLIPSELINK_MUTABLE_COMPOSITE_MUTABLE_LABEL_DEFAULT;
	public static String ECLIPSELINK_MUTABLE_COMPOSITE_TRUE;
	public static String ECLIPSELINK_MUTABLE_COMPOSITE_FALSE;
	
	public static String ECLIPSELINK_OBJECT_TYPE_CONVERTER_COMPOSITE_DATA_TYPE_LABEL;
	public static String ECLIPSELINK_OBJECT_TYPE_CONVERTER_COMPOSITE_OBJECT_TYPE_LABEL;
	public static String ECLIPSELINK_OBJECT_TYPE_CONVERTER_COMPOSITE_CONVERSION_VALUE_EDIT;
	public static String ECLIPSELINK_OBJECT_TYPE_CONVERTER_COMPOSITE_CONVERSION_VALUES_DATA_VALUE_COLUMN;
	public static String ECLIPSELINK_OBJECT_TYPE_CONVERTER_COMPOSITE_CONVERSION_VALUES_OBJECT_VALUE_COLUMN;
	public static String ECLIPSELINK_OBJECT_TYPE_CONVERTER_COMPOSITE_DEFAULT_OBJECT_VALUE_LABEL;
	public static String ECLIPSELINK_OBJECT_TYPE_CONVERTER_COMPOSITE_CONVERSION_VALUES_GROUP_TITLE;
	
	public static String ECLIPSELINK_CONVERSION_VALUE_DIALOG_ADD_CONVERSION_VALUE;
	public static String ECLIPSELINK_CONVERSION_VALUE_DIALOG_EDIT_CONVERSION_VALUE;
	public static String ECLIPSELINK_CONVERSION_VALUE_DIALOG_ADD_CONVERSION_VALUE_DESCRIPTION_TITLE;
	public static String ECLIPSELINK_CONVERSION_VALUE_DIALOG_EDIT_CONVERSION_VALUE_DESCRIPTION_TITLE;
	public static String ECLIPSELINK_CONVERSION_VALUE_DIALOG_ADD_CONVERSION_VALUE_DESCRIPTION;
	public static String ECLIPSELINK_CONVERSION_VALUE_DIALOG_EDIT_CONVERSION_VALUE_DESCRIPTION;
	public static String ECLIPSELINK_CONVERSION_VALUE_DIALOG_DATA_VALUE;
	public static String ECLIPSELINK_CONVERSION_VALUE_DIALOG_OBJECT_VALUE;

	public static String ECLIPSELINK_CONVERSION_VALUE_STATE_OBJECT_DATA_VALUE_MUST_BE_SPECIFIED;
	public static String ECLIPSELINK_CONVERSION_VALUE_STATE_OBJECT_OBJECT_VALUE_MUST_BE_SPECIFIED;
	public static String ECLIPSELINK_CONVERSION_VALUE_STATE_OBJECT_DATA_VALUE_ALREADY_EXISTS;
	
	public static String ECLIPSELINK_PRIVATE_OWNED_COMPOSITE_PRIVATE_OWNED_LABEL;
	
	public static String ECLIPSELINK_READ_ONLY_COMPOSITE_READ_ONLY_LABEL;
	public static String ECLIPSELINK_READ_ONLY_COMPOSITE_READ_ONLY_WITH_DEFAULT;
	
	public static String ECLIPSELINK_TYPE_CONVERTER_COMPOSITE_DATA_TYPE_LABEL;
	public static String ECLIPSELINK_TYPE_CONVERTER_COMPOSITE_OBJECT_TYPE_LABEL;

	public static String DEFAULT_ECLIPSELINK_ONE_TO_ONE_MAPPING_UI_PROVIDER_LABEL;
	public static String DEFAULT_ECLIPSELINK_ONE_TO_ONE_MAPPING_UI_PROVIDER_LINK_LABEL;
	public static String DEFAULT_ECLIPSELINK_VARIABLE_ONE_TO_ONE_MAPPING_UI_PROVIDER_LABEL;
	public static String DEFAULT_ECLIPSELINK_VARIABLE_ONE_TO_ONE_MAPPING_UI_PROVIDER_LINK_LABEL;
	public static String DEFAULT_ECLIPSELINK_ONE_TO_MANY_MAPPING_UI_PROVIDER_LABEL;
	public static String DEFAULT_ECLIPSELINK_ONE_TO_MANY_MAPPING_UI_PROVIDER_LINK_LABEL;
	
	public static String ECLIPSELINK_CONVERTER_DIALOG_NAME;
	public static String ECLIPSELINK_CONVERTER_DIALOG_CONVERTER_TYPE;
	public static String ECLIPSELINK_CONVERTER_DIALOG_ADD_CONVERTER;
	public static String ECLIPSELINK_CONVERTER_DIALOG_ADD_CONVERTER_DESCRIPTION_TITLE;
	public static String ECLIPSELINK_CONVERTER_DIALOG_ADD_CONVERTER_DESCRIPTION;

	public static String ECLIPSELINK_CONVERTER_STATE_OBJECT_NAME_EXISTS;
	public static String ECLIPSELINK_CONVERTER_STATE_OBJECT_NAME_IS_RESERVED;
	public static String ECLIPSELINK_CONVERTER_STATE_OBJECT_NAME_MUST_BE_SPECIFIED;
	public static String ECLIPSELINK_CONVERTER_STATE_OBJECT_TYPE_MUST_BE_SPECIFIED;

	public static String ECLIPSELINK_MAPPED_SUPERCLASS_COMPOSITE_QUERIES;

	public static String TENANT_DISCRIMINATOR_COLUMNS_GROUP_LABEL;
	public static String TENANT_DISCRIMINATOR_COLUMN_COMPOSITE_NAME_LABEL;
	public static String TENANT_DISCRIMINATOR_COLUMN_COMPOSITE_DEFAULT_TENANT_DISCRIMINATOR_COLUMN_NAME_LABEL;
	public static String TENANT_DISCRIMINATOR_COLUMN_COMPOSITE_TABLE_LABEL;
	public static String TENANT_DISCRIMINATOR_COLUMN_COMPOSITE_CONTEXT_PROPERTY_LABEL;
	public static String TENANT_DISCRIMINATOR_COLUMN_COMPOSITE_DISCRIMINATOR_TYPE_LABEL;
	public static String TENANT_DISCRIMINATOR_COLUMN_COMPOSITE_LENGTH_LABEL;
	public static String TENANT_DISCRIMINATOR_COLUMN_COMPOSITE_COLUMN_DEFINITION_LABEL;
	public static String TENANT_DISCRIMINATOR_COLUMN_COMPOSITE_PRIMARY_KEY_WITH_DEFAULT;
	public static String TENANT_DISCRIMINATOR_COLUMN_COMPOSITE_PRIMARY_KEY;
	
	public static String ECLIPSELINK_ENTITY_MAPPINGS_TENANT_DISCRIMINATOR_COLUMNS_COMPOSITE_COLLAPSIBLE_SECTION;
	public static String ECLIPSELINK_MULTITENANCY_OVERRIDE_DEFAULT_TENANT_DISCRIMINATOR_COLUMNS;

	public static String ECLIPSELINK_MULTITENANCY_COMPOSITE_STRATEGY;
	public static String ECLIPSELINK_MULTITENANCY_COMPOSITE_SINGLE_TABLE;
	public static String ECLIPSELINK_MULTITENANCY_COMPOSITE_TABLE_PER_TENANT;
	public static String ECLIPSELINK_MULTITENANCY_COMPOSITE_VPD;
	public static String ECLIPSELINK_MULTITENANCY_COMPOSITE_INCLUDE_CRITERIA;
	public static String ECLIPSELINK_MULTITENANCY_COMPOSITE_INCLUDE_CRITERIA_WITH_DEFAULT;

	public static String ORM_ATTRIBUTE_TYPE_COMPOSITE_ATTRIBUTE_TYPE;

	private JptJpaEclipseLinkUiDetailsMessages() {
		throw new UnsupportedOperationException();
	}
}
