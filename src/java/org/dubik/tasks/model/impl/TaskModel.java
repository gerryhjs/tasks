/*
 * Copyright 2006 Sergiy Dubovik
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
package org.dubik.tasks.model.impl;

import org.dubik.tasks.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergiy Dubovik
 */
public class TaskModel implements ITaskModel {
    private List<ITask> tasks;
    private List<ITaskModelChangeListener> changeListeners = new ArrayList<ITaskModelChangeListener>();

    public TaskModel() {
        tasks = new ArrayList<ITask>();
    }

    public ITask addTask(String title, String description, TaskPriority priority, long estimatedTime) {
        assert title != null;

        return addTask(title, description, priority, estimatedTime, System.currentTimeMillis());
    }

    public ITask addTask(String title, String description, TaskPriority priority, long estimatedTime, long creationTime) {
        return addTask(null, title, description, priority, estimatedTime, creationTime, false, false);
    }

    public ITask addTask(ITask parent, String title, String description, TaskPriority priority, long estimatedTime,
                         long creationTime, boolean completed, boolean highlighed) {
        Task task = new Task(title, description, priority, estimatedTime);
        task.setCreationTime(creationTime);
        task.setCompleted(completed);
        task.setHighlighted(highlighed);
        task.setParent(parent);

        if (parent == null) {
            tasks.add(task);
        }
        else {
            parent.add(task);
        }

        fireAddTaskEvent(task);

        return task;
    }

    public ITask addTask(ITask parent, String title, String description, TaskPriority priority, long estimatedTime, long actualTime,
                         long creationTime, boolean completed, boolean highlighted) {
        Task task = (Task) addTask(parent, title, description, priority, estimatedTime, creationTime, completed, highlighted);
        task.setActualTime(actualTime);

        return task;
    }

    public void addTask(ITask parentTask, String title, String description, TaskPriority priority, long estimatedTime) {
        Task task = new Task(title, description, priority, estimatedTime);
        parentTask.add(task);
        task.setParent(parentTask);
        fireAddTaskEvent(task);
    }

    public void updateTask(ITask task, ITask parent, String title, String description, TaskPriority priority, long estimatedTime) {
        assert task != null;

        firePreChangeTaskEvent(task);

        Task mutableTask = (Task) task;
        mutableTask.setTitle(title);
        mutableTask.setDescription(description);
        mutableTask.setPriority(priority);
        mutableTask.setEstimatedTime(estimatedTime);

        if (task.getParent() != parent) {
            moveTask(task, parent, -1);
        }

        fireChangeTaskEvent(task);

    }

    public void moveTask(ITask task, ITask newParent, int index) {
        Task currentParent = (Task) task.getParent();
        if (currentParent != null) {
            currentParent.remove(task);
        }
        else {
            tasks.remove(task);
        }

        //when moving a task to the end of a node, the index is too big, since
        //the original node is already removed.
        if (index > tasks.size()) {
            index = tasks.size();
        }

        if (newParent != null) {
            Task mutableParent = (Task) newParent;
            if (index > -1) {
                mutableParent.add(index, task);
            }
            else {
                mutableParent.add(task);
            }
        }
        else {
            if (index > -1) {
                tasks.add(index, task);
            }
            else {
                tasks.add(task);
            }
        }

        Task mutableTask = (Task) task;
        mutableTask.setParent(newParent);

        fireChangeTaskEvent(task);


    }

    public void updateActualTime(ITask task, long actualTime) {
        assert task != null;
        assert actualTime >= 0;

        firePreChangeTaskEvent(task);

        Task mutableTask = (Task) task;
        mutableTask.setActualTime(actualTime);

        fireChangeTaskEvent(task);
    }

    public void deleteTask(ITask task) {
        assert task != null;

        firePreDeleteTaskEvent(task);
        ITask parent = task.getParent();
        if (parent == null) {
            tasks.remove(task);
        }
        else {
            Task mutableParent = (Task) parent;
            mutableParent.remove(task);
        }
        fireDeleteTaskEvent(task);
    }

    public void completeTask(ITask task) {
        assert task != null;

        firePreChangeTaskEvent(task);

        Task mutableTask = (Task) task;
        mutableTask.setCompleted(true);

        fireChangeTaskEvent(task);
    }

    public void uncompleteTask(ITask task) {
        assert task != null;

        firePreChangeTaskEvent(task);

        Task mutableTask = (Task) task;
        mutableTask.setCompleted(false);

        fireChangeTaskEvent(task);
    }

