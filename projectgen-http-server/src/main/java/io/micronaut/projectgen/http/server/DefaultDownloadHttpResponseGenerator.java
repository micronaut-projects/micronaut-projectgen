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
package io.micronaut.projectgen.http.server;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.projectgen.core.diff.FeatureDiffer;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.io.zip.ZipGenerator;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

import static io.micronaut.projectgen.http.server.AttachmentUtils.attachment;

@Singleton
public class DefaultDownloadHttpResponseGenerator implements DownloadHttpResponseGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDownloadHttpResponseGenerator.class);
    public static final String FILE_EXTENSION_ZIP = ".zip";
    public static final String ACTION_ZIP = "zip";
    public static final String ACTION_DIFF = "diff";
    public static final String ACTION_PREVIEW = "preview";
    protected final OptionsBuilder optionsBuilder;
    protected final ZipGenerator zipGenerator;
    protected final PreviewGenerator previewGenerator;
    protected final FeatureDiffer featureDiffer;

    public DefaultDownloadHttpResponseGenerator(OptionsBuilder optionsBuilder,
                                                ZipGenerator zipGenerator,
                                                PreviewGenerator previewGenerator,
                                                FeatureDiffer featureDiffer) {
        this.optionsBuilder = optionsBuilder;
        this.zipGenerator = zipGenerator;
        this.previewGenerator = previewGenerator;
        this.featureDiffer = featureDiffer;
    }

    protected Optional<String> parseAction(Map<String, Object> form) {
        Object actionObject = form.get("action");
        if (actionObject == null) {
            return Optional.empty();
        }
        return Optional.of(actionObject.toString());
    }

    @Override
    public HttpResponse<?> generate(HttpRequest<?> request, Map<String, Object> form) {
        Optional<String> actionOptional = parseAction(form);
        if (actionOptional.isEmpty()) {
            return HttpResponse.unprocessableEntity();
        }
        String action = actionOptional.get();
        return generate(action, form);
    }

    protected HttpResponse<?> generate(String action, Map<String, Object> form) {
        Options options = optionsBuilder.createOptions(form);
        return generate(action, options);
    }

    protected HttpResponse<?> generateDiff(Options options) {
        try {
            return attachment(featureDiffer.diff(options),
                MediaType.ZIP_TYPE,
                options.name() + DownloadDiffController.FILE_EXTENSION_DIFF);
        } catch (Exception e) {
            return HttpResponse.serverError();
        }
    }

    protected HttpResponse<?> generateZip(Options options) {
        return attachment(zipGenerator.zip(options),
            MediaType.TEXT_PLAIN_TYPE,
            options.name() + FILE_EXTENSION_ZIP);
    }

    protected HttpResponse<?> generatePreview(Options options) {
        try {
            return HttpResponse.ok(previewGenerator.generate(options));
        } catch (Exception e) {
            LOG.error("could not generate preview", e);
            return HttpResponse.serverError();
        }
    }

    protected HttpResponse<?> generate(String action, Options options) {
        if (action.equals(ACTION_ZIP)) {
            return generateZip(options);
        } else if (action.equals(ACTION_DIFF)) {
            return generateDiff(options);
        } else if (action.equals(ACTION_PREVIEW)) {
            return generatePreview(options);
        }


        return HttpResponse.unprocessableEntity();
    }
}
