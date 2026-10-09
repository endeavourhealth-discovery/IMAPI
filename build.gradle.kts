import java.util.zip.ZipFile

plugins {
  // Support convention plugins written in Groovy. Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
  alias(libs.plugins.sonar)
  id("java")
  id("jacoco")
  id("war")
  alias(libs.plugins.static.const.generator)
  id("java-library")
  id("maven-publish")
  kotlin("jvm")
  kotlin("plugin.spring") version "2.2.20"
}

group = "org.endeavourhealth.imapi"
version = "2.1-SNAPSHOT"
description = "Information Model API"


repositories {
  gradlePluginPortal()
  mavenCentral()
  maven {
    url = uri("https://artifactory.endhealth.co.uk/repository/maven-releases")
  }
  maven {
    url = uri("https://artifactory.endhealth.co.uk/repository/maven-snapshots")
  }
  mavenLocal()
}

val ENV = System.getenv("ENV") ?: "dev"
println("Build environment = [$ENV]")

val CI = System.getenv("CI") ?: "false"
if (CI == "false") {
  tasks.named<JavaCompile>("compileJava") {
    dependsOn("staticConstGenerator")
  }
} else {
  tasks.build { finalizedBy("safeSonar") }
  tasks.build { finalizedBy("publish") }
}

tasks.register("safeSonar") {
  //Action block
  doLast {
    try {
      sonar
    } catch (e: Error) {
      throw StopActionException(e.toString())
    }
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

  // spring-boot-devtools is only for running from the IDE, so leave just that jar out of the war. Do not declare it
  // providedRuntime instead: the war task drops everything in providedRuntime *and its transitive dependencies*, which
  // removed spring-boot, spring-boot-autoconfigure, spring-core and others, so Tomcat deployed the war and never started Spring.
  classpath = classpath?.filter { !it.name.startsWith("spring-boot-devtools") }

  doLast {
    val entries = ZipFile(archiveFile.get().asFile).use { zip -> zip.entries().asSequence().map { it.name }.toList() }
    val required = mapOf(
      "spring-boot" to Regex("""WEB-INF/lib/spring-boot-\d.*\.jar"""),
      "spring-boot-autoconfigure" to Regex("""WEB-INF/lib/spring-boot-autoconfigure-.*\.jar"""),
      "spring-core" to Regex("""WEB-INF/lib/spring-core-.*\.jar"""),
      "spring-context" to Regex("""WEB-INF/lib/spring-context-.*\.jar"""),
      "spring-web" to Regex("""WEB-INF/lib/spring-web-.*\.jar"""),
    )
    val missing = required.filterValues { pattern -> entries.none { pattern.matches(it) } }.keys
    if (missing.isNotEmpty()) throw GradleException("imapi.war is missing required jars $missing; Tomcat would deploy it but never start Spring")
    if (entries.any { it.startsWith("WEB-INF/lib/spring-boot-devtools") }) throw GradleException("imapi.war must not contain spring-boot-devtools")
  }
}

tasks {
  staticConstGenerator {
    inputJson = "vocab.json"
    javaOutputFolder = "src/main/java/org/endeavourhealth/imapi/vocabulary/"
  }
}

dependencies {
  implementation(libs.angus.mail)
  implementation(libs.antlr)
  implementation(libs.apache.collections4)
  implementation(libs.apache.poi)
  implementation(libs.apache.text)
  implementation(libs.caffeine)
  implementation(libs.lucene.analyzers.common)
  implementation(libs.aws.sdk.bom)
  implementation(libs.aws.sdk.core)
  implementation(libs.aws.s3)
  implementation(libs.dropwizard)
  implementation(libs.dropwizard.graphite)
  implementation(libs.dropwizard.servlets)
  implementation(libs.fact.plus.plus)
  implementation(libs.jackson.databind)
  implementation(libs.jackson.kotlin)
  implementation(libs.logback.core)
  implementation(libs.logback.classic)
  implementation(libs.hapi.fhir.r4)
  implementation(libs.jersey.client)
  implementation(libs.jersey.inject)
  implementation(libs.owl.api)
  implementation(libs.open.llet)
  implementation(libs.rdf4j.common)
  implementation(libs.rdf4j.query)
  implementation(libs.rdf4j.iterator)
  implementation(libs.rdf4j.repo.api)
  implementation(libs.rdf4j.repo.http)
  implementation(libs.rdf4j.repo.sail)
  implementation(libs.rdf4j.sail.native)
  implementation(libs.slf4j)
  implementation(libs.spring.context)
  implementation(libs.spring.oauth.server)
  implementation(libs.spring.security)
  implementation(libs.spring.web)
  implementation(libs.springdoc)
  implementation(libs.validation)
  implementation(libs.woodstox)
  implementation(libs.wsrs)

  runtimeOnly(libs.spring.dev.tools)

  testImplementation(libs.assert.j)
  testImplementation(libs.cucumber)
  testImplementation(libs.cucumber.junit)
  testImplementation(libs.cucumber.spring)
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

tasks.test {
  jvmArgs("-XX:+EnableDynamicAgentLoading")
  useJUnitPlatform {
    excludeTags("IMQTest", "IMQFullTest", "IMQQOFQueriesTest", "IMQSMHQueriesTest", "IMQREGQueriesTest")
  }
  if (CI != "false") {
    finalizedBy("jacocoTestReport")
  }
}

tasks.register("imqTests", Test::class.java) {
  testClassesDirs = sourceSets["test"].output.classesDirs
  classpath = sourceSets["test"].runtimeClasspath
  useJUnitPlatform {
    includeTags("IMQTest")
  }
}

tasks.register("imqQOFQueriesTest", Test::class.java) {
  testClassesDirs = sourceSets["test"].output.classesDirs
  classpath = sourceSets["test"].runtimeClasspath
  useJUnitPlatform {
    includeTags("IMQQOFQueriesTest")
  }
}

tasks.register("imqSMHQueriesTest", Test::class.java) {
  testClassesDirs = sourceSets["test"].output.classesDirs
  classpath = sourceSets["test"].runtimeClasspath
  useJUnitPlatform {
    includeTags("IMQSMHQueriesTest")
  }
}

tasks.register("imqREGQueriesTest", Test::class.java) {
  testClassesDirs = sourceSets["test"].output.classesDirs
  classpath = sourceSets["test"].runtimeClasspath
  useJUnitPlatform {
    includeTags("IMQREGQueriesTest")
  }
}

tasks.jacocoTestReport {
  reports {
    xml.required.set(true)
  }
}


kotlin {
  jvmToolchain(21)
}
configurations.all {
  // log4j-core clashes with the log4j-to-slf4j bridge (log4j API -> logback). The Elasticsearch client used to pull it in; kept so it cannot return transitively.
  exclude(group = "org.apache.logging.log4j", module = "log4j-core")
  // icu4j (14 MB) is only used by org.hl7.fhir.utilities.i18n.I18nBase for plural rules in validation/rendering messages,
  // which IMAPI never uses (it only builds and encodes FHIR resources). FhirContextHolderTest covers that path.
  exclude(group = "com.ibm.icu", module = "icu4j")
}
