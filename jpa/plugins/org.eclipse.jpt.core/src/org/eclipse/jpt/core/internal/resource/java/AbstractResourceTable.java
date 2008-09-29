/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.resource.java;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.utility.jdt.ShortCircuitAnnotationElementAdapter;
import org.eclipse.jpt.core.resource.java.ContainerAnnotation;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.core.resource.java.NestableUniqueConstraint;
import org.eclipse.jpt.core.resource.java.TableAnnotation;
import org.eclipse.jpt.core.resource.java.UniqueConstraintAnnotation;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.core.utility.jdt.AnnotationAdapter;
import org.eclipse.jpt.core.utility.jdt.AnnotationElementAdapter;
import org.eclipse.jpt.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.core.utility.jdt.DeclarationAnnotationElementAdapter;
import org.eclipse.jpt.core.utility.jdt.Member;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.iterators.CloneListIterator;

public abstract class AbstractResourceTable extends AbstractResourceAnnotation<Member> implements TableAnnotation
{
	// hold this so we can get the 'name' text range
	private final DeclarationAnnotationElementAdapter<String> nameDeclarationAdapter;

	// hold this so we can get the 'schema' text range
	private final DeclarationAnnotationElementAdapter<String> schemaDeclarationAdapter;

	// hold this so we can get the 'catalog' text range
	private final DeclarationAnnotationElementAdapter<String> catalogDeclarationAdapter;

	private final AnnotationElementAdapter<String> nameAdapter;

	private final AnnotationElementAdapter<String> schemaAdapter;

	private final AnnotationElementAdapter<String> catalogAdapter;

	private String name;
	
	private String catalog;
	
	private String schema;
	
	final List<NestableUniqueConstraint> uniqueConstraints;
	
	private final UniqueConstraintsContainerAnnotation uniqueConstraintsContainerAnnotation;
	
	protected AbstractResourceTable(JavaResourceNode parent, Member member, DeclarationAnnotationAdapter daa, AnnotationAdapter annotationAdapter) {
		super(parent, member, daa, annotationAdapter);
		this.nameDeclarationAdapter = this.getNameAdapter(daa);
		this.schemaDeclarationAdapter = this.getSchemaAdapter(daa);
		this.catalogDeclarationAdapter = this.getCatalogAdapter(daa);
		this.nameAdapter = buildAnnotationElementAdapter(this.nameDeclarationAdapter);
		this.schemaAdapter = buildAnnotationElementAdapter(this.schemaDeclarationAdapter);
		this.catalogAdapter = buildAnnotationElementAdapter(this.catalogDeclarationAdapter);
		this.uniqueConstraints = new ArrayList<NestableUniqueConstraint>();
		this.uniqueConstraintsContainerAnnotation = new UniqueConstraintsContainerAnnotation();
	}
	
	public void initialize(CompilationUnit astRoot) {
		this.name = this.name(astRoot);
		this.catalog = this.catalog(astRoot);
		this.schema = this.schema(astRoot);
		ContainerAnnotationTools.initializeNestedAnnotations(astRoot, this.uniqueConstraintsContainerAnnotation);
	}
	
	protected AnnotationElementAdapter<String> buildAnnotationElementAdapter(DeclarationAnnotationElementAdapter<String> daea) {
		return new ShortCircuitAnnotationElementAdapter<String>(this.getMember(), daea);
	}

	/**
	 * Build and return a declaration element adapter for the table's 'name' element
	 */
	protected abstract DeclarationAnnotationElementAdapter<String> getNameAdapter(DeclarationAnnotationAdapter declarationAnnotationAdapter);

	/**
	 * Build and return a declaration element adapter for the table's 'schema' element
	 */
	protected abstract DeclarationAnnotationElementAdapter<String> getSchemaAdapter(DeclarationAnnotationAdapter declarationAnnotationAdapter);

	/**
	 * Build and return a declaration element adapter for the table's 'catalog' element
	 */
	protected abstract DeclarationAnnotationElementAdapter<String> getCatalogAdapter(DeclarationAnnotationAdapter declarationAnnotationAdapter);

	/**
	 * Return the uniqueConstraints element name
	 */
	protected abstract String getUniqueConstraintsElementName();
	
	public String getName() {
		return this.name;
	}

	public void setName(String newName) {
		if (attributeValueHasNotChanged(this.name, newName)) {
			return;
		}
		String oldName = this.name;
		this.name = newName;
		this.nameAdapter.setValue(newName);
		firePropertyChanged(NAME_PROPERTY, oldName, newName);
	}

