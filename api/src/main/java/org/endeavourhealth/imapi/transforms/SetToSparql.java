package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;

public class SetToSparql {
  private EntityRepository entityRepository = new EntityRepository();
  private String tabs = "   ";


  public String getExpansionSparql(String entityVar, String iri) throws DataFormatException {

    Set<String> predicates = Set.of(RDFS.LABEL, IM.DEFINITION);
    TTEntity entity = entityRepository.getEntityPredicates(iri, predicates).getEntity();
    StringBuilder subQuery = new StringBuilder();
    if (entity.get(TTIriRef.iri(IM.HAS_MEMBER)) != null) {
      subQuery.append("?").append(entityVar).append(" ")
        .append("<").append(IM.IS_A)
        .append("> ?member.\n").append("?member ^<")
        .append(IM.HAS_MEMBER).append("> <").append(iri).append(">.\n");
      return subQuery.toString();
    }
    throw new DataFormatException("Set queries in IM must be pre-expanded (" + iri + ")");


  }

  private void getExpansionWhere(TTArray definition, StringBuilder subQuery) {
    if (definition.isIriRef()) {
      simpleSuperClass(definition.asIriRef(), subQuery);
    } else if (definition.asNode().get(TTIriRef.iri(SHACL.OR)) != null) {
      orClause(definition.asNode().get(TTIriRef.iri(SHACL.OR)), subQuery);

    } else if (definition.asNode().get(TTIriRef.iri(SHACL.AND)) != null) {
      boolean hasRoles = andClause(definition.asNode().get(TTIriRef.iri(SHACL.AND)), true, subQuery);
      if (hasRoles) {
        andClause(definition.asNode().get(TTIriRef.iri(SHACL.AND)), false, subQuery);
      }
    }
  }

  private void simpleSuperClass(TTIriRef superClass, StringBuilder subQuery) {
    subQuery.append(tabs).append("?entity im:isA <").append(superClass.asIriRef().getIri()).append(">.\n");

  }


  private void orClause(TTArray ors, StringBuilder subQuery) {
    subQuery.append(tabs).append("{");
    StringBuilder values = new StringBuilder();
    for (TTValue superClass : ors.getElements()) {
      if (superClass.isIriRef())
        values.append(tabs).append(iri(superClass.asIriRef().getIri())).append(" ");
    }
    if (!values.toString().equals("")) {
      subQuery.append(tabs).append(tabs).append("?entity im:isA").append(" ?superClass.\n");
      values = new StringBuilder(tabs + "VALUES ?superClass {" + values + "}\n");
      subQuery.append(tabs).append(values);

    }

    for (TTValue complexClass : ors.getElements()) {
      if (complexClass.isNode()) {

        addUnion(complexClass.asNode(), subQuery);
      }
    }
    subQuery.append(tabs + "}\n");
  }


  private void addUnion(TTNode union, StringBuilder subQuery) {

    if (union.get(TTIriRef.iri(SHACL.AND)) != null) {
      subQuery.append(tabs).append("UNION {\n");
      boolean hasRoles = andClause(union.get(TTIriRef.iri(SHACL.AND)), true, subQuery);
      subQuery.append(tabs).append("}\n");
      if (hasRoles) {
        subQuery.append(tabs).append("UNION {\n");
        andClause(union.get(TTIriRef.iri(SHACL.AND)), false, subQuery);
        subQuery.append(tabs).append("}\n");
      }
    } else {
      subQuery.append(tabs).append("UNION {\n");
      roles(union, true, subQuery); //adds a set of roles from a group.
      subQuery.append(tabs).append("}\n");
      subQuery.append(tabs).append("UNION {\n");
      roles(union, false, subQuery);
      subQuery.append(tabs).append("}");
    }
  }

  private Boolean andClause(TTArray and, boolean group, StringBuilder subqQuery) {
    boolean hasRoles = false;
    for (TTValue inter : and.getElements()) {
      if (inter.isNode() && inter.asNode().get(TTIriRef.iri(SHACL.NOT)) == null) {
        roles(inter.asNode(), group, subqQuery);
        hasRoles = true;
      }
    }
    for (TTValue inter : and.getElements()) {
      if (inter.isIriRef()) {
        simpleSuperClass(inter.asIriRef(), subqQuery);
      }
    }
    for (TTValue inter : and.getElements()) {
      if (inter.isNode() && inter.asNode().get(TTIriRef.iri(SHACL.NOT)) != null)
        notClause(inter.asNode().get(TTIriRef.iri(SHACL.NOT)).asValue(), subqQuery);
    }
    return hasRoles;
  }


  private void notClause(TTValue not, StringBuilder subQuery) {
    subQuery.append(tabs).append("MINUS {\n");
    if (not.isIriRef())
      simpleSuperClass(not.asIriRef(), subQuery);
    else if (not.isNode()) {
      if (not.asNode().get(TTIriRef.iri(SHACL.OR)) != null) {
        orClause(not.asNode().get(TTIriRef.iri(SHACL.OR)), subQuery);
      } else if (not.asNode().get(TTIriRef.iri(SHACL.AND)) != null) {
        boolean hasRoles = andClause(not.asNode().get(TTIriRef.iri(SHACL.AND)), true, subQuery);
        if (hasRoles) {
          andClause(not.asNode().get(TTIriRef.iri(SHACL.AND)), false, subQuery);
        }
      }
    }
    subQuery.append(tabs).append("}\n");
  }


  private void roles(TTNode node, boolean group, StringBuilder subQuery) {
    int count = 1;
    for (Map.Entry<TTIriRef, TTArray> entry : node.getPredicateMap().entrySet()) {
      count++;
      String obj = "?subo_" + count;
      String pred = "?subp_" + count;
      subQuery.append(tabs).append(obj).append(" im:isA ").append(iri(entry.getValue().asIriRef().getIri())).append(".\n");
      subQuery.append(tabs).append(pred).append(" im:isA ").append(iri(entry.getKey().getIri())).append(".\n");
      if (group) {
        subQuery.append(tabs).append("?roleGroup ").append(pred).append(" ").append(obj).append(".\n");
        subQuery.append(tabs + " FILTER (isBlank(?roleGroup))");
        subQuery.append(tabs).append("?superMember ").append(iri(IM.ROLE_GROUP)).append(" ?roleGroup.\n");
      } else {
        subQuery.append(tabs).append("?superMember ").append(pred).append(" ").append(obj).append(".\n");
        subQuery.append(tabs).append("  FILTER (isIri(?superMember))");
      }
    }
    subQuery.append(tabs).append("?entity ").append(iri(IM.IS_A)).append(" ?superMember.\n");

  }


  private String iri(String iri) {
    if (iri.startsWith("http") || iri.startsWith("urn:"))
      return "<" + iri + ">";
    else return iri;
  }


}
