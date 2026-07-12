plugins {
    id("io.micronaut.application") version "4.5.4"
    id("com.gradleup.shadow") version "8.3.11"
}
version = "0.1"
group = "io.micronaut.projectgen.demo"
repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.gradle.org/gradle/libs-releases")
    }
}
dependencies {
    annotationProcessor("info.picocli:picocli-codegen")
    implementation(project(":test-suite-helloworld"))
    implementation(project(":test-suite-helloworld-openrewrite"))
    implementation(project(":micronaut-projectgen-openrewrite-runner-gradle"))
    implementation(project(":micronaut-projectgen-openrewrite-runner-maven"))
    implementation("info.picocli:picocli")
    implementation("io.micronaut.picocli:micronaut-picocli")
    runtimeOnly("ch.qos.logback:logback-classic")
}
application {
    mainClass = "io.micronaut.projectgen.demo.MainCommand"
}
java {
    sourceCompatibility = JavaVersion.toVersion("25")
    targetCompatibility = JavaVersion.toVersion("25")
}

micronaut {
    version(libs.versions.micronaut.platform.get())
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("io.micronaut.projectgen.demo.*")
    }
}

