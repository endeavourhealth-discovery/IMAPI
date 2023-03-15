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
		this.override = override;
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
		if (iri.getIri()!=null){
			return localName(iri.getIri());
		}
		else if (iri.getSet()!=null)
			return localName(iri.getSet());
		else if (iri.getType()!=null)
			return localName(iri.getType());
		else
			if (iri.getVariable()!=null)
				return " {"+ iri.getVariable()+"} ";
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
		if (!override && match.getDescription()!=null)
				return;
		StringBuilder summary= new StringBuilder();
		if (match.getWith()!=null){
			summariseWith(match.getWith());
			match.setDescription(match.getWith().getDescription());
		}
		else {
			if (match.getWhere() != null) {
				summariseWhereProperty(summary, match);
				for (Where subWhere : match.getWhere()) {
					summariseWhere(subWhere);
				}
			}

			if (match.getWhere() == null)
				summariseWhereProperty(summary, match);
		}


	}

	private void summariseFrom(From from) {
		if (!override && from.getDescription()!=null)
				return;
		if (from.getIri() != null)
			from.setDescription(summariseAlias(from));
		if (from.getFrom()!= null) {
			for (From subFrom : from.getFrom()) {
				summariseFrom(subFrom);
			}
		}
		if (from.getWhere() != null) {
			for (Where where : from.getWhere())
				summariseWhere(where);
		}
	}


	private boolean isDate(TTAlias reference){
		if (reference.getIri()!=null) {
			if (reference.getIri().contains("date")) {
				return true;
			}
			else if (reference.getIri().contains("Date")) {
				return true;
			}
			else return false;
		}
		else if (reference.getAlias()!=null)
			if (reference.getAlias().contains("date"))
				return true;
			else return false;
		else return false;
	}

	private void summariseWith(With with) {
		if (!override && with.getDescription()!=null)
				return;
		summariseWhere(with);
		String whereLabel= with.getDescription();
		String orderLabel="";
		if (with.getOrderBy()!=null) {
			if (isDate(with.getOrderBy())) {
				if (with.getDirection() == Order.descending) {
					orderLabel = ", get latest ";
				}
				else
					orderLabel = ", get earliest ";
			}
			else {
				if (with.getDirection() == Order.descending) {
					orderLabel = ", get Maximum ";
				}
				else
					orderLabel = ", get Minimum ";
			}
			if (with.getCount() > 1) {
				orderLabel= orderLabel + with.getCount() + " ";
			}
		}
		String thenLabel="";
		if (with.getThen()!=null){
			summariseWhere(with.getThen());
			thenLabel= ", then test : "+with.getThen().getDescription();
		}
		with.setDescription(whereLabel+orderLabel+thenLabel);
	}

	public void summariseWhereProperty(StringBuilder summary,Where where) {
		if (!override && where.getDescription() != null)
			return;
		if (where.getIri() != null) {
			summary.append(summariseAlias(where));
		}

		if (where.getIn() != null) {
			summariseWherePropertyAppendIn(summary, where);
		} else if (where.getNotIn() != null) {
			summariseWherePropertyAppendNotIn(summary, where);
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

	private void summariseWherePropertyAppendIn(StringBuilder summary, Where where) {
		int i = 0;
		summary.append(" is : ");
		if (where.getValueLabel()!=null){
			summary.append(where.getValueLabel());
		}
		else {
			for (TTAlias in : where.getIn()) {
				i++;
				if (i == 1) {

					summary.append(summariseAlias(in));
				}
				else
					summary.append(", ");
				if (i > 3)
					summary.append(" .. ");
			}
		}
	}

	private void summariseWherePropertyAppendNotIn(StringBuilder summary, Where where) {
		summary.append(" not in");
		int i = 0;
		for (TTAlias in : where.getNotIn()) {
			i++;
			if (i == 1) {
				summary.append(summariseAlias(in));
			} else
				summary.append(", ");
			if (i > 3)
				summary.append(" ...");
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
		if (value.getRelativeTo()!=null && !value.getRelativeTo().equals("$referenceDate"))
				result= result+ " relative to "+ resolveVariable(value.getRelativeTo());
		return result;
	}

	private String resolveVariable(String variable){
		return "tbd";
	}


}
