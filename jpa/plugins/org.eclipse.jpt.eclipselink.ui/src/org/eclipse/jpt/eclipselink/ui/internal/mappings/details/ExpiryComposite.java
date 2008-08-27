/*******************************************************************************
 *  Copyright (c) 2008 Oracle. All rights reserved. This
 *  program and the accompanying materials are made available under the terms of
 *  the Eclipse Public License v1.0 which accompanies this distribution, and is
 *  available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors: Oracle. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.mappings.details;

import org.eclipse.jpt.eclipselink.core.context.EclipseLinkCaching;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkExpiryTimeOfDay;
import org.eclipse.jpt.eclipselink.ui.internal.mappings.EclipseLinkUiMappingsMessages;
import org.eclipse.jpt.ui.internal.util.ControlEnabler;
import org.eclipse.jpt.ui.internal.widgets.FormPane;
import org.eclipse.jpt.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.utility.model.value.WritablePropertyValueModel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Spinner;

/**
 * Here is the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * | - Expiry -------------------------------------------------------------- | |
 * | |                                                                       | |
 * | | o No expiry                                                           | |
 * | |                     					----------------                 | |
 * | | o Time to live expiry   Expire after | I          |I| milliseconds    | |
 * | |                                      ----------------                 | |
 * | |                     				    --------------------             | |
 * | | o Daily expiry          Expire at    | HH:MM:SS:AM/PM |I|             | |
 * | |                                      --------------------             | |
 * | ------------------------------------------------------------------------- |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see EclipseLinkCaching
 * @see EclipseLinkExpiryTimeOfDay
 * @see CachingComposite - A container of this widget
 *
 * @version 2.1
 * @since 2.1
 */
public class ExpiryComposite extends FormPane<EclipseLinkCaching> {

	public ExpiryComposite(FormPane<? extends EclipseLinkCaching> parentPane,
	                          Composite parent) {

		super(parentPane, parent);
	}

	@Override
	protected void initializeLayout(Composite container) {
		// Expiry group pane
		Group expiryGroupPane = addTitledGroup(
			container,
			EclipseLinkUiMappingsMessages.ExpiryComposite_expirySection,
			2,
			null
		);
				
		// No Expiry radio button
		Button button = addRadioButton(
			expiryGroupPane,
			EclipseLinkUiMappingsMessages.ExpiryComposite_noExpiry,
			buildNoExpiryHolder(),
			null
		);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		button.setLayoutData(gridData);

		
		// Time To Live Expiry radio button
		addRadioButton(
			expiryGroupPane,
			EclipseLinkUiMappingsMessages.ExpiryComposite_timeToLiveExpiry,
			buildExpiryHolder(),
			null
		);
		
		addTimeToLiveComposite(expiryGroupPane);
		
		// Daily Expiry radio button
		addRadioButton(
			expiryGroupPane,
			EclipseLinkUiMappingsMessages.ExpiryComposite_dailyExpiry,
			buildTimeOfDayExpiryBooleanHolder(),
			null
		);
		
		addTimeOfDayComposite(expiryGroupPane);
	}
	
	protected void addTimeToLiveComposite(Composite parent) {
		Composite container = this.addSubPane(parent, 3, 0, 10, 0, 0);


		Control expireAfterLabel = addUnmanagedLabel(
			container,
			EclipseLinkUiMappingsMessages.ExpiryComposite_timeToLiveExpiryExpireAfter
		);
	
		Spinner expirySpinner = addUnmanagedSpinner(
			container,
			buildTimeToLiveExpiryHolder(),
			-1,
			-1,
			Integer.MAX_VALUE,
			null
		);
		//a border Container is created in FormWidgetFactory.createSpinner
		if (expirySpinner.getParent() != container) {
			((GridData) expirySpinner.getParent().getLayoutData()).grabExcessHorizontalSpace = false;
		}
		else {
			((GridData) expirySpinner.getLayoutData()).grabExcessHorizontalSpace = false;
		}
		
		Control millisecondsLabel =	addUnmanagedLabel(
			container,
			EclipseLinkUiMappingsMessages.ExpiryComposite_timeToLiveExpiryMilliseconds
		);
		
		new ControlEnabler(buildTimeToLiveExpiryEnabler(), expireAfterLabel, expirySpinner, millisecondsLabel);
	}
	
	protected void addTimeOfDayComposite(Composite parent) {
		Composite container = this.addSubPane(parent, 2, 0, 10, 0, 0);


		Control expireAtLabel = addUnmanagedLabel(
			container,
			EclipseLinkUiMappingsMessages.ExpiryComposite_timeOfDayExpiryExpireAt
		);
		
		PropertyValueModel<EclipseLinkExpiryTimeOfDay> timeOfDayExpiryHolder = buildTimeOfDayExpiryHolder();
		DateTime dateTime = addUnmanagedDateTime(
			container, 
			buildTimeOfDayExpiryHourHolder(timeOfDayExpiryHolder), 
			buildTimeOfDayExpiryMinuteHolder(timeOfDayExpiryHolder),
			buildTimeOfDayExpirySecondHolder(timeOfDayExpiryHolder),
			null);

		new ControlEnabler(buildTimeOfDayExpiryEnabler(), expireAtLabel, dateTime);
	}
	
