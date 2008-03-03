/*******************************************************************************
 *  Copyright (c) 2006, 2007 Oracle. All rights reserved. This
 *  program and the accompanying materials are made available under the terms of
 *  the Eclipse Public License v1.0 which accompanies this distribution, and is
 *  available at http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: Oracle. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.internal.resource.orm.translators;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jpt.core.internal.resource.common.translators.BooleanTranslator;
import org.eclipse.jpt.core.resource.orm.OrmFactory;
import org.eclipse.wst.common.internal.emf.resource.IDTranslator;
import org.eclipse.wst.common.internal.emf.resource.Translator;

public class BasicTranslator extends Translator 
	implements OrmXmlMapper
{
	private Translator[] children;	
	
	
	public BasicTranslator(String domNameAndPath, EStructuralFeature aFeature) {
		super(domNameAndPath, aFeature);
	}
	
	@Override
	public EObject createEMFObject(String nodeName, String readAheadName) {
		return OrmFactory.eINSTANCE.createXmlBasicImpl();
	}

	@Override
	public Translator[] getChildren(Object target, int versionID) {
		if (this.children == null) {
			this.children = createChildren();
		}
		return this.children;
	}
		
	protected Translator[] createChildren() {
		return new Translator[] {
			IDTranslator.INSTANCE,
			createNameTranslator(),
			createFetchTranslator(),
			createOptionalTranslator(),
			createColumnTranslator(), 
			createLobTranslator(),
			createTemporalTranslator(),
			createEnumeratedTranslator()
		};
	}
	
	private Translator createNameTranslator() {
		return new Translator(NAME, ORM_PKG.getXmlAttributeMapping_Name(), DOM_ATTRIBUTE);
	}
	
	private Translator createFetchTranslator() {
		return new Translator(FETCH, ORM_PKG.getXmlBasic_Fetch(), DOM_ATTRIBUTE);
	}
	
	private Translator createOptionalTranslator() {
		return new BooleanTranslator(OPTIONAL, ORM_PKG.getXmlBasic_Optional(), DOM_ATTRIBUTE);
	}
	
	private Translator createColumnTranslator() {
		return new ColumnTranslator(COLUMN, ORM_PKG.getColumnMapping_Column());
	}
	
	private Translator createLobTranslator() {
		return new EmptyTagBooleanTranslator(LOB, ORM_PKG.getXmlBasic_Lob());
	}
	
	private Translator createTemporalTranslator() {
		return new Translator(TEMPORAL, ORM_PKG.getXmlBasic_Temporal());
	}
	
	private Translator createEnumeratedTranslator() {
		return new Translator(ENUMERATED, ORM_PKG.getXmlBasic_Enumerated());
	}
}
