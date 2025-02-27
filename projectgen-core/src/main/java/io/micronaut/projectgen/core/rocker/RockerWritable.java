/*
 * Copyright 2017-2022 original authors
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
import com.fizzed.rocker.runtime.OutputStreamOutput;
import io.micronaut.projectgen.core.template.Writable;

import java.io.OutputStream;

/**
 * Rocker Writable.
 */
public class RockerWritable implements Writable {

    private final RockerModel model;

    public RockerWritable(RockerModel model) {
        this.model = model;
    }

    /**
     *
     * @return the rocker model
     */
    public RockerModel getModel() {
        return model;
    }

    @Override
    public void write(OutputStream outputStream) {
        model.render((contentType, charsetName) ->
            new OutputStreamOutput(contentType, outputStream, charsetName));
    }
}
