/*******************************************************************************
 * Copyright (c) 2009, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa1.context.java;

import java.util.List;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAttribute;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.jpa.core.context.NamedColumn;
import org.eclipse.jpt.jpa.core.context.java.JavaAttributeMapping;
import org.eclipse.jpt.jpa.core.context.java.JavaSpecifiedPersistentAttribute;
import org.eclipse.jpt.jpa.core.internal.context.JpaValidator;
import org.eclipse.jpt.jpa.core.internal.context.java.AbstractJavaContextModel;
import org.eclipse.jpt.jpa.core.internal.jpa2.context.OrderColumnValidator;
import org.eclipse.jpt.jpa.core.internal.jpa2.context.java.GenericJavaOrderColumn2_0;
import org.eclipse.jpt.jpa.core.internal.jpa2.resource.java.OrderColumnAnnotationDefinition2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.SpecifiedOrderColumn2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.java.JavaOrderable2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.java.JavaSpecifiedOrderColumn2_0;
import org.eclipse.jpt.jpa.core.jpa2.resource.java.OrderColumnAnnotation2_0;
import org.eclipse.jpt.jpa.core.resource.java.OrderByAnnotation;
import org.eclipse.jpt.jpa.core.validation.JptJpaCoreValidationMessages;
import org.eclipse.jpt.jpa.db.Table;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

/**
 * Java ordering
 * <p>
 * <strong>NB:</strong> Setting any flag to <code>false</code> (or setting the
 * specified "order by" to <code>null</code>) can be a bit unpredictable. The
 * intent is to set a flag to <code>true</code> (or set the specified "order by"
 * to a non-<code>null</code> value).
 * <p>
 * <strong>(JPA 2.0 only) NB:</strong> If both the "order by" and the "order
 * column" annotations are present (which is prohibited by the JPA spec),
 * both are ignored.
 */
