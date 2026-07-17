package io.micronaut.projectgen.demo;

import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.core.template.PropertiesTemplate;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
class ProjectGenPropertiesFeatures implements DefaultFeature {
    @Override
    public String getName() {
        return "project-gen-properties";
    }
    @Override
    public String getTitle() {
        return "Project Gen Properties";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getDescription() {
        return "It generates a properties file with the selected options when the project was created";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Options options = generatorContext.getOptions();
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate("projectgen.properties",
            new PropertiesTemplate("projectgen.properties", properties(options)));
    }

    private Map<String, Object> properties(Options options) {
        Map<String, Object> props = new HashMap<>();
        if (options.name() != null) {
            props.put("name", options.name());
        }
        if (options.operatingSystem() != null) {
            props.put("operatingSystem", options.operatingSystem());
        }
        if (!"DEFAULT".equals(options.template())) {
            props.put("template", options.template());
        }
        if (options.language() != Language.JAVA) {
            props.put("language", options.language());
        }
        if (options.buildTools() != null) {
            props.put("buildTools", options.buildTools());
        }
        if (options.configurationFormat() != null) {
            props.put("configurationFormat", options.configurationFormat());
        }
        if (options.gradleDsl() != null) {
            props.put("gradleDsl", options.gradleDsl());
        }
        if (options.group() != null) {
            props.put("group", options.group());
        }
        if (options.artifact() != null) {
            props.put("artifact", options.artifact());
        }
        if (options.java() != null) {
            props.put("java", options.java());
        }
        if (options.packageName() != null) {
            props.put("packageName", options.packageName());
        }
        if (options.version() != null) {
            props.put("version", options.version());
        }
        if (options.packaging() != null) {
            props.put("packaging", options.packaging());
        }
        if (options.testFramework() != TestFramework.DEFAULT_OPTION) {
            props.put("testFramework", options.testFramework());
        }
        return props;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return true;
    }
}
