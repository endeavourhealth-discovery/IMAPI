package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.parser.owlfs.OWLFSBaseVisitor;
import org.endeavourhealth.imapi.parser.owlfs.OWLFSLexer;
import org.endeavourhealth.imapi.parser.owlfs.OWLFSParser;

/**
 * Converts Functional syntax to Endeavour/ Discovery syntax using an ANTLR parser
 */
public class OWLToTT extends OWLFSBaseVisitor {
  private final OWLFSLexer lexer;
  private final OWLFSParser parser;
  private TTEntity entity;
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

  private void addType(TTEntity entity, TTIriRefExtended type) {
    if ( entity.get(new TTIriRefExtended(RdfVocab. TYPE)) ==null){
      TTArray types = new TTArray();
      entity.set(new TTIriRefExtended(RdfVocab. TYPE),types);
    } else{
      TTArray types = entity.get(new TTIriRefExtended(RdfVocab. TYPE));
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
      addType(entity, new TTIriRefExtended(OwlVocab.REFLEXIVE));
    } else if (ctx.transitiveObjectProperty() != null) {
      addType(entity, new TTIriRefExtended(OwlVocab.TRANSITIVE));
    }

    return null;
  }

  @Override
  public Object visitSubClassOf(OWLFSParser.SubClassOfContext ctx) {
    if (!isGCI(ctx)) {
      TTArray subClassOf = addArrayAxiom(new TTIriRefExtended(RdfsVocab.SUBCLASS_OF));
      subClassOf.add(convertClassExpression(ctx.superClass().classExpression()));
    }
    return this.defaultResult();
  }

  private TTArray addArrayAxiom(TTIriRefExtended predicate) {
    if (entity.get(predicate) == null) {
      TTArray array = new TTArray();
      entity.set(predicate, array);
    }
    return entity.get(predicate);
  }

  @Override
  public Object visitEquivalentClasses(OWLFSParser.EquivalentClassesContext ctx) {
    TTArray equivalent = addArrayAxiom(new TTIriRefExtended(OwlVocab.EQUIVALENT_CLASS));
    equivalent.add(convertClassExpression(ctx.classExpression().get(1)));
    return null;
  }

  @Override
  public Object visitSubObjectPropertyOf(OWLFSParser.SubObjectPropertyOfContext ctx) {

    if (ctx.subObjectPropertyExpression().propertyExpressionChain() != null) {
      entity.set(new TTIriRefExtended(OwlVocab.PROPERTY_CHAIN),
        convertPropertyChain(ctx.subObjectPropertyExpression().propertyExpressionChain()));
    } else {
      TTArray superProp = addArrayAxiom(new TTIriRefExtended(RdfsVocab.SUB_PROPERTY_OF));
      superProp.add(new TTIriRefExtended(expand(ctx.superObjectPropertyExpression()
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
      chain.add(new TTIriRefExtended(expand(opcs.objectProperty().iri().getText())));
    }
    return chain;
  }

  private TTValue convertClassExpression(OWLFSParser.ClassExpressionContext ctx) {
    if (ctx.iri() != null)
      return new TTIriRefExtended(expand(ctx.getText()));
    else if (ctx.objectIntersectionOf() != null) {
      TTNode exp = new TTNode();
      TTArray inters = new TTArray();
      exp.set(new TTIriRefExtended(OwlVocab.INTERSECTION_OF), inters);
      for (OWLFSParser.ClassExpressionContext ctxInter : ctx.objectIntersectionOf().classExpression()) {
        inters.add(convertClassExpression(ctxInter));
      }
      return exp;
    } else if (ctx.objectSomeValuesFrom() != null) {
      TTNode exp = new TTNode();
      exp.set(new TTIriRefExtended(RdfVocab. TYPE),new TTIriRefExtended(OwlVocab.RESTRICTION));
      exp.set(new TTIriRefExtended(OwlVocab.ON_PROPERTY), new TTIriRefExtended(expand(ctx.objectSomeValuesFrom()
        .objectPropertyExpression()
        .objectProperty()
        .iri()
        .getText())));
      exp.set(new TTIriRefExtended(OwlVocab.SOME_VALUES_FROM),
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
