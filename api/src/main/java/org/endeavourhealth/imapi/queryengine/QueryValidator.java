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
			if (match.getVariable() == null&&match.getNodeRef()==null)
				match.setVariable(mainEntity);

			String subject = match.getVariable();
			if (subject==null)
				subject=match.getNodeRef();
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
		if (path.getReturn() != null) {
			if (pathMap.get(subject) != null) {
				Set<String> nodeVariables = pathMap.get(subject).get(path.getPropertyRef() + "\t" + path.getIri());
				if (path.getReturn().getNodeRef() != null) {
					if (!nodeVariables.contains(path.getReturn().getNodeRef())) {
						throw new QueryException("return_ path/node variable '" + path.getReturn().getNodeRef() +
							"' must match a match path/node variable");
					}
				}
			}
			validateReturn(path.getReturn());
		}
		else {
			if (propertyMap.get(subject)!=null) {
				String valueVariable = propertyMap.get(subject).get(path.getIri());
				if (valueVariable != null) {
					path.setValueRef(valueVariable);
				}
			}
		}
	}





	private void validateMatch(Match match, String subject) throws QueryException {
		if (match.getVariable()!=null){
			variables.put(match.getVariable(), VarType.NODE);
		}
		if (match.getNodeRef()!=null){
			if (variables.get(match.getNodeRef())==null)
				throw new QueryException("match clause contains a node reference that has not been declared as a variable");
		}
		if (match.getMatch()!=null){
			for (Match subMatch:match.getMatch()){
				validateMatch(subMatch,subMatch.getVariable());
			}
		}
		subject= match.getVariable();
		if (match.getProperty()!=null) {
			for (Property where:match.getProperty()){
				validateWhere(where,subject);
			}
		}

	}





	private void validateWhere(Property where, String subject) throws QueryException {
		if (where.getVariable()!=null)
			variables.put(where.getVariable(),VarType.PATH);
		if (where.getIri() == null&&where.getParameter()==null&&where.getProperty()==null)
				throw new QueryException("Property clause has no property iri  (set @id to property iri) ir a parameter");
		if (where.getNodeRef() != null) {
				if (!variables.containsKey(where.getNodeRef()))
					throw new QueryException("Property clause variable '" + where.getNodeRef() + "' has not been declared in a match path");
		}
		if (where.getValueVariable()!=null)
			variables.put(where.getValueVariable(),VarType.NODE);
		String object=where.getValueVariable();
		if (object==null) {
			o++;
			object = "o" + o;
			where.setValueVariable(object);
		}
		propertyMap.computeIfAbsent(subject, s -> new HashMap<>())
				.put(where.getIri(), object);
		if (where.getProperty()!=null){
				for (Property property:where.getProperty())
					validateWhere(property,subject);
			}
		if (where.getMatch()!=null){
				if (where.getMatch().getVariable()==null){
					where.getMatch().setVariable(object);
				}
				validateMatch(where.getMatch(),where.getMatch().getVariable());
			}

	}

}
