plugins {
    id("io.micronaut.application") version "4.5.3"
    id("com.gradleup.shadow") version "8.3.6"
    id("io.micronaut.aot") version "4.5.3"
}

version = "0.1"
group = "io.micronaut.monolith"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":micronaut-projectgen-http-server"))

    annotationProcessor("io.micronaut:micronaut-http-validation")

    // HTML
    implementation("io.micronaut.views:micronaut-views-htmx")

    // SourceGen
    annotationProcessor(mnSourcegen.micronaut.sourcegen.generator.java)
    implementation(mnSourcegen.micronaut.sourcegen.annotations)

    // Validation
    annotationProcessor("io.micronaut.validation:micronaut-validation-processor")
    implementation("io.micronaut.validation:micronaut-validation")

    // Serialization
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut.serde:micronaut-serde-jackson")

    // Views
    implementation("io.micronaut.views:micronaut-views-fieldset")
    implementation("io.micronaut.views:micronaut-views-thymeleaf")

    // Feature differ
    implementation(libs.java.diff.utils)


    runtimeOnly("ch.qos.logback:logback-classic")

    compileOnly("io.micronaut:micronaut-http-client")
    testImplementation("io.micronaut:micronaut-http-client")
}

application {
    mainClass = "io.micronaut.monolith.Application"
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}


graalvmNative.toolchainDetection = false

micronaut {
    version("4.8.2")
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("io.micronaut.monolith.*")
    }
    aot {
        // Please review carefully the optimizations enabled below
        // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading = false
        convertYamlToJava = false
        precomputeOperations = true
        cacheEnvironment = true
        optimizeClassLoading = true
        deduceEnvironment = true
        optimizeNetty = true
        replaceLogbackXml = true
    }
}


tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}


