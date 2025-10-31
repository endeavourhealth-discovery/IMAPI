package org.endeavourhealth.imapi.transformation.output;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.endeavourhealth.imapi.model.imq.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Custom Jackson serializer for Match objects.
 * 
 * Handles serialization of Match objects to JSON with the following features:
 * - Enforces property ordering: ifTrue, ifFalse, name, description, nodeRef, header, typeOf, instanceOf,
 *   path, and, or, not, where, return, then
 * - Filters out default/null values using @JsonInclude(Include.NON_DEFAULT)
 * - Properly serializes nested Match lists (and, or, not)
 * - Delegates to PathSerializer for Path objects
 * 
 * Example JSON output:
 * {
 *   "name": "Selection Criteria",
 *   "description": "Criteria description",
 *   "and": [...],
 *   "path": [...],
 *   "where": {...}
 * }
 * 
 * @see QuerySerializer
 * @see PathSerializer
 */
public class MatchSerializer extends JsonSerializer<Match> {
    
    private static final Logger logger = LoggerFactory.getLogger(MatchSerializer.class);
    
    /**
     * Serializes a Match object to JSON.
     * 
     * The serialization leverages Jackson's default serialization mechanism combined with
     * @JsonPropertyOrder and @JsonInclude annotations on the Match class to ensure:
     * - Properties are serialized in the specified order
     * - Default/null values are omitted
     * - Nested Match objects are properly serialized
     * 
     * @param match The Match object to serialize
     * @param gen The JsonGenerator to write to
     * @param provider The SerializerProvider for accessing other serializers
     * @throws IOException If an I/O error occurs during serialization
     */
    @Override
    public void serialize(Match match, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (match == null) {
            gen.writeNull();
            return;
        }
        
        logger.debug("Serializing Match: {}", match.getName() != null ? match.getName() : "unnamed");
        
        // Use default serialization which respects @JsonPropertyOrder and @JsonInclude annotations
        provider.defaultSerializeValue(match, gen);
    }
    
    /**
     * Returns the type handled by this serializer.
     * 
     * @return Match class
     */
    @Override
    public Class<Match> handledType() {
        return Match.class;
    }
}