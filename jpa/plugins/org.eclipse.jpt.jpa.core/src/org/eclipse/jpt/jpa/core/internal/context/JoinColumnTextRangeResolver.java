/*******************************************************************************
 *  Copyright (c) 2010, 2011  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.context;

/**
 * Interface to resolve text ranges on join columns
 */
public interface JoinColumnTextRangeResolver
	extends BaseJoinColumnTextRangeResolver, TableColumnTextRangeResolver
{
	// combine interfaces
}
