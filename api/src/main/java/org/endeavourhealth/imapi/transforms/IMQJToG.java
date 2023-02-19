package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTPrefix;

import java.util.List;
import java.util.stream.Collectors;

public class IMQJToG {
	QueryRequest jrequest;
	private StringBuilder request;
	private int tab=0;

	public String convert(QueryRequest jRequest){
		this.jrequest= jRequest;
		request= new StringBuilder();
		request.append("{\n");
		if (jRequest.getContext()!=null)
			convertPrefixes(jRequest.getContext());
		if (jRequest.getArgument()!=null)
			convertArguments(jRequest.getArgument());
		if (jRequest.getQuery()!=null)
			convertQuery(jRequest.getQuery());
		request.append("}");
		return request.toString();
	}

	private void convertQuery(Query query) {
		request
			.append("query {\n");
		convertIriRef(query);
		convertFrom(query.getFrom());
		if (query.getSelect()!=null){
			request.append("\n").append("select [");
			for (int i=0; i<query.getSelect().size(); i++){
				if (i>0)
					request.append(",\n");
				convertSelect(query.getSelect().get(i));
			}
			request.append("]");
		}
		request.append("}");
	}

	private void convertSelect(Select select) {
		request.append("{");
		convertTTAlias(select);
		if (select.getWhere()!=null){
			request.append("\n").append("where {");
			convertWhere(select.getWhere());
			request.append("}");
		}
		request.append("}");
	}

	private void convertFrom(From from){
		request.append("\nfrom {");
		convertTTAlias(from);
		if (from.getWhere()!=null){
			request.append("\n").append("where {");
			convertWhere(from.getWhere());
			request.append("\n").append("}");
		}
		request.append("}");
	}

	private void convertTTAlias(TTAlias jAlias) {
		if (jAlias.isDescendantsOrSelfOf())
			request.append("<< ");
		if (jAlias.isDescendantsOf())
			request.append("< ");
		if (jAlias.isAncestorsOf())
			request.append(">");
		String iri=null;
		if (jAlias.getIri()!=null)
			iri= jAlias.getIri();
		else if (jAlias.getType()!=null) {
			request.append("type ");
			iri = jAlias.getType();
		}
		else if (jAlias.getSet()!=null) {
			request.append("set ");
			iri = jAlias.getSet();
		}
		if (iri!=null){
			request.append(iri);
			if (jAlias.getName()!= null)
				request.append("|").append(jAlias.getName()).append("|");
			request.append(" ");
		}
		else if (jAlias.getVariable()!=null){
			String variable= jAlias.getVariable();
			if (!variable.startsWith("$"))
				request.append("$").append(variable).append(" ");
			else
				request.append(variable).append(" ");
		}
			if (jAlias.getAlias()!=null)
				request.append(" alias ").append(jAlias.getAlias());
			request.append(" \n");
	}

	private void convertWhere(Where where) {
		convertTTAlias(where);
		if (where.getIn()!=null){
			request.append(" in ");
			convertWhereIn(where.getIn());
		}
		if (where.getNotIn()!=null){
			request.append(" notIn ");
			convertWhereIn(where.getNotIn());
		}

	}

	private void convertWhereIn(List<From> in) {
		if (in.size()==1){
			convertTTAlias(in.get(0));
		}
		else {
			request.append("[");
			for (int i=0; i<in.size(); i++){
				if (i>0)
					request.append(", ");
				convertTTAlias(in.get(i));
			}
			request.append("]");
		}
	}


	private void convertIriRef(TTIriRef iriRef) {
		if (iriRef.getIri()!=null) {
			request.append(iriRef.getIri());
			if (iriRef.getName() != null)
				request.append("|").append(iriRef.getName()).append("|");
		}
		else if (iriRef.getName()!=null)
			request.append("name \"").append(iriRef.getName())
				.append("\"");
	}



	private void convertArguments(List<Argument> arguments) {
		request.append(tabs()).append("argument [");
		boolean first= true;
		for (Argument argument:arguments) {
			if (!first)
				request.append(",\n");
			first = false;
			request.append(argument.getParameter())
				.append(" : ");
			if (argument.getValueData() != null) {
				request.append("\"")
					.append(argument.getValueData())
					.append("\"");
			}
			else if (argument.getValueIri()!=null)
				request.append(argument.getValueIri().getIri());
			else if (argument.getValueIriList()!=null){
				request.append("[");
				request.append(String.join(", ",argument.getValueIriList()
					.stream()
					.map(iri-> iri.getIri())
					.collect(Collectors.toList())));
				request.append("]");
			}
		}
		request.append("\n").append(tabs());
		request.append("]\n");
		tab=tab-9;
	}

	private void convertPrefixes(TTContext context) {
		for (TTPrefix prefix: context.getPrefixes()){
			request.append(tabs())
				.append("prefix ")
				.append(prefix.getPrefix())
				.append(": ")
				.append(prefix.getIri())
				.append("\n");
		}
		request.append("\n");

	}

	private String tabs(){
		String indent="";
		for (int i=0;i<tab; i++ )
				indent= indent+" ";
		return indent;
	}



}
