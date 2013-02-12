/*******************************************************************************
 * Copyright (c) 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.predicate;

/**
 * A predicate that evaulates to the result of some combination of a set of
 * other predicates (e.g. the compound predicate's value may be the result
 * of ANDing the values of its child predicates together).
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @param <V> the type of objects to be evaluated
 * 
 * @see org.eclipse.jpt.common.utility.internal.predicate.PredicateTools
 */
public interface CompoundPredicate<V>
	extends Predicate<V>
{
	/**
	 * Return the child predicates used to calculate the compound predicate's
	 * value (e.g. the compound predicate's value may be the result
	 * of ANDing the values of these predicates together).
	 */
	Predicate<? super V>[] getPredicates();
}
