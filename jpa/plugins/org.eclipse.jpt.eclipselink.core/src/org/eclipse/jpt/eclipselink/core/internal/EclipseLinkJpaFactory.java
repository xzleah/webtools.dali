/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal;

import org.eclipse.jpt.core.context.JpaContextNode;
import org.eclipse.jpt.core.context.XmlContextNode;
import org.eclipse.jpt.core.context.java.JavaBasicMapping;
import org.eclipse.jpt.core.context.java.JavaEmbeddable;
import org.eclipse.jpt.core.context.java.JavaIdMapping;
import org.eclipse.jpt.core.context.java.JavaManyToManyMapping;
import org.eclipse.jpt.core.context.java.JavaManyToOneMapping;
import org.eclipse.jpt.core.context.java.JavaOneToManyMapping;
import org.eclipse.jpt.core.context.java.JavaOneToOneMapping;
import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.context.java.JavaPersistentType;
import org.eclipse.jpt.core.context.java.JavaVersionMapping;
import org.eclipse.jpt.core.context.orm.EntityMappings;
import org.eclipse.jpt.core.context.orm.OrmBasicMapping;
import org.eclipse.jpt.core.context.orm.OrmEmbeddable;
import org.eclipse.jpt.core.context.orm.OrmEntity;
import org.eclipse.jpt.core.context.orm.OrmIdMapping;
import org.eclipse.jpt.core.context.orm.OrmManyToManyMapping;
import org.eclipse.jpt.core.context.orm.OrmManyToOneMapping;
import org.eclipse.jpt.core.context.orm.OrmMappedSuperclass;
import org.eclipse.jpt.core.context.orm.OrmOneToManyMapping;
import org.eclipse.jpt.core.context.orm.OrmOneToOneMapping;
import org.eclipse.jpt.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.core.context.orm.OrmPersistentTypeContext;
import org.eclipse.jpt.core.context.orm.OrmTypeMapping;
import org.eclipse.jpt.core.context.orm.OrmVersionMapping;
import org.eclipse.jpt.core.context.orm.OrmXml;
import org.eclipse.jpt.core.context.persistence.MappingFileRef;
import org.eclipse.jpt.core.context.persistence.Persistence;
import org.eclipse.jpt.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.core.internal.platform.GenericJpaFactory;
import org.eclipse.jpt.core.resource.common.JpaXmlResource;
import org.eclipse.jpt.core.resource.orm.XmlBasic;
import org.eclipse.jpt.core.resource.orm.XmlEntityMappings;
import org.eclipse.jpt.core.resource.orm.XmlId;
import org.eclipse.jpt.core.resource.orm.XmlManyToMany;
import org.eclipse.jpt.core.resource.orm.XmlManyToOne;
import org.eclipse.jpt.core.resource.orm.XmlOneToMany;
import org.eclipse.jpt.core.resource.orm.XmlOneToOne;
import org.eclipse.jpt.core.resource.orm.XmlVersion;
import org.eclipse.jpt.core.resource.persistence.XmlPersistenceUnit;
import org.eclipse.jpt.eclipselink.core.EclipseLinkJpaFile;
import org.eclipse.jpt.eclipselink.core.context.java.EclipseLinkJavaEntity;
import org.eclipse.jpt.eclipselink.core.context.java.EclipseLinkJavaMappedSuperclass;
import org.eclipse.jpt.eclipselink.core.internal.context.java.EclipseLinkJavaBasicMappingImpl;
import org.eclipse.jpt.eclipselink.core.internal.context.java.EclipseLinkJavaEmbeddableImpl;
import org.eclipse.jpt.eclipselink.core.internal.context.java.EclipseLinkJavaEntityImpl;
import org.eclipse.jpt.eclipselink.core.internal.context.java.EclipseLinkJavaIdMappingImpl;
import org.eclipse.jpt.eclipselink.core.internal.context.java.EclipseLinkJavaManyToManyMappingImpl;
import org.eclipse.jpt.eclipselink.core.internal.context.java.EclipseLinkJavaManyToOneMappingImpl;
import org.eclipse.jpt.eclipselink.core.internal.context.java.EclipseLinkJavaMappedSuperclassImpl;
import org.eclipse.jpt.eclipselink.core.internal.context.java.EclipseLinkJavaOneToManyMappingImpl;
import org.eclipse.jpt.eclipselink.core.internal.context.java.EclipseLinkJavaOneToOneMappingImpl;
import org.eclipse.jpt.eclipselink.core.internal.context.java.EclipseLinkJavaVersionMappingImpl;
import org.eclipse.jpt.eclipselink.core.internal.context.java.JavaBasicCollectionMapping;
import org.eclipse.jpt.eclipselink.core.internal.context.java.JavaBasicMapMapping;
import org.eclipse.jpt.eclipselink.core.internal.context.java.JavaTransformationMapping;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkEntityMappingsImpl;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkOrmBasicMapping;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkOrmEmbeddableImpl;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkOrmEntityImpl;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkOrmIdMapping;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkOrmManyToManyMapping;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkOrmManyToOneMapping;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkOrmMappedSuperclassImpl;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkOrmOneToManyMapping;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkOrmOneToOneMapping;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkOrmPersistentType;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkOrmVersionMapping;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkOrmXml;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkVirtualXmlBasic;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkVirtualXmlId;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkVirtualXmlManyToMany;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkVirtualXmlManyToOne;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkVirtualXmlOneToMany;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkVirtualXmlOneToOne;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkVirtualXmlVersion;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.OrmBasicCollectionMapping;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.OrmBasicMapMapping;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.OrmTransformationMapping;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.VirtualXmlBasicCollection;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.VirtualXmlBasicMap;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.VirtualXmlTransformation;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.EclipseLinkPersistenceUnit;
import org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmResource;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlBasicCollection;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlBasicMap;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlTransformation;

