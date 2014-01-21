/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2012 SAP AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Petya Sabeva - initial API, implementation and documentation
 *
 * </copyright>
 *
 *******************************************************************************/

package org.eclipse.jpt.jpadiagrameditor.ui.internal.command;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jpt.common.core.resource.java.Annotation;
import org.eclipse.jpt.common.utility.command.Command;
import org.eclipse.jpt.jpa.core.context.AttributeMapping;
import org.eclipse.jpt.jpa.core.context.MappedByRelationship;
import org.eclipse.jpt.jpa.core.context.PersistentAttribute;
import org.eclipse.jpt.jpa.core.context.PersistentType;
import org.eclipse.jpt.jpa.core.context.RelationshipMapping;
import org.eclipse.jpt.jpa.core.context.SpecifiedMappedByRelationshipStrategy;
import org.eclipse.jpt.jpa.core.context.java.JavaAttributeMapping;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.jpa.core.resource.java.OwnableRelationshipMappingAnnotation;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.IJPAEditorFeatureProvider;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.EntityChangeListener;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPAEditorConstants;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JpaArtifactFactory;

public class SetMappedByNewValueCommand implements Command{
	
	private IJPAEditorFeatureProvider fp;
	private PersistenceUnit pu;
	private String inverseEntityName;
	private String inverseAttributeName;
	private String newAtName;
	private PersistentAttribute oldAt;

	public SetMappedByNewValueCommand(IJPAEditorFeatureProvider fp,
			PersistenceUnit pu, String inverseEntityName,
			String inverseAttributeName, String newAt,
			PersistentAttribute oldAt) {
		super();
		this.fp =fp;
		this.pu = pu;
		this.inverseEntityName = inverseEntityName;
		this.inverseAttributeName = inverseAttributeName;
		this.newAtName = newAt;
		this.oldAt = oldAt;
	}

	public void execute() {
		fp.addAttribForUpdate(pu, inverseEntityName
				+ EntityChangeListener.SEPARATOR + inverseAttributeName
				+ EntityChangeListener.SEPARATOR + newAtName
				+ EntityChangeListener.SEPARATOR + oldAt.getName());
		
		PersistentType pt = pu.getPersistentType(inverseEntityName);
		if(pt == null) {
			return;
		}
		
		PersistentAttribute pa = pt.getAttributeNamed(inverseAttributeName);
		if(pa == null) {
			return;
		}
		
		AttributeMapping m = JpaArtifactFactory.instance().getAttributeMapping(pa);
		
		if(m != null && m instanceof RelationshipMapping) {
			
			MappedByRelationship mappedByrelationShip = (MappedByRelationship) ((RelationshipMapping)m).getRelationship();
			SpecifiedMappedByRelationshipStrategy mappedByStrategy = mappedByrelationShip.getMappedByStrategy();
			String mappedBy = mappedByStrategy.getMappedByAttribute();
			if (mappedBy == null)
				return;
			String[] mappedByAttrs = mappedBy.split(JPAEditorConstants.MAPPED_BY_ATTRIBUTE_SPLIT_SEPARATOR);		
			if(mappedByAttrs.length > 1){
				if(mappedByAttrs[0].equals(oldAt.getName())){
					mappedBy = newAtName + JPAEditorConstants.MAPPED_BY_ATTRIBUTE_SEPARATOR + mappedByAttrs[1];
				} else if(mappedByAttrs[1].equals(oldAt.getName())){
					mappedBy = mappedByAttrs[0] + JPAEditorConstants.MAPPED_BY_ATTRIBUTE_SEPARATOR + newAtName;
				}
			} else if(mappedBy.equals(oldAt.getName())){
				mappedBy = newAtName;
			}

			mappedByStrategy.setMappedByAttribute(mappedBy);
			
			if(pa.getJavaPersistentAttribute() != null && (JpaArtifactFactory.instance().getORMPersistentAttribute(pa) == null)) {
				JavaAttributeMapping attrM = pa.getJavaPersistentAttribute().getMapping();
				Annotation a = attrM.getMappingAnnotation();
				if(a != null) {
					((OwnableRelationshipMappingAnnotation)a).setMappedBy(mappedBy);
				}
			}
			mappedByrelationShip.synchronizeWithResourceModel();
		}
		
		ICompilationUnit un = fp.getCompilationUnit(pt);
		try {
			if (un.isWorkingCopy()) {
				un.commitWorkingCopy(true, new NullProgressMonitor());
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}

	}
	
}
