/*******************************************************************************
 * Copyright (c) 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.internal.predicate;

/**
 * This predicate evaluates to <code>true</code> if the variable is
 * identical to (<code>==</code>) the criterion specified during
 * construction. If the criterion is <code>null</code>, the predicate
 * will evaluate to <code>true</code> if the variable is also
 * <code>null</code>.
 * 
 * @param <V> the type of objects to be evaluated by the predicate
 */
public class IdentityPredicate<V>
	extends CriterionPredicate<V, V>
{
	private static final long serialVersionUID = 1L;


	/**
	 * Construct a predicate that will evaluate to <code>true</code> if the
	 * variable is <code>null</code>.
	 */
	public IdentityPredicate() {
		this(null);
	}

	/**
	 * Construct a predicate that will evaluate to <code>true</code> if the
	 * variable is identical to (<code>==</code>) the
	 * specified criterion.
	 */
	public IdentityPredicate(V criterion) {
		super(criterion);
	}

	public boolean evaluate(V variable) {
		return variable == this.criterion;
	}

	@Override
	public IdentityPredicate<V> clone() {
		return (IdentityPredicate<V>) super.clone();
	}
}
