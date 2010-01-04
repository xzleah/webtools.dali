/*******************************************************************************
 * Copyright (c) 2008, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.details;

import org.eclipse.jpt.core.context.BasicMapping;
import org.eclipse.jpt.core.context.Column;
import org.eclipse.jpt.core.context.Converter;
import org.eclipse.jpt.core.context.ConvertibleMapping;
import org.eclipse.jpt.core.context.EnumeratedConverter;
import org.eclipse.jpt.core.context.TemporalConverter;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkConvert;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkBasicMapping;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkMutable;
import org.eclipse.jpt.ui.WidgetFactory;
import org.eclipse.jpt.ui.details.JpaComposite;
import org.eclipse.jpt.ui.internal.details.ColumnComposite;
import org.eclipse.jpt.ui.internal.details.EnumTypeComposite;
import org.eclipse.jpt.ui.internal.details.FetchTypeComposite;
import org.eclipse.jpt.ui.internal.details.JptUiDetailsMessages;
import org.eclipse.jpt.ui.internal.details.OptionalComposite;
import org.eclipse.jpt.ui.internal.details.TemporalTypeComposite;
import org.eclipse.jpt.ui.internal.widgets.FormPane;
import org.eclipse.jpt.ui.internal.widgets.Pane;
import org.eclipse.jpt.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.utility.model.value.WritablePropertyValueModel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * Here the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | ColumnComposite                                                       | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | FetchTypeComposite                                                    | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | TemporalTypeComposite                                                 | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | EnumTypeComposite                                                     | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | OptionalComposite                                                     | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | MutableComposite                                                      | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | LobComposite                                                          | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see BasicMapping
 * @see ColumnComposite
 * @see EnumTypeComposite
 * @see FetchTypeComposite
 * @see LobComposite
 * @see OptionalComposite
 * @see TemporalTypeComposite
 *
 * @version 2.1
 * @since 2.1
 */
