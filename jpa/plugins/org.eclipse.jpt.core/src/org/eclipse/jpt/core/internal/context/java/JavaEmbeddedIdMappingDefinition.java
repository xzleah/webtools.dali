/*******************************************************************************
 * Copyright (c) 2006, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import org.eclipse.jpt.common.utility.internal.iterables.ArrayIterable;
import org.eclipse.jpt.core.JpaFactory;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.java.JavaAttributeMapping;
import org.eclipse.jpt.core.context.java.JavaAttributeMappingDefinition;
import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.resource.java.AttributeOverrideAnnotation;
import org.eclipse.jpt.core.resource.java.AttributeOverridesAnnotation;
import org.eclipse.jpt.core.resource.java.EmbeddedIdAnnotation;

public class JavaEmbeddedIdMappingDefinition
	implements JavaAttributeMappingDefinition
{
	// singleton
	private static final JavaAttributeMappingDefinition INSTANCE = new JavaEmbeddedIdMappingDefinition();

	/**
	 * Return the singleton.
	 */
	public static JavaAttributeMappingDefinition instance() {
		return INSTANCE;
	}


	/**
	 * Enforce singletong usage
	 */
	private JavaEmbeddedIdMappingDefinition() {
		super();
	}

	public String getKey() {
		return MappingKeys.EMBEDDED_ID_ATTRIBUTE_MAPPING_KEY;
	}

	public String getAnnotationName() {
		return EmbeddedIdAnnotation.ANNOTATION_NAME;
	}

	public boolean isSpecified(JavaPersistentAttribute persistentAttribute) {
		return persistentAttribute.getResourcePersistentAttribute().getAnnotation(this.getAnnotationName()) != null;
	}

	public Iterable<String> getSupportingAnnotationNames() {
		return SUPPORTING_ANNOTATION_NAMES;
	}

	protected static final String[] SUPPORTING_ANNOTATION_NAMES_ARRAY = new String[] {
		AttributeOverrideAnnotation.ANNOTATION_NAME,
		AttributeOverridesAnnotation.ANNOTATION_NAME,
	};
	protected static final Iterable<String> SUPPORTING_ANNOTATION_NAMES = new ArrayIterable<String>(SUPPORTING_ANNOTATION_NAMES_ARRAY);

	public JavaAttributeMapping buildMapping(JavaPersistentAttribute persistentAttribute, JpaFactory factory) {
		return factory.buildJavaEmbeddedIdMapping(persistentAttribute);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
