/*******************************************************************************
 * Copyright (c) 2008, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.context.orm;

import org.eclipse.jpt.core.context.java.JavaTransientMapping;
import org.eclipse.jpt.core.context.orm.OrmTypeMapping;
import org.eclipse.jpt.core.internal.context.orm.VirtualXmlTransient;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlAccessMethods;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlTransient;

/**
 * VirtualBasic is an implementation of Basic used when there is 
 * no tag in the orm.xml and an underlying javaBasicMapping exists.
 */
public class EclipseLinkVirtualXmlTransient extends XmlTransient
{
		
	protected OrmTypeMapping ormTypeMapping;
	
	protected final JavaTransientMapping javaAttributeMapping;

	protected final VirtualXmlTransient virtualXmlTransient;
	
	public EclipseLinkVirtualXmlTransient(OrmTypeMapping ormTypeMapping, JavaTransientMapping javaTransientMapping) {
		super();
		this.ormTypeMapping = ormTypeMapping;
		this.javaAttributeMapping = javaTransientMapping;
		this.virtualXmlTransient = new VirtualXmlTransient(ormTypeMapping, javaTransientMapping);
	}
	
	protected boolean isOrmMetadataComplete() {
		return this.ormTypeMapping.isMetadataComplete();
	}
	
	@Override
	public String getMappingKey() {
		return this.virtualXmlTransient.getMappingKey();
	}
	
	@Override
	public String getName() {
		return this.virtualXmlTransient.getName();
	}

	@Override
	public void setName(String newName) {
		this.virtualXmlTransient.setName(newName);
	}
	
	@Override
	public TextRange getNameTextRange() {
		return this.virtualXmlTransient.getNameTextRange();
	}
	
	@Override
	public XmlAccessMethods getAccessMethods() {
		return null;
	}
	
	@Override
	public void setAccessMethods(XmlAccessMethods newAccessMethods) {
		throw new UnsupportedOperationException("cannot set values on a virtual mapping"); //$NON-NLS-1$		
	}

}
