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
import java.util.List;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.core.internal.resource.java.EmbeddableImpl;
import org.eclipse.jpt.core.internal.resource.java.EntityImpl;
import org.eclipse.jpt.core.resource.java.AccessType;
import org.eclipse.jpt.core.resource.java.EmbeddableAnnotation;
import org.eclipse.jpt.core.resource.java.EntityAnnotation;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.core.resource.java.MappedSuperclassAnnotation;
import org.eclipse.jpt.core.resource.java.SecondaryTableAnnotation;
import org.eclipse.jpt.core.resource.java.TableAnnotation;
import org.eclipse.jpt.core.utility.jdt.ModifiedDeclaration;
import org.eclipse.jpt.core.utility.jdt.Member.Editor;
import org.eclipse.jpt.utility.internal.ClassTools;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;

public class JavaResourcePersistentTypeTests extends JavaResourceModelTestCase {
	
	public JavaResourcePersistentTypeTests(String name) {
		super(name);
	}
	
	private void createEntityAnnotation() throws Exception{
		this.createAnnotationAndMembers("Entity", "String name() default \"\";");		
	}
	
	private void createEmbeddableAnnotation() throws Exception{
		this.createAnnotationAndMembers("Embeddable", "");		
	}
	
	private void createMappedSuperclassAnnotation() throws Exception{
		this.createAnnotationAndMembers("MappedSuperclass", "");		
	}
	
	private void createSecondaryTableAnnotation() throws Exception{
		this.createAnnotationAndMembers("SecondaryTable", "String name();");		
	}
	
	private void createSecondaryTablesAnnotation() throws Exception{
		this.createAnnotationAndMembers("SecondaryTables", "");		
	}
	
	
	private ICompilationUnit createTestEntity() throws Exception {
		createEntityAnnotation();

		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
		});
	}
	
	private ICompilationUnit createTestInvalidAnnotations() throws Exception {
		this.createAnnotationAndMembers("Foo", "String name();");

		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>("javax.persistence.Foo");
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Foo");
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Foo");
			}
		});
	}

	private ICompilationUnit createTestEntityWithMemberEmbeddable() throws Exception {
		createEntityAnnotation();
		createEmbeddableAnnotation();

		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.EMBEDDABLE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			
			@Override
			public void appendMemberTypeTo(StringBuilder sb) {
				sb.append("     @Embeddable");
				sb.append(CR);
				sb.append("     public static class Foo { }").append(CR);

			}
		});
	}

	private ICompilationUnit createTestEntityDuplicates() throws Exception {
		createEntityAnnotation();

		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity(name=\"FirstEntity\")").append(CR);
				sb.append("@Entity(name=\"SecondEntity\")");
			}
		});
	}

	private ICompilationUnit createTestEntityWithEmbeddable() throws Exception {
		createEntityAnnotation();
		createEmbeddableAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.EMBEDDABLE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
				sb.append(CR);
				sb.append("@Embeddable");
			}
		});
	}
	
	private ICompilationUnit createTestEntityAnnotatedField() throws Exception {
		createEntityAnnotation();
		this.createAnnotationAndMembers("Id", "");
	
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.ID);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Id");
			}
		});
	}

	private ICompilationUnit createTestEntityAnnotatedMethod() throws Exception {
		createEntityAnnotation();
		this.createAnnotationAndMembers("Id", "");
	
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.ID);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			
			@Override
			public void appendGetIdMethodAnnotationTo(StringBuilder sb) {
				sb.append("@Id");
			}
		});
	}
	private ICompilationUnit createTestEntityAnnotatedFieldAndMethod() throws Exception {
		createEntityAnnotation();
		this.createAnnotationAndMembers("Id", "");
	
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.ID);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			
			@Override
			public void appendGetIdMethodAnnotationTo(StringBuilder sb) {
				sb.append("@Id");
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Id");
			}
		});
	}
	
	private ICompilationUnit createTestEntityAnnotatedNonPersistableMethod() throws Exception {
		createEntityAnnotation();
		this.createAnnotationAndMembers("Id", "");
	
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.ID);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			@Override
			public void appendGetNameMethodAnnotationTo(StringBuilder sb) {
				sb.append("@Id");
			}
		});
	}
	
	private ICompilationUnit createTestEntityAnnotatedPersistableMethodNonPersistableField() throws Exception {
		createEntityAnnotation();
		this.createAnnotationAndMembers("Id", "");
		this.createAnnotationAndMembers("Column", "");
	
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.COLUMN);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Column");
				sb.append("    private transient int notPersistable;").append(CR);
				sb.append(CR);

			}
			@Override
			public void appendGetIdMethodAnnotationTo(StringBuilder sb) {
				sb.append("@Column");
			}
		});
	}
	private ICompilationUnit createTestEntityNoPersistableFields() throws Exception {
		createEntityAnnotation();
	
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			@Override
			public void appendMemberTypeTo(StringBuilder sb) {
				sb.delete(sb.indexOf("private int id;"), sb.indexOf("private int id;") + "private int id;".length());
				sb.delete(sb.indexOf("private String name;"), sb.indexOf("private String name;") + "private String name;".length());
			}
		});
	}

