/*
 * Copyright 2007 the original author or authors.
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

package org.gradle;

import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionGraph;
import org.gradle.api.execution.TaskExecutionGraphListener;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.internal.BuildInternal;
import org.gradle.api.internal.SettingsInternal;
import org.gradle.api.invocation.Build;
import org.gradle.configuration.BuildConfigurer;
import org.gradle.execution.BuildExecuter;
import org.gradle.initialization.*;
import org.gradle.util.ListenerBroadcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>{@code Gradle} is the main entry point for embedding Gradle. You use this class to manage a Gradle build, as
 * follows:</p>
 * <p/>
 * <ol>
 * <p/>
 * <li>Create a {@link StartParameter} instance and configure it with the desired properties. The properties of {@code
 * StartParameter} generally correspond to the command-line options of Gradle.</li>
 * <p/>
 * <li>Obtain a {@code Gradle} instance by calling {@link #newInstance}, passing in the {@code StartParameter}.</li>
 * <p/>
 * <li>Optionally, add one or more {@link BuildListener}s to receive events as the build executes by calling {@link
 * #addBuildListener}.</li>
 * <p/>
 * <li>Call {@link #run} to execute the build. This will return a {@link BuildResult}. Note that if the build fails, the
 * resulting exception will be contained in the {@code BuildResult}.</li>
 * <p/>
 * <li>Query the build result. You might want to call {@link BuildResult#rethrowFailure()} to rethrow any build
 * failure.</li>
 * <p/>
 * </ol>
 *
 * @author Hans Dockter
 */
public class Gradle {
    private static Logger logger = LoggerFactory.getLogger(Gradle.class);

    private static GradleFactory factory = new DefaultGradleFactory(new DefaultLoggingConfigurer(), new DefaultCommandLine2StartParameterConverter());

    private StartParameter startParameter;
    private ISettingsFinder settingsFinder;
    private IGradlePropertiesLoader gradlePropertiesLoader;
    private SettingsProcessor settingsProcessor;
    private BuildLoader buildLoader;
    private BuildConfigurer buildConfigurer;

    private final ListenerBroadcast<BuildListener> buildListeners = new ListenerBroadcast<BuildListener>(
            BuildListener.class);

    public Gradle(StartParameter startParameter, ISettingsFinder settingsFinder,
                  IGradlePropertiesLoader gradlePropertiesLoader, SettingsProcessor settingsProcessor,
                  BuildLoader buildLoader, BuildConfigurer buildConfigurer) {
        this.startParameter = startParameter;
        this.settingsFinder = settingsFinder;
        this.gradlePropertiesLoader = gradlePropertiesLoader;
        this.settingsProcessor = settingsProcessor;
        this.buildLoader = buildLoader;
        this.buildConfigurer = buildConfigurer;
    }

    /**
     * <p>Executes the build for this Gradle instance and returns the result. Note that when the build fails, the
     * exception is available using {@link BuildResult#getFailure()}.</p>
     *
     * @return The result. Never returns null.
     */
    public BuildResult run() {
        return doBuild(new RunSpecification() {
            public BuildInternal run(SettingsInternal settings, StartParameter startParameter) {
                return loadAndConfigureAndRun(settings, startParameter, startParameter.isDryRun());
            }
        });
    }

    /**
     * Evaluates the settings and all the projects. The information about available tasks and projects is accessible
     * via the {@link org.gradle.api.invocation.Build#getRootProject()} object.
     *
     * @return A BuildResult object
     */
    public BuildResult getBuildAnalysis() {
        return doBuild(new RunSpecification() {
            public BuildInternal run(SettingsInternal settings, StartParameter startParameter) {
                return loadAndConfigure(settings, startParameter);
            }
        });
    }

    /**
     * Evaluates the settings and all the projects. Evaluates the settings and all the projects.
     * The information about available tasks and projects is accessible via the
     * {@link org.gradle.api.invocation.Build#getRootProject()} object. Fills the execution plan without running the build.
     * The tasks to be executed tasks are available via {@link org.gradle.api.invocation.Build#getTaskGraph()}.
     *
     * @return A BuildResult object
     */
    public BuildResult getBuildAndRunAnalysis() {
        return doBuild(new RunSpecification() {
            public BuildInternal run(SettingsInternal settings, StartParameter startParameter) {
                return loadAndConfigureAndRun(settings, startParameter, true);
            }
        });
    }

    private TaskExecutionListener createDisableTaskListener() {
        return new TaskExecutionListener() {
            public void beforeExecute(Task task) {
                task.setEnabled(false);
            }

            public void afterExecute(Task task, Throwable failure) {
            }
        };
    }

