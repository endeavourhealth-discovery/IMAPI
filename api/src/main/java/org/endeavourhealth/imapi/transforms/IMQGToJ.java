package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
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
			convertPrefixes(qr.prefixDecl(),jqr);
		}
		if (qr.arguments()!=null){
			convertArguments(qr.arguments(),jqr);
		}
		if (qr.query()!=null){
			convertQuery(qr.query(),jqr);
		}
		return jqr;
	}

	private void convertPrefixes(List<IMQParser.PrefixDeclContext> prefixDecl,QueryRequest jqr) {
		TTContext context= new TTContext();
		for (IMQParser.PrefixDeclContext prefix:prefixDecl){
			String iri= prefix.IRI_REF().getText();
			prefixes.put(prefix.PN_PROPERTY().getText(),iri);
			context.add(iri,prefix.PN_PROPERTY().getText());
		}
		jqr.setContext(context);
	}




	private String resolvePrefixed(String prefixed) throws DataFormatException {
		String []parts= prefixed.split(":");
		String prefix= prefixes.get(parts[0]);
		return (prefix+ prefixed.substring(prefixed.indexOf(":")+1));
	}

	private void convertQuery(IMQParser.QueryContext qry, QueryRequest jqr) throws DataFormatException {
		Query jq= new Query();
		jqr.setQuery(jq);
		if (qry.iriRef()!=null) {
			convertIriRef(qry.iriRef(), jq);
		}
		if (qry.properName()!=null)
			jq.setName(getString(qry.properName().string().getText()));
		if (qry.description()!=null)
			jq.setDescription(getString(qry.description().string().getText()));
		if (qry.activeOnly()!=null)
			jq.setActiveOnly(true);
		if (qry.fromClause()!=null){
			convertMainFromClause(qry.fromClause(),jq);
		}
		if (qry.selectClause()!=null)
			convertSelectClause(qry.selectClause(),jq);
	}

	private String getString(String g4String){
		return g4String.substring(1,g4String.length()-1);
	}

	private void convertSelectClause(IMQParser.SelectClauseContext selectClause, Query jq) throws DataFormatException {
		for (IMQParser.SelectContext select:selectClause.selectList().select()){
			Select jSelect= new Select();
			jq.addSelect(jSelect);
			convertSelect(select,jSelect);
		}
	}

	private void convertSelect(IMQParser.SelectContext select, Select jSelect) throws DataFormatException {
		if (select.iriRef()!=null)
			convertIriRef(select.iriRef(),jSelect);
		else if (select.PN_PROPERTY()!=null)
			jSelect.setId(select.PN_PROPERTY().getText());
		if (select.whereClause()!=null)
			convertWhereClause(select.whereClause(),jSelect);
		if (select.selectClause()!=null){
			for (IMQParser.SelectContext subSelect:select.selectClause().selectList().select()){
				Select jSubSelect= new Select();
				jSelect.addSelect(jSubSelect);
				convertSelect(subSelect,jSubSelect);
			}
		}
	}


	private void convertMainFromClause(IMQParser.FromClauseContext fromClause,Query jquery) throws DataFormatException {
		From jfrom = new From();
		jquery.setFrom(jfrom);
		if (fromClause.from() != null) {
			convertFrom(fromClause.from(), jfrom);
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
		if (orFrom.bracketFrom().size()>0) {
			for (IMQParser.BracketFromContext bracketFrom : orFrom.bracketFrom()) {
				addBracketFrom(bracketFrom,jfrom);
			}
		}
	}


	private void convertWhereClause(IMQParser.WhereClauseContext whereClause, From jFrom) throws DataFormatException {
		Where jWhere = new Where();
		jFrom.setWhere(jWhere);
		convertSubWhere(whereClause.subWhere(), jWhere);
	}
	private void convertWhereClause(IMQParser.WhereClauseContext whereClause, Select jSelect) throws DataFormatException {
		Where jWhere = new Where();
		jSelect.setWhere(jWhere);
		convertSubWhere(whereClause.subWhere(), jWhere);
	}

	private void convertWhereClause(IMQParser.WhereClauseContext whereClause, Where jWhere) throws DataFormatException {
		Where jSubWhere = new Where();
		jWhere.addWhere(jSubWhere);
		convertSubWhere(whereClause.subWhere(), jSubWhere);
	}

	private void convertSubWhere(IMQParser.SubWhereContext subWhere, Where jWhere) throws DataFormatException {
		if (subWhere.where()!=null){
			convertWhere(subWhere.where(),jWhere);
		}
		if (subWhere.whereBoolean()!=null){
			convertWhereBoolean(subWhere.whereBoolean(),jWhere);
		}
		if (subWhere.bracketWhere()!=null)
			convertBracketWhere(subWhere.bracketWhere(),jWhere);
	}

	private void convertBracketWhere(IMQParser.BracketWhereContext bracketWhere, Where jWhere) throws DataFormatException {
		if (bracketWhere.where()!=null)
			convertWhere(bracketWhere.where(),jWhere);
		if (bracketWhere.whereBoolean()!=null)
			convertWhereBoolean(bracketWhere.whereBoolean(),jWhere);
	}


	private void convertWhereBoolean(IMQParser.WhereBooleanContext whereBoolean, Where jWhere) throws DataFormatException {
		if (whereBoolean.orWhere()!=null) {
			convertOrWhere(whereBoolean.orWhere(),jWhere);
		}
		else if (whereBoolean.andWhere()!=null) {
			convertAndWhere(whereBoolean.andWhere(), jWhere);
		}
		else
			convertNotWhere(whereBoolean.notWhere(),jWhere);
	}

	private void convertOrWhere(IMQParser.OrWhereContext orWhere, Where jWhere) throws DataFormatException {
		jWhere.setBool(Bool.or);
		if (orWhere.where().size()>0) {
			for (IMQParser.WhereContext where : orWhere.where()) {
				addWhere(where,jWhere);
			}
		}
		if (orWhere.bracketWhere().size()>0) {
			for (IMQParser.BracketWhereContext bracketWhere : orWhere.bracketWhere()) {
				addBracketWhere(bracketWhere,jWhere);
			}
		}
	}

	private void addBracketWhere(IMQParser.BracketWhereContext bracketWhere, Where jWhere) throws DataFormatException {
		Where jsubWhere= new Where();
		jWhere.addWhere(jsubWhere);
		convertBracketWhere(bracketWhere,jsubWhere);
	}


	private void convertAndWhere(IMQParser.AndWhereContext andWhere, Where jWhere) throws DataFormatException {
		jWhere.setBool(Bool.and);
		if (andWhere.where().size()>0) {
			for (IMQParser.WhereContext where : andWhere.where()) {
				addWhere(where,jWhere);
			}
		}

		if (andWhere.bracketWhere().size()>0) {
			for (IMQParser.BracketWhereContext bracketWhere : andWhere.bracketWhere()) {
				addBracketWhere(bracketWhere,jWhere);
			}
		}
	}

	private void convertNotWhere(IMQParser.NotWhereContext notWhere, Where jWhere) throws DataFormatException {
		jWhere.setBool(Bool.not);
		if (notWhere.where().size()>0) {
			for (IMQParser.WhereContext where : notWhere.where()) {
				addWhere(where,jWhere);
			}
		}
		if (notWhere.bracketWhere().size()>0) {
			for (IMQParser.BracketWhereContext bracketWhere : notWhere.bracketWhere()) {
				addBracketWhere(bracketWhere,jWhere);
			}
		}
	}

	private void addWhere(IMQParser.WhereContext where, Where jWhere) throws DataFormatException {
		Where jSubWhere= new Where();
		jWhere.addWhere(jSubWhere);
		convertWhere(where,jSubWhere);
	}



	private void convertWhereValueTest(IMQParser.WhereValueTestContext whereValue, Where jWhere) throws DataFormatException {
		if (whereValue.inClause()!=null){
			List<From> inList= new ArrayList<>();
			jWhere.setIn(inList);
			convertConceptSet(whereValue.inClause().conceptSet(),inList);
		}
		if (whereValue.notInClause()!=null){
			List<From> notList= new ArrayList<>();
			jWhere.setNotIn(notList);
			convertConceptSet(whereValue.notInClause().conceptSet(),notList);
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



	private void convertConceptSet(IMQParser.ConceptSetContext conceptSet, List<From> fromList) throws DataFormatException {
		if (conceptSet.from()!=null) {
				addToFrom(conceptSet.from(), fromList);
			}
		if (conceptSet.fromBoolean()!=null){
			for (IMQParser.FromContext from:conceptSet.fromBoolean().orFrom().from()) {
				addToFrom(from, fromList);
			}
		}
		if (conceptSet.bracketFrom()!=null){
				addToBracketFrom(conceptSet.bracketFrom(), fromList);
			}

	}



	private void addToBracketFrom(IMQParser.BracketFromContext bracketFrom, List<From> fromList) throws DataFormatException {
		From inFrom= new From();
		fromList.add(inFrom);
		convertBracketFrom(bracketFrom,inFrom);
	}




	private void convertBracketFrom(IMQParser.BracketFromContext bracketFrom, From jFrom) throws DataFormatException {
		if (bracketFrom.from()!=null)
			convertFrom(bracketFrom.from(),jFrom);
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
		if (from.whereClause()!=null)
			convertWhereClause(from.whereClause(),jfrom);
	}

	private void convertWhere(IMQParser.WhereContext where, Where jWhere) throws DataFormatException {
		if (where.with()!=null){
			With jWith= new With();
			jWhere.setWith(jWith);
			convertWith(where.with(),jWith);
		}
		if (where.description()!=null)
			jWhere.setDescription(where.description().string().getText());
		if (where.reference()!=null){
			convertReference(where.reference(),jWhere);
		}
		if (where.whereValueTest()!=null){
			convertWhereValueTest(where.whereValueTest(),jWhere);
		}
		if (where.whereClause()!=null){
			convertWhereClause(where.whereClause(),jWhere);
		}
	}

	private void convertWith(IMQParser.WithContext with,With jWith) throws DataFormatException {
		if (with.where()!=null)
			convertWhere(with.where(),jWith);
		if (with.whereBoolean()!=null)
			convertWhereBoolean(with.whereBoolean(),jWith);
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

	private void convertIriRef(IMQParser.IriRefContext iriRef,TTIriRef jIriRef) throws DataFormatException {
			if (iriRef.IRI_REF()!=null) {
				jIriRef.setIri(iriRef.IRI_REF().getText());
			}
			else if (iriRef.PN_PREFIXED()!=null)
				jIriRef.setIri(resolvePrefixed(iriRef.PN_PREFIXED().getText()));
			else
				throw new DataFormatException("Error in iri translation");
			if (iriRef.name()!=null){
				jIriRef.setName(iriRef.name().getText());
			}

	}

	private void convertReference(IMQParser.ReferenceContext al, TTAlias jfrom) throws DataFormatException {
		if (al.inverseOf()!=null)
			jfrom.setInverse(true);
		String iri=null;
		if (al.IRI_REF()!=null)
			iri = al.IRI_REF().getText();
		else if (al.PN_PREFIXED()!=null)
			iri = resolvePrefixed(al.PN_PREFIXED().getText());
		 if (al.sourceType().TYPE() != null)
			 jfrom.setType(iri);
		 else if (al.sourceType().SET()!=null)
					jfrom.setSet(iri);
		 else
			 jfrom.setIri(iri);
		if (al.alias()!=null){
			jfrom.setAlias(al.alias().PN_PROPERTY().getText());
		}
		if (al.variable()!=null){
			jfrom.setVariable(al.variable().getText());
		}
		if (al.name()!=null){
			jfrom.setName(getString(al.name().getText()).replaceAll("\\|",""));
		}
		if (al.subsumption()!=null) {
			IMQParser.SubsumptionContext sub = al.subsumption();
			if (sub.ancestorAndDescendantOf()!=null){
				jfrom.setDescendantsOrSelfOf(true);
				jfrom.setAncestorsOf(true);
			}
			else if (sub.descendantorselfof() != null) {
				jfrom.setDescendantsOrSelfOf(true);
			}
			else if (sub.ancestorOf() != null) {
				jfrom.setAncestorsOf(true);
			}
			if (sub.descendantof() != null)
				jfrom.setDescendantsOf(true);
		}
	}



	private void convertArguments(IMQParser.ArgumentsContext args, QueryRequest jqr) throws DataFormatException {
		if (args.argument().size()>0){
		for (IMQParser.ArgumentContext arg:args.argument())
			addArgument(arg,jqr);
		}
	}

	private void addArgument(IMQParser.ArgumentContext arg, QueryRequest jqr) throws DataFormatException {
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
			if (arg.valueIriList().iriRef().size()==1){
				TTIriRef valueIri= new TTIriRef();
				jarg.setValueIri(valueIri);
				convertIriRef(arg.valueIriList().iriRef(0),valueIri);
			}
			else {
				for (IMQParser.IriRefContext iri : arg.valueIriList().iriRef()) {
					TTIriRef valueIri = new TTIriRef();
					jarg.addToValueIriList(valueIri);
					convertIriRef(iri, valueIri);
				}
			}
		}
	}


}
