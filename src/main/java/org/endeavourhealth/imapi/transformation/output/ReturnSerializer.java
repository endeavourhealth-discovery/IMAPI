package org.endeavourhealth.imapi.transformation.output;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.endeavourhealth.imapi.model.imq.Return;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Custom Jackson serializer for Return objects.
 * 
 * Handles serialization of Return objects to JSON with the following features:
 * - Enforces property ordering: nodeRef, function, property, groupBy, as
 * - Filters out default/null values using @JsonInclude(Include.NON_DEFAULT)
 * - Properly serializes ReturnProperty objects within Return clauses
 * - Handles FunctionClause serialization for aggregate functions
 * 
 * Example JSON output:
 * {
 *   "nodeRef": "node-id",
 *   "function": {
 *     "type": "count"
 *   },
 *   "as": "alias"
 * }
 * 
 * @see QuerySerializer
 * @see ReturnPropertySerializer
 */
public class ReturnSerializer extends JsonSerializer<Return> {
    
    private static final Logger logger = LoggerFactory.getLogger(ReturnSerializer.class);
    
    /**
     * Serializes a Return object to JSON.
     * 
     * The serialization leverages Jackson's default serialization mechanism combined with
     * @JsonPropertyOrder and @JsonInclude annotations on the Return class to ensure:
     * - Properties are serialized in the specified order
     * - Default/null values are omitted
     * - Nested ReturnProperty objects are properly serialized
     * 
     * @param returnn The Return object to serialize
     * @param gen The JsonGenerator to write to
     * @param provider The SerializerProvider for accessing other serializers
     * @throws IOException If an I/O error occurs during serialization
     */
    @Override
    public void serialize(Return returnn, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (returnn == null) {
            gen.writeNull();
            return;
        }
        
        logger.debug("Serializing Return: nodeRef={}", returnn.getNodeRef() != null ? returnn.getNodeRef() : "unnamed");
        
        // Use default serialization which respects @JsonPropertyOrder and @JsonInclude annotations
        provider.defaultSerializeValue(returnn, gen);
    }
    
    /**
     * Returns the type handled by this serializer.
     * 
     * @return Return class
     */
    @Override
    public Class<Return> handledType() {
        return Return.class;
    }
}