/*******************************************************************************
 * Copyright (c) 2009, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.db.internal.vendor;

import org.eclipse.jpt.common.utility.internal.StringTools;

/**
 * Fold "normal" identifiers to lower case.
 * Ignore the case of "normal" identifiers.
 */
class LowerCaseFoldingStrategy
	implements FoldingStrategy
{

	// singleton
	private static final FoldingStrategy INSTANCE = new LowerCaseFoldingStrategy();

	/**
	 * Return the singleton.
	 */
	static FoldingStrategy instance() {
		return INSTANCE;
	}

	/**
	 * Ensure single instance.
	 */
	private LowerCaseFoldingStrategy() {
		super();
	}

	public String fold(String name) {
		return name.toLowerCase();
	}

	public boolean nameIsFolded(String name) {
		return StringTools.stringIsLowercase(name);
	}

	public boolean regularIdentifiersAreCaseSensitive() {
		return false;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}