    private BuildResult doBuild(RunSpecification runSpecification) {
        fireBuildStarted(startParameter);

        SettingsInternal settings = null;
        Throwable failure = null;
        Build build = null;
        try {
            settings = loadSettings(startParameter);
            build = runSpecification.run(settings, startParameter);
        } catch (Throwable t) {
            failure = t;
        }
        BuildResult buildResult = new BuildResult(settings, build, failure);
        fireBuildFinished(buildResult);

        return buildResult;
    }

    private BuildInternal loadAndConfigureAndRun(SettingsInternal settings, StartParameter startParameter, boolean disableTasks) {
        BuildInternal build = loadAndConfigure(settings, startParameter);
        if (disableTasks) {
            build.getTaskGraph().addTaskExecutionListener(createDisableTaskListener());
        }
        attachTaskGraphListener(build);

        // Execute build
        BuildExecuter executer = startParameter.getBuildExecuter();

        executer.select(build.getDefaultProject());
        logger.info(String.format("Starting build for %s.", executer.getDisplayName()));
        executer.execute(build.getTaskGraph());
        return build;
    }

    private BuildInternal loadAndConfigure(SettingsInternal settings, StartParameter startParameter) {
        ClassLoader classLoader = settings.createClassLoader();

        // Load build
        BuildInternal build = buildLoader.load(settings.getRootProject(), classLoader, startParameter,
                gradlePropertiesLoader.getGradleProperties());
        fireProjectsLoaded(build);

        // Configure build
        buildConfigurer.process(build.getRootProject());
        fireProjectsEvaluated(build);
        return build;
    }

    private void attachTaskGraphListener(BuildInternal build) {
        build.getTaskGraph().addTaskExecutionGraphListener(new TaskExecutionGraphListener() {
            public void graphPopulated(TaskExecutionGraph graph) {
                fireTaskGraphPrepared(graph);
            }
        });
    }

    private SettingsInternal loadSettings(StartParameter startParameter) {
        SettingsInternal settings = settingsProcessor.process(settingsFinder, startParameter, gradlePropertiesLoader);
        fireSettingsEvaluated(settings);
        return settings;
    }

    /**
     * Returns a Gradle instance based on the passed start parameter.
     *
     * @param startParameter The start parameter object the Gradle instance is initialized with
     */
    public static Gradle newInstance(final StartParameter startParameter) {
        return factory.newInstance(startParameter);
    }

    /**
     * Returns a Gradle instance based on the passed command line syntax arguments. Certain command line arguments
     * won't have any effect if you choose this method (e.g. -v, -h). If you want to act upon, you better
     * use {@link #createStartParameter(String[])} in conjunction with {@link #newInstance(String[])}.
     *
     * @param commandLineArgs A String array where each element denotes an entry of the Gradle command line syntax
     */
    public static Gradle newInstance(final String[] commandLineArgs) {
        return factory.newInstance(commandLineArgs);
    }

    /**
     * Returns a StartParameter object out of command line syntax arguments. Every possible command line
     * option has it associated field in the StartParameter object.
     *
     * @param commandLineArgs A String array where each element denotes an entry of the Gradle command line syntax
     */
    public static StartParameter createStartParameter(final String[] commandLineArgs) {
        return factory.createStartParameter(commandLineArgs);
    }

    private void fireBuildStarted(StartParameter startParameter) {
        buildListeners.getSource().buildStarted(startParameter);
    }

    private void fireSettingsEvaluated(SettingsInternal settings) {
        buildListeners.getSource().settingsEvaluated(settings);
    }

    private void fireTaskGraphPrepared(TaskExecutionGraph graph) {
        buildListeners.getSource().taskGraphPopulated(graph);
    }

    private void fireProjectsLoaded(BuildInternal build) {
        buildListeners.getSource().projectsLoaded(build);
    }

    private void fireProjectsEvaluated(BuildInternal build) {
        buildListeners.getSource().projectsEvaluated(build);
    }

    private void fireBuildFinished(BuildResult buildResult) {
        buildListeners.getSource().buildFinished(buildResult);
    }

    // This is used for mocking
    public static void injectCustomFactory(GradleFactory gradleFactory) {
        factory = gradleFactory == null ? new DefaultGradleFactory(new DefaultLoggingConfigurer(), new DefaultCommandLine2StartParameterConverter()) : gradleFactory;
    }

    public StartParameter getStartParameter() {
        return startParameter;
    }

    /**
     * <p>Adds a {@link BuildListener} to this Gradle instance. The listener is notified of events which occur during a
     * build.</p>
     *
     * @param buildListener The listener to add.
     */
    public void addBuildListener(BuildListener buildListener) {
        buildListeners.add(buildListener);
    }

    private static interface RunSpecification {
        BuildInternal run(SettingsInternal settings, StartParameter startParameter);
    }
}
