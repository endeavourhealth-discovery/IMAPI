/**
 * IMQ to SQL Converter - Phase 3 Implementation
 * 
 * Converts Information Model Query (IMQ) JSON structures into executable SQL statements
 * for execution against the Compass 2.3.0 healthcare database.
 * 
 * Architecture:
 * - Modular converter pipeline with 6 phases
 * - Configurable entity/property mapping via YAML configuration
 * - Support for basic IMQ queries with WHERE, PATH, and RETURN components
 * - Compass-specific optimizations and context handling
 */

package org.endeavourhealth.imapi.transforms.compass;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.model.imq.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main converter class orchestrating IMQ to SQL transformation.
 * Implements MVP functionality (Implementation Phase 1 from plan).
 */
@Slf4j
public class IMQToSQLConverter {
    private final IMQToCompassMappingConfig mappingConfig;
    private final SQLGenerator sqlGenerator;
    private final EntityResolver entityResolver;
    private final ConditionTranslator conditionTranslator;

    public IMQToSQLConverter(InputStream mappingConfigStream) throws IOException {
        this.mappingConfig = loadMappingConfig(mappingConfigStream);
        this.entityResolver = new EntityResolver(mappingConfig);
        this.conditionTranslator = new ConditionTranslator(mappingConfig);
        this.sqlGenerator = new SQLGenerator(mappingConfig);
    }

    /**
     * Phase 1: Parse & Validate
     * Validates IMQ structure and extracts components
     */
    public IMQValidationResult validateIMQ(Query imqQuery) {
        try {
            if (imqQuery == null) {
                return IMQValidationResult.error("IMQ Query is null");
            }

            // Check for at least one primary component
            boolean hasTypeOf = imqQuery.getTypeOf() != null;
            boolean hasPath = imqQuery.getPath() != null && !imqQuery.getPath().isEmpty();
            boolean hasWhere = imqQuery.getWhere() != null;

            if (!hasTypeOf && !hasPath && !hasWhere) {
                return IMQValidationResult.error(
                    "IMQ must contain at least one of: typeOf, path, or where clause"
                );
            }

            return IMQValidationResult.success();
        } catch (Exception e) {
            return IMQValidationResult.error("Validation error: " + e.getMessage());
        }
    }

    /**
     * Phase 2: Entity Resolution
     * Maps IMQ entity IRIs to Compass tables and identifies required joins
     */
    public ConversionContext resolveEntities(Query imqQuery) throws ConversionException {
        ConversionContext context = new ConversionContext();

        // Resolve primary entity type
        if (imqQuery.getTypeOf() != null) {
            Node typeOfNode = imqQuery.getTypeOf();
            String entityIri = typeOfNode.getIri();
            EntityMapping entityMapping = entityResolver.resolveEntity(entityIri);
            if (entityMapping == null) {
                throw new ConversionException("Unknown entity type: " + entityIri);
            }
            context.setPrimaryEntity(entityMapping);
            context.addTable(entityMapping.getTableName(), "primary");
        }

        // Resolve properties in WHERE clause for additional joins
        if (imqQuery.getWhere() != null) {
            resolvePropertiesInWhere(imqQuery.getWhere(), context);
        }

        // Resolve properties in PATH for navigation joins
        if (imqQuery.getPath() != null && !imqQuery.getPath().isEmpty()) {
            resolvePath(imqQuery.getPath(), context);
        }

        // Resolve return properties
        Return returnClause = imqQuery.getReturn();
        if (returnClause != null && returnClause.getProperty() != null && !returnClause.getProperty().isEmpty()) {
            resolveReturnProperties(returnClause.getProperty(), context);
        }

        return context;
    }

    /**
     * Phase 3: Condition Translation
     * Converts WHERE clauses to SQL conditions
     */
    public String translateConditions(Where where, ConversionContext context) 
            throws ConversionException {
        if (where == null) {
            return null;
        }
        return conditionTranslator.translateWhere(where, context);
    }

    /**
     * Phase 6: Query Assembly & Optimization
     * Generates final SQL from all components
     */
    public CompassQuery generateSQL(Query imqQuery, String organizationId, Long patientId) 
            throws ConversionException {
        // Validate
        IMQValidationResult validation = validateIMQ(imqQuery);
        if (!validation.isValid()) {
            throw new ConversionException("Invalid IMQ: " + validation.getErrorMessage());
        }

        // Phase 2: Resolve entities
        ConversionContext context = resolveEntities(imqQuery);
        context.setOrganizationId(organizationId);
        context.setPatientId(patientId);

        // Phase 3: Translate conditions
        String whereClause = translateConditions(imqQuery.getWhere(), context);

        // Phase 4 & 5: Generate SQL
        CompassQuery query = sqlGenerator.generateQuery(
            context,
            whereClause,
            imqQuery.getReturn(),
            imqQuery.getOrderBy()
        );

        return query;
    }

