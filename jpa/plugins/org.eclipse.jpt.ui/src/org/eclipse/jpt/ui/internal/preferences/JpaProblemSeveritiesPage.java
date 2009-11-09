/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.preferences;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jdt.internal.ui.preferences.PropertyAndPreferencePage;
import org.eclipse.jdt.internal.ui.preferences.ScrolledPageContent;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.internal.validation.JpaValidationMessages;
import org.eclipse.jpt.core.internal.validation.JpaValidationPreferences;
import org.eclipse.jpt.ui.JptUiPlugin;
import org.eclipse.jpt.ui.internal.JptUiMessages;
import org.eclipse.jpt.ui.internal.JptUiValidationPreferenceMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

/**
 * This page shows the Java Persistence validation options. It supports
 * workspace and project levels severities.
 *
 * @version 2.2
 * @since 2.2
 */
@SuppressWarnings({"restriction", "nls"})
public class JpaProblemSeveritiesPage extends PropertyAndPreferencePage {

	/**
	 * The list of <code>Combo</code>s is cached in order to perform a revert of
	 * the properties.
	 */
	private List<Combo> combos;

	/**
	 * The position of the "Error" choice in the combo's model.
	 */
	private final int DEFAULT_INDEX = 0;

	/**
	 * The position of the "Error" choice in the combo's model.
	 */
	private final int ERROR_INDEX = 1;

	/**
	 * The list of <code>ExpandableComposite</code> is cached in order to save
	 * and restore the expansion state.
	 */
	private List<ExpandableComposite> expandablePanes;

	/**
	 * The position of the "Ignore" choice in the combo's model.
	 */
	private final int IGNORE_INDEX = 3;

	/**
	 * The position of the "Info" choice in the combo's model.
	 */
	private final int INFO_INDEX = 4;

	/**
	 * The unique identifier for this page when it is shown in the IDE
	 * preferences dialog.
	 */
	private final String JPT_PREFERENCES_PROBLEM_SEVERITIES_ID = "org.eclipse.jpt.ui.preferences.problemSeverities";

	/**
	 * The unique identifier for this page when it is shown in the project
	 * preferences dialog.
	 */
	private final String JPT_PROPERTY_PAGES_PROBLEM_SEVERITIES_ID = "org.eclipse.jpt.ui.propertyPages.problemSeverities";

	/**
	 * A constant used to store and retrieve the preference key (message ID) from
	 * the combo.
	 */
	private final String PREFERENCE_KEY = "preferenceKey";

	/**
	 * The scrollable pane used to show the content of this page.
	 */
	private ScrolledPageContent scrollable;

	/**
	 * The possible choices which describes the severity of a single problem.
	 */
	private String[] severityDisplayStrings;

	/**
	 * The position of the "Warning" choice in the combo's model.
	 */
	private final int WARNING_INDEX = 2;

	/**
	 * Constant used to store the expansion state of each expandable pane.
	 */
	public static final String SETTINGS_EXPANDED = "expanded";

	/**
	 * The preference key used to retrieve the dialog settings where the expansion
	 * states have been stored.
	 */
	public static final String SETTINGS_SECTION_NAME = "JpaProblemSeveritiesPage";

	/**
	 * Creates a new <code>JpaProblemSeveritiesPage</code>.
	 */
	public JpaProblemSeveritiesPage() {
		super();
		initialize();
	}

