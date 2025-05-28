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
package io.micronaut.projectgen.http.server.controllers;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.projectgen.core.diff.FeatureDiffer;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.http.server.utils.AttachmentUtils;
import io.micronaut.projectgen.http.server.OptionsBuilder;
import io.micronaut.projectgen.http.server.conf.DownloadDiffControllerConfiguration;

import java.util.Map;


@Requires(beans = FeatureDiffer.class)
@Requires(property = DownloadDiffControllerConfiguration.PREFIX + ".enabled", notEquals = StringUtils.FALSE, defaultValue = StringUtils.TRUE)
@Controller("${" + DownloadDiffControllerConfiguration.PREFIX + ".path:/api/v1/download/diff}")
class DownloadDiffController {
    private final FeatureDiffer featureDiffer;
    private final OptionsBuilder optionsBuilder;
    public static final String FILE_EXTENSION_DIFF = ".diff";

    DownloadDiffController(FeatureDiffer featureDiffer,
                           OptionsBuilder optionsBuilder) {
        this.featureDiffer = featureDiffer;
        this.optionsBuilder = optionsBuilder;
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post
    HttpResponse<?> download(@Body Map<String, Object> form) {
        Options options = optionsBuilder.createOptions(form);
        try {
            return AttachmentUtils.attachment(featureDiffer.diff(options),
                MediaType.TEXT_PLAIN_TYPE,
                options.name() + FILE_EXTENSION_DIFF);
        } catch (Exception e) {
            return HttpResponse.serverError();
        }
    }
}
