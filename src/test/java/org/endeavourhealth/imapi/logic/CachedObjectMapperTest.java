package org.endeavourhealth.imapi.logic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

class CachedObjectMapperTest {

    @Test
    void shouldReadAndWriteValue() throws JsonProcessingException {
        try (CachedObjectMapper mapper = new CachedObjectMapper()) {
            TestObject obj = new TestObject("test", 123);
            String json = mapper.writeValueAsString(obj);
            assertThat(json).contains("\"name\":\"test\"").contains("\"value\":123");

            TestObject read = mapper.readValue(json, TestObject.class);
            assertThat(read.getName()).isEqualTo("test");
            assertThat(read.getValue()).isEqualTo(123);
        }
    }

    @Test
    void shouldReadValueWithTypeReference() throws JsonProcessingException {
        try (CachedObjectMapper mapper = new CachedObjectMapper()) {
            String json = "{\"key\":\"value\"}";
            Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>() {});
            assertThat(map).containsEntry("key", "value");
        }
    }

    @Test
    void shouldReadTree() throws JsonProcessingException {
        try (CachedObjectMapper mapper = new CachedObjectMapper()) {
            String json = "{\"name\":\"test\"}";
            JsonNode node = mapper.readTree(json);
            assertThat(node.get("name").asText()).isEqualTo("test");
        }
    }

    @Test
    void shouldCreateNodes() {
        try (CachedObjectMapper mapper = new CachedObjectMapper()) {
            ObjectNode obj = mapper.createObjectNode();
            assertThat(obj).isNotNull();

            ArrayNode arr = mapper.createArrayNode();
            assertThat(arr).isNotNull();
        }
    }

    @Test
    void shouldConvertValueToTree() {
        try (CachedObjectMapper mapper = new CachedObjectMapper()) {
            List<TTIriRef> iris = Arrays.asList(iri("iri1"), iri("iri2"));
            JsonNode node = mapper.valueToTree(iris);
            assertThat(node.isArray()).isTrue();
            assertThat(node.size()).isEqualTo(2);
        }
    }

    @Test
    void shouldConvertStringArrayToTree() {
        try (CachedObjectMapper mapper = new CachedObjectMapper()) {
            List<String> strings = Arrays.asList("a", "b");
            JsonNode node = mapper.stringArrayToTree(strings);
            assertThat(node.isArray()).isTrue();
            assertThat(node.size()).isEqualTo(2);
        }
    }

    @Test
    void shouldConvertTreeToValue() throws JsonProcessingException {
        try (CachedObjectMapper mapper = new CachedObjectMapper()) {
            ObjectNode node = mapper.createObjectNode();
            node.put("name", "test");
            node.put("value", 123);

            TestObject obj = mapper.treeToValue(node, TestObject.class);
            assertThat(obj.getName()).isEqualTo("test");
            assertThat(obj.getValue()).isEqualTo(123);
        }
    }

    @Test
    void shouldReadValueFromFile(@TempDir Path tempDir) throws IOException {
        File file = tempDir.resolve("test.json").toFile();
        Files.write(file.toPath(), "{\"name\":\"test\",\"value\":123}".getBytes());

        try (CachedObjectMapper mapper = new CachedObjectMapper()) {
            TestObject obj = mapper.readValue(file, TestObject.class);
            assertThat(obj.getName()).isEqualTo("test");
            assertThat(obj.getValue()).isEqualTo(123);
        }
    }

    @Test
    void shouldSetSerializationInclusionAlways() throws JsonProcessingException {
        try (CachedObjectMapper mapper = new CachedObjectMapper()) {
            mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
            TestObjectAlways obj = new TestObjectAlways();
            String json = mapper.writeValueAsString(obj);
            assertThat(json).contains("\"name\":null");
        }
    }

    @Test
    void shouldSetSerializationInclusionNonEmpty() throws JsonProcessingException {
        try (CachedObjectMapper mapper = new CachedObjectMapper()) {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            TestObjectNonEmpty obj = new TestObjectNonEmpty();
            String json = mapper.writeValueAsString(obj);
            assertThat(json).doesNotContain("\"name\"");
        }
    }

    public static class TestObjectAlways {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class TestObjectNonEmpty {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @Test
    void shouldProvideWriterWithDefaultPrettyPrinter() {
        try (CachedObjectMapper mapper = new CachedObjectMapper()) {
            assertThat(mapper.writerWithDefaultPrettyPrinter()).isNotNull();
        }
    }

    public static class TestObject {
        private String name;
        private int value;

        public TestObject() {}

        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getValue() { return value; }
        public void setValue(int value) { this.value = value; }
    }
}
