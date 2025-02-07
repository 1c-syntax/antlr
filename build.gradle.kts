import me.qoomon.gitversioning.commons.GitRefType
import java.util.*

plugins {
    `java-library`
    `maven-publish`
    jacoco
    signing
    id("org.sonarqube") version "6.0.1.5171"
    id("org.cadixdev.licenser") version "0.6.1"
    id("me.qoomon.git-versioning") version "6.4.4"
    id("io.codearte.nexus-staging") version "0.30.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
}

group = "io.github.1c-syntax"
gitVersioning.apply {
    refs {
        considerTagsOnBranches = true
        tag("v(?<tagVersion>[0-9].*)") {
            version = "\${ref.tagVersion}\${dirty}"
        }
        branch(".+") {
            version = "\${ref}-\${commit.short}\${dirty}"
        }
    }

    rev {
        version = "\${commit.short}\${dirty}"
    }
}
val isSnapshot = gitVersioning.gitVersionDetails.refType != GitRefType.TAG

dependencies {
    api("org.antlr", "antlr-runtime", "3.5.3")
    api("org.antlr", "ST4", "4.3.4")
    compileOnly("com.ibm.icu", "icu4j", "58.3")
    implementation("org.abego.treelayout", "org.abego.treelayout.core", "1.0.3")

    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.11.4")
    testImplementation("org.junit.jupiter", "junit-jupiter-engine", "5.11.4")
    testImplementation("org.junit.jupiter", "junit-jupiter-params", "5.11.4")
    testImplementation("org.assertj", "assertj-core", "3.27.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:unchecked")
    options.compilerArgs.add("-Xlint:deprecation")
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
    isFailOnError = false
    //todo разбор и корректировка javadoc не самая приоритетная задача, в будущем исправлю
    (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:none", true)
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed", "standard_error")
    }

    reports {
        html.required.set(true)
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        xml.outputLocation.set(File("${layout.buildDirectory.get()}/reports/jacoco/test/jacoco.xml"))
    }
}

var curYear = Calendar.getInstance().get(Calendar.YEAR).toString()
if ("2025" != curYear) {
    curYear = "2025-$curYear"
}
license {
    header(rootProject.file("license/HEADER.txt"))
    newLine(false)
    ext["year"] = curYear
    ext["name"] = "Valery Maximov <maximovvalery@gmail.com>"
    ext["project"] = "ANTLR"
    include("**/*.java")
}

sonar {
    properties {
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "1c-syntax")
        property("sonar.projectKey", "1c-syntax_antlr")
        property("sonar.projectName", "ANTLR")
        property("sonar.scm.exclusions.disabled", "true")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${layout.buildDirectory.get()}/reports/jacoco/test/jacoco.xml"
        )
    }
}

artifacts {
    archives(tasks["jar"])
    archives(tasks["sourcesJar"])
    archives(tasks["javadocJar"])
}

signing {
    val signingInMemoryKey: String? by project      // env.ORG_GRADLE_PROJECT_signingInMemoryKey
    val signingInMemoryPassword: String? by project // env.ORG_GRADLE_PROJECT_signingInMemoryPassword
    if (signingInMemoryKey != null) {
        useInMemoryPgpKeys(signingInMemoryKey, signingInMemoryPassword)
        sign(publishing.publications)
    }
}

publishing {
    repositories {
        maven {
            name = "sonatype"
            url = if (isSnapshot)
                uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            else
                uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")

            val sonatypeUsername: String? by project
            val sonatypePassword: String? by project

            credentials {
                username = sonatypeUsername // ORG_GRADLE_PROJECT_sonatypeUsername
                password = sonatypePassword // ORG_GRADLE_PROJECT_sonatypePassword
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            if (isSnapshot && project.hasProperty("simplifyVersion")) {
                version = findProperty("git.ref.slug") as String + "-SNAPSHOT"
            }

            pom {
                name.set(project.name)
                description.set("ANTLR4 special fork")
                url.set("https://github.com/1c-syntax/antlr")
                licenses {
                    license {
                        name.set("BSD-3-Clause license")
                        url.set("https://opensource.org/license/bsd-3-clause")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("theshadowco")
                        name.set("Valery Maximov")
                        email.set("maximovvalery@gmail.com")
                        url.set("https://github.com/theshadowco")
                        organization.set("1c-syntax")
                        organizationUrl.set("https://github.com/1c-syntax")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/1c-syntax/antlr.git")
                    developerConnection.set("scm:git:git@github.com:1c-syntax/antlr.git")
                    url.set("https://github.com/1c-syntax/antlr")
                }
            }
        }
    }
}

nexusStaging {
    serverUrl = "https://s01.oss.sonatype.org/service/local/"
    stagingProfileId = "15bd88b4d17915" // ./gradlew getStagingProfile
}
