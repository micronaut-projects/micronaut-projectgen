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
import io.micronaut.core.io.Writable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.projectgen.core.io.zip.ZipGenerator;
import io.micronaut.projectgen.core.options.Options;

import java.util.Map;

import static io.micronaut.projectgen.http.server.DefaultDownloadHttpResponseGenerator.FILE_EXTENSION_ZIP;
import static io.micronaut.projectgen.http.server.DownloadController.*;

@Requires(property = DownloadZipControllerConfiguration.PREFIX + ".enabled", notEquals = StringUtils.FALSE, defaultValue = StringUtils.TRUE)
@Controller("${" + DownloadZipControllerConfiguration.PREFIX + ".path:/download/zip}")
class DownloadZipController {
    private final ZipGenerator zipGenerator;
    private final OptionsBuilder optionsBuilder;

    DownloadZipController(ZipGenerator zipGenerator,
                          OptionsBuilder optionsBuilder) {
        this.zipGenerator = zipGenerator;
        this.optionsBuilder = optionsBuilder;
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post
    HttpResponse<?> download(@Body Map<String, Object> form) {
        Options options = optionsBuilder.createOptions(form);
        return AttachmentUtils.attachment(zipGenerator.zip(options),
            MediaType.ZIP_TYPE,
            options.name() + FILE_EXTENSION_ZIP);
    }
}
