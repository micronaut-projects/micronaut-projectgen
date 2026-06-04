import net.ltgt.gradle.errorprone.errorprone

plugins {
    id("io.micronaut.build.internal.projectgen-module")
    id("nu.studer.rocker") version "3.3.1"
}
dependencies {
    api(project(":micronaut-projectgen-core"))
    api(project(":micronaut-projectgen-micronaut-openrewrite"))
    annotationProcessor(mnSourcegen.micronaut.sourcegen.generator.java)
    implementation(mnSourcegen.micronaut.sourcegen.annotations)
    implementation(libs.snakeyaml)
    implementation(libs.typesafeconfig)
    implementation("io.micronaut.testresources:micronaut-test-resources-build-tools:4.0.0-M1")
    implementation(mn.micronaut.http.client)
    testAnnotationProcessor(mn.micronaut.inject.java)
    testImplementation(mnTest.micronaut.test.junit5)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
    testImplementation(mnTest.junit.jupiter.params)
    testImplementation(project(":micronaut-projectgen-test"))
    testRuntimeOnly(mnLogging.logback.classic)
}
rocker {
    configurations {
        create("main") {
            optimize.set(true)
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
tasks.withType<JavaCompile>().configureEach {
    options.errorprone.option("NullAway:UnannotatedSubPackages", "io.micronaut.projectgen.micronaut.template")
}
