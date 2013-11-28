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
package org.dubik.tasks.ui.tree;

import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.UIUtil;
import org.dubik.tasks.TaskController;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.ui.TasksUIManager;
import org.dubik.tasks.ui.widgets.ProgressTooltip;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * @author Sergiy Dubovik
 */
public class TasksTree extends Tree {

    public TasksTree(TaskController taskController, TaskTreeModel taskModel) {
        setTransferHandler( new TaskTransferHandler(taskController));
        setDragEnabled(true);
        setDropMode(DropMode.ON_OR_INSERT);

        UIUtil.setLineStyleAngled(this);
        setShowsRootHandles(true);
        setRootVisible(false);
        setCellRenderer( new TaskTreeCellRenderer());

        addTreeSelectionListener(taskController);
        addMouseListener(new TaskTreeMouseAdapter(TasksUIManager.createTaskTreePopup("TasksPopupGroup")));

        setModel(taskModel);

        ToolTipManager.sharedInstance().registerComponent(this);

    }

    public JToolTip createToolTip() {
        Point pos = getMousePosition();
        JToolTip tooltip = super.createToolTip();

        if (pos != null) {
            TreePath treePath = getPathForLocation(pos.x, pos.y);
            if (treePath != null) {
                Object lastComponent = treePath.getLastPathComponent();
                if (lastComponent != null) {
                    ITask task = (ITask) lastComponent;
                    if (task.size() != 0) {
                        tooltip = new ProgressTooltip((float) completed(task) / (float) task.size());
                    }
                }
            }
        }

        tooltip.setComponent(this);
        return tooltip;
    }

    private int completed(@NotNull ITask task) {
        int completed = 0;
        for (int i = 0; i < task.size(); i++) {
            if (task.get(i).isCompleted()) {
                completed++;
            }
        }

        return completed;
    }
}
