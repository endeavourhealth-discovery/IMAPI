# IMAPI
![Version](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild/badges/IMViewer-Spring/version.svg)
![Build Status](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild/badges/IMViewer-Spring/build.svg)
![Unit Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild/badges/IMViewer-Spring/unit-test.svg)


## Virtuoso maven install
```
mvn install:install-file -q -Dfile=/tmp/virt_rdf4j.jar -DgroupId=com.openlink.virtuoso -DartifactId=virt_rdf4j -Dversion=3.0 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -q -Dfile=/tmp/virtjdbc4.jar -DgroupId=com.openlink.virtuoso -DartifactId=virtjdbc4 -Dversion=4.0 -Dpackaging=jar -DgeneratePom=true
```
