package org.endeavourhealth.imapi.queryengine;

import org.endeavourhealth.imapi.model.imq.*;

import java.util.*;

public class QueryValidator {
	private final Map<String, VarType> variables = new HashMap<>();
	private int o = 0;
	private final Set<String> resultColumns = new HashSet<>();
	private final Map<String, Map<String, Set<String>>> pathMap = new HashMap<>();
	private final Map<String, Map<String, String>> propertyMap = new HashMap<>();

	public void validateQuery(Query query) throws QueryException {
		if (query.getMatch() == null)
			throw new QueryException("Query must have match clause");
		for (Match match : query.getMatch()) {
			if (match.getVariable() != null)
				variables.put(match.getVariable(), VarType.NODE);
			String subject = match.getVariable();
			validateMatch(match, subject);
		}

		if (query.getReturn()!= null) {
			for (Return aReturn : query.getReturn()) {
				validateReturn(aReturn);
			}
		}
	}

	private void validateReturn(Return aReturn) throws QueryException {
		if (aReturn.getNodeRef() != null) {
			if (variables.get(aReturn.getNodeRef()) == null)
				throw new QueryException("return_ clause uses an unbound node reference variable (" + aReturn.getNodeRef() + ") Should it be a propertyRef?");
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
				throw new QueryException("return_ clause uses an unbound property reference variable (" + path.getPropertyRef()+") should it be a node ref?");
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

		if (match.getMatch()!=null){
			if (match.getBoolWhere()!=null|(match.getBoolPath()!=null))
				throw new QueryException("Match clause cannot contain where or path and boolean match");
			for (Match subMatch:match.getMatch()){
				if (subMatch.getVariable()==null)
					subMatch.setVariable(subject);
				variables.put(subMatch.getVariable(),VarType.NODE);
				validateMatch(subMatch,subMatch.getVariable());
			}
		}
		subject= match.getVariable();
		if (match.getPath()!=null){
			for (Path path:match.getPath())
				validatePath(subject,path);
		}
		if (match.getWhere()!=null) {
			for (Where where:match.getWhere()){
				validateWhere(where,subject);
			}
		}

	}





	private void validateWhere(Where where,String subject) throws QueryException {
		if (where.getVariable()!=null)
			variables.put(where.getVariable(),VarType.PATH);
		if (where.getWhere()!=null){
			if (where.getBoolWhere()==null)
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

	private void validatePath(String subject,Path path) throws QueryException {
		o++;
		String object;
		Match node= path.getMatch();
		if (node==null) {
			node = new Match();
			path.setMatch(node);
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
		validateMatch(path.getMatch(),object);
	}

}
