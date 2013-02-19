/*******************************************************************************
 * Copyright (c) 2010, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa1.context;

import org.eclipse.jpt.common.core.utility.ValidationMessage;
import org.eclipse.jpt.jpa.core.context.ReadOnlyBaseColumn;
import org.eclipse.jpt.jpa.core.context.ReadOnlyPersistentAttribute;
import org.eclipse.jpt.jpa.core.internal.context.JptValidator;
import org.eclipse.jpt.jpa.core.validation.JptJpaCoreValidationMessages;

public class MapKeyColumnValidator
	extends AbstractNamedColumnValidator<ReadOnlyBaseColumn>
{
	public MapKeyColumnValidator(
				ReadOnlyPersistentAttribute persistentAttribute,
				ReadOnlyBaseColumn column,
				TableDescriptionProvider provider) {
		super(persistentAttribute, column, provider);
	}

	@Override
	protected JptValidator buildTableValidator() {
		return new TableValidator();
	}

	@Override
	protected ValidationMessage getUnresolvedNameMessage() {
		return JptJpaCoreValidationMessages.MAP_KEY_COLUMN_UNRESOLVED_NAME;
	}

	@Override
	protected ValidationMessage getVirtualAttributeUnresolvedNameMessage() {
		return JptJpaCoreValidationMessages.VIRTUAL_ATTRIBUTE_MAP_KEY_COLUMN_UNRESOLVED_NAME;
	}


	// ********** table validator **********

	protected class TableValidator
		extends BaseColumnTableValidator
	{
		protected TableValidator() {
			super();
		}

		@Override
		protected ValidationMessage getColumnTableNotValidMessage() {
			return JptJpaCoreValidationMessages.MAP_KEY_COLUMN_TABLE_NOT_VALID;
		}

		@Override
		protected ValidationMessage getVirtualAttributeColumnTableNotValidMessage() {
			return JptJpaCoreValidationMessages.VIRTUAL_ATTRIBUTE_MAP_KEY_COLUMN_TABLE_NOT_VALID;
		}
	}
}
