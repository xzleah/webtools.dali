/*******************************************************************************
 * Copyright (c) 2008, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.internal.context.orm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jpt.common.core.internal.utility.JavaProjectTools;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAbstractType;
import org.eclipse.jpt.common.utility.internal.ObjectTools;
import org.eclipse.jpt.common.utility.internal.collection.ListTools;
import org.eclipse.jpt.common.utility.internal.iterable.IterableTools;
import org.eclipse.jpt.common.utility.iterable.ListIterable;
import org.eclipse.jpt.jpa.core.context.JpaContextModel;
import org.eclipse.jpt.jpa.core.context.JpaNamedContextModel;
import org.eclipse.jpt.jpa.core.internal.context.MappingTools;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkConversionValue;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkObjectTypeConverter;
import org.eclipse.jpt.jpa.eclipselink.core.internal.context.java.EclipseLinkJavaConversionValue;
import org.eclipse.jpt.jpa.eclipselink.core.internal.context.java.EclipseLinkJavaObjectTypeConverter;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.EclipseLinkOrmFactory;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlConversionValue;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlObjectTypeConverter;
import org.eclipse.jpt.jpa.eclipselink.core.validation.JptJpaEclipseLinkCoreValidationMessages;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

public class EclipseLinkOrmObjectTypeConverter
	extends EclipseLinkOrmConverter<XmlObjectTypeConverter>
	implements EclipseLinkObjectTypeConverter
{
	private String dataType;
	private String fullyQualifiedDataType;

	private String objectType;
	private String fullyQualifiedObjectType;


	protected final ContextListContainer<EclipseLinkOrmConversionValue, XmlConversionValue> conversionValueContainer;

	private String defaultObjectValue;


	public EclipseLinkOrmObjectTypeConverter(JpaContextModel parent, XmlObjectTypeConverter xmlConverter) {
		super(parent, xmlConverter);
		this.dataType = xmlConverter.getDataType();
		this.objectType = xmlConverter.getObjectType();
		this.conversionValueContainer = this.buildConversionValueContainer();
		this.defaultObjectValue = xmlConverter.getDefaultObjectValue();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.setDataType_(this.xmlConverter.getDataType());
		this.setObjectType_(this.xmlConverter.getObjectType());
		this.syncConversionValues();
		this.setDefaultObjectValue_(this.xmlConverter.getDefaultObjectValue());
	}

	@Override
	public void update() {
		super.update();
		this.setFullyQualifiedDataType(this.buildFullyQualifiedDataType());
		this.setFullyQualifiedObjectType(this.buildFullyQualifiedObjectType());
		this.updateModels(this.getConversionValues());
	}


	// ********** data type **********

	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.setDataType_(dataType);
		this.xmlConverter.setDataType(dataType);
	}

	protected void setDataType_(String dataType) {
		String old = this.dataType;
		this.dataType = dataType;
		this.firePropertyChanged(DATA_TYPE_PROPERTY, old, dataType);
	}

	public String getFullyQualifiedDataType() {
		return this.fullyQualifiedDataType;
	}

	protected void setFullyQualifiedDataType(String dataType) {
		String old = this.fullyQualifiedDataType;
		this.fullyQualifiedDataType = dataType;
		this.firePropertyChanged(FULLY_QUALIFIED_DATA_TYPE_PROPERTY, old, dataType);
	}

	protected String buildFullyQualifiedDataType() {
		return this.getMappingFileRoot().qualify(this.dataType);
	}

	protected boolean dataTypeIsFor(String typeName) {
		return this.typeIsFor(this.getDataTypeJavaResourceType(), typeName);
	}

	protected boolean dataTypeIsIn(IPackageFragment packageFragment) {
		return this.typeIsIn(this.getDataTypeJavaResourceType(), packageFragment);
	}

	protected JavaResourceAbstractType getDataTypeJavaResourceType() {
		if (this.fullyQualifiedDataType == null) {
			return null;
		}
		return this.getJpaProject().getJavaResourceType(this.fullyQualifiedDataType);
	}


	// ********** object type **********

	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(String objectType) {
		this.setObjectType_(objectType);
		this.xmlConverter.setObjectType(objectType);
	}

	protected void setObjectType_(String objectType) {
		String old = this.objectType;
		this.objectType = objectType;
		this.firePropertyChanged(OBJECT_TYPE_PROPERTY, old, objectType);
	}

	public String getFullyQualifiedObjectType() {
		return this.fullyQualifiedObjectType;
	}

	protected void setFullyQualifiedObjectType(String objectType) {
		String old = this.fullyQualifiedObjectType;
		this.fullyQualifiedObjectType = objectType;
		this.firePropertyChanged(FULLY_QUALIFIED_OBJECT_TYPE_PROPERTY, old, objectType);
	}

	protected String buildFullyQualifiedObjectType() {
		return this.getMappingFileRoot().qualify(this.objectType);
	}

	protected boolean objectTypeIsFor(String typeName) {
		return this.typeIsFor(this.getObjectTypeJavaResourceType(), typeName);
	}

	protected boolean objectTypeIsIn(IPackageFragment packageFragment) {
		return this.typeIsIn(this.getObjectTypeJavaResourceType(), packageFragment);
	}

	protected JavaResourceAbstractType getObjectTypeJavaResourceType() {
		if (this.fullyQualifiedObjectType == null) {
			return null;
		}
		return this.getJpaProject().getJavaResourceType(this.fullyQualifiedObjectType);
	}


	// ********** conversion values **********

	public ListIterable<EclipseLinkOrmConversionValue> getConversionValues() {
		return this.conversionValueContainer.getContextElements();
	}

	public int getConversionValuesSize() {
		return this.conversionValueContainer.getContextElementsSize();
	}

	public EclipseLinkConversionValue getConversionValue(int index) {
		return this.conversionValueContainer.get(index);
	}

	public EclipseLinkOrmConversionValue addConversionValue() {
		return this.addConversionValue(this.getConversionValuesSize());
	}

	public EclipseLinkOrmConversionValue addConversionValue(int index) {
		XmlConversionValue xmlConversionValue = this.buildXmlConversionValue();
		EclipseLinkOrmConversionValue conversionValue = this.conversionValueContainer.addContextElement(index, xmlConversionValue);
		this.xmlConverter.getConversionValues().add(index, xmlConversionValue);
		return conversionValue;
	}

	protected XmlConversionValue buildXmlConversionValue() {
		return EclipseLinkOrmFactory.eINSTANCE.createXmlConversionValue();
	}

	public void removeConversionValue(EclipseLinkConversionValue conversionValue) {
		this.removeConversionValue(this.conversionValueContainer.indexOfContextElement((EclipseLinkOrmConversionValue) conversionValue));
	}

	public void removeConversionValue(int index) {
		this.conversionValueContainer.removeContextElement(index);
		this.xmlConverter.getConversionValues().remove(index);
	}

	public void moveConversionValue(int targetIndex, int sourceIndex) {
		this.conversionValueContainer.moveContextElement(targetIndex, sourceIndex);
		this.xmlConverter.getConversionValues().move(targetIndex, sourceIndex);
	}


	protected EclipseLinkOrmConversionValue buildConversionValue(XmlConversionValue xmlConversionValue) {
		return new EclipseLinkOrmConversionValue(this, xmlConversionValue);
	}

	protected void syncConversionValues() {
		this.conversionValueContainer.synchronizeWithResourceModel();
	}

	protected ListIterable<XmlConversionValue> getXmlConversionValues() {
		// clone to reduce chance of concurrency problems
		return IterableTools.cloneLive(this.xmlConverter.getConversionValues());
	}

	protected ContextListContainer<EclipseLinkOrmConversionValue, XmlConversionValue> buildConversionValueContainer() {
		ConversionValueContainer container = new ConversionValueContainer();
		container.initialize();
		return container;
	}

	/**
	 * conversion value container
	 */
	protected class ConversionValueContainer
			extends ContextListContainer<EclipseLinkOrmConversionValue, XmlConversionValue> {
		
		@Override
		protected String getContextElementsPropertyName() {
			return CONVERSION_VALUES_LIST;
		}
		
		@Override
		protected EclipseLinkOrmConversionValue buildContextElement(XmlConversionValue resourceElement) {
			return EclipseLinkOrmObjectTypeConverter.this.buildConversionValue(resourceElement);
		}
		
		@Override
		protected ListIterable<XmlConversionValue> getResourceElements() {
			return EclipseLinkOrmObjectTypeConverter.this.getXmlConversionValues();
		}
		
		@Override
		protected XmlConversionValue getResourceElement(EclipseLinkOrmConversionValue contextElement) {
			return contextElement.getXmlConversionValue();
		}
	}


	// ********** data values **********

	public Iterable<String> getDataValues() {
		return IterableTools.transform(this.getConversionValues(), EclipseLinkConversionValue.DATA_VALUE_TRANSFORMER);
	}

	public int getDataValuesSize() {
		return this.getConversionValuesSize();
	}

	// ********** default object value **********

	public String getDefaultObjectValue() {
		return this.defaultObjectValue;
	}

	public void setDefaultObjectValue(String value) {
		this.setDefaultObjectValue_(value);
		this.xmlConverter.setDefaultObjectValue(value);
	}

	protected void setDefaultObjectValue_(String value) {
		String old = this.defaultObjectValue;
		this.defaultObjectValue = value;
		this.firePropertyChanged(DEFAULT_OBJECT_VALUE_PROPERTY, old, value);
	}


	// ********** misc **********

	public Class<EclipseLinkObjectTypeConverter> getType() {
		return EclipseLinkObjectTypeConverter.class;
	}

	protected boolean typeIsFor(JavaResourceAbstractType type, String typeName) {
		return (type != null) && type.getTypeBinding().getQualifiedName().equals(typeName);
	}

	protected boolean typeIsIn(JavaResourceAbstractType type, IPackageFragment packageFragment) {
		return (type != null) && type.isIn(packageFragment);
	}


	// ********** refactoring **********

	@SuppressWarnings("unchecked")
	public Iterable<ReplaceEdit> createRenameTypeEdits(IType originalType, String newName) {
		return IterableTools.concatenate(
				this.createRenameDataTypeEdits(originalType, newName),
				this.createRenameObjectTypeEdits(originalType, newName)
			);
	}

	protected Iterable<ReplaceEdit> createRenameDataTypeEdits(IType originalType, String newName) {
		return this.dataTypeIsFor(originalType.getFullyQualifiedName('.')) ?
				IterableTools.singletonIterable(this.createRenameDataTypeEdit(originalType, newName)) :
				IterableTools.<ReplaceEdit>emptyIterable();
	}

	protected ReplaceEdit createRenameDataTypeEdit(IType originalType, String newName) {
		return this.xmlConverter.createRenameDataTypeEdit(originalType, newName);
	}

	protected Iterable<ReplaceEdit> createRenameObjectTypeEdits(IType originalType, String newName) {
		return this.objectTypeIsFor(originalType.getFullyQualifiedName('.')) ?
				IterableTools.singletonIterable(this.createRenameObjectTypeEdit(originalType, newName)) :
				IterableTools.<ReplaceEdit>emptyIterable();
	}

	protected ReplaceEdit createRenameObjectTypeEdit(IType originalType, String newName) {
		return this.xmlConverter.createRenameObjectTypeEdit(originalType, newName);
	}

	@SuppressWarnings("unchecked")
	public Iterable<ReplaceEdit> createMoveTypeEdits(IType originalType, IPackageFragment newPackage) {
		return IterableTools.concatenate(
				this.createMoveDataTypeEdits(originalType, newPackage),
				this.createMoveObjectTypeEdits(originalType, newPackage)
			);
	}

	protected Iterable<ReplaceEdit> createMoveDataTypeEdits(IType originalType, IPackageFragment newPackage) {
		return this.dataTypeIsFor(originalType.getFullyQualifiedName('.')) ?
				IterableTools.singletonIterable(this.createRenameDataTypePackageEdit(newPackage.getElementName())) :
				IterableTools.<ReplaceEdit>emptyIterable();
	}

	protected ReplaceEdit createRenameDataTypePackageEdit(String newName) {
		return this.xmlConverter.createRenameDataTypePackageEdit(newName);
	}

	protected Iterable<ReplaceEdit> createMoveObjectTypeEdits(IType originalType, IPackageFragment newPackage) {
		return this.objectTypeIsFor(originalType.getFullyQualifiedName('.')) ?
				IterableTools.singletonIterable(this.createRenameObjectTypePackageEdit(newPackage.getElementName())) :
				IterableTools.<ReplaceEdit>emptyIterable();
	}

	protected ReplaceEdit createRenameObjectTypePackageEdit(String newName) {
		return this.xmlConverter.createRenameObjectTypePackageEdit(newName);
	}

	@SuppressWarnings("unchecked")
	public Iterable<ReplaceEdit> createRenamePackageEdits(IPackageFragment originalPackage, String newName) {
		return IterableTools.concatenate(
				this.createRenameDataTypePackageEdits(originalPackage, newName),
				this.createRenameObjectTypePackageEdits(originalPackage, newName)
			);
	}

	protected Iterable<ReplaceEdit> createRenameDataTypePackageEdits(IPackageFragment originalPackage, String newName) {
		return this.dataTypeIsIn(originalPackage) ?
				IterableTools.singletonIterable(this.createRenameDataTypePackageEdit(newName)) :
				IterableTools.<ReplaceEdit>emptyIterable();
	}

	protected Iterable<ReplaceEdit> createRenameObjectTypePackageEdits(IPackageFragment originalPackage, String newName) {
		return this.objectTypeIsIn(originalPackage) ?
				IterableTools.singletonIterable(this.createRenameObjectTypePackageEdit(newName)) :
				IterableTools.<ReplaceEdit>emptyIterable();
	}


	// ********** validation **********

	@Override
	public void validate(List<IMessage> messages, IReporter reporter) {
		super.validate(messages, reporter);
		this.checkForDuplicateDataValues(messages);
		for (EclipseLinkOrmConversionValue conversionValue : this.getConversionValues()) {
			conversionValue.validate(messages, reporter);
		}
	}

	protected void checkForDuplicateDataValues(List<IMessage> messages) {
		for (ArrayList<EclipseLinkOrmConversionValue> dups : this.mapConversionValuesByDataValue().values()) {
			if (dups.size() > 1) {
				for (EclipseLinkOrmConversionValue dup : dups) {
					messages.add(
						this.buildValidationMessage(
							dup.getDataValueTextRange(),
							JptJpaEclipseLinkCoreValidationMessages.MULTIPLE_OBJECT_VALUES_FOR_DATA_VALUE,
							dup.getDataValue()
						)
					);
				}
			}
		}
	}

	protected HashMap<String, ArrayList<EclipseLinkOrmConversionValue>> mapConversionValuesByDataValue() {
		HashMap<String, ArrayList<EclipseLinkOrmConversionValue>> map = new HashMap<String, ArrayList<EclipseLinkOrmConversionValue>>(this.getConversionValuesSize());
		for (EclipseLinkOrmConversionValue conversionValue : this.getConversionValues()) {
			String dataValue = conversionValue.getDataValue();
			ArrayList<EclipseLinkOrmConversionValue> list = map.get(dataValue);
			if (list == null) {
				list = new ArrayList<EclipseLinkOrmConversionValue>();
				map.put(dataValue, list);
			}
			list.add(conversionValue);
		}
		return map;
	}

	@Override
	public boolean isEquivalentTo(JpaNamedContextModel node) {
		return super.isEquivalentTo(node)
				&& this.isEquivalentTo((EclipseLinkObjectTypeConverter) node);
	}

	protected boolean isEquivalentTo(EclipseLinkObjectTypeConverter converter) {
		return ObjectTools.equals(this.fullyQualifiedObjectType, converter.getFullyQualifiedObjectType()) &&
				ObjectTools.equals(this.fullyQualifiedDataType, converter.getFullyQualifiedDataType()) &&
				ObjectTools.equals(this.defaultObjectValue, converter.getDefaultObjectValue()) &&
				this.conversionValuesAreEquivalentTo(converter);
	}

	protected boolean conversionValuesAreEquivalentTo(EclipseLinkObjectTypeConverter converter) {
		// get fixed lists of the conversion values
		ArrayList<EclipseLinkOrmConversionValue> conversionValues1 = ListTools.list(this.getConversionValues());
		ArrayList<? extends EclipseLinkConversionValue> conversionValues2 = ListTools.list(converter.getConversionValues());
		if (conversionValues1.size() != conversionValues2.size()) {
			return false;
		}
		for (int i = 0; i < conversionValues1.size(); i++) {
			if ( ! conversionValues1.get(i).isEquivalentTo(conversionValues2.get(i))) {
				return false;
			}
		}
		return true;
	}


	// ********** metadata conversion **********

	public void convertFrom(EclipseLinkJavaObjectTypeConverter javaConverter) {
		super.convertFrom(javaConverter);
		this.setDataType(javaConverter.getFullyQualifiedDataType());
		this.setObjectType(javaConverter.getFullyQualifiedObjectType());
		this.setDefaultObjectValue(javaConverter.getDefaultObjectValue());
		for (EclipseLinkJavaConversionValue value : javaConverter.getConversionValues()) {
			this.addConversionValue().convertFrom(value);
		}
	}

	// ********** completion proposals **********

	@Override
	public Iterable<String> getCompletionProposals(int pos) {
		Iterable<String> result = super.getCompletionProposals(pos);
		if (result != null) {
			return result;
		}
		if (this.objectTypeTouches(pos)) {
			return this.getCandidateTypeNames();
		}
		if (this.dataTypeTouches(pos)) {
			return this.getCandidateTypeNames();
		}
		return null;
	}

	protected boolean objectTypeTouches(int pos) {
		return this.xmlConverter.objectTypeTouches(pos);
	}
	
	protected boolean dataTypeTouches(int pos) {
		return this.xmlConverter.dataTypeTouches(pos);
	}

	@SuppressWarnings("unchecked")
	protected Iterable<String> getCandidateTypeNames() {
		return IterableTools.concatenate(
				MappingTools.getPrimaryBasicTypeNamesWithoutPrimitives(),
				MappingTools.getBasicArrayTypeNames(),
				//Add java enums to cover the case where object type is a user defined Enum
				JavaProjectTools.getSortedEnumNames(this.getJavaProject())
				);
	}
	
	protected boolean defaultObjectValueTouches(int pos) {
		return this.xmlConverter.defaultObjectValueTouches(pos);
	}
}
