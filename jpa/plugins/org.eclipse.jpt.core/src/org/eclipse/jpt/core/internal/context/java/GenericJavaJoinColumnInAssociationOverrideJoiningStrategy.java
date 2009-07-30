/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import java.util.ListIterator;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.context.BaseJoinColumn;
import org.eclipse.jpt.core.context.Entity;
import org.eclipse.jpt.core.context.RelationshipMapping;
import org.eclipse.jpt.core.context.TypeMapping;
import org.eclipse.jpt.core.context.java.JavaAssociationOverride;
import org.eclipse.jpt.core.context.java.JavaAssociationOverrideRelationshipReference;
import org.eclipse.jpt.core.context.java.JavaJoinColumn;
import org.eclipse.jpt.core.context.java.JavaJoinColumnInAssociationOverrideJoiningStrategy;
import org.eclipse.jpt.core.context.java.JavaJoinColumn.Owner;
import org.eclipse.jpt.core.internal.resource.java.NullJoinColumnAnnotation;
import org.eclipse.jpt.core.resource.java.AssociationOverrideAnnotation;
import org.eclipse.jpt.core.resource.java.JoinColumnAnnotation;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.db.Table;

public class GenericJavaJoinColumnInAssociationOverrideJoiningStrategy 
	extends AbstractJavaJoinColumnJoiningStrategy
	implements JavaJoinColumnInAssociationOverrideJoiningStrategy
{
	protected transient AssociationOverrideAnnotation associationOverrideAnnotation;
	
	public GenericJavaJoinColumnInAssociationOverrideJoiningStrategy(JavaAssociationOverrideRelationshipReference parent) {
		super(parent);
	}
	
	@Override
	protected Owner buildJoinColumnOwner() {
		return new JoinColumnOwner();
	}
	
	public TypeMapping getTypeMapping() {
		return getAssociationOverride().getOwner().getTypeMapping();
	}
	
	protected JavaAssociationOverride getAssociationOverride() {
		return this.getRelationshipReference().getAssociationOverride();
	}
	
	@Override
	public JavaAssociationOverrideRelationshipReference getRelationshipReference() {
		return (JavaAssociationOverrideRelationshipReference) super.getRelationshipReference();
	}
	
	@Override
	protected ListIterator<JoinColumnAnnotation> joinColumnAnnotations() {
		return this.associationOverrideAnnotation.joinColumns();
	}
	
	@Override
	protected JoinColumnAnnotation buildNullJoinColumnAnnotation() {
		return new NullJoinColumnAnnotation(this.associationOverrideAnnotation);
	}
		
	@Override
	protected JoinColumnAnnotation addAnnotation(int index) {
		return this.associationOverrideAnnotation.addJoinColumn(index);
	}
	
	@Override
	protected void removeAnnotation(int index) {
		this.associationOverrideAnnotation.removeJoinColumn(index);
	}
	
	@Override
	protected void moveAnnotation(int targetIndex, int sourceIndex) {
		this.associationOverrideAnnotation.moveJoinColumn(targetIndex, sourceIndex);
	}
	
	public void initialize(AssociationOverrideAnnotation associationOverride) {
		this.associationOverrideAnnotation = associationOverride;
		super.initialize();
	}
	
	public void update(AssociationOverrideAnnotation associationOverride) {
		this.associationOverrideAnnotation = associationOverride;
		super.update();
	}
	
	public TextRange getValidationTextRange(CompilationUnit astRoot) {
		return this.getRelationshipReference().getAssociationOverride().getValidationTextRange(astRoot);
	}
//	 ********** join column owner adapter **********
	
	protected class JoinColumnOwner
		implements JavaJoinColumn.Owner
	{

		protected JoinColumnOwner() {
			super();
		}

		/**
		 * by default, the join column is in the type mapping's primary table
		 */
		public String getDefaultTableName() {
			return getTypeMapping().getPrimaryTableName();
		}
		
		public String getDefaultColumnName() {
			//built in MappingTools.buildJoinColumnDefaultName()
			return null;
		}
		
		public Entity getTargetEntity() {
			RelationshipMapping relationshipMapping = getRelationshipMapping();
			return relationshipMapping == null ? null : relationshipMapping.getResolvedTargetEntity();
		}

		public String getAttributeName() {
			return GenericJavaJoinColumnInAssociationOverrideJoiningStrategy.this.getRelationshipReference().getAssociationOverride().getName();
		}

		public RelationshipMapping getRelationshipMapping() {
			return GenericJavaJoinColumnInAssociationOverrideJoiningStrategy.this.getRelationshipReference().getAssociationOverride().getOwner().getRelationshipMapping(GenericJavaJoinColumnInAssociationOverrideJoiningStrategy.this.getRelationshipReference().getAssociationOverride().getName());
		}

		public boolean tableNameIsInvalid(String tableName) {
			return getTypeMapping().tableNameIsInvalid(tableName);
		}

		/**
		 * the join column can be on a secondary table
		 */
		public boolean tableIsAllowed() {
			return true;
		}

		public TextRange getValidationTextRange(CompilationUnit astRoot) {
			return null;
		}

		public TypeMapping getTypeMapping() {
			return GenericJavaJoinColumnInAssociationOverrideJoiningStrategy.this.getRelationshipReference().getAssociationOverride().getOwner().getTypeMapping();
		}

		public Table getDbTable(String tableName) {
			return getTypeMapping().getDbTable(tableName);
		}

		public Table getReferencedColumnDbTable() {
			Entity targetEntity = getTargetEntity();
			return (targetEntity == null) ? null : targetEntity.getPrimaryDbTable();
		}
		
		public boolean isVirtual(BaseJoinColumn joinColumn) {
			return false;
		}

		public int joinColumnsSize() {
			return GenericJavaJoinColumnInAssociationOverrideJoiningStrategy.this.getRelationshipReference().getJoinColumnJoiningStrategy().joinColumnsSize();
		}

	}

}
