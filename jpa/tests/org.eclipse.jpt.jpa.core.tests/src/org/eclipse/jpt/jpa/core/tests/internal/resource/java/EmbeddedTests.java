/*******************************************************************************
 * Copyright (c) 2007, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.tests.internal.resource.java;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.common.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.jpa.core.resource.java.EmbeddedAnnotation;
import org.eclipse.jpt.jpa.core.resource.java.JPA;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourcePersistentType;

@SuppressWarnings("nls")
public class EmbeddedTests extends JpaJavaResourceModelTestCase {

	public EmbeddedTests(String name) {
		super(name);
	}

	private ICompilationUnit createTestEmbedded() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.EMBEDDED);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Embedded");
			}
		});
	}
	
	public void testEmbedded() throws Exception {
		ICompilationUnit cu = this.createTestEmbedded();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		
		JavaResourceNode mappingAnnotation = attributeResource.getAnnotation(EmbeddedAnnotation.ANNOTATION_NAME);
		assertTrue(mappingAnnotation instanceof EmbeddedAnnotation);
	}

}