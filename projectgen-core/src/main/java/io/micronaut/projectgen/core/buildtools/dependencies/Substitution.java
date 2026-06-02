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

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Substitution.
 */
public class Substitution {

    private final @NonNull Dependency target;

    private final @NonNull Dependency replacement;

    Substitution(@NonNull Dependency target,
                 @NonNull Dependency replacement) {
        this.target = target;
        this.replacement = replacement;
    }

    /**
     *
     * @return Target Dependency
     */
    public @NonNull Dependency getTarget() {
        return target;
    }

    /**
     *
     * @return Replacement
     */
    public @NonNull Dependency getReplacement() {
        return replacement;
    }

    /**
     *
     * @return The Builder
     */
    public static @NonNull Builder builder() {
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
        @Nullable
        private Dependency target;
        @Nullable
        private Dependency replacement;

        /**
         *
         * @param target Target
         * @return Builder
         */
        public @NonNull Builder target(@NonNull Dependency target) {
            this.target = target;
            return this;
        }

        /**
         *
         * @param replacement Replacement
         * @return Builder
         */
        public @NonNull Builder replacement(@NonNull Dependency replacement) {
            this.replacement = replacement;
            return this;
        }

        /**
         *
         * @return Substitution
         */
        public @NonNull Substitution build() {
            return new Substitution(Objects.requireNonNull(target),
                Objects.requireNonNull(replacement));
        }
    }
}
