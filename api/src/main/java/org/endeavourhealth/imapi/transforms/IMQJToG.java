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
import java.util.stream.Collectors;

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
		request.append("{");
		tab++;
		if (jRequest.getContext()!=null)
			convertPrefixes(jRequest.getContext());
		if (jRequest.getArgument()!=null)
			convertArguments(jRequest.getArgument());
		if (jRequest.getQuery()!=null)
			convertQuery(jRequest.getQuery());
		request.append("}");
		return request.toString();
	}

	private String getText(String text){
		return "\""+ text.replaceAll("\"","\\\"")+"\"";
	}

	private void convertQuery(Query query) {
		request
			.append(nl())
			.append("query {");
		tab++;
		if (query.getIri()!=null) {
			request.append(nl());
			request.append("@id ").append(query.getIri());
			if (query.getName()!=null)
				request.append("|").append(getText(query.getName()+"|"));
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
		request.append(nl()).append("select ");
		tab++;
		convertSelectList(selectList);
		tab--;
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
		request.append("{");
		convertTTAlias(select);
		if (select.getWhere()!=null){
			tab++;
			convertWhereClause(select.getWhere());
			tab--;
		}
		if (select.getSelect()!=null) {
			tab++;
			convertSelectClause(select.getSelect());
			tab--;
		}
		request.append("}");
	}


	private void convertWhereClause(Where where) {
		request.append(nl());
		request.append("where ");
		tab++;
		convertWhere(where);
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
		if (from.getBool()!=null)
			convertFromBool(from);
		else {
			request.append("{");
			convertTTAlias(from);
			if (from.getDescription()!=null)
				request.append(nl()).append("description ").append(getText(from.getDescription()));
			if (from.getWhere() != null) {
				convertWhereClause(from.getWhere());
			}
			request.append("}");
		}
	}

	private void convertFromBool(From from) {
		boolean first=true;
		tab++;
		for (From subFrom:from.getFrom()){
			if (!first)
				request.append(nl()).append(" ").append(from.getBool().toString()).append(" ");
			first= false;
			convertFrom(subFrom);
		}
		tab--;
	}


	private void convertWhere(Where where) {

		if (where.getIri()!=null||where.getId()!=null||where.getVariable()!=null) {
			request.append("{");
			convertTTAlias(where);
		}
		if (where.getWith()!=null)
			convertWith(where.getWith());
		if (where.getDescription()!=null)
			request.append(nl()).append("description ").append(getText(where.getDescription()));
		if (where.getWhere()!=null) {
				convertBoolWhere(where);
		}
		else if (where.getIn()!=null){
			request.append(" in ");
			convertWhereIn(where.getIn());
		}
		else if (where.getNotIn()!=null){
			request.append(" notIn ");
			convertWhereIn(where.getNotIn());
		}
		if (where.getIri()!=null||where.getId()!=null)
			request.append("}");


	}

	private void convertWith(With with){
		request.append(" with {");
		tab++;
		convertWhere(with);
		convertSortable(with);
		request.append("}");
		tab--;
	}

	private void convertSortable(With with) {
		if (with.getLatest()!=null)
			request.append(nl()).append("latest ").append(with.getLatest());
		if (with.getEarliest()!=null)
			request.append(nl()).append("earliest ").append(with.getEarliest());
		if (with.getMaximum()!=null)
			request.append(nl()).append("maximum ").append(with.getMaximum());
		if (with.getMinimum()!=null)
			request.append(nl()).append("minimum ").append(with.getMinimum());
		if (with.getCount()!=null)
			request.append(" count ").append(with.getCount());
	}

	private void convertBoolWhere(Where where) {
		boolean first=true;
		tab++;
		for (Where subWhere:where.getWhere()){
			if (!first)
				request.append(nl()).append(" ").append(where.getBool().toString()).append(" ");
			first= false;
			convertWhere(subWhere);
		}
		tab--;
	}

	private void convertWhereIn(List<From> in) {
		boolean first=true;
		tab++;
		for (From from:in) {
			if (in.size()>1)
				request.append(nl());
			if (!first)
				request.append("or ");
			first = false;
			convertFrom(from);
		}
		tab--;
	}

	private String getIri(String iri){
			if (this.context!=null){
			 return context.prefix(iri);
		}
		return iri;
	}


	private void convertIriRef(TTIriRef iriRef) {
		if (iriRef.getIri()!=null) {
			request.append("@id ").append(getIri(iriRef.getIri()));
			if (iriRef.getName() != null)
				request.append("|").append(iriRef.getName()).append("|");
		}
		else if (iriRef.getName()!=null)
			request.append("name \"").append(iriRef.getName())
				.append("\"");
	}
	private void convertTTAlias(TTAlias ttAlias) {
		if (ttAlias.isInverse())
			request.append("inverseOf ");
		String variable=null;
		if (ttAlias.getType()!=null)
			request.append("@type ");
		else if (ttAlias.getSet()!=null)
			request.append("@set ");
		else 	if (ttAlias.getVariable()!=null) {
			variable = ttAlias.getVariable();
			if (!variable.startsWith("$"))
				variable = "$" + variable;
			request.append("@var ");
		}
		else  if (ttAlias.getIri()!=null)
			request.append("@id ");
		else if (ttAlias.getId()!=null)
			request.append("@id ");
		if (ttAlias.isDescendantsOf())
			request.append("<");
		if (ttAlias.isDescendantsOrSelfOf()&&ttAlias.isAncestorsOf())
			request.append(">><<");
		else if (ttAlias.isDescendantsOrSelfOf())
			request.append("<<");
		else if (ttAlias.isAncestorsOf())
			request.append(">>");
		if (ttAlias.getIri()!=null)
			request.append(getIri(ttAlias.getIri()));
		if (ttAlias.getType()!=null)
			request.append(getIri(ttAlias.getType()));
		if (ttAlias.getSet()!=null)
			request.append(getIri(ttAlias.getSet()));
		else if (ttAlias.getId()!=null)
			request.append(":").append(ttAlias.getId());
		else if (ttAlias.getVariable()!=null)
			request.append(variable);
		if (ttAlias.getName()!=null)
			request.append("name \"").append(ttAlias.getName())
				.append("\"");
	}


	private void convertArguments(List<Argument> arguments) {
		request.append(tabs()).append("arguments {");
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
		for (TTPrefix prefix: context.getPrefixes()){
			request.append(tabs())
				.append(nl())
				.append("prefix ")
				.append(prefix.getPrefix())
				.append(": ")
				.append(prefix.getIri());
		}
		request.append("\n");

	}

	private String tabs(){
		String indent="";
		for (int i=0;i<tab; i++ )
				indent= indent+"  ";
		return indent;
	}

	private String nl(){
		return "\n"+ tabs();
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
