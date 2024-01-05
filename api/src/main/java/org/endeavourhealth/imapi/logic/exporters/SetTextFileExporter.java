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

import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class SetTextFileExporter {
    private static final Logger LOG = LoggerFactory.getLogger(SetTextFileExporter.class);
    private final EntityTripleRepository entityTripleRepository = new EntityTripleRepository();
    private final SetExporter setExporter = new SetExporter();
    private final EntityRepository2 entityRepository2 = new EntityRepository2();

    public String getSetFile(String setIri, boolean definition, boolean core, boolean legacy, boolean includeSubsets,
                             boolean ownRow, boolean im1id, List<String> schemes, String del) throws QueryException, JsonProcessingException {
        LOG.debug("Exporting set to TSV");
        String setName = entityRepository2.getBundle(setIri,Set.of(RDFS.LABEL)).getEntity().getName();

        String result = "";

        TTEntity entity = entityTripleRepository.getEntityPredicates(setIri, Set.of(IM.DEFINITION)).getEntity();
        if (entity.getIri() == null || entity.getIri().isEmpty())
            return null;

        String ecl = getEcl(entity);
        if(ecl != null && definition) {
            result = ecl;
        }

        if (core || legacy) {
            Set<Concept> members = setExporter.getExpandedSetMembers(setIri, legacy, includeSubsets, schemes)
                    .stream().sorted(Comparator.comparing(Concept::getName)).collect(Collectors.toCollection(LinkedHashSet::new));

            if(includeSubsets) {
                members = members.stream().sorted(Comparator.comparing(m -> m.getIsContainedIn().iterator().next().getName()))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
            }
            result = generateFile(setName,members, legacy, ownRow, im1id, del, includeSubsets).toString();
        }
        return result ;
    }

    private String getEcl(TTEntity entity) throws QueryException, JsonProcessingException {
        if (entity.get(iri(IM.DEFINITION)) == null)
            return null;
        return IMLToECL.getECLFromQuery(entity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class), true);
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
                    results.add(getCoreHeaderWithSubset(del));
                } else {
                    if(im1id){
                        results.add(getCoreHeaderWithSubset(del) + del + getLegacyHeader(del)+ del +"im1id");
                    } else {
                        results.add(getCoreHeaderWithSubset(del) + del + getLegacyHeader(del));
                    }
                }
            } else {
                if(im1id) {
                    results.add(getCoreHeaderWithSubset(del) + del +"im1id");
                } else {
                    results.add(getCoreHeaderWithSubset(del));
                }
            }
        } else {
            if(includeLegacy) {
                if(ownRow){
                    results.add(getCoreHeader(del));
                } else {
                    if(im1id){
                        results.add(getCoreHeader(del) + del + getLegacyHeader(del) + del +"im1id");
                    } else {
                        results.add(getCoreHeader(del) + del + getLegacyHeader(del));
                    }
                }
            } else {
                if(im1id) {
                    results.add(getCoreHeader(del) + del +"im1id");
                } else {
                    results.add(getCoreHeader(del));
                }
            }
        }

    }

    private void addLegacy(boolean ownRow, boolean im1id, StringJoiner results, Concept member, String setName, String isExtension, String del, boolean includeSubset) {
        String scheme = member.getScheme().getName();
        String subSet = member.getIsContainedIn() != null ? member.getIsContainedIn().iterator().next().getName() : null;
        String subsetIri = member.getIsContainedIn() != null ? member.getIsContainedIn().iterator().next().getIri() : null;
        String usage = member.getUsage() != null ? member.getUsage().toString() : null;
        String code= member.getAlternativeCode()==null ?member.getCode(): member.getAlternativeCode();
        if(ownRow) {
            if(includeSubset && subSet != null) {
                addLineData(del, results, code, member.getName(), scheme, usage, setName, subSet, subsetIri, isExtension);
            } else {
                addLineData(del, results, code, member.getName(), scheme, usage, setName, isExtension);
            }
        }
        for (Concept legacy: member.getMatchedFrom()) {
            String legacyUsage = legacy.getUsage() != null ? legacy.getUsage().toString() : null;
            if(ownRow) {
                if(im1id && legacy.getIm1Id() != null) {
                    legacy.getIm1Id().forEach(im1 -> {
                        addLineData(del, results, legacy.getCode(), legacy.getName(), legacy.getScheme().getIri(), legacyUsage, legacy.getCodeId(),im1);
                    });
                } else {
                    addLineData(del, results, legacy.getCode(), legacy.getName(), legacy.getScheme().getIri(), legacyUsage,legacy.getCodeId());
                }
            } else {
                if(im1id && legacy.getIm1Id() != null) {
                    legacy.getIm1Id().forEach(im1 -> {
                        if(includeSubset && subSet != null) {
                            addLineData(del, results, code, member.getName(), scheme, usage, setName, subSet, subsetIri, isExtension,
                                    legacy.getCode(), legacy.getName(), legacy.getScheme().getIri(), legacyUsage, legacy.getCodeId(),im1);
                        } else {
                            addLineData(del, results, code, member.getName(), scheme, usage, setName, isExtension,
                                    legacy.getCode(), legacy.getName(), legacy.getScheme().getIri(), legacyUsage, legacy.getCodeId(),im1);
                        }
                    });
                } else {
                    if(includeSubset && subSet != null) {
                        addLineData(del, results, code, member.getName(), scheme, usage, setName, subSet, subsetIri, isExtension,
                                legacy.getCode(), legacy.getName(), legacy.getScheme().getIri(), legacyUsage,legacy.getCodeId());
                    } else {
                        addLineData(del, results, code, member.getName(), scheme, usage, setName, isExtension,
                                legacy.getCode(), legacy.getName(), legacy.getScheme().getIri(), legacyUsage,legacy.getCodeId());
                    }
                }
            }
        }
    }

    private void addOnlyCore(boolean im1id, StringJoiner results, Concept member, String setName, String isExtension, String del, boolean includeSubset) {
        String scheme = member.getScheme().getName();
        String usage = member.getUsage() != null ? member.getUsage().toString() : null;
        String subSet = member.getIsContainedIn() != null ? member.getIsContainedIn().iterator().next().getName() : null;
        String subsetIri = member.getIsContainedIn() != null ? member.getIsContainedIn().iterator().next().getIri() : null;

        if(im1id && member.getIm1Id() != null) {
            member.getIm1Id().forEach(im1 -> {
                if(includeSubset && subSet != null) {
                    addLineData(del, results, member.getCode(), member.getName(), scheme, usage, setName, subSet, subsetIri, isExtension, im1);
                } else {
                    addLineData(del, results, member.getCode(), member.getName(), scheme, usage, setName, isExtension, im1);
                }
            });
        } else {
            if(includeSubset && subSet != null) {
                addLineData(del, results, member.getCode(), member.getName(), scheme, usage, setName, subSet, subsetIri, isExtension);
            } else {
                addLineData(del, results, member.getCode(), member.getName(), scheme, usage, setName, isExtension);
            }
        }

    }

    private void addLineData(String del, StringJoiner results, Object... values) {
        StringJoiner line = new StringJoiner(del);
        for (Object value : values) {
            if(",".equals(del)) {
                line.add("\"" + value + "\"");
            } else  {
                line.add((CharSequence) value);
            }
        }
        results.add(line.toString());
    }

    private static String getLegacyHeader(String del) {
        return "legacy code"+ del +"legacy term"+ del +"legacy scheme"+ del +"legacy usage"+ del+ "code id";
    }

    private static String getCoreHeader(String del) {
        return "code"+ del +"term"+ del + "scheme"+ del + "usage" + del +"set"+ del +"extension";
    }
    private static String getCoreHeaderWithSubset(String del) {
        return "code"+ del +"term"+ del + "scheme"+ del + "usage" + del +"set"+ del +"subset"+ del +"subsetIri"+ del +"extension";
    }


}
