/*******************************************************************************
* Copyright (c) 2008, 2009 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
* 
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.persistence.options;

import org.eclipse.jpt.eclipselink.core.context.persistence.options.Options;
import org.eclipse.jpt.eclipselink.ui.internal.EclipseLinkHelpContextIds;
import org.eclipse.jpt.eclipselink.ui.internal.EclipseLinkUiMessages;
import org.eclipse.jpt.ui.internal.util.SWTUtil;
import org.eclipse.jpt.ui.internal.widgets.Pane;
import org.eclipse.jpt.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.utility.internal.model.value.PropertyListValueModelAdapter;
import org.eclipse.jpt.utility.model.value.ListValueModel;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.utility.model.value.WritablePropertyValueModel;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;

/**
 *  SessionNameComposite
 */
public class SessionNameComposite extends Pane<Options>
{
	/**
	 * Creates a new <code>SessionNameComposite</code>.
	 *
	 * @param parentPane The parent container of this one
	 * @param parent The parent container
	 */
	public SessionNameComposite(
								Pane<? extends Options> parentPane,
	                           Composite parent) {

		super(parentPane, parent);
	}

	private PropertyValueModel<String> buildDefaultSessionNameHolder() {
		return new PropertyAspectAdapter<Options, String>(this.getSubjectHolder(), Options.DEFAULT_SESSION_NAME) {
			@Override
			protected String buildValue_() {
				return SessionNameComposite.this.getDefaultValue(subject);
			}
		};
	}

	private ListValueModel<String> buildDefaultSessionNameListHolder() {
		return new PropertyListValueModelAdapter<String>(
			this.buildDefaultSessionNameHolder()
		);
	}

	private WritablePropertyValueModel<String> buildSessionNameHolder() {
		return new PropertyAspectAdapter<Options, String>(this.getSubjectHolder(), Options.SESSION_NAME_PROPERTY) {
			@Override
			protected String buildValue_() {

				String name = subject.getSessionName();
				if (name == null) {
					name = SessionNameComposite.this.getDefaultValue(subject);
				}
				return name;
			}

			@Override
			protected void setValue_(String value) {

				if (getDefaultValue(subject).equals(value)) {
					value = null;
				}
				subject.setSessionName(value);
			}
		};
	}

	private String getDefaultValue(Options subject) {
		String defaultValue = subject.getDefaultSessionName();

		if (defaultValue != null) {
			return NLS.bind(
				EclipseLinkUiMessages.PersistenceXmlOptionsTab_defaultWithOneParam,
				defaultValue
			);
		}
		else {
			return EclipseLinkUiMessages.PersistenceXmlOptionsTab_defaultEmpty;
		}
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected void initializeLayout(Composite container) {

		CCombo combo = addLabeledEditableCCombo(
			container,
			EclipseLinkUiMessages.PersistenceXmlOptionsTab_sessionName,
			this.buildDefaultSessionNameListHolder(),
			this.buildSessionNameHolder(),
			EclipseLinkHelpContextIds.PERSISTENCE_OPTIONS_SESSION_NAME
		);
		SWTUtil.attachDefaultValueHandler(combo);
	}
}