	private void addAttributeLevelCategory(Composite parent) {

		parent = addExpandableSection(parent, JptUiValidationPreferenceMessages.ATTRIBUTE_LEVEL_CATEGORY);

		addLabeledCombo(parent, JptUiValidationPreferenceMessages.MAPPING_UNRESOLVED_MAPPED_BY,      JpaValidationMessages.MAPPING_UNRESOLVED_MAPPED_BY);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.MAPPING_INVALID_MAPPED_BY,         JpaValidationMessages.MAPPING_INVALID_MAPPED_BY);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.MAPPING_MAPPED_BY_WITH_JOIN_TABLE, JpaValidationMessages.MAPPING_MAPPED_BY_WITH_JOIN_TABLE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.MAPPING_MAPPED_BY_ON_BOTH_SIDES,   JpaValidationMessages.MAPPING_MAPPED_BY_ON_BOTH_SIDES);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.TARGET_ENTITY_NOT_DEFINED,         JpaValidationMessages.TARGET_ENTITY_NOT_DEFINED);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.TARGET_ENTITY_IS_NOT_AN_ENTITY,    JpaValidationMessages.TARGET_ENTITY_IS_NOT_AN_ENTITY);
	}

	/**
	 * Creates and adds to the given <code>Composite</code> an expandable pane
	 * where its content can be shown or hidden depending on the expansion state.
	 *
	 * @param parent The parent container
	 * @param text The title of the expandable section
	 * @return The container to which widgets can be added, which is a child of
	 * the expandable pane
	 */
	private Composite addExpandableSection(Composite parent, String text) {

		ExpandableComposite expandablePane = new ExpandableComposite(
			parent,
			SWT.NONE,
			ExpandableComposite.TWISTIE | ExpandableComposite.CLIENT_INDENT
		);

		expandablePane.setText(text);
		expandablePane.setExpanded(false);
		expandablePane.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
		expandablePane.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		expandablePane.addExpansionListener(buildExpansionListener());

		scrollable.adaptChild(expandablePane);
		expandablePanes.add(expandablePane);

		parent = new Composite(expandablePane, SWT.NONE);
		parent.setLayout(new GridLayout(2, false));
		expandablePane.setClient(parent);

		return parent;
	}

	private void addImpliedAssociationCategory(Composite parent) {

		parent = addExpandableSection(parent, JptUiValidationPreferenceMessages.IMPLIED_ASSOCIATION_CATEGORY);

		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_UNRESOLVED_TABLE,                  JpaValidationMessages.VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_UNRESOLVED_TABLE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_UNRESOLVED_NAME,                   JpaValidationMessages.VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_UNRESOLVED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_REFERENCED_COLUMN_UNRESOLVED_NAME, JpaValidationMessages.VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_REFERENCED_COLUMN_UNRESOLVED_NAME);
	}

	private void addImpliedAttributeCategory(Composite parent) {

		parent = addExpandableSection(parent, JptUiValidationPreferenceMessages.IMPLIED_ATTRIBUTE_CATEGORY);

		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ATTRIBUTE_JOIN_TABLE_UNRESOLVED_CATALOG,                                       JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_TABLE_UNRESOLVED_CATALOG);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ATTRIBUTE_JOIN_TABLE_UNRESOLVED_SCHEMA,                                        JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_TABLE_UNRESOLVED_SCHEMA);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ATTRIBUTE_JOIN_TABLE_UNRESOLVED_NAME,                                          JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_TABLE_UNRESOLVED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ATTRIBUTE_COLUMN_UNRESOLVED_TABLE,                                             JpaValidationMessages.VIRTUAL_ATTRIBUTE_COLUMN_UNRESOLVED_TABLE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ATTRIBUTE_OVERRIDE_COLUMN_UNRESOLVED_TABLE,                                    JpaValidationMessages.VIRTUAL_ATTRIBUTE_OVERRIDE_COLUMN_UNRESOLVED_TABLE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ATTRIBUTE_COLUMN_UNRESOLVED_NAME,                                              JpaValidationMessages.VIRTUAL_ATTRIBUTE_COLUMN_UNRESOLVED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ATTRIBUTE_OVERRIDE_COLUMN_UNRESOLVED_NAME,                                     JpaValidationMessages.VIRTUAL_ATTRIBUTE_OVERRIDE_COLUMN_UNRESOLVED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_UNRESOLVED_TABLE,                                        JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_UNRESOLVED_TABLE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_UNRESOLVED_NAME,                                         JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_UNRESOLVED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_UNRESOLVED_NAME_MULTIPLE_JOIN_COLUMNS,                   JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_UNRESOLVED_NAME_MULTIPLE_JOIN_COLUMNS);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_REFERENCED_COLUMN_UNRESOLVED_NAME,                       JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_REFERENCED_COLUMN_UNRESOLVED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_REFERENCED_COLUMN_UNRESOLVED_NAME_MULTIPLE_JOIN_COLUMNS, JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_REFERENCED_COLUMN_UNRESOLVED_NAME_MULTIPLE_JOIN_COLUMNS);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ATTRIBUTE_TARGET_ENTITY_IS_NOT_AN_ENTITY,                                      JpaValidationMessages.VIRTUAL_ATTRIBUTE_TARGET_ENTITY_IS_NOT_AN_ENTITY);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.VIRTUAL_ATTRIBUTE_TARGET_ENTITY_NOT_DEFINED,                                           JpaValidationMessages.VIRTUAL_ATTRIBUTE_TARGET_ENTITY_NOT_DEFINED);
	}

	private void addInheritanceStrategoryCategory(Composite parent) {

		parent = addExpandableSection(parent, JptUiValidationPreferenceMessages.INHERITANCE_CATEGORY);

		addLabeledCombo(parent, JptUiValidationPreferenceMessages.DISCRIMINATOR_COLUMN_UNRESOLVED_NAME,             JpaValidationMessages.DISCRIMINATOR_COLUMN_UNRESOLVED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.ENTITY_TABLE_PER_CLASS_NOT_SUPPORTED_ON_PLATFORM, JpaValidationMessages.ENTITY_TABLE_PER_CLASS_NOT_SUPPORTED_ON_PLATFORM);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.ENTITY_TABLE_PER_CLASS_NOT_PORTABLE_ON_PLATFORM,  JpaValidationMessages.ENTITY_TABLE_PER_CLASS_NOT_PORTABLE_ON_PLATFORM);
	}

	/**
	 * Creates and adds to the given parent a labeled combo where the possible
	 * choices are "Ignore", "Error" and "Warning".
	 *
	 * @param parent The parent to which the widgets are added
	 * @param labelText The text labelling the combo
	 * @param preferenceKey The key used to retrieve and to store the value
	 * associated with the validation problem
	 */
	private void addLabeledCombo(Composite parent,
	                             String labelText,
	                             String preferenceKey) {


		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText(labelText);

		Combo combo = new Combo(parent, SWT.READ_ONLY);
		combo.setItems(severityDisplayStrings);
		combo.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		combo.setData(PREFERENCE_KEY, preferenceKey);
		combo.select(convertPreferenceValueToIndex(preferenceKey));
		combo.addSelectionListener(buildComboSelectionListener());

		scrollable.adaptChild(combo);
		combos.add(combo);
	}

	private void addPersistenceLevelCategory(Composite parent) {

		parent = addExpandableSection(parent, JptUiValidationPreferenceMessages.PERSISTENCE_UNIT_LEVEL_CATEGORY);

		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_UNIT_UNSPECIFIED_MAPPING_FILE,                      JpaValidationMessages.PERSISTENCE_UNIT_UNSPECIFIED_MAPPING_FILE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_UNIT_UNSUPPORTED_MAPPING_FILE_CONTENT,              JpaValidationMessages.PERSISTENCE_UNIT_UNSUPPORTED_MAPPING_FILE_CONTENT);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_UNIT_NONEXISTENT_MAPPING_FILE,                      JpaValidationMessages.PERSISTENCE_UNIT_NONEXISTENT_MAPPING_FILE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_UNIT_INVALID_MAPPING_FILE,                          JpaValidationMessages.PERSISTENCE_UNIT_INVALID_MAPPING_FILE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_UNIT_DUPLICATE_MAPPING_FILE,                        JpaValidationMessages.PERSISTENCE_UNIT_DUPLICATE_MAPPING_FILE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_UNIT_UNSPECIFIED_CLASS,                             JpaValidationMessages.PERSISTENCE_UNIT_UNSPECIFIED_CLASS);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_UNIT_NONEXISTENT_CLASS,                             JpaValidationMessages.PERSISTENCE_UNIT_NONEXISTENT_CLASS);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_UNIT_INVALID_CLASS,                                 JpaValidationMessages.PERSISTENCE_UNIT_INVALID_CLASS);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_UNIT_DUPLICATE_CLASS,                               JpaValidationMessages.PERSISTENCE_UNIT_DUPLICATE_CLASS);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_UNIT_REDUNDANT_CLASS,                               JpaValidationMessages.PERSISTENCE_UNIT_REDUNDANT_CLASS);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_UNIT_DUPLICATE_JAR_FILE,                            JpaValidationMessages.PERSISTENCE_UNIT_DUPLICATE_JAR_FILE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_UNIT_UNSPECIFIED_JAR_FILE,                          JpaValidationMessages.PERSISTENCE_UNIT_UNSPECIFIED_JAR_FILE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_UNIT_JAR_FILE_DEPLOYMENT_PATH_WARNING,              JpaValidationMessages.PERSISTENCE_UNIT_JAR_FILE_DEPLOYMENT_PATH_WARNING);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_UNIT_NONEXISTENT_JAR_FILE,                          JpaValidationMessages.PERSISTENCE_UNIT_NONEXISTENT_JAR_FILE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.MAPPING_FILE_EXTRANEOUS_PERSISTENCE_UNIT_DEFAULTS,              JpaValidationMessages.MAPPING_FILE_EXTRANEOUS_PERSISTENCE_UNIT_DEFAULTS);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENT_TYPE_MAPPED_BUT_NOT_INCLUDED_IN_PERSISTENCE_UNIT,    JpaValidationMessages.PERSISTENT_TYPE_MAPPED_BUT_NOT_INCLUDED_IN_PERSISTENCE_UNIT);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENT_TYPE_ANNOTATED_BUT_NOT_INCLUDED_IN_PERSISTENCE_UNIT, JpaValidationMessages.PERSISTENT_TYPE_ANNOTATED_BUT_NOT_INCLUDED_IN_PERSISTENCE_UNIT);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENT_TYPE_UNSPECIFIED_CLASS,                              JpaValidationMessages.PERSISTENT_TYPE_UNSPECIFIED_CLASS);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENT_TYPE_UNRESOLVED_CLASS,                               JpaValidationMessages.PERSISTENT_TYPE_UNRESOLVED_CLASS);
	}

	private void addPhysicalMappingCategory(Composite parent) {

		parent = addExpandableSection(parent, JptUiValidationPreferenceMessages.PHYSICAL_MAPPING_CATEGORY);

		addLabeledCombo(parent, JptUiValidationPreferenceMessages.TABLE_UNRESOLVED_CATALOG,                                            JpaValidationMessages.TABLE_UNRESOLVED_CATALOG);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.TABLE_UNRESOLVED_SCHEMA,                                             JpaValidationMessages.TABLE_UNRESOLVED_SCHEMA);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.TABLE_UNRESOLVED_NAME,                                               JpaValidationMessages.TABLE_UNRESOLVED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.SECONDARY_TABLE_UNRESOLVED_CATALOG,                                  JpaValidationMessages.SECONDARY_TABLE_UNRESOLVED_CATALOG);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.SECONDARY_TABLE_UNRESOLVED_SCHEMA,                                   JpaValidationMessages.SECONDARY_TABLE_UNRESOLVED_SCHEMA);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.SECONDARY_TABLE_UNRESOLVED_NAME,                                     JpaValidationMessages.SECONDARY_TABLE_UNRESOLVED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.JOIN_TABLE_UNRESOLVED_CATALOG,                                       JpaValidationMessages.JOIN_TABLE_UNRESOLVED_CATALOG);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.JOIN_TABLE_UNRESOLVED_SCHEMA,                                        JpaValidationMessages.JOIN_TABLE_UNRESOLVED_SCHEMA);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.JOIN_TABLE_UNRESOLVED_NAME,                                          JpaValidationMessages.JOIN_TABLE_UNRESOLVED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.COLUMN_UNRESOLVED_TABLE,                                             JpaValidationMessages.COLUMN_UNRESOLVED_TABLE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.COLUMN_UNRESOLVED_NAME,                                              JpaValidationMessages.COLUMN_UNRESOLVED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.JOIN_COLUMN_UNRESOLVED_TABLE,                                        JpaValidationMessages.JOIN_COLUMN_UNRESOLVED_TABLE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.JOIN_COLUMN_UNRESOLVED_NAME,                                         JpaValidationMessages.JOIN_COLUMN_UNRESOLVED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.JOIN_COLUMN_UNRESOLVED_NAME_MULTIPLE_JOIN_COLUMNS,                   JpaValidationMessages.JOIN_COLUMN_UNRESOLVED_NAME_MULTIPLE_JOIN_COLUMNS);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.JOIN_COLUMN_REFERENCED_COLUMN_UNRESOLVED_NAME,                       JpaValidationMessages.JOIN_COLUMN_REFERENCED_COLUMN_UNRESOLVED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.JOIN_COLUMN_REFERENCED_COLUMN_UNRESOLVED_NAME_MULTIPLE_JOIN_COLUMNS, JpaValidationMessages.JOIN_COLUMN_REFERENCED_COLUMN_UNRESOLVED_NAME_MULTIPLE_JOIN_COLUMNS);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_NAME,                             JpaValidationMessages.PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME,           JpaValidationMessages.PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME);
	}

	private void addProjectLevelCategory(Composite parent) {

		parent = addExpandableSection(parent, JptUiValidationPreferenceMessages.PROJECT_LEVEL_CATEGORY);

		addLabeledCombo(parent, JptUiValidationPreferenceMessages.NO_JPA_PROJECT,                         JpaValidationMessages.NO_JPA_PROJECT);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PROJECT_NO_CONNECTION,                  JpaValidationMessages.PROJECT_NO_CONNECTION);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PROJECT_INVALID_CONNECTION,             JpaValidationMessages.PROJECT_INVALID_CONNECTION);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PROJECT_INACTIVE_CONNECTION,            JpaValidationMessages.PROJECT_INACTIVE_CONNECTION);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.XML_VERSION_NOT_LATEST,                 JpaValidationMessages.XML_VERSION_NOT_LATEST);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PROJECT_NO_PERSISTENCE_XML,             JpaValidationMessages.PROJECT_NO_PERSISTENCE_XML);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PROJECT_MULTIPLE_PERSISTENCE_XML,       JpaValidationMessages.PROJECT_MULTIPLE_PERSISTENCE_XML);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_NO_PERSISTENCE_UNIT,        JpaValidationMessages.PERSISTENCE_NO_PERSISTENCE_UNIT);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_MULTIPLE_PERSISTENCE_UNITS, JpaValidationMessages.PERSISTENCE_MULTIPLE_PERSISTENCE_UNITS);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENCE_XML_INVALID_CONTENT,        JpaValidationMessages.PERSISTENCE_XML_INVALID_CONTENT);
	}

	private void addQueriesGeneratorsCategory(Composite parent) {

		parent = addExpandableSection(parent, JptUiValidationPreferenceMessages.QUERIES_GENERATORS_CATEGORY);

		addLabeledCombo(parent, JptUiValidationPreferenceMessages.GENERATOR_DUPLICATE_NAME,             JpaValidationMessages.GENERATOR_DUPLICATE_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.ID_MAPPING_UNRESOLVED_GENERATOR_NAME, JpaValidationMessages.ID_MAPPING_UNRESOLVED_GENERATOR_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.GENERATED_VALUE_UNRESOLVED_GENERATOR, JpaValidationMessages.GENERATED_VALUE_UNRESOLVED_GENERATOR);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.QUERY_DUPLICATE_NAME,                 JpaValidationMessages.QUERY_DUPLICATE_NAME);
	}

	private void addTypeLevelCategory(Composite parent) {

		parent = addExpandableSection(parent, JptUiValidationPreferenceMessages.TYPE_LEVEL_CATEGORY);

		addLabeledCombo(parent, JptUiValidationPreferenceMessages.ENTITY_NO_ID,                                            JpaValidationMessages.ENTITY_NO_ID);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.ENTITY_SINGLE_TABLE_DESCENDANT_DEFINES_TABLE,            JpaValidationMessages.ENTITY_SINGLE_TABLE_DESCENDANT_DEFINES_TABLE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.ENTITY_ABSTRACT_TABLE_PER_CLASS_DEFINES_TABLE,           JpaValidationMessages.ENTITY_ABSTRACT_TABLE_PER_CLASS_DEFINES_TABLE);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.ENTITY_ABSTRACT_DISCRIMINATOR_VALUE_DEFINED,             JpaValidationMessages.ENTITY_ABSTRACT_DISCRIMINATOR_VALUE_DEFINED);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.ENTITY_TABLE_PER_CLASS_DISCRIMINATOR_VALUE_DEFINED,      JpaValidationMessages.ENTITY_TABLE_PER_CLASS_DISCRIMINATOR_VALUE_DEFINED);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.ENTITY_NON_ROOT_DISCRIMINATOR_COLUMN_DEFINED,            JpaValidationMessages.ENTITY_NON_ROOT_DISCRIMINATOR_COLUMN_DEFINED);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.ENTITY_TABLE_PER_CLASS_DISCRIMINATOR_COLUMN_DEFINED,     JpaValidationMessages.ENTITY_TABLE_PER_CLASS_DISCRIMINATOR_COLUMN_DEFINED);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENT_ATTRIBUTE_UNSPECIFIED_NAME,                   JpaValidationMessages.PERSISTENT_ATTRIBUTE_UNSPECIFIED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENT_ATTRIBUTE_UNRESOLVED_NAME,                    JpaValidationMessages.PERSISTENT_ATTRIBUTE_UNRESOLVED_NAME);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENT_ATTRIBUTE_INHERITED_ATTRIBUTES_NOT_SUPPORTED, JpaValidationMessages.PERSISTENT_ATTRIBUTE_INHERITED_ATTRIBUTES_NOT_SUPPORTED);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENT_ATTRIBUTE_INVALID_MAPPING,                    JpaValidationMessages.PERSISTENT_ATTRIBUTE_INVALID_MAPPING);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENT_ATTRIBUTE_FINAL_FIELD,                        JpaValidationMessages.PERSISTENT_ATTRIBUTE_FINAL_FIELD);
		addLabeledCombo(parent, JptUiValidationPreferenceMessages.PERSISTENT_ATTRIBUTE_PUBLIC_FIELD,                       JpaValidationMessages.PERSISTENT_ATTRIBUTE_PUBLIC_FIELD);
	}

	private SelectionListener buildComboSelectionListener() {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo combo = (Combo) e.widget;
				String preferenceKey = (String) combo.getData(PREFERENCE_KEY);
				String value = convertToPreferenceValue(combo.getSelectionIndex());
				updatePreference(preferenceKey, value);
			}
		};
	}

	private ExpansionAdapter buildExpansionListener() {
		return new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				JpaProblemSeveritiesPage.this.expansionStateChanged();
			}
		};
	}

	protected String[] buildSeverityDisplayStrings() {
		String[] severities = new String[5];
		severities[DEFAULT_INDEX] = JptUiMessages.JpaProblemSeveritiesPage_Default;
		severities[ERROR_INDEX]   = JptUiMessages.JpaProblemSeveritiesPage_Error;
		severities[WARNING_INDEX] = JptUiMessages.JpaProblemSeveritiesPage_Warning;
		severities[IGNORE_INDEX]  = JptUiMessages.JpaProblemSeveritiesPage_Ignore;
		severities[INFO_INDEX]    = JptUiMessages.JpaProblemSeveritiesPage_Info;
		return severities;
	}

	protected int convertPreferenceValueToIndex(String preferenceKey) {
		String severity = preferenceValue(preferenceKey);

		if (JpaValidationPreferences.HIGH_SEVERITY.equals(severity)) {
			return ERROR_INDEX;
		}

		if (JpaValidationPreferences.NORMAL_SEVERITY.equals(severity)) {
			return WARNING_INDEX;
		}

		if (JpaValidationPreferences.LOW_SEVERITY.equals(severity)) {
			return INFO_INDEX;
		}

		if (JpaValidationPreferences.IGNORE.equals(severity)) {
			return IGNORE_INDEX;
		}

		return DEFAULT_INDEX;
	}

	protected String convertToPreferenceValue(int selectionIndex) {
		switch (selectionIndex) {
			case ERROR_INDEX:   return JpaValidationPreferences.HIGH_SEVERITY;
			case WARNING_INDEX: return JpaValidationPreferences.NORMAL_SEVERITY;
			case INFO_INDEX:    return JpaValidationPreferences.LOW_SEVERITY;
			case IGNORE_INDEX:  return JpaValidationPreferences.IGNORE;
			default:            return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createPreferenceContent(Composite parent) {

		PixelConverter pixelConverter = new PixelConverter(parent);

		// Create a container because the caller will set the GridData and we need
		// to change the heightHint of the first child and we also need to set the
		// font otherwise the layout won't be calculated correctly
		Composite container = new Composite(parent, SWT.NONE);
		container.setFont(parent.getFont());
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth  = 0;
		container.setLayout(layout);

		// Create the main composite of this page
		scrollable = new ScrolledPageContent(container);

		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData.heightHint = pixelConverter.convertHeightInCharsToPixels(20);
		scrollable.setLayoutData(gridData);

		// Update the layout of the ScrolledPageContent's body
		layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth  = 0;

		parent = scrollable.getBody();
		parent.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		parent.setLayout(layout);

		// Add each expandable category
		addProjectLevelCategory(parent);
		addPersistenceLevelCategory(parent);
		addTypeLevelCategory(parent);
		addAttributeLevelCategory(parent);
		addPhysicalMappingCategory(parent);
		addImpliedAttributeCategory(parent);
		addImpliedAssociationCategory(parent);
		addInheritanceStrategoryCategory(parent);
		addQueriesGeneratorsCategory(parent);

		// Restore the expansion states
		restoreSectionExpansionStates(dialogPreferences());

		return container;
	}

	protected IDialogSettings dialogPreferences() {
		IDialogSettings rootSettings = JptUiPlugin.instance().getDialogSettings();
		IDialogSettings settings = rootSettings.getSection(SETTINGS_SECTION_NAME);
		if (settings == null) {
			settings = rootSettings.addNewSection(SETTINGS_SECTION_NAME);
		}
		return settings;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		storeSectionExpansionStates(dialogPreferences());
		super.dispose();
	}

	/**
	 * Revalidates the layout in order to show or hide the vertical scroll bar
	 * after a section was either expanded or collapsed. This unfortunately does
	 * not happen automatically.
	 */
	protected void expansionStateChanged() {
		scrollable.reflow(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getPreferencePageID() {
		return JPT_PREFERENCES_PROBLEM_SEVERITIES_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getPropertyPageID() {
		return JPT_PROPERTY_PAGES_PROBLEM_SEVERITIES_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean hasProjectSpecificOptions(IProject project) {
		IEclipsePreferences projectPreferences = JptCorePlugin.getProjectPreferences(project);

		// Iterate through all the message IDs
		for (Field field : JpaValidationMessages.class.getFields()) {
			try {
				// Retrieve the value
				String value = (String) field.get(null);

				// Skip this one
				if (JpaValidationMessages.BUNDLE_NAME.equals(value)) {
					continue;
				}

				// Check to see if there is a value associated with the message ID
				if (projectPreferences.get(value, null) != null) {
					return true;
				}
			}
			catch (Exception exception) {
				// I think we can safely ignore any problem
			}
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(JptUiPlugin.instance().getPreferenceStore());
		setDescription(JptUiMessages.JpaProblemSeveritiesPage_Description);
	}

	/**
	 * Initializes this page.
	 */
	protected void initialize() {
		combos                 = new ArrayList<Combo>();
		expandablePanes        = new ArrayList<ExpandableComposite>();
		severityDisplayStrings = buildSeverityDisplayStrings();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performDefaults() {
		super.performDefaults();
		revertToDefault();
	}

	protected String preferenceValue(String preferenceKey) {
		if (isProjectPreferencePage()) {
			return JpaValidationPreferences.getProjectLevelProblemPreference(getProject(), preferenceKey);
		}
		return JpaValidationPreferences.getWorkspaceLevelProblemPreference(preferenceKey);
	}

	protected void restoreSectionExpansionStates(IDialogSettings settings) {

		for (int index = expandablePanes.size(); --index >= 0; ) {

			ExpandableComposite expandablePane = expandablePanes.get(index);

			if (settings == null) {
				// Only expand the first node by default
				expandablePane.setExpanded(index == 0);
			}
			else {
				expandablePane.setExpanded(settings.getBoolean(SETTINGS_EXPANDED + index));
			}
		}
	}

	protected void revertToDefault() {

		int defaultValue = DEFAULT_INDEX;

		for (Combo combo : combos) {
			String preferenceKey = (String) combo.getData(PREFERENCE_KEY);
			String value = convertToPreferenceValue(defaultValue);
			updatePreference(preferenceKey, value);
			combo.select(defaultValue);
		}
	}

	protected void storeSectionExpansionStates(IDialogSettings settings) {

		for (int index = expandablePanes.size(); --index >= 0; ) {
			ExpandableComposite expandablePane = expandablePanes.get(index);
			settings.put(SETTINGS_EXPANDED + index, expandablePane.isExpanded());
		}
	}

	protected void updatePreference(String preferenceKey, String value) {
		if (isProjectPreferencePage()) {
			JpaValidationPreferences.setProjectLevelProblemPreference(getProject(), preferenceKey, value);
		}
		else {
			JpaValidationPreferences.setWorkspaceLevelProblemPreference(preferenceKey, value);
		}
	}
}