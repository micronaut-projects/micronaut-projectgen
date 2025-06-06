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
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.projectgen.core.io.zip.ZipGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.http.server.utils.AttachmentUtils;
import io.micronaut.projectgen.http.server.OptionsBuilder;
import io.micronaut.projectgen.http.server.conf.DownloadZipControllerConfiguration;

import java.util.Map;

@Requires(property = DownloadZipControllerConfiguration.PREFIX + ".enabled", notEquals = StringUtils.FALSE, defaultValue = StringUtils.TRUE)
@Controller("${" + DownloadZipControllerConfiguration.PREFIX + ".path:/api/v1/download/zip}")
class DownloadZipController {
    public static final String ZIP = ".zip";
    private final ZipGenerator zipGenerator;
    private final OptionsBuilder optionsBuilder;

    DownloadZipController(ZipGenerator zipGenerator,
                          OptionsBuilder optionsBuilder) {
        this.zipGenerator = zipGenerator;
        this.optionsBuilder = optionsBuilder;
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post
    HttpResponse<?> download(@Body @Nullable Map<String, Object> form) {
        Options options = optionsBuilder.createOptions(form);
        return AttachmentUtils.attachment(zipGenerator.zip(options),
            MediaType.ZIP_TYPE,
            options.name() + ZIP);
    }
}
