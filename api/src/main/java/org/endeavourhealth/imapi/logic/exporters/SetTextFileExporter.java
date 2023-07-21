package org.endeavourhealth.imapi.logic.exporters;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.EntityRepository2;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.IMLToECL;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.StringJoiner;

public class SetTextFileExporter {
    private static final Logger LOG = LoggerFactory.getLogger(SetTextFileExporter.class);
    private final EntityTripleRepository entityTripleRepository = new EntityTripleRepository();
    private final SetExporter setExporter = new SetExporter();
    private final EntityRepository2 entityRepository2 = new EntityRepository2();

    public String getSetFile(String setIri, boolean definition, boolean core, boolean legacy, boolean includeSubsets, boolean ownRow, boolean im1id, String del) throws QueryException, JsonProcessingException {
        LOG.debug("Exporting set to TSV");
        String setName = entityRepository2.getBundle(setIri,Set.of(RDFS.LABEL.getIri())).getEntity().getName();

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
            result = generateFile(setName,members, legacy, ownRow, im1id, del, includeSubsets).toString();
        }
        return result ;
    }

    private String getEcl(TTEntity entity) throws QueryException, JsonProcessingException {
        if (entity.get(IM.DEFINITION) == null)
            return null;
        return IMLToECL.getECLFromQuery(entity.get(IM.DEFINITION).asLiteral().objectValue(Query.class), true);
    }

    private StringJoiner generateFile(String setName, Set<Concept> members, boolean includeLegacy, boolean ownRow, boolean im1id, String del, boolean includeSubset) {

        LOG.trace("Generating output...");

        StringJoiner results = new StringJoiner(System.lineSeparator());
        addHeader(includeLegacy, ownRow, im1id, results, del, includeSubset);

        for(Concept member : members) {
            String isExtension = member.getScheme().getIri().contains("sct#") ? "N" : "Y";
            if (member.getMatchedFrom() != null && includeLegacy){
                addLegacy(ownRow, im1id, results, member, setName, isExtension, del, includeSubset);
            } else {
                addOnlyCore(im1id, results, member, setName, isExtension, del, includeSubset);
            }
            if(includeLegacy && ownRow) {
                results.add("");
            }
        }
        return results;
    }

    private static void addHeader(boolean includeLegacy, boolean ownRow, boolean im1id, StringJoiner results, String del, boolean includeSubset) {
        if(includeSubset) {
            if(includeLegacy) {
                if(ownRow){
                    results.add("core code"+ del +"core term"+ del +"set"+ del +"subset"+ del +"extension");
                } else {
                    if(im1id){
                        results.add("core code"+ del +"core term"+ del +"set"+ del +"subset"+ del +"extension"+ del +"legacy code"+ del +"legacy term"+ del +"legacy scheme"+ del +"codeId"+ del +"usage"+ del +"im1id");
                    } else {
                        results.add("core code"+ del +"core term"+ del +"set"+ del +"subset"+ del +"extension"+ del +"legacy code"+ del +"legacy term"+ del +"legacy scheme"+ del +"codeId");
                    }
                }
            } else {
                if(im1id) {
                    results.add("core code"+ del +"core term"+ del +"set"+ del +"subset"+ del +"extension"+ del +"usage"+ del +"im1id");
                } else {
                    results.add("core code"+ del +"core term"+ del +"set"+ del +"subset"+ del +"extension");
                }
            }
        } else {
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

    }

    private void addLegacy(boolean ownRow, boolean im1id, StringJoiner results, Concept member, String setName, String isExtension, String del, boolean includeSubset) {
        String subSet = member.getIsContainedIn() != null ? member.getIsContainedIn().iterator().next().getName() : null;
        if(ownRow) {
            if(includeSubset && subSet != null) {
                addLineData(del, results, member.getCode(), member.getName(), setName, subSet, isExtension);
            } else {
                addLineData(del, results, member.getCode(), member.getName(), setName, isExtension);
            }
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
                        if(includeSubset && subSet != null) {
                            addLineData(del, results, member.getCode(), member.getName(), setName, subSet, isExtension, legacy.getCode(),
                                    legacy.getName(), legacy.getScheme().getIri(), legacy.getCodeId(), usage, im1);
                        } else {
                            addLineData(del, results, member.getCode(), member.getName(), setName, isExtension, legacy.getCode(),
                                    legacy.getName(), legacy.getScheme().getIri(), legacy.getCodeId(), usage, im1);
                        }
                    });
                } else {
                    if(includeSubset && subSet != null) {
                        addLineData(del, results, member.getCode(), member.getName(), setName, subSet, isExtension, legacy.getCode(),
                                legacy.getName(), legacy.getScheme().getIri(), legacy.getCodeId());
                    } else {
                        addLineData(del, results, member.getCode(), member.getName(), setName, isExtension, legacy.getCode(),
                                legacy.getName(), legacy.getScheme().getIri(), legacy.getCodeId());
                    }
                }
            }
        }
    }

    private void addOnlyCore(boolean im1id, StringJoiner results, Concept member, String setName, String isExtension, String del, boolean includeSubset) {
        String usage = member.getUsage() != null ? member.getUsage().toString() : null;
        String subSet = member.getIsContainedIn() != null ? member.getIsContainedIn().iterator().next().getName() : null;

        if(im1id && member.getIm1Id() != null) {
            member.getIm1Id().forEach(im1 -> {
                if(includeSubset && subSet != null) {
                    addLineData(del, results, member.getCode(), member.getName(), setName, subSet, isExtension, usage, im1);
                } else {
                    addLineData(del, results, member.getCode(), member.getName(), setName, isExtension, usage, im1);
                }
            });
        } else {
            if(includeSubset && subSet != null) {
                addLineData(del, results, member.getCode(), member.getName(), setName, subSet, isExtension);
            } else {
                addLineData(del, results, member.getCode(), member.getName(), setName, isExtension);
            }
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
