/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.context.orm;

import java.util.List;
import org.eclipse.jpt.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.core.internal.context.orm.GenericOrmEmbeddable;
import org.eclipse.jpt.core.resource.orm.XmlEmbeddable;
import org.eclipse.jpt.core.resource.orm.XmlEntityMappings;
import org.eclipse.jpt.eclipselink.core.context.ChangeTracking;
import org.eclipse.jpt.eclipselink.core.context.Customizer;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkEmbeddable;
import org.eclipse.jpt.eclipselink.core.context.java.EclipseLinkJavaEmbeddable;
import org.eclipse.jpt.eclipselink.core.context.java.JavaCustomizer;
import org.eclipse.jpt.eclipselink.core.internal.EclipseLinkJpaFactory;
import org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmFactory;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlCustomizerHolder;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;

public class EclipseLinkOrmEmbeddable extends GenericOrmEmbeddable
	implements EclipseLinkEmbeddable
{
	protected final EclipseLinkOrmCustomizer customizer;
	
	
	public EclipseLinkOrmEmbeddable(OrmPersistentType parent) {
		super(parent);
		this.customizer = getJpaFactory().buildOrmCustomizer(this);
	}
	
	
	@Override
	protected EclipseLinkJpaFactory getJpaFactory() {
		return (EclipseLinkJpaFactory) super.getJpaFactory();
	}

	public ChangeTracking getChangeTracking() {
		// TODO Auto-generated method stub
		return null;
	}

	public Customizer getCustomizer() {
		return this.customizer;
	}

	
	
	// **************** resource-context interaction ***************************
	
	@Override
	public XmlEmbeddable addToResourceModel(XmlEntityMappings entityMappings) {
		XmlEmbeddable embeddable = EclipseLinkOrmFactory.eINSTANCE.createXmlEmbeddable();
		getPersistentType().initialize(embeddable);
		entityMappings.getEmbeddables().add(embeddable);
		return embeddable;
	}
	
	@Override
	public void initialize(XmlEmbeddable mappedSuperclass) {
		super.initialize(mappedSuperclass);
		this.customizer.initialize((XmlCustomizerHolder) mappedSuperclass, getJavaCustomizer());
	}
	
	@Override
	public void update(XmlEmbeddable mappedSuperclass) {
		super.update(mappedSuperclass);
		this.customizer.update((XmlCustomizerHolder) mappedSuperclass, getJavaCustomizer());
	}
	
	@Override
	protected EclipseLinkJavaEmbeddable getJavaEmbeddableForDefaults() {
		return (EclipseLinkJavaEmbeddable) super.getJavaEmbeddableForDefaults();
	}
	
	
	protected JavaCustomizer getJavaCustomizer() {
		EclipseLinkJavaEmbeddable javaEmbeddable = getJavaEmbeddableForDefaults();
		return (javaEmbeddable == null) ? null : javaEmbeddable.getCustomizer();
	}
	
	
	// **************** validation **************************************
	
	@Override
	public void validate(List<IMessage> messages) {
		super.validate(messages);
		this.customizer.validate(messages);
	}
}
