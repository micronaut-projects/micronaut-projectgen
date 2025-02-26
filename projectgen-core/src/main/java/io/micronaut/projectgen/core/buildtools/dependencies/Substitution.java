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
package io.micronaut.projectgen.core.buildtools.dependencies;

import io.micronaut.core.annotation.NonNull;

import java.util.Objects;

/**
 * Substitution.
 */
public class Substitution {

    @NonNull
    private final Dependency target;

    @NonNull
    private final Dependency replacement;

    Substitution(@NonNull Dependency target,
                 @NonNull Dependency replacement) {
        this.target = target;
        this.replacement = replacement;
    }

    /**
     *
     * @return Target Dependency
     */
    @NonNull
    public Dependency getTarget() {
        return target;
    }

    /**
     *
     * @return Replacement
     */
    @NonNull
    public Dependency getReplacement() {
        return replacement;
    }

    /**
     *
     * @return The Builder
     */
    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Substitution that = (Substitution) o;

        if (!target.equals(that.target)) {
            return false;
        }
        return replacement.equals(that.replacement);
    }

    @Override
    public int hashCode() {
        int result = target.hashCode();
        result = 31 * result + replacement.hashCode();
        return result;
    }

    /**
     * Builder.
     */
    public static class Builder {
        private Dependency target;
        private Dependency replacement;

        /**
         *
         * @param target Target
         * @return Builder
         */
        @NonNull
        public Builder target(@NonNull Dependency target) {
            this.target = target;
            return this;
        }

        /**
         *
         * @param replacement Replacement
         * @return Builder
         */
        @NonNull
        public Builder replacement(@NonNull Dependency replacement) {
            this.replacement = replacement;
            return this;
        }

        /**
         *
         * @return Substitution
         */
        @NonNull
        public Substitution build() {
            return new Substitution(Objects.requireNonNull(target),
                Objects.requireNonNull(replacement));
        }
    }
}
