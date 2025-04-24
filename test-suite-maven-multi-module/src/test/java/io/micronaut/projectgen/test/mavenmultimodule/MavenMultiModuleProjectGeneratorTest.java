package io.micronaut.projectgen.test.mavenmultimodule;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.OptionsImpl;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class MavenMultiModuleProjectGeneratorTest {

    @Test
    void testMavenMultiModule(MavenMultiModuleProjectGenerator projectGenerator) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        Options options = OptionsImpl.builder()
            .buildTool(BuildTool.MAVEN)
            .name("org.springframework.gs-multi-module")
            .build();
        projectGenerator.generate(options, outputHandler);
        Map<String, String> project = outputHandler.getProject();
        assertTrue(project.keySet().contains("pom.xml"));
        assertTrue(project.keySet().contains("library/pom.xml"), project.keySet().toString());
        String rootPom = project.get("pom.xml");
        assertNotNull(rootPom);
//        assertEquals("""
//            <?xml version="1.0" encoding="UTF-8"?>
//            <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
//                xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
//                <modelVersion>4.0.0</modelVersion>
//
//                <groupId>org.springframework</groupId>
//                <artifactId>gs-multi-module</artifactId>
//                <version>0.1.0</version>
//                <packaging>pom</packaging>
//
//                <modules>
//                    <module>library</module>
//                    <module>application</module>
//                </modules>
//
//            </project>""", rootPom, rootPom);

        String libraryPom = project.get("library/pom.xml");
        assertNotNull(libraryPom);
        System.out.println(libraryPom);
    }
}
