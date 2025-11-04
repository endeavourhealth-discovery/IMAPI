---
description: Repository Information Overview
alwaysApply: true
---

# IMAPI (Information Model API) Information

## Summary
IMAPI (Information Model API) is a comprehensive Spring Boot REST API application for managing information models, semantic queries, and transformations. The project includes support for FHIR, OWL API, RDF4J semantic stores, Elasticsearch integration, and AWS cloud services. It features a QOF (Quality and Outcomes Framework) to IMQ (Information Model Query) transformer for healthcare data transformation.

## Structure
- **src/main/java**: Core Java application code (org.endeavourhealth.imapi package)
- **src/main/resources**: Configuration files, properties, and static assets
- **src/test/java**: Test suites including unit and integration tests
- **docs**: Documentation including requirements, implementation plans, and usage guides
- **artifacts/docker**: Docker setup scripts for database and message queue dependencies
- **TestQueries**: Sample SPARQL and test queries
- **TestTransforms**: Test data for transformation validation
- **gradle**: Gradle build configuration and dependency versions
- **AI-Specs**: Specifications for work packages, designed for use by Zencoder (AI). Markdown files should be numbered to maintain order, starting at 1 within each subfolder.

## Language & Runtime
**Language**: Java
**Version**: Java 21 (source and target compatibility: JavaVersion.VERSION_21)
**Java Home**: %USERPROFILE%\.jdks\corretto-21.0.8
**Build System**: Gradle with wrapper
**Gradle Version**: Latest (wrapper-based)
**Package Manager**: Gradle with catalog-based version management

## Dependencies
**Main Framework & Web**:
- Spring Boot 3.4.5 (spring-web, spring-amqp, spring-security, spring-data-jpa)
- Spring Framework 6.2.5
- Jersey 3.1.10 (jersey-client, jersey-inject)
- Tomcat embedded server

**Semantic & Data Processing**:
- HAPI FHIR R4 7.4.0
- OWL API 5.5.0
- RDF4J 4.3.13
- Elasticsearch 7.17.28
- OpenLlet 2.6.5

**Serialization & JSON**:
- Jackson 2.18.0
- XML processing (Woodstox 7.1.0)

**Metrics & Monitoring**:
- Dropwizard Metrics 4.2.30 (core, graphite, servlets)
- SLF4J 2.0.17
- Logback 1.5.18

**AWS Integration**:
- AWS SDK 2.28.13 (Cognito IDP, S3, Core)
- RabbitMQ AMQP Client 5.24.0

**Utilities**:
- Apache Commons Collections 4.4
- Apache Commons Text 1.10.0
- Apache POI 5.4.0
- ANTLR 4.13.1
- Lombok 1.18.30

**Development Dependencies**:
- JUnit 5.10.1
- JUnit Suite 1.11.4
- Mockito 5.14.2
- Cucumber 7.22.0
- Spring Test
- Spring Test Security 6.2.0
- System Stubs 2.1.7

**Database Drivers**:
- MySQL Connector 9.2.0
- PostgreSQL Driver 42.7.4
- H2 Database 2.3.232

## Build & Installation
```bash
# Build the project
./gradlew build

# Build with specific tasks
./gradlew clean build

# Generate WAR artifact (imapi.war)
./gradlew war

# Generate Fat JAR for QOF→IMQ CLI
./gradlew qofimqFatJar

# Run tests
./gradlew test

# Generate code coverage report
./gradlew jacocoTestReport

# Publish artifacts
./gradlew publish
