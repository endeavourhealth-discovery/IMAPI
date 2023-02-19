package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.parser.imq.IMQLexer;
import org.endeavourhealth.imapi.parser.imq.IMQParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

/**
 * Converts IMQ breif syntax to json format
 */
public class IMQGToJ {
	private final IMQParser parser;
	private final IMQLexer lexer;
	private final Map<String,String> prefixes= new HashMap<>();

	public IMQGToJ() {
		this.lexer = new IMQLexer(null);
		this.parser = new IMQParser(null);
		this.parser.removeErrorListeners();
		this.parser.addErrorListener(new ParserErrorListener());
	}


	public QueryRequest convert(String imq) throws DataFormatException {
		lexer.setInputStream(CharStreams.fromString(imq));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		parser.setTokenStream(tokens);
		IMQParser.QueryRequestContext qr= parser.queryRequest();
		QueryRequest jqr= new QueryRequest();
		if (qr.prefixDecl()!=null){
			convertPrefixes(qr.prefixDecl());
		}
		if (qr.arguments()!=null){
			convertArguments(qr.arguments(),jqr);
		}
		if (qr.query()!=null){
			convertQuery(qr.query(),jqr);
		}
		return jqr;
	}

	private void convertPrefixes(List<IMQParser.PrefixDeclContext> prefixDecl) {
		for (IMQParser.PrefixDeclContext prefix:prefixDecl){
			String iri= prefix.IRI_REF().getText();
			prefixes.put(prefix.PN_PROPERTY().getText(),iri);
		}
	}




	private String resolvePrefixed(String prefixed) throws DataFormatException {
		String resolved= prefixes.get(prefixed.split(":")[0]);
		if (resolved==null)
			throw new DataFormatException("Unresolved prefixed iri in IM text : "+ prefixed);
		return resolved;
	}

	private void convertQuery(IMQParser.QueryContext qry, QueryRequest jqr) throws DataFormatException {
		Query jq= new Query();
		jqr.setQuery(jq);
		if (qry.reference()!=null) {
			convertReference(qry.reference(), jq);
		}
		if (qry.description()!=null)
			jq.setDescription(qry.description().string().getText());

		if (qry.fromClause()!=null){
			convertMainFromClause(qry.fromClause(),jq);
		}
	}

	private void convertMainFromClause(IMQParser.FromClauseContext fromClause,Query jquery) throws DataFormatException {
		From jfrom = new From();
		jquery.setFrom(jfrom);
		if (fromClause.from() != null) {
			convertFrom(fromClause.from(), jfrom);
		}
		else if (fromClause.fromWhere()!=null){
			convertFromWhere(fromClause.fromWhere(),jfrom);
		}
		else if(fromClause.fromBoolean()!=null) {
			convertFromBoolean(fromClause.fromBoolean(), jfrom);
		}
	}

	private void convertFromBoolean(IMQParser.FromBooleanContext fromBoolean, From jfrom) throws DataFormatException {
		if (fromBoolean.orFrom()!=null) {
			convertOrFrom(fromBoolean.orFrom(),jfrom);
		}
		else if (fromBoolean.andFrom()!=null) {
			convertAndFrom(fromBoolean.andFrom(), jfrom);
		}
		else
			convertNotFrom(fromBoolean.notFrom(),jfrom);
	}

	private void convertAndFrom(IMQParser.AndFromContext andFrom, From jfrom) throws DataFormatException {
		jfrom.setBool(Bool.and);
		if (andFrom.from().size()>0) {
			for (IMQParser.FromContext from : andFrom.from()) {
				addFrom(from,jfrom);
			}
		}
		if (andFrom.fromWhere().size()>0) {
			for (IMQParser.FromWhereContext fromWhere : andFrom.fromWhere()) {
				addFromWhere(fromWhere,jfrom);
			}
		}
		if (andFrom.bracketFrom().size()>0) {
			for (IMQParser.BracketFromContext bracketFrom : andFrom.bracketFrom()) {
				addBracketFrom(bracketFrom,jfrom);
			}
		}

	}

