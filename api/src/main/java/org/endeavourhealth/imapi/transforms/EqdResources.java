package org.endeavourhealth.imapi.transforms;

import org.apache.commons.collections4.CollectionUtils;
import org.endeavourhealth.imapi.logic.exporters.ImportMaps;
import org.endeavourhealth.imapi.model.iml.ConceptSet;
import org.endeavourhealth.imapi.model.iml.ModelDocument;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

public class EqdResources {
	Map<String, String> reportNames = new HashMap<>();
	private final ImportMaps importMaps = new ImportMaps();
	private final Map<Object, Object> vocabMap = new HashMap<>();
	private Properties dataMap;
	private Properties labels;
	private String activeReport;
	private String activeReportName;
	private ModelDocument document;
	private final Map<String,Set<TTIriRef>> valueMap= new HashMap<>();
	private int counter=0;
	public Map<String, ConceptSet> valueSets = new HashMap<>();


	public Map<String, ConceptSet> getValueSets() {
		return valueSets;
	}

	public EqdResources setValueSets(Map<String, ConceptSet> valueSets) {
		this.valueSets = valueSets;
		return this;
	}

	public String getActiveReportName() {
		return activeReportName;
	}

	public EqdResources setActiveReportName(String activeReportName) {
		this.activeReportName = activeReportName;
		return this;
	}

	public ModelDocument getDocument() {
		return document;
	}

	public EqdResources setDocument(ModelDocument document) {
		this.document = document;
		return this;
	}

	public void setActiveReport(String activeReport) {
		this.activeReport = activeReport;
	}

	public EqdResources() {
		setVocabMaps();
	}

	public void setDataMap(Properties dataMap) {
		this.dataMap = dataMap;
	}

	public void setLabels(Properties labels) {
		this.labels = labels;
	}

	private void setVocabMaps() {
		vocabMap.put(VocRangeFromOperator.GTEQ, Operator.gte);
		vocabMap.put(VocRangeFromOperator.GT, Operator.gt);
		vocabMap.put(VocRangeToOperator.LT, Operator.lt);
		vocabMap.put(VocRangeToOperator.LTEQ, Operator.lte);
		vocabMap.put(VocOrderDirection.DESC, "DESC");
		vocabMap.put(VocOrderDirection.ASC, "ASC");
	}

	public TTIriRef getIri(String token) throws IOException {
		if (token.equals("label"))
			return RDFS.LABEL.setName("label");
		else {
			if (!token.contains(":")) {
				TTIriRef iri = TTIriRef.iri(IM.NAMESPACE + token);
				iri.setName(importMaps.getCoreName(IM.NAMESPACE + token));
				return iri;
			} else {
				TTIriRef iri = TTIriRef.iri(token);
				iri.setName(importMaps.getCoreName(token));
				return iri;
			}
		}
	}


	public void convertCriteria(EQDOCCriteria eqCriteria,
															 Where match) throws DataFormatException, IOException {

		if ((eqCriteria.getPopulationCriterion() != null)) {
			EQDOCSearchIdentifier srch = eqCriteria.getPopulationCriterion();
			match.in(f->f
				.setIri(IM.IS_SUBSET_OF.getIri())
				.setSet("urn:uuid:" + srch.getReportGuid())
				.setName(reportNames.get(srch.getReportGuid())));
		} else {
			convertCriterion(eqCriteria.getCriterion(),match);
		}
	}



	private void convertCriterion(EQDOCCriterion eqCriterion, Where match) throws DataFormatException, IOException {
		if (eqCriterion.isNegation()){
			Where notWhere= new Where();
			match.setBool(Bool.not);
			match.addWhere(notWhere);
			match= notWhere;
		}



		if (eqCriterion.getLinkedCriterion() != null) {
			convertLinkedCriterion(eqCriterion, match);
		}

		else {
			convertStandardCriterion(eqCriterion, match);
		}

	}

