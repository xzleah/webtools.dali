/*******************************************************************************
 * Copyright (c) 2009, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.ui.internal.details;

import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkOneToManyRelationship;
import org.eclipse.jpt.jpa.ui.details.JptJpaUiDetailsMessages;
import org.eclipse.jpt.jpa.ui.internal.details.JoinColumnJoiningStrategyPane;
import org.eclipse.jpt.jpa.ui.internal.details.JoinTableJoiningStrategyPane;
import org.eclipse.jpt.jpa.ui.internal.details.MappedByJoiningStrategyPane;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;

public class EclipseLinkOneToManyJoiningStrategyPane 
	extends Pane<EclipseLinkOneToManyRelationship>
{
	public EclipseLinkOneToManyJoiningStrategyPane(
			Pane<?> parentPane, 
			PropertyValueModel<? extends EclipseLinkOneToManyRelationship> subjectHolder, 
			Composite parent) {
		
		super(parentPane, subjectHolder, parent);
	}
	
	@Override
	protected Composite addComposite(Composite container) {
		Section section = this.getWidgetFactory().createSection(container, 
				ExpandableComposite.TITLE_BAR | 
				ExpandableComposite.TWISTIE | 
				ExpandableComposite.EXPANDED);
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		section.setText(JptJpaUiDetailsMessages.Joining_title);

		Composite client = this.getWidgetFactory().createComposite(section);
		client.setLayout(new GridLayout(1, false));
		client.setLayoutData(new GridData(GridData.FILL_BOTH));
		section.setClient(client);

		return client;
	}

	@Override
	protected void initializeLayout(Composite container) {
		addRadioButton(
			container,
			JptJpaUiDetailsMessages.Joining_mappedByLabel,
			MappedByJoiningStrategyPane.buildUsesMappedByJoiningStrategyHolder(getSubjectHolder()),
			null);

		new MappedByJoiningStrategyPane(this, container);
		
		addRadioButton(
			container,
			JptJpaUiDetailsMessages.Joining_joinColumnJoiningLabel,
			JoinColumnJoiningStrategyPane.buildUsesJoinColumnJoiningStrategyHolder(getSubjectHolder()),
			null);

		JoinColumnJoiningStrategyPane.
			buildJoinColumnJoiningStrategyPaneWithoutIncludeOverrideCheckBox(this, container);
		
		addRadioButton(
			container,
			JptJpaUiDetailsMessages.Joining_joinTableJoiningLabel,
			JoinTableJoiningStrategyPane.buildUsesJoinTableJoiningStrategyHolder(getSubjectHolder()),
			null);

		new JoinTableJoiningStrategyPane(this, container);
	}
}
