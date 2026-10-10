# IMAPI environment variables

IMAPI is a compiled Java/Kotlin Spring Boot app, so configuration splits cleanly in two:

- **Build time:** read by Gradle while *building*. Nothing here ends up in the built jar.
- **Run time:** read with `System.getenv` when the application *runs*. None of these are baked in, so one build can be deployed to any environment by changing its environment.

> Never commit real values. `.env` is git-ignored, and secrets belong in your deployment's secret store.

## Build time (Gradle)

| Variable                           | Required                  | Purpose                                                                                                                                                                                                                                                               |
|------------------------------------|---------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `CI`                               | no                        | Default `false`. When it is anything other than `false` (CI servers normally set `true`), the `staticConstGenerator` step that regenerates the `vocabulary/*.java` constants from `vocab.json` is **skipped** during `compileJava`. Locally it runs on every compile. |
| `ENV`                              | no                        | Default `dev`. Only printed in the build log as `Build environment`.                                                                                                                                                                                                  |
| `MAVEN_USERNAME`, `MAVEN_PASSWORD` | for snapshot dependencies | Credentials for the Endeavour Artifactory snapshot repository.                                                                                                                                                                                                        |
| `SONAR_LOGIN`                      | for Sonar analysis        | SonarCloud token.                                                                                                                                                                                                                                                     |

## Run time

### Authentication (Casdoor)

IMAPI validates Casdoor access tokens and talks to Casdoor directly. These are read when a request first needs them, so the app starts, and public endpoints work, even if they are missing. See `docs/casdoor-authorisation.md` for how they are used.

| Variable                                     | Required     | Purpose                                                                                                                                                                                                                                    |
|----------------------------------------------|--------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `CASDOOR_URL`                                | yes          | Base URL of Casdoor. **Must be `https`.** Casdoor's signing keys are fetched from `<url>/.well-known/jwks`.                                                                                                                                |
| `CASDOOR_ORGANISATION_NAME`                  | yes          | Casdoor organisation that owns the users. Tokens for users outside it are rejected.                                                                                                                                                        |
| `CASDOOR_CLIENT_ID`, `CASDOOR_CLIENT_SECRET` | yes (secret) | Credentials of the Casdoor application IMAPI uses for management calls (read users, save preferences, ask the enforcer). A dedicated "IMAPI" application is recommended. Do **not** list this application in `CASDOOR_ALLOWED_CLIENT_IDS`. |
| `CASDOOR_ENFORCER_ID`                        | yes          | Casdoor casbin enforcer (`owner/name`) used for permission checks.                                                                                                                                                                         |
| `CASDOOR_ALLOWED_CLIENT_IDS`                 | yes          | Comma-separated client IDs of the applications whose tokens IMAPI accepts (the token `aud`), e.g. IMDirectory, IMQueryRunner and any third-party machine clients.                                                                          |
| `CASDOOR_ISSUER`                             | no           | Expected token issuer. Defaults to `CASDOOR_URL`; set it if Casdoor's configured origin differs.                                                                                                                                           |
| `CASDOOR_JWS_ALGORITHM`                      | no           | Token signing algorithm. Default `RS256`.                                                                                                                                                                                                  |

### Hosting

| Variable                              | Required | Purpose                                                                                                                                                                                    |
|---------------------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `HOSTING_MODE`                        | no       | `public`: `/api/*/protected/**` endpoints are open without sign-in. `private`: sign-in is required and unauthenticated calls get `403` rather than `401`. Anything else: sign-in required. |
| `MODE`                                | no       | `production` switches on production behaviour: GitHub release config is refreshed at startup and set exports are written to the S3 bucket. Anything else is treated as development.        |
| `CORS_ALLOWED_ORIGINS`                | no       | Comma-separated browser origins allowed to call the API. Default `http://localhost:8082`.                                                                                                  |
| `SERVER_PORT`                         | no       | Standard Spring setting. Default `8080`.                                                                                                                                                   |
| `SERVER_MAX_HTTP_REQUEST_HEADER_SIZE` | no       | Largest accepted request headers. Default `32KB` (set in `application.properties`), because Casdoor access tokens are large.                                                               |

