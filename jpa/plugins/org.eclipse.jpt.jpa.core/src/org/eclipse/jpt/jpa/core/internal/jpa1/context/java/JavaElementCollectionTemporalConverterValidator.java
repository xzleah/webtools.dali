/*******************************************************************************
 * Copyright (c) 2011, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa1.context.java;

import org.eclipse.jpt.common.core.utility.ValidationMessage;
import org.eclipse.jpt.jpa.core.context.BaseTemporalConverter;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.AbstractTemporalConverterValidator;
import org.eclipse.jpt.jpa.core.jpa2.context.java.JavaElementCollectionMapping2_0;
import org.eclipse.jpt.jpa.core.validation.JptJpaCoreValidationMessages;

public class JavaElementCollectionTemporalConverterValidator
	extends AbstractTemporalConverterValidator
{
	public JavaElementCollectionTemporalConverterValidator(BaseTemporalConverter converter) {
		super(converter);
	}

	@Override
	protected JavaElementCollectionMapping2_0 getAttributeMapping() {
		return (JavaElementCollectionMapping2_0) super.getAttributeMapping();
	}
	@Override
	protected String getTypeName() {
		return this.getAttributeMapping().getFullyQualifiedTargetClass();
	}

	@Override
	protected ValidationMessage getInvalidTemporalMappingTypeMessage() {
		return JptJpaCoreValidationMessages.PERSISTENT_ATTRIBUTE_ELEMENT_COLLECTION_INVALID_VALUE_TYPE;
	}

	@Override
	protected ValidationMessage getVirtualAttributeInvalidTemporalMappingTypeMessage() {
		return JptJpaCoreValidationMessages.VIRTUAL_ATTRIBUTE_ELEMENT_COLLECTION_INVALID_VALUE_TYPE;
	}
}
