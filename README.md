# IMAPI

## Current Status

| Environment | Status                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
|----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Live     | ![Version](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-live/version.svg) ![Build](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-live/build.svg) ![Unit Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-live/unit-test.svg) ![E2E Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-live/e2e-test.svg) |
| UAT      | ![Version](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-uat/version.svg) ![Build](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-uat/build.svg) ![Unit Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-uat/unit-test.svg) ![E2E Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-uat/e2e-test.svg)     |
| Dev      | ![Version](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-dev/version.svg) ![Build](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-dev/build.svg) ![Unit Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-dev/unit-test.svg) ![E2E Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMDirectory-dev/e2e-test.svg)     |

## Description

Main API implementation for the Endeavour Information Model

NOTE: The following environment variables need to be set

| Name                           | Value                                                           |
|--------------------------------|-----------------------------------------------------------------|
| AWS_ACCESS_KEY_ID              | AWS Key ID                                                      |
| AWS_SECRET_ACCESS_KEY          | AWS Secret                                                      |
| DELTA_PATH                     | Directory to store deltas when saving/filing                    |
| EMAILER_HOST                   | Host SMTP server for sending emails                             |
| EMAILER_PASSWORD               | SMTP server password                                            |
| EMAILER_PORT                   | SMTP server port                                                |
| EMAILER_USERNAME               | SMTP username                                                   |
| ENDEAVOUR_SECURITY_HOST        | Endeavour security hostname                                     |
| ENDEAVOUR_SECURITY_APPLICATION | "IMDirectory"                                                   |
| GITHUB_TOKEN                   | Github API token (for retrieval of version)                     | 
| HOSTING_MODE                   | Whether login is required to view ("public") or not ("private") |       
| MODE                           | Runtime mode [dev\|production]                                  |
| OPENSEARCH_AUTH                | OpenSearch authentication token                                 |              
| OPENSEARCH_INDEX               | Name of the OpenSearch index to use                             |                           
| OPENSEARCH_URL                 | Base URL of the OpenSearch server                               |    
| UPRN_API                       | UPRN host                                                       |
| UPRN_USERNAME                  | UPRN username                                                   |
| UPRN_PASSWORD                  | UPRN password                                                   |