	private void convertStandardCriterion(EQDOCCriterion eqCriterion, Where match) throws DataFormatException, IOException {
		String path= getPath(eqCriterion.getTable());
		if (!path.equals("")) {
			match.setIri(IM.NAMESPACE+path);
		}
		if (eqCriterion.getFilterAttribute().getRestriction() != null) {
			convertRestrictionCriterion(eqCriterion, match);

		}
		else {
			convertColumns(eqCriterion, match);
			if (eqCriterion.getId() != null)
				if (labels.get(eqCriterion.getId()) != null) {
					match.setDescription(labels.get(eqCriterion.getId()).toString());
				}
		}
	}

	private void convertColumns(EQDOCCriterion eqCriterion, Where match) throws DataFormatException, IOException {
		EQDOCFilterAttribute filterAttribute = eqCriterion.getFilterAttribute();
		List<EQDOCColumnValue> cvs= filterAttribute.getColumnValue();
		if (cvs.size()==1){
			Where columnWhere= match;
			if (match.getIri()!=null){
				columnWhere= new Where();
				match.addWhere(columnWhere);
			}
			EQDOCColumnValue cv= cvs.get(0);
			setColumnWhere(cv,eqCriterion.getTable(),columnWhere);
		}
		else {
			match.setBool(Bool.and);
			for (EQDOCColumnValue cv : filterAttribute.getColumnValue()) {
				Where columnWhere= new Where();
				match.addWhere(columnWhere);
				setColumnWhere(cv,eqCriterion.getTable(),columnWhere);
			}
		}

	}

	private void setColumnWhere(EQDOCColumnValue cv, String eqTable,Where match) throws DataFormatException, IOException {
		String eqColumn= String.join("/",cv.getColumn());
		String subPath= getPath(eqTable+"/"+ eqColumn);
		String[] subPaths= subPath.split(" ");
		if (subPath.contains(" ")) {
			for (int i = 0; i < subPaths.length - 1; i++) {
				match.setIri(IM.NAMESPACE+subPaths[i]);
				Where subWhere= new Where();
				match.addWhere(subWhere);
				match = subWhere;
			}
		}
		match.setIri(IM.NAMESPACE+subPaths[subPaths.length-1]);
		setWhere(cv,match);
		match.setDescription(getLabel(cv));
	}


	private void setWhere(EQDOCColumnValue cv, Where pv) throws DataFormatException, IOException {

		VocColumnValueInNotIn in = cv.getInNotIn();
		boolean notIn = (in == VocColumnValueInNotIn.NOTIN);
		if (!cv.getValueSet().isEmpty()) {
			setWhereValueSetSetters(cv, pv, notIn);
		} else if (!CollectionUtils.isEmpty(cv.getLibraryItem())) {
			for (String vset : cv.getLibraryItem()) {
				String vsetName = "Unknown code set";
				if (labels.get(vset) != null)
					pv.setValueLabel((String) labels.get(vset));
				From iri = new From().setSet("urn:uuid:" + vset).setName(vsetName);
				if (!notIn)
					pv.addIn(iri);
				else {
					pv.addNotIn(iri);
				}
				storeLibraryItem(iri.getSet(),vsetName);
			}
		} else if (cv.getRangeValue() != null) {
			setRangeValue(cv.getRangeValue(), pv);
		}
	}

	private void setWhereValueSetSetters(EQDOCColumnValue cv, Where pv, boolean notIn) throws DataFormatException, IOException {
		for (EQDOCValueSet vs : cv.getValueSet()) {
			if (vs.getId()!=null)
				if (labels.get(vs.getId())!=null) {
					pv.setValueLabel(labels.get(vs.getId()).toString());
				}
			if (vs.getAllValues() != null) {
				pv.setNotIn(getExceptionSet(vs.getAllValues()));
			} else {
				if (!notIn) {
					pv.setIn(getInlineValues(vs,pv));
				}
				else {
					pv.setNotIn(getInlineValues(vs,pv));
				}
			}
		}
	}


