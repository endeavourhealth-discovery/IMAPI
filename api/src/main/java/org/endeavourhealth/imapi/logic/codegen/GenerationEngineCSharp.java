package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.vocabulary.XSD;

import java.util.Map;

public class GenerationEngineCSharp extends GenerationEngine {
    private static final String LIST = "List<${BASE_DATA_TYPE}>";

    private static final String HEADER = """
    namespace ${NAMESPACE}
    {
        public class ${MODEL_NAME}
        {
    """;

    private static final String PRIVATE_PROPERTY = """
            public ${DATA_TYPE} ${PROPERTY_NAME} { get; set; }
    """;

    private static final String PUBLIC_PROPERTY = "";

    private static final String PUBLIC_ARRAY_ADDER = "";

    private static final String FOOTER = """
        }
    }
    """;

    private static final Map<String, String> TYPE_MAP = Map.of(
        XSD.STRING, "string",
        "http://endhealth.info/im#DateTime", "PartialDateTime"
    );

    public GenerationEngineCSharp() {
        super(
            HEADER, PRIVATE_PROPERTY, PUBLIC_PROPERTY, PUBLIC_ARRAY_ADDER, LIST, FOOTER, TYPE_MAP, "String", ".cs"
        );
    }
}
