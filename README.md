# IMAPI

![Version](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild/badges/IMAPI/version.svg)
![Build Status](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild/badges/IMAPI/build.svg)
![Unit Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild/badges/IMAPI/unit-test.svg)
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

