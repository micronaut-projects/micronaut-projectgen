plugins {
    id("io.micronaut.build.internal.projectgen-base")
    id("io.micronaut.build.internal.bom")
}
micronautBuild {
    binaryCompatibility {
        enabled = false
    }
}
