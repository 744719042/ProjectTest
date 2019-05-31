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
package org.gradle.initialization;

import org.gradle.StartParameter;
import org.gradle.api.initialization.Settings;

import java.io.File;

/**
 * @author Hans Dockter
 */
public class MasterDirSettingsFinderStrategy extends AbstractSettingsFileSearchStrategyTemplate {
    public static final String MASTER_DIR_NAME = "master";

    protected File findBeyondCurrentDir(StartParameter startParameter) {
        File searchDir = startParameter.getCurrentDir().getParentFile();
        if (searchDir != null && startParameter.isSearchUpwards()) {
            for (File file : searchDir.listFiles()) {
                if (file.isDirectory() && file.getName().equals(MASTER_DIR_NAME)) {
                    File settingsFile = new File(file, Settings.DEFAULT_SETTINGS_FILE);
                    if (settingsFile.isFile()) {
                        return settingsFile;
                    }
                }
            }
        }
        return null;
    }
}
