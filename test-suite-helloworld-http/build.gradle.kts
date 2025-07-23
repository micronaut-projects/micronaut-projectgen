plugins {
    id("io.micronaut.application") version "4.5.4"
    id("com.gradleup.shadow") version "8.3.8"
}
version = "0.1"
group = "io.micronaut.projectgen.demo"
repositories {
    mavenCentral()
}
dependencies {
    implementation(project(":micronaut-projectgen-http-server"))
    implementation(project(":test-suite-helloworld"))
    annotationProcessor("io.micronaut:micronaut-http-validation")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    compileOnly("io.micronaut:micronaut-http-client")
    runtimeOnly("ch.qos.logback:logback-classic")
    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("org.skyscreamer:jsonassert:1.5.3")
    implementation(libs.java.diff.utils)
}
application {
    mainClass = "io.micronaut.projectgen.demo.Application"
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
    targetCompatibility = JavaVersion.toVersion("17")
}
micronaut {
    version(libs.versions.micronaut.platform.get())
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("io.micronaut.projectgen.demo.*")
    }
}
