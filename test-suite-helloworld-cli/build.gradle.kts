plugins {
    id("io.micronaut.application") version "4.5.3"
    id("com.gradleup.shadow") version "8.3.6"
}
version = "0.1"
group = "io.micronaut.projectgen.demo"
repositories {
    mavenCentral()
}
dependencies {
    annotationProcessor("info.picocli:picocli-codegen")
    implementation(project(":test-suite-helloworld"))
    implementation("info.picocli:picocli")
    implementation("io.micronaut.picocli:micronaut-picocli")
    runtimeOnly("ch.qos.logback:logback-classic")
}
application {
    mainClass = "io.micronaut.projectgen.demo.ProjectGenCommand"
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
    targetCompatibility = JavaVersion.toVersion("17")
}

micronaut {
    version(libs.versions.micronaut.platform.get())
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("io.micronaut.projectgen.demo.*")
    }
}

