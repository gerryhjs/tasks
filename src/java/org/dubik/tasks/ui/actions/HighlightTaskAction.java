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

import java.util.List;

/**
 * Highlight task.
 *
 * @author Sergiy Dubovik
 */
@SuppressWarnings({"WeakerAccess"})
public class HighlightTaskAction extends BaseToggleTaskAction {

    @Override
    public boolean isSelected(AnActionEvent e) {
        return areAllHighlighted(getTaskController(e).getSelectedTasks());
    }

    @Override
    public void setSelected(AnActionEvent e, boolean state) {
        TaskController controller = getTaskController(e);
        if (controller != null) {
            List<ITask> selectedTasks = controller.getSelectedTasks();
            if (canHighlightOrUnhighlight(selectedTasks, controller)) {
                for (ITask task : selectedTasks) {
                    if (state) {
                        controller.highlightTask(task);
                    }
                    else {
                        controller.unhighlightTask(task);
                    }
                }
            }
        }

    }

    protected void update(TaskController controller, List<ITask> selectedTasks, Presentation presentation) {
        if (selectedTasks.size() == 0) {
            presentation.setEnabled(false);
            return;
        }

        presentation.setEnabled(canHighlightOrUnhighlight(selectedTasks, controller));
    }

    private boolean areAllHighlighted(List<ITask> tasks) {
        boolean highlighted = true;

        for (int i = 0; i < tasks.size() && highlighted; i++) {
            ITask task = tasks.get(i);
            highlighted = task.isHighlighted();
        }

        return highlighted;
    }

    private boolean canHighlightOrUnhighlight(List<ITask> tasks, TaskController controller) {
        boolean result = true;

        for (ITask task : tasks) {
            result = result && (controller.canHighlight(task) || controller.canBeUnhighlighted(task));
        }

        return result;
    }
}