//	private ICompilationUnit createTestEntityLarge(final int i) throws Exception {
//		return this.createTestType(new DefaultAnnotationWriter() {
//			@Override
//			public Iterator<String> imports() {
//				return new ArrayIterator<String>(JPA.ENTITY + i);
//			}
//			@Override
//			public void appendTypeAnnotationTo(StringBuilder sb) {
//				sb.append("@Entity" + i);
//				sb.append(CR);
//			}
//			@Override
//			public String typeName() {
//				return TYPE_NAME + i;
//			}
//		});
//	}
//
//	public void testLarge() throws Exception {
//		for (int i = START; i <= END; i++) {
//			this.createAnnotationAndMembers("Entity" +i, "String name();");
//		}
//
//		List<ICompilationUnit> cus = new ArrayList<ICompilationUnit>();
//		for (int i = START; i <= END; i++) {
//			cus.add(createTestEntityLarge(i));
//		}
//		long start = System.currentTimeMillis();
//		List<JavaPersistentTypeResource> typeResources = new ArrayList<JavaPersistentTypeResource>();
//		for (int i = 0; i < END; i++) {
//			typeResources.add(buildJavaTypeResource(cus.get(i))); 
//		}
//		long end = System.currentTimeMillis();
//		
//		System.out.println(end-start + "ms");
//		for (int i = 0; i < END; i++) {
//			assertEquals(1, CollectionTools.size(typeResources.get(i).javaTypeMappingAnnotations()));
//			assertNotNull(typeResources.get(i).javaTypeMappingAnnotation(JPA.ENTITY + (i+1)));
//		}
//		
//
////		assertEquals(0, CollectionTools.size(typeResource.javaTypeAnnotations()));
//	}

	
	
	private ICompilationUnit createTestEntityWithTable() throws Exception {
		createEntityAnnotation();
		this.createAnnotationAndMembers("Table", "String name(); String schema();");
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.TABLE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
				sb.append(CR);
				sb.append("@Table(name=\"FOO\", schema=\"BAR\")");
			}
		});
	}
	
