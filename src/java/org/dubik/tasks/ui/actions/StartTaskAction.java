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
import com.intellij.openapi.application.ApplicationManager;
import org.dubik.tasks.TaskController;
import org.dubik.tasks.TaskSettings;
import org.dubik.tasks.TasksApplicationComponent;
import org.dubik.tasks.model.ITask;

import java.util.List;

public class StartTaskAction extends BaseTaskAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
        TaskController taskController = this.getController(e);
        List<ITask> selectedTasks = taskController.getSelectedTasks();
        selectedTasks.get(0).start();
    }

    @Override
    protected void update(TaskController controller, List<ITask> selectedTasks, Presentation presentation) {
        TasksApplicationComponent appComp = ApplicationManager.getApplication()
                .getComponent(TasksApplicationComponent.class);

        TaskSettings settings = appComp.getSettings();
        presentation.setEnabled(selectedTasks.size() == 1 &&
                !selectedTasks.get(0).isRunning() &&
                !selectedTasks.get(0).isCompleted() &&
                settings.isEnableActualTime());
    }

}
