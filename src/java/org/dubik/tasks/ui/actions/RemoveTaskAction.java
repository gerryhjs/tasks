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
import com.intellij.openapi.actionSystem.Presentation;
import org.dubik.tasks.TaskController;
import org.dubik.tasks.TasksBundle;
import org.dubik.tasks.model.ITask;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergiy Dubovik
 */
@SuppressWarnings({"WeakerAccess"})
public class RemoveTaskAction extends BaseTaskAction {
    public void actionPerformed(AnActionEvent e) {
        TaskController controller = getController(e);
        if (controller != null) {
            List<ITask> selectedTasks = controller.getSelectedTasks();
            List<ITask> tasks = new ArrayList<ITask>();

            boolean hasSubTasks = false;
            for (ITask selectedTask : selectedTasks) {
                tasks.add(selectedTask);
                List<ITask> subTasks = controller.getSubTasks(selectedTask);
                if (subTasks.size() > 0) {
                    tasks.addAll(subTasks);
                    hasSubTasks = true;
                }
            }

            if (tasks.size() > 1)  {

                String message = null;
                if (hasSubTasks) {
                    message = TasksBundle.message("removetask.confirm.subtasks", tasks.size());
                }
                else {
                    message = TasksBundle.message("removetask.confirm", tasks.size());
                }

                if (JOptionPane.showConfirmDialog(null, message, TasksBundle.message("removetask.title"),
                                                  JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    deleteTasks(selectedTasks, controller);
                }
            }
            else {
                deleteTasks(selectedTasks, controller);
            }
        }
    }

    private void deleteTasks(List<ITask> selectedTasks, TaskController controller) {
        for (ITask selectedTask : selectedTasks) {
            controller.deleteTask(selectedTask);
        }
    }

    protected void update(TaskController controller, List<ITask> selectedTasks,
                          Presentation presentation) {
        presentation.setEnabled(selectedTasks.size() != 0);

        for (ITask task : selectedTasks) {
            if (!controller.canDelete(task)) {
                presentation.setEnabled(false);
                break;
            }
        }
    }
}
