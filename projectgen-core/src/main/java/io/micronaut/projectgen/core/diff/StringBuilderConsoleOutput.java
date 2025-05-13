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
package io.micronaut.projectgen.core.diff;

import io.micronaut.core.annotation.Internal;
import io.micronaut.projectgen.core.io.ConsoleOutput;

@Internal
public class StringBuilderConsoleOutput implements ConsoleOutput {
    private final String lineSeparator;
    private final StringBuilder sb;

    public StringBuilderConsoleOutput() {
        this(System.getProperty("line.separator"));
    }

    public StringBuilderConsoleOutput(String lineSeparator) {
        this.sb = new StringBuilder();
        this.lineSeparator = lineSeparator;
    }

    @Override
    public void out(String message) {
        sb.append(message).append(lineSeparator);
    }

    @Override
    public void err(String message) {
        // will never be called
    }

    @Override
    public void warning(String message) {
        // will never be called
    }

    @Override
    public boolean showStacktrace() {
        return false;
    }

    @Override
    public boolean verbose() {
        return false;
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
