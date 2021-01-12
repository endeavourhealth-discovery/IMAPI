package com.endavourhealth.dataaccess.entity;

import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(
	    name = Classification.CLASSIFICATION_WITH_CHILD_HAS_CHILDREN_MAPPING,
		entities = { 
			@EntityResult(
	            entityClass = Classification.class,
	            fields = { 
	            	@FieldResult(name = "dbid", column = "dbid"),
	            	@FieldResult(name = "module", column = "moduleDbid"),
	            	@FieldResult(name = "parent", column = "parentDbid"),
	            	@FieldResult(name = "child", column = "childDbid"),  
	            }      
	    	),	   			
	    },
	    columns = @ColumnResult(name = "childHasChildren", type = Boolean.class)           
	)

// note that a child will only be considered to have grandchildren if at least one grandchild 
// is is the same module as the child (and therefore the parent)
@NamedNativeQuery(
    name=Classification.FIND_CLASSIFICATION_BY_PARENT_IRI,
    query="SELECT c.dbid as dbid, \n"
    		+ "	  p.dbid as parentDbid, \n"
    		+ "   p.name as parentName, \n"
    		+ "   p.iri as parentIri, \n"
    		+ "   r.dbid as childDbid, \n"
    		+ "   r.name as childName, \n"
    		+ "   r.iri as childIri, \n"	
    		+ "   m.dbid as moduleDbid, \n"
    		+ "   m.iri as moduleIri, \n"
    		+ "   IF((SELECT DISTINCT gc.module FROM classification gc\n"
    		+ "     WHERE gc.module = c.module\n"
    		+ "     AND gc.parent = c.child) IS NULL, 0, 1) as childHasChildren \n"		    		
    		+ "FROM classification c\n"
    		+ "JOIN module m ON c.module = m.dbid\n"
    		+ "JOIN concept p ON p.dbid = c.parent\n"
    		+ "JOIN concept r ON r.dbid = c.child\n"
    		+ "WHERE p.iri = :parentIri\n"
    		+ "GROUP BY c.child;",
    resultSetMapping = Classification.CLASSIFICATION_WITH_CHILD_HAS_CHILDREN_MAPPING,
    resultClass = Classification.class
)

//note that a child will only be considered to have grandchildren if at least one grandchild 
//is is the same module as the child (and therefore the parent)
@NamedNativeQuery(
	    name=Classification.FIND_CLASSIFICATION_BY_PARENT_IRI_AND_CHILD_NAMESPACES,
	    query="SELECT c.dbid as dbid, \n"
	    		+ "	  p.dbid as parentDbid, \n"
	    		+ "   p.name as parentName, \n"
	    		+ "   p.iri as parentIri, \n"
	    		+ "   r.dbid as childDbid, \n"
	    		+ "   r.name as childName, \n"
	    		+ "   r.iri as childIri, \n"
	    		+ "   m.dbid as moduleDbid, \n"
	    		+ "   m.iri as moduleIri, \n"
	    		+ "   IF((SELECT DISTINCT gc.module FROM classification gc\n"
	    		+ "     WHERE gc.module = c.module\n"
	    		+ "     AND gc.parent = c.child) IS NULL, 0, 1) as childHasChildren \n"		    		
	    		+ "FROM classification c\n"
	    		+ "JOIN module m ON c.module = m.dbid\n"
	    		+ "JOIN concept p ON p.dbid = c.parent\n"
	    		+ "JOIN concept r ON r.dbid = c.child\n"
	    		+ "JOIN namespace n ON n.dbid = r.namespace\n"
	    		+ "WHERE p.iri = :parentIri\n"
	    		+ "AND n.prefix IN (:namespacePrefixes)\n"
	    		+ "GROUP BY c.child;",
	    resultSetMapping = Classification.CLASSIFICATION_WITH_CHILD_HAS_CHILDREN_MAPPING,
	    resultClass = Classification.class
)

@Entity
public class Classification {
	
	public static final String FIND_CLASSIFICATION_BY_PARENT_IRI_AND_CHILD_NAMESPACES = "Classification.findByParent_Iri_AndChild_Namespace_PrefixIn";
	public static final String FIND_CLASSIFICATION_BY_PARENT_IRI = "Classification.findByParent_Iri";
	public static final String CLASSIFICATION_WITH_CHILD_HAS_CHILDREN_MAPPING = "ClassificationChildHasChildrenMapping";

	@Id()
	private Integer dbid;
    
	@OneToOne()
    @JoinColumn(name="parent", referencedColumnName="dbid")
    private Concept parent;
    
	@OneToOne()
    @JoinColumn(name="child", referencedColumnName="dbid")
    private Concept child;
    
	@OneToOne()
    @JoinColumn(name="module", referencedColumnName="dbid")
    private Module module;

    public Classification() {
		super();
	}

	public Classification(Integer dbid, Concept parent, Concept child, Module module) {
		super();
		this.dbid = dbid;
        this.parent = parent;
        this.child = child;
        this.module = module;
	}

    public Integer getDbid() {
        return dbid;
    }

    public Classification setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public Concept getParent() {
        return parent;
    }

    public Classification setParent(Concept parent) {
        this.parent = parent;
        return this;
    }

    public Concept getChild() {
        return child;
    }

    public Classification setChild(Concept child) {
        this.child = child;
        return this;
    }

    public Module getModule() {
        return module;
    }

    public Classification setModule(Module module) {
        this.module = module;
        return this;
    }
}
