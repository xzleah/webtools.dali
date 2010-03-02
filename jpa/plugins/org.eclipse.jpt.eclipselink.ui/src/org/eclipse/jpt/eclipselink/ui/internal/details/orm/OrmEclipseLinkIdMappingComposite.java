/*******************************************************************************
 * Copyright (c) 2008, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.details.orm;

import org.eclipse.jpt.core.context.orm.OrmIdMapping;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkConvert;
import org.eclipse.jpt.eclipselink.ui.internal.details.EclipseLinkIdMappingComposite;
import org.eclipse.jpt.eclipselink.ui.internal.details.EclipseLinkMutableComposite;
import org.eclipse.jpt.ui.WidgetFactory;
import org.eclipse.jpt.ui.internal.details.ColumnComposite;
import org.eclipse.jpt.ui.internal.details.orm.OrmMappingNameChooser;
import org.eclipse.jpt.ui.internal.widgets.Pane;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.swt.widgets.Composite;

//Remove the Converters section from 1.0 orm id mappings.
//This is supported in EclipseLink in version 1.1, but not 1.0
public class OrmEclipseLinkIdMappingComposite extends EclipseLinkIdMappingComposite<OrmIdMapping>
{
	/**
	 * Creates a new <code>EclipseLinkOrmIdMappingComposite</code>.
	 *
	 * @param subjectHolder The holder of the subject <code>IdMapping</code>
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create various common widgets
	 */
	public OrmEclipseLinkIdMappingComposite(PropertyValueModel<? extends OrmIdMapping> subjectHolder,
	                               Composite parent,
	                               WidgetFactory widgetFactory) {

		super(subjectHolder, parent, widgetFactory);
	}
	
	@Override
	protected void initializeIdSection(Composite container) {		
		new ColumnComposite(this, buildColumnHolder(), container);
		new OrmMappingNameChooser(this, getSubjectHolder(), container);
		new EclipseLinkMutableComposite(this, buildMutableHolder(), container);
	}	

	@Override
	//everything but the 'Define Converter' section.  This is not supported in eclipselink 1.0, but is in 1.1
	protected Pane<EclipseLinkConvert> buildConvertComposite(PropertyValueModel<EclipseLinkConvert> convertHolder, Composite container) {
		return new OrmEclipseLinkConvert1_0Composite(convertHolder, container, getWidgetFactory());
	}
}