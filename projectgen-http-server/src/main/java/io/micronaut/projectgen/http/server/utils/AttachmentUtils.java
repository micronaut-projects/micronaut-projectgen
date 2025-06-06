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
package io.micronaut.projectgen.http.server.utils;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;

/**
 * Utility class for controller attachment responses.
 */
public final class AttachmentUtils {
    private static final String ATTACHMENT_FILENAME = "attachment; filename=";

    private AttachmentUtils() {
    }

    /**
     *
     * @param body Response Body
     * @param contentType Response Content-Type
     * @param fileName attachment file name
     * @return HTTP Response
     */
    public static HttpResponse<?> attachment(Object body,
                                             MediaType contentType,
                                             String fileName) {
        return HttpResponse.ok(body)
            .header(HttpHeaders.CONTENT_TYPE, contentType)
            .header(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fileName);
    }
}
