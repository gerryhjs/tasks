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
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.model.ITaskGroup;

import java.util.List;

/**
 * @author Sergiy Dubovik
 */
public class MoveTaskDownAction extends BaseTaskAction {
    public void actionPerformed(AnActionEvent e) {
        TaskController controller = getController(e);
        ITask selectedTask = controller.getSelectedTasks().get(0);
        controller.moveDown(selectedTask);
        getTreeController(e).selectObject(selectedTask);

    }

    protected void update(TaskController controller, List<ITask> selectedTasks, Presentation presentation) {
        if (selectedTasks.size()== 1 && !(selectedTasks.get(0) instanceof ITaskGroup)) {
            ITask task = selectedTasks.get(0);
            presentation.setEnabled(controller.canMoveDown(task));
        }
        else {
            presentation.setEnabled(false);
        }
    }
}
