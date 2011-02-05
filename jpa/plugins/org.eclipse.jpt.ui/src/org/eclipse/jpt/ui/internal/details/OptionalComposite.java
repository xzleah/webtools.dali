/*******************************************************************************
 * Copyright (c) 2006, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.details;

import org.eclipse.jpt.common.ui.internal.JptCommonUiMessages;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.WritablePropertyValueModel;
import org.eclipse.jpt.core.context.OptionalMapping;
import org.eclipse.jpt.ui.internal.JpaHelpContextIds;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;

/**
 * This composite simply shows a tri-state check box for the Optional option.
 *
 * @see BasicMapping
 * @see BasicMappingComposite - A container of this pane
 * @see ManyToOneMappingComposite - A container of this pane
 * @see OneToOneMappingComposite - A container of this pane
 *
 * @version 1.0
 * @since 2.0
 */
public class OptionalComposite extends Pane<OptionalMapping>
{
	/**
	 * Creates a new <code>OptionalComposite</code>.
	 *
	 * @param parentPane The parent container of this one
	 * @param parent The parent container
	 */
	public OptionalComposite(Pane<? extends OptionalMapping> parentPane,
	                         Composite parent)
	{
		super(parentPane, parent);
	}

	@Override
	protected void initializeLayout(Composite container) {

		addTriStateCheckBoxWithDefault(
			addSubPane(container, 4),
			JptUiDetailsMessages.BasicGeneralSection_optionalLabel,
			buildOptionalHolder(),
			buildOptionalStringHolder(),
			JpaHelpContextIds.MAPPING_OPTIONAL
		);
	}
	private WritablePropertyValueModel<Boolean> buildOptionalHolder() {
		return new PropertyAspectAdapter<OptionalMapping, Boolean>(getSubjectHolder(), OptionalMapping.SPECIFIED_OPTIONAL_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getSpecifiedOptional();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setSpecifiedOptional(value);
			}
		};
	}

	private PropertyValueModel<String> buildOptionalStringHolder() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultOptionalHolder()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptCommonUiMessages.Boolean_True : JptCommonUiMessages.Boolean_False;
					return NLS.bind(JptUiDetailsMessages.BasicGeneralSection_optionalLabelDefault, defaultStringValue);
				}
				return JptUiDetailsMessages.BasicGeneralSection_optionalLabel;
			}
		};
	}
	
	
	private PropertyValueModel<Boolean> buildDefaultOptionalHolder() {
		return new PropertyAspectAdapter<OptionalMapping, Boolean>(
			getSubjectHolder(),
			OptionalMapping.SPECIFIED_OPTIONAL_PROPERTY,
			OptionalMapping.DEFAULT_OPTIONAL_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getSpecifiedOptional() != null) {
					return null;
				}
				return Boolean.valueOf(this.subject.isDefaultOptional());
			}
		};
	}
}