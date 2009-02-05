/*******************************************************************************
 * Copyright (c) 2006, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jpt.core.JpaStructureNode;
import org.eclipse.jpt.core.context.PersistentAttribute;
import org.eclipse.jpt.core.context.java.JavaPersistentType;
import org.eclipse.jpt.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.core.context.orm.OrmTypeMapping;
import org.eclipse.jpt.core.internal.context.AbstractXmlContextNode;
import org.eclipse.jpt.core.internal.validation.DefaultJpaValidationMessages;
import org.eclipse.jpt.core.internal.validation.JpaValidationMessages;
import org.eclipse.jpt.core.resource.orm.XmlTypeMapping;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.db.Schema;
import org.eclipse.jpt.db.Table;
import org.eclipse.jpt.utility.internal.StringTools;
import org.eclipse.jpt.utility.internal.iterators.EmptyIterator;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;


public abstract class AbstractOrmTypeMapping<T extends XmlTypeMapping>
	extends AbstractXmlContextNode 
	implements OrmTypeMapping
{
	protected String class_;
	
	public boolean defaultMetadataComplete;
	
	protected Boolean specifiedMetadataComplete;
	
	protected T resourceTypeMapping;
	
	
	protected AbstractOrmTypeMapping(OrmPersistentType parent) {
		super(parent);
	}	
	
	// **************** Type Mapping implementation *****************************

	@Override
	public OrmPersistentType getParent() {
		return (OrmPersistentType) super.getParent();
	}

	public OrmPersistentType getPersistentType() {
		return this.getParent();
	}
	
	protected JavaPersistentType getJavaPersistentType() {
		return this.getPersistentType().getJavaPersistentType();
	}
	
	public boolean isMapped() {
		return true;
	}

	public String getPrimaryTableName() {
		return null;
	}

	public String getClass_() {
		return this.class_;
	}

	public void setClass(String newClass) {
		String oldClass = this.class_;
		this.class_ = newClass;
		this.resourceTypeMapping.setClassName(newClass);
		firePropertyChanged(CLASS_PROPERTY, oldClass, newClass);
		getPersistentType().classChanged(oldClass, newClass);
	}
	

	public boolean isMetadataComplete() {
		if (isDefaultMetadataComplete()) {
			//entity-mappings/persistence-unit-metadata/xml-mapping-metadata-complete is specified, then it overrides
			//anything set here
			return true;
		}
		return (this.getSpecifiedMetadataComplete() == null) ? this.isDefaultMetadataComplete() : this.getSpecifiedMetadataComplete().booleanValue();
	}

	public boolean isDefaultMetadataComplete() {
		return this.defaultMetadataComplete;
	}
	
	protected void setDefaultMetadataComplete(boolean newDefaultMetadataComplete) {
		boolean oldMetadataComplete = this.defaultMetadataComplete;
		this.defaultMetadataComplete = newDefaultMetadataComplete;
		firePropertyChanged(DEFAULT_METADATA_COMPLETE_PROPERTY, oldMetadataComplete, newDefaultMetadataComplete);
	}
	
	public Boolean getSpecifiedMetadataComplete() {
		return this.specifiedMetadataComplete;
	}
	
	public void setSpecifiedMetadataComplete(Boolean newSpecifiedMetadataComplete) {
		Boolean oldMetadataComplete = this.specifiedMetadataComplete;
		this.specifiedMetadataComplete = newSpecifiedMetadataComplete;
		this.resourceTypeMapping.setMetadataComplete(newSpecifiedMetadataComplete);
		firePropertyChanged(SPECIFIED_METADATA_COMPLETE_PROPERTY, oldMetadataComplete, newSpecifiedMetadataComplete);
	}

	/**
	 * ITypeMapping is changed and various ITypeMappings may have
	 * common settings.  In this method initialize the new ITypeMapping (this)
	 * fromthe old ITypeMapping (oldMapping)
	 */
	public void initializeFrom(OrmTypeMapping oldMapping) {
		this.setClass(oldMapping.getClass_());
		this.setSpecifiedMetadataComplete(oldMapping.getSpecifiedMetadataComplete());
		this.setDefaultMetadataComplete(oldMapping.isDefaultMetadataComplete());
	}
	
	public Table getPrimaryDbTable() {
		return null;
	}

	public Table getDbTable(String tableName) {
		return null;
	}

	public Schema getDbSchema() {
		return null;
	}

	public boolean attributeMappingKeyAllowed(String attributeMappingKey) {
		return true;
	}

	public Iterator<OrmPersistentAttribute> overridableAttributes() {
		return EmptyIterator.instance();
	}

	public Iterator<String> overridableAttributeNames() {
		return EmptyIterator.instance();
	}
	
	public Iterator<PersistentAttribute> allOverridableAttributes() {
		return EmptyIterator.instance();
	}

	public Iterator<String> allOverridableAttributeNames() {
		return EmptyIterator.instance();
	}

	public Iterator<OrmPersistentAttribute> overridableAssociations() {
		return EmptyIterator.instance();
	}	
	
	public Iterator<String> overridableAssociationNames() {
		return EmptyIterator.instance();
	}

	public Iterator<PersistentAttribute> allOverridableAssociations() {
		return EmptyIterator.instance();
	}

	public Iterator<String> allOverridableAssociationNames() {
		return EmptyIterator.instance();
	}

	public T getResourceTypeMapping() {
		return this.resourceTypeMapping;
	}
	
	@SuppressWarnings("unchecked")
	public void initialize(XmlTypeMapping resourceTypeMapping) {
		this.resourceTypeMapping = (T) resourceTypeMapping;
		this.initialize();
	}
	
	protected void initialize() {
		this.class_ = this.getResourceClassName();
		this.specifiedMetadataComplete = this.getResourceMetadataComplete();
		this.defaultMetadataComplete = this.getPersistentType().isDefaultMetadataComplete();
	}
	
	public void update() {
		this.setClass(this.getResourceClassName());
		this.setSpecifiedMetadataComplete(this.getResourceMetadataComplete());
		this.setDefaultMetadataComplete(this.getPersistentType().isDefaultMetadataComplete());
	}
	
	protected String getResourceClassName() {
		return this.resourceTypeMapping.getClassName();
	}
	
	protected Boolean getResourceMetadataComplete() {
		return this.resourceTypeMapping.getMetadataComplete();
	}
	
	public IContentType getContentType() {
		return this.getPersistentType().getContentType();
	}

	
	// *************************************************************************
	
	public JpaStructureNode getStructureNode(int offset) {
		if (this.resourceTypeMapping.containsOffset(offset)) {
			return getPersistentType();
		}
		return null;
	}
	
	public TextRange getSelectionTextRange() {
		return this.resourceTypeMapping.getSelectionTextRange();
	}
	
	public TextRange getClassTextRange() {
		return this.resourceTypeMapping.getClassTextRange();
	}
	
	public TextRange getAttributesTextRange() {
		return this.resourceTypeMapping.getAttributesTextRange();
	}

	public boolean containsOffset(int textOffset) {
		return (this.resourceTypeMapping != null)
				&& this.resourceTypeMapping.containsOffset(textOffset);
	}
	
	//************************* validation ************************
	@Override
	public void validate(List<IMessage> messages) {
		super.validate(messages);
		this.validateClass(messages);
	}

	protected void validateClass(List<IMessage> messages) {
		if (StringTools.stringIsEmpty(this.class_)) {
			messages.add(
				DefaultJpaValidationMessages.buildMessage(
					IMessage.HIGH_SEVERITY,
					JpaValidationMessages.PERSISTENT_TYPE_UNSPECIFIED_CLASS,
					this, 
					this.getClassTextRange()
				)
			);
			return;
		}
	}

	public TextRange getValidationTextRange() {
		return this.resourceTypeMapping.getValidationTextRange();
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.getPersistentType().getName());
	}

}
