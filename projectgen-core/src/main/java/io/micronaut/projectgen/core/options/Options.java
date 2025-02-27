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
package io.micronaut.projectgen.core.options;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import java.util.*;

/**
 * Project creation options.
*/
public interface Options {
    /**
     *
     * @return Operating System
     */
    default OperatingSystem operatingSystem() {
        return null;
    }

    /**
     *
     * @return Project name
     */
    String name();

    /**
     *
     * @return Project Package name
     */
    String packageName();

    /**
     *
     * @return Features
     */
    List<String> features();

    /**
     *
     * @return Language
     */
    Language language();

    /**
     *
     * @return Test framework
     */
    TestFramework testFramework();

    /**
     *
     * @return Build Tool
     */
    List<BuildTool> buildTools();

    /**
     *
     * @return Java Version
     */
    JdkVersion javaVersion();

    /**
     *
     * @return artifact
     */
    default String artifact() {
        return null;
    }

    /**
     *
     * @return group
     */
    default String group() {
        return null;
    }

    /**
     *
     * @return Version
     */
    default String version() {
        return null;
    }
}
