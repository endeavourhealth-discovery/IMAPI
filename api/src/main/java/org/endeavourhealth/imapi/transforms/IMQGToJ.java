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
		if (qr.prefixes()!=null){
			convertPrefixes(qr.prefixes(),jqr);
		}
		if (qr.arguments()!=null){
			convertArguments(qr.arguments(),jqr);
		}
		if (qr.query()!=null){
			convertQuery(qr.query(),jqr);
		}
		return jqr;
	}

	private void convertPrefixes(IMQParser.PrefixesContext prefixList,QueryRequest jqr) {
		TTContext context= new TTContext();
		for (IMQParser.PrefixedContext prefix:prefixList.prefixed()){
			String iri= prefix.IRI_REF().getText();
			prefixes.put(prefix.PN_PROPERTY().getText(),iri);
			context.add(iri,prefix.PN_PROPERTY().getText());
		}
		jqr.setContext(context);
	}


	private String resolvePrefixed(String prefixed) {
		String []parts= prefixed.split(":");
		String prefix;
		if (!parts[0].equals("")) {
			prefix = prefixes.get(parts[0]);
			if (prefix!=null)
				return (prefix + prefixed.substring(prefixed.indexOf(":") + 1));
			else
				return prefixed;
		}
		else
			return prefixed;
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
		for (IMQParser.SelectContext select:selectClause.select()){
			Select jSelect= new Select();
			jq.addSelect(jSelect);
			convertSelect(select,jSelect);
		}
	}

	private void convertSelect(IMQParser.SelectContext select, Select jSelect) throws DataFormatException {
		if (select.iriRef()!=null)
			convertIriRef(select.iriRef(),jSelect);
		else if (select.PN_PROPERTY()!=null)
			jSelect.setAlias(select.PN_PROPERTY().getText());
		if (select.whereClause()!=null)
			convertWhereClause(select.whereClause(),jSelect);
		if (select.select()!=null){
			for (IMQParser.SelectContext subSelect:select.select()){
				Select jSubSelect= new Select();
				jSelect.addSelect(jSubSelect);
				convertSelect(subSelect,jSubSelect);
			}
		}
	}


	private void convertMainFromClause(IMQParser.FromClauseContext fromClause,Query jquery) throws DataFormatException {
		From jfrom = new From();
		jquery.setFrom(jfrom);
		if (fromClause.booleanFrom()!=null){
			convertBooleanFrom(fromClause.booleanFrom(),jfrom);
		}
		else
			convertFrom(fromClause.from(), jfrom);
	}


	private void convertFrom(IMQParser.FromContext from,From jfrom) throws DataFormatException {
		if (from.graph() != null) {
			jfrom.setGraph(TTAlias.iri(from.graph().IRI_REF().getText()));
		}
		if (from.description() != null)
			jfrom.setDescription(getString(from.description().string().getText()));
		if (from.reference() != null) {
			convertReference(from.reference(), jfrom);
		}
		if (from.booleanFrom()!=null)
			convertBooleanFrom(from.booleanFrom(),jfrom);

		if (from.whereClause()!=null)
			convertWhereClause(from.whereClause(),jfrom);

	}


	private void convertBooleanFrom(IMQParser.BooleanFromContext fromBoolean, From jfrom) throws DataFormatException {
		if (fromBoolean.orFrom()!=null) {
			convertOrFrom(fromBoolean.orFrom(),jfrom);
		}
		else if (fromBoolean.andFrom()!=null) {
			convertAndFrom(fromBoolean.andFrom(), jfrom);
		}
		else if (fromBoolean.notFrom()!=null){
			convertNotFrom(fromBoolean.notFrom(),jfrom);
		}
	}

	private void convertAndFrom(IMQParser.AndFromContext andFrom, From jfrom) throws DataFormatException {
		jfrom.setBool(Bool.and);
		if (andFrom.from().size()>0) {
			for (IMQParser.FromContext from : andFrom.from()) {
				addFrom(from,jfrom);
			}
		}
	}



	private void addFrom(IMQParser.FromContext from, From jFrom) throws DataFormatException {
		From subFrom = new From();
		jFrom.addFrom(subFrom);
		convertFrom(from, subFrom);
	}

	private void convertNotFrom(IMQParser.NotFromContext notFrom, From jfrom) throws DataFormatException {
		jfrom.setBool(Bool.not);
		if (notFrom.from()!=null) {
				addFrom(notFrom.from(),jfrom);
		}

	}

	private void convertOrFrom(IMQParser.OrFromContext orFrom, From jfrom) throws DataFormatException {
		jfrom.setBool(Bool.or);
		if (orFrom.from().size()>0) {
			for (IMQParser.FromContext from : orFrom.from()) {
				addFrom(from,jfrom);
			}
		}
	}


	private void convertWhereClause(IMQParser.WhereClauseContext whereClause, From jFrom) throws DataFormatException {
		Where jWhere = new Where();
		jFrom.setWhere(jWhere);
		if (whereClause.booleanWhere()!=null){
			convertBooleanWhere(whereClause.booleanWhere(),jWhere);
		}
		else
			convertWhere(whereClause.where(),jWhere);
	}
	private void convertWhereClause(IMQParser.WhereClauseContext whereClause, Select jSelect) throws DataFormatException {
		Where jWhere = new Where();
		jSelect.setWhere(jWhere);
		if (whereClause.booleanWhere()!=null){
			convertBooleanWhere(whereClause.booleanWhere(),jWhere);
		}
		else
			convertWhere(whereClause.where(), jWhere);
	}

	private void convertWhereClause(IMQParser.WhereClauseContext whereClause, Where jWhere) throws DataFormatException {
		Where jSubWhere = new Where();
		jWhere.addWhere(jSubWhere);
		if (whereClause.booleanWhere()!=null){
			convertBooleanWhere(whereClause.booleanWhere(),jWhere);
		}
		else
			convertWhere(whereClause.where(), jWhere);
	}




	private void convertBooleanWhere(IMQParser.BooleanWhereContext whereBoolean, Where jWhere) throws DataFormatException {
		if (whereBoolean.orWhere()!=null) {
			convertOrWhere(whereBoolean.orWhere(),jWhere);
		}
		else if (whereBoolean.andWhere()!=null) {
			convertAndWhere(whereBoolean.andWhere(), jWhere);
		}
		else if (whereBoolean.notWhere()!=null){
			convertNotWhere(whereBoolean.notWhere(),jWhere);
		}
	}

	private void convertOrWhere(IMQParser.OrWhereContext orWhere, Where jWhere) throws DataFormatException {
		jWhere.setBool(Bool.or);
		if (orWhere.where().size()>0) {
			for (IMQParser.WhereContext where : orWhere.where()) {
				addWhere(where,jWhere);
			}
		}
	}




	private void convertAndWhere(IMQParser.AndWhereContext andWhere, Where jWhere) throws DataFormatException {
		jWhere.setBool(Bool.and);
		if (andWhere.where().size()>0) {
			for (IMQParser.WhereContext where : andWhere.where()) {
				addWhere(where,jWhere);
			}
		}

	}

	private void convertNotWhere(IMQParser.NotWhereContext notWhere, Where jWhere) throws DataFormatException {
		jWhere.setBool(Bool.not);
		if (notWhere.where()!=null) {
				addWhere(notWhere.where(),jWhere);
		}

	}

	private void addWhere(IMQParser.WhereContext where, Where jWhere) throws DataFormatException {
		Where jSubWhere= new Where();
		jWhere.addWhere(jSubWhere);
		convertWhere(where,jSubWhere);
	}



	private void convertWhereValueTest(IMQParser.WhereValueTestContext whereValue, Where jWhere) {
		if (whereValue.inClause()!=null){
			List<TTAlias> setList = new ArrayList<>();
			jWhere.setIn(setList);
			convertSetList(whereValue.inClause().reference(), setList);
			}
			else if (whereValue.notInClause()!=null){
				List<TTAlias> setList= new ArrayList<>();
				jWhere.setNotIn(setList);
				convertSetList(whereValue.notInClause().reference(),setList);
			}

		if (whereValue.whereMeasure()!=null) {
			convertAssignable(whereValue.whereMeasure(),jWhere);
		}
		if (whereValue.range()!=null)
			convertRange(whereValue.range(),jWhere);
		if (whereValue.valueLabel()!=null)
			jWhere.setValueLabel(getString(whereValue.valueLabel().string().getText()));
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
			jassign.setOperator(Operator.get(whereMeasure.operator().getText()).get());
		if (whereMeasure.number()!=null)
			jassign.setValue(whereMeasure.number().getText());
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




	private void convertSetList(List<IMQParser.ReferenceContext> setList, List<TTAlias> fromList) {
			for (IMQParser.ReferenceContext from : setList) {
				TTAlias jFrom = new TTAlias();
				fromList.add(jFrom);
				convertReference(from, jFrom);
			}
	}
	private void convertWhere(IMQParser.WhereContext where, Where jWhere) throws DataFormatException {
		if (where.with()!=null){
			With jWith= new With();
			jWhere.setWith(jWith);
			convertWith(where.with(),jWith);
		}
		if (where.description()!=null)
			jWhere.setDescription(getString(where.description().string().getText()));
		if (where.reference()!=null){
			convertReference(where.reference(),jWhere);
		}
		if (where.whereValueTest()!=null){
			convertWhereValueTest(where.whereValueTest(),jWhere);
		}
		if (where.whereClause()!=null){
			Where subWhere= new Where();
			jWhere.addWhere(subWhere);
			convertWhereClause(where.whereClause(),subWhere);
		}
		if (where.booleanWhere()!=null){
			convertBooleanWhere(where.booleanWhere(),jWhere);
		}


		if (where.whereClause()!=null){
			convertWhereClause(where.whereClause(),jWhere);
		}

	}

	private void convertWith(IMQParser.WithContext with,With jWith) throws DataFormatException {
		convertWhereClause(with.whereClause(),jWith);
		if (with.sortable()!=null)
			convertSortable(with.sortable(),jWith);
	}

	private void convertSortable(IMQParser.SortableContext sortable, With jWith) {
		String column=null;
		if (sortable.PN_PROPERTY()!=null)
			column= sortable.PN_PROPERTY().getText();
		else
			column= sortable.iriRef().getText();
		jWith.setOrderBy(new TTAlias().setIri(column));
		if (sortable.direction().DESCENDING()!=null)
			jWith.setDirection(Order.descending);
		else
			jWith.setDirection(Order.ascending);
		if (sortable.count()!=null)
			jWith.setCount(Integer.parseInt(sortable.count().INTEGER().getText()));
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

	private void convertReference(IMQParser.ReferenceContext al, TTAlias jfrom) {
		if (al.inverseOf()!=null)
			jfrom.setInverse(true);
		String iri=null;
		if (al.IRI_REF()!=null)
			iri = al.IRI_REF().getText();
		else if (al.PN_PREFIXED()!=null)
			iri = resolvePrefixed(al.PN_PREFIXED().getText());
		if (al.sourceType()!=null) {
			IMQParser.SourceTypeContext sourceType = al.sourceType();
			if (sourceType.type() != null)
				jfrom.setType(iri);
			else if (sourceType.set() != null)
				jfrom.setSet(iri);
			else {
				jfrom.setIri(iri);
				if (sourceType.ancestorAndDescendantOf() != null) {
					jfrom.setDescendantsOrSelfOf(true);
					jfrom.setAncestorsOf(true);
				}
				else if (sourceType.descendantorselfof() != null) {
					jfrom.setDescendantsOrSelfOf(true);
				}
				else if (sourceType.ancestorOf() != null) {
					jfrom.setAncestorsOf(true);
				}
				if (sourceType.descendantof() != null)
					jfrom.setDescendantsOf(true);
			}
		}
		else
			jfrom.setIri(iri);
		if (al.alias()!=null){
			jfrom.setAlias(al.alias().PN_PROPERTY().getText());
		}
		if (al.variable()!=null){
			jfrom.setVariable(al.variable().getText());
		}
		if (al.name()!=null){
			jfrom.setName(al.name().getText().replaceAll("\\|",""));
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
