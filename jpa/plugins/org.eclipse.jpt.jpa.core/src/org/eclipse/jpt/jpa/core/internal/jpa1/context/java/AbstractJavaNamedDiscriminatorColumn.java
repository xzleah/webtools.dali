/*******************************************************************************
 * Copyright (c) 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0, which accompanies this distribution and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa1.context.java;

import org.eclipse.jpt.jpa.core.context.DiscriminatorType;
import org.eclipse.jpt.jpa.core.context.java.JavaNamedDiscriminatorColumn;
import org.eclipse.jpt.jpa.core.context.java.JavaJpaContextNode;
import org.eclipse.jpt.jpa.core.context.java.JavaReadOnlyNamedDiscriminatorColumn;
import org.eclipse.jpt.jpa.core.internal.context.java.AbstractJavaNamedColumn;
import org.eclipse.jpt.jpa.core.resource.java.DiscriminatorColumnAnnotation;

/**
 * Java named discriminator column
 */
public abstract class AbstractJavaNamedDiscriminatorColumn<A extends DiscriminatorColumnAnnotation, O extends JavaReadOnlyNamedDiscriminatorColumn.Owner>
	extends AbstractJavaNamedColumn<A, O>
	implements JavaNamedDiscriminatorColumn
{
	protected DiscriminatorType specifiedDiscriminatorType;
	protected DiscriminatorType defaultDiscriminatorType;

	protected Integer specifiedLength;
	protected int defaultLength;

	protected AbstractJavaNamedDiscriminatorColumn(JavaJpaContextNode parent, O owner) {
		this(parent, owner, null);
	}

	protected AbstractJavaNamedDiscriminatorColumn(JavaJpaContextNode parent, O owner, A columnAnnotation) {
		super(parent, owner, columnAnnotation);
		this.specifiedDiscriminatorType = this.buildSpecifiedDiscriminatorType();
		this.specifiedLength = this.buildSpecifiedLength();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.setSpecifiedDiscriminatorType_(this.buildSpecifiedDiscriminatorType());
		this.setSpecifiedLength_(this.buildSpecifiedLength());
	}

	@Override
	public void update() {
		super.update();
		this.setDefaultDiscriminatorType(this.buildDefaultDiscriminatorType());
		this.setDefaultLength(this.buildDefaultLength());
	}


	// ********** column annotation **********

	@Override
	public abstract A getColumnAnnotation();


	// ********** discriminator type **********

	public DiscriminatorType getDiscriminatorType() {
		return (this.specifiedDiscriminatorType != null) ? this.specifiedDiscriminatorType : this.defaultDiscriminatorType;
	}

	public DiscriminatorType getSpecifiedDiscriminatorType() {
		return this.specifiedDiscriminatorType;
	}

	public void setSpecifiedDiscriminatorType(DiscriminatorType discriminatorType) {
		if (this.valuesAreDifferent(this.specifiedDiscriminatorType, discriminatorType)) {
			this.getColumnAnnotation().setDiscriminatorType(DiscriminatorType.toJavaResourceModel(discriminatorType));
			this.removeColumnAnnotationIfUnset();
			this.setSpecifiedDiscriminatorType_(discriminatorType);
		}
	}

	protected void setSpecifiedDiscriminatorType_(DiscriminatorType discriminatorType) {
		DiscriminatorType old = this.specifiedDiscriminatorType;
		this.specifiedDiscriminatorType = discriminatorType;
		this.firePropertyChanged(SPECIFIED_DISCRIMINATOR_TYPE_PROPERTY, old, discriminatorType);
	}

	protected DiscriminatorType buildSpecifiedDiscriminatorType() {
		return DiscriminatorType.fromJavaResourceModel(this.getColumnAnnotation().getDiscriminatorType());
	}

	public DiscriminatorType getDefaultDiscriminatorType() {
		return this.defaultDiscriminatorType;
	}

	protected void setDefaultDiscriminatorType(DiscriminatorType discriminatorType) {
		DiscriminatorType old = this.defaultDiscriminatorType;
		this.defaultDiscriminatorType = discriminatorType;
		this.firePropertyChanged(DEFAULT_DISCRIMINATOR_TYPE_PROPERTY, old, discriminatorType);
	}

	protected DiscriminatorType buildDefaultDiscriminatorType() {
		return this.owner.getDefaultDiscriminatorType();
	}


	// ********** length **********

	public int getLength() {
		return (this.specifiedLength != null) ? this.specifiedLength.intValue() : this.defaultLength;
	}

	public Integer getSpecifiedLength() {
		return this.specifiedLength;
	}

	public void setSpecifiedLength(Integer length) {
		if (this.valuesAreDifferent(this.specifiedLength, length)) {
			this.getColumnAnnotation().setLength(length);
			this.removeColumnAnnotationIfUnset();
			this.setSpecifiedLength_(length);
		}
	}

	protected void setSpecifiedLength_(Integer length) {
		Integer old = this.specifiedLength;
		this.specifiedLength = length;
		this.firePropertyChanged(SPECIFIED_LENGTH_PROPERTY, old, length);
	}

	protected Integer buildSpecifiedLength() {
		return this.getColumnAnnotation().getLength();
	}

	public int getDefaultLength() {
		return this.defaultLength;
	}

	protected void setDefaultLength(int length) {
		int old = this.defaultLength;
		this.defaultLength = length;
		this.firePropertyChanged(DEFAULT_LENGTH_PROPERTY, old, length);
	}

	protected int buildDefaultLength() {
		return this.owner.getDefaultLength();
	}
}
