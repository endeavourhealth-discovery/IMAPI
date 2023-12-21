package org.endeavourhealth.imapi.transforms;

import org.apache.commons.collections4.CollectionUtils;
import org.endeavourhealth.imapi.logic.exporters.ImportMaps;
import org.endeavourhealth.imapi.model.iml.ConceptSet;
import org.endeavourhealth.imapi.model.iml.ModelDocument;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.endeavourhealth.imapi.vocabulary.im.GRAPH;

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
			return RDFS.LABEL.asTTIriRef().setName("label");
		else {
			if (!token.contains(":")) {
				TTIriRef iri = TTIriRef.iri(IM.NAMESPACE.iri + token);
				iri.setName(importMaps.getCoreName(IM.NAMESPACE.iri + token));
				return iri;
			} else {
				TTIriRef iri = TTIriRef.iri(token);
				iri.setName(importMaps.getCoreName(token));
				return iri;
			}
		}
	}


	public void convertCriteria(EQDOCCriteria eqCriteria,
															 Match match) throws DataFormatException, IOException, QueryException {

		if ((eqCriteria.getPopulationCriterion() != null)) {
			EQDOCSearchIdentifier srch = eqCriteria.getPopulationCriterion();
			match
				.addInSet(new Node().setIri("urn:uuid:" + srch.getReportGuid()))
				.setName(reportNames.get(srch.getReportGuid()));
		} else {
			convertCriterion(eqCriteria.getCriterion(),match);
		}
	}



	private void convertCriterion(EQDOCCriterion eqCriterion, Match match) throws DataFormatException, IOException, QueryException {
		if (eqCriterion.isNegation()) {
			match.setExclude(true);
		}

		if (eqCriterion.getLinkedCriterion() != null) {
			convertLinkedCriterion(eqCriterion, match);
		}
		else {
			convertStandardCriterion(eqCriterion, match);
		}

	}

	private Match getTablePath(String eqKey,Match match) throws DataFormatException, QueryException {
		String pathMap = getPath(eqKey);
		if (pathMap.equals(""))
			return match;
		String[] paths= pathMap.split(" ");
		for (int i=0; i<paths.length-1; i=i+2) {
				Property path = new Property();
				match.addProperty(path);
				path.setIri(paths[i]);
				Match subMatch = new Match();
				path.setMatch(subMatch);
				subMatch.setTypeOf(paths[i + 1]);
				match = subMatch;
		}
		return match;
	}

	private Match getPaths(Match match, String pathMap) throws QueryException {
		String[] paths= pathMap.split(" ");
			for (int i=0; i<paths.length-2; i=i+2) {
				Match subMatch = getPathMatch(match, paths[i]);
				if (subMatch == null) {
					Property path = new Property();
					match.addProperty(path);
					path.setIri(paths[i]);
					subMatch = new Match();
					path.setMatch(subMatch);
					subMatch.setTypeOf(paths[i + 1]);
					match = subMatch;
				}
				else
					return subMatch;
			}
			return match;
	}
	private Match getPathMatch(Match match, String property){
		if (match.getProperty()==null)
			return null;
		for (Property prop:match.getProperty()) {
			if (prop.getIri().equals(property)||prop.getIri().equals(IM.NAMESPACE.iri+property))
				return prop.getMatch();
		}
		return null;
	}

	private void convertStandardCriterion(EQDOCCriterion eqCriterion, Match match) throws DataFormatException, IOException, QueryException {
		if (eqCriterion.getFilterAttribute().getRestriction() != null) {
			convertRestrictionCriterion(eqCriterion, match);

		}
		else {
			match= getTablePath(eqCriterion.getTable(),match);
			convertColumns(eqCriterion, match);
		}
	}

	private void convertColumns(EQDOCCriterion eqCriterion, Match match) throws DataFormatException, IOException, QueryException {
		EQDOCFilterAttribute filterAttribute = eqCriterion.getFilterAttribute();
		List<EQDOCColumnValue> cvs= filterAttribute.getColumnValue();
		if (cvs.size()==1){
			EQDOCColumnValue cv= cvs.get(0);
			setColumnWhere(cv,eqCriterion.getTable(),match);
		}
		else {
			match.setBool(Bool.and);
			for (EQDOCColumnValue cv : filterAttribute.getColumnValue()) {
				setColumnWhere(cv,eqCriterion.getTable(),match);
			}
		}

	}

	private void setColumnWhere(EQDOCColumnValue cv, String eqTable, Match match) throws DataFormatException, IOException, QueryException {
		String eqColumn= String.join("/",cv.getColumn());
		String pathMap= getPath(eqTable+"/"+ eqColumn);
		match= getPaths(match,pathMap);
		Property property= new Property();
		match.addProperty(property);
		property.setIri(pathMap.substring(pathMap.lastIndexOf(" ")+1));;
		setWhere(cv,property);
	}


	private void setWhere(EQDOCColumnValue cv, Property pv) throws DataFormatException, IOException {

		VocColumnValueInNotIn in = cv.getInNotIn();
		boolean notIn = (in == VocColumnValueInNotIn.NOTIN);
		if (!cv.getValueSet().isEmpty()) {
			setWhereValueSetSetters(cv, pv, notIn);
		}
		else if (!CollectionUtils.isEmpty(cv.getLibraryItem())) {
			String valueLabel="";
			for (String vset : cv.getLibraryItem()) {
				String vsetName=null;
				if (labels.get(vset) != null){
					vsetName= (String) labels.get(vset);
				}
				else
					vsetName="unknown concept set";
				valueLabel= valueLabel+ (valueLabel.equals("") ?"": ", ")+ vsetName;
				Node iri = new Node().setIri("urn:uuid:" + vset);
				if (vsetName!=null)
					iri.setName(vsetName);
				else
					iri.setName("Unknown value set");
				if (!notIn)
					pv.addInSet(iri);
				else {
					pv.addNotInSet(iri);
				}
				storeLibraryItem(iri.getIri(),vsetName);
				if (valueLabel.equals(""))
					valueLabel="Unknown value set";
				pv.setValueLabel(valueLabel);
			}
		} else if (cv.getRangeValue() != null) {
			setRangeValue(cv.getRangeValue(), pv);
		}
	}

	private void setWhereValueSetSetters(EQDOCColumnValue cv, Property pv, boolean notIn) throws DataFormatException, IOException {
		for (EQDOCValueSet vs : cv.getValueSet()) {
			if (vs.getId()!=null)
				if (labels.get(vs.getId())!=null) {
					pv.setValueLabel(labels.get(vs.getId()).toString());
				}
			if (vs.getAllValues() != null) {
				pv.setIsNot(getExceptionSet(vs.getAllValues()));
			} else {
				if (!notIn) {
					pv.setIs(getInlineValues(vs,pv));
				}
				else {
					pv.setIsNot(getInlineValues(vs,pv));
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


	private void convertRestrictionCriterion(EQDOCCriterion eqCriterion, Match match) throws DataFormatException, IOException, QueryException {
		Match restricted=match;
		if (eqCriterion.getFilterAttribute().getRestriction().getTestAttribute()!=null) {
			match.setBool(Bool.and);
			restricted = new Match();
			match.addMatch(restricted);
		}

		restricted = getTablePath(eqCriterion.getTable(),restricted);
		convertColumns(eqCriterion, restricted);
		setRestriction(eqCriterion, restricted);

		if (eqCriterion.getFilterAttribute().getRestriction().getTestAttribute()!=null) {
			counter++;
			String variable="match_"+counter;
			restricted.setVariable(variable);
			Match testMatch = new Match();
			testMatch.setNodeRef(variable);
			match.addMatch(testMatch);
			restrictionTest(eqCriterion, testMatch,variable);
		}
	}




	private void restrictionTest(EQDOCCriterion eqCriterion, Match testMatch,String nodeVariable) throws IOException, DataFormatException, QueryException {
		EQDOCTestAttribute testAtt= eqCriterion.getFilterAttribute().getRestriction().getTestAttribute();
		if (testAtt != null) {
			testMatch.setBool(Bool.and);
				for (EQDOCColumnValue cv : testAtt.getColumnValue()) {
					setColumnWhere(cv,eqCriterion.getTable(),testMatch);
				}
			}

	}

	private String setRestriction(EQDOCCriterion eqCriterion, Match restricted) throws DataFormatException {
	 Order direction;
		EQDOCFilterRestriction restrict = eqCriterion.getFilterAttribute().getRestriction();
		if (restrict.getColumnOrder().getColumns().get(0).getDirection() == VocOrderDirection.ASC) {
			direction= Order.ascending;
		}
		else {
			direction=Order.descending;
		}
		String linkColumn = eqCriterion.getFilterAttribute().getRestriction()
			.getColumnOrder().getColumns().get(0).getColumn().get(0);
		String orderBy= getPath(eqCriterion.getTable()+"/"+linkColumn);
		counter++;
		String linkElement= restricted.getProperty().get(0).getVariable();
		restricted.orderBy(o->o
			.addProperty(new OrderDirection()
				.setIri(orderBy)
				.setDirection(direction))
			.setLimit(1));
		return linkElement;
	}



			private void convertLinkedCriterion(EQDOCCriterion eqCriterion, Match topMatch) throws DataFormatException, IOException, QueryException {
				Match targetMatch= new Match();
				topMatch.addMatch(targetMatch);
				convertStandardCriterion(eqCriterion,targetMatch);
				counter++;
				String variable= "match_"+counter;
				targetMatch.setVariable(variable);
				Match linkMatch= new Match();
				topMatch.addMatch(linkMatch);
				EQDOCLinkedCriterion eqLinked= eqCriterion.getLinkedCriterion();
				EQDOCCriterion eqLinkedCriterion= eqLinked.getCriterion();
				convertCriterion(eqLinkedCriterion,linkMatch);
				Property relationWhere = new Property();
				linkMatch.addProperty(relationWhere);
				EQDOCRelationship eqRel = eqLinked.getRelationship();
				String parent=getPath(eqCriterion.getTable()+"/"+ eqRel.getParentColumn());
				String child=getPath(eqLinkedCriterion.getTable()+"/"+ eqRel.getChildColumn());
					relationWhere
						.setIri(child)
						.setOperator((Operator) vocabMap.get(eqRel.getRangeValue().getRangeFrom().getOperator()))
						.setValue(eqRel.getRangeValue().getRangeFrom().getValue().getValue())
						.setUnit(eqRel.getRangeValue().getRangeFrom().getValue().getUnit().value())
						.relativeTo(r->r.setNodeRef(variable).setIri(parent));

			}

			private void setRangeValue(EQDOCRangeValue rv, Property pv) throws DataFormatException {

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
					pv.setRelativeTo(new PropertyRef().setParameter("$referenceDate"));
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


			private void setRangeCompare(Property pv, EQDOCRangeFrom rFrom, EQDOCRangeTo rTo) throws DataFormatException {
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


			private List<Node> getExceptionSet(EQDOCException set) throws DataFormatException, IOException {
				List<Node> valueSet = new ArrayList<>();
				VocCodeSystemEx scheme = set.getCodeSystem();
				for (EQDOCExceptionValue ev : set.getValues()) {
					Set<Node> values = getValue(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
					if (values != null) {
						valueSet.addAll(new ArrayList<>(values));
					} else
						System.err.println("Missing exception sets\t" + ev.getValue() + "\t " + ev.getDisplayName());
				}

				return valueSet;
			}



			private List<Node> getInlineValues(EQDOCValueSet vs, Property pv) throws DataFormatException, IOException {
				List<Node> setContent = new ArrayList<>();
				VocCodeSystemEx scheme = vs.getCodeSystem();
				for (EQDOCValueSetValue ev : vs.getValues()) {
					Set<Node> concepts = getValue(scheme, ev);
					if (concepts != null) {
						for (Node iri : concepts) {
							Node conRef = new Node().setIri(iri.getIri()).setName(iri.getName())
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
						.addType(IM.CONCEPT_SET.asTTIriRef())
						.setName(name);
					conceptSet.addUsedIn(TTIriRef.iri("urn:uuid:" + activeReport));
					valueSets.put(iri, conceptSet);
					valueSets.get(iri).addUsedIn(TTIriRef.iri("urn:uuid:" + activeReport).setName(activeReportName));

				}
			}

			private Set<Node> getValue(VocCodeSystemEx scheme, EQDOCValueSetValue ev) throws DataFormatException, IOException {
				return getValue(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
			}

			private Set<Node> getValue(VocCodeSystemEx scheme, String originalCode,
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
					schemes.add(GRAPH.EMIS.getIri());
					Set<TTIriRef> snomed = valueMap.get(originalCode);
					if (snomed == null) {
						snomed = setValueSnomedChecks(originalCode, originalTerm, legacyCode, schemes);
						if (snomed != null)
							valueMap.put(originalCode, snomed);
					}
					if (snomed != null)
						return snomed.stream().map(e -> new Node().setIri(e.getIri()).setName(e.getName())).collect(Collectors.toSet());
					else return null;
				} else
					throw new DataFormatException("code scheme not recognised : " + scheme.value());

			}

			private Set<Node> getValueIriResult(Object mapValue) throws IOException {
				Node iri = new Node().setIri(mapValue.toString());
				String name = importMaps.getCoreName(iri.getIri());
				if (name != null)
					iri.setName(name);
				Set<Node> result = new HashSet<>();
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
					return importMaps.getCoreFromCodeId(originalCode, GRAPH.EMIS.iri);
				} catch (Exception e) {
					System.err.println("unable to retrieve iri from term code " + e.getMessage());
					e.printStackTrace();
					return null;
				}
			}


			private Set<TTIriRef> getLegacyFromTermCode(String originalCode) {
				try {
					return importMaps.getLegacyFromTermCode(originalCode, GRAPH.EMIS.iri);
				} catch (Exception e) {
					System.err.println("unable to retrieve iri match term code " + e.getMessage());
					return null;
				}
			}



			private Set<TTIriRef> getCoreFromLegacyTerm(String originalTerm) {
				try {
					if (originalTerm.contains("s disease of lymph nodes of head, face AND/OR neck"))
						System.out.println("!!");

					return importMaps.getCoreFromLegacyTerm(originalTerm, GRAPH.EMIS.iri);
				} catch (Exception e) {
					System.err.println("unable to retrieve iri match term " + e.getMessage());
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
					StringBuilder setIris = new StringBuilder();
					int i = 0;
					for (String item : cv.getLibraryItem()) {
						i++;
						if (i > 1)
							setIris.append(",");
						setIris.append(item);
					}
					if (labels.get(setIris.toString()) != null)
						return (String) labels.get(setIris.toString());

				}
				return null;
			}

			private String getLabelStringBuilder(EQDOCColumnValue cv) {
				StringBuilder setIris = new StringBuilder();
				int i = 0;
				for (EQDOCValueSet vs : cv.getValueSet()) {
					i++;
					if (i > 1)
						setIris.append(",");
					setIris.append(vs.getId());
				}
				if (labels.get(setIris.toString()) != null)
					return (String) labels.get(setIris.toString());
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