	public String getPath(String eqdPath) throws DataFormatException {
		Object target = dataMap.get(eqdPath);
		if (target == null)
			throw new DataFormatException("unknown map : " + eqdPath);
		return (String) target;
	}


	private void convertRestrictionCriterion(EQDOCCriterion eqCriterion, Where match) throws DataFormatException, IOException {
		With with= new With();
		match.setWith(with);
		if (eqCriterion.getDescription() != null)
			with.setDescription(eqCriterion.getDescription());
		convertColumns(eqCriterion, with);
		setRestriction(eqCriterion, with);
		if (eqCriterion.getFilterAttribute().getRestriction().getTestAttribute()!=null)
			restrictionTest(eqCriterion, with);

	}


	private void restrictionTest(EQDOCCriterion eqCriterion, With with) throws IOException, DataFormatException {
		EQDOCTestAttribute testAtt= eqCriterion.getFilterAttribute().getRestriction().getTestAttribute();
		if (testAtt != null) {
			Where then= new Where();
			with.setThen(then);
			List<EQDOCColumnValue> cvs = testAtt.getColumnValue();
			if (cvs.size()==1){
				EQDOCColumnValue cv= cvs.get(0);
				setColumnWhere(cv,eqCriterion.getTable(),then);
			}
			else {
				then.setBool(Bool.and);
				for (EQDOCColumnValue cv : testAtt.getColumnValue()) {
					Where columnWhere= new Where();
					then.addWhere(columnWhere);
					setColumnWhere(cv,eqCriterion.getTable(),columnWhere);
				}
			}
		}
	}

	private void setRestriction(EQDOCCriterion eqCriterion, With with) throws DataFormatException {
		String eqTable = eqCriterion.getTable();
		String linkColumn = eqCriterion.getFilterAttribute().getRestriction()
			.getColumnOrder().getColumns().get(0).getColumn().get(0);
		String orderBy= getPath(eqTable+"/"+linkColumn);
		with.setOrderBy(new TTAlias().setIri(IM.NAMESPACE+ orderBy));
		with.setCount(1);
		EQDOCFilterRestriction restrict = eqCriterion.getFilterAttribute().getRestriction();
		if (restrict.getColumnOrder().getColumns().get(0).getDirection() == VocOrderDirection.ASC) {
			with.setDirection(Order.ascending);
		}
		else {
			with.setDirection(Order.ascending);
		}
	}

	private String checkForProperty(Where match,String property){
		boolean found=false;
		if (match.getIri()!=null){
			if (match.getIri().equals(property)){
				counter++;
				match.setAlias(property+"_"+counter);
				return property+"_"+counter;
			}
		}
		if (match.getWith()!=null){
			String matchedProperty= checkForProperty(match.getWith(),property);
			if (matchedProperty!=null)
				return matchedProperty;
		}
		if (match.getWhere()!=null) {
			for (Where where : match.getWhere()) {
				if (where.getIri().equals(property)) {
					counter++;
					where.setAlias(property + "_" + counter);
					return property + "_" + counter;
				}
			}
		}
		return null;

	}


