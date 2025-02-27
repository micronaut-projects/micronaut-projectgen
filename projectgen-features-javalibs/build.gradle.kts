plugins {
    id("io.micronaut.build.internal.projectgen-module")
    id("nu.studer.rocker") version "3.0.4"
}
dependencies {
    api(project(":micronaut-projectgen-openrewrite"))
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
