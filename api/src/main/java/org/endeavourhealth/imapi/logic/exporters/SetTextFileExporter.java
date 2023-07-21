package org.endeavourhealth.imapi.logic.exporters;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.IMLToECL;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.StringJoiner;

public class SetTextFileExporter {
    private static final Logger LOG = LoggerFactory.getLogger(SetTextFileExporter.class);
    private final EntityTripleRepository entityTripleRepository = new EntityTripleRepository();
    private final SetExporter setExporter = new SetExporter();

    public String getSetFile(String setIri, boolean definition, boolean core, boolean legacy, boolean includeSubsets, boolean ownRow, boolean im1id, String del) throws QueryException, JsonProcessingException {
        LOG.debug("Exporting set to TSV");

        String result = "";

        TTEntity entity = entityTripleRepository.getEntityPredicates(setIri, Set.of(IM.DEFINITION.getIri())).getEntity();
        if (entity.getIri() == null || entity.getIri().isEmpty())
            return null;

        String ecl = getEcl(entity);
        if(ecl != null && definition) {
            result = ecl;
        }

        if (core || legacy) {
            Set<Concept> members = setExporter.getExpandedSetMembers(setIri, legacy, includeSubsets);
            result = generateFile(members, legacy, ownRow, im1id, del).toString();
        }
        return result ;
    }

    private String getEcl(TTEntity entity) throws QueryException, JsonProcessingException {
        if (entity.get(IM.DEFINITION) == null)
            return null;
        return IMLToECL.getECLFromQuery(entity.get(IM.DEFINITION).asLiteral().objectValue(Query.class), true);
    }

    private StringJoiner generateFile(Set<Concept> members, boolean includeLegacy, boolean ownRow, boolean im1id, String del) {

        LOG.trace("Generating output...");

        StringJoiner results = new StringJoiner(System.lineSeparator());
        addHeader(includeLegacy, ownRow, im1id, results, del);

        for(Concept member : members) {
            String set = member.getIsContainedIn().iterator().next().getName();
            String isExtension = member.getScheme().getIri().contains("sct#") ? "N" : "Y";
            if (member.getMatchedFrom() != null && includeLegacy){
                addLegacy(ownRow, im1id, results, member, set, isExtension, del);
            } else {
                addOnlyCore(im1id, results, member, set, isExtension, del);
            }
            if(includeLegacy && ownRow) {
                results.add("");
            }
        }
        return results;
    }

    private static void addHeader(boolean includeLegacy, boolean ownRow, boolean im1id, StringJoiner results, String del) {
        if(includeLegacy) {
            if(ownRow){
                results.add("core code"+ del +"core term"+ del +"set"+ del +"extension");
            } else {
                if(im1id){
                    results.add("core code"+ del +"core term"+ del +"set"+ del +"extension"+ del +"legacy code"+ del +"legacy term"+ del +"legacy scheme"+ del +"codeId"+ del +"usage"+ del +"im1id");
                } else {
                    results.add("core code"+ del +"core term"+ del +"set"+ del +"extension"+ del +"legacy code"+ del +"legacy term"+ del +"legacy scheme"+ del +"codeId");
                }
            }
        } else {
            if(im1id) {
                results.add("core code"+ del +"core term"+ del +"set"+ del +"extension"+ del +"usage"+ del +"im1id");
            } else {
                results.add("core code"+ del +"core term"+ del +"set"+ del +"extension");
            }
        }
    }

    private void addLegacy(boolean ownRow, boolean im1id, StringJoiner results, Concept member, String set, String isExtension, String del) {
        if(ownRow) {
            addLineData(del, results, member.getCode(), member.getName(), set, isExtension);
        }
        for (Concept legacy: member.getMatchedFrom()) {
            String usage = legacy.getUsage() != null ? legacy.getUsage().toString() : null;
            if(ownRow) {
                if(im1id && legacy.getIm1Id() != null) {
                    legacy.getIm1Id().forEach(im1 -> {
                        addLineData(del, results, legacy.getCode(), legacy.getName(), legacy.getScheme().getIri(),
                            legacy.getCodeId(), usage, im1);
                    });
                } else {
                    addLineData(del, results, legacy.getCode(), legacy.getName(), legacy.getScheme().getIri(), legacy.getCodeId());
                }
            } else {
                if(im1id && legacy.getIm1Id() != null) {
                    legacy.getIm1Id().forEach(im1 -> {
                        addLineData(del, results, member.getCode(), member.getName(), set, isExtension, legacy.getCode(),
                            legacy.getName(), legacy.getScheme().getIri(), legacy.getCodeId(), usage, im1);
                    });
                } else {
                    addLineData(del, results, member.getCode(), member.getName(), set, isExtension, legacy.getCode(),
                            legacy.getName(), legacy.getScheme().getIri(), legacy.getCodeId());
                }
            }
        }
    }

    private void addOnlyCore(boolean im1id, StringJoiner results, Concept member, String set, String isExtension, String del) {
        String usage = member.getUsage() != null ? member.getUsage().toString() : null;
        if(im1id && member.getIm1Id() != null) {
            member.getIm1Id().forEach(im1 -> {
                addLineData(del, results, member.getCode(), member.getName(), set, isExtension, usage, im1);
            });
        } else {
            addLineData(del, results, member.getCode(), member.getName(), set, isExtension);
        }

    }

    private void addLineData(String del, StringJoiner results, Object... values) {
        StringJoiner line = new StringJoiner(del);
        for (Object value : values) {
            line.add((CharSequence) value);
        }
        results.add(line.toString());
    }


}
