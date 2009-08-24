/*******************************************************************************
 * Copyright (c) 2008, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.persistence.connection;

import org.eclipse.jpt.eclipselink.core.context.persistence.connection.Connection;
import org.eclipse.jpt.eclipselink.ui.internal.EclipseLinkUiMessages;
import org.eclipse.jpt.ui.internal.JpaHelpContextIds;
import org.eclipse.jpt.ui.internal.mappings.JptUiMappingsMessages;
import org.eclipse.jpt.ui.internal.widgets.Pane;
import org.eclipse.jpt.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.utility.model.value.WritablePropertyValueModel;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;

/**
 * NativeSqlComposite
 */
public class NativeSqlComposite extends Pane<Connection>
{
	/**
	 * Creates a new <code>NativeSqlComposite</code>.
	 *
	 * @param parentController
	 *            The parent container of this one
	 * @param parent
	 *            The parent container
	 */
	public NativeSqlComposite(
					Pane<? extends Connection> parentComposite,
					Composite parent) {

		super(parentComposite, parent);
	}
	
	@Override
	protected void initializeLayout(Composite container) {
		this.addTriStateCheckBoxWithDefault(
			container,
			EclipseLinkUiMessages.PersistenceXmlConnectionTab_nativeSqlLabel,
			this.buildNativeSqlHolder(),
			this.buildNativeSqlStringHolder(),
			JpaHelpContextIds.PERSISTENCE_XML_CONNECTION
		);
	}
	
	private WritablePropertyValueModel<Boolean> buildNativeSqlHolder() {
		return new PropertyAspectAdapter<Connection, Boolean>(getSubjectHolder(), Connection.NATIVE_SQL_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getNativeSql();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setNativeSql(value);
			}

		};
	}

	private PropertyValueModel<String> buildNativeSqlStringHolder() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultNativeSqlHolder()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptUiMappingsMessages.Boolean_True : JptUiMappingsMessages.Boolean_False;
					return NLS.bind(EclipseLinkUiMessages.PersistenceXmlConnectionTab_nativeSqlLabelDefault, defaultStringValue);
				}
				return EclipseLinkUiMessages.PersistenceXmlConnectionTab_nativeSqlLabel;
			}
		};
	}
	
	private PropertyValueModel<Boolean> buildDefaultNativeSqlHolder() {
		return new PropertyAspectAdapter<Connection, Boolean>(
			getSubjectHolder(),
			Connection.NATIVE_SQL_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getNativeSql() != null) {
					return null;
				}
				return this.subject.getDefaultNativeSql();
			}
		};
	}
}