    // ===== Helper Methods =====

    private void resolvePropertiesInWhere(Where where, ConversionContext context) {
        if (where == null) return;

        // Resolve properties from this where clause
        if (where.getIri() != null) {
            PropertyMapping propMapping = entityResolver.resolveProperty(where.getIri());
            if (propMapping != null && propMapping.getCompassColumn() != null) {
                // Property is resolvable; add join if needed
                // Note: RequiresJoin is not in PropertyMapping; joins determined by context
            }
        }

        // Recursively resolve nested where clauses
        if (where.getAnd() != null) {
            where.getAnd().forEach(w -> resolvePropertiesInWhere(w, context));
        }
        if (where.getOr() != null) {
            where.getOr().forEach(w -> resolvePropertiesInWhere(w, context));
        }
        // Note: where.getNot() returns boolean flag, not a nested Where object
    }

    private void resolvePath(List<Path> paths, ConversionContext context) {
        for (Path path : paths) {
            if (path.getIri() != null) {
                PropertyMapping propMapping = entityResolver.resolveProperty(path.getIri());
                if (propMapping != null && propMapping.getCompassColumn() != null) {
                    // Path property resolved successfully
                }
            }
            // Recursively handle nested paths
            if (path.getPath() != null && !path.getPath().isEmpty()) {
                resolvePath(path.getPath(), context);
            }
        }
    }

    private void resolveReturnProperties(List<ReturnProperty> returnProps, ConversionContext context) {
        for (ReturnProperty returnProp : returnProps) {
            if (returnProp.getIri() != null) {
                PropertyMapping propMapping = entityResolver.resolveProperty(returnProp.getIri());
                if (propMapping != null && propMapping.getCompassColumn() != null) {
                    // Return property resolved successfully
                }
            }
        }
    }

    private IMQToCompassMappingConfig loadMappingConfig(InputStream configStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(configStream, IMQToCompassMappingConfig.class);
    }

    // ===== Inner Classes =====

    /**
     * Entity Resolver - Maps IMQ entity IRIs to Compass schema
     */
    private static class EntityResolver {
        private final IMQToCompassMappingConfig config;

        EntityResolver(IMQToCompassMappingConfig config) {
            this.config = config;
        }

        EntityMapping resolveEntity(String entityIri) {
            return config.getEntityMappings()
                .stream()
                .filter(e -> e.getImqEntityIri().equalsIgnoreCase(entityIri) ||
                            e.getImqEntityIri().endsWith("#" + extractLastSegment(entityIri)))
                .findFirst()
                .orElse(null);
        }

        PropertyMapping resolveProperty(String propertyIri) {
            return config.getPropertyMappings()
                .stream()
                .filter(p -> p.getImqPropertyIri().equalsIgnoreCase(propertyIri) ||
                            p.getImqPropertyIri().endsWith("#" + extractLastSegment(propertyIri)))
                .findFirst()
                .orElse(null);
        }

        private String extractLastSegment(String iri) {
            return iri.contains("#") ? iri.substring(iri.lastIndexOf("#") + 1) : iri;
        }
    }

    /**
     * Condition Translator - Converts WHERE clauses to SQL WHERE conditions
     */
    private static class ConditionTranslator {
        private final IMQToCompassMappingConfig config;

        ConditionTranslator(IMQToCompassMappingConfig config) {
            this.config = config;
        }

