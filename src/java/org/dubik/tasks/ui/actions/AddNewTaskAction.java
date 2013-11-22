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

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import org.dubik.tasks.TaskController;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.model.impl.TaskBuilder;
import org.dubik.tasks.ui.forms.TaskForm;

/**
 * Creates task action.
 *
 * @author Sergiy Dubovik
 */
public class AddNewTaskAction extends BaseTaskAction {

    public void actionPerformed(AnActionEvent e) {
        Project project = DataKeys.PROJECT.getData(e.getDataContext());
        actionPerformed(project, null);
    }

    public void actionPerformed(Project project, String title) {
        TaskController controller = getController(project);

        TaskForm newTaskForm = new TaskForm(project, getSettings(), false );
        newTaskForm.setTaskTitle(title);
        newTaskForm.setTaskDescription(null);
        newTaskForm.setActualsVisible(false);
        TasksActionUtils.preselectPriority(controller, newTaskForm);
        TasksActionUtils.preselectParentTask(controller, newTaskForm);

        newTaskForm.show();
        int exitCode = newTaskForm.getExitCode();

        if (exitCode == TaskForm.EXIT_ADD_TO_ROOT || exitCode == TaskForm.EXIT_ADD) {
            ITask task = new TaskBuilder()
                    .setTitle(newTaskForm.getTaskTitle())
                    .setDescription(newTaskForm.getTaskDescription())
                    .setPriority(newTaskForm.getPriority())
                    .setEstimatedTime(newTaskForm.getEstimatedTime())
                    .build();

            ITask parent = null;
            if (exitCode == TaskForm.EXIT_ADD && newTaskForm.getSelectedParent() != controller.getDummyRootTaskInstance()) {
                parent = newTaskForm.getSelectedParent();
            }

            controller.addTask(parent, task);
            getTreeController(project).expandToObject(parent);
        }
    }

}