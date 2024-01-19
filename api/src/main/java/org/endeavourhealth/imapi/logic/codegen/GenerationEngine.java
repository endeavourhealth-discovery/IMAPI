package org.endeavourhealth.imapi.logic.codegen;

import java.util.Map;
import java.util.StringJoiner;

public class GenerationEngine {
    private String HEADER;
    private String PRIVATE_PROPERTY;
    private String PUBLIC_PROPERTY;
    private String PUBLIC_ARRAY_ADDER;
    private String LIST_WRAPPER;
    private String FOOTER;
    private String DEFAULT_TYPE;
    private String FILE_EXTENSION;
    private Map<String, String> TYPE_MAP;

    public GenerationEngine(String header, String privateProperty, String publicProperty, String publicArrayAdder, String listWrapper, String footer, Map<String, String> typeMap, String defaultType, String fileExtension) {
        HEADER = header;
        PRIVATE_PROPERTY = privateProperty;
        PUBLIC_PROPERTY = publicProperty;
        PUBLIC_ARRAY_ADDER =publicArrayAdder;
        LIST_WRAPPER = listWrapper;
        FOOTER = footer;
        TYPE_MAP = typeMap;
        DEFAULT_TYPE = defaultType;
        FILE_EXTENSION = fileExtension;
    }

    public String getFilename(DataModel model) {
        return capitalise(model.getName()) + FILE_EXTENSION;
    }

    public String generateCodeForModel(String namespace, DataModel model) {
        StringBuilder os = new StringBuilder();

        os.append(replaceClassTokens(HEADER, namespace, model));

        for (DataModelProperty property : model.getProperties()) {
            os.append(replacePropertyTokens(PRIVATE_PROPERTY, model, property));
        }

        for (DataModelProperty property : model.getProperties()) {
            os.append(replacePropertyTokens(PUBLIC_PROPERTY, model, property));

            boolean isArray = property.getMaxCount() != null && property.getMaxCount() == 0;
            if (isArray) {
                os.append(replacePropertyTokens(PUBLIC_ARRAY_ADDER, model, property));
            }
        }

        os.append(FOOTER);
        return os.toString();
    }

    String replaceClassTokens(String template, String namespace, DataModel model) {
        return template
            .replace("${NAMESPACE}", namespace)
            .replace("${MODEL_NAME}", model.getName())
            .replace("${MODEL_COMMENT}", model.getComment());
    }

    String replacePropertyTokens(String template, DataModel model, DataModelProperty property) {
        boolean isArray = property.getMaxCount() != null && property.getMaxCount() == 0;
        String basePropertyType = getDataType(property.getDataType().getIri());
        String propertyType = isArray ? LIST_WRAPPER.replace("${BASE_DATA_TYPE}", basePropertyType) : basePropertyType;

        return template
            .replace("${MODEL_NAME}", model.getName())
            .replace("${MODEL_COMMENT}", model.getComment())
            .replace("${BASE_DATA_TYPE}", basePropertyType)
            .replace("${DATA_TYPE}", propertyType)
            .replace("${property_name}", property.getName().toLowerCase())
            .replace("${PROPERTY_NAME}", property.getName());
    }

    String capitalise(String name) {
        if (null == name) {
            return null;
        }
        //name as name
        StringBuilder output = new StringBuilder();

        String[] words = name.toLowerCase().replace("\"", "")
            .replaceAll("[^\\p{L}]", " ").split("\\s+");

        for (String word : words) {
            String camelCase = Character.toUpperCase(word.charAt(0)) + word.substring(1);
            output.append(camelCase);
        }
        return output.toString();
    }

    private String getDataType(String iri) {
        String result = TYPE_MAP.get(iri);

        if (result == null)
            System.out.println("Unknown type [" + iri + "], defaulting to [" + DEFAULT_TYPE + "]");

        return null == result ? DEFAULT_TYPE : result;
    }

    String getSuffix(String iri) {
        return iri.substring(iri.lastIndexOf("#") + 1);
    }
}