	public String getCatalog() {
		return this.catalog;
	}

	public void setCatalog(String newCatalog) {
		if (attributeValueHasNotChanged(this.catalog, newCatalog)) {
			return;
		}
		String oldCatalog = this.catalog;
		this.catalog = newCatalog;
		this.catalogAdapter.setValue(newCatalog);
		firePropertyChanged(CATALOG_PROPERTY, oldCatalog, newCatalog);
	}

	public String getSchema() {
		return this.schema;
	}

	public void setSchema(String newSchema) {
		if (attributeValueHasNotChanged(this.schema, newSchema)) {
			return;
		}
		String oldSchema = this.schema;
		this.schema = newSchema;
		this.schemaAdapter.setValue(newSchema);
		firePropertyChanged(SCHEMA_PROPERTY, oldSchema, newSchema);
	}
	
	public ListIterator<UniqueConstraintAnnotation> uniqueConstraints() {
		return new CloneListIterator<UniqueConstraintAnnotation>(this.uniqueConstraints);
	}
	
	public int uniqueConstraintsSize() {
		return this.uniqueConstraints.size();
	}
	
	public NestableUniqueConstraint uniqueConstraintAt(int index) {
		return this.uniqueConstraints.get(index);
	}
	
	public int indexOfUniqueConstraint(UniqueConstraintAnnotation uniqueConstraint) {
		return this.uniqueConstraints.indexOf(uniqueConstraint);
	}
	
	public NestableUniqueConstraint addUniqueConstraint(int index) {
		NestableUniqueConstraint uniqueConstraint = (NestableUniqueConstraint) ContainerAnnotationTools.addNestedAnnotation(index, this.uniqueConstraintsContainerAnnotation);
		fireItemAdded(TableAnnotation.UNIQUE_CONSTRAINTS_LIST, index, uniqueConstraint);
		return uniqueConstraint;
	}
	
	protected void addUniqueConstraint(int index, NestableUniqueConstraint uniqueConstraint) {
		addItemToList(index, uniqueConstraint, this.uniqueConstraints, UNIQUE_CONSTRAINTS_LIST);
	}
	
	public void removeUniqueConstraint(int index) {
		NestableUniqueConstraint uniqueConstraint = this.uniqueConstraintAt(index);
		removeUniqueConstraint(uniqueConstraint);
		uniqueConstraint.removeAnnotation();
		ContainerAnnotationTools.synchAnnotationsAfterRemove(index, this.uniqueConstraintsContainerAnnotation);
	}

	protected void removeUniqueConstraint(NestableUniqueConstraint uniqueConstraint) {
		removeItemFromList(uniqueConstraint, this.uniqueConstraints, UNIQUE_CONSTRAINTS_LIST);
	}
	
	public void moveUniqueConstraint(int targetIndex, int sourceIndex) {
		moveUniqueConstraintInternal(targetIndex, sourceIndex);
		ContainerAnnotationTools.synchAnnotationsAfterMove(targetIndex, sourceIndex, this.uniqueConstraintsContainerAnnotation);
		fireItemMoved(TableAnnotation.UNIQUE_CONSTRAINTS_LIST, targetIndex, sourceIndex);
	}
	
	protected void moveUniqueConstraintInternal(int targetIndex, int sourceIndex) {
		CollectionTools.move(this.uniqueConstraints, targetIndex, sourceIndex);
	}
	
	protected abstract NestableUniqueConstraint createUniqueConstraint(int index);

	public TextRange getNameTextRange(CompilationUnit astRoot) {
		return getElementTextRange(this.nameDeclarationAdapter, astRoot);
	}
	
	public TextRange getSchemaTextRange(CompilationUnit astRoot) {
		return getElementTextRange(this.schemaDeclarationAdapter, astRoot);
	}
	
	public TextRange getCatalogTextRange(CompilationUnit astRoot) {
		return getElementTextRange(this.catalogDeclarationAdapter, astRoot);
	}
	
	public boolean nameTouches(int pos, CompilationUnit astRoot) {
		return this.elementTouches(this.nameDeclarationAdapter, pos, astRoot);
	}

	public boolean catalogTouches(int pos, CompilationUnit astRoot) {
		return this.elementTouches(this.catalogDeclarationAdapter, pos, astRoot);
	}
	
	public boolean schemaTouches(int pos, CompilationUnit astRoot) {
		return this.elementTouches(this.schemaDeclarationAdapter, pos, astRoot);
	}
	
