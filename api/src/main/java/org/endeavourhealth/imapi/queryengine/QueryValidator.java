package org.endeavourhealth.imapi.queryengine;

import org.endeavourhealth.imapi.model.imq.*;

import java.util.*;

public class QueryValidator {
	private final Map<String, VarType> variables = new HashMap<>();
	private int o = 0;
	private String mainEntity = null;
	private final Set<String> resultColumns = new HashSet<>();
	private final Map<String, Map<String, Set<String>>> pathMap = new HashMap<>();
	private final Map<String, Map<String, String>> propertyMap = new HashMap<>();

	public void validateQuery(Query query) throws QueryException {
		if (query.getMatch() == null)
			throw new QueryException("Query must have match clause");
		mainEntity = query.getMatch().get(0).getVariable();
		if (mainEntity==null)
			mainEntity="entity";
		for (Match match : query.getMatch()) {
			if (match.getVariable() == null)
				match.setVariable(mainEntity);
			variables.put(match.getVariable(), VarType.NODE);
			String subject = match.getVariable();
			validateMatch(match, subject);
		}

		if (query.getReturn() == null) {
			query.return_(r -> r.setNodeRef(mainEntity));
		}
		if (query.getReturn()!= null) {
			for (Return aReturn : query.getReturn()) {
				if (aReturn.getNodeRef() == null) {
					aReturn.setNodeRef(mainEntity);
				}
				validateReturn(aReturn);
			}
		}
	}

	private void validateReturn(Return aReturn) throws QueryException {
		if (aReturn.getNodeRef() != null) {
			if (variables.get(aReturn.getNodeRef()) == null)
				throw new QueryException("return_ clause uses an unbound variable (" + aReturn.getNodeRef() + ")");
			if (variables.get(aReturn.getNodeRef()) != VarType.NODE)
				throw new QueryException("return_ clause uses a path variable (" + aReturn.getNodeRef() + ") ,must be a node variable");
		}

		if (aReturn.getProperty() != null) {
			for (ReturnProperty property : aReturn.getProperty()) {
				validateReturnProperty(aReturn.getNodeRef(), property);

			}
		}
	}


	private void validateReturnProperty(String subject, ReturnProperty path) throws QueryException {
		if (path.getPropertyRef() != null) {
			if (variables.get(path.getPropertyRef()) == null)
				throw new QueryException("return_ clause uses an unbound variable (" + path.getPropertyRef());
			if (variables.get(path.getPropertyRef()) != VarType.PATH)
				throw new QueryException("return_ clause uses a node variable ("
					+ path.getPropertyRef() + ") ,must be a path variable");

		}
		if (path.getNode() != null) {
			if (pathMap.get(subject) != null) {
				Set<String> nodeVariables = pathMap.get(subject).get(path.getPropertyRef() + "\t" + path.getIri());
				if (path.getNode().getNodeRef() != null) {
					if (!nodeVariables.contains(path.getNode().getNodeRef())) {
						throw new QueryException("return_ path/node variable '" + path.getNode().getNodeRef() +
							"' must match a match path/node variable");
					}
				}
			}
			validateReturn(path.getNode());
		}
		else {
			if (propertyMap.get(subject)!=null) {
				String valueVariable = propertyMap.get(subject).get(path.getIri());
				if (valueVariable != null) {
					path.setValueVariable(valueVariable);
				}
			}
		}
	}





	private void validateMatch(Match match, String subject) throws QueryException {
		if (match.getVariable()!=null){
			if (mainEntity!=null) {
				if (!match.getVariable().equals(mainEntity))
					throw new QueryException("Match clauses must all have the same main root entity variable (or none). Cartesion product queries are not supported.");
			}
			mainEntity= match.getVariable();
		}
		if (match.getMatch()!=null){
			for (Match subMatch:match.getMatch()){
				if (subMatch.getVariable()==null)
					subMatch.setVariable(subject);
				variables.put(subMatch.getVariable(),VarType.NODE);
				validateMatch(subMatch,subMatch.getVariable());
			}
		}
		subject= match.getVariable();
		if (match.getPath()!=null){
			subject=validatePath(match.getVariable(),match.getPath());
		}
		if (match.getWhere()!=null) {
			for (Where where:match.getWhere()){
				validateWhere(where,subject);
			}
		}

	}





	private void validateWhere(Where where,String subject) throws QueryException {
		if (where.getWhere()!=null){
			if (where.getBool()==null)
				throw new QueryException("Where clause has nested where without a boolean operator");
			if (where.getIri()!=null)
				throw new QueryException("Where clause cannot be both boolean and have a property test. If the intention is to traverse a graph, use match/path");
			for (Where subWhere:where.getWhere()){
				validateWhere(subWhere,subject);
			}
		}
		else {
			if (where.getIri() == null)
				throw new QueryException("Where clause has no property iri  (set @id to property iri)");
			if (where.getNodeRef() != null) {
				if (!variables.containsKey(where.getNodeRef()))
					throw new QueryException("Where clause variable '" + where.getNodeRef() + "' has not been declared in a match path");
			}
			o++;
			String object = "o" + o;
			propertyMap.computeIfAbsent(subject, s -> new HashMap<>())
				.put(where.getIri(), object);
			where.setValueVariable(object);
		}

	}

	private String validatePath(String subject, Path path){
		o++;
		String object;
		Node node= path.getNode();
		if (node==null) {
			node = new Node();
			path.setNode(node);
		}
		if (node.getVariable() != null) {
			object = node.getVariable();
		}
		else {
			o++;
			object = "o" + o;
			node.setVariable(object);
		}
		variables.put(object,VarType.NODE);
		pathMap.computeIfAbsent(subject,s-> new HashMap<>())
				.computeIfAbsent(path.getVariable()+"\t"+path.getIri(),p-> new HashSet<>())
				.add(object);

		if (path.getVariable()!=null){
			variables.put(path.getVariable(),VarType.PATH);
		}
		if (node.getPath()!=null) {
			return validatePath(object, node.getPath());
		}
		else {
			return object;
		}
	}

}