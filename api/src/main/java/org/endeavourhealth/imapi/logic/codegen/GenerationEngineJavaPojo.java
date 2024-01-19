package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.vocabulary.XSD;

import java.util.Map;

public class GenerationEngineJavaPojo extends GenerationEngine {
    private static final String LIST = "List<${BASE_DATA_TYPE}>";

    private static final String HEADER = """
    package ${NAMESPACE};
    
    import java.util.ArrayList;
    import java.util.List;
    import java.util.UUID;
    
    /**
    * Represents ${MODEL_NAME}
    * ${MODEL_COMMENT}
    */
    
    public class ${MODEL_NAME} {
    """;

    private static final String PRIVATE_PROPERTY = """
        private ${DATA_TYPE} ${property_name};    
    """;

    private static final String PUBLIC_PROPERTY = """
        /**
        * Gets the ${property_name} of this ${MODEL_NAME}
        * @return ${property_name}
        */
        public ${DATA_TYPE} get${PROPERTY_NAME}() {
            return ${property_name};
        }
        /**
        * Sets the ${property_name} of this ${MODEL_NAME}
        * @param ${property_name} The new ${property_name} to set
        * @return ${MODEL_NAME}
        */
        public ${MODEL_NAME} set${PROPERTY_NAME}(${DATA_TYPE} value) {
            ${property_name} = value;
            return this;
        }
        
    """;

    private static final String PUBLIC_ARRAY_ADDER = """
        /**
        * Adds the given ${property_name} to this %{MODEL_NAME}
        * @param ${property_name} The ${property_name} to add
        * @return ${MODEL_NAME}
        */
        public ${MODEL_NAME} add${PROPERTY_NAME}(${BASE_DATA_TYPE} value) {
            ${DATA_TYPE} array = this.get${PROPERTY_NAME}();
        
            if (null == array) {
                array = new ArrayList();
                this.set${PROPERTY_NAME}(array);
            }
            
            array.add(value);
            return this;
        }
    """;

    private static final String FOOTER = """
    }
    """;

    private static final Map<String, String> TYPE_MAP = Map.of(
        XSD.STRING, "String",
        "http://endhealth.info/im#DateTime", "PartialDateTime"
    );

    public GenerationEngineJavaPojo() {
        super(
            HEADER, PRIVATE_PROPERTY, PUBLIC_PROPERTY, PUBLIC_ARRAY_ADDER, LIST, FOOTER, TYPE_MAP, "String", ".java"
        );
    }
}
