plugins {
    `java-library`
    `maven-publish`
    jacoco
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
}

group = "io.github.1c-syntax"
version = "4.13.2.1"
java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
    api("org.abego.treelayout", "org.abego.treelayout.core", "1.0.3")
    api("org.antlr", "antlr-runtime", "3.5.3")
    api("org.antlr", "ST4", "4.3.4")
    compileOnly("com.ibm.icu", "icu4j", "58.3")

    testImplementation("junit", "junit", "4.12")
}

java {
    withSourcesJar()
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:unchecked")
    options.compilerArgs.add("-Xlint:deprecation")
}

tasks.javadoc {
    options.encoding = "UTF-8"
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