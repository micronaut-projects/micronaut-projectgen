package io.micronaut.projectgen.core.template;

import java.io.IOException;
import com.fizzed.rocker.ForIterator;
import com.fizzed.rocker.RenderingException;
import com.fizzed.rocker.RockerContent;
import com.fizzed.rocker.RockerOutput;
import com.fizzed.rocker.runtime.DefaultRockerTemplate;
import com.fizzed.rocker.runtime.PlainTextUnloadedClassLoader;
// import @ [1:1]
import io.micronaut.projectgen.core.buildtools.maven.ParentPom;

/*
 * Auto generated code to render template io/micronaut/projectgen/core/template/parentPom.rocker.raw
 * Do not edit this file. Changes will eventually be overwritten by Rocker parser!
 */
@SuppressWarnings("unused")
public class parentPom extends com.fizzed.rocker.runtime.DefaultRockerModel {

    static public com.fizzed.rocker.ContentType getContentType() { return com.fizzed.rocker.ContentType.RAW; }
    static public String getTemplateName() { return "parentPom.rocker.raw"; }
    static public String getTemplatePackageName() { return "io.micronaut.projectgen.core.template"; }
    static public String getHeaderHash() { return "1388494246"; }
    static public long getModifiedAt() { return 1740582722088L; }
    static public String[] getArgumentNames() { return new String[] { "parentPom" }; }

    // argument @ [3:2]
    private ParentPom parentPom;

    public parentPom parentPom(ParentPom parentPom) {
        this.parentPom = parentPom;
        return this;
    }

    public ParentPom parentPom() {
        return this.parentPom;
    }

    static public parentPom template(ParentPom parentPom) {
        return new parentPom()
            .parentPom(parentPom);
    }

    @Override
    protected DefaultRockerTemplate buildTemplate() throws RenderingException {
        // optimized for convenience (runtime auto reloading enabled if rocker.reloading=true)
        return com.fizzed.rocker.runtime.RockerRuntime.getInstance().getBootstrap().template(this.getClass(), this);
    }

    static public class Template extends com.fizzed.rocker.runtime.DefaultRockerTemplate {

        // \n\n  <parent>\n     <groupId>
        static private final byte[] PLAIN_TEXT_0_0;
        // </groupId>\n     <artifactId>
        static private final byte[] PLAIN_TEXT_1_0;
        // </artifactId>\n     <version>
        static private final byte[] PLAIN_TEXT_2_0;
        // </version>\n
        static private final byte[] PLAIN_TEXT_3_0;
        // \n     <relativePath/> <!-- lookup parent from repository -->\n
        static private final byte[] PLAIN_TEXT_4_0;
        // \n  </parent>\n
        static private final byte[] PLAIN_TEXT_5_0;

        static {
            PlainTextUnloadedClassLoader loader = PlainTextUnloadedClassLoader.tryLoad(parentPom.class.getClassLoader(), parentPom.class.getName() + "$PlainText", "UTF-8");
            PLAIN_TEXT_0_0 = loader.tryGet("PLAIN_TEXT_0_0");
            PLAIN_TEXT_1_0 = loader.tryGet("PLAIN_TEXT_1_0");
            PLAIN_TEXT_2_0 = loader.tryGet("PLAIN_TEXT_2_0");
            PLAIN_TEXT_3_0 = loader.tryGet("PLAIN_TEXT_3_0");
            PLAIN_TEXT_4_0 = loader.tryGet("PLAIN_TEXT_4_0");
            PLAIN_TEXT_5_0 = loader.tryGet("PLAIN_TEXT_5_0");
        }

        // argument @ [3:2]
        protected final ParentPom parentPom;

        public Template(parentPom model) {
            super(model);
            __internal.setCharset("UTF-8");
            __internal.setContentType(getContentType());
            __internal.setTemplateName(getTemplateName());
            __internal.setTemplatePackageName(getTemplatePackageName());
            this.parentPom = model.parentPom();
        }

        @Override
        protected void __doRender() throws IOException, RenderingException {
            // PlainText @ [3:28]
            __internal.aboutToExecutePosInTemplate(3, 28);
            __internal.writeValue(PLAIN_TEXT_0_0);
            // EvalExpression @ [6:15]
            __internal.aboutToExecutePosInTemplate(6, 15);
            __internal.renderValue((parentPom.groupId()), false);
            // PlainText @ [6:37]
            __internal.aboutToExecutePosInTemplate(6, 37);
            __internal.writeValue(PLAIN_TEXT_1_0);
            // EvalExpression @ [7:18]
            __internal.aboutToExecutePosInTemplate(7, 18);
            __internal.renderValue((parentPom.artifactId()), false);
            // PlainText @ [7:43]
            __internal.aboutToExecutePosInTemplate(7, 43);
            __internal.writeValue(PLAIN_TEXT_2_0);
            // EvalExpression @ [8:15]
            __internal.aboutToExecutePosInTemplate(8, 15);
            __internal.renderValue((parentPom.version()), false);
            // PlainText @ [8:37]
            __internal.aboutToExecutePosInTemplate(8, 37);
            __internal.writeValue(PLAIN_TEXT_3_0);
            // IfBlockBegin @ [9:1]
            __internal.aboutToExecutePosInTemplate(9, 1);
            if (parentPom.relativePath()) {
                // PlainText @ [9:33]
                __internal.aboutToExecutePosInTemplate(9, 33);
                __internal.writeValue(PLAIN_TEXT_4_0);
                // IfBlockEnd @ [9:1]
                __internal.aboutToExecutePosInTemplate(9, 1);
            } // if end @ [9:1]
            // PlainText @ [11:2]
            __internal.aboutToExecutePosInTemplate(11, 2);
            __internal.writeValue(PLAIN_TEXT_5_0);
        }
    }

    private static class PlainText {

        static private final String PLAIN_TEXT_0_0 = "\n\n  <parent>\n     <groupId>";
        static private final String PLAIN_TEXT_1_0 = "</groupId>\n     <artifactId>";
        static private final String PLAIN_TEXT_2_0 = "</artifactId>\n     <version>";
        static private final String PLAIN_TEXT_3_0 = "</version>\n";
        static private final String PLAIN_TEXT_4_0 = "\n     <relativePath/> <!-- lookup parent from repository -->\n";
        static private final String PLAIN_TEXT_5_0 = "\n  </parent>\n";

    }

}
