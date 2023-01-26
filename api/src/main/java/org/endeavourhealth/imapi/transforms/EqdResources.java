package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.collections4.CollectionUtils;
import org.endeavourhealth.imapi.logic.exporters.ImportMaps;
import org.endeavourhealth.imapi.model.iml.*;
import org.endeavourhealth.imapi.model.tripletree.*;
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
	private final Map<Object, String> vocabMap = new HashMap<>();
	private Properties dataMap;
	private Properties labels;
	private String activeReport;
	private String activeReportName;
	private ModelDocument document;
	private final Map<String,Set<TTIriRef>> valueMap= new HashMap<>();
	private int counter=0;
	private int whereCount=0;
	public Map<TTIriRef, ConceptSet> valueSets = new HashMap<>();


	public Map<TTIriRef, ConceptSet> getValueSets() {
		return valueSets;
	}

	public EqdResources setValueSets(Map<TTIriRef, ConceptSet> valueSets) {
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
		vocabMap.put(VocRangeFromOperator.GTEQ, ">=");
		vocabMap.put(VocRangeFromOperator.GT, ">");
		vocabMap.put(VocRangeToOperator.LT, "<");
		vocabMap.put(VocRangeToOperator.LTEQ, "<=");
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
	public void setFrom(Query query, TTIriRef parent) {
		TTAlias from = new TTAlias();
		from.setIsSet(true);
		from.setIri(parent.getIri());
		if (parent.getName()!=null)
			from.setName(parent.getName());
		query.addFrom(from);
	}




	public void convertCriteria(EQDOCCriteria eqCriteria,
															 Where where) throws DataFormatException, IOException {

		if ((eqCriteria.getPopulationCriterion() != null)) {
			EQDOCSearchIdentifier srch = eqCriteria.getPopulationCriterion();
			where.from(f->f
					.setIsSet(true)
					.setIri("urn:uuid:" + srch.getReportGuid())
					.setName(reportNames.get(srch.getReportGuid()))
				);
		} else {
			convertCriterion(eqCriteria.getCriterion(),where);
		}
	}





	private void convertCriterion(EQDOCCriterion eqCriterion, Where match) throws DataFormatException, IOException {
		Where negatationWhere=match;
		if (eqCriterion.isNegation()){
			Where notWhere= new Where();
			match.setNotExist(notWhere);
			match= notWhere;
		}
		String entityPath= getPath(eqCriterion.getTable());
		if (!entityPath.equals("")) {
			match.setPathTo(entityPath);
		}

		if (eqCriterion.getLinkedCriterion() != null) {
			convertLinkedCriterion(eqCriterion, match);
		}
		else if (eqCriterion.getFilterAttribute().getRestriction() != null) {
			convertRestrictionCriterion(eqCriterion, match,null);
		}
		else {
			convertColumns(eqCriterion, match);
		}
		if (eqCriterion.isNegation()){
			negatationWhere.setAlias(summarise(negatationWhere,null));
		}

	}



	private void convertLinkedCriterion(EQDOCCriterion eqCriterion, Where topWhere) throws DataFormatException, IOException {
		Where targetWhere= topWhere;
		Select targetSelect;
		if (eqCriterion.getFilterAttribute().getRestriction().getTestAttribute()!=null){
			targetSelect= new Select();
			topWhere.setWith(targetSelect);
			targetWhere = new Where();
			targetSelect.setWhere(targetWhere);
			convertRestrictionCriterion(eqCriterion, targetWhere,IM.NAMESPACE+"Date");
		}
		else if (eqCriterion.getFilterAttribute().getRestriction() != null) {
			convertRestrictionCriterion(eqCriterion, topWhere,IM.NAMESPACE+"Date");
			targetSelect= topWhere.getWith();
		}
		else {
			targetSelect= new Select();
			topWhere.setWith(targetSelect);
			targetWhere = new Where();
			targetSelect.setWhere(targetWhere);
			targetSelect.setProperty(IM.NAMESPACE+"Date");
			targetSelect.setAlias("Date of "+ targetWhere.getAlias());

			convertColumns(eqCriterion, targetWhere);
		}
		counter++;
		targetSelect.setVariable("target_"+counter);
		Where linkWhere= new Where();
		topWhere.addAnd(linkWhere);

		EQDOCLinkedCriterion eqLinked= eqCriterion.getLinkedCriterion();
		EQDOCCriterion eqLinkedCriterion= eqLinked.getCriterion();
		if (eqLinkedCriterion.getFilterAttribute().getRestriction() != null) {
				convertRestrictionCriterion(eqLinkedCriterion, linkWhere,null);
			}
			else {
				convertColumns(eqCriterion, linkWhere);
			}
		Where relationWhere = new Where();
		topWhere.addAnd(relationWhere);

		EQDOCRelationship eqRel = eqLinked.getRelationship();
		if (eqRel.getParentColumn().contains("DATE")){
			relationWhere.setProperty(new TTAlias().setIri(IM.NAMESPACE+"effectiveDate"));
			Within within= new Within();
			relationWhere.setWithin(within);
			Value value= new Value();
			within.setValue(value);
			value
				.setComparison(vocabMap.get(eqRel.getRangeValue().getRangeFrom().getOperator()))
				.setValue(eqRel.getRangeValue().getRangeFrom().getValue().getValue());
			value.setUnitOfTime(eqRel.getRangeValue().getRangeFrom().getValue().getUnit().value());
			within.setOf(new Compare()
					.setAlias(targetSelect.getAlias())
				.setVariable(targetSelect.getVariable())
				.setProperty(TTIriRef.iri(IM.NAMESPACE+"effectiveDate")));
			relationWhere.setAlias(summarise(relationWhere,null));
		}
		else
			throw new DataFormatException("Only date link fields supported at the moment");


	}


	private void convertRestrictionCriterion(EQDOCCriterion eqCriterion, Where topWhere,String selectWhat) throws DataFormatException, IOException {
		Select select= new Select();
		if (selectWhat!=null) {
			select.setProperty(selectWhat);
		}
		topWhere.setWith(select);
		Where restrictionWhere= new Where();
		select.setWhere(restrictionWhere);
		if (eqCriterion.getDescription() != null)
			restrictionWhere.setDescription(eqCriterion.getDescription());
		convertColumns(eqCriterion, restrictionWhere);
		setRestriction(eqCriterion, select,restrictionWhere);
		restrictionTest(eqCriterion, select, topWhere);
		if (selectWhat!=null){
			select.setAlias("Date of "+ restrictionWhere.getAlias());
		}
	}

	private void restrictionTest(EQDOCCriterion eqCriterion, Select select, Where testWhere) throws IOException, DataFormatException {
		EQDOCTestAttribute testAtt= eqCriterion.getFilterAttribute().getRestriction().getTestAttribute();
		if (testAtt != null) {
				List<EQDOCColumnValue> cvs= testAtt.getColumnValue();
				if (cvs.size()==1){
					setMainCriterion(eqCriterion.getTable(), cvs.get(0), testWhere,"");
					if (testWhere.getAlias()==null)
						testWhere.setAlias(summarise(testWhere,testAtt.getColumnValue().get(0)));
				}
				else {
					for (EQDOCColumnValue cv : cvs) {
						Where pv = new Where();
						testWhere.addAnd(pv);
						setMainCriterion(eqCriterion.getTable(), cv, pv,"");
						if (pv.getAlias()==null)
							pv.setAlias(summarise(pv,cv));
					}
					testWhere.setAlias(summarise(testWhere,null));
				}

			}
	}


	private void convertColumns(EQDOCCriterion eqCriterion, Where match) throws DataFormatException, IOException {
		match.setAlias(getLabel(eqCriterion));
		EQDOCFilterAttribute filterAttribute = eqCriterion.getFilterAttribute();
		String entityPath= getPath(eqCriterion.getTable());
		List<EQDOCColumnValue> cvs= filterAttribute.getColumnValue();
		if (cvs.size()==1){
			setMainCriterion(eqCriterion.getTable(), cvs.get(0),match,entityPath);
			match.setAlias(summarise(match,cvs.get(0)));
		}
		else {
			for (EQDOCColumnValue cv : filterAttribute.getColumnValue()) {
				Where pv = new Where();
				match.addAnd(pv);
				setMainCriterion(eqCriterion.getTable(), cv, pv,"");
				pv.setAlias(summarise(pv,cv));
			}
		}
	}




	private void setMainCriterion(String eqTable, EQDOCColumnValue cv, Where pv,String mainPath) throws DataFormatException, IOException {
		String eqColumn= String.join("/",cv.getColumn());
		setWhere(cv, eqTable, eqColumn, pv,mainPath);
	}



	private void setWhere(EQDOCColumnValue cv, String eqTable, String eqColumn,
																Where pv,String mainPath) throws DataFormatException, IOException {
        String subPath = getPath(eqTable + "/" + eqColumn);

        if (!mainPath.equals(""))
            subPath = mainPath + " " + subPath;

        if (subPath.contains(" ")) {
            pv.setPathTo(subPath.substring(0, subPath.lastIndexOf(" ")));
            pv.setProperty(new TTAlias().setIri(subPath.substring(subPath.lastIndexOf(" ") + 1)));
        } else
            pv.setProperty(new TTAlias().setIri(subPath));

        VocColumnValueInNotIn in = cv.getInNotIn();
        boolean notIn = (in == VocColumnValueInNotIn.NOTIN);
        if (!cv.getValueSet().isEmpty()) {
            setWhereValueSet(cv, pv, notIn);
        } else if (!CollectionUtils.isEmpty(cv.getLibraryItem())) {
            setWhereLibraryItem(cv, pv, notIn);
        } else if (cv.getRangeValue() != null) {
            setRangeValue(cv.getRangeValue(), pv);
        }

    }

    private void setWhereValueSet(EQDOCColumnValue cv, Where pv, boolean notIn) throws DataFormatException, IOException {
        for (EQDOCValueSet vs : cv.getValueSet()) {
            if ((vs.getId() != null) && (labels.get(vs.getId()) != null)) {
                    String alias = labels.get(vs.getId()).toString();
                    pv.setAlias(pv.getAlias() == null ? alias : pv.getAlias() + " " + alias);
                }
            if (vs.getAllValues() != null) {
                pv.setNot(true);
                pv.setIn(getExceptionSet(vs.getAllValues()));
            } else {
                if (!notIn) {
                    pv.setIn(getInlineValues(vs));
                } else {
                    pv.setNot(true);
                    pv.setIn(getInlineValues(vs));
                }
            }
        }
    }

    private void setWhereLibraryItem(EQDOCColumnValue cv, Where pv, boolean notIn) {
        for (String vset : cv.getLibraryItem()) {
            String vsetName = "Unknown code set";
            if (labels.get(vset) != null)
                vsetName = (String) labels.get(vset);
            TTAlias iri = TTAlias.iri("urn:uuid:" + vset).setName(vsetName);
            if (!notIn)
                pv.addIn(iri);
            else {
                pv.setNot(true);
                pv.addIn(iri);
            }
            storeLibraryItem(iri);
        }
    }

    public String getPath(String eqdPath) throws DataFormatException {
		Object target = dataMap.get(eqdPath);
		if (target == null)
			throw new DataFormatException("unknown map : " + eqdPath);
		String targetPath= (String) target;
		if (targetPath.equals(""))
			return "";
		String[] paths = targetPath.split(" ");
		List<String> pathList = Arrays.stream(paths).map(p-> IM.NAMESPACE+p).collect(Collectors.toList());
		if (pathList.size()==1){
			return pathList.get(0);
		}
		else {
			return String.join(" ",pathList);
		}
	}


	private void setRestriction(EQDOCCriterion eqCriterion, Select select,Where restrictionWhere) throws DataFormatException {
		String eqTable = eqCriterion.getTable();
		String linkColumn = eqCriterion.getFilterAttribute().getRestriction()
			.getColumnOrder().getColumns().get(0).getColumn().get(0);

		select.setLimit(1);
		EQDOCFilterRestriction restrict = eqCriterion.getFilterAttribute().getRestriction();
		String direction;
		if (restrict.getColumnOrder().getColumns().get(0).getDirection() == VocOrderDirection.ASC) {
			direction= "ASC";
			select.setAlias("Earliest"+ restrictionWhere.getAlias());
		}
		else {
			direction= "DESC";
			select.setAlias("Latest" + restrictionWhere.getAlias());
		}
		select.addOrderBy(new OrderBy()
			.setIri(getPath(eqTable + "/" + linkColumn))
			.setDirection(direction));
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

	private void setCompareFrom(Where pv, EQDOCRangeFrom rFrom) throws DataFormatException {
		String comp;
		if (rFrom.getOperator() != null)
			comp = vocabMap.get(rFrom.getOperator());
		else
			comp = "=";
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

	private void setCompare(Where pv, String comp, String value, String units, VocRelation relation,
													boolean from) throws DataFormatException {
		if (relation == VocRelation.RELATIVE) {
			Within within= new Within();
			pv.setWithin(within);
			within.setOf(new Compare().setVariable("$referenceDate"));
			if (from) {
				comp = reverseComp(comp);
				value = String.valueOf(-Integer.parseInt(value));
			}
			String finalComp = comp;
			String finalValue = value;
			within
				.value(v->v
					.setComparison(finalComp)
					.setValue(finalValue)
					.setUnitOfTime(units));
		}
		else {
			String finalComp1 = comp;
			String finalValue1 = value;
			pv
				.value(v -> v
					.setComparison(finalComp1)
					.setValue(finalValue1));
			if (pv.getProperty().getIri().contains("age")) {
				if (units == null)
					throw new DataFormatException("missing units from age");
				pv.getValue()
					.setUnitOfTime(units);
			}
		}

	}

	private String reverseComp(String comp) {
		switch (comp) {
			case ">=":
				return "<=";
			case ">":
				return "<";
			case "<":
				return ">";
			case "<=":
				return ">=";
            default:
                return comp;
		}
	}


	private void setCompareTo(Where pv, EQDOCRangeTo rTo) throws DataFormatException {
		String comp;
		if (rTo.getOperator() != null)
			comp = vocabMap.get(rTo.getOperator());
		else
			comp = "=";
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
        Range range= new Range();
        setRangeCompareFrom(pv, rFrom, range);
        setRangeCompareTo(rTo, range);

    }

    private void setRangeCompareFrom(Where pv, EQDOCRangeFrom rFrom, Range range) throws DataFormatException {
        String fromComp;
        Within within= null;
        if (rFrom.getOperator() != null)
            fromComp = vocabMap.get(rFrom.getOperator());
        else
            fromComp = "=";
        String fromValue = rFrom.getValue().getValue();
        String units = null;
        if (rFrom.getValue().getUnit() != null)
            units = rFrom.getValue().getUnit().value();
        if (rFrom.getValue().getRelation() != null && rFrom.getValue().getRelation() == VocRelation.RELATIVE) {
            within= new Within();
            pv.setWithin(within);
            within.setRange(range);
            within.of(w->w
                .setVariable("$referenceDate"));
            range.setFrom(new Value()
                .setComparison(fromComp)
                .setValue(fromValue));
            if (units!=null)
                range.getFrom().setUnitOfTime(units);
        }
        else {
            pv.setRange(range);
            range.setFrom(new Value()
                .setComparison(fromComp)
                .setValue(fromValue));
            if (pv.getProperty().getIri().contains("age")) {
                if (units == null)
                    throw new DataFormatException("missing units from age");
                range.getFrom().setUnitOfTime(units);
            }
        }
    }

    private void setRangeCompareTo(EQDOCRangeTo rTo, Range range) {
        String units;
        String toComp;
        if (rTo.getOperator() != null)
            toComp = vocabMap.get(rTo.getOperator());
        else
            toComp = "=";
        String toValue = rTo.getValue().getValue();
        units = null;
        if (rTo.getValue().getUnit() != null)
            units = rTo.getValue().getUnit().value();
        if (rTo.getValue().getRelation() != null && rTo.getValue().getRelation() == VocRelation.RELATIVE) {
            range.setTo(new Value()
                .setComparison(toComp)
                .setValue(toValue));
            if (units!=null)
                range.getTo().setUnitOfTime(units);
        }
        else {
            range.setTo(new Value()
                .setComparison(toComp)
                .setValue(toValue));
        }
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



	private List<TTAlias> getInlineValues(EQDOCValueSet vs) throws DataFormatException, IOException {
		List<TTAlias> setContent = new ArrayList<>();
		VocCodeSystemEx scheme = vs.getCodeSystem();
		for (EQDOCValueSetValue ev : vs.getValues()) {
			Set<TTAlias> concepts = getValue(scheme, ev);
			if (concepts != null) {
				for (TTAlias iri : concepts) {
					TTAlias conRef = new TTAlias().setIri(iri.getIri()).setName(iri.getName());
					setContent.add(conRef);
				}
			} else
				System.err.println("Missing \t" + ev.getValue() + "\t " + ev.getDisplayName());

		}
		return setContent;
	}


	private List<TTAlias> getValueSet(EQDOCValueSet vs) throws DataFormatException, IOException {
		List<TTAlias> setContent = new ArrayList<>();
		StringBuilder vsetName = new StringBuilder();
		VocCodeSystemEx scheme = vs.getCodeSystem();
		if (labels.get(vs.getId())!=null){
			vsetName.append((String) labels.get(vs.getId()));
		}
		if (vs.getDescription() != null)
			vsetName = new StringBuilder(vs.getDescription());
        getValueSetValues(vs, setContent, vsetName, scheme);

        storeValueSet(vs, setContent, vsetName.toString());
		setContent.add(TTAlias.iri("urn:uuid:" + vs.getId()).setName(vsetName.toString()));
		return setContent;
	}

    private void getValueSetValues(EQDOCValueSet vs, List<TTAlias> setContent, StringBuilder vsetName, VocCodeSystemEx scheme) throws DataFormatException, IOException {
        int i = 0;
        for (EQDOCValueSetValue ev : vs.getValues()) {
            i++;
            Set<TTAlias> concepts = getValue(scheme, ev);
            if (concepts != null) {
                setContent.addAll(new ArrayList<>(concepts));
                if (i == 1) {
                    if (ev.getDisplayName() != null) {
                        vsetName.append(ev.getDisplayName());
                    } else {
                        Optional<TTAlias> first = concepts.stream().findFirst();
						first.ifPresent(ttAlias -> vsetName.append(ttAlias.getName()));
                    }
                }
                if (i == 2)
                    vsetName.append("AndMore");
            } else
                System.err.println("Missing \t" + ev.getValue() + "\t " + ev.getDisplayName());

        }
    }

    private void storeLibraryItem(TTIriRef iri) {
		if (!valueSets.containsKey(iri)) {
			ConceptSet conceptSet = new ConceptSet();
				conceptSet.setIri(iri.getIri())
				.setType(IM.CONCEPT_SET)
				.setName(iri.getName());
			conceptSet.addUsedIn(TTIriRef.iri("urn:uuid:" + activeReport));
			valueSets.put(TTIriRef.iri(iri.getIri()), conceptSet);
			valueSets.get(iri).addUsedIn(TTIriRef.iri("urn:uuid:" + activeReport).setName(activeReportName));

		}
	}

	private void storeValueSet(EQDOCValueSet vs, List<TTAlias> valueSet, String vSetName) throws JsonProcessingException {
		if (vs.getId() != null) {
			TTIriRef iri = TTIriRef.iri("urn:uuid:" + vs.getId()).setName(vSetName);
			if (!valueSets.containsKey(iri)) {
				ConceptSet conceptSet = new ConceptSet();
					conceptSet.setIri(iri.getIri())
					.setType(IM.CONCEPT_SET)
					.setName(vSetName);
				Query definition= new Query();

				for (TTAlias member : valueSet)
					definition.addFrom(member);
				conceptSet.setDefinition(definition);
				valueSets.put(iri, conceptSet);
			}
			valueSets.get(iri).addUsedIn(TTIriRef.iri("urn:uuid:" + activeReport).setName(activeReportName));
		}
	}

	private Set<TTAlias> getValue(VocCodeSystemEx scheme, EQDOCValueSetValue ev) throws DataFormatException, IOException {
		return getValue(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
	}

	private Set<TTAlias> getValue(VocCodeSystemEx scheme, String originalCode,
																	 String originalTerm, String legacyCode) throws DataFormatException, IOException {
		if (scheme == VocCodeSystemEx.EMISINTERNAL) {
            return getValueEmisInternal(originalCode);
        } else if (scheme == VocCodeSystemEx.SNOMED_CONCEPT || scheme.value().contains("SCT")) {
            return getValueSnomed(originalCode, originalTerm, legacyCode);
        } else {
			throw new DataFormatException("code scheme not recognised : " + scheme.value());
		}
	}

    private Set<TTAlias> getValueEmisInternal(String originalCode) throws IOException, DataFormatException {
        String key = "EMISINTERNAL/" + originalCode;
        Object mapValue = dataMap.get(key);
        if (mapValue != null) {
            TTAlias iri = new TTAlias(getIri(mapValue.toString()));
            String name = importMaps.getCoreName(iri.getIri());
            if (name != null)
                iri.setName(name);
            Set<TTAlias> result = new HashSet<>();
            result.add(iri);
            return result;
        } else
            throw new DataFormatException("unmapped emis internal code : " + key);
    }

    private Set<TTAlias> getValueSnomed(String originalCode, String originalTerm, String legacyCode) {
        List<String> schemes = new ArrayList<>();
        schemes.add(SNOMED.NAMESPACE);
        schemes.add(IM.CODE_SCHEME_EMIS.getIri());
        Set<TTIriRef> snomed = valueMap.get(originalCode);
        if (snomed == null) {

            snomed = getCoreFromCode(originalCode, schemes);
            if ((snomed == null) && (legacyCode != null))
                    snomed = getCoreFromCode(legacyCode, schemes);
            if ((snomed == null) && (originalTerm != null))
                    snomed = getCoreFromLegacyTerm(originalTerm);
            if (snomed == null)
                snomed = getCoreFromCodeId(originalCode);
            if (snomed == null)
                snomed = getLegacyFromTermCode(originalCode);

            if (snomed != null)
                valueMap.put(originalCode, snomed);
        }
        if (snomed!=null)
            return snomed.stream().map(e -> TTAlias.iri(e.getIri()).setName(e.getName())).collect(Collectors.toSet());
        else return null;
    }


    private Set<TTIriRef> getCoreFromCodeId(String originalCode) {

		try {
			return importMaps.getCoreFromCodeId(originalCode, IM.CODE_SCHEME_EMIS.getIri());
		} catch (Exception e) {
			System.err.println("unable to retrieve iri from term code " + e.getMessage());
			// e.printStackTrace();
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

	private String getLabel(EQDOCCriterion eqCriterion) {

		String label = null;
        List<EQDOCColumnValue> cvs = eqCriterion.getFilterAttribute().getColumnValue();
        if (cvs != null) {
            for (EQDOCColumnValue cv : cvs) {
                if (cv.getValueSet() != null) {
                    label = getLabelValueSet(cv.getValueSet());
                } else if (cv.getLibraryItem() != null) {
                    label = getLabelLibraryItem(cv.getLibraryItem());
                }
				if (label != null)
					return label;
            }
        }
        if (labels.get(eqCriterion.getId()) != null)
            return (String) labels.get(eqCriterion.getId());

        return "NoAlias_" + (++counter);
    }

    private String getLabelValueSet(List<EQDOCValueSet> sets) {
        String setIds = sets.stream()
            .map(EQDOCValueSet::getId)
            .collect(Collectors.joining(","));

        if (labels.get(setIds) != null)
            return (String) labels.get(setIds);

        Optional<EQDOCValueSet> vs = sets.stream()
            .filter(vsv -> vsv.getValues() != null && vsv.getValues().get(0) != null)
            .findFirst();

        if (vs.isPresent()) {
            return vs.get().getValues().get(0).getDisplayName().split(" ")[0] + "_" + (++counter);
        }

        return null;
    }
    private String getLabelLibraryItem(List<String> libraryItems) {
        String setIds = String.join(",", libraryItems);
        if (labels.get(setIds) != null)
            return (String) labels.get(setIds);
        return null;
    }

	private String summarise(Where match,EQDOCColumnValue cv) {
        StringBuilder summary = new StringBuilder();
        if (cv != null) {
            if (labels.get(cv.getId()) != null) {
                return labels.get(cv.getId()).toString();
            }
            if (cv.getValueSet() != null) {
                summarizeValueSet(cv, summary);
            }
            if (cv.getLibraryItem() != null) {
                summarizeLibraryItem(cv, summary);
            }
        }

        if (summary.toString().isEmpty())
            summariseWhere(match, summary);

        if (summary.toString().isEmpty())
            summary.append("Criteria_").append(++whereCount);

        return summary.toString();
    }

    private void summarizeLibraryItem(EQDOCColumnValue cv, StringBuilder summary) {
        StringBuilder setIds = new StringBuilder();
        int i = 0;
        for (String item : cv.getLibraryItem()) {
            i++;
            if (i > 1)
                setIds.append(",");
            setIds.append(item);
        }
        if (labels.get(setIds.toString()) != null)
            summary.append((String) labels.get(setIds.toString()));
    }

    private void summarizeValueSet(EQDOCColumnValue cv, StringBuilder summary) {
        StringBuilder setIds = new StringBuilder();
        int i = 0;
        for (EQDOCValueSet vs : cv.getValueSet()) {
            i++;
            if (i > 1)
                setIds.append(",");
            setIds.append(vs.getId());
        }
        if (labels.get(setIds.toString()) != null)
            summary.append((String) labels.get(setIds.toString()));
    }

    private void summariseWhere(Where where,StringBuilder summary) {
        if (where.getNotExist() != null) {
            summary.append("Not ").append(where.getNotExist().getAlias());
        }

        if (where.isNot())
            summary.append("not in ");

        if (where.getProperty() != null) {
            summariseWhereProperty(where, summary);
        } else if (where.getRange() != null) {
            summary.append(" ").append(summariseRange(where.getRange()));
        }

        if (where.getIn() != null) {
            summariseWhereIn(where, summary);
        } else if (where.getAnd() != null) {
            summariseWhereAnd(where, summary);
        }
    }

    private void summariseWhereProperty(Where where, StringBuilder summary) {
        String property = localName(where.getProperty().getIri());
        if (where.getWithin() != null) {
            Within within = where.getWithin();
            summary.append(property);
            summary.append("within ");
            if (within.getValue() != null)
                summary.append(" ").append(summariseValue(within.getValue()));
            else
                summary.append(" ").append(summariseRange(within.getRange()));
            summary.append(" ").append(summariseCompare(within.getOf()));
        } else if (where.getValue() != null)
            summary.append(" ").append(summariseValue(where.getValue()));
    }

    private static void summariseWhereIn(Where where, StringBuilder summary) {
        Optional<TTAlias> firstNamed = where.getIn().stream().filter(in -> in.getName() != null).findFirst();
        if (firstNamed.isPresent())
            summary.append(firstNamed.get().getName());

        if (where.getIn().size() > 1)
            summary.append(" (and mode) ");
    }

    private static void summariseWhereAnd(Where where, StringBuilder summary) {
        List<String> ands = where.getAnd().stream().map(Where::getAlias).collect(Collectors.toList());
        summary.append(String.join("+", ands));
    }

    private String summariseRange(Range range) {
		String result="from "+ summariseValue(range.getFrom());
		if (range.getFrom().getUnitOfTime()!=null)
		   result =result+"to "+ summariseValue((range.getTo()));
		if (range.getTo().getUnitOfTime()!=null)
			result= result+" "+range.getTo().getUnitOfTime();
		return result;
	}



	private String summariseCompare(Compare compare) {
		if (compare.getVariable()!=null){
			if (compare.getVariable().equals("$referenceDate"))
				return " of ref date";
			else
				return " of "+compare.getVariable();
		}
		if (compare.getAlias()!=null){
			return " of "+compare.getAlias();
		}
		return "";
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


	private String localName(String path){
		if (!path.contains(" "))
			return path.substring(path.lastIndexOf("#")+1);
		else {
			String[] iris= path.split(" ");
			List<String> locals= Arrays.stream(iris).sequential().map(i-> i.substring(i.lastIndexOf("#")+1)).collect(Collectors.toList());
			return String.join(", ",locals);
		}
	}


	private String summariseValue(Value value){
		String result= value.getComparison() + " " +
			value.getValue();
		if (value.getUnitOfTime()!=null)
			result=result+" "+ value.getUnitOfTime();
		return result;
	}







}
