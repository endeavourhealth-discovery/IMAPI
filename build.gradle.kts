import cz.habarta.typescript.generator.*

plugins {
  // Support convention plugins written in Groovy. Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
  alias(libs.plugins.sonar)
  id("java")
  id("jacoco")
  id("war")
  alias(libs.plugins.typescript.generator)
  alias(libs.plugins.static.const.generator)
  id("java-library")
  id("maven-publish")
}

group = "org.endeavourhealth.imapi"
version = "2.0-SNAPSHOT"
description = "Information Model API"


repositories {
  gradlePluginPortal()
}

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

val ENV = System.getenv("ENV") ?: "dev"
println("Build environment = [$ENV]")
if (ENV == "prod") {
  tasks.build { finalizedBy("sonar") }
  tasks.build { finalizedBy("publish") }
} else {
  tasks.named<JavaCompile>("compileJava") {
    dependsOn("staticConstGenerator")
  }
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
    }
  }
  repositories {
    maven {
      url = uri("https://artifactory.endhealth.co.uk/repository/maven-snapshots")
      credentials {
        username = System.getenv("MAVEN_USERNAME")
        password = System.getenv("MAVEN_PASSWORD")
      }
    }
  }
}


sonar {
  properties {
    property("sonar.token", System.getenv("SONAR_LOGIN"))
    property("sonar.host.url", "https://sonarcloud.io")
    property("sonar.organization", "endeavourhealth-discovery")
    property("sonar.projectKey", "IMAPI")
    property("sonar.projectName", "Information Model API")
    property("sonar.sources", "src/main/java")
    property("sonar.tests", "src/test/java")
    property("sonar.junit.reportPaths", "build/test-results/test")
    property("sonar.exclusions", "**/parser/**, **/transforms/**/eqd/")
    property(
      "sonar.coverage.exclusions",
      "**/config/**, **/controllers/**, **/dataaccess/**, **/errorhandling/**, **/filer/**, **/vocabulary/**, **/transforms/eqd/**"
    )

  }
}

tasks.war {
  archiveFileName.set("imapi.war")
}

tasks.generateTypeScript {
  jsonLibrary = JsonLibrary.jackson2
  outputFileType = TypeScriptFileType.implementationFile
  optionalProperties = OptionalProperties.useLibraryDefinition
  classPatterns = listOf(
    "org.endeavourhealth.imapi.model.DataModelProperty",
    "org.endeavourhealth.imapi.model.iml.*",
    "org.endeavourhealth.imapi.model.search.*",
    "org.endeavourhealth.imapi.model.set.EclSearchRequest",
    "org.endeavourhealth.imapi.model.set.SetOptions",
    "org.endeavourhealth.imapi.model.set.SetExportRequest",
    "org.endeavourhealth.imapi.model.imq.*",
    "org.endeavourhealth.imapi.model.eclBuilder.*",
    "org.endeavourhealth.imapi.vocabulary.*",
    "org.endeavourhealth.imapi.vocabulary.**.*",
    "org.endeavourhealth.imapi.model.github.*",
    "org.endeavourhealth.imapi.model.workflow.*",
    "org.endeavourhealth.imapi.model.workflow.**.*",
    "org.endeavourhealth.imapi.model.DownloadEntityOptions",
    "org.endeavourhealth.imapi.model.EntityReferenceNode",
    "org.endeavourhealth.imapi.model.tripletree.TTDocument",
    "org.endeavourhealth.imapi.model.tripletree.TTEntity",
    "org.endeavourhealth.imapi.model.Pageable",
    "org.endeavourhealth.imapi.model.ConceptContextMap",
    "org.endeavourhealth.imapi.model.validation.EntityValidationRequest",
    "org.endeavourhealth.imapi.model.tripletree.TTDocument",
    "org.endeavourhealth.imapi.model.ConceptContextMap",
    "org.endeavourhealth.imapi.model.dto.CodeGenDto",
    "org.endeavourhealth.imapi.model.editor.*"
  )
  outputFile = "../IMDirectory/src/interfaces/AutoGen.ts"
  outputKind = TypeScriptOutputKind.module
  mapEnum = EnumMapping.asEnum
}

tasks {
  staticConstGenerator {
    inputJson = "vocab.json"
    javaOutputFolder = "src/main/java/org/endeavourhealth/imapi/vocabulary/"
    typeScriptOutputFolder = "../IMDirectory/src/vocabulary/"
  }
}

dependencies {
  implementation(libs.angus.mail)
  implementation(libs.antlr)
  implementation(libs.apache.collections4)
  implementation(libs.apache.poi)
  implementation(libs.apache.text)
  implementation(libs.apache.commons.text)
  implementation(libs.assert.j)
  implementation(libs.aws.cognito.idp)
  implementation(libs.aws.sdk.bom)
  implementation(libs.aws.sdk.core)
  implementation(libs.aws.s3)
  implementation(libs.dropwizard)
  implementation(libs.dropwizard.graphite)
  implementation(libs.dropwizard.servlets)
  implementation(libs.fact.plus.plus)
  implementation(libs.jackson.databind)
  implementation(libs.logback.core)
  implementation(libs.logback.classic)
  implementation(libs.elasticsearch)
  implementation(libs.hapi.fhir.r4)
  implementation(libs.jersey.client)
  implementation(libs.jersey.inject)
  implementation(libs.owl.api)
  implementation(libs.open.llet)
  implementation(libs.postgres)
  implementation(libs.reactor.core)
  implementation(libs.rdf4j.common)
  implementation(libs.rdf4j.query)
  implementation(libs.rdf4j.iterator)
  implementation(libs.rdf4j.repo.api)
  implementation(libs.rdf4j.repo.http)
  implementation(libs.rdf4j.repo.sail)
  implementation(libs.rdf4j.sail.native)
  implementation(libs.slf4j)
  implementation(libs.spring.context)
  implementation(libs.spring.data.jpa)
  implementation(libs.spring.oauth.server)
  implementation(libs.spring.security)
  implementation(libs.spring.web)
  implementation(libs.springdoc)
  implementation(libs.validation)
  implementation(libs.woodstox)
  implementation(libs.wsrs)
  implementation(libs.mysqlConncector)

  runtimeOnly(libs.h2database)
  runtimeOnly(libs.spring.dev.tools)

  testImplementation(libs.cucumber)
  testImplementation(libs.cucumber.junit)
  testImplementation(libs.junit)
  testImplementation(libs.junit.suite)
  testImplementation(libs.mockito)
  testImplementation(libs.spring.test)
  testImplementation(libs.spring.test.security)
  testImplementation(libs.system.stubs)

  providedCompile(libs.spring.tomcat)

  compileOnly(libs.jackson.annotations)
  compileOnly(libs.lombok)

  annotationProcessor(libs.jackson.annotations)
  annotationProcessor(libs.lombok)
}

repositories {
  mavenLocal()
  mavenCentral()
  maven {
    url = uri("https://artifactory.endhealth.co.uk/repository/maven-releases")
  }
  maven {
    url = uri("https://artifactory.endhealth.co.uk/repository/maven-snapshots")
  }
}

tasks.test {
  jvmArgs("-XX:+EnableDynamicAgentLoading")
  useJUnitPlatform()
  finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
  reports {
    xml.required.set(true)
  }
}


