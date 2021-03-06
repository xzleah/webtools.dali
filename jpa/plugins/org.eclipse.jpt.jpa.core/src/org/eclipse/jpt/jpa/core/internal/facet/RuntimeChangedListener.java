/*******************************************************************************
 * Copyright (c) 2010, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.facet;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jpt.jpa.core.JpaPlatform;
import org.eclipse.jpt.jpa.core.JpaProject;
import org.eclipse.jpt.jpa.core.internal.plugin.JptJpaCorePlugin;
import org.eclipse.jpt.jpa.core.libprov.JpaLibraryProviderInstallOperationConfig;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryInstallDelegate;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectBase;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectEvent;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectListener;

public class RuntimeChangedListener
	implements IFacetedProjectListener
{
	public void handleEvent(IFacetedProjectEvent facetedProjectEvent) {
		
		IFacetedProjectBase fpb = facetedProjectEvent.getWorkingCopy();
		if (fpb == null) {
			fpb = facetedProjectEvent.getProject();
		}
		IProjectFacetVersion pfv = fpb.getProjectFacetVersion(JpaProject.FACET);
		if (pfv != null) {
			Map<String, Object> enablementVariables = new HashMap<String, Object>();
			enablementVariables.put(JpaLibraryProviderInstallOperationConfig.JPA_PLATFORM_ENABLEMENT_EXP, getJpaPlatformId(fpb.getProject()));
			enablementVariables.put(JpaLibraryProviderInstallOperationConfig.JPA_PLATFORM_DESCRIPTION_ENABLEMENT_EXP, getJpaPlatformConfig(fpb.getProject()));
			LibraryInstallDelegate lp = new LibraryInstallDelegate(fpb, pfv, enablementVariables);
			try {
				lp.execute(new NullProgressMonitor());
			}
			catch (CoreException ce) {
				JptJpaCorePlugin.instance().logError(ce);
			}
			finally {
				lp.dispose();
			}
		}
	}
	
	protected String getJpaPlatformId(IProject project) {
		JpaProject jpaProject = this.getJpaProject(project);
		return (jpaProject == null) ? null : jpaProject.getJpaPlatform().getId();
	}
	
	protected JpaPlatform.Config getJpaPlatformConfig(IProject project) {
		JpaProject jpaProject = this.getJpaProject(project);
		return (jpaProject == null) ? null : jpaProject.getJpaPlatform().getConfig();
	}

	protected JpaProject getJpaProject(IProject project) {
		try {
			return this.getJpaProject_(project);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			return null;
		}
	}

	protected JpaProject getJpaProject_(IProject project) throws InterruptedException {
		JpaProject.Reference ref = this.getJpaProjectReference(project);
		return (ref == null) ? null : ref.getValue();
	}

	protected JpaProject.Reference getJpaProjectReference(IProject project) {
		return (JpaProject.Reference) project.getAdapter(JpaProject.Reference.class);
	}
}
