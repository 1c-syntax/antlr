import java.util.Calendar
import org.jreleaser.model.Active.*

plugins {
    `java-library`
    `maven-publish`
    jacoco
    id("cloud.rio.license") version "0.18.0"
    id("me.qoomon.git-versioning") version "6.4.4"
    id("org.jreleaser") version "1.23.0"
    id("org.sonarqube") version "7.3.0.8198"
    id("io.freefair.lombok") version "9.4.0"
}

repositories {
    mavenLocal()
    mavenCentral()
}

group = "io.github.1c-syntax"
gitVersioning.apply {
    refs {
        describeTagFirstParent = false
        tag("v(?<tagVersion>[0-9].*)") {
            version = "\${ref.tagVersion}\${dirty}"
        }

        branch("develop") {
            version = "\${describe.tag.version.major}." +
                    "\${describe.tag.version.minor.next}.0." +
                    "\${describe.distance}-SNAPSHOT\${dirty}"
        }

        branch(".+") {
            version = "\${ref}-\${commit.short}\${dirty}"
        }
    }

    rev {
        version = "\${commit.short}\${dirty}"
    }
}

dependencies {
    implementation("org.antlr:ST4:4.3.4")
    implementation("commons-io:commons-io:2.22.0")
    implementation("org.abego.treelayout:org.abego.treelayout.core:1.0.3")

    implementation("io.github.1c-syntax:utils:0.7.0")
    api("org.jspecify:jspecify:1.0.0")

    compileOnly("com.ibm.icu:icu4j:78.3")

    testImplementation(platform("org.junit:junit-bom:6.0.3"))
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core:3.27.7")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
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

tasks.javadoc {
    exclude("dot/org/antlr/v4/**")
    exclude("**/**/*.g")
    exclude("**/**/*.g4")
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
    header = rootProject.file("license/HEADER.txt")
    strictCheck = true
    mapping("java", "SLASHSTAR_STYLE")
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

publishing {
    repositories {
        maven {
            name = "staging"
            url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

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
                issueManagement {
                    system.set("GitHub Issues")
                    url.set("https://github.com/1c-syntax/antlr/issues")
                }
                ciManagement {
                    system.set("GitHub Actions")
                    url.set("https://github.com/1c-syntax/antlr/actions")
                }
            }
        }
    }
}

jreleaser {
    signing {
        active = ALWAYS
        armored = true
    }
    deploy {
        maven {
            mavenCentral {
                create("release-deploy") {
                    active = RELEASE
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")
                }
            }
            nexus2 {
                create("snapshot-deploy") {
                    active = SNAPSHOT
                    snapshotUrl = "https://central.sonatype.com/repository/maven-snapshots/"
                    applyMavenCentralRules = true
                    snapshotSupported = true
                    closeRepository = true
                    releaseRepository = true
                    stagingRepository("build/staging-deploy")
                }
            }
        }
    }
}