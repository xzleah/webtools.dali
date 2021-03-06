/*******************************************************************************
 * Copyright (c) 2010, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.context;

import org.eclipse.jpt.common.utility.internal.transformer.TransformerAdapter;

/**
 * Named context model. Sorta. :-)
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface JpaNamedContextModel
	extends JpaContextModel
{
	String getName();
		String NAME_PROPERTY = "name"; //$NON-NLS-1$
	void setName(String name);

	TransformerAdapter<JpaNamedContextModel, String> NAME_TRANSFORMER = new NameTransformer();
	class NameTransformer
		extends TransformerAdapter<JpaNamedContextModel, String>
	{
		@Override
		public String transform(JpaNamedContextModel node) {
			return node.getName();
		}
	}

	Class<? extends JpaNamedContextModel> getType();

	/**
	 * Return whether the specified model is <em>not</em> this model and it has
	 * the same state. Typically the specified model would be the same type as
	 * this model.
	 * @see #getType()
	 */
	boolean isEquivalentTo(JpaNamedContextModel node);
}
