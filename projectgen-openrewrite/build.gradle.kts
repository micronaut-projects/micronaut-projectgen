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
    implementation(libs.rewrite.properties)
}
