package io.micronaut.monolith;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.json.JsonMapper;
import io.micronaut.projectgen.core.diff.FeatureDiffer;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.io.TreeNode;
import io.micronaut.projectgen.core.io.TreeNodeGenerator;
import io.micronaut.projectgen.core.io.zip.ZipGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.CodeSample;
import io.micronaut.projectgen.http.server.DefaultDownloadHttpResponseGenerator;
import io.micronaut.projectgen.http.server.DownloadHttpResponseGenerator;
import io.micronaut.projectgen.http.server.OptionsBuilder;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.htmx.http.HtmxRequestUtils;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Replaces(DownloadHttpResponseGenerator.class)
@Singleton
public class HtmxAwareDownloadHttpResponseGenerator extends DefaultDownloadHttpResponseGenerator implements DownloadHttpResponseGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(HtmxAwareDownloadHttpResponseGenerator.class);

    private final TreeNodeGenerator treeNodeGenerator;
    private final JsonMapper jsonMapper;
    public HtmxAwareDownloadHttpResponseGenerator(OptionsBuilder optionsBuilder,
                                                  ZipGenerator zipGenerator,
                                                  FeatureDiffer featureDiffer,
                                                  PreviewGenerator previewGenerator,
                                                  TreeNodeGenerator treeNodeGenerator,
                                                  JsonMapper jsonMapper) {
        super(optionsBuilder, zipGenerator, previewGenerator, featureDiffer);
        this.treeNodeGenerator = treeNodeGenerator;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public HttpResponse<?> generate(HttpRequest<?> request, Map<String, Object> form) {
        Options options = optionsBuilder.createOptions(form);
        Optional<String> actionOptional = parseAction(form);
        if (actionOptional.isEmpty()) {
            return HttpResponse.unprocessableEntity();
        }
        String action = actionOptional.get();
        if (HtmxRequestUtils.isHtmxRequest(request)) {
            if (action.equalsIgnoreCase(ACTION_DIFF)) {
                try {
                    Map<String, Object> model = Map.of(
                        "diff", featureDiffer.diff(options),
                        "form", form
                    );
                    return HttpResponse.ok(new ModelAndView<>("modal/diff.html",model));
                } catch (Exception e) {
                    return HttpResponse.unprocessableEntity();
                }
            } else if (action.equalsIgnoreCase(ACTION_PREVIEW)) {
                try {
                    Map<String, String> project = previewGenerator.generate(options);
                    TreeNode treeNode = treeNodeGenerator.generate(project);
                    List<CodeSample> codeSamples = CodeSample.of(project);
                    String treeJson = jsonMapper.writeValueAsString(treeNode.children());
                    return HttpResponse.ok(new ModelAndView<>("modal/preview.html",
                        Map.of(
                            "tree", treeNode,
                            "codeSamples", codeSamples,
                            "treeJson", treeJson
                        )
                    ));
                } catch (Exception e) {
                    LOG.error("could not generate preview", e);
                    return HttpResponse.serverError();
                }
            }
        }
        return generate(action, options);
    }
}
