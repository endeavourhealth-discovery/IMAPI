# Information Model API (IMAPI) Developer Guide

This document provides a structural overview of the IMAPI codebase, its core technologies, and development workflows.

## Project Overview
IMAPI is a backend service for managing semantic health data. It is built using **Java 21** and **Spring Boot**, with **Kotlin** used for some components and build scripts.

### Core Technologies
- **Framework**: Spring Boot
- **Data Model**: TripleTree (TT) - a flexible graph-based model
- **Serialization**: Jackson (custom deserializers/serializers for TT)
- **Database/Search**: OpenSearch, RDF4J, SQL (via JPA)
- **Grammars**: ANTLR4 (for ECL, IML, etc.)
- **Build Tool**: Gradle (Kotlin DSL)

---

## Directory Structure
```text
IMAPI/
├── src/main/                 # Application source
│   ├── java/org/endeavourhealth/imapi/
│   │   ├── controllers/      # REST API Endpoints
│   │   ├── dataaccess/       # Repositories and Data Access Objects
│   │   ├── logic/            # Business logic and Services
│   │   ├── model/            # Data models (DTOs, TripleTree, etc.)
│   │   ├── transforms/       # Data transformation logic (ECL, FHIR, etc.)
│   │   ├── vocabulary/       # Generated vocabulary constants
│   │   └── ImApiSpringApplication.java
│   ├── grammars/             # ANTLR4 grammar files (.g4)
│   └── resources/            # Configuration and static assets
├── src/test/                 # Test source (JUnit, Cucumber)
├── TestQueries/              # Sample queries and expected results for testing
├── TestTransforms/           # Sample data for transformation testing
├── docs/                     # Documentation
├── vocab.json                # Source of truth for core vocabulary
└── build.gradle.kts          # Build configuration
```

---

## Key Concepts

### TripleTree (TT)
The core of the Information Model is the **TripleTree** (found in `org.endeavourhealth.imapi.model.tripletree`). It allows for a highly flexible, graph-based representation of entities and their relationships.

### Vocabulary Management
The `vocab.json` file defines the core terminology. 
- **Static Constants**: Java constants are generated from `vocab.json` using the `staticConstGenerator` task.
- **Location**: Generated files reside in `org.endeavourhealth.imapi.vocabulary`.

### Grammars and Parsing
IMAPI uses ANTLR4 to parse various languages used in health informatics, such as:
- **ECL**: Expression Constraint Language
- **IML**: Information Model Language
- **IMQ**: Information Model Query

---

## Development Workflow

### Build and Run
- **Build**: `./gradlew build`
- **Run**: Standard Spring Boot run via IDE or `./gradlew bootRun`
- **Generate TypeScript**: `./gradlew generateTypeScript` (Syncs models with the frontend)
- **Static Const Generation**: `./gradlew staticConstGenerator`

### Environment Variables
The application requires several environment variables for full functionality (AWS, OpenSearch, Security). Refer to the main `README.md` for the full list.

### Testing
- **Unit Tests**: JUnit 5 and Mockito.
- **BDD Tests**: Cucumber for feature-based testing.
- **Command**: `./gradlew test`

---

## Integrations
- **AWS**: S3 for storage and Cognito for security.
- **OpenSearch**: Powering search capabilities across the information model.
- **RDF4J**: Used for graph-based data handling and filing.
- **HAPI FHIR**: Integration with FHIR standards.
