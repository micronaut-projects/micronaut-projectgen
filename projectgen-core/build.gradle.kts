plugins {
    id("io.micronaut.build.internal.projectgen-module")
    id("nu.studer.rocker") version "3.0.4"
}
dependencies {
    annotationProcessor(mnSourcegen.micronaut.sourcegen.generator.java)
    implementation(mnSourcegen.micronaut.sourcegen.annotations)
    implementation(libs.rocker.runtime)
    implementation(libs.apache.commons.compress)
    annotationProcessor(mnSerde.micronaut.serde.processor)
    implementation(mnSerde.micronaut.serde.jackson)
    testImplementation(mnTest.micronaut.test.junit5)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
    testImplementation(mnTest.junit.jupiter.params)
}
rocker {
    configurations {
        create("main") {
            templateDir.set(file("src/rocker"))
            outputDir.set(file("src/generated/rocker"))
        }
    }
}

spotless {
    java {
        targetExclude("src/**/*.rocker.raw")
    }
}
