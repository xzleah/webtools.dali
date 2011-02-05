/*******************************************************************************
 * Copyright (c) 2006, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.internal.synch;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.common.utility.internal.iterables.TransformationIterable;
import org.eclipse.jpt.core.JpaProject;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.context.persistence.MappingFileRef;
import org.eclipse.jpt.core.context.persistence.Persistence;
import org.eclipse.jpt.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.core.context.persistence.PersistenceXml;
import org.eclipse.jpt.core.internal.JptCoreMessages;
import org.eclipse.jpt.core.resource.persistence.PersistenceFactory;
import org.eclipse.jpt.core.resource.persistence.XmlJavaClassRef;
import org.eclipse.jpt.core.resource.persistence.XmlPersistence;
import org.eclipse.jpt.core.resource.persistence.XmlPersistenceUnit;
import org.eclipse.jpt.core.resource.xml.JpaXmlResource;

/**
 * Synchronizes the lists of persistent classes in a persistence unit and a 
 * persistence project.
 */
public class SynchronizeClassesJob extends WorkspaceJob
{
	private IFile persistenceXmlFile;
	
	
	public SynchronizeClassesJob(IFile file) {
		super(JptCoreMessages.SYNCHRONIZE_CLASSES_JOB);
		IResourceRuleFactory ruleFactory = ResourcesPlugin.getWorkspace().getRuleFactory();
		setRule(ruleFactory.modifyRule(file.getProject()));
		this.persistenceXmlFile = file;
	}
	
	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor) {
		if (monitor.isCanceled()) {
			return Status.CANCEL_STATUS;
		}
		final SubMonitor sm = SubMonitor.convert(monitor, JptCoreMessages.SYNCHRONIZING_CLASSES_TASK, 20);
		final JpaProject jpaProject = JptCorePlugin.getJpaProject(this.persistenceXmlFile.getProject());
		final JpaXmlResource resource = jpaProject.getPersistenceXmlResource();
		if (resource == null) {
			//the resource would only be null if the persistence.xml file had an invalid content type
			return Status.OK_STATUS;
		}
		if (sm.isCanceled()) {
			return Status.CANCEL_STATUS;
		}
		sm.worked(1);
		
		resource.modify(new Runnable() {
			public void run() {
				XmlPersistence persistence = (XmlPersistence) resource.getRootObject();					
				XmlPersistenceUnit persistenceUnit;
				
				if (persistence.getPersistenceUnits().size() > 0) {
					persistenceUnit = persistence.getPersistenceUnits().get(0);
				}
				else {
					persistenceUnit = PersistenceFactory.eINSTANCE.createXmlPersistenceUnit();
					persistenceUnit.setName(jpaProject.getName());
					persistence.getPersistenceUnits().add(persistenceUnit);
				}
				sm.worked(1);
				
				persistenceUnit.getClasses().clear();
				sm.worked(1);
		
				addClassRefs(sm.newChild(17), jpaProject, persistenceUnit);
			}
		});
		return Status.OK_STATUS;
	}
	
	protected void addClassRefs(IProgressMonitor monitor, JpaProject jpaProject, XmlPersistenceUnit persistenceUnit) {
		Iterable<String> mappedClassNames = getMappedClassNames(jpaProject, '$');
		final SubMonitor sm = SubMonitor.convert(monitor, CollectionTools.size(mappedClassNames));
		
		for (String fullyQualifiedTypeName : mappedClassNames) {
			if ( ! mappingFileContains(jpaProject, fullyQualifiedTypeName)) {
				XmlJavaClassRef classRef = PersistenceFactory.eINSTANCE.createXmlJavaClassRef();
				classRef.setJavaClass(fullyQualifiedTypeName);
				persistenceUnit.getClasses().add(classRef);
			}
			sm.worked(1);
		}
	}

	protected Iterable<String> getMappedClassNames(final JpaProject jpaProject, final char enclosingTypeSeparator) {
		return new TransformationIterable<String, String>(jpaProject.getMappedJavaSourceClassNames()) {
			@Override
			protected String transform(String fullyQualifiedName) {
				IType jdtType = SynchronizeClassesJob.this.findType(jpaProject, fullyQualifiedName);
				return jdtType.getFullyQualifiedName(enclosingTypeSeparator);
			}
		};
	}
	
	protected IType findType(JpaProject jpaProject, String typeName) {
		try {
			return jpaProject.getJavaProject().findType(typeName);
		} catch (JavaModelException ex) {
			return null;  // ignore exception?
		}
	}

	boolean mappingFileContains(JpaProject jpaProject, String fullyQualifiedTypeName) {
		PersistenceXml persistenceXml = jpaProject.getRootContextNode().getPersistenceXml();
		if (persistenceXml == null) {
			return false;
		}
		Persistence persistence = persistenceXml.getPersistence();
		if (persistence == null) {
			return false;
		}
		if (persistence.persistenceUnitsSize() == 0) {
			return false;
		}
		PersistenceUnit persistenceUnit = persistence.persistenceUnits().next();
		for (MappingFileRef mappingFileRef : CollectionTools.iterable(persistenceUnit.mappingFileRefs())) {
			if (mappingFileRef.getPersistentType(fullyQualifiedTypeName) != null) {
				return true;
			}
		}
		return false;
	}
}