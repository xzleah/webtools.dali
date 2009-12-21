/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.resource.java;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jpt.core.resource.java.Annotation;
import org.eclipse.jpt.core.resource.java.AnnotationDefinition;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentMember;
import org.eclipse.jpt.core.utility.jdt.Attribute;
import org.eclipse.jpt.core.utility.jdt.Member;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.binary.BinaryEclipseLinkPrivateOwnedAnnotation;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.source.SourceEclipseLinkPrivateOwnedAnnotation;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLinkPrivateOwnedAnnotation;

/**
 * org.eclipse.persistence.annotations.PrivateOwned
 */
public class EclipseLinkPrivateOwnedAnnotationDefinition
	implements AnnotationDefinition
{
	// singleton
	private static final AnnotationDefinition INSTANCE = new EclipseLinkPrivateOwnedAnnotationDefinition();

	/**
	 * Return the singleton.
	 */
	public static AnnotationDefinition instance() {
		return INSTANCE;
	}

	/**
	 * Ensure single instance.
	 */
	private EclipseLinkPrivateOwnedAnnotationDefinition() {
		super();
	}

	public Annotation buildAnnotation(JavaResourcePersistentMember parent, Member member) {
		return new SourceEclipseLinkPrivateOwnedAnnotation((JavaResourcePersistentAttribute) parent, (Attribute) member);
	}

	public Annotation buildNullAnnotation(JavaResourcePersistentMember parent) {
		throw new UnsupportedOperationException();
	}

	public Annotation buildAnnotation(JavaResourcePersistentMember parent, IAnnotation jdtAnnotation) {
		return new BinaryEclipseLinkPrivateOwnedAnnotation((JavaResourcePersistentAttribute) parent, jdtAnnotation);
	}

	public String getAnnotationName() {
		return EclipseLinkPrivateOwnedAnnotation.ANNOTATION_NAME;
	}

}