	private void convertLinkedCriterion(EQDOCCriterion eqCriterion, Where topWhere) throws DataFormatException, IOException {
		Where targetWhere= new Where();
		topWhere.addWhere(targetWhere);
		convertStandardCriterion(eqCriterion,targetWhere);
		Where linkWhere= new Where();
		topWhere.addWhere(linkWhere);
		EQDOCLinkedCriterion eqLinked= eqCriterion.getLinkedCriterion();
		EQDOCCriterion eqLinkedCriterion= eqLinked.getCriterion();
		convertCriterion(eqLinkedCriterion,linkWhere);
		Where relationWhere = new Where();
		linkWhere.addWhere(relationWhere);
		EQDOCRelationship eqRel = eqLinked.getRelationship();
		String parentAlias= checkForProperty(targetWhere,IM.NAMESPACE+"effectiveDate");
		if (parentAlias==null){
				Where missing = new Where();
				topWhere.addWhere(missing);
				counter++;
				missing.setIri(IM.NAMESPACE+"effectiveDate")
					.setAlias("effectiveDate_"+counter);
				parentAlias= "effectiveDate_"+counter;
			}
		if (eqRel.getParentColumn().contains("DATE")){
			relationWhere
				.setIri(IM.NAMESPACE+"effectiveDate")
				.setOperator((Operator) vocabMap.get(eqRel.getRangeValue().getRangeFrom().getOperator()))
				.setValue(eqRel.getRangeValue().getRangeFrom().getValue().getValue())
			  .setUnit(eqRel.getRangeValue().getRangeFrom().getValue().getUnit().value())
				.setRelativeTo(parentAlias);
		}
		else
			throw new DataFormatException("Only date link fields supported at the moment");


	}

	private void setRangeValue(EQDOCRangeValue rv, Where pv) throws DataFormatException {

		EQDOCRangeFrom rFrom = rv.getRangeFrom();
		EQDOCRangeTo rTo = rv.getRangeTo();
		if (rFrom != null) {
			if (rTo == null) {
				setCompareFrom(pv, rFrom);
			} else {
				setRangeCompare(pv, rFrom, rTo);
			}
		}
		if (rTo != null && rFrom == null) {
			setCompareTo(pv, rTo);
		}

	}

	private void setCompareFrom(Assignable pv, EQDOCRangeFrom rFrom) throws DataFormatException {
		Operator comp;
		if (rFrom.getOperator() != null)
			comp = (Operator) vocabMap.get(rFrom.getOperator());
		else
			comp= Operator.eq;
		String value = rFrom.getValue().getValue();
		String units = null;
		if (rFrom.getValue().getUnit() != null)
			units = rFrom.getValue().getUnit().value();
		VocRelation relation = VocRelation.ABSOLUTE;
		if (rFrom.getValue().getRelation() != null && rFrom.getValue().getRelation() == VocRelation.RELATIVE) {
			relation = VocRelation.RELATIVE;
		}
		setCompare(pv, comp, value, units, relation, true);
	}

	private void setCompare(Assignable pv, Operator comp, String value, String units, VocRelation relation,
													boolean from) throws DataFormatException {
		if (relation == VocRelation.RELATIVE) {
			pv.setRelativeTo("$referenceDate");
			if (from) {
				comp = reverseComp(comp);
				value = String.valueOf(-Integer.parseInt(value));
			}
		}

		pv.setOperator(comp);
		pv.setValue(value);
		if (units!=null)
				pv.setUnit(units);
	}

	private Operator reverseComp(Operator comp) {
		switch (comp) {
			case gte:
				return Operator.lte;
			case gt:
				return Operator.lt;
			case lt:
				return Operator.gt;
			case lte:
				return Operator.gte;
		}
		return comp;
	}


	private void setCompareTo(Assignable pv, EQDOCRangeTo rTo) throws DataFormatException {
		Operator comp;
		if (rTo.getOperator() != null)
			comp = (Operator) vocabMap.get(rTo.getOperator());
		else
			comp = Operator.eq;
		String value = rTo.getValue().getValue();
		String units = null;
		if (rTo.getValue().getUnit() != null)
			units = rTo.getValue().getUnit().value();
		VocRelation relation = VocRelation.ABSOLUTE;
		if (rTo.getValue().getRelation() != null && rTo.getValue().getRelation() == VocRelation.RELATIVE) {
			relation = VocRelation.RELATIVE;
		}
		setCompare(pv, comp, value, units, relation, false);
	}


