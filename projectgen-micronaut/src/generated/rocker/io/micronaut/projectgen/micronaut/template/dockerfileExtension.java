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
import io.micronaut.projectgen.core.buildtools.Dockerfile;

/*
 * Auto generated code to render template io/micronaut/projectgen/micronaut/template/dockerfileExtension.rocker.raw
 * Do not edit this file. Changes will eventually be overwritten by Rocker parser!
 */
@SuppressWarnings("unused")
public class dockerfileExtension extends com.fizzed.rocker.runtime.DefaultRockerModel {

    static public com.fizzed.rocker.ContentType getContentType() { return com.fizzed.rocker.ContentType.RAW; }
    static public String getTemplateName() { return "dockerfileExtension.rocker.raw"; }
    static public String getTemplatePackageName() { return "io.micronaut.projectgen.micronaut.template"; }
    static public String getHeaderHash() { return "1715534742"; }
    static public String[] getArgumentNames() { return new String[] { "dsl", "dockerfile" }; }

    // argument @ [4:2]
    private GradleDsl dsl;
    // argument @ [4:2]
    private Dockerfile dockerfile;

    public dockerfileExtension dsl(GradleDsl dsl) {
        this.dsl = dsl;
        return this;
    }

    public GradleDsl dsl() {
        return this.dsl;
    }

    public dockerfileExtension dockerfile(Dockerfile dockerfile) {
        this.dockerfile = dockerfile;
        return this;
    }

    public Dockerfile dockerfile() {
        return this.dockerfile;
    }

    static public dockerfileExtension template(GradleDsl dsl, Dockerfile dockerfile) {
        return new dockerfileExtension()
            .dsl(dsl)
            .dockerfile(dockerfile);
    }

    @Override
    protected DefaultRockerTemplate buildTemplate() throws RenderingException {
        // optimized for performance (via rocker.optimize flag; no auto reloading)
        return new Template(this);
    }

    static public class Template extends com.fizzed.rocker.runtime.DefaultRockerTemplate {

        // \n
        static private final byte[] PLAIN_TEXT_0_0;
        //     baseImage = \"
        static private final byte[] PLAIN_TEXT_1_0;
        // \"\n
        static private final byte[] PLAIN_TEXT_2_0;
        //     args(\n
        static private final byte[] PLAIN_TEXT_3_0;
        //         \"
        static private final byte[] PLAIN_TEXT_4_0;
        // \"
        static private final byte[] PLAIN_TEXT_5_0;
        // ,
        static private final byte[] PLAIN_TEXT_6_0;
        //     )\n
        static private final byte[] PLAIN_TEXT_7_0;

        static {
            PlainTextUnloadedClassLoader loader = PlainTextUnloadedClassLoader.tryLoad(dockerfileExtension.class.getClassLoader(), dockerfileExtension.class.getName() + "$PlainText", "UTF-8");
            PLAIN_TEXT_0_0 = loader.tryGet("PLAIN_TEXT_0_0");
            PLAIN_TEXT_1_0 = loader.tryGet("PLAIN_TEXT_1_0");
            PLAIN_TEXT_2_0 = loader.tryGet("PLAIN_TEXT_2_0");
            PLAIN_TEXT_3_0 = loader.tryGet("PLAIN_TEXT_3_0");
            PLAIN_TEXT_4_0 = loader.tryGet("PLAIN_TEXT_4_0");
            PLAIN_TEXT_5_0 = loader.tryGet("PLAIN_TEXT_5_0");
            PLAIN_TEXT_6_0 = loader.tryGet("PLAIN_TEXT_6_0");
            PLAIN_TEXT_7_0 = loader.tryGet("PLAIN_TEXT_7_0");
        }

        // argument @ [4:2]
        protected final GradleDsl dsl;
        // argument @ [4:2]
        protected final Dockerfile dockerfile;

        public Template(dockerfileExtension model) {
            super(model);
            __internal.setCharset("UTF-8");
            __internal.setContentType(getContentType());
            __internal.setTemplateName(getTemplateName());
            __internal.setTemplatePackageName(getTemplatePackageName());
            this.dsl = model.dsl();
            this.dockerfile = model.dockerfile();
        }

