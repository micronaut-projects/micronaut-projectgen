package io.micronaut.projectgen.core.template;

import java.io.IOException;
import com.fizzed.rocker.ForIterator;
import com.fizzed.rocker.RenderingException;
import com.fizzed.rocker.RockerContent;
import com.fizzed.rocker.RockerOutput;
import com.fizzed.rocker.runtime.DefaultRockerTemplate;
import com.fizzed.rocker.runtime.PlainTextUnloadedClassLoader;

/*
 * Auto generated code to render template io/micronaut/projectgen/core/template/mavenRepository.rocker.raw
 * Do not edit this file. Changes will eventually be overwritten by Rocker parser!
 */
@SuppressWarnings("unused")
public class mavenRepository extends com.fizzed.rocker.runtime.DefaultRockerModel {

    static public com.fizzed.rocker.ContentType getContentType() { return com.fizzed.rocker.ContentType.RAW; }
    static public String getTemplateName() { return "mavenRepository.rocker.raw"; }
    static public String getTemplatePackageName() { return "io.micronaut.projectgen.core.template"; }
    static public String getHeaderHash() { return "988232872"; }
    static public long getModifiedAt() { return 1740582162644L; }
    static public String[] getArgumentNames() { return new String[] { "id", "url", "snapshot" }; }

    // argument @ [1:2]
    private String id;
    // argument @ [1:2]
    private String url;
    // argument @ [1:2]
    private boolean snapshot;

    public mavenRepository id(String id) {
        this.id = id;
        return this;
    }

    public String id() {
        return this.id;
    }

    public mavenRepository url(String url) {
        this.url = url;
        return this;
    }

    public String url() {
        return this.url;
    }

    public mavenRepository snapshot(boolean snapshot) {
        this.snapshot = snapshot;
        return this;
    }

    public boolean snapshot() {
        return this.snapshot;
    }

    static public mavenRepository template(String id, String url, boolean snapshot) {
        return new mavenRepository()
            .id(id)
            .url(url)
            .snapshot(snapshot);
    }

    @Override
    protected DefaultRockerTemplate buildTemplate() throws RenderingException {
        // optimized for convenience (runtime auto reloading enabled if rocker.reloading=true)
        return com.fizzed.rocker.runtime.RockerRuntime.getInstance().getBootstrap().template(this.getClass(), this);
    }

    static public class Template extends com.fizzed.rocker.runtime.DefaultRockerTemplate {

        // \n<repository>\n  <id>
        static private final byte[] PLAIN_TEXT_0_0;
        // </id>\n  <url>
        static private final byte[] PLAIN_TEXT_1_0;
        // </url>\n
        static private final byte[] PLAIN_TEXT_2_0;
        // \n  <snapshots>\n    <enabled>true</enabled>\n  </snapshots>\n  <releases>\n    <enabled>false</enabled>\n  </releases>\n
        static private final byte[] PLAIN_TEXT_3_0;
        // \n</repository>\n
        static private final byte[] PLAIN_TEXT_4_0;

        static {
            PlainTextUnloadedClassLoader loader = PlainTextUnloadedClassLoader.tryLoad(mavenRepository.class.getClassLoader(), mavenRepository.class.getName() + "$PlainText", "UTF-8");
            PLAIN_TEXT_0_0 = loader.tryGet("PLAIN_TEXT_0_0");
            PLAIN_TEXT_1_0 = loader.tryGet("PLAIN_TEXT_1_0");
            PLAIN_TEXT_2_0 = loader.tryGet("PLAIN_TEXT_2_0");
            PLAIN_TEXT_3_0 = loader.tryGet("PLAIN_TEXT_3_0");
            PLAIN_TEXT_4_0 = loader.tryGet("PLAIN_TEXT_4_0");
        }

        // argument @ [1:2]
        protected final String id;
        // argument @ [1:2]
        protected final String url;
        // argument @ [1:2]
        protected final boolean snapshot;

        public Template(mavenRepository model) {
            super(model);
            __internal.setCharset("UTF-8");
            __internal.setContentType(getContentType());
            __internal.setTemplateName(getTemplateName());
            __internal.setTemplatePackageName(getTemplatePackageName());
            this.id = model.id();
            this.url = model.url();
            this.snapshot = model.snapshot();
        }

        @Override
        protected void __doRender() throws IOException, RenderingException {
            // PlainText @ [1:47]
            __internal.aboutToExecutePosInTemplate(1, 47);
            __internal.writeValue(PLAIN_TEXT_0_0);
            // ValueExpression @ [3:7]
            __internal.aboutToExecutePosInTemplate(3, 7);
            __internal.renderValue(id, false);
            // PlainText @ [3:10]
            __internal.aboutToExecutePosInTemplate(3, 10);
            __internal.writeValue(PLAIN_TEXT_1_0);
            // ValueExpression @ [4:8]
            __internal.aboutToExecutePosInTemplate(4, 8);
            __internal.renderValue(url, false);
            // PlainText @ [4:12]
            __internal.aboutToExecutePosInTemplate(4, 12);
            __internal.writeValue(PLAIN_TEXT_2_0);
            // IfBlockBegin @ [5:1]
            __internal.aboutToExecutePosInTemplate(5, 1);
            if (snapshot) {
                // PlainText @ [5:16]
                __internal.aboutToExecutePosInTemplate(5, 16);
                __internal.writeValue(PLAIN_TEXT_3_0);
                // IfBlockEnd @ [5:1]
                __internal.aboutToExecutePosInTemplate(5, 1);
            } // if end @ [5:1]
            // PlainText @ [12:2]
            __internal.aboutToExecutePosInTemplate(12, 2);
            __internal.writeValue(PLAIN_TEXT_4_0);
        }
    }

    private static class PlainText {

        static private final String PLAIN_TEXT_0_0 = "\n<repository>\n  <id>";
        static private final String PLAIN_TEXT_1_0 = "</id>\n  <url>";
        static private final String PLAIN_TEXT_2_0 = "</url>\n";
        static private final String PLAIN_TEXT_3_0 = "\n  <snapshots>\n    <enabled>true</enabled>\n  </snapshots>\n  <releases>\n    <enabled>false</enabled>\n  </releases>\n";
        static private final String PLAIN_TEXT_4_0 = "\n</repository>\n";

    }

}
