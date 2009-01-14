/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.core.tests.internal.context.persistence;

import org.eclipse.jpt.core.context.persistence.Property;
import org.eclipse.jpt.core.internal.facet.JpaFacetDataModelProperties;
import org.eclipse.jpt.core.internal.facet.JpaFacetDataModelProvider;
import org.eclipse.jpt.core.tests.internal.context.ContextModelTestCase;
import org.eclipse.jpt.eclipselink.core.internal.EclipseLinkJpaPlatform;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.EclipseLinkPersistenceUnit;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.EclipseLinkPersistenceUnitProperties;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.PersistenceUnitProperties;
import org.eclipse.jpt.utility.internal.model.AbstractModel;
import org.eclipse.jpt.utility.internal.model.value.SimplePropertyValueModel;
import org.eclipse.jpt.utility.model.event.ListChangeEvent;
import org.eclipse.jpt.utility.model.event.PropertyChangeEvent;
import org.eclipse.jpt.utility.model.listener.PropertyChangeListener;
import org.eclipse.jpt.utility.model.value.ListValueModel;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

/**
 * PersistenceUnitTestCase
 */
@SuppressWarnings("nls")
public abstract class PersistenceUnitTestCase extends ContextModelTestCase
{
	protected EclipseLinkPersistenceUnit subject;

	protected PropertyValueModel<EclipseLinkPersistenceUnit> subjectHolder;

	protected PropertyChangeEvent propertyChangedEvent;

	protected int propertyChangedEventCount;

	protected int propertiesTotal;

	protected int modelPropertiesSizeOriginal;

	protected int modelPropertiesSize;

	protected PersistenceUnitTestCase(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.subject = this.getPersistenceUnit();
		this.subjectHolder = new SimplePropertyValueModel<EclipseLinkPersistenceUnit>(this.subject);
		this.populatePu();
	}

	@Override
	protected IDataModel buildJpaConfigDataModel() {
		IDataModel dataModel = DataModelFactory.createDataModel(new JpaFacetDataModelProvider());		
		dataModel.setProperty(JpaFacetDataModelProperties.PLATFORM_ID, EclipseLinkJpaPlatform.ID);
		dataModel.setProperty(JpaFacetDataModelProperties.CREATE_ORM_XML, Boolean.FALSE);
		return dataModel;
	}
	
	@Override
	protected EclipseLinkPersistenceUnit getPersistenceUnit() {
		return (EclipseLinkPersistenceUnit) super.getPersistenceUnit();
	}
	
	// ****** abstract methods *******
	protected abstract PersistenceUnitProperties getModel();

	/**
	 * Initializes directly the PU properties before testing. Cannot use
	 * Property Holder to initialize because it is not created yet
	 */
	protected abstract void populatePu();

	/**
	 * Gets the model's property identified by the given propertyName.
	 * 
	 * @param propertyName
	 *            name of property to get
	 * @throws Exception
	 */
	protected abstract Object getProperty(String propertyName) throws Exception;


	/**
	 * Sets the model's property identified by the given propertyName.
	 * Used in verifySetProperty()
	 * 
	 * @param propertyName
	 *            name of property to be set
	 * @param newValue
	 *            value of property
	 * @throws Exception
	 */
	protected abstract void setProperty(String propertyName, Object newValue) throws Exception;

	
	// ****** convenience test methods *******

	protected String getEclipseLinkStringValueOf(Object value) {
		return EclipseLinkPersistenceUnitProperties.getEclipseLinkStringValueOf(value);
	}

	/**
	 * Put into persistenceUnit properties. Do not allows to put duplicate entry.
	 * 
	 * @param key -
	 *            EclipseLink Key
	 * @param value -
	 *            property value
	 */
	protected void persistenceUnitPut(String key, Object value) {
		
		this.persistenceUnitPut( key, value, false);
	}
	
	protected void persistenceUnitPut(String key, Object value, boolean allowDuplicates) {
		if (key == null) {
			throw new IllegalArgumentException("EclipseLink Key cannot be null");
		}
		if (value == null)
			this.putNullProperty(key);
		else
			this.putProperty_(key, value, allowDuplicates);
	}

	private void putProperty_(String elKey, Object value, boolean allowDuplicates) {
		this.clearEvent();
		this.getPersistenceUnit().putProperty(elKey, this.getEclipseLinkStringValueOf(value), allowDuplicates);
	}

