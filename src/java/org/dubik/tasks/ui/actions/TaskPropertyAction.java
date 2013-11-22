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
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.dubik.tasks.TaskController;
import org.dubik.tasks.TasksBundle;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.ui.forms.TaskForm;

import java.util.List;

/**
 * @author Sergiy Dubovik
 */
@SuppressWarnings({"WeakerAccess"})
public class TaskPropertyAction extends BaseTaskAction {

    public void actionPerformed(AnActionEvent e) {
        Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
        if (project != null) {
            TaskController controller = getController(e);
            List<ITask> selectedTasks = controller.getSelectedTasks();
            if (selectedTasks.size()== 1 && controller.canEdit(selectedTasks.get(0))) {
                ITask sTask = selectedTasks.get(0);

                TaskForm taskForm = new TaskForm(project, getSettings(), true );
                taskForm.setTitle(TasksBundle.message("properties.title"));
                taskForm.setTaskTitle(sTask.getTitle());
                taskForm.setTaskDescription(sTask.getDescription());
                taskForm.setPriority(sTask.getPriority());
                taskForm.setEstimatedTime(sTask.getEstimatedTime());
                taskForm.setActualTime(sTask.getActualTime());

                if (sTask.getParent() == null) {
                    taskForm.setSelectedParentTask(controller.getDummyRootTaskInstance());
                }
                else {
                    taskForm.setSelectedParentTask(sTask.getParent());
                }

                taskForm.setParentTasksList(controller.getDummyRootTaskInstance(), controller.findPossibleParents(sTask));

                taskForm.show();

                int exitCode = taskForm.getExitCode();
                if (exitCode == TaskForm.OK_EXIT_CODE) {
                    ITask parent = taskForm.getSelectedParent();

                    if (parent == controller.getDummyRootTaskInstance()) {
                        //user chose root manually
                        parent = null;
                    }

                    controller.updateTask(sTask, parent,
                            taskForm.getTaskTitle(), taskForm.getTaskDescription(),
                            taskForm.getPriority(), taskForm.getEstimatedTime());

                    controller.updateActualTime(sTask, taskForm.getActualTime());
                }
            }
        }
    }

    protected void update(TaskController controller, ITask[] selectedTasks,
                          Presentation presentation) {
        presentation.setEnabled(selectedTasks.length == 1 && controller.canEdit(selectedTasks[0]));
    }
}
