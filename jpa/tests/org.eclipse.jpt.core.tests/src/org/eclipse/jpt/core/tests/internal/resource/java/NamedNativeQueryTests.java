/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.tests.internal.resource.java;

import java.util.Iterator;
import org.eclipse.jdt.core.IType;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.core.resource.java.NamedNativeQueryAnnotation;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;

public class NamedNativeQueryTests extends JavaResourceModelTestCase {

	private static final String QUERY_NAME = "myQuery";
	private static final String QUERY_QUERY = "SELECT name FROM Employee";
	private static final String QUERY_RESULT_CLASS = "Result";
	private static final String QUERY_RESULT_SET_MAPPING = "resultSetMapping";
	
	public NamedNativeQueryTests(String name) {
		super(name);
	}

	private void createNamedNativeQueryAnnotation() throws Exception {
		createQueryHintAnnotation();
		this.createAnnotationAndMembers("NamedNativeQuery", "String name(); " +
			"String query();" + 
			"QueryHint[] hints() default{};");
	}
	
	private void createQueryHintAnnotation() throws Exception {
		this.createAnnotationAndMembers("QueryHint", "String name(); " +
			"String value();");
	}
	
	private IType createTestNamedNativeQuery() throws Exception {
		createNamedNativeQueryAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.NAMED_NATIVE_QUERY);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@NamedNativeQuery");
			}
		});
	}
	
	private IType createTestNamedNativeQueryWithName() throws Exception {
		return createTestNamedNativeQueryWithStringElement("name", QUERY_NAME);
	}
	
	private IType createTestNamedNativeQueryWithQuery() throws Exception {
		return createTestNamedNativeQueryWithStringElement("query", QUERY_QUERY);
	}
	
	private IType createTestNamedNativeQueryWithResultSetMapping() throws Exception {
		return createTestNamedNativeQueryWithStringElement("resultSetMapping", QUERY_RESULT_SET_MAPPING);
	}
	

	private IType createTestNamedNativeQueryWithStringElement(final String elementName, final String value) throws Exception {
		createNamedNativeQueryAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.NAMED_NATIVE_QUERY);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@NamedNativeQuery(" + elementName + "=\"" + value + "\")");
			}
		});
	}

	private IType createTestNamedNativeQueryWithResultClass() throws Exception {
		createNamedNativeQueryAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.NAMED_NATIVE_QUERY);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@NamedNativeQuery(resultClass=" + QUERY_RESULT_CLASS + ".class)");
			}
		});
	}
	private IType createTestNamedNativeQueryWithQueryHints() throws Exception {
		createNamedNativeQueryAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.NAMED_NATIVE_QUERY, JPA.QUERY_HINT);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@NamedNativeQuery(hints={@QueryHint(name=\"BAR\", value=\"FOO\"), @QueryHint})");
			}
		});
	}

	public void testNamedNativeQuery() throws Exception {
		IType testType = this.createTestNamedNativeQuery();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType);
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		assertNotNull(namedQuery);
	}

	public void testGetName() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithName();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		assertEquals(QUERY_NAME, namedQuery.getName());
	}

	public void testSetName() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithName();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		assertEquals(QUERY_NAME, namedQuery.getName());
		
		namedQuery.setName("foo");
		assertEquals("foo", namedQuery.getName());
		
		assertSourceContains("@NamedNativeQuery(name=\"foo\")");
		
		namedQuery.setName(null);
		assertNull(namedQuery.getName());
		
		assertSourceDoesNotContain("@NamedNativeQuery");
	}

	public void testGetQuery() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithQuery();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		assertEquals(QUERY_QUERY, namedQuery.getQuery());
	}

	public void testSetQuery() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithQuery();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		assertEquals(QUERY_QUERY, namedQuery.getQuery());
		
		namedQuery.setQuery("foo");
		assertEquals("foo", namedQuery.getQuery());
		
		assertSourceContains("@NamedNativeQuery(query=\"foo\")");
		
		namedQuery.setQuery(null);
		assertNull(namedQuery.getQuery());
		
		assertSourceDoesNotContain("@NamedNativeQuery");
	}
	
	public void testGetResultClass() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithResultClass();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		assertEquals(QUERY_RESULT_CLASS, namedQuery.getResultClass());
	}

	public void testSetResultClass() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithResultClass();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		assertEquals(QUERY_RESULT_CLASS, namedQuery.getResultClass());
		
		namedQuery.setResultClass("foo");
		assertEquals("foo", namedQuery.getResultClass());
		
		assertSourceContains("@NamedNativeQuery(resultClass=foo.class)");
		
		namedQuery.setResultClass(null);
		assertNull(namedQuery.getResultClass());
		
		assertSourceDoesNotContain("@NamedNativeQuery");
	}

	public void testGetFullyQualifiedClass() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithResultClass();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType);

		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		assertNotNull(namedQuery.getResultClass());
		assertNull(namedQuery.getFullyQualifiedResultClass());

		namedQuery.setResultClass(TYPE_NAME);		
		
		assertEquals(FULLY_QUALIFIED_TYPE_NAME, namedQuery.getFullyQualifiedResultClass());				
		assertSourceContains("@NamedNativeQuery(resultClass=" + TYPE_NAME + ".class)");
	}
	
	public void testGetResultSetMapping() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithResultSetMapping();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		assertEquals(QUERY_RESULT_SET_MAPPING, namedQuery.getResultSetMapping());
	}

	public void testSetResultSetMapping() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithResultSetMapping();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		assertEquals(QUERY_RESULT_SET_MAPPING, namedQuery.getResultSetMapping());
		
		namedQuery.setResultSetMapping("foo");
		assertEquals("foo", namedQuery.getResultSetMapping());
		
		assertSourceContains("@NamedNativeQuery(resultSetMapping=\"foo\")");
		
		namedQuery.setResultSetMapping(null);
		assertNull(namedQuery.getResultSetMapping());
		
		assertSourceDoesNotContain("@NamedNativeQuery");
	}

	public void testHints() throws Exception {
		IType testType = this.createTestNamedNativeQuery();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		
		assertEquals(0, namedQuery.hintsSize());
	}
	
	public void testHints2() throws Exception {
		IType testType = this.createTestNamedNativeQuery();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		
		namedQuery.addHint(0);
		namedQuery.addHint(1);
		
		assertEquals(2, namedQuery.hintsSize());
	}
	
	public void testHints3() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithQueryHints();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		
		assertEquals(2, namedQuery.hintsSize());
	}
	
	
	public void testAddHint() throws Exception {
		IType testType = this.createTestNamedNativeQuery();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		
		namedQuery.addHint(0).setName("FOO");
		namedQuery.addHint(1);
		namedQuery.addHint(0).setName("BAR");

		assertEquals("BAR", namedQuery.hintAt(0).getName());
		assertEquals("FOO", namedQuery.hintAt(1).getName());
		assertNull(namedQuery.hintAt(2).getName());
		assertSourceContains("@NamedNativeQuery(hints={@QueryHint(name=\"BAR\"),@QueryHint(name=\"FOO\"), @QueryHint})");
	}
	
	public void testRemoveHint() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithQueryHints();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		namedQuery.addHint(0).setName("BAZ");
		
		assertEquals("BAZ", namedQuery.hintAt(0).getName());
		assertEquals("BAR", namedQuery.hintAt(1).getName());
		assertNull(namedQuery.hintAt(2).getName());
		assertEquals(3, namedQuery.hintsSize());
		
		namedQuery.removeHint(2);
		assertEquals("BAZ", namedQuery.hintAt(0).getName());
		assertEquals("BAR", namedQuery.hintAt(1).getName());
		assertEquals(2, namedQuery.hintsSize());
		assertSourceContains("@NamedNativeQuery(hints={@QueryHint(name=\"BAZ\"), @QueryHint(name=\"BAR\", value=\"FOO\")})");
		
		namedQuery.removeHint(0);
		assertEquals("BAR", namedQuery.hintAt(0).getName());
		assertEquals(1, namedQuery.hintsSize());
		assertSourceContains("@NamedNativeQuery(hints=@QueryHint(name=\"BAR\", value=\"FOO\"))");
		
	
		namedQuery.removeHint(0);
		assertEquals(0, namedQuery.hintsSize());
		assertSourceDoesNotContain("@NamedNativeQuery");
	}
	
	public void testMoveHint() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithQueryHints();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		
		namedQuery.addHint(0).setName("BAZ");
		
		assertEquals("BAZ", namedQuery.hintAt(0).getName());
		assertEquals("BAR", namedQuery.hintAt(1).getName());
		assertNull(namedQuery.hintAt(2).getName());
		assertEquals(3, namedQuery.hintsSize());
	
		namedQuery.moveHint(2, 0);
		
		assertEquals("BAR", namedQuery.hintAt(0).getName());
		assertNull(namedQuery.hintAt(1).getName());
		assertEquals("BAZ", namedQuery.hintAt(2).getName());
		assertEquals(3, namedQuery.hintsSize());
		assertSourceContains("@NamedNativeQuery(hints={@QueryHint(name=\"BAR\", value=\"FOO\"), @QueryHint, @QueryHint(name=\"BAZ\")})");
	}
	
	public void testMoveHint2() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithQueryHints();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.annotation(JPA.NAMED_NATIVE_QUERY);
		namedQuery.addHint(0).setName("BAZ");
		
		assertEquals("BAZ", namedQuery.hintAt(0).getName());
		assertEquals("BAR", namedQuery.hintAt(1).getName());
		assertNull(namedQuery.hintAt(2).getName());
		assertEquals(3, namedQuery.hintsSize());
	
		namedQuery.moveHint(0, 2);
		
		assertNull(namedQuery.hintAt(0).getName());
		assertEquals("BAZ", namedQuery.hintAt(1).getName());
		assertEquals("BAR", namedQuery.hintAt(2).getName());
		assertEquals(3, namedQuery.hintsSize());
		assertSourceContains("@NamedNativeQuery(hints={@QueryHint, @QueryHint(name=\"BAZ\"), @QueryHint(name=\"BAR\", value=\"FOO\")})");
	}
	
}
