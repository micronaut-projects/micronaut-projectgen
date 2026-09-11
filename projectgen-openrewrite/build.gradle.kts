plugins {
    id("io.micronaut.build.internal.projectgen-module")
}
dependencies {
    api(project(":micronaut-projectgen-core"))
    api(platform(libs.rewrite.recipe.bom))
    api(libs.rewrite.core)
    api(libs.rewrite.maven)
    api(libs.rewrite.gradle)
    api(libs.rewrite.java.dependencies) {
        exclude(group = "org.openrewrite", module = "rewrite-groovy")
    }
    api(libs.rewrite.java)
    api(libs.rewrite.java17)
    api(libs.rewrite.yaml)
    api(libs.rewrite.properties)
    constraints {
        // rewrite-core pins micrometer-core 1.9.17, which is affected by GHSA-g3pr-3p32-fp23
        api(libs.micrometer.core)
    }
    testImplementation(libs.rewrite.test)
    testImplementation(mnTest.junit.jupiter.api)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
    testImplementation(mnTest.junit.jupiter.params)
    testRuntimeOnly(mnLogging.logback.classic)

}
