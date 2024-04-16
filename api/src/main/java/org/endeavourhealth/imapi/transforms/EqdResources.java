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
import org.endeavourhealth.imapi.vocabulary.GRAPH;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EqdResources {
    Map<String, String> reportNames = new HashMap<>();
    private final ImportMaps importMaps = new ImportMaps();
    private final Map<Object, Object> vocabMap = new HashMap<>();
    private Properties dataMap;
    private Properties labels;
    private String activeReport;
    private String activeReportName;
    private ModelDocument document;
    private final Map<String, Set<TTIriRef>> valueMap = new HashMap<>();
    private int counter = 0;
    private boolean isLinked;


    public Map<String, String> getReportNames() {
        return reportNames;
    }

    public EqdResources setReportNames(Map<String, String> reportNames) {
        this.reportNames = reportNames;
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
            return iri(RDFS.LABEL).setName("label");
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


    public Match convertCriteria(EQDOCCriteria eqCriteria) throws DataFormatException, IOException, QueryException {
        if ((eqCriteria.getPopulationCriterion() != null)) {
            EQDOCSearchIdentifier srch = eqCriteria.getPopulationCriterion();
            Match match= new Match();
            match
              .addIs(new Node().setIri("urn:uuid:" + srch.getReportGuid()))
              .setName(reportNames.get(srch.getReportGuid()));
            return match;
        } else {
            return convertCriterion(eqCriteria.getCriterion());
        }
    }


    private Match convertCriterion(EQDOCCriterion eqCriterion) throws DataFormatException, IOException, QueryException {

        if (eqCriterion.getLinkedCriterion() != null) {
            isLinked= true;
            Match match= convertLinkedCriterion(eqCriterion);
            //nestWheres(match);
            return match;
        } else {
            isLinked=false;
            Match match= convertStandardCriterion(eqCriterion);
            //nestWheres(match);
            return match;
        }

    }
    private void nestWheres(Match match){
        if (match.getWhere()!=null){
            if (match.getWhere().size()>1){
                List<Where> wheres= match.getWhere();
                Where newWhere= new Where();
                newWhere.setBool(Bool.and);
                newWhere.setWhere(wheres);
                match.setWhere(List.of(newWhere));
            }
        }
        if (match.getMatch()!=null){
            for (Match subMatch:match.getMatch())
                nestWheres(subMatch);
        }
        if (match.getThen()!=null){
            nestWheres(match.getThen());
        }
    }

    private Match convertStandardCriterion(EQDOCCriterion eqCriterion) throws DataFormatException, IOException, QueryException {
        if (eqCriterion.getFilterAttribute().getRestriction() != null) {
            Match match= convertRestrictionCriterion(eqCriterion);
            if (eqCriterion.isNegation()) {
                match.setExclude(true);
            }
            return match;


        } else {
             Match match=convertColumns(eqCriterion);
            if (eqCriterion.isNegation()) {
                match.setExclude(true);
            }
            return match;

        }
    }

    private Match convertColumns(EQDOCCriterion eqCriterion) throws DataFormatException, IOException, QueryException {
        EQDOCFilterAttribute filterAttribute = eqCriterion.getFilterAttribute();
        List<EQDOCColumnValue> cvs = filterAttribute.getColumnValue();
        return convertColumnValues(cvs, eqCriterion.getTable());
    }

    private void convertPaths(List<Match> matches, Map<String,Match> pathMatchMap,String eqTable,EQDOCColumnValue cv) throws DataFormatException, IOException {
        String tablePath= getPath(eqTable);
        String eqColumn = String.join("/", cv.getColumn());
        String columnPath= getPath(eqTable+"/"+ eqColumn);
        String property;
        if (columnPath.contains(" ")){
            property= columnPath.substring(columnPath.lastIndexOf(" ")+1);
            columnPath= columnPath.substring(0,columnPath.lastIndexOf(" "));
        }
        else {
            property= columnPath;
            columnPath="";
        }
        String fullPath= (tablePath+" "+ columnPath).trim();
        Match match=pathMatchMap.get(fullPath);
        if (match==null) {
            match = new Match();
            matches.add(match);
            pathMatchMap.put(fullPath,match);
            if (tablePath.contains(" ")) {
                match.addPath(new IriLD().setIri(IM.NAMESPACE + tablePath.split(" ")[0].replace("^", "")));
                match.setTypeOf(new Node().setIri(IM.NAMESPACE + tablePath.split(" ")[1]));
            }
            if(columnPath.contains(" ")) {
                String[] paths= columnPath.split(" ");
                for (int i=0; i<paths.length; i=i+2){
                    Where subProperty= new Where();
                    match.addWhere(subProperty);
                    subProperty.setIri(IM.NAMESPACE+ columnPath.split(" ")[i]);
                    subProperty.setMatch(new Match());
                    match=subProperty.getMatch();
                    match.setTypeOf(new Node().setIri(IM.NAMESPACE+columnPath.split(" ")[i+1]));
                    }
                }
        }
        Where where= new Where();
        match.addWhere(where);
        where.setIri(IM.NAMESPACE+property);
        setProperty(cv,where);

    }


    private Match convertColumnValues(List<EQDOCColumnValue> cvs,String eqTable) throws QueryException, DataFormatException, IOException {
        List<Match> matches= new ArrayList<>();
        Map<String,Match> pathMatchMap= new HashMap<>();
        for (EQDOCColumnValue cv : cvs) {
            convertPaths(matches,pathMatchMap,eqTable,cv);
        }

        if (matches.size()==1){
            return matches.get(0);
        }
        else {
            Match match= new Match();
            match.setBool(Bool.and);
            match.setMatch(matches);
            return match;
        }
    }



    private void setProperty(EQDOCColumnValue cv, Where pv) throws DataFormatException, IOException {

        VocColumnValueInNotIn in = cv.getInNotIn();
        boolean notIn = (in == VocColumnValueInNotIn.NOTIN);
        if (!cv.getValueSet().isEmpty()) {
            setPropertyValueSetSetters(cv, pv, notIn);
        } else if (!CollectionUtils.isEmpty(cv.getLibraryItem())) {
            String valueLabel = "";
            for (String vset : cv.getLibraryItem()) {
                String vsetName = null;
                if (labels.get(vset) != null) {
                    vsetName = (String) labels.get(vset);
                } else
                    vsetName = "unknown concept set";
                valueLabel = valueLabel + (valueLabel.equals("") ? "" : ", ") + vsetName;
                Node iri = new Node().setIri("urn:uuid:" + vset);
                if (vsetName != null)
                    iri.setName(vsetName);
                else
                    iri.setName("Unknown value set");
                if (!notIn)
                    pv.addIs(iri);
                else {
                    pv.addIsNot(iri);
                }
                if (valueLabel.equals(""))
                    valueLabel = "Unknown value set";
                pv.setValueLabel(valueLabel);
            }
        } else if (cv.getRangeValue() != null) {
            setRangeValue(cv.getRangeValue(), pv);
        }
    }

    private void setPropertyValueSetSetters(EQDOCColumnValue cv, Where pv, boolean notIn) throws DataFormatException, IOException {
        for (EQDOCValueSet vs : cv.getValueSet()) {
            if (vs.getId() != null)
                if (labels.get(vs.getId()) != null) {
                    pv.setValueLabel(labels.get(vs.getId()).toString());
                }
            if (vs.getAllValues() != null) {
                pv.setIsNot(getExceptionSet(vs.getAllValues()));
            } else {
                if (!notIn) {
                    pv.addIs(getInlineValues(vs, pv));
                } else {
                    pv.addIsNot(getInlineValues(vs, pv));
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


    private Match convertRestrictionCriterion(EQDOCCriterion eqCriterion) throws DataFormatException, IOException, QueryException {
        Match restricted = convertColumns(eqCriterion);
        setRestriction(eqCriterion, restricted);
        if (eqCriterion.getFilterAttribute().getRestriction().getTestAttribute() != null) {
            counter++;
            String variable = "match_" + counter;
            if (isLinked)
                restricted.setVariable(variable);
            Match testMatch = restrictionTest(eqCriterion, variable);
            if (testMatch!=null) {
                restricted.setThen(testMatch);
            }

        }
        return restricted;
    }


    private Match restrictionTest(EQDOCCriterion eqCriterion, String nodeVariable) throws IOException, DataFormatException, QueryException {
        EQDOCTestAttribute testAtt = eqCriterion.getFilterAttribute().getRestriction().getTestAttribute();
        if (testAtt != null) {
            return convertColumnValues(testAtt.getColumnValue(),eqCriterion.getTable());
        }
        return null;

    }

    private void setRestriction(EQDOCCriterion eqCriterion, Match restricted) throws DataFormatException {
        Order direction;
        EQDOCFilterRestriction restrict = eqCriterion.getFilterAttribute().getRestriction();
        if (restrict.getColumnOrder().getColumns().get(0).getDirection() == VocOrderDirection.ASC) {
            direction = Order.ascending;
        } else {
            direction = Order.descending;
        }
        String linkColumn = eqCriterion.getFilterAttribute().getRestriction()
                .getColumnOrder().getColumns().get(0).getColumn().get(0);
        String orderBy = getPath(eqCriterion.getTable() + "/" + linkColumn);
        counter++;
        restricted.orderBy(o -> o
                .setProperty(new OrderDirection()
                        .setIri(orderBy)
                        .setDirection(direction))
                .setLimit(1));
    }


    private Match convertLinkedCriterion(EQDOCCriterion eqCriterion) throws DataFormatException, IOException, QueryException {
        Match topMatch= convertStandardCriterion(eqCriterion);
        counter++;
        String variable = "match_" + counter;
        topMatch.setVariable(variable);

        EQDOCLinkedCriterion eqLinked = eqCriterion.getLinkedCriterion();
        EQDOCCriterion eqLinkedCriterion = eqLinked.getCriterion();
        Match linkMatch= convertCriterion(eqLinkedCriterion);
        topMatch.setThen(linkMatch);
        Where relationProperty = new Where();
        linkMatch.addWhere(relationProperty);
        EQDOCRelationship eqRel = eqLinked.getRelationship();
        String parent = getPath(eqCriterion.getTable() + "/" + eqRel.getParentColumn());
        String child = getPath(eqLinkedCriterion.getTable() + "/" + eqRel.getChildColumn());
        relationProperty
                .setIri(child)
                .setOperator((Operator) vocabMap.get(eqRel.getRangeValue().getRangeFrom().getOperator()))
                .setValue(eqRel.getRangeValue().getRangeFrom().getValue().getValue())
                .setUnit(eqRel.getRangeValue().getRangeFrom().getValue().getUnit().value())
                .relativeTo(r -> r.setNodeRef(variable).setIri(parent));
        return topMatch;
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
            comp = Operator.eq;
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
        if (units != null)
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
        Range range = new Range();
        pv.setRange(range);
        Value fromValue = new Value();
        range.setFrom(fromValue);
        setCompareFrom(fromValue, rFrom);
        Value toValue = new Value();
        range.setTo(toValue);
        setCompareTo(toValue, rTo);
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


    private Node getInlineValues(EQDOCValueSet vs, Where pv) throws DataFormatException, IOException {
        Set<Node> setContent = new HashSet<>();
        Set<Node> excContent = new HashSet<>();
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
            if (ev.getException().size()>0){
                for (EQDOCException exc:ev.getException()){
                    for (EQDOCExceptionValue val:exc.getValues()){
                        Set<Node> exceptionValue= getValue(scheme,val);
                        if (exceptionValue!=null){
                            for (Node iri:exceptionValue){
                                Node conRef = new Node().setIri(iri.getIri()).setName(iri.getName())
                                  .setDescendantsOrSelfOf(true);
                                excContent.add(conRef);
                            }
                        }
                    }
                }
            }

        }
        Query query= new Query();
        Match match = new Match();
        match.setBool(Bool.or);
        query.addMatch(match);
        String name="";
        int i=0;
        for (Node node:setContent){
            i++;
            Match member= new Match();
            match.addMatch(member);
            member.setInstanceOf(node);
            if (node.getName()!=null){
                if (i==3)
                    name=name+" + more...";
                else if (i==1)
                    name=getShortName(node.getName(),null);
                else if (i==2) {
                    name=getShortName(node.getName(),name);
                }
            }
        }
        if (!excContent.isEmpty()){
            name=name+" with exclusions";
            Match outerMatch= new Match();
            outerMatch.addMatch(match);
            outerMatch.setBool(Bool.and);
            query.setMatch(new ArrayList<>());
            query.addMatch(outerMatch);
            for (Node node:excContent){
                Match member= new Match();
                outerMatch.addMatch(member);
                member.setExclude(true);
                member.setInstanceOf(node);
            }
        }
        if (setContent.size()==1&&excContent.isEmpty()){
            return setContent.stream().findFirst().get();
        }
        else {
            ConceptSet set = new ConceptSet();
            set.setIri("urn:uuid:" + vs.getId());
            set.setName(name);
            set.setDefinition(query);
            set.addUsedIn(TTIriRef.iri("urn:uuid:" + activeReport));
            document.addConceptSet(set);
            return new Node().setIri(set.getIri());
        }
    }

    private String getShortName(String name, String previous){
        name=name.split(" \\(")[0];
        if (previous==null){
            return name;
        }
        else {
            if (name.contains("resolved"))
                return previous+" or resolved";
            else
                return previous+" or "+ name;
        }
    }


    private Set<Node> getValue(VocCodeSystemEx scheme, EQDOCExceptionValue ev) throws DataFormatException, IOException {
        return getValue(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
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
            schemes.add(GRAPH.EMIS);
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
            return importMaps.getCoreFromCodeId(originalCode, GRAPH.EMIS);
        } catch (Exception e) {
            System.err.println("unable to retrieve iri from term code " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    private Set<TTIriRef> getLegacyFromTermCode(String originalCode) {
        try {
            return importMaps.getLegacyFromTermCode(originalCode, GRAPH.EMIS);
        } catch (Exception e) {
            System.err.println("unable to retrieve iri match term code " + e.getMessage());
            return null;
        }
    }


    private Set<TTIriRef> getCoreFromLegacyTerm(String originalTerm) {
        try {
            if (originalTerm.contains("s disease of lymph nodes of head, face AND/OR neck"))
                System.out.println("!!");

            return importMaps.getCoreFromLegacyTerm(originalTerm, GRAPH.EMIS);
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
        } else if (cv.getLibraryItem() != null) {
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
