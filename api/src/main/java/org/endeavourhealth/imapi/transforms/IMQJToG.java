package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTPrefix;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.util.List;

public class IMQJToG {
	QueryRequest jrequest;
	TTContext context;
	private StringBuilder request;
	private int tab;

	public String convert(QueryRequest jRequest){
		tab=0;
		this.jrequest= jRequest;
		this.context= jRequest.getContext();
		if (this.context==null) {
			this.context = getDefaultContext();
			jRequest.setContext(this.context);
		}
		request= new StringBuilder();
		if (jRequest.getContext()!=null)
			convertPrefixes(jRequest.getContext());
		if (jRequest.getArgument()!=null)
			convertArguments(jRequest.getArgument());
		if (jRequest.getQuery()!=null)
			convertQuery(jRequest.getQuery());
		return request.toString();
	}

	private String getText(String text){
		return "\""+ text.replace("\"","\\\"")+"\"";
	}

	private void convertQuery(Query query) {
		request
			.append(nl())
			.append("query {");
		tab++;

		if (query.getIri()!=null) {
			request.append(nl());
			request.append(query.getIri());
			if (query.getName()!=null)
				request.append("|").append(query.getName()).append("|");
		}
		else if (query.getName()!=null) {
			request.append(nl());
			request.append("name ").append(getText(query.getName()));
		}
		if (query.getDescription()!=null)
			request.append(nl()).append("description ").append(getText(query.getDescription()));
		if (query.isActiveOnly())
			request.append(nl()).append("activeOnly");
		if (query.getFrom()!=null)
			convertFromClause(query.getFrom());
		if (query.getSelect()!=null)
			convertSelectClause(query.getSelect());
		request.append("}");
		tab--;
	}

	private void convertSelectClause(List<Select> selectList) {
		request.append(nl()).append("select {");
		tab++;
		convertSelectList(selectList);
		tab--;
		request.append("}");
	}
	private void convertSelectList(List<Select> selectList) {
		boolean first= true;
		for (Select select:selectList){
			if (!first)
				request.append(",").append(nl());
			first= false;
			convertSelect(select);
		}
	}



	private void convertSelect(Select select) {
		request.append(nl());
		convertTTAlias(select);
		if (select.getWhere()!=null){
			tab++;
			convertWhereClause(select.getWhere());
			tab--;
		}
		if (select.getSelect()!=null) {
			tab=tab+6;
			request.append(nl());
			request.append("{");
			convertSelectList(select.getSelect());
			request.append("}");
			tab=tab-6;
		}
	}


	private void convertWhereClause(Where where) {
		tab++;
		request.append(nl())
			.append("where ");

		if (where.getIri()==null&&where.getBool()!=null){
			convertBoolWhere(where);
		}
		else {
			convertWhere(where);
		}
		tab--;
	}

	private void convertFromClause(From from) {
		request.append(nl());
		request.append("from ");
		tab++;
		convertFrom(from);
		tab--;
	}
	private void convertFrom(From from){
		request.append("{");
		if (from.isExclude())
			request.append(nl()).append(" not ");
		if (from.getBoolFrom()!=null)
			convertFromBool(from);
		else {
			if (from.getDescription() != null) {
				request.append(nl()).append("description ").append(getText(from.getDescription()));
			}
			request.append(nl());
			convertTTAlias(from);
			if (from.getWhere() != null) {
				boolean first = true;
				tab++;
				request.append(nl());
				request.append("where ");
				for (Where where : from.getWhere()) {
					if (!first) {
						String bool="and";
						if (from.getBool()!=null)
							bool=from.getBool().toString();
						request.append(nl()).append(bool).append(" ");
					}
					tab++;
					first = false;
					convertWhere(where);
					tab--;
				}
				tab--;
			}
		}
		request.append(nl()).append("}");
	}

	private void convertFromBool(From from) {
		boolean first=true;
		for (From subFrom:from.getFrom()){
			tab++;
			if (!first)
				request.append(nl()).append(from.getBoolFrom().toString()).append(" ");
			first= false;
			convertFrom(subFrom);
			tab--;
		}
	}


	private void convertWhere(Where where) {
		request.append("{");
		if (where.isExclude())
			request.append(nl()).append(" not ");
		if (where.getDescription() != null) {
			request.append(nl());
			request.append("description ").append(getText(where.getDescription()));
		}
		if (where.getIri() != null || where.getVariable() != null) {
			request.append(nl());
			convertTTAlias(where);
		}

		if (where.getWhere() != null) {
			request.append(nl()).append("where ");
			convertBoolWhere(where);
		} else if (where.getIn() != null) {
			request.append(" in ");
			convertWhereIn(where.getIn());
		} else if (where.getNotIn() != null) {
			request.append(" notIn ");
			convertWhereIn(where.getNotIn());
		}
		convertAssignable(where);
		convertRange(where);
		if (where.getValueLabel() != null) {
			request.append(nl()).append("valueLabel ").append(getText(where.getValueLabel()));
		}
		if (where.getOrderBy() != null||where.getThen()!=null) {
			convertWith(where);
		}
		request.append(nl()).append("}");
	}


