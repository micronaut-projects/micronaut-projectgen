package io.micronaut.projectgen.micronaut.template;

import java.io.IOException;
import com.fizzed.rocker.ForIterator;
import com.fizzed.rocker.RenderingException;
import com.fizzed.rocker.RockerContent;
import com.fizzed.rocker.RockerOutput;
import com.fizzed.rocker.runtime.DefaultRockerTemplate;
import com.fizzed.rocker.runtime.PlainTextUnloadedClassLoader;
// import @ [2:1]
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
// import @ [3:1]
import java.util.Map;

/*
 * Auto generated code to render template io/micronaut/projectgen/micronaut/template/aotExtension.rocker.raw
 * Do not edit this file. Changes will eventually be overwritten by Rocker parser!
 */
@SuppressWarnings("unused")
public class aotExtension extends com.fizzed.rocker.runtime.DefaultRockerModel {

    static public com.fizzed.rocker.ContentType getContentType() { return com.fizzed.rocker.ContentType.RAW; }
    static public String getTemplateName() { return "aotExtension.rocker.raw"; }
    static public String getTemplatePackageName() { return "io.micronaut.projectgen.micronaut.template"; }
    static public String getHeaderHash() { return "1938534268"; }
    static public String[] getArgumentNames() { return new String[] { "aotKeys" }; }

    // argument @ [4:2]
    private Map<String,String> aotKeys;

    public aotExtension aotKeys(Map<String,String> aotKeys) {
        this.aotKeys = aotKeys;
        return this;
    }

    public Map<String,String> aotKeys() {
        return this.aotKeys;
    }

    static public aotExtension template(Map<String,String> aotKeys) {
        return new aotExtension()
            .aotKeys(aotKeys);
    }

    @Override
    protected DefaultRockerTemplate buildTemplate() throws RenderingException {
        // optimized for performance (via rocker.optimize flag; no auto reloading)
        return new Template(this);
    }

    static public class Template extends com.fizzed.rocker.runtime.DefaultRockerTemplate {

        //     aot {\n        // Please review carefully the optimizations enabled below\n        // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details\n        optimizeServiceLoading = false\n        convertYamlToJava = false\n        precomputeOperations = true\n        cacheEnvironment = true\n        optimizeClassLoading = true\n        deduceEnvironment = true\n        optimizeNetty = true\n        replaceLogbackXml = true\n
        static private final byte[] PLAIN_TEXT_0_0;
        //         configurationProperties.put(\"
        static private final byte[] PLAIN_TEXT_1_0;
        // \",\"
        static private final byte[] PLAIN_TEXT_2_0;
        // \")\n
        static private final byte[] PLAIN_TEXT_3_0;
        //     }\n
        static private final byte[] PLAIN_TEXT_4_0;

        static {
            PlainTextUnloadedClassLoader loader = PlainTextUnloadedClassLoader.tryLoad(aotExtension.class.getClassLoader(), aotExtension.class.getName() + "$PlainText", "UTF-8");
            PLAIN_TEXT_0_0 = loader.tryGet("PLAIN_TEXT_0_0");
            PLAIN_TEXT_1_0 = loader.tryGet("PLAIN_TEXT_1_0");
            PLAIN_TEXT_2_0 = loader.tryGet("PLAIN_TEXT_2_0");
            PLAIN_TEXT_3_0 = loader.tryGet("PLAIN_TEXT_3_0");
            PLAIN_TEXT_4_0 = loader.tryGet("PLAIN_TEXT_4_0");
        }

        // argument @ [4:2]
        protected final Map<String,String> aotKeys;

        public Template(aotExtension model) {
            super(model);
            __internal.setCharset("UTF-8");
            __internal.setContentType(getContentType());
            __internal.setTemplateName(getTemplateName());
            __internal.setTemplatePackageName(getTemplatePackageName());
            this.aotKeys = model.aotKeys();
        }

        @Override
        protected void __doRender() throws IOException, RenderingException {
            // PlainText @ [4:35]
            __internal.aboutToExecutePosInTemplate(4, 35);
            __internal.writeValue(PLAIN_TEXT_0_0);
            // IfBlockBegin @ [16:1]
            __internal.aboutToExecutePosInTemplate(16, 1);
            if (aotKeys != null) {
                // ForBlockBegin @ [17:1]
                __internal.aboutToExecutePosInTemplate(17, 1);
                try {
                    final com.fizzed.rocker.runtime.IterableForIterator<String> __forIterator0 = new com.fizzed.rocker.runtime.IterableForIterator<String>(aotKeys.keySet());
                    while (__forIterator0.hasNext()) {
                        final String keyName = __forIterator0.next();
                        try {
                            // PlainText @ [17:43]
                            __internal.aboutToExecutePosInTemplate(17, 43);
                            __internal.writeValue(PLAIN_TEXT_1_0);
                            // EvalExpression @ [18:38]
                            __internal.aboutToExecutePosInTemplate(18, 38);
                            __internal.renderValue((keyName), false);
                            // PlainText @ [18:48]
                            __internal.aboutToExecutePosInTemplate(18, 48);
                            __internal.writeValue(PLAIN_TEXT_2_0);
                            // EvalExpression @ [18:51]
                            __internal.aboutToExecutePosInTemplate(18, 51);
                            __internal.renderValue((aotKeys.get(keyName)), false);
                            // PlainText @ [18:74]
                            __internal.aboutToExecutePosInTemplate(18, 74);
                            __internal.writeValue(PLAIN_TEXT_3_0);
                            // ForBlockEnd @ [17:1]
                            __internal.aboutToExecutePosInTemplate(17, 1);
                        } catch (com.fizzed.rocker.runtime.ContinueException e) {
                            // support for continuing for loops
                        }
                    } // for end @ [17:1]
                } catch (com.fizzed.rocker.runtime.BreakException e) {
                    // support for breaking for loops
                }
                // IfBlockEnd @ [16:1]
                __internal.aboutToExecutePosInTemplate(16, 1);
            } // if end @ [16:1]
            // PlainText @ [20:2]
            __internal.aboutToExecutePosInTemplate(20, 2);
            __internal.writeValue(PLAIN_TEXT_4_0);
        }
    }

    private static class PlainText {

        static private final String PLAIN_TEXT_0_0 = "    aot {\n        // Please review carefully the optimizations enabled below\n        // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details\n        optimizeServiceLoading = false\n        convertYamlToJava = false\n        precomputeOperations = true\n        cacheEnvironment = true\n        optimizeClassLoading = true\n        deduceEnvironment = true\n        optimizeNetty = true\n        replaceLogbackXml = true\n";
        static private final String PLAIN_TEXT_1_0 = "        configurationProperties.put(\"";
        static private final String PLAIN_TEXT_2_0 = "\",\"";
        static private final String PLAIN_TEXT_3_0 = "\")\n";
        static private final String PLAIN_TEXT_4_0 = "    }\n";

    }

}
