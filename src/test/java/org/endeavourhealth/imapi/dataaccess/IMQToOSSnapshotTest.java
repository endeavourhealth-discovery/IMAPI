package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.dataaccess.opensearch.Fuzziness;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.TextSearchStyle;
import org.endeavourhealth.imapi.model.imq.Where;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.utility.SharedObjectMapper;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.NAMESPACE;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Characterisation test for the OpenSearch queries built by {@link IMQToOS}.
 * <p>
 * Each case builds a query and compares it, after normalisation, with a stored golden file in
 * src/test/resources/opensearch-golden. If a golden file does not exist yet it is written and the test fails once so
 * the new file is reviewed and committed. To regenerate deliberately, delete the golden files and run this test.
 * <p>
 * Normalisation sorts keys and arrays of plain values (they come from hash sets) and drops settings that are only
 * the library's defaults, so the comparison is about what is queried and not how a client library spells it.
 */
class IMQToOSSnapshotTest {
  private static final Path GOLDEN_DIR = Path.of("src/test/resources/opensearch-golden");

  private static QueryRequest request(String text, Query query) {
    return new QueryRequest().setTextSearch(text).setQuery(query);
  }

  private static Map<String, Supplier<String>> cases() {
    Map<String, Supplier<String>> cases = new LinkedHashMap<>();
    cases.put("ngram_plain", () -> build(request("heart attack", null), null, TextSearchStyle.ngram, Fuzziness.ZERO));
    cases.put("ngram_fuzzy", () -> build(request("heart atack", null), null, TextSearchStyle.ngram, Fuzziness.TWO));
    cases.put("multiword", () -> build(request("myocardial infarction (acute)", null), null, TextSearchStyle.multiword, Fuzziness.ZERO));
    cases.put("autocomplete", () -> build(request("heart att", null), null, TextSearchStyle.autocomplete, Fuzziness.ZERO));
    cases.put("autocomplete_prefixed", () -> build(request("sn:22298006", null), null, TextSearchStyle.autocomplete, Fuzziness.ZERO));
    cases.put("exact", () -> build(request("FOXG1", null), null, TextSearchStyle.exact, Fuzziness.ZERO));

    Query scheme = new Query().setWhere(new Where().setIri(IM.HAS_SCHEME).setIs(List.of(new Node().setIri(NAMESPACE.SNOMED.toString()))));
    cases.put("ngram_scheme", () -> build(request("FOXG1", scheme), scheme, TextSearchStyle.ngram, Fuzziness.ZERO));

    Query isA = new Query().setIs(new Node().setIri("http://snomed.info/sct#57148006").setDescendantsOrSelfOf(true));
    cases.put("ngram_isa", () -> build(request("FOXG1", isA), isA, TextSearchStyle.ngram, Fuzziness.ZERO));

    Query member = new Query().setWhere(new Where().setIri(IM.HAS_MEMBER).setInverse(true).setIs(List.of(new Node().setIri("http://endhealth.info/im#VSET_ASD"))));
    cases.put("ngram_member", () -> build(request("FOXG1", member), member, TextSearchStyle.ngram, Fuzziness.ZERO));

    Query activeType = new Query().setActiveOnly(true).setTypeOf("http://endhealth.info/im#Concept");
    cases.put("ngram_active_typeof", () -> build(request("FOXG1", activeType), activeType, TextSearchStyle.ngram, Fuzziness.ZERO));
    cases.put("exact_active_typeof", () -> build(request("FOXG1", activeType), activeType, TextSearchStyle.exact, Fuzziness.ZERO));

    cases.put("ngram_page", () -> build(new QueryRequest().setTextSearch("heart").setPage(new Page().setPageNumber(3).setPageSize(20)), null, TextSearchStyle.ngram, Fuzziness.ZERO));
    cases.put("ngram_offset", () -> build(new QueryRequest().setTextSearch("heart").setPage(new Page().setOffset(50).setPageSize(10)), null, TextSearchStyle.ngram, Fuzziness.ZERO));
    return cases;
  }