	private void setRangeCompare(Where pv, EQDOCRangeFrom rFrom, EQDOCRangeTo rTo) throws DataFormatException {
		String fromComp;
		Range range= new Range();
		pv.setRange(range);
		Value fromValue= new Value();
		range.setFrom(fromValue);
		setCompareFrom(fromValue,rFrom);
		Value toValue= new Value();
		range.setTo(toValue);
		setCompareTo(toValue,rTo);
		range.setFrom(fromValue);

		}


	private List<TTAlias> getExceptionSet(EQDOCException set) throws DataFormatException, IOException {
		List<TTAlias> valueSet = new ArrayList<>();
		VocCodeSystemEx scheme = set.getCodeSystem();
		for (EQDOCExceptionValue ev : set.getValues()) {
			Set<TTAlias> values = getValue(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
			if (values != null) {
				valueSet.addAll(new ArrayList<>(values));
			} else
				System.err.println("Missing exception sets\t" + ev.getValue() + "\t " + ev.getDisplayName());
		}

		return valueSet;
	}



	private List<TTAlias> getInlineValues(EQDOCValueSet vs,Where pv) throws DataFormatException, IOException {
		List<TTAlias> setContent = new ArrayList<>();
		VocCodeSystemEx scheme = vs.getCodeSystem();
		for (EQDOCValueSetValue ev : vs.getValues()) {
			Set<TTAlias> concepts = getValue(scheme, ev);
			if (concepts != null) {
				for (TTAlias iri : concepts) {
					TTAlias conRef = new TTAlias().setIri(iri.getIri()).setName(iri.getName())
						.setDescendantsOrSelfOf(true);
					setContent.add(conRef);
				}
			} else
				System.err.println("Missing \t" + ev.getValue() + "\t " + ev.getDisplayName());

		}
		return setContent;
	}



	private void storeLibraryItem(String iri,String name) {
		if (!valueSets.containsKey(iri)) {
			ConceptSet conceptSet = new ConceptSet();
				conceptSet.setIri(iri)
				.addType(IM.CONCEPT_SET)
				.setName(name);
			conceptSet.addUsedIn(TTIriRef.iri("urn:uuid:" + activeReport));
			valueSets.put(iri, conceptSet);
			valueSets.get(iri).addUsedIn(TTIriRef.iri("urn:uuid:" + activeReport).setName(activeReportName));

		}
	}

	private Set<TTAlias> getValue(VocCodeSystemEx scheme, EQDOCValueSetValue ev) throws DataFormatException, IOException {
		return getValue(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
	}

	private Set<TTAlias> getValue(VocCodeSystemEx scheme, String originalCode,
																	 String originalTerm, String legacyCode) throws DataFormatException, IOException {
		if (scheme == VocCodeSystemEx.EMISINTERNAL) {
			String key = "EMISINTERNAL/" + originalCode;
			Object mapValue = dataMap.get(key);
			if (mapValue != null) {
				return getValueIriResult(mapValue);
			} else
				throw new DataFormatException("unmapped emis internal code : " + key);
		} else if (scheme == VocCodeSystemEx.SNOMED_CONCEPT || scheme.value().contains("SCT")) {
			List<String> schemes = new ArrayList<>();
			schemes.add(SNOMED.NAMESPACE);
			schemes.add(IM.CODE_SCHEME_EMIS.getIri());
			Set<TTIriRef> snomed = valueMap.get(originalCode);
			if (snomed == null) {
				snomed = setValueSnomedChecks(originalCode, originalTerm, legacyCode, schemes);
				if (snomed != null)
					valueMap.put(originalCode, snomed);
			}
			if (snomed != null)
				return snomed.stream().map(e -> new From().setIri(e.getIri()).setName(e.getName())).collect(Collectors.toSet());
			else return null;
		} else
			throw new DataFormatException("code scheme not recognised : " + scheme.value());

	}

	private Set<TTAlias> getValueIriResult(Object mapValue) throws IOException {
		TTAlias iri = new TTAlias().setIri(mapValue.toString());
		String name = importMaps.getCoreName(iri.getIri());
		if (name != null)
			iri.setName(name);
		Set<TTAlias> result = new HashSet<>();
		result.add(iri);
		return result;
	}

	private Set<TTIriRef> setValueSnomedChecks(String originalCode, String originalTerm, String legacyCode, List<String> schemes) {
		Set<TTIriRef> snomed;
		snomed = getCoreFromCode(originalCode, schemes);
		if (snomed == null && legacyCode != null)
			snomed = getCoreFromCode(legacyCode, schemes);
		if (snomed == null && originalTerm != null)
			snomed = getCoreFromLegacyTerm(originalTerm);
		if (snomed == null)
			snomed = getCoreFromCodeId(originalCode);
		if (snomed == null)
			snomed = getLegacyFromTermCode(originalCode);
		return snomed;
	}


	private Set<TTIriRef> getCoreFromCodeId(String originalCode) {

		try {
			return importMaps.getCoreFromCodeId(originalCode, IM.CODE_SCHEME_EMIS.getIri());
		} catch (Exception e) {
			System.err.println("unable to retrieve iri from term code " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}


	private Set<TTIriRef> getLegacyFromTermCode(String originalCode) {
		try {
			return importMaps.getLegacyFromTermCode(originalCode, IM.CODE_SCHEME_EMIS.getIri());
		} catch (Exception e) {
			System.err.println("unable to retrieve iri from term code " + e.getMessage());
			return null;
		}
	}



	private Set<TTIriRef> getCoreFromLegacyTerm(String originalTerm) {
		try {
			if (originalTerm.contains("s disease of lymph nodes of head, face AND/OR neck"))
				System.out.println("!!");

			return importMaps.getCoreFromLegacyTerm(originalTerm, IM.CODE_SCHEME_EMIS.getIri());
		} catch (Exception e) {
			System.err.println("unable to retrieve iri from term " + e.getMessage());
			return null;
		}
	}

	private Set<TTIriRef> getCoreFromCode(String originalCode, List<String> schemes) {
		return importMaps.getCoreFromCode(originalCode, schemes);
	}

	private String getLabel(EQDOCColumnValue cv) {

		if (cv.getValueSet() != null) {
			return getLabelStringBuilder(cv);
		}
		else if (cv.getLibraryItem() != null) {
			StringBuilder setIds = new StringBuilder();
			int i = 0;
			for (String item : cv.getLibraryItem()) {
				i++;
				if (i > 1)
					setIds.append(",");
				setIds.append(item);
			}
			if (labels.get(setIds.toString()) != null)
				return (String) labels.get(setIds.toString());

		}
		return null;
	}

	private String getLabelStringBuilder(EQDOCColumnValue cv) {
		StringBuilder setIds = new StringBuilder();
		int i = 0;
		for (EQDOCValueSet vs : cv.getValueSet()) {
			i++;
			if (i > 1)
				setIds.append(",");
			setIds.append(vs.getId());
		}
		if (labels.get(setIds.toString()) != null)
			return (String) labels.get(setIds.toString());
		else {
			return getLabelGetString(cv);
		}
	}

	private String getLabelGetString(EQDOCColumnValue cv) {
		int i;
		i = 0;
		for (EQDOCValueSet vs : cv.getValueSet()) {
			i++;
			if (vs.getValues() != null)
				if (vs.getValues().size() > 0) {
					if (vs.getValues().get(0).getDisplayName() != null) {
						counter++;
						return (vs.getValues().get(0).getDisplayName() + " ....");
					}
				} else if (vs.getAllValues() != null && vs.getAllValues().getValues() != null
						&& vs.getAllValues().getValues().get(0).getDisplayName() != null) {
					counter++;
					return (vs.getAllValues().getValues().get(0).getDisplayName() + " ....");
				}
		}
		return null;
	}


}
