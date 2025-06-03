plugins {
    id("io.micronaut.build.internal.projectgen-module")
}
dependencies {
    implementation(platform(libs.rewrite.recipe.bom))
    implementation(project(":micronaut-projectgen-core"))
    implementation(libs.rewrite.java)
    implementation(libs.rewrite.java17)
    implementation(libs.rewrite.yaml)
    testImplementation(libs.rewrite.test)
    testImplementation(mnTest.junit.jupiter.api)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
    testImplementation(mnTest.junit.jupiter.params)
    testRuntimeOnly(mnLogging.logback.classic)
}
