// Generated from C:/Users/david/CloudStation/EhealthTrust/DiscoveryDataService/IMAPI/api/src/main/grammars\Turtlite.g4 by ANTLR 4.10.1
package org.endeavourhealth.imapi.parser.turtle;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TurtliteParser}.
 */
public interface TurtliteListener extends ParseTreeListener {
  /**
   * Enter a parse tree produced by {@link TurtliteParser#turtleDoc}.
   *
   * @param ctx the parse tree
   */
  void enterTurtleDoc(TurtliteParser.TurtleDocContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#turtleDoc}.
   *
   * @param ctx the parse tree
   */
  void exitTurtleDoc(TurtliteParser.TurtleDocContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#statement}.
   *
   * @param ctx the parse tree
   */
  void enterStatement(TurtliteParser.StatementContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#statement}.
   *
   * @param ctx the parse tree
   */
  void exitStatement(TurtliteParser.StatementContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#directive}.
   *
   * @param ctx the parse tree
   */
  void enterDirective(TurtliteParser.DirectiveContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#directive}.
   *
   * @param ctx the parse tree
   */
  void exitDirective(TurtliteParser.DirectiveContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#prefixID}.
   *
   * @param ctx the parse tree
   */
  void enterPrefixID(TurtliteParser.PrefixIDContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#prefixID}.
   *
   * @param ctx the parse tree
   */
  void exitPrefixID(TurtliteParser.PrefixIDContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#base}.
   *
   * @param ctx the parse tree
   */
  void enterBase(TurtliteParser.BaseContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#base}.
   *
   * @param ctx the parse tree
   */
  void exitBase(TurtliteParser.BaseContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#sparqlBase}.
   *
   * @param ctx the parse tree
   */
  void enterSparqlBase(TurtliteParser.SparqlBaseContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#sparqlBase}.
   *
   * @param ctx the parse tree
   */
  void exitSparqlBase(TurtliteParser.SparqlBaseContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#sparqlPrefix}.
   *
   * @param ctx the parse tree
   */
  void enterSparqlPrefix(TurtliteParser.SparqlPrefixContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#sparqlPrefix}.
   *
   * @param ctx the parse tree
   */
  void exitSparqlPrefix(TurtliteParser.SparqlPrefixContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#triples}.
   *
   * @param ctx the parse tree
   */
  void enterTriples(TurtliteParser.TriplesContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#triples}.
   *
   * @param ctx the parse tree
   */
  void exitTriples(TurtliteParser.TriplesContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#predicateObjectList}.
   *
   * @param ctx the parse tree
   */
  void enterPredicateObjectList(TurtliteParser.PredicateObjectListContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#predicateObjectList}.
   *
   * @param ctx the parse tree
   */
  void exitPredicateObjectList(TurtliteParser.PredicateObjectListContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#objectList}.
   *
   * @param ctx the parse tree
   */
  void enterObjectList(TurtliteParser.ObjectListContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#objectList}.
   *
   * @param ctx the parse tree
   */
  void exitObjectList(TurtliteParser.ObjectListContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#verb}.
   *
   * @param ctx the parse tree
   */
  void enterVerb(TurtliteParser.VerbContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#verb}.
   *
   * @param ctx the parse tree
   */
  void exitVerb(TurtliteParser.VerbContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#subject}.
   *
   * @param ctx the parse tree
   */
  void enterSubject(TurtliteParser.SubjectContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#subject}.
   *
   * @param ctx the parse tree
   */
  void exitSubject(TurtliteParser.SubjectContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#predicate}.
   *
   * @param ctx the parse tree
   */
  void enterPredicate(TurtliteParser.PredicateContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#predicate}.
   *
   * @param ctx the parse tree
   */
  void exitPredicate(TurtliteParser.PredicateContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#object}.
   *
   * @param ctx the parse tree
   */
  void enterObject(TurtliteParser.ObjectContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#object}.
   *
   * @param ctx the parse tree
   */
  void exitObject(TurtliteParser.ObjectContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#literal}.
   *
   * @param ctx the parse tree
   */
  void enterLiteral(TurtliteParser.LiteralContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#literal}.
   *
   * @param ctx the parse tree
   */
  void exitLiteral(TurtliteParser.LiteralContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#blankNodePropertyList}.
   *
   * @param ctx the parse tree
   */
  void enterBlankNodePropertyList(TurtliteParser.BlankNodePropertyListContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#blankNodePropertyList}.
   *
   * @param ctx the parse tree
   */
  void exitBlankNodePropertyList(TurtliteParser.BlankNodePropertyListContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#collection}.
   *
   * @param ctx the parse tree
   */
  void enterCollection(TurtliteParser.CollectionContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#collection}.
   *
   * @param ctx the parse tree
   */
  void exitCollection(TurtliteParser.CollectionContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#rdfLiteral}.
   *
   * @param ctx the parse tree
   */
  void enterRdfLiteral(TurtliteParser.RdfLiteralContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#rdfLiteral}.
   *
   * @param ctx the parse tree
   */
  void exitRdfLiteral(TurtliteParser.RdfLiteralContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#comment}.
   *
   * @param ctx the parse tree
   */
  void enterComment(TurtliteParser.CommentContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#comment}.
   *
   * @param ctx the parse tree
   */
  void exitComment(TurtliteParser.CommentContext ctx);

  /**
   * Enter a parse tree produced by {@link TurtliteParser#iri}.
   *
   * @param ctx the parse tree
   */
  void enterIri(TurtliteParser.IriContext ctx);

  /**
   * Exit a parse tree produced by {@link TurtliteParser#iri}.
   *
   * @param ctx the parse tree
   */
  void exitIri(TurtliteParser.IriContext ctx);
}