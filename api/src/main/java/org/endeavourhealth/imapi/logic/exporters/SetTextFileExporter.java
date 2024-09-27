package org.endeavourhealth.imapi.logic.exporters;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.exporters.SetExporterOptions;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.IMQToECL;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.logic.exporters.helpers.ExporterHelpers.*;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class SetTextFileExporter {
  public static final String IM_1_ID = "im1id";
  private static final Logger LOG = LoggerFactory.getLogger(SetTextFileExporter.class);
  private final SetExporter setExporter = new SetExporter();
  private final EntityRepository entityRepository = new EntityRepository();

  private static void addHeader(SetExporterOptions options, StringJoiner results, String del) {
    if (options.includeSubsets()) {
      addSubsetHeader(options, results, del);
    } else {
      if (options.includeLegacy()) {
        if (options.isOwnRow()) {
          results.add(getCoreHeader(del));
        } else {
          if (options.isIm1id()) {
            results.add(getCoreHeader(del) + del + getLegacyHeader(del) + del + IM_1_ID);
          } else {
            results.add(getCoreHeader(del) + del + getLegacyHeader(del));
          }
        }
      } else {
        if (options.isIm1id()) {
          results.add(getCoreHeader(del) + del + IM_1_ID);
        } else {
          results.add(getCoreHeader(del));
        }
      }
    }

  }

  private static void addSubsetHeader(SetExporterOptions options, StringJoiner results, String del) {
    if (options.includeLegacy()) {
      if (options.isOwnRow()) {
        results.add(getCoreHeaderWithSubset(del));
      } else {
        if (options.isIm1id()) {
          results.add(getCoreHeaderWithSubset(del) + del + getLegacyHeader(del) + del + IM_1_ID);
        } else {
          results.add(getCoreHeaderWithSubset(del) + del + getLegacyHeader(del));
        }
      }
    } else {
      if (options.isIm1id()) {
        results.add(getCoreHeaderWithSubset(del) + del + IM_1_ID);
      } else {
        results.add(getCoreHeaderWithSubset(del));
      }
    }
  }

  private static String getLegacyHeader(String del) {
    return "legacy code" + del + "legacy term" + del + "legacy scheme" + del + "legacy usage" + del + "code id";
  }

  private static String getCoreHeader(String del) {
    return "code" + del + "term" + del + "status" + del + "scheme" + del + "usage" + del + "set" + del + "extension";
  }

  private static String getCoreHeaderWithSubset(String del) {
    return "code" + del + "term" + del + "status" + del + "scheme" + del + "usage" + del + "set" + del + "subset" + del + "subsetIri" + del + "subsetVersion" + del + "extension";
  }

  public String getSetFile(SetExporterOptions options, String del) throws QueryException, JsonProcessingException {
    LOG.debug("Exporting set to TSV");
    String setName = entityRepository.getBundle(options.getSetIri(), Set.of(RDFS.LABEL)).getEntity().getName();

    String result = "";

    TTEntity entity = entityRepository.getEntityPredicates(options.getSetIri(), Set.of(IM.DEFINITION)).getEntity();
    if (entity.getIri() == null || entity.getIri().isEmpty())
      return null;

    String ecl = getEcl(entity);
    if (ecl != null && options.includeDefinition()) {
      result = ecl;
    }

    if (options.includeCore() || options.includeLegacy()) {
      Set<Concept> members = setExporter.getExpandedSetMembers(options.getSetIri(), options.includeCore(), options.includeLegacy(), options.includeSubsets(), options.getSchemes())
        .stream().sorted(Comparator.comparing(Concept::getName)).collect(Collectors.toCollection(LinkedHashSet::new));

      if (options.includeSubsets()) {
        members = members.stream().sorted(Comparator.comparing(m -> (null == m.getIsContainedIn() || m.getIsContainedIn().isEmpty()) ? "" : m.getIsContainedIn().iterator().next().getName()))
          .collect(Collectors.toCollection(LinkedHashSet::new));
      }
      result = generateFile(setName, members, options, del).toString();
    }
    return result;
  }

  private String getEcl(TTEntity entity) throws QueryException, JsonProcessingException {
    if (entity.get(iri(IM.DEFINITION)) == null)
      return null;
    return new IMQToECL().getECLFromQuery(entity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class), true);
  }

  private StringJoiner generateFile(String setName, Set<Concept> members, SetExporterOptions options, String del) {

    LOG.trace("Generating output...");

    StringJoiner results = new StringJoiner(System.lineSeparator());
    addHeader(options, results, del);

    for (Concept member : members) {
      String isExtension = member.getScheme().getIri().contains("sct#") ? "N" : "Y";
      if (member.getMatchedFrom() != null && options.includeLegacy()) {
        addLegacy(options, results, member, setName, isExtension, del);
      } else {
        addOnlyCore(options, results, member, setName, isExtension, del);
      }
      if (options.includeLegacy() && options.isOwnRow()) {
        results.add("");
      }
    }
    return results;
  }

  private void addLegacy(SetExporterOptions options, StringJoiner results, Concept member, String setName, String isExtension, String del) {
    String scheme = member.getScheme().getName();
    String subSet = getSubSet(member);
    String subsetIri = getSubsetIri(member);
    String subsetVersion = setSubsetVersion(member);
    String status = member.getStatus().getName();

    String usage = getUsage(member);
    String code = getCode(member);
    if (options.isOwnRow()) {
      if (options.includeSubsets() && subSet != null) {
        addLineData(del, results, code, member.getName(), status, scheme, usage, setName, subSet, subsetIri, subsetVersion, isExtension);
      } else {
        addLineData(del, results, code, member.getName(), status, scheme, usage, setName, isExtension);
      }
    }
    for (Concept legacy : member.getMatchedFrom()) {
      String legacyUsage = legacy.getUsage() != null ? legacy.getUsage().toString() : null;
      if (options.isOwnRow()) {
        if (options.isIm1id() && legacy.getIm1Id() != null) {
          legacy.getIm1Id().forEach(im1 -> addLineData(del, results, legacy.getCode(), legacy.getName(), legacy.getScheme().getIri(), legacyUsage, legacy.getCodeId(), im1));
        } else {
          addLineData(del, results, legacy.getCode(), legacy.getName(), legacy.getScheme().getIri(), legacyUsage, legacy.getCodeId());
        }
      } else {
        if (options.isIm1id() && legacy.getIm1Id() != null) {
          legacy.getIm1Id().forEach(im1 -> {
            if (options.includeSubsets() && subSet != null) {
              addLineData(del, results, code, member.getName(), status, scheme, usage, setName, subSet, subsetIri, subsetVersion, isExtension,
                legacy.getCode(), legacy.getName(), legacy.getScheme().getIri(), legacyUsage, legacy.getCodeId(), im1);
            } else {
              addLineData(del, results, code, member.getName(), status, scheme, usage, setName, isExtension,
                legacy.getCode(), legacy.getName(), legacy.getScheme().getIri(), legacyUsage, legacy.getCodeId(), im1);
            }
          });
        } else {
          if (options.includeSubsets() && subSet != null) {
            addLineData(del, results, code, member.getName(), status, scheme, usage, setName, subSet, subsetIri, subsetVersion, isExtension,
              legacy.getCode(), legacy.getName(), legacy.getScheme().getIri(), legacyUsage, legacy.getCodeId());
          } else {
            addLineData(del, results, code, member.getName(), status, scheme, usage, setName, isExtension,
              legacy.getCode(), legacy.getName(), legacy.getScheme().getIri(), legacyUsage, legacy.getCodeId());
          }
        }
      }
    }
  }

  private void addOnlyCore(SetExporterOptions options, StringJoiner results, Concept member, String setName, String isExtension, String del) {
    String scheme = member.getScheme().getName();
    String usage = getUsage(member);
    String subSet = getSubSet(member);
    String subsetIri = getSubsetIri(member);
    String subsetVersion = setSubsetVersion(member);
    String status = getStatus(member);
    if (options.isIm1id() && member.getIm1Id() != null) {
      member.getIm1Id().forEach(im1 -> {
        if (options.includeSubsets() && subSet != null) {
          addLineData(del, results, member.getCode(), member.getName(), status, scheme, usage, setName, subSet, subsetIri, subsetVersion, isExtension, im1);
        } else {
          addLineData(del, results, member.getCode(), member.getName(), status, scheme, usage, setName, isExtension, im1);
        }
      });
    } else {
      if (options.includeSubsets() && subSet != null) {
        addLineData(del, results, member.getCode(), member.getName(), status, scheme, usage, setName, subSet, subsetIri, subsetVersion, isExtension);
      } else {
        addLineData(del, results, member.getCode(), member.getName(), status, scheme, usage, setName, isExtension);
      }
    }

  }

  private void addLineData(String del, StringJoiner results, Object... values) {
    StringJoiner line = new StringJoiner(del);
    for (Object value : values) {
      if (",".equals(del)) {
        line.add("\"" + value + "\"");
      } else {
        line.add((CharSequence) value);
      }
    }
    results.add(line.toString());
  }


}