	protected void putNullProperty(String elKey) {
		this.clearEvent();
		this.getPersistenceUnit().putProperty(elKey, null, false);
	}

	protected void clearEvent() {
		this.propertyChangedEvent = null;
		this.propertyChangedEventCount = 0;
	}
	
	protected void throwMissingDefinition(String methodName, String propertyName) throws NoSuchFieldException {
		throw new NoSuchFieldException("Missing Definition for: " + methodName + "( " + propertyName + ")");
	}

	protected void throwUnsupportedOperationException(ListChangeEvent e) {
		throw new UnsupportedOperationException(e.getAspectName());
	}

	protected PropertyChangeListener buildPropertyChangeListener() {
		return new PropertyChangeListener() {
			public void propertyChanged(PropertyChangeEvent event) {
				PersistenceUnitTestCase.this.propertyChangedEvent = event;
				PersistenceUnitTestCase.this.propertyChangedEventCount++;
			}
	
			@Override
			public String toString() {
				return "PersistenceUnit listener";
			}
		};
	}
	
	// ****** verify EclipseLink properties *******
	/**
	 * Performs three value tests:<br>
	 * 1. subject value<br>
	 * 2. aspect adapter value<br>
	 * 3. persistenceUnit property value<br>
	 */
	protected void verifyAAValue(Boolean expectedValue, Boolean subjectValue, PropertyValueModel<Boolean> aa, String persistenceXmlKey) {
		assertEquals(expectedValue, subjectValue);
		assertEquals(expectedValue, aa.getValue());
		if (expectedValue != null) {
			assertEquals(expectedValue.toString(), this.getPersistenceUnit().getProperty(persistenceXmlKey).getValue());
		}
	}

	/**
	 * Performs three value tests:<br>
	 * 1. subject value<br>
	 * 2. aspect adapter value<br>
	 * 3. persistenceUnit property value<br>
	 */
	protected <T extends Enum<T>> void verifyAAValue(T expectedValue, T subjectValue, PropertyValueModel<? extends Enum<T>> aa, String elKey) {
		assertEquals(expectedValue, subjectValue);
		assertEquals(expectedValue, aa.getValue());
		if (expectedValue != null) {
			assertEquals(this.getEclipseLinkStringValueOf(expectedValue), this.getPersistenceUnit().getProperty(elKey).getValue());
		}
	}

	/**
	 * Performs the following tests:<br>
	 * 1. verify total number of EclipseLink properties<br>
	 * 2. verify PU has the given propertyName<br>
	 * 3. verify listening to propertyListAdapter<br>
	 * 4. verify that the model can identify propertyName<br>
	 */
	protected void verifyInitialState(String propertyName, String elKey, ListValueModel<Property> propertyListAdapter) throws Exception {
		assertEquals("Total not updated in populatePu(): ", propertyListAdapter.size(), this.propertiesTotal);
		this.verifyPuHasProperty(elKey, "Property not added to populatePu()");
		this.verifyHasListeners(propertyListAdapter);
		this.verifyHasListeners(this.getModel(), propertyName);
		
		Property property = this.getPersistenceUnit().getProperty(elKey);
		assertTrue("model.itemIsProperty() is false: ", getModel().itemIsProperty(property));
		assertEquals("propertyIdFor() not updated: ", propertyName, getModel().propertyIdFor(property));
	}

	/**
	 * Verifies that the persistence unit is populated, and that the model for
	 * the tested Property is initialized with the value from the persistence
	 * unit.
	 * @throws Exception 
	 */
	protected void verifyModelInitialized(String elKey, Object expectedValue) throws Exception {
		Property property = this.getPersistenceUnit().getProperty(elKey);
		assertTrue("model.itemIsProperty() is false: ", getModel().itemIsProperty(property));

		assertEquals("PersistenceUnit not populated - populatedPu()", this.getEclipseLinkStringValueOf(expectedValue), property.getValue());
		String propertyName = this.getModel().propertyIdFor(property);
		Object modelValue = this.getProperty(propertyName);
		assertEquals(
			"Model not initialized - model.initializeProperties() - modelValue = " + modelValue, 
			expectedValue, 
			modelValue);
	}

