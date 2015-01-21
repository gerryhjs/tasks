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

import org.dubik.tasks.model.*;
import org.dubik.tasks.model.impl.Task;
import org.dubik.tasks.model.impl.TaskGroup;
import org.dubik.tasks.ui.filters.PriorityFilter;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Task tree model. Feeds tree with the data.
 *
 * @author Sergiy Dubovik
 */
public class TaskTreeModel implements TreeModel, ITaskModelChangeListener {
    private EventListenerList listeners = new EventListenerList();
    private ITaskGroup root;
    private ITaskModel taskModel;
    private ITaskFilter taskFilter;
    private boolean isGrouped;

    public TaskTreeModel(ITaskModel taskModel) {
        this.taskModel = taskModel;

        taskModel.addChangeListener(this);

        root = new TaskGroup("All Tasks");
        root.setTaskModel(taskModel);
    }

    public Object getRoot() {
        return root;
    }

    public Object getChild(Object parent, int index) {
        ITask task;

        if (parent == null) {
            task = root;
        }
        else {
            task = (ITask) parent;
        }

        if (taskFilter != null) {
            int taskIndex;

            for (taskIndex = 0; taskIndex < task.size(); taskIndex++) {
                if (taskFilter.accept(task.get(taskIndex))) {
                    if (index == 0) {
                        return task.get(taskIndex);
                    }

                    index--;
                }
            }
        }
        else {
            return task.get(index);
        }

        return null;
    }

    public int getChildCount(Object parent) {
        int size = 0;
        ITask task = null;

        if (parent == null) {
            task = root;
        }
        if (parent instanceof ITask) {
            task = (ITask) parent;
        }

        size = task.size();
        if (taskFilter != null) {
            for (int i = 0; i < task.size(); i++) {
                if (!taskFilter.accept(task.get(i))) {
                    size--;
                }
            }
        }


        return size;
    }

    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        if (!isGrouped() && getTaskFilter() == null && path.getLastPathComponent() instanceof Task) {
            Task task = (Task) path.getLastPathComponent();
            if (!task.getTitle().equals(newValue)) {
                task.setTitle((String) newValue);
                TaskChangeEvent event = new TaskChangeEvent(task.getParent(), task, task.getParent() == null ? root.indexOf(task) : task.getParent().indexOf(task));
                handleChangeTaskEvent(event);
            }
        }
    }

    public int getIndexOfChild(Object parent, Object child) {
        for (int i = 0; i < getChildCount(parent); i++) {
            if (getChild(parent, i) == child) {
                return i;
            }
        }

        return -1;
    }

    public void setTaskFilter(ITaskFilter taskFilter) {
        this.taskFilter = taskFilter;
        updateTree();
    }

    public ITaskFilter getTaskFilter() {
        return taskFilter;
    }

    private void updateTree() {
        fireTreeStructureChanged(new TreeModelEvent(this, new Object[]{root}));
    }

    public void handleAddTaskEvent(TaskChangeEvent event) {
        ITask task = event.getTask();
        Object[] pathToObject = findPathToObject(root, task);
        TreePath path = new TreePath(pathToObject);

        TreeModelEvent treeModelEvent = new TreeModelEvent(this, path, new int[]{event.getIndex()}, new Object[]{task});
        if (getChildCount(getRoot()) == 1 ) {
            fireTreeStructureChanged(treeModelEvent);
        }
        else {
            fireTreeNodesInserted(treeModelEvent);
        }
    }

    public void handlePreDeleteTaskEvent(TaskChangeEvent event) {
    }

    public void handleDeleteTaskEvent(TaskChangeEvent event) {
        ITask task = event.getTask();
        ITask parent = event.getParent();
        Object[] pathToObject = parent == null ? new Object[]{root} : findPathToObject(root, parent);
        TreePath path = new TreePath(pathToObject);

        if (parent != null) {
            path = path.pathByAddingChild(parent);
        }

        fireTreeNodesRemoved(new TreeModelEvent(this, path, new int[]{event.getIndex()}, new Object[]{task}));
    }

    public void handlePreChangeTaskEvent(TaskChangeEvent event) {
    }

    public void handleChangeTaskEvent(TaskChangeEvent event) {
        ITask task = event.getTask();
        Object[] pathToObject = findPathToObject(root, task);
        TreePath path = new TreePath(pathToObject);

        fireTreeNodesChanged(new TreeModelEvent(this, path, new int[]{event.getIndex()}, new Object[]{task}));
    }

    public Object[] findPathToObject(Object root, Object task) {
        List<Object> path = new ArrayList<Object>();
        findPathToObject(root, task, path);
        Collections.reverse(path);
        Object[] oPath = new Object[path.size()];
        oPath = path.toArray(oPath);
        return oPath;
    }

    private boolean findPathToObject(Object parent, Object task, List<Object> path) {
        if (parent == task) {
            return true;
        }

        for (int i = 0; i < getChildCount(parent); i++) {
            if (findPathToObject(getChild(parent, i), task, path)) {
                path.add(parent);
                return true;
            }

        }

        return false;
    }

    public void groupByPriority(boolean group) {
        isGrouped = group;

        ITaskGroup newRoot = new TaskGroup("All Tasks");

        if (group) {
            for (TaskPriority priority : TaskPriority.values()) {
                ITaskGroup taskGroup = new TaskGroup(priority.toString());
                taskGroup.setTaskModel(taskModel);
                taskGroup.setTaskFilter(new PriorityFilter(priority));

                newRoot.add(taskGroup);
            }
        }
        else {
            newRoot.setTaskModel(taskModel);
        }

        root = newRoot;
        updateTree();
    }

    public boolean isGrouped() {
        return isGrouped;
    }


    public void addTreeModelListener(TreeModelListener listener) {
        listeners.add(TreeModelListener.class, listener);
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        listeners.remove(TreeModelListener.class, listener);
    }

    public void fireTreeNodesChanged(TreeModelEvent e) {
        for (TreeModelListener listener : listeners.getListeners(TreeModelListener.class)) {
            listener.treeNodesChanged(e);
        }
    }

    public void fireTreeNodesInserted(TreeModelEvent e) {
        for (TreeModelListener listener : listeners.getListeners(TreeModelListener.class)) {
            listener.treeNodesInserted(e);
        }
    }

    public void fireTreeNodesRemoved(TreeModelEvent e) {
        for (TreeModelListener listener : listeners.getListeners(TreeModelListener.class)) {
            listener.treeNodesRemoved(e);
        }
    }

    public void fireTreeStructureChanged(TreeModelEvent e) {
        for (TreeModelListener listener : listeners.getListeners(TreeModelListener.class)) {
            listener.treeStructureChanged(e);
        }
    }

}