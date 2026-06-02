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
package io.micronaut.projectgen.core.generator;

import org.jspecify.annotations.Nullable;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.maven.Packaging;
import io.micronaut.projectgen.core.buildtools.maven.ParentPom;

/**
 * Module Attributes.
 */
public class ModuleAttributes {
    @Nullable
    private ParentPom parentPom;
    @Nullable
    private String packaging;
    @Nullable
    private Coordinate coordinate;
    @Nullable
    private String name;
    @Nullable
    private String description;

    /**
     *
     * @return Parent POM
     */
    @Nullable
    public ParentPom getParentPom() {
        return parentPom;
    }

    /**
     *
     * @param parentPom Parent POM
     */
    public void setParentPom(@Nullable ParentPom parentPom) {
        this.parentPom = parentPom;
    }

    /**
     *
     * @return Coordinate
     */
    @Nullable
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     *
     * @param coordinate Coordinate
     */
    public void setCoordinate(@Nullable Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     *
     * @return name
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     *
     * @param name name
     */
    public void setName(@Nullable String name) {
        this.name = name;
    }

    /**
     *
     * @return Description
     */
    @Nullable
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description Description
     */
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    /**
     *
     * @return Packaging
     */
    @Nullable
    public String getPackaging() {
        return packaging;
    }

    /**
     *
     * @param packaging packaging
     */
    public void setPackaging(Packaging packaging) {
        setPackaging(packaging.toString());
    }

    /**
     *
     * @param packaging packaging
     */
    public void setPackaging(@Nullable String packaging) {
        this.packaging = packaging;
    }
}