	/**
	 * Performs the following operations with the property:<br>
	 * 1. verifies the initial state<br>
	 * 2. persistenceUnit putProperty<br>
	 * 3. adapter setProperty<br>
	 */
	protected void verifySetProperty(String elKey, Object testValue1, Object testValue2) throws Exception {
		ListValueModel<Property> propertyListAdapter = this.subject.getPropertyListAdapter();
		Property property = this.getPersistenceUnit().getProperty(elKey);
		String propertyName = this.getModel().propertyIdFor(property);

		// Basic
		this.verifyInitialState(propertyName, elKey, propertyListAdapter);
		
		// Replace
		this.persistenceUnitPut(elKey, testValue2);
		assertEquals(this.propertiesTotal, propertyListAdapter.size());
		this.verifyPutProperty(propertyName, testValue2);
		
		// Replace by setting model object
		this.clearEvent();
		this.setProperty(propertyName, testValue1);
		assertEquals(this.propertiesTotal, propertyListAdapter.size());
		this.verifyPutProperty(propertyName, testValue1);
	}

	/**
	 * Performs the following operations with the property:<br>
	 * 1. performs a remove on the PU<br>
	 * 2. performs a add with putProperty<br>
	 * 3. performs a replace with putProperty<br>
	 */
	protected void verifyAddRemoveProperty(String elKey, Object testValue1, Object testValue2) throws Exception {
		ListValueModel<Property> propertyListAdapter = this.subject.getPropertyListAdapter();
		Property property = this.getPersistenceUnit().getProperty(elKey);
		String propertyName = this.getModel().propertyIdFor(property);

		// Remove
		this.clearEvent();
		--this.propertiesTotal;
		--this.modelPropertiesSize;
		assertTrue("persistenceUnit.properties doesn't contains: " + elKey, this.getPersistenceUnit().containsProperty(elKey));
		this.getPersistenceUnit().removeProperty(elKey);
		assertFalse(this.getPersistenceUnit().containsProperty(elKey));
		assertEquals(this.modelPropertiesSize, this.modelPropertiesSizeOriginal - 1);
		assertEquals(this.propertiesTotal, propertyListAdapter.size());
		this.verifyPutProperty(propertyName, null);
		
		// Add original CacheTypeDefault
		++this.propertiesTotal;
		++this.modelPropertiesSize;
		this.persistenceUnitPut(elKey, testValue1);
		assertEquals(this.propertiesTotal, propertyListAdapter.size());
		this.verifyPutProperty(propertyName, testValue1);
		
		// Replace
		this.persistenceUnitPut(elKey, testValue2);
		assertEquals(this.propertiesTotal, propertyListAdapter.size());
		this.verifyPutProperty(propertyName, testValue2);
	}
	
	/**
	 * Verifies the model's property identified by the given propertyName
	 * Used in verifySetProperty() and verifyAddRemoveProperty
	 * 
	 * @param propertyName
	 *            name of property to be verified
	 * @param expectedValue
	 * @throws Exception
	 */
	protected void verifyPutProperty(String propertyName, Object expectedValue) throws Exception {

		this.verifyPutEvent(propertyName, this.getProperty(propertyName), expectedValue);
	}

	/**
	 * Verifies the event of the put() action.
	 * 
	 * @param propertyName
	 *            name of property to be verified
	 * @param propertyValue
	 *            value of property
	 * @param expectedValue
	 * @throws Exception
	 */
	protected void verifyPutEvent(String propertyName, Object propertyValue, Object expectedValue) {
		
		this.verifyEvent(propertyName);
		this.verifyEventValue(propertyValue, expectedValue);
	}

	/**
	 * Performs the following tests:<br>
	 * 1. verifies the new value of this.propertyChangedEvent<br>
	 * 2. verifies the given value<br>
	 */
	protected void verifyEventValue(Object value, Object expectedValue) {
		// verify event value
		assertEquals(expectedValue, this.propertyChangedEvent.getNewValue());
		assertEquals(expectedValue, value);
	}

	/**
	 * Performs the following tests:<br>
	 * 1. verifies that an event is fired<br>
	 * 2. verifies that it is the correct event<br>
	 * 3. verifies that a single event is fired<br>
	 */
	protected void verifyEvent(String propertyName) {
		// verify event received
		assertNotNull("No Event Fired.", this.propertyChangedEvent);
		// verify event for the expected property
		assertEquals("Wrong Event.", this.propertyChangedEvent.getAspectName(), propertyName);
		// verify event occurence
		assertTrue("No Event Received.", this.propertyChangedEventCount > 0);
		assertTrue("Multiple Event Received (" +  this.propertyChangedEventCount + ")",
			this.propertyChangedEventCount < 2);
	}

