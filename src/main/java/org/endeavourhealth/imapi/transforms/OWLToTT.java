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
  private TTEntityJava entity;
  private TTContextJava context;

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
  public void convertAxiom(TTEntityJava entity, String owl, TTContextJava context) {

    this.entity = entity;
    this.context = context;
    lexer.setInputStream(CharStreams.fromString(owl));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    parser.setTokenStream(tokens);
    OWLFSParser.AxiomContext axiomCtx = parser.axiom();
    visitAxiom(axiomCtx);

  }

  private void addType(TTEntityJava entity, TTIriRef type) {
    if (entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE)) == null) {
      TTArrayJava types = new TTArrayJava();
      entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE), types);
    } else {
      TTArrayJava types = entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE));
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
      addType(entity, TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.REFLEXIVE));
    } else if (ctx.transitiveObjectProperty() != null) {
      addType(entity, TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.TRANSITIVE));
    }

    return null;
  }

  @Override
  public Object visitSubClassOf(OWLFSParser.SubClassOfContext ctx) {
    if (!isGCI(ctx)) {
      TTArrayJava subClassOf = addArrayAxiom(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF));
      subClassOf.add(convertClassExpression(ctx.superClass().classExpression()));
    }
    return this.defaultResult();
  }

  private TTArrayJava addArrayAxiom(TTIriRef predicate) {
    if (entity.get(predicate) == null) {
      TTArrayJava array = new TTArrayJava();
      entity.set(predicate, array);
    }
    return entity.get(predicate);
  }

  @Override
  public Object visitEquivalentClasses(OWLFSParser.EquivalentClassesContext ctx) {
    TTArrayJava equivalent = addArrayAxiom(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.EQUIVALENT_CLASS));
    equivalent.add(convertClassExpression(ctx.classExpression().get(1)));
    return null;
  }

  @Override
  public Object visitSubObjectPropertyOf(OWLFSParser.SubObjectPropertyOfContext ctx) {

    if (ctx.subObjectPropertyExpression().propertyExpressionChain() != null) {
      entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.PROPERTY_CHAIN),
        convertPropertyChain(ctx.subObjectPropertyExpression().propertyExpressionChain()));
    } else {
      TTArrayJava superProp = addArrayAxiom(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUB_PROPERTY_OF));
      superProp.add(TTIriRefExtensionsKt.iri(new TTIriRef(), expand(ctx.superObjectPropertyExpression()
        .objectPropertyExpression()
        .objectProperty()
        .iri()
        .getText())));
    }
    return null;
  }

  private TTArrayJava convertPropertyChain(OWLFSParser.PropertyExpressionChainContext chainContext) {
    TTArrayJava chain = new TTArrayJava();
    for (OWLFSParser.ObjectPropertyExpressionContext opcs : chainContext.objectPropertyExpression()) {
      chain.add(TTIriRefExtensionsKt.iri(new TTIriRef(), expand(opcs.objectProperty().iri().getText())));
    }
    return chain;
  }

  private TTValueJava convertClassExpression(OWLFSParser.ClassExpressionContext ctx) {
    if (ctx.iri() != null)
      return TTIriRefExtensionsKt.iri(new TTIriRef(), expand(ctx.getText()));
    else if (ctx.objectIntersectionOf() != null) {
      TTNodeJava exp = new TTNodeJava();
      TTArrayJava inters = new TTArrayJava();
      exp.set(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF), inters);
      for (OWLFSParser.ClassExpressionContext ctxInter : ctx.objectIntersectionOf().classExpression()) {
        inters.add(convertClassExpression(ctxInter));
      }
      return exp;
    } else if (ctx.objectSomeValuesFrom() != null) {
      TTNodeJava exp = new TTNodeJava();
      exp.set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE), TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.RESTRICTION));
      exp.set(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.ON_PROPERTY), TTIriRefExtensionsKt.iri(new TTIriRef(), expand(ctx.objectSomeValuesFrom()
        .objectPropertyExpression()
        .objectProperty()
        .iri()
        .getText())));
      exp.set(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.SOME_VALUES_FROM),
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
