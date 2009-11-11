/*******************************************************************************
 * Copyright (c) 2006, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.context.FetchType;
import org.eclipse.jpt.core.context.java.JavaMultiRelationshipMapping;
import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.internal.context.MappingTools;
import org.eclipse.jpt.core.jpa2.context.java.JavaPersistentAttribute2_0;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.MapKeyAnnotation;
import org.eclipse.jpt.core.resource.java.OrderByAnnotation;
import org.eclipse.jpt.core.resource.java.RelationshipMappingAnnotation;
import org.eclipse.jpt.utility.Filter;
import org.eclipse.jpt.utility.internal.ArrayTools;
import org.eclipse.jpt.utility.internal.StringTools;
import org.eclipse.jpt.utility.internal.iterators.FilteringIterator;

/**
 * Java multi-relationship (m:m, 1:m) mapping
 */
public abstract class AbstractJavaMultiRelationshipMapping<T extends RelationshipMappingAnnotation>
	extends AbstractJavaRelationshipMapping<T> 
	implements JavaMultiRelationshipMapping
{
	protected String specifiedOrderBy = null;
	protected boolean noOrdering = false;
	protected boolean pkOrdering = false;
	protected boolean customOrdering = false;

	protected String specifiedMapKey;
	protected boolean noMapKey = false;
	protected boolean pkMapKey = false;
	protected boolean customMapKey = false;


	protected AbstractJavaMultiRelationshipMapping(JavaPersistentAttribute parent) {
		super(parent);
	}

	@Override
	protected void initialize() {
		super.initialize();
		this.initializeOrderBy();
		this.initializeMapKey();
	}

	@Override
	protected void update() {
		super.update();
		this.updateOrderBy();
		this.updateMapKey();
	}

	// ********** AbstractJavaAttributeMapping implementation **********  

	@Override
	protected String[] buildSupportingAnnotationNames() {
		return ArrayTools.addAll(
			super.buildSupportingAnnotationNames(),
			JPA.JOIN_TABLE,
			JPA.MAP_KEY,
			JPA.ORDER_BY
			);
	}

	// ********** AbstractJavaRelationshipMapping implementation **********  

	@Override
	protected String buildDefaultTargetEntity() {
		return this.getPersistentAttribute().getMultiReferenceEntityTypeName();
	}


	// ********** order by **********  

	public String getOrderBy() {
		if (this.noOrdering) {
			return null;
		}
		if (this.pkOrdering) {
			return this.getTargetEntityIdAttributeName();
		}
		if (this.customOrdering) {
			return this.specifiedOrderBy;
		}
		throw new IllegalStateException("unknown ordering"); //$NON-NLS-1$
	}

	public String getSpecifiedOrderBy() {
		return this.specifiedOrderBy;
	}

	public void setSpecifiedOrderBy(String orderBy) {
		String old = this.specifiedOrderBy;
		this.specifiedOrderBy = orderBy;
		OrderByAnnotation orderByAnnotation = this.getOrderByAnnotation();
		if (orderBy == null) {
			if (orderByAnnotation != null) { 
				this.removeOrderByAnnotation();
			}
		} else {
			if (orderByAnnotation == null) {
				orderByAnnotation = this.addOrderByAnnotation();
			}
			orderByAnnotation.setValue(orderBy);
		}
		this.firePropertyChanged(SPECIFIED_ORDER_BY_PROPERTY, old, orderBy);
	}

	protected void setSpecifiedOrderBy_(String orderBy) {
		String old = this.specifiedOrderBy;
		this.specifiedOrderBy = orderBy;
		this.firePropertyChanged(SPECIFIED_ORDER_BY_PROPERTY, old, orderBy);
	}

	protected void initializeOrderBy() {
		OrderByAnnotation orderByAnnotation = this.getOrderByAnnotation();
		if (orderByAnnotation == null) {
			this.noOrdering = true;
		} else {
			this.specifiedOrderBy = orderByAnnotation.getValue();
			if (this.specifiedOrderBy == null) {
				this.pkOrdering = true;
			} else {
				this.customOrdering = true;
			}
		}
	}

	protected void updateOrderBy() {
		OrderByAnnotation orderByAnnotation = this.getOrderByAnnotation();
		if (orderByAnnotation == null) {
			this.setSpecifiedOrderBy_(null);
			this.setNoOrdering_(true);
			this.setPkOrdering_(false);
			this.setCustomOrdering_(false);
		} else {
			String ob = orderByAnnotation.getValue();
			this.setSpecifiedOrderBy_(ob);
			this.setNoOrdering_(false);
			this.setPkOrdering_(ob == null);
			this.setCustomOrdering_(ob != null);
		}
	}

	protected OrderByAnnotation getOrderByAnnotation() {
		return (OrderByAnnotation) this.getResourcePersistentAttribute().getAnnotation(OrderByAnnotation.ANNOTATION_NAME);
	}

	protected OrderByAnnotation addOrderByAnnotation() {
		return (OrderByAnnotation) this.getResourcePersistentAttribute().addAnnotation(OrderByAnnotation.ANNOTATION_NAME);
	}

	protected void removeOrderByAnnotation() {
		this.getResourcePersistentAttribute().removeAnnotation(OrderByAnnotation.ANNOTATION_NAME);
	}


	// ********** no ordering **********  

	public boolean isNoOrdering() {
		return this.noOrdering;
	}

	public void setNoOrdering(boolean noOrdering) {
		boolean old = this.noOrdering;
		this.noOrdering = noOrdering;
		if (noOrdering) {
			if (this.getOrderByAnnotation() != null) {
				this.removeOrderByAnnotation();
			}
		} else {
			// the 'noOrdering' flag is cleared as a
			// side-effect of setting the other flags,
			// via a call to #setNoOrdering_(boolean)
		}
		this.firePropertyChanged(NO_ORDERING_PROPERTY, old, noOrdering);
	}

	protected void setNoOrdering_(boolean noOrdering) {
		boolean old = this.noOrdering;
		this.noOrdering = noOrdering;
		this.firePropertyChanged(NO_ORDERING_PROPERTY, old, noOrdering);	
	}


	// ********** pk ordering **********  

	public boolean isPkOrdering() {
		return this.pkOrdering;
	}

	public void setPkOrdering(boolean pkOrdering) {
		boolean old = this.pkOrdering;
		this.pkOrdering = pkOrdering;
		OrderByAnnotation orderByAnnotation = this.getOrderByAnnotation();
		if (pkOrdering) {
			if (orderByAnnotation == null) {
				this.addOrderByAnnotation();
			} else {
				orderByAnnotation.setValue(null);
			}
		} else {
			// the 'pkOrdering' flag is cleared as a
			// side-effect of setting the other flags,
			// via a call to #setPkOrdering_(boolean)
		}
		this.firePropertyChanged(PK_ORDERING_PROPERTY, old, pkOrdering);
	}

	protected void setPkOrdering_(boolean pkOrdering) {
		boolean old = this.pkOrdering;
		this.pkOrdering = pkOrdering;
		this.firePropertyChanged(PK_ORDERING_PROPERTY, old, pkOrdering);
	}


	// ********** custom ordering **********  

	public boolean isCustomOrdering() {
		return this.customOrdering;
	}

	public void setCustomOrdering(boolean customOrdering) {
		boolean old = this.customOrdering;
		this.customOrdering = customOrdering;
		if (customOrdering) {
			this.setSpecifiedOrderBy(""); //$NON-NLS-1$
		} else {
			// the 'customOrdering' flag is cleared as a
			// side-effect of setting the other flags,
			// via a call to #setCustomOrdering_(boolean)
		}
		this.firePropertyChanged(CUSTOM_ORDERING_PROPERTY, old, customOrdering);
	}

	protected void setCustomOrdering_(boolean customOrdering) {
		boolean old = this.customOrdering;
		this.customOrdering = customOrdering;
		this.firePropertyChanged(CUSTOM_ORDERING_PROPERTY, old, customOrdering);
	}


	// ********** Fetchable implementation **********  

	public FetchType getDefaultFetch() {
		return DEFAULT_FETCH_TYPE;
	}


	// ********** map key **********  

	public String getMapKey() {
		if (this.noMapKey) {
			return null;
		}
		if (this.pkMapKey) {
			return this.getTargetEntityIdAttributeName();
		}
		if (this.customMapKey) {
			return this.specifiedMapKey;
		}
		throw new IllegalStateException("unknown map key"); //$NON-NLS-1$
	}

	public String getSpecifiedMapKey() {
		return this.specifiedMapKey;
	}

	public void setSpecifiedMapKey(String mapKey) {
		String old = this.specifiedMapKey;
		this.specifiedMapKey = mapKey;
		MapKeyAnnotation mapKeyAnnotation = this.getMapKeyAnnotation();
		if (mapKey == null) {
			if (mapKeyAnnotation != null) {
				this.removeMapKeyAnnotation();
			}
		} else {
			if (mapKeyAnnotation == null) {
				mapKeyAnnotation = this.addMapKeyAnnotation();
			}
			mapKeyAnnotation.setName(mapKey);
		}
		this.firePropertyChanged(SPECIFIED_MAP_KEY_PROPERTY, old, mapKey);
	}

	protected void setSpecifiedMapKey_(String mapKey) {
		String old = this.specifiedMapKey;
		this.specifiedMapKey = mapKey;
		this.firePropertyChanged(SPECIFIED_MAP_KEY_PROPERTY, old, mapKey);
	}

	protected void initializeMapKey() {
		MapKeyAnnotation mapKeyAnnotation = this.getMapKeyAnnotation();
		if (mapKeyAnnotation == null) {
			this.noMapKey = true;
		} else {
			this.specifiedMapKey = mapKeyAnnotation.getName();
			if (this.specifiedMapKey == null) {
				this.pkMapKey = true;
			} else {
				this.customMapKey = true;
			}
		}
	}

	protected void updateMapKey() {
		MapKeyAnnotation mapKeyAnnotation = this.getMapKeyAnnotation();
		if (mapKeyAnnotation == null) {
			this.setSpecifiedMapKey_(null);
			this.setNoMapKey_(true);
			this.setPkMapKey_(false);
			this.setCustomMapKey_(false);
		} else {
			String mk = mapKeyAnnotation.getName();
			this.setSpecifiedMapKey_(mk);
			this.setNoMapKey_(false);
			this.setPkMapKey_(mk == null);
			this.setCustomMapKey_(mk != null);
		}
	}

	protected MapKeyAnnotation getMapKeyAnnotation() {
		return (MapKeyAnnotation) this.getResourcePersistentAttribute().getAnnotation(MapKeyAnnotation.ANNOTATION_NAME);
	}

	protected MapKeyAnnotation addMapKeyAnnotation() {
		return (MapKeyAnnotation) this.getResourcePersistentAttribute().addAnnotation(MapKeyAnnotation.ANNOTATION_NAME);
	}

	protected void removeMapKeyAnnotation() {
		this.getResourcePersistentAttribute().removeAnnotation(MapKeyAnnotation.ANNOTATION_NAME);
	}

	protected boolean mapKeyNameTouches(int pos, CompilationUnit astRoot) {
		MapKeyAnnotation mapKeyAnnotation = this.getMapKeyAnnotation();
		return (mapKeyAnnotation != null) && mapKeyAnnotation.nameTouches(pos, astRoot);
	}


	// ********** no map key **********  

	public boolean isNoMapKey() {
		return this.noMapKey;
	}

	public void setNoMapKey(boolean noMapKey) {
		boolean old = this.noMapKey;
		this.noMapKey = noMapKey;
		if (noMapKey) {
			if (this.getMapKeyAnnotation() != null) {
				this.removeMapKeyAnnotation();
			}
		} else {
			// the 'noMapKey' flag is cleared as a
			// side-effect of setting the other flags,
			// via a call to #setNoMapKey_(boolean)
		}
		this.firePropertyChanged(NO_MAP_KEY_PROPERTY, old, noMapKey);
	}

	protected void setNoMapKey_(boolean noMapKey) {
		boolean old = this.noMapKey;
		this.noMapKey = noMapKey;
		this.firePropertyChanged(NO_MAP_KEY_PROPERTY, old, noMapKey);	
	}


	// ********** pk map key **********  

	public boolean isPkMapKey() {
		return this.pkMapKey;
	}

	public void setPkMapKey(boolean pkMapKey) {
		boolean old = this.pkMapKey;
		this.pkMapKey = pkMapKey;
		MapKeyAnnotation mapKeyAnnotation = this.getMapKeyAnnotation();
		if (pkMapKey) {
			if (mapKeyAnnotation == null) {
				this.addMapKeyAnnotation();
			} else {
				mapKeyAnnotation.setName(null);
			}
		} else {
			// the 'pkMapKey' flag is cleared as a
			// side-effect of setting the other flags,
			// via a call to #setPkMapKey_(boolean)
		}
		this.firePropertyChanged(PK_MAP_KEY_PROPERTY, old, pkMapKey);
	}

	protected void setPkMapKey_(boolean pkMapKey) {
		boolean old = this.pkMapKey;
		this.pkMapKey = pkMapKey;
		this.firePropertyChanged(PK_MAP_KEY_PROPERTY, old, pkMapKey);
	}


	// ********** custom map key **********  

	public boolean isCustomMapKey() {
		return this.customMapKey;
	}

	public void setCustomMapKey(boolean customMapKey) {
		boolean old = this.customMapKey;
		this.customMapKey = customMapKey;
		if (customMapKey) {
			this.setSpecifiedMapKey(""); //$NON-NLS-1$
		} else {
			// the 'customMapKey' flag is cleared as a
			// side-effect of setting the other flags,
			// via a call to #setCustomMapKey_(boolean)
		}
		this.firePropertyChanged(CUSTOM_MAP_KEY_PROPERTY, old, customMapKey);
	}

	protected void setCustomMapKey_(boolean customMapKey) {
		boolean old = this.customMapKey;
		this.customMapKey = customMapKey;
		this.firePropertyChanged(CUSTOM_MAP_KEY_PROPERTY, old, customMapKey);
	}


	// ********** Java completion proposals **********  

	@Override
	public Iterator<String> javaCompletionProposals(int pos, Filter<String> filter, CompilationUnit astRoot) {
		Iterator<String> result = super.javaCompletionProposals(pos, filter, astRoot);
		if (result != null) {
			return result;
		}
		if (this.mapKeyNameTouches(pos, astRoot)) {
			return this.javaCandidateMapKeyNames(filter);
		}
		return null;
	}

	protected Iterator<String> javaCandidateMapKeyNames(Filter<String> filter) {
		return StringTools.convertToJavaStringLiterals(this.candidateMapKeyNames(filter));
	}

	protected Iterator<String> candidateMapKeyNames(Filter<String> filter) {
		return new FilteringIterator<String, String>(this.candidateMapKeyNames(), filter);
	}

	public Iterator<String> candidateMapKeyNames() {
		return this.allTargetEntityAttributeNames();
	}


	// ********** metamodel **********  

	@Override
	protected String getMetamodelFieldTypeName() {
		return ((JavaPersistentAttribute2_0) this.getPersistentAttribute()).getMetamodelContainerFieldTypeName();
	}

	@Override
	protected void addMetamodelFieldTypeArgumentNamesTo(ArrayList<String> typeArgumentNames) {
		this.addMetamodelFieldMapKeyTypeArgumentNameTo(typeArgumentNames);
		super.addMetamodelFieldTypeArgumentNamesTo(typeArgumentNames);
	}

	protected void addMetamodelFieldMapKeyTypeArgumentNameTo(ArrayList<String> typeArgumentNames) {
		String keyTypeName = ((JavaPersistentAttribute2_0) this.getPersistentAttribute()).getMetamodelContainerFieldMapKeyTypeName();
		if (keyTypeName != null) {
			typeArgumentNames.add(keyTypeName);
		}
	}

	public String getMetamodelFieldMapKeyTypeName() {
		return MappingTools.getMetamodelFieldMapKeyTypeName(this);
	}

}
