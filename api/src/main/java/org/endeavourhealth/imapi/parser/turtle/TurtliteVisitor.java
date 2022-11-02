// Generated from C:/Users/david/CloudStation/EhealthTrust/DiscoveryDataService/IMAPI/api/src/main/grammars\Turtlite.g4 by ANTLR 4.10.1
package org.endeavourhealth.imapi.parser.turtle;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TurtliteParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TurtliteVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#turtleDoc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTurtleDoc(TurtliteParser.TurtleDocContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(TurtliteParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirective(TurtliteParser.DirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#prefixID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixID(TurtliteParser.PrefixIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#base}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBase(TurtliteParser.BaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#sparqlBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSparqlBase(TurtliteParser.SparqlBaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#sparqlPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSparqlPrefix(TurtliteParser.SparqlPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#triples}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriples(TurtliteParser.TriplesContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#predicateObjectList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateObjectList(TurtliteParser.PredicateObjectListContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#objectList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectList(TurtliteParser.ObjectListContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#verb}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVerb(TurtliteParser.VerbContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#subject}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubject(TurtliteParser.SubjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicate(TurtliteParser.PredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject(TurtliteParser.ObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(TurtliteParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#blankNodePropertyList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlankNodePropertyList(TurtliteParser.BlankNodePropertyListContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#collection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollection(TurtliteParser.CollectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#rdfLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRdfLiteral(TurtliteParser.RdfLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment(TurtliteParser.CommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TurtliteParser#iri}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIri(TurtliteParser.IriContext ctx);
}