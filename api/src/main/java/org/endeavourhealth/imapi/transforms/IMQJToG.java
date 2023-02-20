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
	TTContext context;
	private StringBuilder request;
	private int tab=0;

	public String convert(QueryRequest jRequest){
		this.jrequest= jRequest;
		this.context= jRequest.getContext();
		request= new StringBuilder();
		request.append("{");
		if (jRequest.getContext()!=null)
			convertPrefixes(jRequest.getContext());
		if (jRequest.getArgument()!=null)
			convertArguments(jRequest.getArgument());
		if (jRequest.getQuery()!=null)
			convertQuery(jRequest.getQuery());
		request.append("}");
		return beautify(request.toString());
	}

	private String getText(String text){
		return "\""+ text.replaceAll("\"","\\\"")+"\"";
	}

	private void convertQuery(Query query) {
		request
			.append("query {");
		if (query.getIri()!=null) {
			request.append("@id ").append(query.getIri());
			if (query.getName()!=null)
				request.append("|").append(getText(query.getName()+"|"));
		}
		else if (query.getName()!=null)
			request.append("\nname ").append(getText(query.getName()));
		convertFrom(query.getFrom());
		if (query.getSelect()!=null){
			request.append("select [");
			for (int i=0; i<query.getSelect().size(); i++){
				if (i>0)
					request.append(", ");
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
			request.append("where {");
			convertWhere(select.getWhere());
			request.append("}");
		}
		request.append("}");
	}

	private void convertFrom(From from){
		request.append("\nfrom {");
		if (from.getBool()!=null)
			convertFromBool(from);
		else if (from.getIri()!=null||from.getId()!=null)
			convertTTAlias(from);
		if (from.getWhere()!=null){
			request.append("\nwhere {");
			convertWhere(from.getWhere());
			request.append("}");
		}
		request.append("}");
	}

	private void convertFromBool(From from) {
		boolean first=true;
		for (From subFrom:from.getFrom()){
			if (!first)
				request.append(" ").append(from.getBool().toString()).append(" ");
			first= false;
			convertFrom(subFrom);
		}
	}


	private void convertWhere(Where where) {
		if (where.getWith()!=null)
			convertWith(where.getWith());
		if (where.getIri()!=null||where.getId()!=null)
			convertTTAlias(where);
		if (where.getBool()!=null)
			convertBoolWhere(where);
		if (where.getIn()!=null){
			request.append(" in ");
			convertWhereIn(where.getIn());
		}
		if (where.getNotIn()!=null){
			request.append(" notIn ");
			convertWhereIn(where.getNotIn());
		}
	}

	private void convertWith(With with){
		request.append(" with {");
		convertWhere(with);
		convertSortable(with);
		request.append("}");
	}

	private void convertSortable(With with) {
		if (with.getLatest()!=null)
			request.append(" ").append(with.getLatest());
		if (with.getEarliest()!=null)
			request.append(" ").append(with.getEarliest());
		if (with.getMaximum()!=null)
			request.append(" ").append(with.getMaximum());
		if (with.getMinimum()!=null)
			request.append(" ").append(with.getMinimum());
		if (with.getCount()!=null)
			request.append(" count").append(with.getCount());
	}

	private void convertBoolWhere(Where where) {
		boolean first=true;
		for (Where subWhere:where.getWhere()){
			if (!first)
				request.append(" ").append(where.getBool().toString()).append(" ");
			first= false;
			convertWhere(subWhere);
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
		if (ttAlias.getType()!=null)
			request.append("@type ");
		else if (ttAlias.getSet()!=null)
			request.append("@set ");
		else
			request.append("@id ");
		if (ttAlias.isDescendantsOf())
			request.append("<");
		if (ttAlias.isDescendantsOrSelfOf())
			request.append("<<");
		if (ttAlias.isAncestorsOf())
			request.append(">>");
		if (ttAlias.getIri()!=null)
			request.append(getIri(ttAlias.getIri()));
		else if (ttAlias.getId()!=null)
			request.append(ttAlias.getId());
		if (ttAlias.getName()!=null)
			request.append("name \"").append(ttAlias.getName())
				.append("\"");
	}


	private void convertArguments(List<Argument> arguments) {
		request.append(tabs()).append("arguments {");
		boolean first= true;
		for (Argument argument:arguments) {
			if (!first)
				request.append(", ");
			first = false;
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
				request.append("[");
				for (TTIriRef iri: argument.getValueIriList()){
					if (!iriFirst)
						request.append(", ");
					iriFirst= false;
					convertIriRef(iri);
				}
			}
		}
		request.append("} ");
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

public	static String beautify(String input) {
		int tabCount = 0;

		StringBuilder inputBuilder = new StringBuilder();
		char[] inputChar = input.toCharArray();

		for (int i = 0; i < inputChar.length; i++) {
			String charI = String.valueOf(inputChar[i]);
			if (charI.equals("}") || charI.equals("]")) {
				tabCount--;
				if (!String.valueOf(inputChar[i - 1]).equals("[") && !String.valueOf(inputChar[i - 1]).equals("{"))
					inputBuilder.append(newLine(tabCount));
			}
			inputBuilder.append(charI);

			if (charI.equals("{") || charI.equals("[")) {
				tabCount++;
				if (String.valueOf(inputChar[i + 1]).equals("]") || String.valueOf(inputChar[i + 1]).equals("}"))
					continue;

				inputBuilder.append(newLine(tabCount));
			}

			if (charI.equals(",")) {
				inputBuilder.append(newLine(tabCount));
			}
		}

		return inputBuilder.toString();
	}

	private static String newLine(int tabCount) {
		StringBuilder builder = new StringBuilder();

		builder.append("\n");
		for (int j = 0; j < tabCount; j++)
			builder.append("  ");

		return builder.toString();
	}




}