public class EclipseLinkJpaFactory
	extends GenericJpaFactory
{
	protected EclipseLinkJpaFactory() {
		super();
	}
		
	
	// **************** Context objects ****************************************
	
	@Override
	public XmlContextNode buildContextNode(JpaContextNode parent, JpaXmlResource resource) {
		if (resource.getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return buildEclipseLinkOrmXml((MappingFileRef) parent, (EclipseLinkOrmResource) resource);
		}
		return super.buildContextNode(parent, resource);
	}
	
	protected EclipseLinkOrmXml buildEclipseLinkOrmXml(MappingFileRef parent, EclipseLinkOrmResource resource) {
		return new EclipseLinkOrmXml(parent, resource);
	}
	
	
	// **************** persistence context objects ****************************
	
	@Override
	public PersistenceUnit buildPersistenceUnit(Persistence parent, XmlPersistenceUnit persistenceUnit) {
		return new EclipseLinkPersistenceUnit(parent, persistenceUnit);
	}
	
	@Override
	public EntityMappings buildEntityMappings(OrmXml parent, XmlEntityMappings xmlEntityMappings) {
		if (parent.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkEntityMappingsImpl((EclipseLinkOrmXml) parent, (org.eclipse.jpt.eclipselink.core.resource.orm.XmlEntityMappings) xmlEntityMappings);
		}
		return super.buildEntityMappings(parent, xmlEntityMappings);
	}
	
	
	// **************** orm resource objects ***********************************
	
	@Override
	public XmlBasic buildVirtualXmlBasic(OrmTypeMapping ormTypeMapping, JavaBasicMapping javaBasicMapping) {
		if (ormTypeMapping.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkVirtualXmlBasic(ormTypeMapping, javaBasicMapping);
		}
		return super.buildVirtualXmlBasic(ormTypeMapping, javaBasicMapping);
	}
	
	@Override
	public XmlId buildVirtualXmlId(OrmTypeMapping ormTypeMapping, JavaIdMapping javaIdMapping) {
		if (ormTypeMapping.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkVirtualXmlId(ormTypeMapping, javaIdMapping);
		}
		return super.buildVirtualXmlId(ormTypeMapping, javaIdMapping);
	}
	
	@Override
	public XmlManyToMany buildVirtualXmlManyToMany(OrmTypeMapping ormTypeMapping, JavaManyToManyMapping javaManyToManyMapping) {
		if (ormTypeMapping.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkVirtualXmlManyToMany(ormTypeMapping, javaManyToManyMapping);
		}
		return super.buildVirtualXmlManyToMany(ormTypeMapping, javaManyToManyMapping);
	}
	
	@Override
	public XmlManyToOne buildVirtualXmlManyToOne(OrmTypeMapping ormTypeMapping, JavaManyToOneMapping javaManyToOneMapping) {
		if (ormTypeMapping.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkVirtualXmlManyToOne(ormTypeMapping, javaManyToOneMapping);
		}
		return super.buildVirtualXmlManyToOne(ormTypeMapping, javaManyToOneMapping);
	}
	
	@Override
	public XmlOneToMany buildVirtualXmlOneToMany(OrmTypeMapping ormTypeMapping, JavaOneToManyMapping javaOneToManyMapping) {
		if (ormTypeMapping.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkVirtualXmlOneToMany(ormTypeMapping, javaOneToManyMapping);
		}
		return super.buildVirtualXmlOneToMany(ormTypeMapping, javaOneToManyMapping);
	}
	
	@Override
	public XmlOneToOne buildVirtualXmlOneToOne(OrmTypeMapping ormTypeMapping, JavaOneToOneMapping javaOneToOneMapping) {
		if (ormTypeMapping.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkVirtualXmlOneToOne(ormTypeMapping, javaOneToOneMapping);
		}
		return super.buildVirtualXmlOneToOne(ormTypeMapping, javaOneToOneMapping);
	}
	
	@Override
	public XmlVersion buildVirtualXmlVersion(OrmTypeMapping ormTypeMapping, JavaVersionMapping javaVersionMapping) {
		if (ormTypeMapping.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkVirtualXmlVersion(ormTypeMapping, javaVersionMapping);
		}
		return super.buildVirtualXmlVersion(ormTypeMapping, javaVersionMapping);
	}
	
	
	// **************** eclipselink orm context objects ************************

	@Override
	public OrmPersistentType buildOrmPersistentType(OrmPersistentTypeContext parent, String mappingKey) {
		if (parent.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkOrmPersistentType(parent, mappingKey);
		}
		return super.buildOrmPersistentType(parent, mappingKey);
	}
	
	@Override
	public OrmEmbeddable buildOrmEmbeddable(OrmPersistentType parent) {
		if (parent.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkOrmEmbeddableImpl(parent);
		}
		return super.buildOrmEmbeddable(parent);
	}
	
	@Override
	public OrmEntity buildOrmEntity(OrmPersistentType parent) {
		if (parent.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkOrmEntityImpl(parent);
		}
		return super.buildOrmEntity(parent);
	}
	
	@Override
	public OrmMappedSuperclass buildOrmMappedSuperclass(OrmPersistentType parent) {
		if (parent.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkOrmMappedSuperclassImpl(parent);
		}
		return super.buildOrmMappedSuperclass(parent);
	}
	
	@Override
	public OrmIdMapping buildOrmIdMapping(OrmPersistentAttribute parent) {
		if (parent.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkOrmIdMapping(parent);
		}
		return super.buildOrmIdMapping(parent);
	}
	
	@Override
	public OrmBasicMapping buildOrmBasicMapping(OrmPersistentAttribute parent) {
		if (parent.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkOrmBasicMapping(parent);
		}
		return super.buildOrmBasicMapping(parent);
	}
	
	@Override
	public OrmVersionMapping buildOrmVersionMapping(OrmPersistentAttribute parent) {
		if (parent.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkOrmVersionMapping(parent);
		}
		return super.buildOrmVersionMapping(parent);
	}
	
	@Override
	public OrmManyToOneMapping buildOrmManyToOneMapping(OrmPersistentAttribute parent) {
		if (parent.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkOrmManyToOneMapping(parent);
		}
		return super.buildOrmManyToOneMapping(parent);
	}
	
	@Override
	public OrmOneToManyMapping buildOrmOneToManyMapping(OrmPersistentAttribute parent) {
		if (parent.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkOrmOneToManyMapping(parent);
		}
		return super.buildOrmOneToManyMapping(parent);
	}
	
	@Override
	public OrmOneToOneMapping buildOrmOneToOneMapping(OrmPersistentAttribute parent) {
		if (parent.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkOrmOneToOneMapping(parent);
		}
		return super.buildOrmOneToOneMapping(parent);
	}
	
	@Override
	public OrmManyToManyMapping buildOrmManyToManyMapping(OrmPersistentAttribute parent) {
		if (parent.getEResource().getType() == EclipseLinkJpaFile.ECLIPSELINK_ORM_RESOURCE_TYPE) {
			return new EclipseLinkOrmManyToManyMapping(parent);
		}
		return super.buildOrmManyToManyMapping(parent);
	}
	
	
	// **************** java context objects ***********************************

	@Override
	public JavaBasicMapping buildJavaBasicMapping(JavaPersistentAttribute parent) {
		return new EclipseLinkJavaBasicMappingImpl(parent);
	}
	
	@Override
	public JavaEmbeddable buildJavaEmbeddable(JavaPersistentType parent) {
		return new EclipseLinkJavaEmbeddableImpl(parent);
	}
	
	@Override
	public EclipseLinkJavaEntity buildJavaEntity(JavaPersistentType parent) {
		return new EclipseLinkJavaEntityImpl(parent);
	}
	
	@Override
	public JavaIdMapping buildJavaIdMapping(JavaPersistentAttribute parent) {
		return new EclipseLinkJavaIdMappingImpl(parent);
	}
	
	@Override
	public EclipseLinkJavaMappedSuperclass buildJavaMappedSuperclass(JavaPersistentType parent) {
		return new EclipseLinkJavaMappedSuperclassImpl(parent);
	}
	
	@Override
	public JavaVersionMapping buildJavaVersionMapping(JavaPersistentAttribute parent) {
		return new EclipseLinkJavaVersionMappingImpl(parent);
	}
	
	@Override
	public JavaOneToManyMapping buildJavaOneToManyMapping(JavaPersistentAttribute parent) {
		return new EclipseLinkJavaOneToManyMappingImpl(parent);
	}
	
	@Override
	public JavaOneToOneMapping buildJavaOneToOneMapping(JavaPersistentAttribute parent) {
		return new EclipseLinkJavaOneToOneMappingImpl(parent);
	}
	
	@Override
	public JavaManyToManyMapping buildJavaManyToManyMapping(JavaPersistentAttribute parent) {
		return new EclipseLinkJavaManyToManyMappingImpl(parent);
	}
	
	@Override
	public JavaManyToOneMapping buildJavaManyToOneMapping(JavaPersistentAttribute parent) {
		return new EclipseLinkJavaManyToOneMappingImpl(parent);
	}
	
	public JavaBasicCollectionMapping buildJavaBasicCollectionMapping(JavaPersistentAttribute parent) {
		return new JavaBasicCollectionMapping(parent);
	}
	
	public JavaBasicMapMapping buildJavaBasicMapMapping(JavaPersistentAttribute parent) {
		return new JavaBasicMapMapping(parent);
	}
	
	public JavaTransformationMapping buildJavaTransformationMapping(JavaPersistentAttribute parent) {
		return new JavaTransformationMapping(parent);
	}
	
	public OrmBasicCollectionMapping buildOrmBasicCollectionMapping(OrmPersistentAttribute parent) {
		return new OrmBasicCollectionMapping(parent);
	}
	
	public OrmBasicMapMapping buildOrmBasicMapMapping(OrmPersistentAttribute parent) {
		return new OrmBasicMapMapping(parent);
	}
	
	public OrmTransformationMapping buildOrmTransformationMapping(OrmPersistentAttribute parent) {
		return new OrmTransformationMapping(parent);
	}
	
	public XmlBasicCollection buildVirtualXmlBasicCollection(OrmTypeMapping ormTypeMapping, JavaBasicCollectionMapping javaBasicCollectionMapping) {
		return new VirtualXmlBasicCollection(ormTypeMapping, javaBasicCollectionMapping);
	}
	
	public XmlBasicMap buildVirtualXmlBasicMap(OrmTypeMapping ormTypeMapping, JavaBasicMapMapping javaBasicMapMapping) {
		return new VirtualXmlBasicMap(ormTypeMapping, javaBasicMapMapping);
	}
	
	public XmlTransformation buildVirtualXmlTransformation(OrmTypeMapping ormTypeMapping, JavaTransformationMapping javaTransformationMapping) {
		return new VirtualXmlTransformation(ormTypeMapping, javaTransformationMapping);
	}
}
