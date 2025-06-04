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
    implementation(project(":micronaut-projectgen-core"))
    testImplementation(project(":micronaut-projectgen-test"))
    implementation("info.picocli:picocli")
    implementation("io.micronaut.picocli:micronaut-picocli")
    runtimeOnly("ch.qos.logback:logback-classic")
}
application {
    mainClass = "io.micronaut.projectgen.demo.ProjectGenCommand"
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}

micronaut {
    version("4.8.2")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("io.micronaut.projectgen.demo.*")
    }
}

