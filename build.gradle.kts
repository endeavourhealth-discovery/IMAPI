plugins {
    // Support convention plugins written in Groovy. Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
    alias(libs.plugins.sonarqube)
    id("java-library")
    id("java")
    id("maven-publish")
}

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
}

val SONAR_LOGIN = System.getenv("SONAR_LOGIN") ?: "null"
println("Sonar = [$SONAR_LOGIN]")

sonar.properties {
    property("sonar.gradle.skipCompile", true)
    property("sonar.projectKey", "endeavourhealth-discovery_IMAPI")
    property("sonar.organization", "endeavourhealth-discovery")
    property("sonar.token", SONAR_LOGIN)
    property("sonar.host.url", "https://sonarcloud.io")
    property("sonar.junit.reportPaths", "build/test-results/test/binary")
    property("sonar.exclusions", "**/imapi/parser/**, **/imapi/transforms/**/eqd/")
    property(
        "sonar.coverage.exclusions",
        "**/imapi/config/**, **/imapi/controllers/**, **/imapi/dataaccess/**, **/imapi/errorhandling/**, **/imapi/filer/**, **/imapi/vocabulary/**, **/imapi/transforms/eqd/**"
    )
}

val ENV = System.getenv("ENV") ?: "dev"
println("Build environment = [$ENV]")
if (ENV == "prod") {
    tasks.build { finalizedBy("sonar") }
    tasks.build { finalizedBy("publish") }
}

allprojects {
    group = "org.endeavourhealth.imapi"
    version = "1.0-SNAPSHOT"

    apply(plugin = "java")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
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
}

tasks.register<Javadoc>("myJavadocs") {
    source = sourceSets["main"].allJava
}

