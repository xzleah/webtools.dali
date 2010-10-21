/*******************************************************************************
 * Copyright (c) 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jaxb.core.tests.internal.resource.java;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.jaxb.core.resource.java.JAXB;
import org.eclipse.jpt.jaxb.core.resource.java.XmlIDAnnotation;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;

@SuppressWarnings("nls")
public class XmlIDAnnotationTests extends JaxbJavaResourceModelTestCase {

	public XmlIDAnnotationTests(String name) {
		super(name);
	}

	private ICompilationUnit createTestXmlID() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JAXB.XML_ID);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@XmlID");
			}
		});
	}

	public void testGetXmlID() throws Exception {
		ICompilationUnit cu = this.createTestXmlID();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();

		XmlIDAnnotation xmlIDAnnotation = (XmlIDAnnotation) attributeResource.getAnnotation(JAXB.XML_ID);
		assertTrue(xmlIDAnnotation != null);

		attributeResource.removeAnnotation(JAXB.XML_ID);
		assertSourceDoesNotContain("@XmlID", cu);
	}
}