	private void convertRange(Where where) {
		if (where.getRange()!=null) {
			Range range = where.getRange();
			request.append(nl());
			request.append("range ");
			convertAssignable(range.getFrom());
			request.append(" to ");
			convertAssignable(range.getTo());
		}
	}

	private void convertAssignable(Assignable measure) {
		if (measure.getOperator()!=null)
			request.append(" ").append(measure.getOperator().getValue());
		if (measure.getValue()!=null)
			request.append(" ").append(measure.getValue());
		if (measure.getUnit()!=null)
			request.append(" (").append(measure.getUnit()).append(")");
		if (measure.getRelativeTo()!=null)
			request.append(" relativeTo ").append(measure.getRelativeTo());
	}

	private void convertWith(Where with){
		convertSortable(with);
		if (with.getThen()!=null){
			request.append(nl());
			request.append("then ");
			tab++;
			convertWhere(with.getThen());
			tab--;
		}
	}

	private void convertSortable(Where with) {
		if (with.getOrderBy()!=null) {
			request.append(nl());
			request.append("orderBy ");
			convertTTAlias(with.getOrderBy());
			if (with.getDirection()== Order.descending) {
				request.append( "descending ");
			}
			else
				request.append(" ascending");
			if (with.getCount() != null)
				request.append(" count ").append(with.getCount());
		}
	}

	private void convertBoolWhere(Where where) {
		String bool= Bool.and.toString();
		if (where.getBool()!=null)
			bool= where.getBool().toString();
		boolean first=true;
		int i=0;
		for (Where subWhere:where.getWhere()){
			i++;
			if (!first) {
				request.append(nl());
				request.append(bool).append(" ");
			}
			first= false;
			tab++;
			convertWhere(subWhere);
			tab--;
		}

	}


	private void convertWhereIn(List<TTAlias> in) {
		boolean first=true;
		tab=tab+6;
		for (TTAlias from:in) {
			if (!first)
				request.append(",").append(nl());
			first = false;
			convertTTAlias(from);
		}
		tab=tab-6;
	}

	private String getShort(String iri){
			if (this.context!=null){
			 return context.prefix(iri);
		}
		return iri;
	}


	private void convertIriRef(TTIriRef iriRef) {
			request.append(getShort(iriRef.getIri()));
			if (iriRef.getName() != null)
				request.append("|").append(iriRef.getName()).append("|");
		}

	private void convertTTAlias(TTAlias ttAlias) {
		if (ttAlias.isInverse())
			request.append("inverseOf ");
		String iri;
		String variable=null;
		if (ttAlias.getType()!=null) {
			request.append("@");
			iri= ttAlias.getType();
		}
		else if (ttAlias.getSet()!=null) {
			request.append("^");
			iri=ttAlias.getSet();
		}
		else
			iri= ttAlias.getIri();
		if (ttAlias.getVariable()!=null) {
			variable = ttAlias.getVariable();
			if (!variable.startsWith("$"))
				variable = "$" + variable;
		}
		if (ttAlias.isDescendantsOrSelfOf()&&ttAlias.isAncestorsOf())
			request.append(">><<");
		else if (ttAlias.isDescendantsOrSelfOf())
			request.append("<<");
		else if (ttAlias.isAncestorsOf())
			request.append(">>");
		if (ttAlias.getVariable()!=null)
			request.append(variable);
		else if (iri!=null) {
				request.append(getShort(iri));
				if (ttAlias.getName() != null)
					request.append("|").append(ttAlias.getName()).append("|");
			}
	}


	private void convertArguments(List<Argument> arguments) {
		request.append(nl()).append("arguments {");
		boolean first= true;
		tab++;
		for (Argument argument:arguments) {
			if (!first)
				request.append(", ");
			first = false;
			request.append(nl());
			request.append(argument.getParameter())
				.append(" : ");
			if (argument.getValueData() != null) {
				request.append("\"")
					.append(argument.getValueData())
					.append("\"");
			}
			else if (argument.getValueIri()!=null)
				convertIriRef(argument.getValueIri());
			else if (argument.getValueIriList()!=null){
				boolean iriFirst= true;
				for (TTIriRef iri: argument.getValueIriList()){
					if (!iriFirst)
						request.append(", ");
					iriFirst= false;
					convertIriRef(iri);
				}
			}
		}
		request.append(nl()).append("} ");
		tab--;
	}

	private void convertPrefixes(TTContext context) {
		request.append("prefixes {");
		tab++;
		boolean first= true;
		for (TTPrefix prefix: context.getPrefixes()){
			if (!first)
				request.append(",");
			first= false;
			request.append(nl())
				.append(prefix.getPrefix())
				.append(": ")
				.append(prefix.getIri());
		}
		tab--;
		request.append(nl()).append("}");

	}



	private String nl(){
		StringBuilder indent= new StringBuilder();
		indent.append("  ".repeat(Math.max(0, tab)));
		return "\n"+ indent;
	}

	public static TTContext getDefaultContext(){
		TTContext context= new TTContext();
		context.add(IM.NAMESPACE,"im");
		context.add(RDFS.NAMESPACE,"rdfs");
		context.add(SNOMED.NAMESPACE,"sn");
		context.add(SHACL.NAMESPACE,"sh");
		context.add(XSD.NAMESPACE,"xsd");
		return context;
	}



}
