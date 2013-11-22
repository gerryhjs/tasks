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
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.model.ITaskGroup;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

/**
 * @author Sergiy Dubovik
 */
public class TaskTransferHandler extends TransferHandler {

    private TaskController taskController;

    public TaskTransferHandler(TaskController taskController) {
        this.taskController = taskController;
    }

    @Nullable
    @Override
    protected Transferable createTransferable(JComponent c) {
        if (taskController.getSelectedTasks().size() > 0) {
            return new TransferableTask(taskController.getSelectedTasks());
        }
        return null;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }

        support.setShowDropLocation(true);

        Tree.DropLocation dropLocation = (Tree.DropLocation) support.getDropLocation();
        ITask dropTask = (ITask) dropLocation.getPath().getLastPathComponent();

        Transferable transferable = support.getTransferable();
        List<ITask> dragTask = null;
        try {
            dragTask = (List<ITask>) transferable.getTransferData(TransferableTask.TASK_FLAVOR);
        }
        catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Tree tree = (Tree) support.getComponent();
        TaskTreeModel model = (TaskTreeModel) tree.getModel();

        if (dropTask instanceof ITaskGroup && model.getRoot() != dropTask) {
            //don't drop on TaskGroups, except the root
            return false;
        }
        else if (dragTask.contains(dropTask)) {
            //don't drop on self
            return false;
        }
        else {
            //don't drop on a child.
            for (ITask iTask : dragTask) {
                if (taskController.getSubTasks(iTask).contains(dropTask)) {
                    return false;
                }
            }

        }

        return dropTask != null;
    }

    @Override
    public boolean importData(TransferSupport support) {
        Tree.DropLocation dropLocation = (Tree.DropLocation) support.getDropLocation();

        ITask dropTask = (ITask) dropLocation.getPath().getLastPathComponent();
        if (dropTask instanceof ITaskGroup) {
            //this must be the root
            dropTask = null;
        }

        try {
            Transferable transferable = support.getTransferable();
            List<ITask> dragTask = (List<ITask>) transferable.getTransferData(TransferableTask.TASK_FLAVOR);
            if (dragTask != null) {
                int dropIndex = dropLocation.getChildIndex();
                for (ITask iTask : dragTask) {
                    taskController.moveTask(iTask, dropTask, dropIndex);
                    if (dropIndex > -1) {
                        dropIndex++;
                    }
                }
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {

    }

    @Override
    public int getSourceActions(JComponent c) {
        return MOVE;
    }

}