        String translateWhere(Where where, ConversionContext context) throws ConversionException {
            if (where == null) return null;

            List<String> conditions = new ArrayList<>();

            // Handle AND conditions
            if (where.getAnd() != null && !where.getAnd().isEmpty()) {
                List<String> andConditions = where.getAnd().stream()
                    .map(w -> {
                        try {
                            return translateWhere(w, context);
                        } catch (ConversionException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
                if (!andConditions.isEmpty()) {
                    conditions.add("(" + String.join(" AND ", andConditions) + ")");
                }
            }
            // Handle OR conditions
            else if (where.getOr() != null && !where.getOr().isEmpty()) {
                List<String> orConditions = where.getOr().stream()
                    .map(w -> {
                        try {
                            return translateWhere(w, context);
                        } catch (ConversionException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
                if (!orConditions.isEmpty()) {
                    conditions.add("(" + String.join(" OR ", orConditions) + ")");
                }
            }
            // Handle simple conditions (based on operator and value)
            else if (where.getIri() != null) {
                String condition = translateSimpleCondition(where, context);
                if (condition != null) {
                    String result = condition;
                    // Apply NOT if flag is set
                    if (where.isNot()) {
                        result = "NOT (" + result + ")";
                    }
                    conditions.add(result);
                }
            }

            return conditions.isEmpty() ? null : String.join(" AND ", conditions);
        }

        private String translateSimpleCondition(Where where, ConversionContext context) 
                throws ConversionException {
            PropertyMapping propMapping = context.getMappingConfig()
                .getPropertyMappings()
                .stream()
                .filter(p -> p.getImqPropertyIri().equals(where.getIri()) ||
                            p.getImqPropertyIri().endsWith("#" + extractLastSegment(where.getIri())))
                .findFirst()
                .orElse(null);

            if (propMapping == null) {
                throw new ConversionException("Unknown property: " + where.getIri());
            }

            String columnRef = propMapping.getCompassTable() + "." + propMapping.getCompassColumn();

            // Handle concept references with "is" operator
            if (where.getIs() != null && !where.getIs().isEmpty()) {
                return translateConceptCondition(where, propMapping, columnRef);
            }

            // Handle comparison operators
            if (where.getOperator() != null && where.getValue() != null) {
                return translateComparisonCondition(where, propMapping, columnRef);
            }

            // Handle null/not null checks
            if (where.getIsNull() || where.getIsNotNull()) {
                return columnRef + (where.getIsNull() ? " IS NULL" : " IS NOT NULL");
            }

            return null;
        }

        private String translateConceptCondition(Where where, PropertyMapping propMapping, String columnRef) {
            // Simple concept matching (MVP)
            if (where.getIs() != null && !where.getIs().isEmpty()) {
                Node conceptNode = where.getIs().get(0);
                String conceptId = extractConceptId(conceptNode.getIri());
                return columnRef + " = " + conceptId;
            }
            return null;
        }

        private String translateComparisonCondition(Where where, PropertyMapping propMapping, String columnRef) 
                throws ConversionException {
            Operator operator = where.getOperator();
            String value = where.getValue();
            
            if (operator == null || value == null) {
                return null;
            }

            String sqlOperator = mapOperator(operator);
            String formattedValue = formatValue(value, propMapping);

            return columnRef + " " + sqlOperator + " " + formattedValue;
        }

        private String mapOperator(Operator op) throws ConversionException {
            if (op == null) return "=";
            
            return switch (op) {
                case eq -> "=";
                case gte -> ">=";
                case gt -> ">";
                case lte -> "<=";
                case lt -> "<";
                case start -> "LIKE";
                case isTrue -> "= 1";
                case contains -> "LIKE";
                default -> throw new ConversionException("Unsupported operator: " + op);
            };
        }

        private String formatValue(Object value, PropertyMapping propMapping) {
            if (value == null) return "NULL";
            if (value instanceof String) return "'" + value.toString().replace("'", "''") + "'";
            return value.toString();
        }

        private String extractConceptId(String iri) {
            return iri.contains("#") ? iri.substring(iri.lastIndexOf("#") + 1) : iri;
        }

        private String extractLastSegment(String iri) {
            return iri.contains("#") ? iri.substring(iri.lastIndexOf("#") + 1) : iri;
        }
    }

    /**
     * SQL Generator - Assembles final SQL queries
     */
    private static class SQLGenerator {
        private final IMQToCompassMappingConfig config;

        SQLGenerator(IMQToCompassMappingConfig config) {
            this.config = config;
        }

        CompassQuery generateQuery(ConversionContext context, String whereClause, 
                                  Return returnClause, OrderLimit orderBy) 
                throws ConversionException {
            CompassQuery query = new CompassQuery();

            // Build FROM clause
            String fromClause = buildFromClause(context);
            query.setFromClause(fromClause);

            // Build WHERE clause with organization context
            String finalWhereClause = buildWhereClause(context, whereClause);
            query.setWhereClause(finalWhereClause);

            // Build SELECT clause
            List<ReturnProperty> returnProps = returnClause != null ? returnClause.getProperty() : null;
            String selectClause = buildSelectClause(context, returnProps);
            query.setSelectClause(selectClause);

            // Build ORDER BY clause
            if (orderBy != null) {
                query.setOrderByClause(buildOrderByClause(orderBy));
            }

            // Assemble final SQL
            String sql = assembleFinalSQL(selectClause, fromClause, finalWhereClause, 
                                         query.getOrderByClause());
            query.setSql(sql);

            return query;
        }

        private String buildFromClause(ConversionContext context) {
            EntityMapping primaryEntity = context.getPrimaryEntity();
            if (primaryEntity == null) {
                throw new IllegalArgumentException("No primary entity resolved");
            }

            StringBuilder from = new StringBuilder();
            from.append("FROM ").append(primaryEntity.getTableName()).append(" p");

            // Add required joins
            for (String joinTable : context.getRequiredJoins()) {
                from.append("\nLEFT JOIN ").append(joinTable).append(" ON ...");
            }

            return from.toString();
        }

        private String buildWhereClause(ConversionContext context, String whereClause) {
            List<String> conditions = new ArrayList<>();

            // Always add organization context (Rule 1)
            if (context.getOrganizationId() != null) {
                conditions.add("p.organization_id = '" + context.getOrganizationId() + "'");
            }

            // Add patient context if available
            if (context.getPatientId() != null) {
                conditions.add("p.person_id = " + context.getPatientId());
            }

            // Add custom where conditions
            if (whereClause != null && !whereClause.isEmpty()) {
                conditions.add(whereClause);
            }

            return conditions.isEmpty() ? "" : "WHERE " + String.join(" AND ", conditions);
        }

        private String buildSelectClause(ConversionContext context, List<ReturnProperty> returnProps) {
            if (returnProps == null || returnProps.isEmpty()) {
                // Default: return primary key
                EntityMapping primaryEntity = context.getPrimaryEntity();
                return "SELECT p." + primaryEntity.getPrimaryKey();
            }

            List<String> selectItems = new ArrayList<>();
            for (ReturnProperty returnProp : returnProps) {
                if (returnProp.getIri() != null) {
                    selectItems.add("p." + returnProp.getIri());
                }
            }

            return "SELECT " + String.join(", ", selectItems);
        }

        private String buildOrderByClause(OrderLimit orderBy) {
            if (orderBy == null || orderBy.getProperty() == null || orderBy.getProperty().isEmpty()) {
                return "";
            }

            StringBuilder orderClause = new StringBuilder("ORDER BY ");
            List<String> orderItems = new ArrayList<>();

            for (OrderDirection orderDir : orderBy.getProperty()) {
                String direction = "ASC";  // Default direction
                if (orderDir.getDirection() != null) {
                    direction = orderDir.getDirection().toString();
                }
                String propertyName = orderDir.getIri() != null ? orderDir.getIri() : "id";
                orderItems.add("p." + propertyName + " " + direction);
            }

            return orderClause.append(String.join(", ", orderItems)).toString();
        }

        private String assembleFinalSQL(String selectClause, String fromClause, 
                                       String whereClause, String orderByClause) {
            StringBuilder sql = new StringBuilder();
            sql.append(selectClause).append("\n");
            sql.append(fromClause).append("\n");
            
            if (whereClause != null && !whereClause.isEmpty()) {
                sql.append(whereClause).append("\n");
            }
            
            if (orderByClause != null && !orderByClause.isEmpty()) {
                sql.append(orderByClause);
            }

            return sql.toString().trim();
        }
    }

    // ===== Data Transfer Objects =====

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class IMQValidationResult {
        private boolean valid;
        private String errorMessage;

        public static IMQValidationResult success() {
            return IMQValidationResult.builder().valid(true).build();
        }

        public static IMQValidationResult error(String message) {
            return IMQValidationResult.builder().valid(false).errorMessage(message).build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CompassQuery {
        private String sql;
        private String selectClause;
        private String fromClause;
        private String whereClause;
        private String orderByClause;
        private Map<String, Object> parameters;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConversionContext {
        private EntityMapping primaryEntity;
        private Map<String, String> tables = new HashMap<>();
        private Set<String> requiredJoins = new HashSet<>();
        private String organizationId;
        private Long patientId;
        private IMQToCompassMappingConfig mappingConfig;

        public void addTable(String tableName, String role) {
            tables.put(role, tableName);
        }

        public void addRequiredJoin(String joinTable) {
            if (joinTable != null) {
                requiredJoins.add(joinTable);
            }
        }
    }

    // ===== Configuration Classes =====

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IMQToCompassMappingConfig {
        private List<EntityMapping> entityMappings;
        private List<PropertyMapping> propertyMappings;
        private Map<String, Integer> temporalParameters;
        private List<Map<String, String>> commonJoins;
        private List<Map<String, String>> conceptResolutionStrategies;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EntityMapping {
        private String imqEntityIri;
        private String tableName;
        private String primaryKey;
        @JsonProperty("isPolymorphic")
        private boolean isPolymorphic;
        private List<String> alternateTables;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PropertyMapping {
        private String imqPropertyIri;
        private String compassTable;
        private String compassColumn;
        private String dataType;
        @JsonProperty("requiresJoin")
        private boolean requiresJoin;
        private String joinTable;
        private String joinCondition;
    }

    // ===== Exception Classes =====

    public static class ConversionException extends Exception {
        public ConversionException(String message) {
            super(message);
        }

        public ConversionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}