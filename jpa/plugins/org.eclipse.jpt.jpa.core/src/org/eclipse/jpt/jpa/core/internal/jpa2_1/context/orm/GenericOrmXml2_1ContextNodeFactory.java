/*******************************************************************************
 * Copyright (c) 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa2_1.context.orm;

import org.eclipse.jpt.jpa.core.internal.jpa2.context.orm.GenericOrmXml2_0ContextNodeFactory;
import org.eclipse.jpt.jpa.core.jpa2_1.context.orm.EntityMappings2_1;
import org.eclipse.jpt.jpa.core.jpa2_1.context.orm.OrmConverterType2_1;
import org.eclipse.jpt.jpa.core.jpa2_1.context.orm.OrmXml2_1ContextNodeFactory;
import org.eclipse.jpt.jpa.core.resource.orm.XmlConverter;
import org.eclipse.jpt.jpa.core.resource.orm.XmlNamedStoredProcedureQuery;
import org.eclipse.jpt.jpa.core.resource.orm.XmlStoredProcedureParameter;

public class GenericOrmXml2_1ContextNodeFactory
	extends GenericOrmXml2_0ContextNodeFactory
	implements OrmXml2_1ContextNodeFactory
{

	public OrmConverterType2_1 buildOrmConverterType(EntityMappings2_1 parent, XmlConverter xmlConverter) {
		return new GenericOrmConverterType(parent, xmlConverter);
	}

	public OrmNamedStoredProcedureQuery2_1 buildOrmNamedStoredProcedureQuery2_1(
			OrmQueryContainer2_1 parent, 
			XmlNamedStoredProcedureQuery xmlNamedStoredProcedureQuery) {
		return new GenericOrmNamedStoredProcedureQuery2_1(parent, xmlNamedStoredProcedureQuery);
	}
	
	public OrmStoredProcedureParameter2_1 buildOrmStoredProcedureParameter(
			OrmNamedStoredProcedureQuery2_1 parent,
			XmlStoredProcedureParameter xmlParameter) {
		return new GenericOrmStoredProcedureParameter2_1(parent, xmlParameter);
	}
}
