/*******************************************************************************
 * Copyright (c) 2009, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.ui.internal.persistence.caching;

import java.util.Collection;
import org.eclipse.jpt.common.ui.JptCommonUiMessages;
import org.eclipse.jpt.common.ui.internal.widgets.EnumFormComboViewer;
import org.eclipse.jpt.common.ui.internal.widgets.IntegerCombo;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.ui.internal.widgets.TriStateCheckBox;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.ModifiablePropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.eclipselink.core.context.persistence.CacheType;
import org.eclipse.jpt.jpa.eclipselink.core.context.persistence.Caching;
import org.eclipse.jpt.jpa.eclipselink.ui.internal.EclipseLinkHelpContextIds;
import org.eclipse.jpt.jpa.eclipselink.ui.internal.JptJpaEclipseLinkUiMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 *  CacheDefaultsComposite
 */
public class CacheDefaultsComposite<T extends Caching> extends Pane<T>
{
	public CacheDefaultsComposite(Pane<T> subjectHolder,
	                                       Composite container) {

		super(subjectHolder, container);
	}

	@Override
	protected Composite addComposite(Composite parent) {
		return this.addTitledGroup(
			parent,
			JptJpaEclipseLinkUiMessages.CACHE_DEFAULTS_COMPOSITE_GROUP_TITLE,
			2,
			null
		);
	}

	@Override
	protected void initializeLayout(Composite parent) {
		// Default Cache Type
		addLabel(parent, JptJpaEclipseLinkUiMessages.PERSISTENCE_XML_CACHING_TAB_DEFAULT_CACHE_TYPE_LABEL);
		buildDefaultCacheTypeCombo(parent);

		// Default Cache Size
		addLabel(parent, JptJpaEclipseLinkUiMessages.DEFAULT_CACHE_SIZE_COMPOSITE_DEFAULT_CACHE_SIZE);
		addDefaultCacheSizeCombo(parent);

		// Default Shared Cache
		TriStateCheckBox sharedCacheCheckBox = this.addTriStateCheckBoxWithDefault(
			parent,
			JptJpaEclipseLinkUiMessages.PERSISTENCE_XML_CACHING_TAB_SHARED_CACHE_DEFAULT_LABEL,
			this.buildDefaultSharedCacheHolder(),
			this.buildDefaultSharedCacheStringHolder(),
			EclipseLinkHelpContextIds.PERSISTENCE_CACHING_DEFAULT_SHARED
		);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		sharedCacheCheckBox.getCheckBox().setLayoutData(gridData);
	}

	protected EnumFormComboViewer<Caching, CacheType> buildDefaultCacheTypeCombo(Composite container) {
		return new EnumFormComboViewer<Caching, CacheType>(this, container) {
			@Override
			protected void addPropertyNames(Collection<String> propertyNames) {
				super.addPropertyNames(propertyNames);
				propertyNames.add(Caching.CACHE_TYPE_DEFAULT_PROPERTY);
			}

			@Override
			protected CacheType[] getChoices() {
				return CacheType.values();
			}
			
			@Override
			protected boolean sortChoices() {
				return false;
			}

			@Override
			protected CacheType getDefaultValue() {
				return getSubject().getDefaultCacheTypeDefault();
			}

			@Override
			protected String displayString(CacheType value) {
				switch (value) {
					case full :
						return JptJpaEclipseLinkUiMessages.CACHE_TYPE_COMPOSITE_FULL;
					case weak :
						return JptJpaEclipseLinkUiMessages.CACHE_TYPE_COMPOSITE_WEAK;
					case soft :
						return JptJpaEclipseLinkUiMessages.CACHE_TYPE_COMPOSITE_SOFT;
					case soft_weak :
						return JptJpaEclipseLinkUiMessages.CACHE_TYPE_COMPOSITE_SOFT_WEAK;
					case hard_weak :
						return JptJpaEclipseLinkUiMessages.CACHE_TYPE_COMPOSITE_HARD_WEAK;
					case none  :
						return JptJpaEclipseLinkUiMessages.CACHE_TYPE_COMPOSITE_NONE;
					default :
						throw new IllegalStateException();
				}

			}

			@Override
			protected CacheType getValue() {
				return getSubject().getCacheTypeDefault();
			}

			@Override
			protected void setValue(CacheType value) {
				getSubject().setCacheTypeDefault(value);
			}

			@Override
			protected String getHelpId() {
				return EclipseLinkHelpContextIds.PERSISTENCE_CACHING_DEFAULT_TYPE;
			}
		};
	}	

	protected void addDefaultCacheSizeCombo(Composite container) {
		new IntegerCombo<Caching>(this, container) {	
			@Override
			protected String getHelpId() {
				return EclipseLinkHelpContextIds.PERSISTENCE_CACHING_DEFAULT_SIZE;
			}

			@Override
			protected PropertyValueModel<Integer> buildDefaultHolder() {
				return new PropertyAspectAdapter<Caching, Integer>(getSubjectHolder()) {
					@Override
					protected Integer buildValue_() {
						return this.subject.getDefaultCacheSizeDefault();
					}
				};
			}

			@Override
			protected ModifiablePropertyValueModel<Integer> buildSelectedItemHolder() {
				return new PropertyAspectAdapter<Caching, Integer>(getSubjectHolder(), Caching.CACHE_SIZE_DEFAULT_PROPERTY) {
					@Override
					protected Integer buildValue_() {
						return this.subject.getCacheSizeDefault();
					}

					@Override
					protected void setValue_(Integer value) {
						this.subject.setCacheSizeDefault(value);
					}
				};
			}
		};
	}
	
	private ModifiablePropertyValueModel<Boolean> buildDefaultSharedCacheHolder() {
		return new PropertyAspectAdapter<Caching, Boolean>(getSubjectHolder(), Caching.SHARED_CACHE_DEFAULT_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getSharedCacheDefault();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setSharedCacheDefault(value);
			}
		};
	}

	private PropertyValueModel<String> buildDefaultSharedCacheStringHolder() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultDefaultSharedCacheHolder()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptCommonUiMessages.BOOLEAN_TRUE : JptCommonUiMessages.BOOLEAN_FALSE;
					return NLS.bind(JptJpaEclipseLinkUiMessages.PERSISTENCE_XML_CACHING_TAB_DEFAULT_SHARED_CACHE_DEFAULT_LABEL, defaultStringValue);
				}
				return JptJpaEclipseLinkUiMessages.PERSISTENCE_XML_CACHING_TAB_SHARED_CACHE_DEFAULT_LABEL;
			}
		};
	}
	private PropertyValueModel<Boolean> buildDefaultDefaultSharedCacheHolder() {
		return new PropertyAspectAdapter<Caching, Boolean>(
			getSubjectHolder(),
			Caching.SHARED_CACHE_DEFAULT_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getSharedCacheDefault() != null) {
					return null;
				}
				return this.subject.getDefaultSharedCacheDefault();
			}
		};
	}
}
