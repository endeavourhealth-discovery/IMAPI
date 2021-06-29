package org.endeavourhealth.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.endeavourhealth.dataaccess.repository.EntityRepository;
import org.endeavourhealth.dataaccess.repository.EntityTripleRepository;
import org.endeavourhealth.dataaccess.repository.TermCodeRepository;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RuntimeService {
    private static final Logger LOG = LoggerFactory.getLogger(RuntimeService.class);
    private static Map<String, String> schemeMap ;

    ConfigService configService = new ConfigService();

    EntityRepository entityRepository = new EntityRepository();

    EntityTripleRepository entityTripleRepository = new EntityTripleRepository();

    TermCodeRepository termCodeRepository = new TermCodeRepository();

    public String getEntityIdForSchemeCode(String scheme, String code) throws SQLException {
        if(scheme==null || scheme.isEmpty() || code == null || code.isEmpty())
            return "";
        return termCodeRepository.findByCodeAndScheme_Iri(code, scheme).getIri();

    }

    public String getMappedCoreCodeForSchemeCode(String scheme, String code, boolean snomedOnly) throws SQLException {
        if(scheme==null || scheme.isEmpty() || code == null || code.isEmpty())
            return "";
        ValueSetMember coreEntity = snomedOnly
                ? getMappedCoreSnomedEntity(scheme, code)
                : getMappedCoreEntity(scheme, code);
        if (coreEntity == null)
            return null;
        return coreEntity.getCode();
    }

    private ValueSetMember getMappedCoreSnomedEntity(String scheme, String code) throws SQLException {
        ValueSetMember entity = getMappedCoreEntity(scheme, code);
        if(entity == null)
            return null;
        if(IM.CODE_SCHEME_SNOMED.getIri().equals(entity.getEntity().getIri())){
            return entity;
        }else
            return null;
    }

    private ValueSetMember getMappedCoreEntity(String scheme, String code) throws SQLException {
        TTIriRef legacy = termCodeRepository.findByCodeAndScheme_Iri(code, scheme);
        Set<ValueSetMember> maps = entityTripleRepository.getObjectBySubjectAndPredicate(legacy.getIri(), IM.MATCHED_TO.getIri());
        if(maps.isEmpty())
            return  null;
        if(maps.size()>1)
            LOG.warn("Multiple maps found for scheme "+ scheme + " and code "+ code);
        return maps.iterator().next();
    }

    public String getCodeForTypeTerm(String scheme, String context, String term, boolean autoCreate) {
        return null;
    }

    public Integer getEntityDbidForSchemeCode(String scheme, String code) throws SQLException {
        if(scheme==null || scheme.isEmpty() || code == null || code.isEmpty())
            return null;
        return termCodeRepository.findDbidByCodeAndScheme_Iri(code, scheme);
    }

    public Integer getMappedCoreEntityDbidForSchemeCode(String scheme, String code ) throws SQLException {
        if(scheme==null || scheme.isEmpty() || code == null || code.isEmpty())
            return null;
        ValueSetMember coreEntity = getMappedCoreEntity(scheme,code);

        if(coreEntity==null)
            return null;

        return null;
    }

    public String getCodeForEntityDbid(Integer dbid) throws SQLException {
        if(dbid==null)
            return "";
        return entityRepository.findByDbid(dbid);
    }

    public Integer getEntityDbidForTypeTerm(String type, String term, Boolean autoCreate) {
        return null;
    }

    public Integer getMappedCoreEntityDbidForTypeTerm(String type, String term) {
        return null;
    }

    public Boolean isInVSet(String code, String v1Scheme, String vSet) throws JsonProcessingException, SQLException {
        if (code == null || code.isEmpty() || v1Scheme == null || v1Scheme.isEmpty() || vSet == null || vSet.isEmpty())
            return false;

        String scheme = getSchemeMap().get(v1Scheme);

        return included(code, scheme, vSet) && !excluded(code, scheme, vSet);
    }

    private Boolean included(String code, String scheme, String vSet) throws SQLException {
        return entityRepository.isCoreCodeSchemeIncludedInVSet(code, scheme, vSet) != null
                || entityRepository.isLegacyCodeSchemeIncludedInVSet(code, scheme, vSet) != null;
    }

    private Boolean excluded(String code, String scheme, String vSet) throws SQLException {
        return entityRepository.isCoreCodeSchemeExcludedInVSet(code, scheme, vSet) != null
                || entityRepository.isLegacyCodeSchemeExcludedInVSet(code, scheme, vSet) != null;
    }

    private Map<String, String> getSchemeMap() throws JsonProcessingException, SQLException {
        if(schemeMap==null)
        {
            TypeReference<HashMap<String,String>> ref = new TypeReference<>() {
            };
            schemeMap = configService.getConfig("schemeMap", ref);
        }
        return schemeMap;
    }
}

