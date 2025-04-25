package io.micronaut.projectgen.test.mavenmultimodule;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.buildtools.maven.ParentPom;
import io.micronaut.projectgen.core.buildtools.maven.ParentPomBuilder;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import multimodule.springBootDependencyManagement;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SpringBootDependencies {
    private static final String GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT = "org.springframework.boot";
    public static final Dependency SPRING_BOOT_STARTER_TEST = Dependency.builder()
        .groupId(GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
        .artifactId("spring-boot-starter-test")
        .test()
        .build();
    public static final Dependency SPRING_BOOT = Dependency.builder()
        .groupId(GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
        .artifactId("spring-boot")
        .compile()
        .build();
    public static final Supplier<GradlePlugin.Builder> SPRING_DEPENDENCY_MANAGEMENT_GRADLE_PLUGIN_BUILDER = () -> GradlePlugin.builder()
    .id("io.spring.dependency-management")
    .version("1.1.5");
    public static final String SPRING_BOOT_VERSION = "3.3.0";
    public static final GradlePlugin.Builder SPRING_BOOT_PLUGIN_BUILDER = GradlePlugin.builder()
        .id(GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
        .version(SPRING_BOOT_VERSION);
    public static final GradlePlugin JAVA_GRADLE_PLUGIN = GradlePlugin.builder()
        .id("java")
        .build();
    public static final ParentPom SPRING_BOOT_PARENT_POM = ParentPomBuilder
        .builder()
        .groupId(GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
        .artifactId("spring-boot-starter-parent")
        .version(SPRING_BOOT_VERSION)
        .relativePath("")
        .build();
    public static final @NonNull MavenPlugin SPRING_BOOT_MAVEN_PLUGIN = MavenPlugin.builder()
        .groupId("org.springframework.boot")
        .artifactId("spring-boot-maven-plugin")
        .build();
    public static final Dependency.Builder SPRING_BOOT_STARTER_WEB = Dependency.builder()
        .groupId("org.springframework.boot")
        .artifactId("spring-boot-starter-web")
        .compile();
    public static final Dependency SPRING_BOOT_STARTER_ACTUATOR = Dependency.builder()
        .groupId("org.springframework.boot")
        .artifactId("spring-boot-starter-actuator")
        .compile().build();
}
