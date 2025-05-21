package io.micronaut.projectgen.core.utils;
import java.util.HashMap;
import java.util.Map;

public class PrismLanguageHelper {

    private static final Map<String, String> EXTENSION_TO_LANGUAGE_CLASS = new HashMap<>();

    static {
        // Core languages
        EXTENSION_TO_LANGUAGE_CLASS.put("properties", "language-properties");
        EXTENSION_TO_LANGUAGE_CLASS.put("html", "language-markup");
        EXTENSION_TO_LANGUAGE_CLASS.put("xml", "language-markup");
        EXTENSION_TO_LANGUAGE_CLASS.put("svg", "language-markup");
        EXTENSION_TO_LANGUAGE_CLASS.put("mathml", "language-markup");
        EXTENSION_TO_LANGUAGE_CLASS.put("css", "language-css");
        EXTENSION_TO_LANGUAGE_CLASS.put("js", "language-javascript");
        EXTENSION_TO_LANGUAGE_CLASS.put("mjs", "language-javascript");
        EXTENSION_TO_LANGUAGE_CLASS.put("ts", "language-typescript");
        EXTENSION_TO_LANGUAGE_CLASS.put("json", "language-json");
        EXTENSION_TO_LANGUAGE_CLASS.put("java", "language-java");
        EXTENSION_TO_LANGUAGE_CLASS.put("kt", "language-kotlin");
        EXTENSION_TO_LANGUAGE_CLASS.put("kts", "language-kotlin");
        EXTENSION_TO_LANGUAGE_CLASS.put("gradle.kts", "language-kotlin");
        EXTENSION_TO_LANGUAGE_CLASS.put("py", "language-python");
        EXTENSION_TO_LANGUAGE_CLASS.put("rb", "language-ruby");
        EXTENSION_TO_LANGUAGE_CLASS.put("php", "language-php");
        EXTENSION_TO_LANGUAGE_CLASS.put("c", "language-c");
        EXTENSION_TO_LANGUAGE_CLASS.put("cpp", "language-cpp");
        EXTENSION_TO_LANGUAGE_CLASS.put("cs", "language-csharp");
        EXTENSION_TO_LANGUAGE_CLASS.put("go", "language-go");
        EXTENSION_TO_LANGUAGE_CLASS.put("rs", "language-rust");
        EXTENSION_TO_LANGUAGE_CLASS.put("swift", "language-swift");
        EXTENSION_TO_LANGUAGE_CLASS.put("scala", "language-scala");
        EXTENSION_TO_LANGUAGE_CLASS.put("sh", "language-bash");
        EXTENSION_TO_LANGUAGE_CLASS.put("bash", "language-bash");
        EXTENSION_TO_LANGUAGE_CLASS.put("zsh", "language-bash");
        EXTENSION_TO_LANGUAGE_CLASS.put("yml", "language-yaml");
        EXTENSION_TO_LANGUAGE_CLASS.put("yaml", "language-yaml");
        EXTENSION_TO_LANGUAGE_CLASS.put("md", "language-markdown");
        EXTENSION_TO_LANGUAGE_CLASS.put("markdown", "language-markdown");
        EXTENSION_TO_LANGUAGE_CLASS.put("sql", "language-sql");
        EXTENSION_TO_LANGUAGE_CLASS.put("xml", "language-markup");
        EXTENSION_TO_LANGUAGE_CLASS.put("ini", "language-ini");
        EXTENSION_TO_LANGUAGE_CLASS.put("toml", "language-toml");
        EXTENSION_TO_LANGUAGE_CLASS.put("dockerfile", "language-docker");
        EXTENSION_TO_LANGUAGE_CLASS.put("makefile", "language-makefile");
        EXTENSION_TO_LANGUAGE_CLASS.put("gradle", "language-groovy");
        EXTENSION_TO_LANGUAGE_CLASS.put("groovy", "language-groovy");
        EXTENSION_TO_LANGUAGE_CLASS.put("dart", "language-dart");
        EXTENSION_TO_LANGUAGE_CLASS.put("h", "language-c");
        EXTENSION_TO_LANGUAGE_CLASS.put("hpp", "language-cpp");
        EXTENSION_TO_LANGUAGE_CLASS.put("hxx", "language-cpp");
        EXTENSION_TO_LANGUAGE_CLASS.put("pl", "language-perl");
        EXTENSION_TO_LANGUAGE_CLASS.put("pm", "language-perl");
        EXTENSION_TO_LANGUAGE_CLASS.put("r", "language-r");
        EXTENSION_TO_LANGUAGE_CLASS.put("tex", "language-latex");
        EXTENSION_TO_LANGUAGE_CLASS.put("coffee", "language-coffeescript");
        EXTENSION_TO_LANGUAGE_CLASS.put("litcoffee", "language-coffeescript");
        EXTENSION_TO_LANGUAGE_CLASS.put("scss", "language-scss");
        EXTENSION_TO_LANGUAGE_CLASS.put("less", "language-less");
        EXTENSION_TO_LANGUAGE_CLASS.put("vue", "language-markup");
        EXTENSION_TO_LANGUAGE_CLASS.put("svelte", "language-markup");
        EXTENSION_TO_LANGUAGE_CLASS.put("jsx", "language-jsx");
        EXTENSION_TO_LANGUAGE_CLASS.put("tsx", "language-tsx");
        EXTENSION_TO_LANGUAGE_CLASS.put("vb", "language-vbnet");
        EXTENSION_TO_LANGUAGE_CLASS.put("asm", "language-nasm");
        EXTENSION_TO_LANGUAGE_CLASS.put("s", "language-nasm");
        EXTENSION_TO_LANGUAGE_CLASS.put("ps1", "language-powershell");
        EXTENSION_TO_LANGUAGE_CLASS.put("bat", "language-batch");
        EXTENSION_TO_LANGUAGE_CLASS.put("cmd", "language-batch");
        EXTENSION_TO_LANGUAGE_CLASS.put("clj", "language-clojure");
        EXTENSION_TO_LANGUAGE_CLASS.put("cljs", "language-clojure");
        EXTENSION_TO_LANGUAGE_CLASS.put("cljc", "language-clojure");
        EXTENSION_TO_LANGUAGE_CLASS.put("edn", "language-clojure");
        EXTENSION_TO_LANGUAGE_CLASS.put("erl", "language-erlang");
        EXTENSION_TO_LANGUAGE_CLASS.put("ex", "language-elixir");
        EXTENSION_TO_LANGUAGE_CLASS.put("exs", "language-elixir");
        EXTENSION_TO_LANGUAGE_CLASS.put("hs", "language-haskell");
        EXTENSION_TO_LANGUAGE_CLASS.put("lhs", "language-haskell");
        EXTENSION_TO_LANGUAGE_CLASS.put("ml", "language-ocaml");
        EXTENSION_TO_LANGUAGE_CLASS.put("mli", "language-ocaml");
        EXTENSION_TO_LANGUAGE_CLASS.put("fs", "language-fsharp");
        EXTENSION_TO_LANGUAGE_CLASS.put("fsi", "language-fsharp");
        EXTENSION_TO_LANGUAGE_CLASS.put("fsx", "language-fsharp");
        EXTENSION_TO_LANGUAGE_CLASS.put("fsscript", "language-fsharp");
        EXTENSION_TO_LANGUAGE_CLASS.put("lisp", "language-lisp");
        EXTENSION_TO_LANGUAGE_CLASS.put("scm", "language-scheme");
        EXTENSION_TO_LANGUAGE_CLASS.put("lua", "language-lua");
        EXTENSION_TO_LANGUAGE_CLASS.put("nim", "language-nim");
        EXTENSION_TO_LANGUAGE_CLASS.put("pas", "language-pascal");
        EXTENSION_TO_LANGUAGE_CLASS.put("pp", "language-pascal");
        EXTENSION_TO_LANGUAGE_CLASS.put("plsql", "language-plsql");
        EXTENSION_TO_LANGUAGE_CLASS.put("sql", "language-sql");
        EXTENSION_TO_LANGUAGE_CLASS.put("vb", "language-vbnet");
        EXTENSION_TO_LANGUAGE_CLASS.put("vbs", "language-vbnet");
        EXTENSION_TO_LANGUAGE_CLASS.put("ts", "language-typescript");
        EXTENSION_TO_LANGUAGE_CLASS.put("tsx", "language-tsx");
        EXTENSION_TO_LANGUAGE_CLASS.put("jsx", "language-jsx");
        EXTENSION_TO_LANGUAGE_CLASS.put("xhtml", "language-markup");
        EXTENSION_TO_LANGUAGE_CLASS.put("xsd", "language-markup");
        EXTENSION_TO_LANGUAGE_CLASS.put("xsl", "language-markup");
        EXTENSION_TO_LANGUAGE_CLASS.put("xslt", "language-markup");
        EXTENSION_TO_LANGUAGE_CLASS.put("yaml", "language-yaml");
        EXTENSION_TO_LANGUAGE_CLASS.put("yml", "language-yaml");
        EXTENSION_TO_LANGUAGE_CLASS.put("zsh", "language-bash");
        // Add more mappings as needed
    }

    /**
     * Returns the Prism.js language class for a given filename.
     *
     * @param filename The name of the file.
     * @return The corresponding Prism.js language class, or "language-none" if unknown.
     */
    public static String getPrismLanguageClass(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "language-none";
        }

        String lowerCaseFilename = filename.toLowerCase();

        // Check for exact matches first (e.g., "build.gradle.kts")
        if (EXTENSION_TO_LANGUAGE_CLASS.containsKey(lowerCaseFilename)) {
            return EXTENSION_TO_LANGUAGE_CLASS.get(lowerCaseFilename);
        }

        // Extract the extension
        int lastDotIndex = lowerCaseFilename.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex < lowerCaseFilename.length() - 1) {
            String extension = lowerCaseFilename.substring(lastDotIndex + 1);
            return EXTENSION_TO_LANGUAGE_CLASS.getOrDefault(extension, "language-none");
        }

        return "language-none";
    }
}
