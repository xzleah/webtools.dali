/*******************************************************************************
 * Copyright (c) 2007, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.internal.context.persistence;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jpt.core.JpaStructureNode;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.context.MappingFile;
import org.eclipse.jpt.core.context.MappingFilePersistenceUnitDefaults;
import org.eclipse.jpt.core.context.MappingFileRoot;
import org.eclipse.jpt.core.context.PersistentType;
import org.eclipse.jpt.core.context.persistence.MappingFileRef;
import org.eclipse.jpt.core.context.persistence.PersistenceStructureNodes;
import org.eclipse.jpt.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.core.internal.context.AbstractXmlContextNode;
import org.eclipse.jpt.core.internal.validation.DefaultJpaValidationMessages;
import org.eclipse.jpt.core.internal.validation.JpaValidationMessages;
import org.eclipse.jpt.core.resource.common.JpaXmlResource;
import org.eclipse.jpt.utility.internal.StringTools;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;

public abstract class AbstractMappingFileRef
	extends AbstractXmlContextNode
	implements MappingFileRef
{

	protected String fileName;

	protected MappingFile mappingFile;


	// ********** construction/initialization **********

	protected AbstractMappingFileRef(PersistenceUnit parent, String resourceFileName) {
		super(parent);
		this.fileName = resourceFileName;
		this.initializeMappingFile();
	}

	protected void initializeMappingFile() {
		JpaXmlResource xmlResource = this.getXmlResource();
		if (xmlResource != null) {
			this.mappingFile = this.buildMappingFile(xmlResource);
		}
	}
	/**
	 * The XmlMappingFileRef resource is the Persistence xml resource.
	 * This returns the resource of the mapping file itself.
	 */
	protected JpaXmlResource getXmlResource() {
		return getJpaProject().getMappingFileResource(this.fileName);
	}
	
	protected IFile getPlatformFile() {
		return JptCorePlugin.getPlatformFile(this.getJpaProject().getProject(), this.fileName);
	}

	// ********** JpaStructureNode implementation **********

	public String getId() {
		return PersistenceStructureNodes.MAPPING_FILE_REF_ID;
	}

	public JpaStructureNode getStructureNode(int textOffset) {
		return this;
	}

	public void dispose() {
		if (this.mappingFile != null) {
			this.mappingFile.dispose();
		}
	}


	// ********** queries **********

	public MappingFilePersistenceUnitDefaults getPersistenceUnitDefaults() {
		MappingFileRoot root = this.getMappingFileRoot_();
		return (root == null) ? null : root.getPersistenceUnitDefaults();
	}

	/**
	 * #getMappingFileRoot() is already defined by JpaContextNode for the
	 * descendants of a "mapping file root" - we want something slightly
	 * different here...
	 */
	protected MappingFileRoot getMappingFileRoot_() {
		return (this.mappingFile == null) ? null : this.mappingFile.getRoot();
	}

	public boolean persistenceUnitDefaultsExists() {
		MappingFilePersistenceUnitDefaults defaults = this.getPersistenceUnitDefaults();
		return (defaults != null) && defaults.resourceExists();
	}

	public PersistentType getPersistentType(String typeName) {
		return (this.mappingFile == null) ? null : this.mappingFile.getPersistentType(typeName);
	}


	// ********** file name **********

	public String getFileName() {
		return this.fileName;
	}


	// ********** mapping file **********

	public MappingFile getMappingFile() {
		return this.mappingFile;
	}

	protected void setMappingFile(MappingFile mappingFile) {
		MappingFile old = this.mappingFile;
		this.mappingFile = mappingFile;
		this.firePropertyChanged(MAPPING_FILE_PROPERTY, old, mappingFile);
	}


	// ********** updating **********

	protected void update() {
		this.updateMappingFile();
	}

	protected void updateMappingFile() {
		JpaXmlResource xmlResource = this.getXmlResource();
		if (xmlResource != null) {
			if (this.mappingFile == null) {
				this.setMappingFile(this.buildMappingFile(xmlResource));
			} else {
				// if the resource type has changed, rebuild the mapping file
				if (this.mappingFile.getXmlResource() != xmlResource) {
					this.mappingFile.dispose();
					this.setMappingFile(this.buildMappingFile(xmlResource));
				}
				else {
					this.mappingFile.update(xmlResource);
				}
			}
		} else {
			if (this.mappingFile != null) {
				this.mappingFile.dispose();
				this.setMappingFile(null);
			}
		}
	}

	protected MappingFile buildMappingFile(JpaXmlResource resource) {
		return this.getJpaPlatform().buildMappingFile(this, resource);
	}


	// ********** validation **********

	@Override
	public void validate(List<IMessage> messages) {
		super.validate(messages);

		if (StringTools.stringIsEmpty(this.fileName)) {
			messages.add(
				DefaultJpaValidationMessages.buildMessage(
					IMessage.HIGH_SEVERITY,
					JpaValidationMessages.PERSISTENCE_UNIT_UNSPECIFIED_MAPPING_FILE,
					this,
					this.getValidationTextRange()
				)
			);
			return;
		}

		if (this.mappingFile == null) {
			IFile platformFile = this.getPlatformFile();
			String msgID = platformFile.exists() ?
					JpaValidationMessages.PERSISTENCE_UNIT_UNSUPPORTED_MAPPING_FILE_CONTENT
				:
					JpaValidationMessages.PERSISTENCE_UNIT_NONEXISTENT_MAPPING_FILE;
			messages.add(
				DefaultJpaValidationMessages.buildMessage(
					IMessage.HIGH_SEVERITY,
					msgID,
					new String[] {this.fileName},
					this,
					this.getValidationTextRange()
				)
			);
			return;
		}

		if (this.mappingFile.getRoot() == null) {
			messages.add(
				DefaultJpaValidationMessages.buildMessage(
					IMessage.HIGH_SEVERITY,
					JpaValidationMessages.PERSISTENCE_UNIT_INVALID_MAPPING_FILE,
					new String[] {this.fileName},
					this,
					this.getValidationTextRange()
				)
			);
		}

		this.mappingFile.validate(messages);
	}


	// ********** misc **********

	@Override
	public void toString(StringBuilder sb) {
		super.toString(sb);
		sb.append(this.fileName);
	}

}
