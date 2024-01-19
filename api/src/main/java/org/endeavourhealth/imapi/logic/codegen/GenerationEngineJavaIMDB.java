package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.vocabulary.XSD;

import java.util.HashMap;
import java.util.Map;

public class GenerationEngineJavaIMDB extends GenerationEngine {
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
    
    public class ${MODEL_NAME} extends IMDMBase<${MODEL_NAME}> {
        /**
        * ${MODEL_NAME} constructor with identifier
        */
        public ${MODEL_NAME}(UUID id) {
            super("${MODEL_NAME}", id);
        }
        
    """;

    private static final String PRIVATE_PROPERTY = "";

    private static final String PUBLIC_PROPERTY = """
        /**
        * Gets the ${property_name} of this ${MODEL_NAME}
        * @return ${property_name}
        */
        public ${DATA_TYPE} get${PROPERTY_NAME}() {
            return getProperty("${property_name}");
        }
        /**
        * Sets the ${property_name} of this ${MODEL_NAME}
        * @param ${property_name} The new ${property_name} to set
        * @return ${MODEL_NAME}
        */
        public ${MODEL_NAME} set${PROPERTY_NAME}(${DATA_TYPE} ${property_name}) {
            setProperty("${property_name}", ${property_name});
            return this;
        }
        
    """;

    private static final String PUBLIC_ARRAY_ADDER = """
        /**
        * Adds the given ${property_name} to this %{MODEL_NAME}
        * @param ${property_name} The ${property_name} to add
        * @return ${MODEL_NAME}
        */
        public ${MODEL_NAME} add${PROPERTY_NAME}(${BASE_DATA_TYPE} ${property_name}) {
            ${DATA_TYPE} array = this.get${PROPERTY_NAME}();
        
            if (null == array) {
                array = new ArrayList();
                this.set${PROPERTY_NAME}(array);
            }
            
            array.add(${property_name});
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

    public GenerationEngineJavaIMDB() {
        super(
            HEADER, PRIVATE_PROPERTY, PUBLIC_PROPERTY, PUBLIC_ARRAY_ADDER, LIST, FOOTER, TYPE_MAP, "String", ".java"
        );
    }
}
