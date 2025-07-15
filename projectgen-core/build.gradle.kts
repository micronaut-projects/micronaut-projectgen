plugins {
    id("io.micronaut.build.internal.projectgen-module")
    id("nu.studer.rocker") version "3.0.4"
}
dependencies {
    // Serialization
    annotationProcessor(mnSerde.micronaut.serde.processor)
    implementation(mnSerde.micronaut.serde.jackson)

    // Validation
    annotationProcessor(mnValidation.micronaut.validation.processor)
    testAnnotationProcessor(mnValidation.micronaut.validation.processor)
    implementation(mnValidation.micronaut.validation)

    // SourceGen
    annotationProcessor(mnSourcegen.micronaut.sourcegen.generator.java)
    implementation(mnSourcegen.micronaut.sourcegen.annotations)

    api(mnViews.rocker.runtime)
    implementation(libs.apache.commons.compress)
    compileOnly(libs.java.diff.utils)
    testAnnotationProcessor(mn.micronaut.inject.java)
    testImplementation(mnTest.micronaut.test.junit5)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
    testImplementation(mnTest.junit.jupiter.params)
    compileOnly(libs.snakeyaml)
    compileOnly(libs.typesafeconfig)
    testImplementation(libs.java.diff.utils)
    implementation(libs.commons.lang3)
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

val generateCoordinateUtils = tasks.register<io.micronaut.projectgen.coordinates.CoordinatesSourceGenerator>("generateCoordinateUtils") {
    packageName.set("io.micronaut.projectgen.build.dependencies")
    outputDirectory.set(layout.buildDirectory.dir("generated-sources/coordinates"))
    versionCatalog.set(project.extensions.getByType<VersionCatalogsExtension>().named("templateLibs"))
    lineSeparator.set(System.lineSeparator())
}

sourceSets["main"].java.srcDir(generateCoordinateUtils)
