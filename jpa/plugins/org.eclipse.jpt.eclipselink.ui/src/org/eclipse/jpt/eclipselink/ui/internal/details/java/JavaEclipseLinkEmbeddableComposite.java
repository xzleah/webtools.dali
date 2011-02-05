/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.details.java;

import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.core.context.Embeddable;
import org.eclipse.jpt.core.context.java.JavaEmbeddable;
import org.eclipse.jpt.ui.details.java.JavaUiFactory;
import org.eclipse.swt.widgets.Composite;

/**
 * This pane does not have any widgets.
 *
 * @see Embeddable
 * @see JavaUiFactory - The factory creating this pane
 * @see EmbeddableUiProvider
 *
 * @version 2.3
 * @since 2.1
 */
public class JavaEclipseLinkEmbeddableComposite extends AbstractJavaEclipseLinkEmbeddableComposite
{
	/**
	 * Creates a new <code>EmbeddableComposite</code>.
	 *
	 * @param subjectHolder The holder of this pane's subject
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create various common widgets
	 */
	public JavaEclipseLinkEmbeddableComposite(PropertyValueModel<? extends JavaEmbeddable> subjectHolder,
	                           Composite parent,
	                           WidgetFactory widgetFactory) {

		super(subjectHolder, parent, widgetFactory);
	}
}