### Graph database (RDF4J / GraphDB / Neptune)

| Variable                      | Required            | Purpose                                                                                                                                 |
|-------------------------------|---------------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| `GRAPH_TYPE`                  | no                  | `http` (default), `file` or `sparql`. Anything else fails to connect.                                                                   |
| `GRAPH_SERVER`                | for `http` / `file` | `http`: server URL, default `http://localhost:7200/`. `file`: directory of the native store, default `C:\rdf4j\`.                       |
| `GRAPH_USER`, `GRAPH_PASS`    | no (secret)         | Basic-auth credentials for the `http` server. Used only when both are set.                                                              |
| `GRAPH_INDEXES`               | no                  | Native-store indexes for `file`. Default `spoc,posc,opsc`.                                                                              |
| `GRAPH_QUERY`, `GRAPH_UPDATE` | for `sparql`        | Query and update endpoint hosts. The code falls back to a development Neptune cluster if they are unset, so always set them explicitly. |

### OpenSearch

| Variable           | Required     | Purpose                                                                                                                        |
|--------------------|--------------|--------------------------------------------------------------------------------------------------------------------------------|
| `OPENSEARCH_URL`   | yes          | Base URL of the OpenSearch server, **with a trailing slash** (the index name is appended directly).                            |
| `OPENSEARCH_INDEX` | yes          | Name of the index to search.                                                                                                   |
| `OPENSEARCH_AUTH`  | yes (secret) | The already base64-encoded `user:password`, sent as `Authorization: Basic <value>`. Search fails with an error if it is unset. |

### Files, exports and notifications

| Variable                                                                                           | Required                  | Purpose                                                                                                                              |
|----------------------------------------------------------------------------------------------------|---------------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| `DELTA_PATH`                                                                                       | yes for filing            | Directory where filed deltas are written and from which they are downloaded.                                                         |
| `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`                                                       | in production (secret)    | AWS credentials for the set-member export to S3.                                                                                     |
| `BUCKET_NAME`, `BUCKET_REGION`                                                                     | in production             | Target bucket and region of that export.                                                                                             |
| `IM1_PUBLISH_BUCKET`, `IM1_PUBLISH_REGION`, `IM1_PUBLISH_ACCESS_KEY`, `IM1_PUBLISH_SECRET_KEY`     | no (secrets)              | Override the bucket, region and credentials used when publishing the IM1 set export. Each falls back to a built-in value when unset. |
| `EMAILER_PORTAL_HOST`, `EMAILER_PORTAL_PORT`, `EMAILER_PORTAL_USERNAME`, `EMAILER_PORTAL_PASSWORD` | for task e-mails (secret) | SMTP server used to e-mail workflow task notifications. `EMAILER_PORTAL_PORT` must be a number.                                      |

### Integrations

| Variable                         | Required                               | Purpose                                                                                                                                                       |
|----------------------------------|----------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `GITHUB_TOKEN`                   | when GitHub features are used (secret) | Token for GitHub API calls (release lookups and config updates). In production mode the config is refreshed at startup, so it is needed from the first start. |
| `UPRN_API`                       | for UPRN features                      | Base URL of the UPRN service.                                                                                                                                 |
| `UPRN_USERNAME`, `UPRN_PASSWORD` | for UPRN features (secret)             | Credentials for the UPRN service.                                                                                                                             |

## Tests

Most unit and Cucumber tests stub the few variables they depend on (via system-stubs), so `./gradlew test` needs little or no environment. Tests tagged for a live graph database or OpenSearch (`imqTests` and friends) need the `GRAPH_*` and `OPENSEARCH_*` variables pointing at real servers.