public class EclipseLinkBasicMappingComposite extends FormPane<BasicMapping>
                                   implements JpaComposite
{
	/**
	 * Creates a new <code>BasicMappingComposite</code>.
	 *
	 * @param subjectHolder The holder of the subject <code>IBasicMapping</code>
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create various common widgets
	 */
	public EclipseLinkBasicMappingComposite(PropertyValueModel<? extends BasicMapping> subjectHolder,
	                             Composite parent,
	                             WidgetFactory widgetFactory) {

		super(subjectHolder, parent, widgetFactory);
	}

	@Override
	protected void initializeLayout(Composite container) {
		initializeGeneralPane(container);
		initializeTypePane(container);
	}
	
	protected void initializeGeneralPane(Composite container) {
		int groupBoxMargin = getGroupBoxMargin();

		new ColumnComposite(this, buildColumnHolder(), container);

		// Align the widgets under the ColumnComposite
		container = addSubPane(container, 0, groupBoxMargin, 0, groupBoxMargin);

		new FetchTypeComposite(this, container);
		new OptionalComposite(this, addSubPane(container, 4));
		
		// Mutable widgets
		new EclipseLinkMutableComposite(this, buildMutableHolder(), container);
	}
	
	protected void initializeTypePane(Composite container) {

		container = addCollapsibleSection(
			container,
			JptUiDetailsMessages.TypeSection_type
		);
		((GridLayout) container.getLayout()).numColumns = 2;

		// No converter
		Button noConverterButton = addRadioButton(
			container, 
			JptUiDetailsMessages.TypeSection_default, 
			buildNoConverterHolder(), 
			null);
		((GridData) noConverterButton.getLayoutData()).horizontalSpan = 2;
		
		// Lob
		Button lobButton = addRadioButton(
			container, 
			JptUiDetailsMessages.TypeSection_lob, 
			buildConverterBooleanHolder(Converter.LOB_CONVERTER), 
			null);
		((GridData) lobButton.getLayoutData()).horizontalSpan = 2;
		
		PropertyValueModel<Converter> converterHolder = buildConverterHolder();
		// Temporal
		addRadioButton(
			container, 
			JptUiDetailsMessages.TypeSection_temporal, 
			buildConverterBooleanHolder(Converter.TEMPORAL_CONVERTER), 
			null);
		registerSubPane(new TemporalTypeComposite(buildTemporalConverterHolder(converterHolder), container, getWidgetFactory()));
		
		
		// Enumerated
		addRadioButton(
			container, 
			JptUiDetailsMessages.TypeSection_enumerated, 
			buildConverterBooleanHolder(Converter.ENUMERATED_CONVERTER), 
			null);
		registerSubPane(new EnumTypeComposite(buildEnumeratedConverterHolder(converterHolder), container, getWidgetFactory()));

		// EclipseLink Converter
		Button elConverterButton = addRadioButton(
			container, 
			EclipseLinkUiDetailsMessages.TypeSection_converted, 
			buildConverterBooleanHolder(EclipseLinkConvert.ECLIPSE_LINK_CONVERTER), 
			null);
		((GridData) elConverterButton.getLayoutData()).horizontalSpan = 2;

		Pane<EclipseLinkConvert> convertComposite = buildConvertComposite(buildEclipseLinkConverterHolder(converterHolder), container);
		GridData gridData = (GridData) convertComposite.getControl().getLayoutData();
		gridData.horizontalSpan = 2;
		gridData.horizontalIndent = 20;
		registerSubPane(convertComposite);
	}

	protected Pane<EclipseLinkConvert> buildConvertComposite(PropertyValueModel<EclipseLinkConvert> convertHolder, Composite container) {
		return new EclipseLinkConvertComposite(convertHolder, container, getWidgetFactory());
	}
	
	protected PropertyValueModel<Column> buildColumnHolder() {
		return new TransformationPropertyValueModel<BasicMapping, Column>(getSubjectHolder()) {
			@Override
			protected Column transform_(BasicMapping value) {
				return value.getColumn();
			}
		};
	}
	
	protected PropertyValueModel<EclipseLinkMutable> buildMutableHolder() {
		return new PropertyAspectAdapter<BasicMapping, EclipseLinkMutable>(getSubjectHolder()) {
			@Override
			protected EclipseLinkMutable buildValue_() {
				return ((EclipseLinkBasicMapping) this.subject).getMutable();
			}
		};
	}
	
	protected PropertyValueModel<Converter> buildConverterHolder() {
		return new PropertyAspectAdapter<BasicMapping, Converter>(getSubjectHolder(), ConvertibleMapping.CONVERTER_PROPERTY) {
			@Override
			protected Converter buildValue_() {
				return this.subject.getConverter();
			}
		};
	}
	
	protected PropertyValueModel<TemporalConverter> buildTemporalConverterHolder(PropertyValueModel<Converter> converterHolder) {
		return new TransformationPropertyValueModel<Converter, TemporalConverter>(converterHolder) {
			@Override
			protected TemporalConverter transform_(Converter converter) {
				return converter.getType() == Converter.TEMPORAL_CONVERTER ? (TemporalConverter) converter : null;
			}
		};
	}
	
	protected PropertyValueModel<EnumeratedConverter> buildEnumeratedConverterHolder(PropertyValueModel<Converter> converterHolder) {
		return new TransformationPropertyValueModel<Converter, EnumeratedConverter>(converterHolder) {
			@Override
			protected EnumeratedConverter transform_(Converter converter) {
				return converter.getType() == Converter.ENUMERATED_CONVERTER ? (EnumeratedConverter) converter : null;
			}
		};
	}
	
	protected PropertyValueModel<EclipseLinkConvert> buildEclipseLinkConverterHolder(PropertyValueModel<Converter> converterHolder) {
		return new TransformationPropertyValueModel<Converter, EclipseLinkConvert>(converterHolder) {
			@Override
			protected EclipseLinkConvert transform_(Converter converter) {
				return converter.getType() == EclipseLinkConvert.ECLIPSE_LINK_CONVERTER ? (EclipseLinkConvert) converter : null;
			}
		};
	}
	
	protected WritablePropertyValueModel<Boolean> buildNoConverterHolder() {
		return new PropertyAspectAdapter<BasicMapping, Boolean>(getSubjectHolder(), ConvertibleMapping.CONVERTER_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return Boolean.valueOf(this.subject.getConverter().getType() == Converter.NO_CONVERTER);
			}

			@Override
			protected void setValue_(Boolean value) {
				if (value.booleanValue()) {
					this.subject.setConverter(Converter.NO_CONVERTER);
				}
			}
		};
	}
	
	protected WritablePropertyValueModel<Boolean> buildConverterBooleanHolder(final String converterType) {
		return new PropertyAspectAdapter<BasicMapping, Boolean>(getSubjectHolder(), ConvertibleMapping.CONVERTER_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				Converter converter = this.subject.getConverter();
				return Boolean.valueOf(converter.getType() == converterType);
			}

			@Override
			protected void setValue_(Boolean value) {
				if (value.booleanValue()) {
					this.subject.setConverter(converterType);
				}
			}
		};
	}
}