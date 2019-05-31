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
package org.gradle.api.internal.artifacts.configurations;

import groovy.lang.Closure;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.UnknownDomainObjectException;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.UnknownConfigurationException;
import org.gradle.api.internal.DefaultDomainObjectContainer;
import org.gradle.api.internal.artifacts.IvyService;
import org.gradle.util.ConfigureUtil;
import org.gradle.api.artifacts.ProjectDependenciesBuildInstruction;

/**
 * @author Hans Dockter
 */
public class DefaultConfigurationContainer extends DefaultDomainObjectContainer<Configuration> 
        implements ConfigurationContainer, ConfigurationsProvider {
    public static final String DETACHED_CONFIGURATION_DEFAULT_NAME = "detachedConfiguration";
    
    private IvyService ivyService;

    private ProjectDependenciesBuildInstruction projectDependenciesBuildInstruction;

    private int detachedConfigurationDefaultNameCounter = 1;

    public DefaultConfigurationContainer(IvyService ivyService, ProjectDependenciesBuildInstruction projectDependenciesBuildInstruction) {
        super(Configuration.class);
        this.ivyService = ivyService;
        this.projectDependenciesBuildInstruction = projectDependenciesBuildInstruction;
    }

    public Configuration add(String name, Closure configureClosure) {
        if (findByName(name) != null) {
            throw new InvalidUserDataException(String.format("Cannot add configuration '%s' as a configuration with that name already exists.",
                    name));
        }
        DefaultConfiguration configuration = new DefaultConfiguration(name, this, ivyService,
                projectDependenciesBuildInstruction);
        addObject(name, configuration);
        ConfigureUtil.configure(configureClosure, configuration);
        return configuration;
    }

    public Configuration add(String name) {
        return add(name, null);
    }

    @Override
    public String getDisplayName() {
        return "configuration container";
    }

    @Override
    protected UnknownDomainObjectException createNotFoundException(String name) {
        return new UnknownConfigurationException(String.format("Configuration with name '%s' not found.", name));
    }

    @Override
    public String toString() {
        return "DefaultConfigurationContainer{" +
                "configurations=" + getAll() +
                '}';
    }

    public IvyService getIvyService() {
        return ivyService;
    }

    public ProjectDependenciesBuildInstruction getProjectDependenciesBuildInstruction() {
        return projectDependenciesBuildInstruction;
    }

    public Configuration detachedConfiguration(Dependency... dependencies) {
        DetachedConfigurationsProvider detachedConfigurationsProvider = new DetachedConfigurationsProvider();
        DefaultConfiguration detachedConfiguration = new DefaultConfiguration(DETACHED_CONFIGURATION_DEFAULT_NAME + detachedConfigurationDefaultNameCounter++,
                detachedConfigurationsProvider, ivyService, projectDependenciesBuildInstruction);
        for (Dependency dependency : dependencies) {
            detachedConfiguration.addDependency(dependency.copy());
        }
        detachedConfigurationsProvider.setTheOnlyConfiguration(detachedConfiguration);
        return detachedConfiguration;
    }
}