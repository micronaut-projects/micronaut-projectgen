/*
 * Copyright 2017-2025 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.projectgen.test;

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.maven.MavenScope;
import io.micronaut.projectgen.core.buildtools.maven.ParentPom;
import io.micronaut.projectgen.core.buildtools.maven.Profile;
import io.micronaut.projectgen.core.options.Language;
import org.jspecify.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * {@link BuildTestVerifier} for Maven builds.
 */
public class MavenBuildTestVerifier implements BuildTestVerifier {
    public static final Scope MAVEN_DEFAULT_SCOPE = Scope.COMPILE;
    @Nullable
    private ParentPom parentPom;
    private final List<Profile> profiles = new ArrayList<>();
    private final List<Coordinate> buildPlugins  = new ArrayList<>();
    private final Map<String, String> properties = new HashMap<>();
    private final List<Dependency> dependencies = new ArrayList<>();
    private final List<Dependency> annotationProcessors = new ArrayList<>();

    private final Language language;

    public MavenBuildTestVerifier(@NonNull String content, Language language) {
        readXMLFile(content);
        this.language = language;
    }

    private void readXMLFile(String xmlContent) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlContent));
            Document document = builder.parse(is);
            document.getDocumentElement().normalize();

            Element projectElement = document.getDocumentElement();
            if (!"project".equals(projectElement.getTagName())) {
                throw new ConfigurationException("Pom does not have a project element");
            }
            this.parentPom = parseParent(projectElement);
            this.profiles.addAll(parseProfiles(projectElement));
            this.dependencies.addAll(parseDependencies(projectElement));
            this.properties.putAll(parseProperties(projectElement));
            this.buildPlugins.addAll(parseBuildPlugins(projectElement));
            this.annotationProcessors.addAll(parseAnnotationProcessors(projectElement));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private ParentPom parseParent(Element projectElement) {
        NodeList parentNodes = projectElement.getElementsByTagName("parent");
        if (parentNodes.getLength() == 0) {
            return null;
        }

        Element parentElement = (Element) parentNodes.item(0);

        String groupId = getElementText(parentElement, "groupId");
        String artifactId = getElementText(parentElement, "artifactId");
        String version = getElementText(parentElement, "version");
        String relativePath = getElementText(parentElement, "relativePath");

        return new ParentPom(groupId, Objects.requireNonNull(artifactId), version, relativePath);
    }

    private List<Coordinate> parseBuildPlugins(Element projectElement) {
        final List<Coordinate> buildPlugins  = new ArrayList<>();
        NodeList buildNodes = projectElement.getElementsByTagName("build");
        if (buildNodes.getLength() > 0) {
            Element buildElement = (Element) buildNodes.item(0);
            NodeList pluginNodes = buildElement.getElementsByTagName("plugin");
            for (int i = 0; i < pluginNodes.getLength(); i++) {
                Element pluginElement = (Element) pluginNodes.item(i);
                String groupId = getElementText(pluginElement, "groupId");
                String artifactId = getElementText(pluginElement, "artifactId");
                buildPlugins.add(Dependency.builder()
                    .groupId(groupId)
                    .artifactId(Objects.requireNonNull(artifactId))
                    .build());
            }
        }
        return buildPlugins;
    }

    private Map<String, String> parseProperties(Element projectElement) {
        Map<String, String> properties = new HashMap<>();
        NodeList propertiesNodes = projectElement.getElementsByTagName("properties");
        if (propertiesNodes.getLength() > 0) {
            Element propertiesElement = (Element) propertiesNodes.item(0);
            NodeList propertyNodes = propertiesElement.getChildNodes();
            for (int i = 0; i < propertyNodes.getLength(); i++) {
                Node node = propertyNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    String propertyName = node.getNodeName();
                    String propertyValue = node.getTextContent();
                    properties.put(propertyName, propertyValue);
                }
            }
        }
        return properties;
    }

    private List<Dependency> parseAnnotationProcessors(Element projectElement) {
        List<Dependency> result = new ArrayList<>();

        NodeList plugins = projectElement.getElementsByTagName("plugin");
        for (int i = 0; i < plugins.getLength(); i++) {
            Element plugin = (Element) plugins.item(i);
            String artifactId = getElementText(plugin, "artifactId");

            if ("maven-compiler-plugin".equals(artifactId)) {
                // Find annotationProcessorPaths element
                NodeList configNodes = plugin.getElementsByTagName("configuration");
                if (configNodes.getLength() > 0) {
                    Element config = (Element) configNodes.item(0);
                    NodeList processorPathsNodes = config.getElementsByTagName("annotationProcessorPaths");
                    if (processorPathsNodes.getLength() > 0) {
                        Element processorPaths = (Element) processorPathsNodes.item(0);
                        NodeList pathNodes = processorPaths.getElementsByTagName("path");

                        // Process each path element
                        for (int j = 0; j < pathNodes.getLength(); j++) {
                            Element path = (Element) pathNodes.item(j);
                            String groupId = getElementText(path, "groupId");
                            String pathArtifactId = getElementText(path, "artifactId");
                            result.add(Dependency.builder()
                                    .groupId(groupId)
                                    .artifactId(Objects.requireNonNull(pathArtifactId))
                                .build());
                        }
                    }
                }
            }
        }


        return result;
    }

    private static List<Profile> parseProfiles(Element projectElement) {
        List<Profile> result = new ArrayList<>();
        NodeList profilesNodes = projectElement.getElementsByTagName("profiles");
        if (profilesNodes.getLength() > 0) {
            Element profilesElement = (Element) profilesNodes.item(0);
            NodeList profileNodes = profilesElement.getElementsByTagName("profile");

            for (int i = 0; i < profileNodes.getLength(); i++) {
                Element profileElement = (Element) profileNodes.item(i);
                String id = getElementText(profileElement, "id");
                result.add(Profile.builder()
                    .id(Objects.requireNonNull(id))
                    .build());
            }
        }
        return result;
    }

    private List<Dependency> parseDependencies(Element projectElement) {
        List<Dependency> result = new ArrayList<>();
        NodeList dependenciesNodes = projectElement.getElementsByTagName("dependencies");
        if (dependenciesNodes.getLength() > 0) {
            Element dependenciesElement = (Element) dependenciesNodes.item(0);
            NodeList dependencyNodes = dependenciesElement.getElementsByTagName("dependency");

            for (int i = 0; i < dependencyNodes.getLength(); i++) {
                Element dependencyElement = (Element) dependencyNodes.item(i);
                String groupId = getElementText(dependencyElement, "groupId");
                String artifactId = getElementText(dependencyElement, "artifactId");
                String version = getElementText(dependencyElement, "version");
                String scope = getElementText(dependencyElement, "scope");
                Optional<MavenScope> mavenScope = scope == null ? Optional.empty() : MavenScope.of(scope);
                Dependency.Builder dependencyBuilder = Dependency.builder();
                dependencyBuilder
                    .groupId(groupId)
                    .artifactId(Objects.requireNonNull(artifactId))
                    .version(version);
                mavenScope.flatMap(MavenScope::toScope).ifPresent(dependencyBuilder::scope);
                result.add(dependencyBuilder.build());
            }
        }
        return result;
    }

    // Helper method to get text content of an element
    @Nullable
    private static String getElementText(Element parentElement, String tagName) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

    @Override
    @Nullable
    public String getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    @Override
    public boolean hasBuildPlugin(String groupId, String artifactId) {
        return this.buildPlugins.stream()
            .anyMatch(bp ->
                    matchCoordinateGroupIdAndArtifactId(bp, groupId, artifactId));
    }

    @Override
    public boolean hasAnnotationProcessor(String groupId, String artifactId) {
        return annotationProcessors.stream()
            .anyMatch(d -> matchCoordinateGroupIdAndArtifactId(d, groupId, artifactId));
    }

    @Override
    public boolean hasTestAnnotationProcessor(String groupId, String artifactId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasBom(String groupId, String artifactId, Scope scope) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasBom(String groupId, String artifactId, String scope) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasDependency(String groupId, String artifactId, Scope scope) {
        if (scope.equals(MAVEN_DEFAULT_SCOPE) && dependencies.stream().anyMatch(d ->
            matchCoordinateGroupIdAndArtifactId(d, groupId, artifactId) && d.getScope() == null)) {
            return true;
        }
        return dependencies.stream().anyMatch(d ->
            matchCoordinateGroupIdAndArtifactId(d, groupId, artifactId) && (d.getScope() != null && d.getScope().equals(scope)));
    }

    /**
     *
     * @param groupId Group ID
     * @param artifactId Artifact ID
     * @param scope Scope
     * @return Whether the build has a dependency with the supplied groupId, artifactId and scope
     */
    public boolean hasDependency(String groupId, String artifactId, MavenScope scope) {
        return dependencies.stream().anyMatch(d ->
            matchCoordinateGroupIdAndArtifactId(d, groupId, artifactId) && matchesMavenScope(d, scope));
    }

    private boolean matchesMavenScope(Dependency dependency, MavenScope scope) {
        Scope dependencyScope = dependency.getScope();
        return dependencyScope != null && MavenScope.of(dependencyScope, language)
            .map(scope::equals)
            .orElse(false);
    }

    @Override
    public boolean hasDependency(String groupId, String artifactId, String scope) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasDependency(String groupId, String artifactId, Scope scope, String version, boolean isProperty) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasDependency(String groupId, String artifactId, String scope, String version, boolean isProperty) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasDependency(String groupId, String artifactId) {
        return dependencies.stream().anyMatch(d ->
                matchCoordinateGroupIdAndArtifactId(d, groupId, artifactId));
    }

    @Override
    public boolean hasExclusion(String groupId, String artifactId, String excludedGroupId, String excludedArtifactId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasExclusion(String groupId, String artifactId, String excludedGroupId, String excludedArtifactId, Scope scope) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasTestResourceDependency(String groupId, String artifactId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasTestResourceDependency(String artifactId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasDependency(String artifactId) {
        return dependencies.stream().anyMatch(d -> d.getArtifactId().equals(artifactId));
    }

    @Override
    public boolean hasTestResourceDependencyWithGroupId(String expectedGroupId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasBuildPlugin(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasParentPom(String groupId, String artifactId) {
        if (parentPom == null) {
            return false;
        }
        return Objects.equals(parentPom.groupId(), groupId) && parentPom.artifactId().equals(artifactId);
    }

    @Override
    public boolean hasProfile(String profileId) {
        return profiles.stream().map(Profile::getId).anyMatch(id -> id.equals(profileId));
    }
}