	public void update(CompilationUnit astRoot) {
		this.setName(this.name(astRoot));
		this.setSchema(this.schema(astRoot));
		this.setCatalog(this.catalog(astRoot));
		this.updateUniqueConstraintsFromJava(astRoot);
	}
	
	protected String name(CompilationUnit astRoot) {
		return this.nameAdapter.getValue(astRoot);
	}
	
	protected String schema(CompilationUnit astRoot) {
		return this.schemaAdapter.getValue(astRoot);
	}
	
	protected String catalog(CompilationUnit astRoot) {
		return this.catalogAdapter.getValue(astRoot);
	}
	
	/**
	 * here we just worry about getting the unique constraints lists the same size;
	 * then we delegate to the unique constraints to synch themselves up
	 */
	private void updateUniqueConstraintsFromJava(CompilationUnit astRoot) {
		ContainerAnnotationTools.updateNestedAnnotationsFromJava(astRoot, this.uniqueConstraintsContainerAnnotation);
	}

	
	private class UniqueConstraintsContainerAnnotation extends AbstractJavaResourceNode 
		implements ContainerAnnotation<NestableUniqueConstraint> 
	{
		public UniqueConstraintsContainerAnnotation() {
			super(AbstractResourceTable.this);
		}
		
		public void initialize(CompilationUnit astRoot) {
			//nothing to initialize
		}

		public NestableUniqueConstraint addInternal(int index) {
			NestableUniqueConstraint uniqueConstraint = AbstractResourceTable.this.createUniqueConstraint(index);
			AbstractResourceTable.this.uniqueConstraints.add(index, uniqueConstraint);			
			return uniqueConstraint;
		}
		
		public NestableUniqueConstraint add(int index) {
			NestableUniqueConstraint uniqueConstraint = AbstractResourceTable.this.createUniqueConstraint(index);
			AbstractResourceTable.this.addUniqueConstraint(index, uniqueConstraint);
			return uniqueConstraint;
		}
		
		public String getAnnotationName() {
			return AbstractResourceTable.this.getAnnotationName();
		}

		public String getNestableAnnotationName() {
			return JPA.UNIQUE_CONSTRAINT;
		}

		public int indexOf(NestableUniqueConstraint uniqueConstraint) {
			return AbstractResourceTable.this.indexOfUniqueConstraint(uniqueConstraint);
		}

		public void move(int targetIndex, int sourceIndex) {
			AbstractResourceTable.this.moveUniqueConstraint(targetIndex, sourceIndex);
		}
		
		public void moveInternal(int targetIndex, int sourceIndex) {
			AbstractResourceTable.this.moveUniqueConstraintInternal(targetIndex, sourceIndex);
		}

		public NestableUniqueConstraint nestedAnnotationAt(int index) {
			return AbstractResourceTable.this.uniqueConstraintAt(index);
		}

		public NestableUniqueConstraint nestedAnnotationFor(Annotation jdtAnnotation) {
			for (NestableUniqueConstraint uniqueConstraint : CollectionTools.iterable(nestedAnnotations())) {
				if (jdtAnnotation == uniqueConstraint.getJdtAnnotation((CompilationUnit) jdtAnnotation.getRoot())) {
					return uniqueConstraint;
				}
			}
			return null;
		}

		public ListIterator<NestableUniqueConstraint> nestedAnnotations() {
			return new CloneListIterator<NestableUniqueConstraint>(AbstractResourceTable.this.uniqueConstraints);
		}

		public int nestedAnnotationsSize() {
			return AbstractResourceTable.this.uniqueConstraintsSize();
		}

		public void remove(NestableUniqueConstraint uniqueConstraint) {
			AbstractResourceTable.this.removeUniqueConstraint(uniqueConstraint);	
		}

		public void remove(int index) {
			this.remove(nestedAnnotationAt(index));
		}

		public Annotation getJdtAnnotation(CompilationUnit astRoot) {
			return AbstractResourceTable.this.getJdtAnnotation(astRoot);
		}

		public void newAnnotation() {
			AbstractResourceTable.this.newAnnotation();
		}

		public void removeAnnotation() {
			AbstractResourceTable.this.removeAnnotation();
		}

		public void update(CompilationUnit astRoot) {
			AbstractResourceTable.this.update(astRoot);
		}
		
		public TextRange getTextRange(CompilationUnit astRoot) {
			return AbstractResourceTable.this.getTextRange(astRoot);
		}
		
		public String getElementName() {
			return AbstractResourceTable.this.getUniqueConstraintsElementName();
		}

		@Override
		public void toString(StringBuilder sb) {
			sb.append(this.getAnnotationName());
		}

	}
}