	private void addBracketFrom(IMQParser.BracketFromContext bracketFrom, From jfrom) throws DataFormatException {
		From subFrom = new From();
		jfrom.addFrom(subFrom);
		convertBracketFrom(bracketFrom, subFrom);
	}

	private void addFromWhere(IMQParser.FromWhereContext fromWhere, From jfrom) throws DataFormatException {
		From subFrom = new From();
		jfrom.addFrom(subFrom);
		convertFromWhere(fromWhere, subFrom);
	}

	private void addFrom(IMQParser.FromContext from, From jFrom) throws DataFormatException {
		From subFrom = new From();
		jFrom.addFrom(subFrom);
		convertFrom(from, subFrom);
	}

	private void convertNotFrom(IMQParser.NotFromContext notFrom, From jfrom) throws DataFormatException {
		jfrom.setBool(Bool.not);
		if (notFrom.from().size()>0) {
			for (IMQParser.FromContext from : notFrom.from()) {
				addFrom(from,jfrom);
			}
		}
		if (notFrom.fromWhere().size()>0) {
			for (IMQParser.FromWhereContext fromWhere :notFrom.fromWhere()) {
				addFromWhere(fromWhere,jfrom);
			}
		}
		if (notFrom.bracketFrom().size()>0) {
			for (IMQParser.BracketFromContext bracketFrom : notFrom.bracketFrom()) {
				addBracketFrom(bracketFrom,jfrom);
			}
		}
	}

	private void convertOrFrom(IMQParser.OrFromContext orFrom, From jfrom) throws DataFormatException {
		jfrom.setBool(Bool.or);
		if (orFrom.from().size()>0) {
			for (IMQParser.FromContext from : orFrom.from()) {
				addFrom(from,jfrom);
			}
		}
		if (orFrom.fromWhere().size()>0) {
			for (IMQParser.FromWhereContext fromWhere :orFrom.fromWhere()) {
				addFromWhere(fromWhere,jfrom);
			}
		}
		if (orFrom.bracketFrom().size()>0) {
			for (IMQParser.BracketFromContext bracketFrom : orFrom.bracketFrom()) {
				addBracketFrom(bracketFrom,jfrom);
			}
		}
	}

	private void convertFromWhere(IMQParser.FromWhereContext fromWhere,From jfrom) throws DataFormatException {
		if (fromWhere.from()!=null)
			convertFrom(fromWhere.from(),jfrom);
		else if (fromWhere.bracketFrom()!=null) {
			convertBracketFrom(fromWhere.bracketFrom(),jfrom);
		}
		if (fromWhere.whereClause()!=null) {
			convertWhereClause(fromWhere.whereClause(), jfrom);
		}
	}

	private void convertWhereClause(IMQParser.WhereClauseContext whereClause, From jfrom) throws DataFormatException {
		Where jWhere= new Where();
		jfrom.setWhere(jWhere);
		if (whereClause.where()!=null){
			convertWhere(whereClause.where(),jWhere);
		}
		if (whereClause.whereValue()!=null){
			convertWhereValue(whereClause.whereValue(),jWhere);
		}
		if (whereClause.whereWith()!=null){
			convertWhereWith(whereClause.whereWith(),jWhere);
		}
		if (whereClause.whereBoolean()!=null){
			convertWhereBoolean(whereClause.whereBoolean(),jWhere);
		}
		if (whereClause.whereWhere()!=null){
			convertWhereWhere(whereClause.whereWhere(),jWhere);
		}
	}

	private void convertWhereWhere(IMQParser.WhereWhereContext whereWhere, Where jWhere) throws DataFormatException {
		convertWhere(whereWhere.where(),jWhere);
		Where jnestedWhere= new Where();
		jWhere.addWhere(jnestedWhere);
		convertNestedWhere(whereWhere.nestedWhere(),jnestedWhere);
	}

	private void convertNestedWhere(IMQParser.NestedWhereContext where, Where jWhere) throws DataFormatException {
		if (where.where()!=null)
			convertWhere(where.where(),jWhere);
		if (where.whereWhere()!=null)
			convertWhereWhere(where.whereWhere(),jWhere);
		if (where.whereBoolean()!=null)
			convertWhereBoolean(where.whereBoolean(),jWhere);
		if (where.whereValue()!=null)
			convertWhereValue(where.whereValue(),jWhere);
		if (where.whereWith()!=null)
			convertWhereWith(where.whereWith(),jWhere);
	}

