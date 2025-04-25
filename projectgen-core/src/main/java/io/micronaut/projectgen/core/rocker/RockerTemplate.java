/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.projectgen.core.rocker;

import com.fizzed.rocker.RockerModel;
import com.fizzed.rocker.RockerOutput;
import io.micronaut.projectgen.core.template.DefaultTemplate;

import java.io.OutputStream;

/**
 * Rocker template.
 */
public class RockerTemplate extends DefaultTemplate {
    private final RockerWritable writable;

    private final boolean executable;

    public RockerTemplate(RockerModel delegate) {
        this( "", delegate);
    }

    public RockerTemplate(String path, RockerModel delegate) {
        this(path, delegate, false);
    }

    public RockerTemplate(String path, RockerModel delegate, boolean executable) {
        super(path);
        this.writable = new RockerWritable(delegate);
        this.executable = executable;
    }

    @Override
    public void write(OutputStream outputStream) {
        writable.write(outputStream);
    }

    @Override
    public boolean isExecutable() {
        return executable;
    }

    /**
     *
     * @return writable
     */
    public RockerWritable getWritable() {
        return writable;
    }

    /**
     *
     * @return render model
     */
    public RockerOutput renderModel() {
        return writable.getModel().render();
    }
}