	protected void verifyHasNoListeners(ListValueModel listValueModel) throws Exception {
		assertTrue(((AbstractModel) listValueModel).hasNoListChangeListeners(ListValueModel.LIST_VALUES));
	}

	protected void verifyHasListeners(ListValueModel listValueModel) throws Exception {
		assertTrue(((AbstractModel) listValueModel).hasAnyListChangeListeners(ListValueModel.LIST_VALUES));
	}

	protected void verifyHasListeners(PersistenceUnitProperties model, String propertyName) throws Exception {
		assertTrue("Listener not added in setUp() - " + propertyName, ((AbstractModel) model).hasAnyPropertyChangeListeners(propertyName));
	}

	protected void verifyHasListeners(PropertyValueModel pvm, String propertyName) throws Exception {
		assertTrue(((AbstractModel) pvm).hasAnyPropertyChangeListeners(propertyName));
	}

	protected void verifyPuHasProperty(String eclipseLinkPropertyName, String msg) {
		assertNotNull(msg + " - " + eclipseLinkPropertyName, this.getPersistenceUnit().getProperty(eclipseLinkPropertyName));
	}

	protected void verifyPuHasNotProperty(String eclipseLinkPropertyName, String msg) {
		assertNull(msg + " - " + eclipseLinkPropertyName, this.getPersistenceUnit().getProperty(eclipseLinkPropertyName));
	}

	// ****** verify Persistence Unit properties *******

	/**
	 * Performs the following tests:<br>
	 * 1. verify Persistence Unit specified property initialized<br>
	 * 2. set the PU property and verify PU its value<br>
	 * 3. set the PU property to null and verify PU its value<br>
	 * 4. set model value property and verify PU modified<br>
	 * 5. set model value property to null and verify PU modified<br>
	 */
	protected void verifySetPersistenceUnitProperty(String propertyName, Object testValue1, Object testValue2) throws Exception {
		// Basic
		this.verifyHasListeners(this.getModel(), propertyName);
		assertEquals("Persistence Unit not initialized.", testValue1, this.getPersistenceUnitProperty(propertyName));
		Object initialModelValue = this.getProperty(propertyName);
		assertEquals(
			"Model not initialized - model.initializeProperties() - modelValue = " + initialModelValue, 
			testValue1, 
			initialModelValue);
		
		// Modifying value by setting PU
		this.clearEvent();
		this.setPersistenceUnitProperty(propertyName, testValue2);
		this.verifyModelModified(propertyName, testValue2);
		
		// Setting PU to null
		this.clearEvent();
		this.setPersistenceUnitProperty(propertyName, null);
		this.verifyModelModified(propertyName, null);
		
		// Modifying value by setting model object
		this.clearEvent();
		this.setProperty(propertyName, testValue1);
		this.verifyPersistenceUnitModified(propertyName, testValue1);

		// Setting model to null
		this.clearEvent();
		this.setProperty(propertyName, null);
		this.verifyPersistenceUnitModified(propertyName, null);
	}

	protected void verifyPersistenceUnitModified(String propertyName, Object expectedValue) throws Exception {
		assertEquals("Persistence Unit not modified.", expectedValue, this.getPersistenceUnitProperty(propertyName));
		this.verifyPutProperty(propertyName, expectedValue);
	}

	protected void verifyModelModified(String propertyName, Object expectedValue) throws Exception {
		Object modelValue = this.getProperty(propertyName);
		assertEquals("connection value not modified.", expectedValue, modelValue);
		this.verifyPutEvent(propertyName, modelValue, expectedValue);
	}
	
	protected void setPersistenceUnitProperty(String propertyName, Object newValue) throws NoSuchFieldException {
		throw new NoSuchMethodError("Missing implementation for setPersistenceUnitProperty");
	}
	
	protected Object getPersistenceUnitProperty(String propertyName) throws NoSuchFieldException {
		throw new NoSuchMethodError("Missing implementation for getPersistenceUnitProperty");
	}
}
