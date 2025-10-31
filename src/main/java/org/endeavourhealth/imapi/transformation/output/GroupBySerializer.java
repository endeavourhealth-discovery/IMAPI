package org.endeavourhealth.imapi.transformation.output;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.endeavourhealth.imapi.model.imq.GroupBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Custom Jackson serializer for GroupBy objects.
 * 
 * Handles serialization of GroupBy objects to JSON with the following features:
 * - Serializes grouping references (nodeRef, valueRef, propertyRef)
 * - Filters out default/null values
 * - Properly handles inherited IriLD properties (iri, name, description)
 * 
 * Example JSON output:
 * {
 *   "nodeRef": "node-id",
 *   "valueRef": "value-id",
 *   "iri": "groupby-iri",
 *   "name": "Group By Name"
 * }
 * 
 * @see QuerySerializer
 * @see MatchSerializer
 */
public class GroupBySerializer extends JsonSerializer<GroupBy> {
    
    private static final Logger logger = LoggerFactory.getLogger(GroupBySerializer.class);
    
    /**
     * Serializes a GroupBy object to JSON.
     * 
     * The serialization leverages Jackson's default serialization mechanism combined with
     * @JsonInclude annotations to ensure:
     * - Default/null values are omitted
     * - Nested references are properly serialized
     * 
     * @param groupBy The GroupBy object to serialize
     * @param gen The JsonGenerator to write to
     * @param provider The SerializerProvider for accessing other serializers
     * @throws IOException If an I/O error occurs during serialization
     */
    @Override
    public void serialize(GroupBy groupBy, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (groupBy == null) {
            gen.writeNull();
            return;
        }
        
        logger.debug("Serializing GroupBy: nodeRef={}, valueRef={}", 
            groupBy.getNodeRef() != null ? groupBy.getNodeRef() : "unnamed",
            groupBy.getValueRef() != null ? groupBy.getValueRef() : "unnamed");
        
        // Use default serialization which respects @JsonInclude annotations
        provider.defaultSerializeValue(groupBy, gen);
    }
    
    /**
     * Returns the type handled by this serializer.
     * 
     * @return GroupBy class
     */
    @Override
    public Class<GroupBy> handledType() {
        return GroupBy.class;
    }
}