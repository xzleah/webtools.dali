/*******************************************************************************
* Copyright (c) 2009 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
* 
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.ui.internal.jpa2.persistence.connection;

import org.eclipse.jpt.core.internal.jpa2.context.persistence.connection.JpaConnection2_0;
import org.eclipse.jpt.ui.internal.jpa2.Jpt2_0UiMessages;
import org.eclipse.jpt.ui.internal.widgets.Pane;
import org.eclipse.swt.widgets.Composite;

/**
 *  ConnectionPropertiesComposite
 */
public class ConnectionPropertiesComposite extends Pane<JpaConnection2_0>
{
	public ConnectionPropertiesComposite(Pane<JpaConnection2_0> parentComposite, Composite parent) {

		super(parentComposite, parent);
	}

	@Override
	protected void initializeLayout(Composite container) {

		container = addTitledGroup(
			container,
			Jpt2_0UiMessages.ConnectionPropertiesComposite_Database_GroupBox
		);

		new DataSourcePropertiesComposite(this, container);
		new JdbcPropertiesComposite(this, container);
	}
}