    public void highlightTask(ITask task) {
        assert task != null;

        firePreChangeTaskEvent(task);

        Task mutableTask = (Task) task;
        mutableTask.setHighlighted(true);

        fireChangeTaskEvent(task);
    }

    public void unhighlightTask(ITask task) {
        assert task != null;

        firePreChangeTaskEvent(task);

        Task mutableTask = (Task) task;
        mutableTask.setHighlighted(false);

        fireChangeTaskEvent(task);
    }

    public boolean canMoveUp(@NotNull ITask task) {
        ITask parent = task.getParent();

        if (parent == null) {
            return tasks.indexOf(task) > 0;
        }
        else {
            return parent.indexOf(task) > 0;
        }
    }

    public boolean canMoveDown(@NotNull ITask task) {
        ITask parent = task.getParent();

        if (parent == null) {
            return tasks.size() - 1 > tasks.indexOf(task);
        }
        else if (parent.size() > 1) {
            return parent.size() - 1 > parent.indexOf(task);
        }

        return false;
    }

    public void moveUp(@NotNull ITask task) {
        Task parent = (Task) task.getParent();

        firePreDeleteTaskEvent(task);

        if (parent == null) {
            int index = tasks.indexOf(task);
            if (index > 0) {
                tasks.remove(index);
                fireDeleteTaskEvent(task);
                tasks.add(index - 1, task);
            }
        }
        else {
            int index = parent.indexOf(task);
            if (index > 0) {
                parent.remove(task);
                fireDeleteTaskEvent(task);
                parent.add(index - 1, task);
            }
        }

        fireAddTaskEvent(task);
    }

    public void moveDown(@NotNull ITask task) {
        Task parent = (Task) task.getParent();

        firePreDeleteTaskEvent(task);

        if (parent == null) {
            int index = tasks.indexOf(task);
            if (index < tasks.size() - 1) {
                tasks.remove(index);
                fireDeleteTaskEvent(task);
                tasks.add(index + 1, task);
            }
        }
        else {
            int index = parent.indexOf(task);
            if (index < parent.size() - 1) {
                parent.remove(task);
                fireDeleteTaskEvent(task);
                parent.add(index + 1, task);
            }
        }

        fireAddTaskEvent(task);
    }

    public void setTaskHighlightingType(ITask task, TaskHighlightingType hightlightingType) {
        firePreChangeTaskEvent(task);

        ((Task) task).setHighlightingType(hightlightingType);

        fireChangeTaskEvent(task);
    }

    public int size() {
        return tasks.size();
    }

    @NotNull
    public ITask getTask(int index) {
        return tasks.get(index);
    }

    public void setTask(int index, @NotNull ITask task) {
        tasks.add(index, task);
        fireChangeTaskEvent(task);
    }

    public void addChangeListener(ITaskModelChangeListener listener) {
        assert listener != null;

        changeListeners.add(listener);
    }

    public void removeChangeListener(ITaskModelChangeListener listener) {
        assert listener != null;

        changeListeners.remove(listener);
    }

    private void fireAddTaskEvent(ITask task) {
        TaskChangeEvent event = new TaskChangeEvent(task);
        for (ITaskModelChangeListener listener : changeListeners) {
            listener.handleAddTaskEvent(event);
        }
    }

    private void firePreDeleteTaskEvent(ITask task) {
        TaskChangeEvent event = new TaskChangeEvent(task);
        for (ITaskModelChangeListener listener : changeListeners) {
            listener.handlePreDeleteTaskEvent(event);
        }
    }

    private void fireDeleteTaskEvent(ITask task) {
        TaskChangeEvent event = new TaskChangeEvent(task);
        for (ITaskModelChangeListener listener : changeListeners) {
            listener.handleDeleteTaskEvent(event);
        }
    }

    private void firePreChangeTaskEvent(ITask task) {
        TaskChangeEvent event = new TaskChangeEvent(task);
        for (ITaskModelChangeListener listener : changeListeners) {
            listener.handlePreChangeTaskEvent(event);
        }
    }

    private void fireChangeTaskEvent(ITask task) {
        TaskChangeEvent event = new TaskChangeEvent(task);
        for (ITaskModelChangeListener listener : changeListeners) {
            listener.handleChangeTaskEvent(event);
        }
    }
}