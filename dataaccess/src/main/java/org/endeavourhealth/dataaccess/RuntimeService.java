package org.endeavourhealth.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.endeavourhealth.dataaccess.entity.Concept;
import org.endeavourhealth.dataaccess.entity.Tpl;
import org.endeavourhealth.dataaccess.repository.ConceptRepository;
import org.endeavourhealth.dataaccess.repository.ConceptTripleRepository;
import org.endeavourhealth.dataaccess.repository.TermCodeRepository;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RuntimeService {
    private static final Logger LOG = LoggerFactory.getLogger(RuntimeService.class);
    private static Map<String, String> schemeMap ;

    ConfigService configService = new ConfigService();

    ConceptRepository conceptRepository = new ConceptRepository();

    ConceptTripleRepository conceptTripleRepository = new ConceptTripleRepository();

    TermCodeRepository termCodeRepository = new TermCodeRepository();

    public String getConceptIdForSchemeCode(String scheme, String code) throws SQLException {
        if(scheme==null || scheme.isEmpty() || code == null || code.isEmpty())
            return "";
        return termCodeRepository.findByCodeAndScheme_Iri(code, scheme).getIri();

    }

//    public String getMappedCoreCodeForSchemeCode(String scheme, String code, boolean snomedOnly) throws SQLException {
//        if(scheme==null || scheme.isEmpty() || code == null || code.isEmpty())
//            return "";
//        Concept coreConcept = snomedOnly
//                ? getMappedCoreSnomedConcept(scheme, code)
//                : getMappedCoreConcept(scheme, code);
//        if (coreConcept == null)
//            return null;
//        return coreConcept.getCode();
//    }
//
//    private Concept getMappedCoreSnomedConcept(String scheme, String code) throws SQLException {
//        Concept concept = getMappedCoreConcept(scheme, code);
//        if(concept == null)
//            return null;
//        if(IM.CODE_SCHEME_SNOMED.getIri().equals(concept.getIri())){
//            return concept;
//        }else
//            return null;
//    }

//    private Concept getMappedCoreConcept(String scheme, String code) throws SQLException {
//        Concept legacy = termCodeRepository.findByCodeAndScheme_Iri(code, scheme);
//        List<Concept> maps = conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(legacy.getIri(), IM.MATCHED_TO.getIri())
//                .stream()
//                .map(Tpl::getObject)
//                .collect(Collectors.toList());
//        if(maps.isEmpty())
//            return  null;
//        if(maps.size()>1)
//            LOG.warn("Multiple maps found for scheme "+ scheme + " and code "+ code);
//        return maps.get(0);
//    }

    public String getCodeForTypeTerm(String scheme, String context, String term, boolean autoCreate) {
        return null;
    }

    public Integer getConceptDbidForSchemeCode(String scheme, String code) throws SQLException {
        if(scheme==null || scheme.isEmpty() || code == null || code.isEmpty())
            return null;
        return termCodeRepository.findByCodeAndScheme_Iri(code, scheme).getDbid();
    }

//    public Integer getMappedCoreConceptDbidForSchemeCode(String scheme, String code ) throws SQLException {
//        if(scheme==null || scheme.isEmpty() || code == null || code.isEmpty())
//            return null;
//        Concept coreConcept = getMappedCoreConcept(scheme,code);
//        if(coreConcept==null)
//            return null;
//        return  coreConcept.getDbid();
//    }

    public String getCodeForConceptDbid(Integer dbid) throws SQLException {
        if(dbid==null)
            return "";
        return conceptRepository.findByDbid(dbid);
    }

    public Integer getConceptDbidForTypeTerm(String type, String term, Boolean autoCreate) {
        return null;
    }

    public Integer getMappedCoreConceptDbidForTypeTerm(String type, String term) {
        return null;
    }

    public Boolean isInVSet(String code, String v1Scheme, String vSet) throws JsonProcessingException, SQLException {
        if (code == null || code.isEmpty() || v1Scheme == null || v1Scheme.isEmpty() || vSet == null || vSet.isEmpty())
            return false;

        String scheme = getSchemeMap().get(v1Scheme);

        return included(code, scheme, vSet) && !excluded(code, scheme, vSet);
    }

    private Boolean included(String code, String scheme, String vSet) throws SQLException {
        return conceptRepository.isCoreCodeSchemeIncludedInVSet(code, scheme, vSet) != null
                || conceptRepository.isLegacyCodeSchemeIncludedInVSet(code, scheme, vSet) != null;
    }

    private Boolean excluded(String code, String scheme, String vSet) throws SQLException {
        return conceptRepository.isCoreCodeSchemeExcludedInVSet(code, scheme, vSet) != null
                || conceptRepository.isLegacyCodeSchemeExcludedInVSet(code, scheme, vSet) != null;
    }

    private Map<String, String> getSchemeMap() throws JsonProcessingException, SQLException {
        if(schemeMap==null)
        {
            TypeReference<HashMap<String,String>> ref = new TypeReference<HashMap<String,String>>() {};
            schemeMap = configService.getConfig("schemeMap", ref);
        }
        return schemeMap;
    }
}

