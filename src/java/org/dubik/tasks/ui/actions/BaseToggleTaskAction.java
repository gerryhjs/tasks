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
package org.dubik.tasks.ui.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.dubik.tasks.TaskController;
import org.dubik.tasks.TasksProjectComponent;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.settings.TaskSettings;
import org.dubik.tasks.ui.tree.TaskTreeController;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Utility class, which provides common objects to other task actions.
 *
 * @author Sergiy Dubovik
 */
abstract public class BaseToggleTaskAction extends ToggleAction {
    /**
     * Returns project associated with an action.
     *
     * @param e action event
     * @return project
     */
    protected Project getProject(AnActionEvent e) {
        return PlatformDataKeys.PROJECT.getData(e.getDataContext());
    }

    /**
     * Returns plugin settings.
     *
     * @return plugin settings
     */
    @NotNull
    protected TaskSettings getSettings() {
        return ServiceManager.getService(TaskSettings.class);
    }

    /**
     * Returns task controller.
     *
     * @param e action event
     * @return task controller
     */
    protected TaskController getTaskController(AnActionEvent e) {
        return getTaskController(getProject(e));
    }

    /**
     * Returns task controller.
     *
     * @param project project associated with an action
     * @return task controller
     */
    protected TaskController getTaskController(Project project) {
        TaskController controller = null;
        if (project != null) {
            TasksProjectComponent tasksProject = project.getComponent(TasksProjectComponent.class);
            if (tasksProject != null) {
                controller = tasksProject.getTaskController();
            }
        }

        return controller;
    }

    /**
     * Returns tree controller.
     *
     * @param project project associated with an action
     * @return tree controller
     */
    protected TaskTreeController getTreeController(Project project) {
        TaskTreeController controller = null;
        if (project != null) {
            TasksProjectComponent tasksProject = project.getComponent(TasksProjectComponent.class);
            if (tasksProject != null) {
                controller = tasksProject.getTaskTreeController();
            }
        }

        return controller;
    }

    protected TaskTreeController getTreeController(AnActionEvent e) {
        Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
        return getTreeController(project);
    }

    public void update(AnActionEvent e) {
        super.update(e);

        TaskController controller = getTaskController(getProject(e));
        if (controller != null) {
            List<ITask> selectedTasks = controller.getSelectedTasks();

            Presentation presentation = e.getPresentation();
            update(controller, selectedTasks, presentation);
        }
    }

    /**
     * You may ovveride this version of update() method if you need additional
     * parameters.
     *
     * @param controller    task controller
     * @param selectedTasks selected tasks
     * @param presentation  presentation
     */
    protected void update(TaskController controller, List<ITask> selectedTasks,
                          Presentation presentation) {
    }
}
