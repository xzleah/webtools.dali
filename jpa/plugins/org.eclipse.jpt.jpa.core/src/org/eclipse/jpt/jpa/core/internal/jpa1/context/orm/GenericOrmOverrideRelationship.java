/*******************************************************************************
 * Copyright (c) 2009, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa1.context.orm;

import java.util.List;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.jpa.core.context.BaseColumn;
import org.eclipse.jpt.jpa.core.context.Entity;
import org.eclipse.jpt.jpa.core.context.JoinColumn;
import org.eclipse.jpt.jpa.core.context.JoinColumnRelationship;
import org.eclipse.jpt.jpa.core.context.JoinTable;
import org.eclipse.jpt.jpa.core.context.JoinTableRelationship;
import org.eclipse.jpt.jpa.core.context.MappedByRelationship;
import org.eclipse.jpt.jpa.core.context.OverrideRelationship;
import org.eclipse.jpt.jpa.core.context.Relationship;
import org.eclipse.jpt.jpa.core.context.RelationshipMapping;
import org.eclipse.jpt.jpa.core.context.SpecifiedOverrideRelationship;
import org.eclipse.jpt.jpa.core.context.SpecifiedRelationship;
import org.eclipse.jpt.jpa.core.context.SpecifiedRelationshipStrategy;
import org.eclipse.jpt.jpa.core.context.TableColumn;
import org.eclipse.jpt.jpa.core.context.TypeMapping;
import org.eclipse.jpt.jpa.core.context.orm.OrmSpecifiedAssociationOverride;
import org.eclipse.jpt.jpa.core.context.orm.OrmSpecifiedJoinColumnRelationshipStrategy;
import org.eclipse.jpt.jpa.core.context.orm.OrmSpecifiedJoinTableRelationshipStrategy;
import org.eclipse.jpt.jpa.core.internal.context.JpaValidator;
import org.eclipse.jpt.jpa.core.internal.context.orm.AbstractOrmXmlContextModel;
import org.eclipse.jpt.jpa.core.internal.context.orm.GenericOrmOverrideJoinColumnRelationshipStrategy;
import org.eclipse.jpt.jpa.core.internal.context.orm.NullOrmJoinTableRelationshipStrategy;
import org.eclipse.jpt.jpa.core.internal.jpa2.context.orm.GenericOrmOverrideJoinTableRelationshipStrategy2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.SpecifiedMappingRelationshipStrategy2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.orm.OrmSpecifiedAssociationOverride2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.orm.OrmSpecifiedOverrideRelationship2_0;
import org.eclipse.jpt.jpa.core.resource.orm.XmlAssociationOverride;
import org.eclipse.jpt.jpa.db.Table;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

public class GenericOrmOverrideRelationship
	extends AbstractOrmXmlContextModel<OrmSpecifiedAssociationOverride>
	implements OrmSpecifiedOverrideRelationship2_0
{
	protected SpecifiedRelationshipStrategy strategy;

	protected final OrmSpecifiedJoinColumnRelationshipStrategy joinColumnStrategy;

	// JPA 2.0
	protected final OrmSpecifiedJoinTableRelationshipStrategy joinTableStrategy;


	public GenericOrmOverrideRelationship(OrmSpecifiedAssociationOverride parent) {
		super(parent);
		this.joinColumnStrategy = this.buildJoinColumnStrategy();
		this.joinTableStrategy = this.buildJoinTableStrategy();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.joinColumnStrategy.synchronizeWithResourceModel();
		this.joinTableStrategy.synchronizeWithResourceModel();
	}

	@Override
	public void update() {
		super.update();
		this.setStrategy(this.buildStrategy());
		this.joinColumnStrategy.update();
		this.joinTableStrategy.update();
	}


	// ********** strategy **********

	public SpecifiedRelationshipStrategy getStrategy() {
		return this.strategy;
	}

	protected void setStrategy(SpecifiedRelationshipStrategy strategy) {
		SpecifiedRelationshipStrategy old = this.strategy;
		this.strategy = strategy;
		this.firePropertyChanged(STRATEGY_PROPERTY, old, strategy);
	}

	protected SpecifiedRelationshipStrategy buildStrategy() {
		return this.isJpa2_0Compatible() ?
				this.buildStrategy2_0() :
				this.joinColumnStrategy;
	}

	/**
	 * The overridden mapping determines the override's strategy.
	 */
	protected SpecifiedRelationshipStrategy buildStrategy2_0() {
		SpecifiedMappingRelationshipStrategy2_0 mappingStrategy = this.getMappingStrategy();
		return (mappingStrategy != null) ?
				(SpecifiedRelationshipStrategy) mappingStrategy.selectOverrideStrategy(this) :
				this.buildMissingMappingStrategy();
	}

	/**
	 * Get the strategy from the overridden mapping.
	 */
	protected SpecifiedMappingRelationshipStrategy2_0 getMappingStrategy() {
		RelationshipMapping mapping = this.getMapping();
		return (mapping == null) ? null : (SpecifiedMappingRelationshipStrategy2_0) mapping.getRelationship().getStrategy();
	}

	/**
	 * Return the strategy to use when the override's name does not match the
	 * name of an appropriate relationship mapping.
	 */
	protected SpecifiedRelationshipStrategy buildMissingMappingStrategy() {
		return this.joinColumnStrategy.hasSpecifiedJoinColumns() ?
				this.joinColumnStrategy :
				this.joinTableStrategy;
	}


	// ********** join column strategy **********

	public OrmSpecifiedJoinColumnRelationshipStrategy getJoinColumnStrategy() {
		return this.joinColumnStrategy;
	}

	public boolean strategyIsJoinColumn() {
		return this.strategy == this.joinColumnStrategy;
	}

	public void setStrategyToJoinColumn() {
		this.joinColumnStrategy.addStrategy();
		this.joinTableStrategy.removeStrategy();
	}

	public boolean mayHaveDefaultJoinColumn() {
		// association overrides do not have defaults
		return false;
	}

	protected OrmSpecifiedJoinColumnRelationshipStrategy buildJoinColumnStrategy() {
		return new GenericOrmOverrideJoinColumnRelationshipStrategy(this);
	}


	// ********** join table strategy **********

	public OrmSpecifiedJoinTableRelationshipStrategy getJoinTableStrategy() {
		return this.joinTableStrategy;
	}

	public boolean strategyIsJoinTable() {
		return this.strategy == this.joinTableStrategy;
	}

	public void setStrategyToJoinTable() {
		this.joinTableStrategy.addStrategy();
		this.joinColumnStrategy.removeStrategy();
	}

	public boolean mayHaveDefaultJoinTable() {
		// association overrides do not have defaults
		return false;
	}

	protected OrmSpecifiedJoinTableRelationshipStrategy buildJoinTableStrategy() {
		return this.isJpa2_0Compatible() ?
				new GenericOrmOverrideJoinTableRelationshipStrategy2_0(this) :
				new NullOrmJoinTableRelationshipStrategy(this);
	}


	// ********** conversions **********

	public void initializeFrom(Relationship oldRelationship) {
		oldRelationship.initializeOn(this);
	}

	public void initializeOn(SpecifiedRelationship newRelationship) {
		newRelationship.initializeFromJoinTableRelationship(this);
		newRelationship.initializeFromJoinColumnRelationship(this);
	}

	public void initializeFromMappedByRelationship(MappedByRelationship oldRelationship) {
		// NOP
	}

	public void initializeFromJoinTableRelationship(JoinTableRelationship oldRelationship) {
		this.joinTableStrategy.initializeFrom(oldRelationship.getJoinTableStrategy());
	}

	public void initializeFromJoinColumnRelationship(JoinColumnRelationship oldRelationship) {
		this.joinColumnStrategy.initializeFrom(oldRelationship.getJoinColumnStrategy());
	}

	public void initializeFromVirtual(OverrideRelationship virtualRelationship) {
		virtualRelationship.initializeOnSpecified(this);
	}

	public void initializeOnSpecified(SpecifiedOverrideRelationship specifiedRelationship) {
		throw new UnsupportedOperationException();
	}

	public void initializeFromVirtualJoinTableRelationship(JoinTableRelationship virtualRelationship) {
		this.joinTableStrategy.initializeFromVirtual(virtualRelationship.getJoinTableStrategy());
	}

	public void initializeFromVirtualJoinColumnRelationship(JoinColumnRelationship virtualRelationship) {
		this.joinColumnStrategy.initializeFromVirtual(virtualRelationship.getJoinColumnStrategy());
	}


	// ********** misc **********

	protected OrmSpecifiedAssociationOverride getAssociationOverride() {
		return this.parent;
	}

	protected OrmSpecifiedAssociationOverride2_0 getAssociationOverride2_0() {
		return (OrmSpecifiedAssociationOverride2_0) this.getAssociationOverride();
	}

	public XmlAssociationOverride getXmlContainer() {
		return this.getAssociationOverride().getXmlOverride();
	}

	public TypeMapping getTypeMapping() {
		return this.getAssociationOverride().getTypeMapping();
	}

	public String getAttributeName() {
		return this.getAssociationOverride().getName();
	}

	public boolean tableNameIsInvalid(String tableName) {
		return this.getAssociationOverride().tableNameIsInvalid(tableName);
	}

	public Iterable<String> getCandidateTableNames() {
		return this.getAssociationOverride().getCandidateTableNames();
	}

	public Table resolveDbTable(String tableName) {
		return this.getAssociationOverride().resolveDbTable(tableName);
	}

	public String getDefaultTableName() {
		return this.getAssociationOverride().getDefaultTableName();
	}

	public JpaValidator buildColumnValidator(BaseColumn column, TableColumn.ParentAdapter parentAdapter) {
		return this.getAssociationOverride().buildColumnValidator(column, parentAdapter);
	}

	public Entity getEntity() {
		TypeMapping typeMapping = this.getTypeMapping();
		return (typeMapping instanceof Entity) ? (Entity) typeMapping : null;
	}

	public boolean isVirtual() {
		return false;
	}

	public RelationshipMapping getMapping() {
		return this.getAssociationOverride().getMapping();
	}


	// ********** validation **********

	public TextRange getValidationTextRange() {
		return this.getAssociationOverride().getValidationTextRange();
	}

	@Override
	public void validate(List<IMessage> messages, IReporter reporter) {
		super.validate(messages, reporter);
		// prevent NPE on JPA 2_0 platforms
		// this.strategy == null when the mapping relationship strategy, e.g. mapped-by, cannot be overridden
		if (this.strategy != null) {
			this.strategy.validate(messages, reporter);
		}
	}

	public JpaValidator buildJoinTableValidator(JoinTable table) {
		return this.getAssociationOverride2_0().buildJoinTableValidator(table);
	}

	public JpaValidator buildJoinTableJoinColumnValidator(JoinColumn column, JoinColumn.ParentAdapter parentAdapter) {
		return this.getAssociationOverride2_0().buildJoinTableJoinColumnValidator(column, parentAdapter);
	}

	public JpaValidator buildJoinTableInverseJoinColumnValidator(JoinColumn column, JoinColumn.ParentAdapter parentAdapter) {
		return this.getAssociationOverride2_0().buildJoinTableInverseJoinColumnValidator(column, parentAdapter);
	}

	// ********** completion proposals **********

	@Override
	public Iterable<String> getCompletionProposals(int pos) {
		Iterable<String> result = super.getCompletionProposals(pos);
		if (result != null) {
			return result;
		}

		return this.strategy == null ? null : this.strategy.getCompletionProposals(pos);
	}
}
