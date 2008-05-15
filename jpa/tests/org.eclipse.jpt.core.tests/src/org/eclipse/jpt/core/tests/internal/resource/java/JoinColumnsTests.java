/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.tests.internal.resource.java;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.core.resource.java.JoinColumnAnnotation;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;

public class JoinColumnsTests extends JavaResourceModelTestCase {
	
	private static final String COLUMN_NAME = "MY_COLUMN";
	private static final String COLUMN_TABLE = "MY_TABLE";
	private static final String COLUMN_COLUMN_DEFINITION = "COLUMN_DEFINITION";
	private static final String COLUMN_REFERENCED_COLUMN_NAME = "MY_REF_COLUMN_NAME";
	
	public JoinColumnsTests(String name) {
		super(name);
	}
	
	private void createJoinColumnAnnotation() throws Exception {
		this.createAnnotationAndMembers("JoinColumn", 
			"String name() default \"\"; " +
			"String referencedColumnName() default \"\"; " +
			"boolean unique() default false; " +
			"boolean nullable() default true; " +
			"boolean insertable() default true; " +
			"boolean updatable() default true; " +
			"String columnDefinition() default \"\"; " +
			"String table() default \"\"; ");
	}
	
	private void createJoinColumnsAnnotation() throws Exception {
		createJoinColumnAnnotation();
		this.createAnnotationAndMembers("JoinColumns", 
			"JoinColumn[] value();");
	}