	private void convertWhereBoolean(IMQParser.WhereBooleanContext whereBoolean, Where jWhere) {
		if (whereBoolean.orWhere()!=null) {
			convertOrWhere(whereBoolean.orWhere(),jWhere);
		}
		else if (whereBoolean.andWhere()!=null) {
			convertAndWhere(whereBoolean.andWhere(), jWhere);
		}
		else
			convertNotWhere(whereBoolean.notWhere(),jWhere);
	}

	private void convertOrWhere(IMQParser.OrWhereContext orWhere, Where jWhere) {
	}

	private void convertAndWhere(IMQParser.AndWhereContext andWhere, Where jWhere) {
	}

	private void convertNotWhere(IMQParser.NotWhereContext notWhere, Where jWhere) {
	}

	private void addWhere(IMQParser.WhereContext where, Where jWhere){

	}

	private void convertWhereWith(IMQParser.WhereWithContext whereWith, Where jWhere) throws DataFormatException {
				With jWith=new With();
				jWhere.setWith(jWith);
				convertWith(whereWith.with(),jWith);
				if (whereWith.where()!=null)
					convertWhere(whereWith.where(),jWhere);
				if (whereWith.whereValue()!=null)
					convertWhereValue(whereWith.whereValue(),jWhere);
				if (whereWith.whereBoolean()!=null)
					convertWhereBoolean(whereWith.whereBoolean(),jWhere);
	}

	private void convertWhereValue(IMQParser.WhereValueContext whereValue, Where jWhere) throws DataFormatException {
		convertWhere(whereValue.where(),jWhere);
		convertWhereValueTest(whereValue.whereValueTest(),jWhere);
	}

	private void convertWhereValueTest(IMQParser.WhereValueTestContext whereValue, Where jWhere) throws DataFormatException {
		if (whereValue.in()!=null){
			List<From> inList= new ArrayList<>();
			jWhere.setIn(inList);
			convertIn(whereValue.in(),inList);
		}
		if (whereValue.notin()!=null){
			List<From> notList= new ArrayList<>();
			jWhere.setNotIn(notList);
			convertNotIn(whereValue.notin(),notList);
		}
		if (whereValue.whereMeasure()!=null) {
			convertAssignable(whereValue.whereMeasure(),jWhere);
		}
		if (whereValue.range()!=null)
			convertRange(whereValue.range(),jWhere);
	}

	private void convertRange(IMQParser.RangeContext range, Where jWhere) {
		Range jRange= new Range();
		jWhere.setRange(jRange);
		if (range.fromRange()!=null){
			Value jFrom= new Value();
			jRange.setFrom(jFrom);
			convertAssignable(range.fromRange().whereMeasure(),jFrom);
		}
		if (range.toRange()!=null){
			Value jTo= new Value();
			jRange.setTo(jTo);
			convertAssignable(range.fromRange().whereMeasure(),jTo);
		}
	}

	private void convertAssignable(IMQParser.WhereMeasureContext whereMeasure, Assignable jassign) {
		if (whereMeasure.operator()!=null)
			jassign.setOperator(Operator.valueOf(whereMeasure.operator().getText()));
		if (whereMeasure.NUMBER()!=null)
			jassign.setValue(whereMeasure.NUMBER().getText());
		else if (whereMeasure.string()!=null)
			jassign.setValue(whereMeasure.string().getText());
		if (whereMeasure.relativeTo()!=null) {
			if (whereMeasure.relativeTo().PN_PROPERTY() != null)
				jassign.setRelativeTo(whereMeasure.relativeTo().PN_PROPERTY().getText());
			else if (whereMeasure.relativeTo().PN_VARIABLE() != null)
				jassign.setRelativeTo(whereMeasure.relativeTo().PN_VARIABLE().getText());
		}
		if (whereMeasure.units()!=null)
			jassign.setUnit(whereMeasure.units().getText());

	}


