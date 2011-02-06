/*******************************************************************************
 * Copyright (c) 2008, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.resource.java;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.jpa.core.resource.java.Annotation;

/**
 * Corresponds to the EclipseLink annotation
 * org.eclipse.persistence.annotations.Convert
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
public interface EclipseLinkConvertAnnotation
	extends Annotation
{
	String ANNOTATION_NAME = EclipseLink.CONVERT;
		
	/**
	 * Corresponds to the 'value' element of the Convert annotation.
	 * Return null if the element does not exist in Java.
	 */
	String getValue();
		String VALUE_PROPERTY = "value"; //$NON-NLS-1$
	
	/**
	 * Corresponds to the 'value' element of the Convert annotation.
	 * Set to null to remove the element.
	 */
	void setValue(String value);
	
	/**
	 * Return the {@link TextRange} for the 'value' element. If the element 
	 * does not exist return the {@link TextRange} for the Convert annotation.
	 */
	TextRange getValueTextRange(CompilationUnit astRoot);

	/**
	 * Return whether the specified position touches the 'value' element.
	 * Return false if the element does not exist.
	 */
	boolean valueTouches(int pos, CompilationUnit astRoot);

}