/*******************************************************************************
 * Copyright (c) 2008, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.ui.internal.persistence.schema.generation;

import java.util.Collection;
import org.eclipse.jpt.common.ui.internal.widgets.EnumFormComboViewer;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.jpa.eclipselink.core.context.persistence.OutputMode;
import org.eclipse.jpt.jpa.eclipselink.core.context.persistence.SchemaGeneration;
import org.eclipse.jpt.jpa.eclipselink.ui.internal.EclipseLinkHelpContextIds;
import org.eclipse.jpt.jpa.eclipselink.ui.internal.EclipseLinkUiMessages;
import org.eclipse.swt.widgets.Composite;

/**
 * OutputModeComposite
 */
public class OutputModeComposite extends Pane<SchemaGeneration>
{
	/**
	 * Creates a new <code>OutputModeComposite</code>.
	 * 
	 * @param parentController
	 *            The parent container of this one
	 * @param parent
	 *            The parent container
	 */
	public OutputModeComposite(
				Pane<? extends SchemaGeneration> parentComposite, 
				Composite parent) {
		
		super(parentComposite, parent);
	}

	private EnumFormComboViewer<SchemaGeneration, OutputMode> addBuildOutputModeCombo(Composite container) {
		return new EnumFormComboViewer<SchemaGeneration, OutputMode>(this, container) {
			@Override
			protected void addPropertyNames(Collection<String> propertyNames) {
				super.addPropertyNames(propertyNames);
				propertyNames.add(SchemaGeneration.OUTPUT_MODE_PROPERTY);
			}

			@Override
			protected OutputMode[] getChoices() {
				return OutputMode.values();
			}

			@Override
			protected OutputMode getDefaultValue() {
				return this.getSubject().getDefaultOutputMode();
			}

			@Override
			protected String displayString(OutputMode value) {
				return this.buildDisplayString(EclipseLinkUiMessages.class, OutputModeComposite.this, value);
			}

			@Override
			protected OutputMode getValue() {
				return this.getSubject().getOutputMode();
			}

			@Override
			protected void setValue(OutputMode value) {
				this.getSubject().setOutputMode(value);
			}
		};
	}

	@Override
	protected void initializeLayout(Composite container) {
		this.addLabeledComposite(
				container,
				EclipseLinkUiMessages.PersistenceXmlSchemaGenerationTab_outputModeLabel,
				this.addBuildOutputModeCombo(container),
				EclipseLinkHelpContextIds.PERSISTENCE_SCHEMA_GENERATION
		);
	}
}
