package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.vocabulary.XSD;

import java.util.Map;

public class GenerationEngineTypeScript extends GenerationEngine {
    private static final String LIST = "${BASE_DATA_TYPE}[]";

    private static final String HEADER = """
    export default class ${MODEL_NAME} {
    """;

    private static final String PRIVATE_PROPERTY = """
        public ${property_name}: ${DATA_TYPE};
    """;

    private static final String PUBLIC_PROPERTY = "";

    private static final String PUBLIC_ARRAY_ADDER = "";

    private static final String FOOTER = """
    }
    """;

    private static final Map<String, String> TYPE_MAP = Map.of(
        XSD.STRING, "string",
        "http://endhealth.info/im#DateTime", "PartialDateTime"
    );

    public GenerationEngineTypeScript() {
        super(
            HEADER, PRIVATE_PROPERTY, PUBLIC_PROPERTY, PUBLIC_ARRAY_ADDER, LIST, FOOTER, TYPE_MAP, "any", ".ts"
        );
    }
}
