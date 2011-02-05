/*******************************************************************************
 * Copyright (c) 2008, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.details;

import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.core.context.Entity;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkChangeTracking;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkCustomizer;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkEntity;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkReadOnly;
import org.eclipse.swt.widgets.Composite;

public class EclipseLinkEntityAdvancedComposite extends Pane<Entity> {
	
	public EclipseLinkEntityAdvancedComposite(
			Pane<? extends Entity> parentPane,
			Composite parent) {

		super(parentPane, parent, false);
	}
	
	@Override
	protected void initializeLayout(Composite container) {
		container = addCollapsibleSection(
			container,
			EclipseLinkUiDetailsMessages.EclipseLinkTypeMappingComposite_advanced
		);
		
		new EclipseLinkReadOnlyComposite(this, buildReadOnlyHolder(), container);
		new EclipseLinkCustomizerComposite(this, buildCustomizerHolder(), container);
		new EclipseLinkChangeTrackingComposite(this, buildChangeTrackingHolder(), container);
	}
	
	private PropertyValueModel<EclipseLinkReadOnly> buildReadOnlyHolder() {
		return new PropertyAspectAdapter<Entity, EclipseLinkReadOnly>(getSubjectHolder()) {
			@Override
			protected EclipseLinkReadOnly buildValue_() {
				return ((EclipseLinkEntity) this.subject).getReadOnly();
			}
		};
	}
	
	private PropertyValueModel<EclipseLinkCustomizer> buildCustomizerHolder() {
		return new PropertyAspectAdapter<Entity, EclipseLinkCustomizer>(getSubjectHolder()) {
			@Override
			protected EclipseLinkCustomizer buildValue_() {
				return ((EclipseLinkEntity) this.subject).getCustomizer();
			}
		};
	}
	
	private PropertyValueModel<EclipseLinkChangeTracking> buildChangeTrackingHolder() {
		return new PropertyAspectAdapter<Entity, EclipseLinkChangeTracking>(getSubjectHolder()) {
			@Override
			protected EclipseLinkChangeTracking buildValue_() {
				return ((EclipseLinkEntity) this.subject).getChangeTracking();
			}
		};
	}
}
