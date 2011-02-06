/*******************************************************************************
 * Copyright (c) 2009, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.context.persistence;

import org.eclipse.jpt.common.utility.internal.Transformer;
import org.eclipse.jpt.jpa.core.context.PersistentType;

/**
 * Interface used by persistence unit to gather up persistent types.
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 2.3
 * @since 2.3
 */
public interface PersistentTypeContainer {

	/**
	 * Return the container's persistent types.
	 */
	Iterable<? extends PersistentType> getPersistentTypes();


	Transformer<PersistentTypeContainer, Iterable<? extends PersistentType>> TRANSFORMER =
		new Transformer<PersistentTypeContainer, Iterable<? extends PersistentType>>() {
			public Iterable<? extends PersistentType> transform(PersistentTypeContainer container) {
				return container.getPersistentTypes();
			}
			@Override
			public String toString() {
				return "PersistentTypeContainer.TRANSFORMER"; //$NON-NLS-1$
			}
		};
}