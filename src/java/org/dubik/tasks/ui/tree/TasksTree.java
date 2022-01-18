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
import org.dubik.tasks.TaskController;
import org.dubik.tasks.utils.UIUtil;

import javax.swing.*;

/**
 * @author Sergiy Dubovik
 */
public class TasksTree extends Tree {

    public TasksTree(TaskController taskController, TaskTreeModel taskModel) {
        setTransferHandler(new TaskTransferHandler(taskController));
        setDragEnabled(true);
        setDropMode(DropMode.ON_OR_INSERT);

        setLineStyleAngled();
        setShowsRootHandles(true);
        setRootVisible(false);
        setCellRenderer(new TaskTreeCellRenderer());
        setEditable(false);
        setInvokesStopCellEditing(true);

        addTreeSelectionListener(taskController);
        addMouseListener(new TaskTreeMouseAdapter(UIUtil.createTaskTreePopup("TasksPopupGroup")));

        setModel(taskModel);


    }

}
