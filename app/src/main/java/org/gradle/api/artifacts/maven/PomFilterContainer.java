/*
 * Copyright 2007-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.artifacts.maven;

import org.gradle.api.internal.artifacts.publish.maven.deploy.PomFilter;

/**
 * @author Hans Dockter
 */
public interface PomFilterContainer {
    String DEFAULT_ARTIFACT_POM_NAME = "default";

    /**
     * Returns the default filter being used. .
     *
     * @see #setFilter(PublishFilter)
     */
    PublishFilter getFilter();

    /**
     * <p>Sets the default filter to be used. This filter is active if no custom filters have been added (see {@link #addFilter(String, PublishFilter)}).
     * If at least one custom filter has been added the default filter is not used any longer.</p>
     * <p>The default for this property is {@link PublishFilter#ALWAYS_ACCEPT}.
     * If there is only one artifact you are fine with this filter. If there is more than one artifact, deployment will lead to
     * an exception, if you don't specify a filter that selects the artifact to deploy. If you want to deploy more than one artiact you have
     * to use the (see {@link #addFilter(String, PublishFilter)} method.</p>
     *
     * @param defaultFilter
     * @see #getFilter()
     */
    void setFilter(PublishFilter defaultFilter);

    /**
     * Returns the pom property of the custom filter.
     * The pom property can be used to customize the pom generation. By default the properties of such a pom object are all null.
     * Null means that Gradle will use default values for generating the Maven pom. Those default values are derived from the deployable artifact
     * and from the project type (e.g. java, war, ...). If you explicitly set a pom property, Gradle will use those instead.
     *
     * @return The Maven Pom
     */
    MavenPom getPom();

    /**
     * <p>Sets the default pom to be used. This pom is active if no custom filters have been added (see {@link #addFilter(String, PublishFilter)}).
     * If at least one custom filter has been added the default pom is not used any longer.</p>
     * <p>Usually you don't need to set this property as the default value provides you a pom object you might use for configuration.
     * By default the properties of such a pom object are all null.
     * If they are null, Gradle will use default values for generating the Maven pom. Those default values are derived from the deployable artifact
     * and from the project type (e.g. java, war, ...). If you explicitly set a pom property, Gradle will use this instead.</p>
     *
     * @param defaultPom
     */
    void setPom(MavenPom defaultPom);

    /**
     * If you want to deploy more than one artifact you need to define filters to select each of those artifacts. The method
     * returns a pom object associated with this filter, that allows you to customize the pom generation for the artifact selected
     * by the filter.
     *
     * @param name The name of the filter
     * @param publishFilter The filter to use
     * @return The pom associated with the filter
     */
    MavenPom addFilter(String name, PublishFilter publishFilter);

    /**
     * Returns a filter added with {@link #addFilter(String, PublishFilter)}
     *
     * @param name The name of the filter
     */
    PublishFilter filter(String name);

    /**
     * Returns the pom associated with a filter added with {@link #addFilter(String, PublishFilter)}.
     *
     * @param name The name of the filter.
     */
    MavenPom pom(String name);

    Iterable<PomFilter> getActivePomFilters();
}
