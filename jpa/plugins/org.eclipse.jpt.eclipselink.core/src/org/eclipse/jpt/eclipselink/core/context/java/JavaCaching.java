/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.context.java;

import org.eclipse.jpt.core.context.java.JavaJpaContextNode;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.eclipselink.core.context.Caching;

/**
 * 
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 2.1
 * @since 2.1
 */
public interface JavaCaching extends Caching, JavaJpaContextNode
{
	
	/**
	 * Return true if the existence-checking model object exists.  
	 * Have to have a separate flag for this since the default existence
	 * type is different depending on whether hasExistenceChecking() returns
	 * true or false.
	 */
	boolean hasExistenceChecking();
	void setExistenceChecking(boolean existenceChecking);
		String EXISTENCE_CHECKING_PROPERTY = "existenceCheckingProperty"; //$NON-NLS-1$
	
	/**
	 * Initialize the EclipseLinkJavaCaching context model object to match the CacheAnnotation 
	 * resource model object. This should be called immediately after object creation.
	 */
	void initialize(JavaResourcePersistentType resourcePersistentType);
	
	/**
	 * Update the EclipseLinkJavaCaching context model object to match the CacheAnnotation 
	 * resource model object. see {@link org.eclipse.jpt.core.JpaProject#update()}
	 */
	void update(JavaResourcePersistentType resourcePersistentType);	
	
	
	//********* covariant overrides ************
	
	JavaExpiryTimeOfDay getExpiryTimeOfDay();
	
	JavaExpiryTimeOfDay addExpiryTimeOfDay();

}
