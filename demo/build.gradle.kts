plugins {
    id("java")
    id("application")
    id("org.openrewrite.rewrite") version "7.7.0"
}
group = "io.micronaut.projectgen"
version = "1.0.0"
repositories {
    mavenLocal()
    mavenCentral()
}
dependencies {
    rewrite("io.micronaut.projectgen:test-suite-helloworld-openrewrite:0.0.1-SNAPSHOT")
}
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
tasks.test {
    useJUnitPlatform()
}
application {
    mainClass.set("com.example.HelloWorld")
}