//	private ICompilationUnit createTestEntityWithTableAndIdClass() throws Exception {
//		createEntityAnnotation();
//		this.createAnnotationAndMembers("Table", "String name(); String schema();");
//		this.createAnnotationAndMembers("IdClass", "Class value();");
//		return this.createTestType(new DefaultAnnotationWriter() {
//			@Override
//			public Iterator<String> imports() {
//				return new ArrayIterator<String>(JPA.ENTITY, JPA.TABLE, JPA.ID_CLASS);
//			}
//			@Override
//			public void appendTypeAnnotationTo(StringBuilder sb) {
//				sb.append("@Entity");
//				sb.append(CR);
//				sb.append("@Table");
//				sb.append(CR);
//				sb.append("@IdClass");
//			}
//		});
//	}
//
	
	private ICompilationUnit createTestEntityMultipleTables() throws Exception {
		createEntityAnnotation();
		this.createAnnotationAndMembers("Table", "String name(); String schema();");
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.TABLE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
				sb.append(CR);
				sb.append("@Table(name=\"FOO\")");
				sb.append(CR);
				sb.append("@Table(name=\"BAR\")");
			}
		});
	}

	private ICompilationUnit createTestEntityWithSecondaryTable() throws Exception {
		createEntityAnnotation();
		createSecondaryTableAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.SECONDARY_TABLE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
				sb.append(CR);
				sb.append("@SecondaryTable(name=\"FOO\")");
			}
		});
	}
	private ICompilationUnit createTestEntityWithEmptySecondaryTables() throws Exception {
		createEntityAnnotation();
		this.createAnnotationAndMembers("SecondaryTable", "String name();");
		this.createAnnotationAndMembers("SecondaryTables", "SecondaryTable[] value();");
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
				sb.append(CR);
				sb.append("@SecondaryTables()");
			}
		});
	}

	private ICompilationUnit createTestEntityWithSecondaryTables() throws Exception {
		createEntityAnnotation();
		this.createAnnotationAndMembers("SecondaryTable", "String name();");
		this.createAnnotationAndMembers("SecondaryTables", "SecondaryTable[] value();");
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
				sb.append(CR);
				sb.append("@SecondaryTables(@SecondaryTable(name=\"FOO\"))");
			}
		});
	}
	
	private ICompilationUnit createTestEntityWith2SecondaryTables() throws Exception {
		createEntityAnnotation();
		this.createAnnotationAndMembers("SecondaryTable", "String name();");
		this.createAnnotationAndMembers("SecondaryTables", "SecondaryTable[] value();");
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
				sb.append(CR);
				sb.append("@SecondaryTables({@SecondaryTable(name=\"FOO\"), @SecondaryTable(name=\"BAR\")})");
			}
		});
	}
	
	private ICompilationUnit createTestEntityWithSecondaryTableAndSecondaryTables() throws Exception {
		createEntityAnnotation();
		this.createAnnotationAndMembers("SecondaryTable", "String name();");
		this.createAnnotationAndMembers("SecondaryTables", "SecondaryTable[] value();");
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
				sb.append(CR);
				sb.append("@SecondaryTable(name=\"FOO\")");
				sb.append(CR);
				sb.append("@SecondaryTables({@SecondaryTable(name=\"BAR\"), @SecondaryTable(name=\"BAZ\")})");
			}
		});
	}

	private ICompilationUnit createTestEntityWithMemberTypes() throws Exception {
		createEntityAnnotation();

		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			@Override
			public void appendMemberTypeTo(StringBuilder sb) {
				sb.append("     static class FooStatic {}").append(CR);
				sb.append(CR);
				sb.append("     class FooNotStatic {}").append(CR);
				sb.append(CR);
				sb.append("     @interface MyAnnotation {}").append(CR);
				sb.append(CR);
				sb.append("     enum MyEnum {}").append(CR);
			}
		});
	}
	
	public void testJavaTypeAnnotations() throws Exception {
		ICompilationUnit cu = this.createTestEntityWithTable();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		assertEquals(1, typeResource.annotationsSize());
	}

	public void testJavaTypeAnnotation() throws Exception {
		ICompilationUnit cu = this.createTestEntityWithTable();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		assertNotNull(typeResource.getAnnotation(JPA.TABLE));
	}

	public void testJavaTypeAnnotationNull() throws Exception {
		ICompilationUnit cu = this.createTestEntity();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		assertNull(typeResource.getAnnotation(JPA.TABLE));
	}

	//This will result in a compilation error, but we assume the first table found
	public void testDuplicateAnnotations() throws Exception {
		ICompilationUnit cu = this.createTestEntityMultipleTables();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		TableAnnotation tableResource = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
		assertEquals("FOO", tableResource.getName());
	}

	public void testRemoveTable() throws Exception {
		ICompilationUnit cu = this.createTestEntityWithTable();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		typeResource.removeAnnotation(JPA.TABLE);
		
		assertSourceDoesNotContain("@Table", cu);
	}
	
	public void testRemoveTableName() throws Exception {
		ICompilationUnit cu = this.createTestEntityWithTable();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 

		TableAnnotation tableResource = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
		tableResource.setSchema(null);
		assertSourceContains("@Table(name=\"FOO\")", cu);

		tableResource.setName(null);
		assertSourceDoesNotContain("@Table", cu);
		
		assertNull(typeResource.getAnnotation(JPA.TABLE));
	}
	
	public void testMultipleTypeMappings() throws Exception {
		ICompilationUnit cu = this.createTestEntityWithEmbeddable();
		createMappedSuperclassAnnotation();

		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		
		assertEquals(2, typeResource.mappingAnnotationsSize());
		assertEquals(0, typeResource.annotationsSize());
		assertNotNull(typeResource.getMappingAnnotation(JPA.EMBEDDABLE));
		assertNotNull(typeResource.getMappingAnnotation(JPA.ENTITY));
		
		JavaResourceNode javaTypeMappingAnnotation = typeResource.getMappingAnnotation();
		assertTrue(javaTypeMappingAnnotation instanceof EmbeddableAnnotation);
		assertSourceContains("@Entity", cu);
		assertSourceContains("@Embeddable", cu);
		
		typeResource.setMappingAnnotation(JPA.MAPPED_SUPERCLASS);
		assertEquals(1, typeResource.mappingAnnotationsSize());
		javaTypeMappingAnnotation = typeResource.getMappingAnnotation();
		assertTrue(javaTypeMappingAnnotation instanceof MappedSuperclassAnnotation);
		assertSourceDoesNotContain("@Entity", cu);
		assertSourceContains("@MappedSuperclass", cu);
		assertSourceDoesNotContain("@Embeddable", cu);
	}
	
	public void testSetJavaTypeMappingAnnotation() throws Exception {
		createEntityAnnotation();
		ICompilationUnit cu = createTestType();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		assertEquals(0, typeResource.mappingAnnotationsSize());
		
		typeResource.setMappingAnnotation(JPA.ENTITY);
		assertTrue(typeResource.getMappingAnnotation() instanceof EntityAnnotation);
		assertSourceContains("@Entity", cu);
	}

	public void testSetJavaTypeMappingAnnotation2() throws Exception {
		ICompilationUnit cu = createTestEntityWithTable();
		createEmbeddableAnnotation();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		assertTrue(typeResource.getMappingAnnotation() instanceof EntityAnnotation);
		
		typeResource.setMappingAnnotation(JPA.EMBEDDABLE);
		assertTrue(typeResource.getMappingAnnotation() instanceof EmbeddableAnnotation);
		
		assertSourceDoesNotContain("@Entity", cu);
		assertSourceContains("@Table", cu);
	}

	public void testAddJavaTypeAnnotation() throws Exception {
		ICompilationUnit cu = createTestEntity();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		
		assertSourceDoesNotContain("@Table", cu);
		typeResource.addAnnotation(JPA.TABLE);
	
		assertSourceContains("@Table", cu);
	}
	
	public void testRemoveJavaTypeAnnotation() throws Exception {
		ICompilationUnit cu = createTestEntityWithTable();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		assertSourceContains("@Table", cu);
		typeResource.removeAnnotation(JPA.TABLE);
		assertSourceDoesNotContain("@Table", cu);
	}
	
	//update source code to change from @Entity to @Embeddable and make sure @Table is not removed
	public void testChangeTypeMappingInSource() throws Exception {
		ICompilationUnit cu = createTestEntityWithTable();
		final JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
		
		testType(cu).edit(new Editor() {
			public void edit(ModifiedDeclaration declaration) {
				((EntityImpl) typeResource.getMappingAnnotation()).getDeclarationAnnotationAdapter().removeAnnotation(declaration);
			}
		});	

		this.createAnnotationAndMembers("Embeddable", "String name();");
		cu.createImport("javax.persistence.Embeddable", null, new NullProgressMonitor());
				
		this.testType(cu).edit(new Editor() {
			public void edit(ModifiedDeclaration declaration) {
				EmbeddableImpl.DECLARATION_ANNOTATION_ADAPTER.newMarkerAnnotation(declaration);
			}
		});		
		
		assertNotNull(typeResource.getAnnotation(JPA.TABLE));
		assertNull(typeResource.getMappingAnnotation(JPA.ENTITY));
		assertNotNull(typeResource.getMappingAnnotation(JPA.EMBEDDABLE));
		assertSourceContains("@Table", cu);
	}
	
	public void testJavaTypeAnnotationsNestable() throws Exception {
		ICompilationUnit cu = createTestEntityWithSecondaryTable();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
		
		assertEquals(1, CollectionTools.size(typeResource.annotations(JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES)));
		
		SecondaryTableAnnotation secondaryTableResource = (SecondaryTableAnnotation) typeResource.annotations(JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES).next();
		
		assertEquals("FOO", secondaryTableResource.getName());
	}
	
	public void testJavaTypeAnnotationsNoNestable() throws Exception {
		ICompilationUnit cu = createTestEntity();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
		
		assertEquals(0, CollectionTools.size(typeResource.annotations(JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES)));
	}
	
	public void testJavaTypeAnnotationsContainerNoNestable() throws Exception {
		ICompilationUnit cu = createTestEntityWithEmptySecondaryTables();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
		
		assertEquals(0, CollectionTools.size(typeResource.annotations(JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES)));
	}

	public void testJavaTypeAnnotationsNestableAndContainer() throws Exception {
		ICompilationUnit cu = createTestEntityWithSecondaryTableAndSecondaryTables();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
		
		assertNotNull(typeResource.getAnnotation(JPA.SECONDARY_TABLE));
		assertNotNull(typeResource.getAnnotation(JPA.SECONDARY_TABLES));
		assertEquals(2, CollectionTools.size(typeResource.annotations(JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES)));

		SecondaryTableAnnotation secondaryTableResource = (SecondaryTableAnnotation) typeResource.annotations(JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES).next();	
		assertEquals("BAR", secondaryTableResource.getName());
	}
	
	//  @Entity     -->>    @Entity
	//						@SecondaryTable(name="FOO")
	public void testAddJavaTypeAnnotationNestableContainer() throws Exception {
		ICompilationUnit cu = createTestEntity();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
		SecondaryTableAnnotation secondaryTableResource = (SecondaryTableAnnotation) typeResource.addAnnotation(0, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
		secondaryTableResource.setName("FOO");
		assertSourceContains("@SecondaryTable(name=\"FOO\")", cu);
	}
	
	//  @Entity     				-->>    @Entity
	//	@SecondaryTable(name="FOO")			@SecondaryTables({@SecondaryTable(name="FOO"), @SecondaryTable(name="BAR")})	
	public void testAddJavaTypeAnnotationNestableContainer2() throws Exception {
		ICompilationUnit cu = createTestEntityWithSecondaryTable();
		createSecondaryTablesAnnotation();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
		
		SecondaryTableAnnotation secondaryTableResource = (SecondaryTableAnnotation) typeResource.addAnnotation(1, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
		secondaryTableResource.setName("BAR");
		assertSourceContains("@SecondaryTables({@SecondaryTable(name=\"FOO\"),@SecondaryTable(name=\"BAR\")})", cu);
		
		assertNull(typeResource.getAnnotation(JPA.SECONDARY_TABLE));
		assertNotNull(typeResource.getAnnotation(JPA.SECONDARY_TABLES));
		assertEquals(2, CollectionTools.size(typeResource.annotations(JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES)));
	}
	
	//  @Entity     				
	//	@SecondaryTables(@SecondaryTable(name="FOO"))
	//           ||
	//           \/
	//  @Entity     				
	//	@SecondaryTables({@SecondaryTable(name="FOO"), @SecondaryTable(name="BAR")})
	public void testAddJavaTypeAnnotationNestableContainer3() throws Exception {
		ICompilationUnit cu = createTestEntityWithSecondaryTables();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
		
		SecondaryTableAnnotation secondaryTableResource = (SecondaryTableAnnotation) typeResource.addAnnotation(1, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
		secondaryTableResource.setName("BAR");
		assertSourceContains("@SecondaryTables({@SecondaryTable(name=\"FOO\"),@SecondaryTable(name=\"BAR\")})", cu);
		
		assertNull(typeResource.getAnnotation(JPA.SECONDARY_TABLE));
		assertNotNull(typeResource.getAnnotation(JPA.SECONDARY_TABLES));
		assertEquals(2, CollectionTools.size(typeResource.annotations(JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES)));
	}
	
	public void testAddJavaTypeAnnotationNestableContainer5() throws Exception {
		ICompilationUnit cu = createTestEntityWithSecondaryTables();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
		
		SecondaryTableAnnotation secondaryTableResource = (SecondaryTableAnnotation) typeResource.addAnnotation(0, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
		secondaryTableResource.setName("BAR");
		assertSourceContains("@SecondaryTables({@SecondaryTable(name=\"BAR\"),@SecondaryTable(name=\"FOO\")})", cu);
		
		assertNull(typeResource.getAnnotation(JPA.SECONDARY_TABLE));
		assertNotNull(typeResource.getAnnotation(JPA.SECONDARY_TABLES));
		assertEquals(2, CollectionTools.size(typeResource.annotations(JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES)));
	}

	public void testAddJavaTypeAnnotationNestableContainer6() throws Exception {
		ICompilationUnit cu = createTestEntityWithSecondaryTables();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
		
		SecondaryTableAnnotation secondaryTableResource = (SecondaryTableAnnotation) typeResource.addAnnotation(0, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
		secondaryTableResource.setName("BAR");
		assertSourceContains("@SecondaryTables({@SecondaryTable(name=\"BAR\"),@SecondaryTable(name=\"FOO\")})", cu);
		
		assertNull(typeResource.getAnnotation(JPA.SECONDARY_TABLE));
		assertNotNull(typeResource.getAnnotation(JPA.SECONDARY_TABLES));
		assertEquals(2, CollectionTools.size(typeResource.annotations(JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES)));

		secondaryTableResource = (SecondaryTableAnnotation) typeResource.addAnnotation(0, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
		secondaryTableResource.setName("BAZ");
		assertSourceContains("@SecondaryTables({@SecondaryTable(name=\"BAZ\"),@SecondaryTable(name=\"BAR\"), @SecondaryTable(name=\"FOO\")})", cu);

		assertEquals(3, CollectionTools.size(typeResource.annotations(JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES)));
	}

	//  @Entity     				
	//	@SecondaryTable(name=\"FOO\")
	//  @SecondaryTables({@SecondaryTable(name=\"BAR\"), @SecondaryTable(name=\"BAZ\")})
	//			 ||
	//           \/
	//  @Entity     				
	//	@SecondaryTable(name=\"FOO\")
	//  @SecondaryTables({@SecondaryTable(name=\"BAR\"), @SecondaryTable(name=\"BAZ\"), @SecondaryTable(name=\"BOO\")})
	public void testAddJavaTypeAnnotationNestableContainer4() throws Exception {
		ICompilationUnit cu = createTestEntityWithSecondaryTableAndSecondaryTables();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
		
		assertNotNull(typeResource.getAnnotation(JPA.SECONDARY_TABLE));
		assertNotNull(typeResource.getAnnotation(JPA.SECONDARY_TABLES));
		assertEquals(2, CollectionTools.size(typeResource.annotations(JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES)));

		SecondaryTableAnnotation secondaryTableResource = (SecondaryTableAnnotation) typeResource.addAnnotation(2, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
		assertSourceContains("@SecondaryTables({@SecondaryTable(name=\"BAR\"), @SecondaryTable(name=\"BAZ\"), @SecondaryTable})", cu);
		secondaryTableResource.setName("BOO");
		
		assertNotNull(typeResource.getAnnotation(JPA.SECONDARY_TABLE));
		assertNotNull(typeResource.getAnnotation(JPA.SECONDARY_TABLES));
		assertEquals(3, CollectionTools.size(typeResource.annotations(JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES)));

		Iterator<JavaResourceNode> secondaryTableAnnotations = typeResource.annotations(JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
		secondaryTableResource = (SecondaryTableAnnotation) secondaryTableAnnotations.next();	
		assertEquals("BAR", secondaryTableResource.getName());
		secondaryTableResource = (SecondaryTableAnnotation) secondaryTableAnnotations.next();	
		assertEquals("BAZ", secondaryTableResource.getName());
		secondaryTableResource = (SecondaryTableAnnotation) secondaryTableAnnotations.next();	
		assertEquals("BOO", secondaryTableResource.getName());
		
		assertSourceContains("@SecondaryTables({@SecondaryTable(name=\"BAR\"), @SecondaryTable(name=\"BAZ\"), @SecondaryTable(name=\"BOO\")})", cu);
	}

	//@Entity
	//@SecondaryTable(name="FOO")
	public void testRemoveJavaTypeAnnotationNestableContainer() throws Exception {
		ICompilationUnit cu = createTestEntityWithSecondaryTable();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
	
		typeResource.removeAnnotation(0, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
		
		assertSourceDoesNotContain("@SecondaryTable", cu);
	}
	

	//@Entity
	//@SecondaryTables(@SecondaryTable(name="FOO"))
	public void testRemoveJavaTypeAnnotationNestableContainer2() throws Exception {
		ICompilationUnit cu = createTestEntityWithSecondaryTables();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
	
		typeResource.removeAnnotation(0, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
		
		assertSourceDoesNotContain("@SecondaryTable", cu);
		assertSourceDoesNotContain("@SecondaryTables", cu);
	}
	
	public void testRemoveJavaTypeAnnotationIndex() throws Exception {
		ICompilationUnit cu = createTestEntityWith2SecondaryTables();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
	
		typeResource.removeAnnotation(0, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
		
		assertSourceDoesNotContain("@SecondaryTable(name=\"FOO\"", cu);
		assertSourceContains("@SecondaryTable(name=\"BAR\"", cu);
		assertSourceDoesNotContain("@SecondaryTables", cu);
	}
	
	public void testRemoveJavaTypeAnnotationIndex2() throws Exception {
		ICompilationUnit cu = createTestEntityWith2SecondaryTables();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
	
		SecondaryTableAnnotation newAnnotation = (SecondaryTableAnnotation)typeResource.addAnnotation(2, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
		newAnnotation.setName("BAZ");
		assertSourceContains("@SecondaryTables({@SecondaryTable(name=\"FOO\"), @SecondaryTable(name=\"BAR\"), @SecondaryTable(name=\"BAZ", cu);
		
		typeResource.removeAnnotation(1, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
		assertSourceContains("@SecondaryTables({@SecondaryTable(name=\"FOO\"), @SecondaryTable(name=\"BAZ\")})", cu);
	}
	
	public void testMoveJavaTypeAnnotation() throws Exception {
		ICompilationUnit cu = createTestEntityWith2SecondaryTables();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
	
		SecondaryTableAnnotation newAnnotation = (SecondaryTableAnnotation)typeResource.addAnnotation(2, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
		newAnnotation.setName("BAZ");
		assertSourceContains("@SecondaryTables({@SecondaryTable(name=\"FOO\"), @SecondaryTable(name=\"BAR\"), @SecondaryTable(name=\"BAZ\")})", cu);
		
		typeResource.move(0, 2, JPA.SECONDARY_TABLES);
		assertSourceContains("@SecondaryTables({@SecondaryTable(name=\"BAZ\"), @SecondaryTable(name=\"FOO\"), @SecondaryTable(name=\"BAR\")})", cu);
	}
	
	public void testMoveJavaTypeAnnotation2() throws Exception {
		ICompilationUnit cu = createTestEntityWith2SecondaryTables();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
	
		SecondaryTableAnnotation newAnnotation = (SecondaryTableAnnotation)typeResource.addAnnotation(2, JPA.SECONDARY_TABLE, JPA.SECONDARY_TABLES);
		newAnnotation.setName("BAZ");
		assertSourceContains("@SecondaryTables({@SecondaryTable(name=\"FOO\"), @SecondaryTable(name=\"BAR\"), @SecondaryTable(name=\"BAZ\")})", cu);
		
		typeResource.move(2, 0, JPA.SECONDARY_TABLES);
		assertSourceContains("@SecondaryTables({@SecondaryTable(name=\"BAR\"), @SecondaryTable(name=\"BAZ\"), @SecondaryTable(name=\"FOO\")})", cu);
	}
	
	public void testNestedTypes() throws Exception {
		ICompilationUnit cu = createTestEntityWithMemberTypes();
		JavaResourcePersistentType persistentType = buildJavaTypeResource(cu);
		
		assertEquals("only persistable types should be returned by nestedTypes()", 1, CollectionTools.size(persistentType.nestedTypes()));
		
		@SuppressWarnings("unchecked")
		List<JavaResourcePersistentType> nestedTypes = (List<JavaResourcePersistentType>) ClassTools.fieldValue(persistentType, "nestedTypes");
		
		assertEquals("enums and interfaces should be ignored", 2, CollectionTools.size(nestedTypes));
		
	}
	
	public void testDuplicateEntityAnnotations() throws Exception {
		ICompilationUnit cu = createTestEntityDuplicates();
		JavaResourcePersistentType persistentType = buildJavaTypeResource(cu);
		
		EntityAnnotation javaTypeMappingAnnotation = (EntityAnnotation) persistentType.getMappingAnnotation(JPA.ENTITY);
		assertEquals("FirstEntity", javaTypeMappingAnnotation.getName());
		
		assertEquals(1, persistentType.mappingAnnotationsSize());
		
		javaTypeMappingAnnotation = (EntityAnnotation) persistentType.getMappingAnnotation();
		assertEquals("FirstEntity", javaTypeMappingAnnotation.getName());
		
	}
	
	
	public void testAttributes() throws Exception {
		// TODO	
	}
	
	public void testFields() throws Exception {
		// TODO
	}
	
	public void testProperties() throws Exception {
		// TODO	
	}
	
	public void testGetAccessNoAttributesAnnotated() throws Exception {
		ICompilationUnit cu = createTestEntity();
		JavaResourcePersistentType persistentType = buildJavaTypeResource(cu);
		
		assertNull(persistentType.getAccess());
	}
	
	public void testGetAccessFieldsAnnotated() throws Exception {
		ICompilationUnit cu = createTestEntityAnnotatedField();
		JavaResourcePersistentType persistentType = buildJavaTypeResource(cu);
		
		assertEquals(AccessType.FIELD, persistentType.getAccess());
	}
	
	public void testGetAccessMethodsAnnotated() throws Exception {
		ICompilationUnit cu = createTestEntityAnnotatedMethod();
		JavaResourcePersistentType persistentType = buildJavaTypeResource(cu);
		
		assertEquals(AccessType.PROPERTY, persistentType.getAccess());
	}
	
	public void testGetAccessFieldsAndMethodsAnnotated() throws Exception {
		ICompilationUnit cu = createTestEntityAnnotatedFieldAndMethod();
		JavaResourcePersistentType persistentType = buildJavaTypeResource(cu);
		
		assertEquals(AccessType.FIELD, persistentType.getAccess());
	}
	
	public void testGetAccessNonPersistableMethodAnnotated() throws Exception {
		ICompilationUnit cu = createTestEntityAnnotatedNonPersistableMethod();
		JavaResourcePersistentType persistentType = buildJavaTypeResource(cu);
		
		assertNull(persistentType.getAccess());
	}
	
	public void testGetAccessPersistableMethodAndNonPersistableFieldAnnotated() throws Exception {
		ICompilationUnit cu = createTestEntityAnnotatedPersistableMethodNonPersistableField();
		JavaResourcePersistentType persistentType = buildJavaTypeResource(cu);
		
		assertEquals(AccessType.PROPERTY, persistentType.getAccess());
	}
	
	public void testGetAccessNoPersistableFieldsAnnotated() throws Exception {
		ICompilationUnit cu = createTestEntityNoPersistableFields();
		JavaResourcePersistentType persistentType = buildJavaTypeResource(cu);
		
		assertEquals(AccessType.PROPERTY, persistentType.getAccess());
	}
	
	//TODO more tests here with superclasses other than Object.
	//1. Test where the superclass does not resolve
	//2. Test a superclass that does resolve
	//3. What about a superclass that is a class file in a jar??
	//4.
	public void testGetSuperclassQualifiedName() throws Exception {
		ICompilationUnit cu = createTestEntity();
		JavaResourcePersistentType persistentType = buildJavaTypeResource(cu);
		
		assertEquals("java.lang.Object", persistentType.getSuperClassQualifiedName());
		
	}
	
	//more detailed tests in JPTToolsTests
	public void testIsPersistable() throws Exception {
		ICompilationUnit cu = createTestEntity();
		JavaResourcePersistentType persistentType = buildJavaTypeResource(cu);
		
		assertTrue(persistentType.isPersistable());
	}
	
	public void testAnnotatedMemberType() throws Exception {
		ICompilationUnit cu = this.createTestEntityWithMemberEmbeddable();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		assertNotNull(typeResource.getMappingAnnotation(JPA.ENTITY));
		assertNull(typeResource.getMappingAnnotation(JPA.EMBEDDABLE));
		
		JavaResourcePersistentType nestedType = typeResource.nestedTypes().next();
		assertNull(nestedType.getMappingAnnotation(JPA.ENTITY));
		assertNotNull(nestedType.getMappingAnnotation(JPA.EMBEDDABLE));	
	}
	
	public void testInvalidAnnotations() throws Exception {
		ICompilationUnit cu = this.createTestInvalidAnnotations();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu);
		assertEquals(0, typeResource.mappingAnnotationsSize());
		assertEquals(0, typeResource.annotationsSize());
		
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		assertEquals(0, attributeResource.mappingAnnotationsSize());
		assertEquals(0, attributeResource.annotationsSize());
	}

}
