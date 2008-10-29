/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.orm.details;

import org.eclipse.jpt.core.context.IdMapping;
import org.eclipse.jpt.eclipselink.ui.internal.mappings.details.EclipseLinkIdMappingComposite;
import org.eclipse.jpt.ui.WidgetFactory;
import org.eclipse.jpt.ui.details.JpaComposite;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.swt.widgets.Composite;

//Temporary to remove the Converters section from orm basic mapping.
//This is supported in EclipseLink in version 1.1, but not 1.0
public class EclipseLinkOrmIdMappingComposite extends EclipseLinkIdMappingComposite                                  implements JpaComposite
{
	/**
	 * Creates a new <code>EclipseLinkOrmIdMappingComposite</code>.
	 *
	 * @param subjectHolder The holder of the subject <code>IdMapping</code>
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create various common widgets
	 */
	public EclipseLinkOrmIdMappingComposite(PropertyValueModel<? extends IdMapping> subjectHolder,
	                               Composite parent,
	                               WidgetFactory widgetFactory) {

		super(subjectHolder, parent, widgetFactory);
	}

	@Override
	protected void initializeLayout(Composite container) {
		initializeGeneralPane(container);
	}
}