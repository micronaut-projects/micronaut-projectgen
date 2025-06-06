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
package io.micronaut.projectgen.core.io.zip;

import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.io.Writable;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.ConsoleOutput;
import io.micronaut.projectgen.core.io.FileSystemOutputHandler;
import io.micronaut.projectgen.core.io.OutputHandler;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Singleton
@Internal
class DefaultZipGenerator implements ZipGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultZipGenerator.class);
    private final ProjectGenerator projectGenerator;

    DefaultZipGenerator(ProjectGenerator projectGenerator) {
        this.projectGenerator = projectGenerator;
    }

    @Override
    @NonNull
    public Writable zip(@NonNull Options options) {
        return new Writable() {
            @Override
            public void writeTo(OutputStream outputStream,
                                @Nullable Charset charset) throws IOException {
                try {
                    projectGenerator.generate(options,
                        new ZipOutputHandler(options.name(), outputStream));
                    outputStream.flush();
                } catch (Exception e) {
                    LOG.error("Error generating application: {}", e.getMessage(), e);
                    throw new IOException(e.getMessage(), e);
                }
            }

            @Override
            public void writeTo(Writer out) {
                // no-op, output stream used
            }
        };
    }
}