        @Override
        protected void __doRender() throws IOException, RenderingException {
            // IfBlockBegin @ [5:1]
            __internal.aboutToExecutePosInTemplate(5, 1);
            if (dsl == GradleDsl.GROOVY) {
                // ValueExpression @ [6:1]
                __internal.aboutToExecutePosInTemplate(6, 1);
                __internal.renderValue(raw("tasks.named(\"dockerfile\") {\n"), false);
                // PlainText @ [6:40]
                __internal.aboutToExecutePosInTemplate(6, 40);
                __internal.writeValue(PLAIN_TEXT_0_0);
                // IfBlockElse @ [7:1]
                __internal.aboutToExecutePosInTemplate(7, 1);
            } else { // else @ [7:1]
                // ValueExpression @ [8:1]
                __internal.aboutToExecutePosInTemplate(8, 1);
                __internal.renderValue(raw("tasks.named<io.micronaut.gradle.docker.MicronautDockerfile>(\"dockerfile\") {\n"), false);
                // PlainText @ [8:88]
                __internal.aboutToExecutePosInTemplate(8, 88);
                __internal.writeValue(PLAIN_TEXT_0_0);
                // IfBlockEnd @ [5:1]
                __internal.aboutToExecutePosInTemplate(5, 1);
            } // if end @ [5:1]
            // IfBlockBegin @ [10:1]
            __internal.aboutToExecutePosInTemplate(10, 1);
            if (dockerfile.getBaseImage() != null) {
                // PlainText @ [10:41]
                __internal.aboutToExecutePosInTemplate(10, 41);
                __internal.writeValue(PLAIN_TEXT_1_0);
                // EvalExpression @ [11:18]
                __internal.aboutToExecutePosInTemplate(11, 18);
                __internal.renderValue((dockerfile.getBaseImage()), false);
                // PlainText @ [11:46]
                __internal.aboutToExecutePosInTemplate(11, 46);
                __internal.writeValue(PLAIN_TEXT_2_0);
                // IfBlockEnd @ [10:1]
                __internal.aboutToExecutePosInTemplate(10, 1);
            } // if end @ [10:1]
            // IfBlockBegin @ [13:1]
            __internal.aboutToExecutePosInTemplate(13, 1);
            if (dockerfile.getArgs() != null) {
                // PlainText @ [13:37]
                __internal.aboutToExecutePosInTemplate(13, 37);
                __internal.writeValue(PLAIN_TEXT_3_0);
                // ForBlockBegin @ [15:5]
                __internal.aboutToExecutePosInTemplate(15, 5);
                try {
                    for (int i = 0; i < dockerfile.getArgs().size(); i++) {
                        try {
                            // PlainText @ [15:61]
                            __internal.aboutToExecutePosInTemplate(15, 61);
                            __internal.writeValue(PLAIN_TEXT_4_0);
                            // EvalExpression @ [16:10]
                            __internal.aboutToExecutePosInTemplate(16, 10);
                            __internal.renderValue((dockerfile.getArgs().get(i)), false);
                            // PlainText @ [16:40]
                            __internal.aboutToExecutePosInTemplate(16, 40);
                            __internal.writeValue(PLAIN_TEXT_5_0);
                            // IfBlockBegin @ [16:41]
                            __internal.aboutToExecutePosInTemplate(16, 41);
                            if (i < (dockerfile.getArgs().size() -1)) {
                                // PlainText @ [16:85]
                                __internal.aboutToExecutePosInTemplate(16, 85);
                                __internal.writeValue(PLAIN_TEXT_6_0);
                                // IfBlockEnd @ [16:41]
                                __internal.aboutToExecutePosInTemplate(16, 41);
                            } // if end @ [16:41]
                            // PlainText @ [16:87]
                            __internal.aboutToExecutePosInTemplate(16, 87);
                            __internal.writeValue(PLAIN_TEXT_0_0);
                            // ForBlockEnd @ [15:5]
                            __internal.aboutToExecutePosInTemplate(15, 5);
                        } catch (com.fizzed.rocker.runtime.ContinueException e) {
                            // support for continuing for loops
                        }
                    } // for end @ [15:5]
                } catch (com.fizzed.rocker.runtime.BreakException e) {
                    // support for breaking for loops
                }
                // PlainText @ [17:6]
                __internal.aboutToExecutePosInTemplate(17, 6);
                __internal.writeValue(PLAIN_TEXT_7_0);
                // IfBlockEnd @ [13:1]
                __internal.aboutToExecutePosInTemplate(13, 1);
            } // if end @ [13:1]
            // ValueExpression @ [20:1]
            __internal.aboutToExecutePosInTemplate(20, 1);
            __internal.renderValue(raw("}\n"), false);
            // PlainText @ [20:12]
            __internal.aboutToExecutePosInTemplate(20, 12);
            __internal.writeValue(PLAIN_TEXT_0_0);
        }
    }

    private static class PlainText {

        static private final String PLAIN_TEXT_0_0 = "\n";
        static private final String PLAIN_TEXT_1_0 = "    baseImage = \"";
        static private final String PLAIN_TEXT_2_0 = "\"\n";
        static private final String PLAIN_TEXT_3_0 = "    args(\n";
        static private final String PLAIN_TEXT_4_0 = "        \"";
        static private final String PLAIN_TEXT_5_0 = "\"";
        static private final String PLAIN_TEXT_6_0 = ",";
        static private final String PLAIN_TEXT_7_0 = "    )\n";

    }

}
