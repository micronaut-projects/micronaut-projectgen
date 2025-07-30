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
package io.micronaut.starter.feature.buildtools.maven;

import io.micronaut.core.annotation.NonNull;

import java.util.Objects;

public class Property {
    @NonNull
    private final String name;

    @NonNull
    private final String value;

    Property(@NonNull String name, @NonNull String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the name of the property.
     *
     * @return the property name, never null
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Returns the value of the property.
     *
     * @return the property value, never null
     */
    @NonNull
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Property property = (Property) o;

        if (!name.equals(property.name)) {
            return false;
        }
        return value.equals(property.value);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        return 31 * result + value.hashCode();
    }

    @NonNull
    public static  Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String value;

        /**
         * Sets the name of the property.
         *
         * @param name The property name.
         * @return This builder instance.
         */
        @NonNull
        public Builder name(@NonNull String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the value of the property.
         *
         * @param value The property value.
         * @return This builder instance.
         */
        @NonNull
        public Builder value(@NonNull String value) {
            this.value = value;
            return this;
        }

        /**
         * Builds the {@link Property} instance using the configured name and value.
         *
         * @return A new {@link Property} instance.
         */
        @NonNull
        public Property build() {
            return new Property(Objects.requireNonNull(this.name),
                Objects.requireNonNull(this.value));
        }
    }
}
