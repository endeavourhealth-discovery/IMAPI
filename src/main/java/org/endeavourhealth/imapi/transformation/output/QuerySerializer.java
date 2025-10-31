package org.endeavourhealth.imapi.transformation.output;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.endeavourhealth.imapi.model.imq.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Custom Jackson serializer for Query objects.
 * 
 * Handles serialization of Query objects to JSON with the following features:
 * - Enforces property ordering: prefix, iri, name, description, query, activeOnly, typeOf, isCohort, instanceOf,
 *   and, or, not, path, where, return, groupBy, dataSet
 * - Filters out default/null values using @JsonInclude(Include.NON_DEFAULT)
 * - Delegates to specialized serializers for complex types (Match, Path, Return, GroupBy)
 * 
 * Example JSON output:
 * {
 *   "prefix": {...},
 *   "iri": "query-iri",
 *   "name": "Query Name",
 *   "description": "Query description",
 *   "where": {...},
 *   "return": {...},
 *   "groupBy": [...]
 * }
 * 
 * @see MatchSerializer
 * @see PathSerializer
 * @see ReturnSerializer
 * @see GroupBySerializer
 */
public class QuerySerializer extends JsonSerializer<Query> {
    
    private static final Logger logger = LoggerFactory.getLogger(QuerySerializer.class);
    
    /**
     * Serializes a Query object to JSON.
     * 
     * The serialization leverages Jackson's default serialization mechanism combined with
     * @JsonPropertyOrder and @JsonInclude annotations on the Query class to ensure:
     * - Properties are serialized in the specified order
     * - Default/null values are omitted
     * 
     * @param query The Query object to serialize
     * @param gen The JsonGenerator to write to
     * @param provider The SerializerProvider for accessing other serializers
     * @throws IOException If an I/O error occurs during serialization
     */
    @Override
    public void serialize(Query query, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (query == null) {
            gen.writeNull();
            return;
        }
        
        logger.debug("Serializing Query: {}", query.getName() != null ? query.getName() : "unnamed");
        
        // Use default serialization which respects @JsonPropertyOrder and @JsonInclude annotations
        provider.defaultSerializeValue(query, gen);
    }
    
    /**
     * Returns the type handled by this serializer.
     * 
     * @return Query class
     */
    @Override
    public Class<Query> handledType() {
        return Query.class;
    }
}