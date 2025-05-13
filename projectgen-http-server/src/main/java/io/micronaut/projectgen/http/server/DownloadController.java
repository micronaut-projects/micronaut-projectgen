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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.io.Writable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.diff.FeatureDiffer;
import io.micronaut.projectgen.core.io.zip.ZipGenerator;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Options;

import java.util.List;
import java.util.Map;

@Requires(property = DownloadControllerConfiguration.PREFIX + ".enabled", notEquals = StringUtils.FALSE, defaultValue = StringUtils.TRUE)
@Controller("${" + DownloadControllerConfiguration.PREFIX + ".path:/download}")
class DownloadController {
    public static final String ATTACHMENT_FILENAME = "attachment; filename=";
    public static final String FILE_EXTENSION_ZIP = ".zip";
    private final ZipGenerator zipGenerator;
    private final OptionsBuilder optionsBuilder;
    private final FeatureDiffer featureDiffer;

    DownloadController(ZipGenerator zipGenerator,
                       OptionsBuilder optionsBuilder,
                       @Nullable FeatureDiffer featureDiffer) {
        this.zipGenerator = zipGenerator;
        this.optionsBuilder = optionsBuilder;
        this.featureDiffer = featureDiffer;
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post
    HttpResponse<?> download(@NonNull @Body Map<String, Object> form) {
        Options options = optionsBuilder.createOptions(form);
        Object actionObject = form.get("action");
        if (actionObject == null) {
            return HttpResponse.unprocessableEntity();
        }
        String action = actionObject.toString();
        if (action.equals("zip")) {
            return attachment(zipGenerator.zip(options),
                MediaType.TEXT_PLAIN_TYPE,
                options.name() + FILE_EXTENSION_ZIP);
        } else if (action.equals("diff")) {
            try {
                return attachment(featureDiffer.diff(options),
                    MediaType.ZIP_TYPE,
                    options.name() + DownloadDiffController.FILE_EXTENSION_DIFF);
            } catch (Exception e) {
                return HttpResponse.serverError();
            }
        }
        return HttpResponse.unprocessableEntity();
    }

    public static HttpResponse<?> attachment(Object body,
                                          MediaType contentType,
                                          String fileName) {
        return HttpResponse.ok(body)
            .header(HttpHeaders.CONTENT_TYPE, contentType)
            .header(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fileName);
    }
}
