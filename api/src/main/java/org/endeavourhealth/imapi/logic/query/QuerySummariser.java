package org.endeavourhealth.imapi.logic.query;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class QuerySummariser {
	boolean override;
	Query query;

	public QuerySummariser(Query query){
		this.query= query;
	}

	public void summarise(boolean override) {
		this.override = true;
		if (query.getFrom() != null) {
				summariseFrom(query.getFrom());
			}
	}


	public void summarise(){
		summarise(true);
	}

	public String summariseAlias(TTAlias iri){
		if (iri.getName()!=null) {
			return iri.getName();
		}
		if (iri.getId()!=null) {
			return iri.getId();
		}
		else if (iri.getIri()!=null)
				return localName(iri.getIri());
		else
			return "";
	}

	private String localName(String path){
		if (!path.contains(" "))
			return path.substring(path.lastIndexOf("#")+1);
		else {
			String[] iris= path.split(" ");
			List<String> locals= Arrays.stream(iris).sequential().map(i-> i.substring(i.lastIndexOf("#")+1)).collect(Collectors.toList());
			return String.join(", ",locals);
		}
	}

	public void summariseWhere(Where match) {
		if (!override)
			if (match.getDescription()!=null)
				return;
		StringBuilder summary= new StringBuilder();
		if (match.getWith()!=null){
			summariseWith(match.getWith());
		}
		if (match.getWhere() != null) {
				for (Where subWhere : match.getWhere()) {
					summariseWhere(subWhere);
				}
			}
		summary.append(summariseAlias(match));
		summariseWhereProperty(summary,match);


	}

	private void summariseFrom(From from) {
		if (from.getIri() != null)
			from.setDescription(summariseAlias(from));
		if (from.getFrom()!= null) {
			for (From subFrom : from.getFrom()) {
				summariseFrom(subFrom);
			}
		}
		if (from.getWhere() != null)
			summariseWhere(from.getWhere());
	}

	private void summariseWith(With with) {
		summariseWhere(with);
		if (with.getLatest()!=null) {
			with.setDescription("latest of "+ with.getDescription());
		}
		else if (with.getEarliest()!=null) {
			with.setDescription("earliest from"+ with.getDescription());
		}
	}

	public void summariseWhereProperty(StringBuilder summary,Where where) {
		if (!override)
			if (where.getDescription() != null)
				return;
		if (where.getBool() == Bool.not)
			summary.append("not = ");
		if (where.getIn() != null) {
			int i = 0;
			for (TTIriRef in : where.getIn()) {
				i++;
				if (i == 1) {
					if (in.getName() != null)
						summary.append(in.getName());
				}
				if (i == 2)
					summary.append(" (and more) ");
			}
		}
		else if (where.getNotIn()!=null){
			summary.append("not in");
			int i = 0;
			for (TTIriRef in : where.getIn()) {
				i++;
				if (i == 1) {
					if (in.getName() != null)
						summary.append(in.getName());
				}
				if (i == 2)
					summary.append(" (and more) ");
			}
		}

		if (where.getRange() != null) {
					summary.append(" ").append(summariseRange(where.getRange()));
				}
		if (where.getValue() != null) {
					summary.append(" ").append(summariseValue(where));
		}
		if (!summary.toString().equals("")) {
			if (where.getDescription() != null)
				where.setDescription(where.getDescription() + " " + summary.toString());
			else
				where.setDescription(summary.toString());
		}
	}


	private String summariseRange(Range range) {
		String unit="";
		String result="from "+ summariseValue(range.getFrom());
		result =result+"to "+ summariseValue((range.getTo()));
		return result;
	}



	private String summariseArguments(List<Argument> arguments) {
		StringBuilder summary= new StringBuilder();
		for (Argument arg:arguments){
			if (arg.getValueData()!=null) {
				if (arg.getParameter().equals("units")) {
					summary.append(" ").append(arg.getValueData().toLowerCase(Locale.ROOT));
				} else
					summary.append(" ").append(arg.getParameter()).append(" = ").append(arg.getValueData());
			}
		}
		return summary.toString();

	}





	private String summariseValue(Assignable value){
		String result= value.getOperator().getValue() + " " +
			value.getValue();
		if (value.getUnit()!=null)
			result=result+" "+ value.getUnit();
		if (value.getRelativeTo()!=null)
			if (!value.getRelativeTo().equals("$referenceDate"))
				result= result+ " relative to "+ resolveVariable(value.getRelativeTo());
		return result;
	}

	private String resolveVariable(String variable){
		return "tbd";
	}


}