	private void addToFrom(IMQParser.FromContext from,List<From> fromList) throws DataFormatException {
		From inFrom = new From();
		fromList.add(inFrom);
		convertFrom(from,inFrom);
	}

	private void convertIn(IMQParser.InContext in, List<From> fromList) throws DataFormatException {
		if (in.from()!=null) {
			addToFrom(in.from(),fromList);
		}
		if (in.fromBoolean()!=null){
			addToFromBoolean(in.fromBoolean(),fromList);
		}
		if (in.fromWhere()!=null){
			addToFromWhere(in.fromWhere(),fromList);
		}
		if (in.bracketFrom()!=null){
			addToBracketFrom(in.bracketFrom(),fromList);
		}
		if (in.fromList()!=null){
			addFromList(in.fromList(),fromList);
		}
	}

	private void convertNotIn(IMQParser.NotinContext notIn, List<From> fromList) throws DataFormatException {
		if (notIn.from()!=null) {
			addToFrom(notIn.from(),fromList);
		}
		if (notIn.fromBoolean()!=null){
			addToFromBoolean(notIn.fromBoolean(),fromList);
		}
		if (notIn.fromWhere()!=null){
			addToFromWhere(notIn.fromWhere(),fromList);
		}
		if (notIn.bracketFrom()!=null){
			addToBracketFrom(notIn.bracketFrom(),fromList);
		}
		if (notIn.fromList()!=null){
			addFromList(notIn.fromList(),fromList);
		}
	}

	private void addToBracketFrom(IMQParser.BracketFromContext bracketFrom, List<From> fromList) throws DataFormatException {
		From inFrom= new From();
		fromList.add(inFrom);
		convertBracketFrom(bracketFrom,inFrom);
	}

	private void addToFromWhere(IMQParser.FromWhereContext fromWhere, List<From> fromList) throws DataFormatException {
		From inFrom= new From();
		fromList.add(inFrom);
		convertFromWhere(fromWhere,inFrom);
	}

	private void addToFromBoolean(IMQParser.FromBooleanContext fromBoolean, List<From> fromList) throws DataFormatException {
		From inFrom= new From();
		fromList.add(inFrom);
		convertFromBoolean(fromBoolean,inFrom);
	}

	private void addFromList(IMQParser.FromListContext fromList, List<From> jList) throws DataFormatException {
		if (fromList.from().size()>0){
			for (IMQParser.FromContext from:fromList.from()){
				addToFrom(from,jList);
			}
		}
		if (fromList.fromWhere().size()>0){
			for (IMQParser.FromWhereContext fromWhere:fromList.fromWhere()){
				addToFromWhere(fromWhere,jList);
			}
		}
		if (fromList.fromBoolean().size()>0){
			for (IMQParser.FromBooleanContext fromBoolean:fromList.fromBoolean()){
				addToFromBoolean(fromBoolean,jList);
			}
		}
		if (fromList.bracketFrom().size()>0){
			for (IMQParser.BracketFromContext bracket:fromList.bracketFrom()){
				addToBracketFrom(bracket,jList);
			}
		}
	}

	private void convertBracketFrom(IMQParser.BracketFromContext bracketFrom, From jFrom) throws DataFormatException {
		if (bracketFrom.from()!=null)
			convertFrom(bracketFrom.from(),jFrom);
		if (bracketFrom.fromWhere()!=null)
			convertFromWhere(bracketFrom.fromWhere(),jFrom);
		if (bracketFrom.fromBoolean()!=null){
			convertFromBoolean(bracketFrom.fromBoolean(),jFrom);
		}
	}

	private void convertFrom(IMQParser.FromContext from,From jfrom) throws DataFormatException {
		if (from.graph() != null) {
			jfrom.setGraph(TTAlias.iri(from.graph().IRI_REF().getText()));
		}
		if (from.description() != null)
			jfrom.setDescription(from.description().string().getText());
		if (from.reference() != null) {
			convertReference(from.reference(), jfrom);
		}
	}

