/*******************************************************************************
* Copyright (c) 2010 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
* 
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.jpa.core.jpa2.resource.java;

import org.eclipse.jpt.jpa.core.resource.java.ManyToOneAnnotation;

/**
 * Corresponds to the JPA 2.0 annotation
 * <code>javax.persistence.ManyToOne</code>
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
public interface ManyToOneAnnotation2_0
	extends ManyToOneAnnotation, RelationshipMappingAnnotation2_0
{
	// combine various interfaces
}
