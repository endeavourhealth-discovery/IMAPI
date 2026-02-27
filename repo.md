# IMAPI Repository Overview

The **Information Model API (IMAPI)** is the core backend implementation for the **Endeavour Health Information Model**. It provides a robust set of services for managing, querying, and transforming semantic health data.

## 🚀 Tech Stack

- **Languages**: [Kotlin](https://kotlinlang.org/) (JVM), Java 21
- **Frameworks**: [Spring Boot](https://spring.io/projects/spring-boot), [Spring Security](https://spring.io/projects/spring-security), [Spring Data JPA](https://spring.io/projects/spring-data-jpa), [Dropwizard](https://www.dropwizard.io/)
- **Semantic Web & Graph**: [RDF4J](https://rdf4j.org/), [OWL API](https://github.com/owlcs/owlapi), [FaCT++ Reasoner](http://factplusplus.sourceforge.net/), [Openllet](https://github.com/Galigator/openllet)
- **Databases & Search**: [OpenSearch](https://opensearch.org/) (Elasticsearch), [MySQL](https://www.mysql.com/)
- **Infrastructure**: [AWS (S3, Cognito, CodeBuild)](https://aws.amazon.com/)
- **Build & CI/CD**: [Gradle](https://gradle.org/), [GitHub Actions](https://github.com/features/actions), [SonarCloud](https://sonarcloud.io/)
- **Healthcare Standards**: [HL7 FHIR (R4)](https://www.hl7.org/fhir/r4/)

## 🏗️ Core Components

### 🟢 Controllers (`org.endeavourhealth.imapi.controllers`)
Exposes REST endpoints for various functionalities:
- **Entity & Concept Management**: CRUD operations and metadata retrieval.
- **Querying**: Support for IMQ (Information Model Query), ECL (Expression Constraint Language), and SPARQL.
- **Transformation**: Data transformation services (e.g., FHIR to IM, OWL to TT).
- **Security**: Authentication and authorization integration.

### 💾 Data Access (`org.endeavourhealth.imapi.dataaccess`)
Handles interactions with underlying data stores:
- **Repositories**: Specialized repositories for Concepts, Entities, Sets, and Workflows.
- **Search**: Integration with OpenSearch for high-performance entity searching.
- **RDF/Triplestore**: Management of TripleTree documents and RDF-based data.

### 🔄 Transformation & Logic (`org.endeavourhealth.imapi.transforms`, `org.endeavourhealth.imapi.logic`)
The engine for data processing:
- **Translators**: Converters between different semantic formats (e.g., Turtle, OWL, SCG, ECL).
- **Filer**: Logic for persisting complex documents into the information model.
- **Reasoner**: Integration with semantic reasoners like FaCT++ for classification.

### 📦 Models (`org.endeavourhealth.imapi.model`)
Defines the core data structures:
- **TripleTree (TT)**: A flexible graph-based data model used throughout the system.
- **IMQ**: Data models for the Information Model Query language.
- **ECL**: Models for SNOMED CT Expression Constraint Language.

## 📂 Project Structure

- `src/main/java`: Primary source code (Spring components, logic, models).
- `src/test/java`: Comprehensive test suite including JUnit and Cucumber (BDD).
- `gradle/`: Build configuration, version catalogs, and custom build scripts.
- `aws_scripts/`: Deployment and build scripts for AWS environments.
- `Postman/`: Pre-configured API collections for testing and development.
- `TestQueries/`: Sample queries in various formats (SPARQL, ECL, IMQ).
- `vocab.json`: Definition of the core vocabulary used by the system.

## 🛠️ Development & Build

### Environment Variables
Key variables required for local development:
- `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`: AWS credentials.
- `COGNITO_USER_POOL`, `COGNITO_WEB_CLIENT`: Cognito configuration.
- `OPENSEARCH_URL`, `OPENSEARCH_AUTH`: Search engine connection.

### Gradle Commands
- **Build**: `./gradlew build`
- **Run Tests**: `./gradlew test`
- **Generate TypeScript**: `./gradlew generateTypeScript` (Generates interfaces for frontend `IMDirectory`)
- **Static Const Generation**: `./gradlew staticConstGenerator` (Syncs `vocab.json` with Java/TS source)

---
*For more detailed documentation, see the [docs/](./docs/) directory.*
