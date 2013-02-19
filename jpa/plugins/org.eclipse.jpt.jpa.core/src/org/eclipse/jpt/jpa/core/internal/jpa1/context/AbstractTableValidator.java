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

import java.util.List;
import org.eclipse.jpt.common.core.internal.utility.ValidationMessageTools;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.core.utility.ValidationMessage;
import org.eclipse.jpt.jpa.core.context.ReadOnlyPersistentAttribute;
import org.eclipse.jpt.jpa.core.context.ReadOnlyTable;
import org.eclipse.jpt.jpa.core.internal.context.JptValidator;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

public abstract class AbstractTableValidator
	implements JptValidator
{
	/** this is <code>null</code> for tables defined on entities */
	protected final ReadOnlyPersistentAttribute persistentAttribute;

	protected final ReadOnlyTable table;

	protected AbstractTableValidator(
				ReadOnlyTable table) {
		this(null, table);
	}

	protected AbstractTableValidator(
				ReadOnlyPersistentAttribute persistentAttribute,
				ReadOnlyTable table) {
		super();
		this.persistentAttribute = persistentAttribute;
		this.table = table;
	}

	public boolean validate(List<IMessage> messages, IReporter reporter) {
		return this.validatesAgainstDatabase() &&
			 	this.validateAgainstDatabase(messages);
	}

	protected boolean validatesAgainstDatabase() {
		return this.table.validatesAgainstDatabase();
	}

	protected boolean validateAgainstDatabase(List<IMessage> messages) {
		if ( ! this.table.catalogIsResolved()) {
			messages.add(this.buildUnresolvedCatalogMessage());
			return false;
		}

		if ( ! this.table.schemaIsResolved()) {
			messages.add(this.buildUnresolvedSchemaMessage());
			return false;
		}

		if ( ! this.table.isResolved()) {
			if (this.table.getName() != null) { //if name is null, the validation will be handled elsewhere, such as the target entity is not defined
				messages.add(this.buildUnresolvedNameMessage());
			}
			return false;
		}
		return true;
	}

	protected IMessage buildUnresolvedCatalogMessage() {
		return this.tableIsPartOfVirtualAttribute() ?
				this.buildVirtualAttributeUnresolvedCatalogMessage() :
				this.buildUnresolvedCatalogMessage(this.getUnresolvedCatalogMessage());
	}

	protected abstract ValidationMessage getUnresolvedCatalogMessage();

	protected IMessage buildUnresolvedCatalogMessage(ValidationMessage message) {
		return ValidationMessageTools.buildErrorValidationMessage(
				message,
				this.table.getResource(),
				this.table.getCatalogValidationTextRange(),
				this.table.getCatalog(),
				this.table.getName()
			);
	}

	protected IMessage buildVirtualAttributeUnresolvedCatalogMessage() {
		return ValidationMessageTools.buildErrorValidationMessage(
				this.getVirtualAttributeUnresolvedCatalogMessage(),
				this.table.getResource(),
				this.getVirtualAttributeValidationTextRange(),
				this.persistentAttribute.getName(),
				this.table.getCatalog(),
				this.table.getName()
			);
	}

	protected TextRange getVirtualAttributeValidationTextRange() {
		return this.persistentAttribute.getValidationTextRange();
	}

	protected abstract ValidationMessage getVirtualAttributeUnresolvedCatalogMessage();

	protected IMessage buildUnresolvedSchemaMessage() {
		return this.tableIsPartOfVirtualAttribute() ?
				this.buildVirtualAttributeUnresolvedSchemaMessage() :
				this.buildUnresolvedSchemaMessage(this.getUnresolvedSchemaMessage());
	}

	protected abstract ValidationMessage getUnresolvedSchemaMessage();

	protected IMessage buildUnresolvedSchemaMessage(ValidationMessage message) {
		return ValidationMessageTools.buildErrorValidationMessage(
				message,
				this.table.getResource(),
				this.table.getSchemaValidationTextRange(),
				this.table.getSchema(),
				this.table.getName()
			);
	}

	protected IMessage buildVirtualAttributeUnresolvedSchemaMessage() {
		return ValidationMessageTools.buildErrorValidationMessage(
				this.getVirtualAttributeUnresolvedSchemaMessage(),
				this.table.getResource(),
				this.getVirtualAttributeValidationTextRange(),
				this.persistentAttribute.getName(),
				this.table.getSchema(),
				this.table.getName()
			);
	}

	protected abstract ValidationMessage getVirtualAttributeUnresolvedSchemaMessage();

	protected IMessage buildUnresolvedNameMessage() {
		return this.tableIsPartOfVirtualAttribute() ?
				this.buildVirtualAttributeUnresolvedNameMessage() :
				this.buildUnresolvedNameMessage(this.getUnresolvedNameMessage());
	}

	protected abstract ValidationMessage getUnresolvedNameMessage();

	protected IMessage buildUnresolvedNameMessage(ValidationMessage message) {
		return ValidationMessageTools.buildErrorValidationMessage(
				message,
				this.table.getResource(),
				this.table.getNameValidationTextRange(),
				this.table.getName()
			);
	}

	protected IMessage buildVirtualAttributeUnresolvedNameMessage() {
		return ValidationMessageTools.buildErrorValidationMessage(
			this.getVirtualAttributeUnresolvedNameMessage(),
			this.table.getResource(),
			this.getVirtualAttributeValidationTextRange(),
			this.persistentAttribute.getName(),
			this.table.getName()
		);
	}

	protected abstract ValidationMessage getVirtualAttributeUnresolvedNameMessage();

	protected boolean tableIsPartOfVirtualAttribute() {
		return (this.persistentAttribute != null) &&
				this.persistentAttribute.isVirtual();
	}
}