	private void convertWhere(IMQParser.WhereContext where, Where jWhere) throws DataFormatException {
		if (where.description()!=null)
			jWhere.setDescription(where.description().string().getText());
		if (where.reference()!=null){
			convertReference(where.reference(),jWhere);
		}
	}

	private void convertWith(IMQParser.WithContext with,With jWith) throws DataFormatException {
		if (with.where()!=null)
			convertWhere(with.where(),jWith);
		if (with.whereWhere()!=null)
			convertWhereWhere(with.whereWhere(),jWith);
		if (with.whereBoolean()!=null)
			convertWhereBoolean(with.whereBoolean(),jWith);
		if (with.whereValue()!=null)
			convertWhereValue(with.whereValue(),jWith);
		if (with.sortable()!=null)
			convertSortable(with.sortable(),jWith);
	}

	private void convertSortable(IMQParser.SortableContext sortable, With jWith) {
		if (sortable.earliest()!=null){
			jWith.setEarliest(sortable.earliest().PN_PROPERTY().getText());
		}
		else if (sortable.latest()!=null)
			jWith.setLatest(sortable.latest().PN_PROPERTY().getText());
		else if (sortable.maximum()!=null)
			jWith.setMaximum(sortable.maximum().PN_PROPERTY().getText());
		else if (sortable.minimum()!=null)
			jWith.setMinimum(sortable.minimum().PN_PROPERTY().getText());
		if (sortable.count()!=null)
			jWith.setCount(Integer.parseInt(sortable.count().DIGIT().getText()));
	}

	private void convertReference(IMQParser.ReferenceContext al, TTAlias jfrom) throws DataFormatException {
		if (al.iriRef()!=null){
			if (al.iriRef().IRI_REF()!=null) {

				if (al.sourceType().TYPE() != null)
					jfrom.setType(al.iriRef().IRI_REF().getText());
				else if (al.sourceType().SET()!=null)
					jfrom.setSet(al.iriRef().IRI_REF().getText());
				else
					jfrom.setIri(al.iriRef().IRI_REF().getText());
			}
			else if (al.iriRef().PN_PREFIXED()!=null)
				jfrom.setIri(resolvePrefixed(al.iriRef().PN_PREFIXED().getText()));
		}
		else if (al.iriRef().PN_PROPERTY()!=null){
			jfrom.setId(al.iriRef().PN_PROPERTY().getText());
		}
		if (al.alias()!=null){
			jfrom.setAlias(al.alias().string().getText());
		}
		if (al.variable()!=null){
			jfrom.setVariable(al.variable().getText());
		}
		if (al.iriRef().name()!=null){
			jfrom.setName(al.iriRef().name().getText().replaceAll("\\|",""));
		}
		if (al.subsumption()!=null) {
			IMQParser.SubsumptionContext sub = al.subsumption();
			if (sub.descendantorselfof() != null) {
				jfrom.setDescendantsOrSelfOf(true);
			}
			if (sub.ancestorOf() != null) {
				jfrom.setAncestorsOf(true);
			}
			if (sub.descendantof() != null)
				jfrom.setDescendantsOf(true);
		}
	}

	

	private void convertArguments(IMQParser.ArgumentsContext args, QueryRequest jqr){
		if (args.argument()!=null){
			addArgument(args.argument(),jqr);
		}
		for (IMQParser.ArgumentContext arg:args.argumentList().argument()){
			addArgument(arg,jqr);
		}
	}

	private void addArgument(IMQParser.ArgumentContext arg, QueryRequest jqr){
		Argument jarg= new Argument();
		jqr.addArgument(jarg);
		jarg.setParameter(arg.parameter().getText());
		if (arg.value()!=null)
			jarg.setValueData(arg.value().getText());
		else if (arg.valueDataList()!=null) {
			for (IMQParser.ValueContext item : arg.valueDataList().value()) {
				jarg.addToValueDataList(item.getText());
			}
		}
		else if (arg.valueIriList()!=null){

			for (IMQParser.IriRefContext iri:arg.valueIriList().iriRef()){
				jarg.addToValueIriList(TTIriRef.iri(iri.IRI_REF().getText()));
			}
		}
	}


}
