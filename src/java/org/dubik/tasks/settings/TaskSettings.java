/*
 * Copyright 2013 Sergiy Dubovik, WarnerJan Veldhuis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dubik.tasks.settings;

import java.beans.PropertyChangeSupport;

/**
 * @author Sergiy Dubovik
 */
public class TaskSettings extends PropertyChangeSupport  {
    private boolean enableActualTime;
    private boolean askActualWhenCompleteTask;
    private boolean enableTasksScope;
    private boolean propagatePriority;
    private boolean oneLevelOnly;

    private static final String TASKS_SETTINGS_ENABLE_ACTUAL_TIME = "enableActualTime";
    private static final String TASKS_SETTINGS_ASK_ACTUAL = "askActualWhenCompleteTask";
    private static final String TASKS_SETTINGS_ENABLE_TASKS_SCOPE = "enableTasksScope";
    private static final String TASKS_SETTINGS_PROPAGATE_PRIORITY = "propagatePriority";
    private static final String TASKS_SETTINGS_ONE_LEVEL_ONLY = "oneLevelOnly";

    public TaskSettings() {
        super(TaskSettings.class);
    }

    public static TaskSettings getDefaults() {
        return new TaskSettings();
    }

    public boolean isEnableActualTime() {
        return true;
    }

    public void setEnableActualTime(boolean enableActualTime) {
        boolean oldValue = this.enableActualTime;
        this.enableActualTime = enableActualTime;
        firePropertyChange(TASKS_SETTINGS_ENABLE_ACTUAL_TIME, oldValue, enableActualTime);
    }

    public boolean isAskActualWhenCompleteTask() {
        return askActualWhenCompleteTask;
    }

    public void setAskActualWhenCompleteTask(boolean askActualWhenCompleteTask) {
        boolean oldValue = this.askActualWhenCompleteTask;
        this.askActualWhenCompleteTask = askActualWhenCompleteTask;
        firePropertyChange(TASKS_SETTINGS_ASK_ACTUAL, oldValue, askActualWhenCompleteTask);
    }

    public void setEnableTasksScope(boolean enableTasksScope) {
        boolean oldValue = this.enableTasksScope;
        this.enableTasksScope = enableTasksScope;
        firePropertyChange(TASKS_SETTINGS_ENABLE_TASKS_SCOPE, oldValue, enableTasksScope);
    }

    public boolean isEnableTasksScope() {
        return enableTasksScope;
    }

    public void setPropagatePriority(boolean propagatePriority) {
        boolean oldValue = this.propagatePriority;
        this.propagatePriority = propagatePriority;
        firePropertyChange(TASKS_SETTINGS_PROPAGATE_PRIORITY, oldValue, propagatePriority);
    }

    public boolean isPropagatePriority() {
        return propagatePriority;
    }

    public void setPriorityPropagatedOneLevelOnly(boolean oneLevelOnly) {
        boolean oldValue = this.oneLevelOnly;
        this.oneLevelOnly = oneLevelOnly;
        firePropertyChange(TASKS_SETTINGS_ONE_LEVEL_ONLY, oldValue, oneLevelOnly);
    }

    public boolean isPriorityPropagatedOneLevelOnly() {
        return oneLevelOnly;
    }
}