  /**
   * The only place that touches the query-builder library, so a change of library only changes this method.
   */
  private static String build(QueryRequest request, Query query, TextSearchStyle style, Fuzziness fuzziness) {
    try {
      return new IMQToOS().buildQuery(request, query, style, fuzziness).toString();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Test
  void queriesMatchTheGoldenFiles() throws Exception {
    List<String> created = new ArrayList<>();
    List<String> differences = new ArrayList<>();
    Files.createDirectories(GOLDEN_DIR);

    for (Map.Entry<String, Supplier<String>> entry : cases().entrySet()) {
      JsonNode actual = normalise(SharedObjectMapper.INSTANCE.readTree(entry.getValue().get()));
      Path golden = GOLDEN_DIR.resolve(entry.getKey() + ".json");
      if (!Files.exists(golden)) {
        Files.writeString(golden, SharedObjectMapper.INSTANCE.writerWithDefaultPrettyPrinter().writeValueAsString(actual) + "\n");
        created.add(entry.getKey());
      } else {
        JsonNode expected = SharedObjectMapper.INSTANCE.readTree(Files.readString(golden));
        if (!expected.equals(actual)) differences.add(entry.getKey() + "\n  expected: " + expected + "\n  actual:   " + actual);
      }
    }

    if (!created.isEmpty()) fail("Created golden files for " + created + ". Review, commit them and run the test again.");
    assertThat(differences).as("queries that differ from their golden files").isEmpty();
  }

  // ---- normalisation ----

  private static JsonNode normalise(JsonNode node) {
    if (node.isObject()) {
      Map<String, JsonNode> sorted = new TreeMap<>();
      Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
      while (fields.hasNext()) {
        Map.Entry<String, JsonNode> field = fields.next();
        JsonNode value = field.getValue();
        if ("inner_hits".equals(field.getKey())) {
          // only which fields are returned matters; the rest is the library's paging and tracking defaults
          ObjectNode kept = JsonNodeFactory.instance.objectNode();
          if (value.has("_source")) kept.set("_source", value.get("_source"));
          value = kept;
        }
        value = normalise(value);
        if (!isLibraryDefault(field.getKey(), value)) sorted.put(field.getKey(), value);
      }
      ObjectNode result = JsonNodeFactory.instance.objectNode();
      sorted.forEach(result::set);
      return result;
    }
    if (node.isArray()) {
      List<JsonNode> items = new ArrayList<>();
      node.forEach(item -> items.add(normalise(item)));
      if (items.stream().allMatch(JsonNode::isValueNode)) items.sort((a, b) -> a.asText().compareTo(b.asText()));
      ArrayNode result = JsonNodeFactory.instance.arrayNode();
      items.forEach(result::add);
      return result;
    }
    if (node.isNumber()) return JsonNodeFactory.instance.numberNode(node.asDouble());
    return node;
  }

  /**
   * Settings that every query carries with the same value unless set otherwise; they say nothing about what is queried.
   */
  private static boolean isLibraryDefault(String key, JsonNode value) {
    return switch (key) {
      case "boost" -> value.isNumber() && value.asDouble() == 1.0;
      case "adjust_pure_negative", "fuzzy_transpositions", "auto_generate_synonyms_phrase_query" -> value.isBoolean() && value.asBoolean();
      case "lenient", "ignore_unmapped" -> value.isBoolean() && !value.asBoolean();
      case "prefix_length" -> value.isNumber() && value.asInt() == 0;
      case "max_expansions" -> value.isNumber() && value.asInt() == 50;
      case "zero_terms_query" -> "NONE".equals(value.asText());
      case "excludes" -> value.isArray() && value.isEmpty();
      case "lang" -> "painless".equals(value.asText());
      case "inner_hits" -> value.isEmpty();
      default -> false;
    };
  }
}
