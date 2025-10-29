# IMAPI

![Version](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMAPI/version.svg)
![Build Status](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMAPI/build.svg)
![Unit Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMAPI/unit-test.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=endeavourhealth-discovery_IMAPI&metric=alert_status)](https://sonarcloud.io/dashboard?id=endeavourhealth-discovery_IMAPI)

Main API implementation for the Endeavour Information Model

NOTE: The following environment variables need to be set

| Name                  | Value                                                           |
|-----------------------|-----------------------------------------------------------------|
| AWS_ACCESS_KEY_ID     | Cognito (credentials) Key ID                                    |
| AWS_SECRET_ACCESS_KEY | Cognito (credentials) Secret                                    |
| COGNITO_IDENTITY_POOL | Cognito Identity pool                                           | 
| COGNITO_REGION        | Cognito Region                                                  |                              
| COGNITO_USER_POOL     | Cognito User Pool ID                                            |                 
| COGNITO_WEB_CLIENT    | Cognito Web Client ID                                           |         
| DELTA_PATH            | Directory to store deltas when saving/filing                    |                      
| GITHUB_TOKEN          | Github API token (for retrieval of version)                     | 
| HOSTING_MODE          | Whether login is required to view ("public") or not ("private") |                                   
| OPENSEARCH_AUTH       | OpenSearch authentication token                                 |              
| OPENSEARCH_INDEX      | Name of the OpenSearch index to use                             |                           
| OPENSEARCH_URL        | Base URL of the OpenSearch server                               |    



## QOF → IMQ Transformer

This project includes a QOF boolean-query to IMQ transformer with CLI support.

- Developer guide: `docs/qofimq/DEVELOPER.md`
- Usage guide: `docs/qofimq/USAGE.md`
- Extending the transformer: `docs/qofimq/EXTENDING.md`

### Build a runnable CLI fat-jar

Use the provided Gradle task to assemble a runnable jar containing all dependencies:

```
./gradlew qofimqFatJar
```

The artifact will be created at `build/libs/imapi-cli.jar`.

### Run the CLI

```
java -jar build/libs/imapi-cli.jar --qofimq-cli \
  --input="Z:\\Data\\QOF" \
  --output="Z:\\Data\\QOF\\_imq_out" \
  --emit-json \
  --parallelism=4
```

Exit code is `0` on success, otherwise the number of failed files (capped at 255). On unexpected exception, exit code `2` is returned.
