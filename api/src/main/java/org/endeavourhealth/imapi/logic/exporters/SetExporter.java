package org.endeavourhealth.imapi.logic.exporters;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.endeavourhealth.imapi.dataaccess.EntityRepository2;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.model.CoreLegacyCode;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

@Component
public class SetExporter {

    private EntityRepository2 entityRepository2 = new EntityRepository2();
    private EntityTripleRepository entityTripleRepository = new EntityTripleRepository();

    public AmazonS3 publishSetToIM1(String setIri){
        Set<CoreLegacyCode> coreMembers = new HashSet<>();
        Set<CoreLegacyCode> legacyMembers = new HashSet<>();
        Set<String> setIris = new HashSet<>();
        setIris.add(setIri);
        List<String> members = entityRepository2.getMemberIris(setIri);
        for(String member : members){
            if(entityRepository2.isSet(member)){
                setIris.add(member);
            }
        }
        for(String iri : setIris){
            TTArray definition = entityTripleRepository.getEntityPredicates(iri, Set.of(IM.DEFINITION.getIri()), 0).getEntity().get(IM.DEFINITION);
            coreMembers = entityRepository2.getSetExpansion(definition, false);
            legacyMembers = entityRepository2.getSetExpansion(definition, true);
        }
        StringJoiner results = new StringJoiner("\t");
        for(CoreLegacyCode coreMember : coreMembers){
            results.add(coreMember.getIri()).add(coreMember.getTerm()).add(coreMember.getCode()).add(coreMember.getScheme().getIri()).add(System.lineSeparator());
        }
        for(CoreLegacyCode legacyMember : legacyMembers){
            results.add(legacyMember.getIri()).add(legacyMember.getTerm()).add(legacyMember.getLegacyCode()).add(legacyMember.getLegacyScheme().getIri()).add(System.lineSeparator());
        }
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();
        try {
            s3.putObject("arn:aws:s3:::imv1sender", "valueset.tsv", results.toString());
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        return s3;

    }

}