	private WritablePropertyValueModel<Boolean> buildNoExpiryHolder() {
		return new PropertyAspectAdapter<EclipseLinkCaching, Boolean>(
					getSubjectHolder(), 
					EclipseLinkCaching.EXPIRY_PROPERTY, 
					EclipseLinkCaching.EXPIRY_TIME_OF_DAY_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return Boolean.valueOf(this.subject.getExpiry() == null && this.subject.getExpiryTimeOfDay() == null);
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setExpiry(null);
				if (this.subject.getExpiryTimeOfDay() != null) {
					this.subject.removeExpiryTimeOfDay();
				}
			}
		};
	}

	private WritablePropertyValueModel<Boolean> buildExpiryHolder() {
		return new PropertyAspectAdapter<EclipseLinkCaching, Boolean>(
					getSubjectHolder(), 
					EclipseLinkCaching.EXPIRY_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return Boolean.valueOf(this.subject.getExpiry() != null);
			}

			@Override
			protected void setValue_(Boolean value) {
				if (value == Boolean.TRUE) {
					this.subject.setExpiry(0);
				}
			}
		};
	}
	
	private WritablePropertyValueModel<Boolean> buildTimeOfDayExpiryBooleanHolder() {
		return new PropertyAspectAdapter<EclipseLinkCaching, Boolean>(
					getSubjectHolder(), 
					EclipseLinkCaching.EXPIRY_TIME_OF_DAY_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return Boolean.valueOf(this.subject.getExpiryTimeOfDay() != null);
			}

			@Override
			protected void setValue_(Boolean value) {
				if (value == Boolean.TRUE) {
					this.subject.addExpiryTimeOfDay();
				}
			}
		};
	}

	private WritablePropertyValueModel<Integer> buildTimeToLiveExpiryHolder() {
		return new PropertyAspectAdapter<EclipseLinkCaching, Integer>(getSubjectHolder(), EclipseLinkCaching.EXPIRY_PROPERTY) {
			@Override
			protected Integer buildValue_() {
				return this.subject.getExpiry();
			}

			@Override
			protected void setValue_(Integer value) {
				if (value == -1) {
					value = null;
				}
				subject.setExpiry(value);
			}
		};
	}
	
	private PropertyValueModel<Boolean> buildTimeToLiveExpiryEnabler() {
		return new PropertyAspectAdapter<EclipseLinkCaching, Boolean>(getSubjectHolder(), EclipseLinkCaching.EXPIRY_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return Boolean.valueOf(this.subject.getExpiry() != null);
			}
		};
	}
	
	private PropertyValueModel<Boolean> buildTimeOfDayExpiryEnabler() {
		return new PropertyAspectAdapter<EclipseLinkCaching, Boolean>(getSubjectHolder(), EclipseLinkCaching.EXPIRY_TIME_OF_DAY_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return Boolean.valueOf(this.subject.getExpiryTimeOfDay() != null);
			}
		};
	}
	
	private PropertyValueModel<EclipseLinkExpiryTimeOfDay> buildTimeOfDayExpiryHolder() {
		return new PropertyAspectAdapter<EclipseLinkCaching, EclipseLinkExpiryTimeOfDay>(getSubjectHolder(), EclipseLinkCaching.EXPIRY_TIME_OF_DAY_PROPERTY) {
			@Override
			protected EclipseLinkExpiryTimeOfDay buildValue_() {
				return this.subject.getExpiryTimeOfDay();
			}
		};
	}

	private WritablePropertyValueModel<Integer> buildTimeOfDayExpiryHourHolder(PropertyValueModel<EclipseLinkExpiryTimeOfDay> timeOfDayExpiryHolder) {
		return new PropertyAspectAdapter<EclipseLinkExpiryTimeOfDay, Integer>(
					timeOfDayExpiryHolder, 
					EclipseLinkExpiryTimeOfDay.HOUR_PROPERTY) {
			@Override
			protected Integer buildValue_() {
				return this.subject.getHour();
			}

			@Override
			protected void setValue_(Integer hour) {
				this.subject.setHour(hour);
			}
		};
	}

	private WritablePropertyValueModel<Integer> buildTimeOfDayExpiryMinuteHolder(PropertyValueModel<EclipseLinkExpiryTimeOfDay> timeOfDayExpiryHolder) {
		return new PropertyAspectAdapter<EclipseLinkExpiryTimeOfDay, Integer>(
					timeOfDayExpiryHolder, 
					EclipseLinkExpiryTimeOfDay.MINUTE_PROPERTY) {
			@Override
			protected Integer buildValue_() {
				return this.subject.getMinute();
			}

			@Override
			protected void setValue_(Integer minute) {
				this.subject.setMinute(minute);
			}
		};
	}

	private WritablePropertyValueModel<Integer> buildTimeOfDayExpirySecondHolder(PropertyValueModel<EclipseLinkExpiryTimeOfDay> timeOfDayExpiryHolder) {
		return new PropertyAspectAdapter<EclipseLinkExpiryTimeOfDay, Integer>(
					timeOfDayExpiryHolder, 
					EclipseLinkExpiryTimeOfDay.SECOND_PROPERTY) {
			@Override
			protected Integer buildValue_() {
				return this.subject.getSecond();
			}

			@Override
			protected void setValue_(Integer second) {
				this.subject.setSecond(second);
			}
		};
	}

}