public class GenericJavaOrderable
	extends AbstractJavaContextModel<JavaAttributeMapping>
	implements JavaOrderable2_0
{
	protected String specifiedOrderBy;
	protected boolean noOrdering = false;
	protected boolean pkOrdering = false;
	protected boolean customOrdering = false;

	// JPA 1.0
	protected OrderColumnAnnotation2_0 nullOrderColumnAnnotation;

	// JPA 2.0
	protected final JavaOrderable2_0.ParentAdapter parentAdapter;
	protected boolean orderColumnOrdering = false;
	protected final JavaSpecifiedOrderColumn2_0 orderColumn;


	/**
	 * JPA 1.0
	 */
	public GenericJavaOrderable(JavaAttributeMapping parent) {
		this(new JavaOrderable2_0.ParentAdapter.Null(parent));
	}

	/**
	 * JPA 2.0
	 */
	public GenericJavaOrderable(JavaOrderable2_0.ParentAdapter parentAdapter) {
		super(parentAdapter.getOrderableParent());
		this.specifiedOrderBy = this.buildSpecifiedOrderBy();
		this.noOrdering = this.buildNoOrdering();
		this.pkOrdering = this.buildPkOrdering();
		this.customOrdering = this.buildCustomOrdering();

		this.parentAdapter = parentAdapter;
		this.orderColumnOrdering = this.buildOrderColumnOrdering();
		this.orderColumn = this.buildOrderColumn();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();

		this.setSpecifiedOrderBy_(this.buildSpecifiedOrderBy());
		this.setNoOrdering_(this.buildNoOrdering());
		this.setPkOrdering_(this.buildPkOrdering());
		this.setCustomOrdering_(this.buildCustomOrdering());

		this.setOrderColumnOrdering_(this.buildOrderColumnOrdering());
		this.orderColumn.synchronizeWithResourceModel();
	}

	@Override
	public void update() {
		super.update();
		this.orderColumn.update();
	}


	// ********** specified order by **********

	public String getSpecifiedOrderBy() {
		return this.specifiedOrderBy;
	}

	public void setSpecifiedOrderBy(String orderBy) {
		if (orderBy != null) {
			this.removeOrderColumnAnnotation();
			this.getOrderByAnnotationForUpdate().setValue(orderBy);

			this.setSpecifiedOrderBy_(orderBy);
			this.setNoOrdering_(false);
			this.setPkOrdering_(false);
			this.setCustomOrdering_(true);
			this.setOrderColumnOrdering_(false);
		} else {
			this.setNoOrdering(true);  // hmmm...
		}
	}

	protected void setSpecifiedOrderBy_(String orderBy) {
		String old = this.specifiedOrderBy;
		this.specifiedOrderBy = orderBy;
		this.firePropertyChanged(SPECIFIED_ORDER_BY_PROPERTY, old, orderBy);
	}

	protected String buildSpecifiedOrderBy() {
		if (this.orderColumnAnnotationIsPresent()) {
			return null;
		}
		OrderByAnnotation orderByAnnotation = this.getOrderByAnnotation();
		return (orderByAnnotation == null) ? null : orderByAnnotation.getValue();
	}


	// ********** no ordering **********

	public boolean isNoOrdering() {
		return this.noOrdering;
	}

	public void setNoOrdering(boolean noOrdering) {
		if (noOrdering) {
			this.removeOrderColumnAnnotation();
			if (this.getOrderByAnnotation() != null) {
				this.removeOrderByAnnotation();
			}

			this.setSpecifiedOrderBy_(null);
			this.setNoOrdering_(true);
			this.setPkOrdering_(false);
			this.setCustomOrdering_(false);
			this.setOrderColumnOrdering_(false);
		} else {
			this.setPkOrdering(true);  // hmmm...
		}
	}

	protected void setNoOrdering_(boolean noOrdering) {
		boolean old = this.noOrdering;
		this.noOrdering = noOrdering;
		this.firePropertyChanged(NO_ORDERING_PROPERTY, old, noOrdering);
	}

	protected boolean buildNoOrdering() {
		return this.isJpa2_0Compatible() ? this.buildNoOrdering2_0() : this.buildNoOrdering1_0();
	}

	/**
	 * both annotations are missing <em>or</em> both are present
	 */
	protected boolean buildNoOrdering2_0() {
		boolean orderByMissing = (this.getOrderByAnnotation() == null);
		boolean orderByPresent = ! orderByMissing;
		boolean orderColumnMissing = (this.getOrderColumnAnnotation() == null);
		boolean orderColumnPresent = ! orderColumnMissing;
		return (orderByMissing && orderColumnMissing) || (orderByPresent && orderColumnPresent);
	}

	/**
	 * the order-by annotation is missing
	 */
	protected boolean buildNoOrdering1_0() {
		return this.getOrderByAnnotation() == null;
	}


	// ********** pk ordering **********

	public boolean isPkOrdering() {
		return this.pkOrdering;
	}

	public void setPkOrdering(boolean pkOrdering) {
		if (pkOrdering) {
			this.removeOrderColumnAnnotation();
			OrderByAnnotation orderByAnnotation = this.getOrderByAnnotation();
			if (orderByAnnotation == null) {
				this.addOrderByAnnotation();
			} else {
				orderByAnnotation.setValue(null);
			}

			this.setSpecifiedOrderBy_(null);
			this.setNoOrdering_(false);
			this.setPkOrdering_(true);
			this.setCustomOrdering_(false);
			this.setOrderColumnOrdering_(false);
		} else {
			this.setNoOrdering(true);  // hmmm...
		}
	}

	protected void setPkOrdering_(boolean pkOrdering) {
		boolean old = this.pkOrdering;
		this.pkOrdering = pkOrdering;
		this.firePropertyChanged(PK_ORDERING_PROPERTY, old, pkOrdering);
	}

	/**
	 * the order-by annotation is present but no value specified
	 */
	protected boolean buildPkOrdering() {
		if (this.orderColumnAnnotationIsPresent()) {
			return false;
		}
		OrderByAnnotation orderByAnnotation = this.getOrderByAnnotation();
		return (orderByAnnotation != null) && (orderByAnnotation.getValue() == null);
	}


	// ********** custom ordering **********

	public boolean isCustomOrdering() {
		return this.customOrdering;
	}

	public void setCustomOrdering(boolean customOrdering) {
		if (customOrdering) {
			this.setSpecifiedOrderBy(""); //$NON-NLS-1$
		} else {
			this.setNoOrdering(true);  // hmmm...
		}
	}

	protected void setCustomOrdering_(boolean customOrdering) {
		boolean old = this.customOrdering;
		this.customOrdering = customOrdering;
		this.firePropertyChanged(CUSTOM_ORDERING_PROPERTY, old, customOrdering);
	}

	/**
	 * the order-by annotation is present and it has a specified value
	 */
	protected boolean buildCustomOrdering() {
		if (this.orderColumnAnnotationIsPresent()) {
			return false;
		}
		OrderByAnnotation orderByAnnotation = this.getOrderByAnnotation();
		return (orderByAnnotation != null) && (orderByAnnotation.getValue() != null);
	}


	// ********** order column ordering **********

	public boolean isOrderColumnOrdering() {
		return this.orderColumnOrdering;
	}

	public void setOrderColumnOrdering(boolean orderColumnOrdering) {
		if (orderColumnOrdering) {
			if (this.getOrderColumnAnnotation() == null) {
				this.addOrderColumnAnnotation();
			}

			this.setSpecifiedOrderBy_(null);
			this.setNoOrdering_(false);
			this.setPkOrdering_(false);
			this.setCustomOrdering_(false);
			this.setOrderColumnOrdering_(true);
		} else {
			this.setNoOrdering(true);  // hmmm...
		}
	}

	protected void setOrderColumnOrdering_(boolean orderColumnOrdering) {
		boolean old = this.orderColumnOrdering;
		this.orderColumnOrdering = orderColumnOrdering;
		this.firePropertyChanged(ORDER_COLUMN_ORDERING_PROPERTY, old, orderColumnOrdering);
	}

	/**
	 * JPA 2.0 only;
	 * the order column annotation is present <em>and</em>
	 * the order-by annotation is missing
	 */
	protected boolean buildOrderColumnOrdering() {
		return this.orderColumnAnnotationIsPresent() &&
				(this.getOrderByAnnotation() == null);
	}


	// ********** order column **********

	public JavaSpecifiedOrderColumn2_0 getOrderColumn() {
		return this.orderColumn;
	}

	protected JavaSpecifiedOrderColumn2_0 buildOrderColumn() {
		JavaSpecifiedOrderColumn2_0.ParentAdapter columnParentAdapter = new OrderColumnParentAdapter();
		return this.isJpa2_0Compatible() ?
				this.getJpaFactory2_0().buildJavaOrderColumn(columnParentAdapter) :
				new GenericJavaOrderColumn2_0(columnParentAdapter);
	}


	// ********** order by annotation **********

	protected OrderByAnnotation getOrderByAnnotation() {
		return (OrderByAnnotation) this.getResourceAttribute().getAnnotation(OrderByAnnotation.ANNOTATION_NAME);
	}

	protected OrderByAnnotation getOrderByAnnotationForUpdate() {
		OrderByAnnotation annotation = this.getOrderByAnnotation();
		return (annotation != null) ? annotation : this.addOrderByAnnotation();
	}

	protected OrderByAnnotation addOrderByAnnotation() {
		return (OrderByAnnotation) this.getResourceAttribute().addAnnotation(OrderByAnnotation.ANNOTATION_NAME);
	}

	protected void removeOrderByAnnotation() {
		this.getResourceAttribute().removeAnnotation(OrderByAnnotation.ANNOTATION_NAME);
	}


	// ********** order column annotation **********

	protected OrderColumnAnnotation2_0 getOrderColumnAnnotation() {
		return (OrderColumnAnnotation2_0) this.getResourceAttribute().getAnnotation(OrderColumnAnnotation2_0.ANNOTATION_NAME);
	}

	/**
	 * NB: Only return <code>true</code> for JPA 2.0 mappings.
	 */
	protected boolean orderColumnAnnotationIsPresent() {
		return this.isJpa2_0Compatible() && (this.getOrderColumnAnnotation() != null);
	}

	protected OrderColumnAnnotation2_0 addOrderColumnAnnotation() {
		return (OrderColumnAnnotation2_0) this.getResourceAttribute().addAnnotation(OrderColumnAnnotation2_0.ANNOTATION_NAME);
	}

	protected void removeOrderColumnAnnotation() {
		if (this.orderColumnAnnotationIsPresent()) {
			this.removeOrderColumnAnnotation_();
		}
	}

	protected void removeOrderColumnAnnotation_() {
		this.getResourceAttribute().removeAnnotation(OrderColumnAnnotation2_0.ANNOTATION_NAME);
	}

	/**
	 * If we are in a JPA 1.0 project, return a <em>null</em> annotation.
	 */
	public OrderColumnAnnotation2_0 getNonNullOrderColumnAnnotation() {
		// hmmmm...
		return this.isJpa2_0Compatible() ?
				(OrderColumnAnnotation2_0) this.getResourceAttribute().getNonNullAnnotation(OrderColumnAnnotation2_0.ANNOTATION_NAME) :
				this.getNullOrderColumnAnnotation();
	}

	protected OrderColumnAnnotation2_0 getNullOrderColumnAnnotation() {
		if (this.nullOrderColumnAnnotation == null) {
			this.nullOrderColumnAnnotation = this.buildNullOrderColumnAnnotation();
		}
		return this.nullOrderColumnAnnotation;
	}

	protected OrderColumnAnnotation2_0 buildNullOrderColumnAnnotation() {
		// hmmmm...
		return (OrderColumnAnnotation2_0) OrderColumnAnnotationDefinition2_0.instance().buildNullAnnotation(this.getResourceAttribute());
	}


	// ********** misc **********

	protected JavaAttributeMapping getAttributeMapping() {
		return this.parent;
	}

	protected JavaSpecifiedPersistentAttribute getPersistentAttribute() {
		return this.getAttributeMapping().getPersistentAttribute();
	}

	public JavaResourceAttribute getResourceAttribute() {
		return this.getPersistentAttribute().getResourceAttribute();
	}

	// JPA 2.0 only
	public String getDefaultTableName() {
		return this.parentAdapter.getTableName();
	}

	// JPA 2.0 only
	protected Table resolveDbTable(String tableName) {
		return this.parentAdapter.resolveDbTable(tableName);
	}


	// ********** Java completion proposals **********

	@Override
	public Iterable<String> getCompletionProposals(int pos) {
		Iterable<String> result = super.getCompletionProposals(pos);
		if (result != null) {
			return result;
		}

		return this.orderColumn.getCompletionProposals(pos);
	}


	// ********** validation **********

	public TextRange getValidationTextRange() {
		TextRange textRange = this.getOrderByAnnotationTextRange();
		return (textRange != null) ? textRange : this.getAttributeMapping().getValidationTextRange();
	}

	protected TextRange getOrderByAnnotationTextRange() {
		OrderByAnnotation orderByAnnotation = this.getOrderByAnnotation();
		return (orderByAnnotation == null) ? null : orderByAnnotation.getTextRange();
	}

	@Override
	public void validate(List<IMessage> messages, IReporter reporter) {
		super.validate(messages, reporter);
		if (this.orderColumnAnnotationIsPresent() && (this.getOrderByAnnotation() != null)) {
			if (this.getPersistentAttribute().isVirtual()) {
				messages.add(
						this.buildValidationMessage(
							this.getAttributeMapping(),
							this.getPersistentAttribute().getValidationTextRange(),
							JptJpaCoreValidationMessages.ORDER_COLUMN_AND_ORDER_BY_BOTH_SPECIFIED,
							this.getPersistentAttribute().getName()
						)
					);
			}
			else {
				messages.add(
					this.buildValidationMessage(
						this.getAttributeMapping(),
						this.getOrderByAnnotationTextRange(),
						JptJpaCoreValidationMessages.ORDER_COLUMN_AND_ORDER_BY_BOTH_SPECIFIED,
						this.getPersistentAttribute().getName()
					)
				);
			}
		}
		if (this.orderColumnOrdering) {
			//TODO validation message if type is not List
			this.orderColumn.validate(messages, reporter);
		}
	}


	// ********** order column parent adapter (JPA 2.0) **********

	public class OrderColumnParentAdapter
		implements JavaSpecifiedOrderColumn2_0.ParentAdapter
	{
		public JavaOrderable2_0 getColumnParent() {
			return GenericJavaOrderable.this;
		}

		public String getDefaultTableName() {
			return GenericJavaOrderable.this.getDefaultTableName();
		}

		public Table resolveDbTable(String tableName) {
			return GenericJavaOrderable.this.resolveDbTable(tableName);
		}

		public String getDefaultColumnName(NamedColumn column) {
			return this.getPersistentAttribute().getName() + "_ORDER"; //$NON-NLS-1$
		}

		public TextRange getValidationTextRange() {
			return GenericJavaOrderable.this.getValidationTextRange();
		}

		public JpaValidator buildColumnValidator(NamedColumn column) {
			return new OrderColumnValidator(this.getPersistentAttribute(), (SpecifiedOrderColumn2_0) column);
		}

		public OrderColumnAnnotation2_0 getColumnAnnotation() {
			return GenericJavaOrderable.this.getNonNullOrderColumnAnnotation();
		}

		public void removeColumnAnnotation() {
			GenericJavaOrderable.this.removeOrderColumnAnnotation_();
		}

		protected JavaSpecifiedPersistentAttribute getPersistentAttribute() {
			return GenericJavaOrderable.this.getPersistentAttribute();
		}
	}
}
