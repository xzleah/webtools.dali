/*******************************************************************************
 * Copyright (c) 2010, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa1.context;

import org.eclipse.jpt.jpa.core.context.ReadOnlyTable;
import org.eclipse.jpt.jpa.core.validation.JptJpaCoreValidationMessages;

public class TableValidator
	extends AbstractTableValidator
{
	public TableValidator(ReadOnlyTable table) {
		super(table);
	}

	@Override
	protected String getUnresolvedCatalogMessage() {
		return JptJpaCoreValidationMessages.TABLE_UNRESOLVED_CATALOG;
	}

	@Override
	protected String getVirtualAttributeUnresolvedCatalogMessage() {
		throw this.buildAttributeTableNotSupportedException();
	}

	@Override
	protected String getUnresolvedSchemaMessage() {
		return JptJpaCoreValidationMessages.TABLE_UNRESOLVED_SCHEMA;
	}

	@Override
	protected String getVirtualAttributeUnresolvedSchemaMessage() {
		throw this.buildAttributeTableNotSupportedException();
	}

	@Override
	protected String getUnresolvedNameMessage() {
		return JptJpaCoreValidationMessages.TABLE_UNRESOLVED_NAME;
	}

	@Override
	protected String getVirtualAttributeUnresolvedNameMessage() {
		throw this.buildAttributeTableNotSupportedException();
	}

	protected UnsupportedOperationException buildAttributeTableNotSupportedException() {
		return new UnsupportedOperationException("An attribute cannot specify a table"); //$NON-NLS-1$
	}
}
