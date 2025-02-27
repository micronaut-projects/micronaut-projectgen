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
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.TestFramework;

/**
 * Test Rocker model provider.
 */
public interface TestRockerModelProvider extends JunitRockerModelProvider {

    /**
     *
     * @param language Selected language
     * @param testFramework selected test framework
     * @return A {@link RockerModel}
     * @throws IllegalArgumentException if the test framework / language combination is not handled
     */
    default RockerModel findModel(Language language, TestFramework testFramework) throws IllegalArgumentException {
        switch (testFramework) {
            case JUNIT:
                return findJunitModel(language);
            case SPOCK:
                return spock();
            case KOTEST:
                return koTest();
            default:
                throw new IllegalArgumentException("unable to find a RockerModel for lang: " + language.getName() + "testFramework: " + testFramework.getName());
        }
    }

    /**
     *
     * @return {@link RockerModel} for {@link TestFramework#SPOCK}
     */
    RockerModel spock();

    /**
     *
     * @return {@link RockerModel} for {@link TestFramework#KOTEST}
     */
    RockerModel koTest();
}
