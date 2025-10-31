package org.endeavourhealth.imapi.transformation.output;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.endeavourhealth.imapi.model.imq.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Custom Jackson serializer for Path objects.
 * 
 * Handles serialization of Path objects to JSON with the following features:
 * - Properly serializes Path hierarchy and nested paths
 * - Filters out default/null values using @JsonInclude(Include.NON_DEFAULT)
 * - Handles inverse and optional path properties
 * - Supports recursive path definitions
 * 
 * Example JSON output:
 * {
 *   "iri": "path-iri",
 *   "name": "Path Name",
 *   "inverse": false,
 *   "optional": false,
 *   "path": [
 *     {
 *       "iri": "nested-path-iri",
 *       "name": "Nested Path"
 *     }
 *   ]
 * }
 * 
 * @see QuerySerializer
 * @see MatchSerializer
 */
public class PathSerializer extends JsonSerializer<Path> {
    
    private static final Logger logger = LoggerFactory.getLogger(PathSerializer.class);
    
    /**
     * Serializes a Path object to JSON.
     * 
     * The serialization leverages Jackson's default serialization mechanism combined with
     * @JsonPropertyOrder and @JsonInclude annotations on the Path class to ensure:
     * - Properties are serialized in the specified order
     * - Default/null values are omitted
     * - Nested Path objects are properly serialized
     * 
     * @param path The Path object to serialize
     * @param gen The JsonGenerator to write to
     * @param provider The SerializerProvider for accessing other serializers
     * @throws IOException If an I/O error occurs during serialization
     */
    @Override
    public void serialize(Path path, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (path == null) {
            gen.writeNull();
            return;
        }
        
        logger.debug("Serializing Path: {} (inverse={}, optional={})", 
            path.getName() != null ? path.getName() : "unnamed",
            path.isInverse(),
            path.isOptional());
        
        // Use default serialization which respects @JsonPropertyOrder and @JsonInclude annotations
        provider.defaultSerializeValue(path, gen);
    }
    
    /**
     * Returns the type handled by this serializer.
     * 
     * @return Path class
     */
    @Override
    public Class<Path> handledType() {
        return Path.class;
    }
}