	private ICompilationUnit createTestJoinColumns() throws Exception {
		createJoinColumnsAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@JoinColumns(@JoinColumn)");
			}
		});
	}
	
	private ICompilationUnit createTestJoinColumnWithName() throws Exception {
		createJoinColumnsAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@JoinColumns(@JoinColumn(name=\"" + COLUMN_NAME + "\"))");
			}
		});
	}
	
	private ICompilationUnit createTestJoinColumnWithTable() throws Exception {
		createJoinColumnsAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@JoinColumns(@JoinColumn(table=\"" + COLUMN_TABLE + "\"))");
			}
		});
	}
	
	private ICompilationUnit createTestJoinColumnWithReferencedColumnName() throws Exception {
		createJoinColumnsAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@JoinColumns(@JoinColumn(referencedColumnName=\"" + COLUMN_REFERENCED_COLUMN_NAME + "\"))");
			}
		});
	}
	
	private ICompilationUnit createTestJoinColumnWithColumnDefinition() throws Exception {
		createJoinColumnsAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@JoinColumns(@JoinColumn(columnDefinition=\"" + COLUMN_COLUMN_DEFINITION + "\"))");
			}
		});
	}
	
	private ICompilationUnit createTestJoinColumnWithBooleanElement(final String booleanElement) throws Exception {
		createJoinColumnsAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@JoinColumns(@JoinColumn(" + booleanElement + "=true))");
			}
		});
	}

	private ICompilationUnit createTestJoinColumn() throws Exception {
		createJoinColumnsAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@JoinColumn(name=\"BAR\", referencedColumnName = \"REF_NAME\", unique = false, nullable = false, insertable = false, updatable = false, columnDefinition = \"COLUMN_DEF\", table = \"TABLE\")");
			}
		});
	}
	
	public void testGetName() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumnWithName();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();
		assertNotNull(column);
		assertEquals(COLUMN_NAME, column.getName());
	}

	public void testGetNull() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumns();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();
		assertNotNull(column);
		assertNull(column.getName());
		assertNull(column.getNullable());
		assertNull(column.getInsertable());
		assertNull(column.getUnique());
		assertNull(column.getUpdatable());
		assertNull(column.getTable());
		assertNull(column.getColumnDefinition());
	}

	public void testSetName() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumns();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();

		assertNotNull(column);
		assertNull(column.getName());

		column.setName("Foo");
		assertEquals("Foo", column.getName());
		
		assertSourceContains("@JoinColumns(@JoinColumn(name=\"Foo\"))", cu);
	}
	
	public void testSetNameNull() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumnWithName();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();

		assertEquals(COLUMN_NAME, column.getName());
		
		column.setName(null);
		assertNull(column.getName());
		
		assertSourceDoesNotContain("@JoinColumn", cu);
	}
	
	public void testGetTable() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumnWithTable();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();
		assertEquals(COLUMN_TABLE, column.getTable());
	}

	public void testSetTable() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumns();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();

		assertNotNull(column);
		assertNull(column.getTable());

		column.setTable("Foo");
		assertEquals("Foo", column.getTable());
		
		assertSourceContains("@JoinColumns(@JoinColumn(table=\"Foo\"))", cu);

		
		column.setTable(null);
		assertSourceDoesNotContain("@JoinColumn", cu);
	}
	
	public void testGetReferencedColumnName() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumnWithReferencedColumnName();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();
		assertEquals(COLUMN_REFERENCED_COLUMN_NAME, column.getReferencedColumnName());
	}

	public void testSetReferencedColumnName() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumns();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();

		assertNotNull(column);
		assertNull(column.getReferencedColumnName());

		column.setReferencedColumnName("Foo");
		assertEquals("Foo", column.getReferencedColumnName());
		
		assertSourceContains("@JoinColumns(@JoinColumn(referencedColumnName=\"Foo\"))", cu);

		
		column.setReferencedColumnName(null);
		assertSourceDoesNotContain("@JoinColumn", cu);
	}

	public void testGetColumnDefinition() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumnWithColumnDefinition();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();
		assertEquals(COLUMN_COLUMN_DEFINITION, column.getColumnDefinition());
	}

	public void testSetColumnDefinition() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumns();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();

		assertNotNull(column);
		assertNull(column.getColumnDefinition());

		column.setColumnDefinition("Foo");
		assertEquals("Foo", column.getColumnDefinition());
		
		assertSourceContains("@JoinColumns(@JoinColumn(columnDefinition=\"Foo\"))", cu);

		
		column.setColumnDefinition(null);
		assertSourceDoesNotContain("@JoinColumn", cu);
	}

	public void testGetUnique() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumnWithBooleanElement("unique");
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();

		assertTrue(column.getUnique());
	}
	
	public void testSetUnique() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumns();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();

		assertNotNull(column);
		assertNull(column.getUnique());

		column.setUnique(false);
		assertFalse(column.getUnique());
		
		assertSourceContains("@JoinColumns(@JoinColumn(unique=false))", cu);
		
		column.setUnique(null);
		assertSourceDoesNotContain("@JoinColumn", cu);
	}
	
	public void testGetNullable() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumnWithBooleanElement("nullable");
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();

		assertTrue(column.getNullable());
	}
	
	public void testSetNullable() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumns();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();

		assertNotNull(column);
		assertNull(column.getNullable());

		column.setNullable(false);
		assertFalse(column.getNullable());
		
		assertSourceContains("@JoinColumns(@JoinColumn(nullable=false))", cu);
		
		column.setNullable(null);
		assertSourceDoesNotContain("@JoinColumn", cu);
	}

	public void testGetInsertable() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumnWithBooleanElement("insertable");
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();

		assertTrue(column.getInsertable());
	}
	
	public void testSetInsertable() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumns();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();

		assertNotNull(column);
		assertNull(column.getInsertable());

		column.setInsertable(false);
		assertFalse(column.getInsertable());
		
		assertSourceContains("@JoinColumns(@JoinColumn(insertable=false))", cu);
		
		column.setInsertable(null);
		assertSourceDoesNotContain("@JoinColumn", cu);
	}
	
	public void testGetUpdatable() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumnWithBooleanElement("updatable");
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();

		assertTrue(column.getUpdatable());
	}
	
	public void testSetUpdatable() throws Exception {
		ICompilationUnit cu = this.createTestJoinColumns();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		JoinColumnAnnotation column = (JoinColumnAnnotation) attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS).next();

		assertNotNull(column);
		assertNull(column.getUpdatable());

		column.setUpdatable(false);
		assertFalse(column.getUpdatable());
		
		assertSourceContains("@JoinColumns(@JoinColumn(updatable=false))", cu);
		
		column.setUpdatable(null);
		assertSourceDoesNotContain("@JoinColumn", cu);
		assertSourceDoesNotContain("@JoinColumns", cu);
	}
	
	
	public void testAddJoinColumnCopyExisting() throws Exception {
		ICompilationUnit cu = createTestJoinColumn();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		
		JoinColumnAnnotation joinColumn = (JoinColumnAnnotation) attributeResource.addAnnotation(1, JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS);
		joinColumn.setName("FOO");
		assertSourceContains("@JoinColumns({@JoinColumn(name=\"BAR\", columnDefinition = \"COLUMN_DEF\", table = \"TABLE\", unique = false, nullable = false, insertable = false, updatable = false, referencedColumnName = \"REF_NAME\"),@JoinColumn(name=\"FOO\")})", cu);
		
		assertNull(attributeResource.getAnnotation(JPA.JOIN_COLUMN));
		assertNotNull(attributeResource.getAnnotation(JPA.JOIN_COLUMNS));
		assertEquals(2, CollectionTools.size(attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS)));
	}
	
	public void testAddJoinColumnToBeginningOfList() throws Exception {
		ICompilationUnit cu = createTestJoinColumn();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		
		JoinColumnAnnotation joinColumn = (JoinColumnAnnotation) attributeResource.addAnnotation(1, JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS);
		joinColumn.setName("FOO");
		assertSourceContains("@JoinColumns({@JoinColumn(name=\"BAR\", columnDefinition = \"COLUMN_DEF\", table = \"TABLE\", unique = false, nullable = false, insertable = false, updatable = false, referencedColumnName = \"REF_NAME\"),@JoinColumn(name=\"FOO\")})", cu);
				
		joinColumn = (JoinColumnAnnotation) attributeResource.addAnnotation(0, JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS);
		joinColumn.setName("BAZ");
		assertSourceContains("@JoinColumns({@JoinColumn(name=\"BAZ\"),@JoinColumn(name=\"BAR\", columnDefinition = \"COLUMN_DEF\", table = \"TABLE\", unique = false, nullable = false, insertable = false, updatable = false, referencedColumnName = \"REF_NAME\"), @JoinColumn(name=\"FOO\")})", cu);

		Iterator<JavaResourceNode> joinColumns = attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS);
		assertEquals("BAZ", ((JoinColumnAnnotation) joinColumns.next()).getName());
		assertEquals("BAR", ((JoinColumnAnnotation) joinColumns.next()).getName());
		assertEquals("FOO", ((JoinColumnAnnotation) joinColumns.next()).getName());
		
		assertNull(attributeResource.getAnnotation(JPA.JOIN_COLUMN));
		assertNotNull(attributeResource.getAnnotation(JPA.JOIN_COLUMNS));
		assertEquals(3, CollectionTools.size(attributeResource.annotations(JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS)));
	}


	public void testRemoveJoinColumnCopyExisting() throws Exception {
		ICompilationUnit cu = createTestJoinColumn();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		
		JoinColumnAnnotation joinColumn = (JoinColumnAnnotation) attributeResource.addAnnotation(1, JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS);
		joinColumn.setName("FOO");
		assertSourceContains("@JoinColumns({@JoinColumn(name=\"BAR\", columnDefinition = \"COLUMN_DEF\", table = \"TABLE\", unique = false, nullable = false, insertable = false, updatable = false, referencedColumnName = \"REF_NAME\"),@JoinColumn(name=\"FOO\")})", cu);
		
		attributeResource.removeAnnotation(1, JPA.JOIN_COLUMN, JPA.JOIN_COLUMNS);
		assertSourceContains("@JoinColumn(name=\"BAR\", columnDefinition = \"COLUMN_DEF\", table = \"TABLE\", unique = false, nullable = false, insertable = false, updatable = false, referencedColumnName = \"REF_NAME\")", cu);
	}

}
