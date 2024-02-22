package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.parser.turtle.TurtliteBaseVisitor;
import org.endeavourhealth.imapi.parser.turtle.TurtliteLexer;
import org.endeavourhealth.imapi.parser.turtle.TurtliteParser;
import org.endeavourhealth.imapi.vocabulary.RDF;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class TurtleToTT extends TurtliteBaseVisitor<TTDocument> {
	private TTDocument document;
	private TTManager manager;
	private final TurtliteParser parser;
	private final TurtliteLexer lexer;
	private String turtle;
	private Map<String, TTNode> blankNodes= new HashMap<>();
	private Map<String,TTEntity> iriMap = new HashMap<>();


	/**
	 * Converts a turtle document to TTDocument containing entities
	 */
	public TurtleToTT() {
		this.lexer = new TurtliteLexer(null);
		this.parser = new TurtliteParser(null);
		this.parser.removeErrorListeners();
		this.parser.addErrorListener(new ParserErrorListener());
	}

	/**
	 * Creates and returns a TTDocument from a turtle document string
	 * @param turtle the string of turtle.
	 * @param graph the iri of the graph for the document
	 * @return the TTDocument
	 */
	public TTDocument getDocument(String turtle, TTIriRef graph) throws DataFormatException {
		this.turtle = turtle;
		lexer.setInputStream(CharStreams.fromString(turtle));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		parser.setTokenStream(tokens);
		TurtliteParser.TurtleDocContext tdoc= parser.turtleDoc();
		manager= new TTManager();
		document= new TTDocument();
		manager.setDocument(document);
		document.setContext(new TTContext());
		convertDoc(tdoc);

		return document;
	}

	private TTDocument convertDoc(TurtliteParser.TurtleDocContext tdoc) throws DataFormatException {
		if (tdoc.statement()!=null)
			for (TurtliteParser.StatementContext statement:tdoc.statement()){
				if (statement.directive()!=null){
					convertPrefix(statement.directive());
				} else if (statement.triples()!=null) {
					convertTriples(statement.triples());
				}
			}

		return document;
	}

	private void convertTriples(TurtliteParser.TriplesContext triples) throws DataFormatException {
		if (triples.subject()!=null){
			if (triples.subject().iri()!=null){
				System.out.println(triples.subject().iri().getText());
				if (triples.predicateObjectList()!=null) {
					TTEntity entity;
					if (iriMap.get(triples.subject().iri().getText())!=null)
						entity= iriMap.get(triples.subject().iri().getText());
					else {
						entity = new TTEntity();
						String iri= getIri(triples.subject().iri().getText());
						entity.setIri(iri);
						document.addEntity(entity);
						iriMap.put(iri, entity);
					}
					convertPredicates(entity, triples.predicateObjectList());
				}
			} else if (triples.subject().BlankNode()!=null){
				String text= triples.subject().BlankNode().getText();
				blankNodes.computeIfAbsent(text, t -> new TTNode());
				convertPredicates(blankNodes.get(text),triples.predicateObjectList());
			}
		}

	}

	private void convertPredicates(TTNode node, List<TurtliteParser.PredicateObjectListContext> poList) throws DataFormatException {
			TTIriRef predicate;
			for (TurtliteParser.PredicateObjectListContext po:poList){
				TurtliteParser.VerbContext verb= po.verb();
				if (verb.getText().equals("a"))
					predicate= iri(RDF.TYPE);
				else
					predicate= TTIriRef.iri(getIri(verb.predicate().iri().getText()));
				convertObjects(node,predicate,po.objectList());
			}

	}

	private void convertObjects(TTNode node, TTIriRef predicate, TurtliteParser.ObjectListContext objectList) throws DataFormatException {
		for (TurtliteParser.ObjectContext object: objectList.object()) {
			if (object.collection()!=null) {
				for (TurtliteParser.ObjectContext member : object.collection().object()) {
					node.addObject(predicate, getObjectValue(member));
				}
			}
			else {
				TTValue value = getObjectValue(object);
				node.addObject(predicate, value);
			}
		}
	}

	private TTValue getObjectValue(TurtliteParser.ObjectContext object) throws DataFormatException {
		TTValue value;
		if (object.literal() != null) {
			if (object.literal().rdfLiteral() != null)
				return TTLiteral.literal(object.literal().rdfLiteral().String().getText().replace("\"",""));
			else
				return TTLiteral.literal(object.literal().getText().replace("\"",""));
		} else if (object.iri() != null) {
			 return TTIriRef.iri(getIri(object.iri().getText()));
		} else if (object.BlankNode() != null) {
			 return getBlankNode(object.BlankNode().getText());
		} else if (object.blankNodePropertyList()!=null){
			value= new TTNode();
			convertBlankNode(value.asNode(),object.blankNodePropertyList());
			return value;
		} else if (object.collection()!=null){
				throw new DataFormatException("Unhandled collection");
		}
		else
			throw new DataFormatException("Unknown object type " + object.getText());
	}

	private void convertCollection(TTArray array, TurtliteParser.CollectionContext collection) throws DataFormatException {
		for (TurtliteParser.ObjectContext object:collection.object()){
			TTValue value= getObjectValue(object);
			array.add(value);

		}
	}

	private void convertBlankNode(TTNode value, TurtliteParser.BlankNodePropertyListContext blankNodePropertyList) throws DataFormatException {
		if (blankNodePropertyList.predicateObjectList()!=null){
			List<TurtliteParser.PredicateObjectListContext> polist= blankNodePropertyList.predicateObjectList();
			convertPredicates(value,polist);
		}
	}

	private TTValue getBlankNode(String blankIri) {
		if (blankNodes.get(blankIri)!=null)
			return blankNodes.get(blankIri);
		else {
			blankNodes.put(blankIri,new TTNode());
			return blankNodes.get(blankIri);
		}
	}

	private String getIri(String iri){
		if (iri.startsWith("<"))
			iri= iri.substring(1, iri.length() - 1);
		return document.getContext().expand(iri);
	}

	private void convertPrefix(TurtliteParser.DirectiveContext directive) {
		String prefix;
		String iri;
		if (directive.prefixID()!=null){
			prefix= directive.prefixID().PNAME_NS().getText();
			iri= directive.prefixID().IRIREF().getText();
			addPrefixIfNotPresent(prefix,iri.substring(1,iri.length()-1));
		} else if (directive.sparqlPrefix()!=null) {
			prefix = directive.sparqlPrefix().PNAME_NS().getText();
			iri = directive.sparqlPrefix().IRIREF().getText();
			addPrefixIfNotPresent(prefix, getIri(iri));
		}
	}

	private void addPrefixIfNotPresent(String prefix, String iri) {
		if (prefix.endsWith(":"))
			prefix= prefix.substring(0,prefix.length()-1);
		if (document.getContext().getNamespace(prefix)==null)
			document.getContext().add(iri,prefix);
	}
}
