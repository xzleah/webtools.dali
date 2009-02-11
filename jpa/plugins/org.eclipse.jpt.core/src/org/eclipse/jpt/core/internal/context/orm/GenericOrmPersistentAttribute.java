/*******************************************************************************
 * Copyright (c) 2006, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import org.eclipse.jpt.core.context.AccessType;
import org.eclipse.jpt.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.core.resource.orm.XmlAttributeMapping;


public class GenericOrmPersistentAttribute extends AbstractOrmPersistentAttribute
{
	
	public GenericOrmPersistentAttribute(OrmPersistentType parent, Owner owner, XmlAttributeMapping resourceMapping) {
		super(parent, owner, resourceMapping);
	}
	
	//****************** AccessHolder implementation *******************
	
	/**
	 * GenericOrmPersistentAttribute does not support specified access (no access element in 1.0), so we return null
	 */
	public AccessType getSpecifiedAccess() {
		return null;
	}
	
	public void setSpecifiedAccess(@SuppressWarnings("unused") AccessType newSpecifiedAccess) {
		throw new UnsupportedOperationException("specifiedAccess is not supported for GenericOrmPersistentAttribute"); //$NON-NLS-1$
	}
}
