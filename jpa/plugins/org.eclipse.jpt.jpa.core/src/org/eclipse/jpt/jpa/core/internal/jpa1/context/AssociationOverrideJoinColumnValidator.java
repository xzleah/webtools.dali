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

import org.eclipse.jpt.common.core.internal.utility.ValidationMessageTools;
import org.eclipse.jpt.common.core.utility.ValidationMessage;
import org.eclipse.jpt.jpa.core.context.ReadOnlyAssociationOverride;
import org.eclipse.jpt.jpa.core.context.ReadOnlyJoinColumn;
import org.eclipse.jpt.jpa.core.context.ReadOnlyPersistentAttribute;
import org.eclipse.jpt.jpa.core.internal.context.JptValidator;
import org.eclipse.jpt.jpa.core.validation.JptJpaCoreValidationMessages;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;

public class AssociationOverrideJoinColumnValidator
	extends JoinColumnValidator
{
	final ReadOnlyAssociationOverride override;


	public AssociationOverrideJoinColumnValidator(
				ReadOnlyAssociationOverride override,
				ReadOnlyJoinColumn column,
				ReadOnlyJoinColumn.Owner joinColumnOwner,
				TableDescriptionProvider provider) {
		super(column, joinColumnOwner, provider);
		this.override = override;
	}

	public AssociationOverrideJoinColumnValidator(
				ReadOnlyPersistentAttribute persistentAttribute,
				ReadOnlyAssociationOverride override,
				ReadOnlyJoinColumn column,
				ReadOnlyJoinColumn.Owner joinColumnOwner,
				TableDescriptionProvider provider) {
		super(persistentAttribute, column, joinColumnOwner, provider);
		this.override = override;
	}

	@Override
	protected JptValidator buildTableValidator() {
		return new TableValidator();
	}

	@Override
	protected IMessage buildUnresolvedNameMessage() {
		return this.override.isVirtual() ?
				this.buildVirtualOverrideUnresolvedNameMessage() :
				super.buildUnresolvedNameMessage();
	}

	protected IMessage buildVirtualOverrideUnresolvedNameMessage() {
		return ValidationMessageTools.buildErrorValidationMessage(
				JptJpaCoreValidationMessages.VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_UNRESOLVED_NAME,
				this.column.getResource(),
				this.column.getNameValidationTextRange(),
				this.override.getName(),
				this.column.getName(),
				this.column.getDbTable().getName()
			);
	}

	@Override
	protected IMessage buildVirtualAttributeUnresolvedNameMessage() {
		return ValidationMessageTools.buildErrorValidationMessage(
				this.getVirtualAttributeUnresolvedNameMessage(),
				this.column.getResource(),
				this.persistentAttribute.getValidationTextRange(),
				this.persistentAttribute.getName(),
				this.override.getName(),
				this.column.getName(),
				this.column.getDbTable().getName()
			);
	}

	@Override
	protected ValidationMessage getVirtualAttributeUnresolvedNameMessage() {
		return JptJpaCoreValidationMessages.VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_JOIN_COLUMN_UNRESOLVED_NAME;
	}

	@Override
	protected IMessage buildUnresolvedReferencedColumnNameMessage() {
		return this.override.isVirtual() ?
				this.buildVirtualOverrideUnresolvedReferencedColumnNameMessage() :
				super.buildUnresolvedReferencedColumnNameMessage();
	}

	protected IMessage buildVirtualOverrideUnresolvedReferencedColumnNameMessage() {
		return ValidationMessageTools.buildErrorValidationMessage(
				JptJpaCoreValidationMessages.VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME,
				this.column.getResource(),
				this.column.getReferencedColumnNameTextRange(),
				this.override.getName(),
				this.column.getReferencedColumnName(),
				this.column.getReferencedColumnDbTable().getName()
			);
	}

	@Override
	protected IMessage buildVirtualAttributeUnresolvedReferencedColumnNameMessage() {
		return ValidationMessageTools.buildErrorValidationMessage(
				this.getVirtualAttributeUnresolvedReferencedColumnNameMessage(),
				this.column.getResource(),
				this.persistentAttribute.getValidationTextRange(),
				this.persistentAttribute.getName(),
				this.override.getName(),
				this.column.getReferencedColumnName(),
				this.column.getReferencedColumnDbTable().getName()
			);
	}

	@Override
	protected ValidationMessage getVirtualAttributeUnresolvedReferencedColumnNameMessage() {
		return JptJpaCoreValidationMessages.VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_JOIN_COLUMN_REFERENCED_COLUMN_UNRESOLVED_NAME;
	}

	@Override
	protected IMessage buildUnspecifiedNameMultipleJoinColumnsMessage() {
		return this.override.isVirtual() ?
				this.buildVirtualOverrideUnspecifiedNameMultipleJoinColumnsMessage() :
				super.buildUnspecifiedNameMultipleJoinColumnsMessage();
	}

	protected IMessage buildVirtualOverrideUnspecifiedNameMultipleJoinColumnsMessage() {
		return ValidationMessageTools.buildErrorValidationMessage(
				JptJpaCoreValidationMessages.VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS,
				this.column.getResource(),
				this.column.getNameValidationTextRange(),
				this.override.getName()
			);
	}

	@Override
	protected IMessage buildVirtualAttributeUnspecifiedNameMultipleJoinColumnsMessage() {
		return ValidationMessageTools.buildErrorValidationMessage(
				this.getVirtualAttributeUnspecifiedNameMultipleJoinColumnsMessage(),
				this.column.getResource(),
				this.persistentAttribute.getValidationTextRange(),
				this.persistentAttribute.getName(),
				this.override.getName()
			);
	}

	@Override
	protected ValidationMessage getVirtualAttributeUnspecifiedNameMultipleJoinColumnsMessage() {
		return JptJpaCoreValidationMessages.VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	}

	@Override
	protected IMessage buildUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage() {
		return this.override.isVirtual() ?
				this.buildVirtualOverrideUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage() :
				super.buildUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage();
	}

	protected IMessage buildVirtualOverrideUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage() {
		return ValidationMessageTools.buildErrorValidationMessage(
				JptJpaCoreValidationMessages.VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS,
				this.column.getResource(),
				this.column.getReferencedColumnNameTextRange(),
				this.override.getName()
			);
	}

	@Override
	protected IMessage buildVirtualAttributeUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage() {
		return ValidationMessageTools.buildErrorValidationMessage(
				this.getVirtualAttributeUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage(),
				this.column.getResource(),
				this.persistentAttribute.getValidationTextRange(),
				this.persistentAttribute.getName(),
				this.override.getName()
			);
	}

	@Override
	protected ValidationMessage getVirtualAttributeUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage() {
		return JptJpaCoreValidationMessages.VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	}


	// ********** table validator **********

	protected class TableValidator
		extends JoinColumnValidator.TableValidator
	{
		protected TableValidator() {
			super();
		}

		@Override
		protected IMessage buildTableNotValidMessage() {
			return AssociationOverrideJoinColumnValidator.this.override.isVirtual() ?
					this.buildVirtualOverrideColumnTableNotValidMessage() :
					super.buildTableNotValidMessage();
		}

		protected IMessage buildVirtualOverrideColumnTableNotValidMessage() {
			return ValidationMessageTools.buildErrorValidationMessage(
					this.getVirtualOverrideColumnTableNotValidMessage(),
					this.getColumn().getResource(),
					this.getColumn().getTableNameValidationTextRange(),
					AssociationOverrideJoinColumnValidator.this.override.getName(),
					this.getColumn().getTableName(),
					this.getColumn().getName(),
					this.getColumnTableDescriptionMessage()
				);
		}

		protected ValidationMessage getVirtualOverrideColumnTableNotValidMessage() {
			return JptJpaCoreValidationMessages.VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_TABLE_NOT_VALID;
		}

		@Override
		protected IMessage buildVirtualAttributeTableNotValidMessage() {
			return ValidationMessageTools.buildErrorValidationMessage(
					this.getVirtualAttributeColumnTableNotValidMessage(),
					this.getColumn().getResource(),
					AssociationOverrideJoinColumnValidator.this.persistentAttribute.getValidationTextRange(),
					AssociationOverrideJoinColumnValidator.this.persistentAttribute.getName(),
					AssociationOverrideJoinColumnValidator.this.override.getName(),
					this.getColumn().getTableName(),
					this.getColumn().getName(),
					this.getColumnTableDescriptionMessage()
				);
		}

		@Override
		protected ValidationMessage getVirtualAttributeColumnTableNotValidMessage() {
			return JptJpaCoreValidationMessages.VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_JOIN_COLUMN_TABLE_NOT_VALID;
		}
	}
}
