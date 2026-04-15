# Repository Guidelines

## Project Structure & Module Organization
The **Information Model API (IMAPI)** is a Java 21/Kotlin backend for managing semantic health data using Spring Boot.
- **`src/main/java`**: Contains the core logic, divided into `controllers`, `dataaccess`, `model`, and `transforms`.
- **`src/test/java`**: Houses JUnit and Cucumber tests.
- **`vocab.json`**: The source of truth for the core vocabulary. Changes here should be synced via `staticConstGenerator`.
- **`TripleTree (TT)`**: A flexible graph-based data model used throughout the system.

## Build, Test, and Development Commands
Use the Gradle wrapper for all tasks:
- **Build**: `./gradlew build`
- **Test**: `./gradlew test`
- **Run Single Test**: `./gradlew test --tests "org.package.TestClassName"`
- **Generate TypeScript**: `./gradlew generateTypeScript` (Syncs Java models with the frontend `VueLibrary`)
- **Static Const Generation**: `./gradlew staticConstGenerator` (Syncs `vocab.json` with Java source)

## Coding Style & Naming Conventions
- **Language**: Primary backend logic is in Java 21, with Kotlin used for newer components and build scripts.
- **Annotations**: Extensively uses **Lombok** (`@Getter`, `@Setter`, `@NoArgsConstructor`) to reduce boilerplate.
- **JSON**: **Jackson** is the primary library for serialization.
- **Naming**: Follow standard Java/Spring camelCase naming for variables and methods.

## Testing Guidelines
- **Frameworks**: Uses **JUnit 5**, **Mockito**, and **Cucumber** for BDD-style testing.
- **Database**: Integration tests often involve Mockito or Spring's test security context.
- **Coverage**: Jacoco is configured for test reporting.

## Commit & Pull Request Guidelines
- **Commit Messages**: Follow a relaxed conventional commit style: `feat:`, `fix:`, `refactor(scope):`, `cleanup:`.
- **Branching**: Development typically happens on feature branches merged into `develop` or directly via PRs.
- **Env Vars**: Ensure required environment variables (AWS, Cognito, OpenSearch) are set for development as listed in the `README.md`.
