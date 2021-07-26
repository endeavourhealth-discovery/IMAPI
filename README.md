# IMAPI
![Version](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMAPI/version.svg)
![Build Status](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMAPI/build.svg)
![Unit Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild-output/badges/IMAPI/unit-test.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=endeavourhealth-discovery_IMAPI&metric=alert_status)](https://sonarcloud.io/dashboard?id=endeavourhealth-discovery_IMAPI)

##Instructions

1. Download and compile all the source
1. Configure the database connection environment variables
   * SPRING_DATASOURCE_URL=<jdbc connection string>
   * SPRING_DATASOURCE_USERNAME=<username>
   * SPRING_DATASOURCE_PASSWORD=<password>
1. Run the api module either as
    * Spring application `org.endeavourhealth.imapi.ImApiSpringApplication`
    * Under Tomcat `api/imapi.war`
