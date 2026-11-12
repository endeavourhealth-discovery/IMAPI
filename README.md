# IMAPI

The **Information Model API (IMAPI)** is the backend for the Endeavour Health **Information Model**: a semantic model of health data (concepts, value sets, data models, mappings and queries) held as a graph. IMAPI lets you store, search, query, validate and transform that model over a REST API.

It is a Java 21 / Kotlin Spring Boot application licensed under [Apache 2.0](LICENSE).

## Current Status

| Environment | Status                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|----------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Live     | ![Version](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-live/version.svg) ![Build](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-live/build.svg) ![Unit Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-live/unit-test.svg) ![E2E Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-live/e2e-test.svg)                                                                                                                                                      |
| UAT      | ![Version](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-uat/version.svg) ![Build](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-uat/build.svg) ![Unit Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-uat/unit-test.svg) ![E2E Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-uat/e2e-test.svg)                                                                                                                                                          |
| Dev      | ![Version](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-dev/version.svg) ![Build](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-dev/build.svg) ![Unit Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-dev/unit-test.svg) ![IMQ Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMAPI-Dev/imq-test.svg)  [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=IMAPI&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=IMAPI) |

## What it does

- **Entities and concepts**: read, create and update Information Model entities, with their metadata and hierarchy.
- **Querying**: run queries written in **IMQ** (Information Model Query), and work with **ECL** (SNOMED CT Expression Constraint Language) and SPARQL.
- **Sets and value sets**: define, expand, compare and export sets of concepts.
- **Search**: full-text entity search backed by OpenSearch.
- **Transformation**: convert between formats such as FHIR, OWL, Turtle, ECL and the IM's own TripleTree (TT) model.
- **Reasoning**: classification using semantic reasoners (FaCT++).
- **Filing**: persist documents and deltas into the graph store.
- **Security**: token-based sign-in and permission checks through [Casdoor](docs/casdoor-authorisation.md).

## Tech stack

Java 21 and Kotlin, Spring Boot and Spring Security, RDF4J (graph store), OpenSearch, MySQL (via JPA), ANTLR4 grammars, Jackson, HAPI FHIR, and Gradle (Kotlin DSL). Tests use JUnit 5, Mockito and Cucumber.

## Prerequisites

- **JDK 21**.
- Nothing else is needed to **build and run the unit tests**. The Gradle wrapper is included, and all dependencies resolve anonymously from Maven Central and the public Endeavour Artifactory. You do **not** need any credentials to build.
- To **run the application** against real data you also need the services IMAPI talks to: an RDF4J-compatible graph database (for example GraphDB or an RDF4J server), OpenSearch, and a Casdoor instance for authentication. See [docs/ENVIRONMENT.md](docs/ENVIRONMENT.md).

## Build and test

```bash
./gradlew build      # compile and run the unit tests
./gradlew test       # run the tests only
./gradlew test --tests "org.endeavourhealth.imapi.logic.service.SomeTest"   # one test class
```

On Windows use `gradlew.bat`.

Most tests stub the few environment variables they depend on, so no configuration is needed. Tests that need a live graph database or OpenSearch (tagged `imqTests` and similar) require the `GRAPH_*` and `OPENSEARCH_*` variables to point at real servers.

> `MAVEN_USERNAME` and `MAVEN_PASSWORD` are only used by the maintainers' CI to **publish** snapshots. Leave them unset.

### Useful Gradle tasks

| Task                       | Purpose                                                                                |
|----------------------------|----------------------------------------------------------------------------------------|
| `./gradlew staticConstGenerator` | Regenerates the Java constants in `vocabulary/` from [`vocab.json`](vocab.json). Runs automatically on local compiles. |
| `./gradlew generateTypeScript`   | Generates TypeScript interfaces from the Java models for the frontend library.          |

[`vocab.json`](vocab.json) is the source of truth for the core vocabulary. Edit it, not the generated files.

## Configuration

IMAPI is configured entirely through environment variables, so one build can be deployed to any environment. Copy nothing into the jar and never commit real values. The full list, with which are required and which are secrets, is in **[docs/ENVIRONMENT.md](docs/ENVIRONMENT.md)**.

For local development you can keep your values in a `.env` file. It is git-ignored.

## API documentation

IMAPI publishes an OpenAPI description through SpringDoc. Once the application is running, the generated API documentation is served by the app itself (SpringDoc's defaults are `/v3/api-docs` and `/swagger-ui.html`, unless overridden). A [Postman collection](Postman/IMQueryRunner.postman_collection.json) is also included.

Endpoints follow the pattern `api/<area>/<public|protected>/...`. `public` endpoints work without sign-in. `protected` endpoints need a valid Casdoor token, unless the instance runs with `HOSTING_MODE=public` (see [docs/ENVIRONMENT.md](docs/ENVIRONMENT.md#hosting)).

For example, once the app is running locally on the default port, the status endpoint needs no authentication:

```bash
curl http://localhost:8080/api/status/public
```

## Repository layout

```text
src/main/java/org/endeavourhealth/imapi/
  controllers/   REST endpoints
  dataaccess/    repositories for the graph store, OpenSearch and SQL
  logic/         services, reasoner, exporters, caching
  model/         data models, including the TripleTree (TT), IMQ and ECL models
  transforms/    converters between formats (ECL, FHIR, OWL, Turtle, IMQ to SQL, ...)
src/main/grammars/   ANTLR4 grammars
src/test/            JUnit and Cucumber tests
TestQueries/         sample queries and expected results
TestTransforms/      sample transformation inputs and outputs
docs/                further documentation
vocab.json           core vocabulary definition
```

## More documentation

- [DEVELOPERS.MD](DEVELOPERS.MD): developer guide and core concepts (TripleTree, vocabulary, grammars).
- [repo.md](repo.md): overview of the main components.
- [docs/ENVIRONMENT.md](docs/ENVIRONMENT.md): environment variables.
- [docs/casdoor-authorisation.md](docs/casdoor-authorisation.md): authentication and permissions.
- [docs/TermSearch.md](docs/TermSearch.md): how term search works.

## Contributing

Contributions are welcome. Please open an issue to discuss larger changes first, work on a feature branch, and make sure `./gradlew test` passes before opening a pull request. Commit messages use a relaxed conventional style (`feat:`, `fix:`, `refactor(scope):`, `cleanup:`).

## Licence

Released under the [Apache License 2.0](LICENSE).
