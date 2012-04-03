/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.wizards.conversion.java;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jpt.jpa.core.JpaProject;
import org.eclipse.jpt.jpa.core.JptJpaCorePlugin;
import org.eclipse.jpt.jpa.ui.internal.wizards.SelectMappingFileDialog;
import org.eclipse.jpt.jpa.ui.internal.wizards.orm.EmbeddedMappingFileWizard;

public abstract class GenericJavaMetadataConversionWizardPage
	extends JavaMetadataConversionWizardPage
{
	public GenericJavaMetadataConversionWizardPage(JpaProject jpaProject, String title, String description) {
		super(jpaProject, title, description);
	}

	@Override
	protected IPath getDefaultMappingFileRuntimPath() {
		return JptJpaCorePlugin.DEFAULT_ORM_XML_RUNTIME_PATH;
	}
	
	@Override
	protected IContentType getMappingFileContentType() {
		return JptJpaCorePlugin.ORM_XML_CONTENT_TYPE;
	}

	@Override
	protected IPath openNewMappingFileWizard() {
		return EmbeddedMappingFileWizard.createNewMappingFile(new StructuredSelection(this.jpaProject.getProject()));
	}

	@Override
	protected SelectMappingFileDialog buildSelectMappingFileDialog(){
		return new SelectMappingFileDialog(this.getShell(), this.jpaProject.getProject());
	}
}
