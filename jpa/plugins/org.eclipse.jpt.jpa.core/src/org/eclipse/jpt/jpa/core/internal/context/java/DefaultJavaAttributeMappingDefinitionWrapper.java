/*******************************************************************************
 * Copyright (c) 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.context.java;

import org.eclipse.jpt.jpa.core.context.java.DefaultJavaAttributeMappingDefinition;
import org.eclipse.jpt.jpa.core.context.java.JavaSpecifiedPersistentAttribute;

public abstract class DefaultJavaAttributeMappingDefinitionWrapper
	extends JavaAttributeMappingDefinitionWrapper
	implements DefaultJavaAttributeMappingDefinition
{
	protected DefaultJavaAttributeMappingDefinitionWrapper() {
		super();
	}

	@Override
	protected abstract DefaultJavaAttributeMappingDefinition getDelegate();

	public boolean isDefault(JavaSpecifiedPersistentAttribute persistentAttribute) {
		return this.getDelegate().isDefault(persistentAttribute);
	}
}
