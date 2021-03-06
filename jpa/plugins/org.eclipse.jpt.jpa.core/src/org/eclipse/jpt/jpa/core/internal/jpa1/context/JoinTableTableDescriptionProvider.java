/*******************************************************************************
 * Copyright (c) 2010, 20201311 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa1.context;

import org.eclipse.jpt.jpa.core.validation.JptJpaCoreValidationArgumentMessages;

public class JoinTableTableDescriptionProvider
	implements AbstractNamedColumnValidator.TableDescriptionProvider
{
	public String getColumnTableDescriptionMessage() {
		return JptJpaCoreValidationArgumentMessages.DOES_NOT_MATCH_JOIN_TABLE;
	}
}
