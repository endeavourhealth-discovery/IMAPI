package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.parser.owlfs.OWLFSBaseVisitor;
import org.endeavourhealth.imapi.parser.owlfs.OWLFSLexer;
import org.endeavourhealth.imapi.parser.owlfs.OWLFSParser;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

/**
 * Converts Functional syntax to Endeavour/ Discovery syntax using an ANTLR parser
 */
public class OWLToTT extends OWLFSBaseVisitor {
  private TTEntity entity;
  private OWLFSLexer lexer;
  private OWLFSParser parser;
  private TTContext context;

  public OWLToTT() {
    this.lexer = new OWLFSLexer(null);
    this.parser = new OWLFSParser(null);
  }

  /**
   * parses an owl functional syntax string to populate an Endeavour/Discovery entity
   * Note that the entity must already have been created with an IRI and consequently the subclass/ sub property expressions in OWL are skipped
   *
   * @param entity  the pre created entity
   * @param owl     string of owl functional syntax containing a single axiom
   * @param context Context object containing the prefixes and namespaces used in the owl string
   */
  public void convertAxiom(TTEntity entity, String owl, TTContext context) {

    this.entity = entity;
    this.context = context;
    lexer.setInputStream(CharStreams.fromString(owl));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    parser.setTokenStream(tokens);
    OWLFSParser.AxiomContext axiomCtx = parser.axiom();
    visitAxiom(axiomCtx);

  }

  private void addType(TTEntity entity, TTIriRef type) {
    if (entity.get(iri(RDF.TYPE)) == null) {
      TTArray types = new TTArray();
      entity.set(iri(RDF.TYPE), types);
    } else {
      TTArray types = entity.get(iri(RDF.TYPE));
      types.add(type);
    }
  }

  @Override
  public Object visitAxiom(OWLFSParser.AxiomContext ctx) {
    if (ctx.subClassOf() != null)
      return visitSubClassOf(ctx.subClassOf());
    else if (ctx.equivalentClasses() != null)
      return visitEquivalentClasses(ctx.equivalentClasses());
    else if (ctx.subObjectPropertyOf() != null)
      return visitSubObjectPropertyOf(ctx.subObjectPropertyOf());
    else if (ctx.reflexiveObjectProperty() != null) {
      addType(entity, iri(OWL.REFLEXIVE));
    } else if (ctx.transitiveObjectProperty() != null) {
      addType(entity, iri(OWL.TRANSITIVE));
    }

    return null;
  }

  @Override
  public Object visitSubClassOf(OWLFSParser.SubClassOfContext ctx) {
    if (!isGCI(ctx)) {
      TTArray subClassOf = addArrayAxiom(iri(RDFS.SUBCLASS_OF));
      subClassOf.add(convertClassExpression(ctx.superClass().classExpression()));
    }
    return this.defaultResult();
  }

  private TTArray addArrayAxiom(TTIriRef predicate) {
    if (entity.get(predicate) == null) {
      TTArray array = new TTArray();
      entity.set(predicate, array);
    }
    return entity.get(predicate);
  }

  @Override
  public Object visitEquivalentClasses(OWLFSParser.EquivalentClassesContext ctx) {
    TTArray equivalent = addArrayAxiom(iri(OWL.EQUIVALENT_CLASS));
    equivalent.add(convertClassExpression(ctx.classExpression().get(1)));
    return null;
  }

  @Override
  public Object visitSubObjectPropertyOf(OWLFSParser.SubObjectPropertyOfContext ctx) {

    if (ctx.subObjectPropertyExpression().propertyExpressionChain() != null) {
      entity.set(iri(OWL.PROPERTY_CHAIN),
        convertPropertyChain(ctx.subObjectPropertyExpression().propertyExpressionChain()));
    } else {
      TTArray superProp = addArrayAxiom(iri(RDFS.SUB_PROPERTY_OF));
      superProp.add(new TTIriRef(expand(ctx.superObjectPropertyExpression()
        .objectPropertyExpression()
        .objectProperty()
        .iri()
        .getText())));
    }
    return null;
  }

  private TTArray convertPropertyChain(OWLFSParser.PropertyExpressionChainContext chainContext) {
    TTArray chain = new TTArray();
    for (OWLFSParser.ObjectPropertyExpressionContext opcs : chainContext.objectPropertyExpression()) {
      chain.add(new TTIriRef(expand(opcs.objectProperty().iri().getText())));
    }
    return chain;
  }

  private TTValue convertClassExpression(OWLFSParser.ClassExpressionContext ctx) {
    if (ctx.iri() != null)
      return new TTIriRef(expand(ctx.getText()));
    else if (ctx.objectIntersectionOf() != null) {
      TTNode exp = new TTNode();
      TTArray inters = new TTArray();
      exp.set(iri(OWL.INTERSECTION_OF), inters);
      for (OWLFSParser.ClassExpressionContext ctxInter : ctx.objectIntersectionOf().classExpression()) {
        inters.add(convertClassExpression(ctxInter));
      }
      return exp;
    } else if (ctx.objectSomeValuesFrom() != null) {
      TTNode exp = new TTNode();
      exp.set(iri(RDF.TYPE), iri(OWL.RESTRICTION));
      exp.set(iri(OWL.ON_PROPERTY), new TTIriRef(expand(ctx.objectSomeValuesFrom()
        .objectPropertyExpression()
        .objectProperty()
        .iri()
        .getText())));
      exp.set(iri(OWL.SOME_VALUES_FROM),
        convertClassExpression(ctx.objectSomeValuesFrom().classExpression()));
      return exp;
    } else
      return null;

  }


  private boolean isGCI(OWLFSParser.SubClassOfContext ctx) {
    return (ctx.subClass().classExpression().objectIntersectionOf() != null);
  }

  private String expand(String iri) {
    return context.expand(iri);
  }

}
