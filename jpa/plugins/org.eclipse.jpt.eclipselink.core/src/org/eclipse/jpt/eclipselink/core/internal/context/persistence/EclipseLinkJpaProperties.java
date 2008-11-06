/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.context.persistence;

import java.util.ListIterator;

import org.eclipse.jpt.core.JpaProject;
import org.eclipse.jpt.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.core.context.persistence.Property;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.caching.Caching;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.caching.EclipseLinkCaching;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.connection.Connection;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.connection.EclipseLinkConnection;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.customization.Customization;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.customization.EclipseLinkCustomization;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.general.EclipseLinkGeneralProperties;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.general.GeneralProperties;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.logging.EclipseLinkLogging;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.logging.Logging;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.options.EclipseLinkOptions;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.options.Options;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.schema.generation.EclipseLinkSchemaGeneration;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.schema.generation.SchemaGeneration;
import org.eclipse.jpt.utility.internal.model.AbstractModel;
import org.eclipse.jpt.utility.internal.model.value.ItemPropertyListValueModelAdapter;
import org.eclipse.jpt.utility.internal.model.value.ListAspectAdapter;
import org.eclipse.jpt.utility.internal.model.value.SimplePropertyValueModel;
import org.eclipse.jpt.utility.model.event.PropertyChangeEvent;
import org.eclipse.jpt.utility.model.value.ListValueModel;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;

/**
 * EclipseLinkJpaProperties
 */
public class EclipseLinkJpaProperties extends AbstractModel
	implements EclipseLinkProperties
{
	private PersistenceUnit persistenceUnit;
	
	private GeneralProperties generalProperties;
	private Connection connection;
	private Customization customization;
	private Caching caching;
	private Logging logging;
	private Options options;
	private SchemaGeneration schemaGeneration;
	
	private ListValueModel<Property> propertiesAdapter;
	private ListValueModel<Property> propertyListAdapter;

	private static final long serialVersionUID = 1L;
	
	// ********** constructors/initialization **********
	public EclipseLinkJpaProperties(PersistenceUnit parent) {
		super();
		this.initialize(parent);
	}

	protected void initialize(PersistenceUnit parent) {
		this.persistenceUnit = parent;
		PropertyValueModel<PersistenceUnit> persistenceUnitHolder = 
			new SimplePropertyValueModel<PersistenceUnit>(this.persistenceUnit);
		
		this.propertiesAdapter = this.buildPropertiesAdapter(persistenceUnitHolder);
		this.propertyListAdapter = this.buildPropertyListAdapter(this.propertiesAdapter);
		
		this.generalProperties = this.buildGeneralProperties();
		this.connection = this.buildConnection();
		this.customization = this.buildCustomization();
		this.caching = this.buildCaching();
		this.logging = this.buildLogging();
		this.options = this.buildOptions();
		this.schemaGeneration = this.buildSchemaGeneration();
	}

	// ********** internal methods **********

	private ListValueModel<Property> buildPropertyListAdapter(ListValueModel<Property> propertiesAdapter) {
		return new ItemPropertyListValueModelAdapter<Property>(propertiesAdapter, Property.VALUE_PROPERTY);
	}

	private ListValueModel<Property> buildPropertiesAdapter(PropertyValueModel<PersistenceUnit> subjectHolder) {
		return new ListAspectAdapter<PersistenceUnit, Property>(subjectHolder, PersistenceUnit.PROPERTIES_LIST) {
			@Override
			protected ListIterator<Property> listIterator_() {
				return this.subject.properties();
			}

			@Override
			protected int size_() {
				return this.subject.propertiesSize();
			}
		};
	}
	
	private GeneralProperties buildGeneralProperties() {
		return new EclipseLinkGeneralProperties(this.getPersistenceUnit(), this.propertyListAdapter());
	}
	
	private Connection buildConnection() {
		return new EclipseLinkConnection(this.getPersistenceUnit(), this.propertyListAdapter());
	}

	private Customization buildCustomization() {
		return new EclipseLinkCustomization(this.getPersistenceUnit(), this.propertyListAdapter());
	}
	
	private Caching buildCaching() {
		return new EclipseLinkCaching(this.getPersistenceUnit(), this.propertyListAdapter());
	}

	private Logging buildLogging() {
		return new EclipseLinkLogging(this.getPersistenceUnit(), this.propertyListAdapter());
	}

	private Options buildOptions() {
		return new EclipseLinkOptions(this.getPersistenceUnit(), this.propertyListAdapter());
	}

	private SchemaGeneration buildSchemaGeneration() {
		return new EclipseLinkSchemaGeneration(this.getPersistenceUnit(), this.propertyListAdapter());
	}

	// ********** queries **********

	public GeneralProperties getGeneralProperties() {
		return this.generalProperties;
	}

	public Connection getConnection() {
		return this.connection;
	}

	public Customization getCustomization() {
		return this.customization;
	}

	public Caching getCaching() {
		return this.caching;
	}
	
	public Logging getLogging() {
		return this.logging;
	}

	public Options getOptions() {
		return this.options;
	}
	
	public SchemaGeneration getSchemaGeneration() {
		return this.schemaGeneration;
	}

	public PersistenceUnit getPersistenceUnit() {
		return this.persistenceUnit;
	}
	
	public JpaProject getJpaProject() {
		return this.persistenceUnit.getJpaProject();
	}

	public ListValueModel<Property> propertiesAdapter() {
		return this.propertiesAdapter;
	}

	public ListValueModel<Property> propertyListAdapter() {
		return this.propertyListAdapter;
	}

	public boolean itemIsProperty(Property item) {
		throw new UnsupportedOperationException();
	}

	public void propertyChanged(PropertyChangeEvent event) {
		throw new UnsupportedOperationException();
	}

	public String propertyIdFor(Property property) {
		throw new UnsupportedOperationException();
	}
}