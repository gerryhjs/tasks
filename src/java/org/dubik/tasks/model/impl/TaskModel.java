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
package org.dubik.tasks.model.impl;

import org.dubik.tasks.model.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergiy Dubovik
 */
public class TaskModel implements ITaskModel {

    private List<ITask> tasks;
    private EventListenerList listeners;

    public TaskModel() {
        tasks = new ArrayList<ITask>();
        listeners = new EventListenerList();
    }


    public ITask addTask(ITask parent, ITask task) {
        if (parent == null) {
            tasks.add(task);
        }
        else {
            parent.add(task);
        }

        fireAddTaskEvent(task);

        return task;
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

        int oldIndex;

        if (currentParent != null) {
            oldIndex = currentParent.indexOf(task);
            currentParent.remove(task);
        }
        else {
            oldIndex= tasks.indexOf(task);
            tasks.remove(task);
        }

        fireDeleteTaskEvent(task, oldIndex);

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

        fireAddTaskEvent(task);

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

        int index = -1;

        firePreDeleteTaskEvent(task);
        ITask parent = task.getParent();
        if (parent == null) {
            index = tasks.indexOf(task);
            tasks.remove(task);
        }
        else {
            Task mutableParent = (Task) parent;
            index = mutableParent.indexOf(task);
            mutableParent.remove(task);
        }

        fireDeleteTaskEvent(task, index);
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
                fireDeleteTaskEvent(task, index);
                tasks.add(index - 1, task);
            }
        }
        else {
            int index = parent.indexOf(task);
            if (index > 0) {
                parent.remove(task);
                fireDeleteTaskEvent(task, index);
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
                fireDeleteTaskEvent(task, index);
                tasks.add(index + 1, task);
            }
        }
        else {
            int index = parent.indexOf(task);
            if (index < parent.size() - 1) {
                parent.remove(task);
                fireDeleteTaskEvent(task, index);
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

        listeners.add(ITaskModelChangeListener.class, listener);
    }

    public void removeChangeListener(ITaskModelChangeListener listener) {
        listeners.remove(ITaskModelChangeListener.class, listener);
    }

    private void fireAddTaskEvent(ITask task) {
        TaskChangeEvent event = new TaskChangeEvent(task.getParent(), task, task.getParent() == null ? tasks.indexOf(task) : task.getParent().indexOf(task) );
        for (ITaskModelChangeListener listener : listeners.getListeners(ITaskModelChangeListener.class)) {
            listener.handleAddTaskEvent(event);
        }
    }

    private void firePreDeleteTaskEvent(ITask task) {
        TaskChangeEvent event = new TaskChangeEvent(task.getParent(), task, task.getParent() == null ? tasks.indexOf(task) : task.getParent().indexOf(task));
        for (ITaskModelChangeListener listener : listeners.getListeners(ITaskModelChangeListener.class)) {
            listener.handlePreDeleteTaskEvent(event);
        }
    }

    private void fireDeleteTaskEvent(ITask task, int index) {
        TaskChangeEvent event = new TaskChangeEvent(task.getParent(), task, index);
        for (ITaskModelChangeListener listener : listeners.getListeners(ITaskModelChangeListener.class)) {
            listener.handleDeleteTaskEvent(event);
        }
    }

    private void firePreChangeTaskEvent(ITask task) {
        TaskChangeEvent event = new TaskChangeEvent(task.getParent(), task, task.getParent() == null ? tasks.indexOf(task) : task.getParent().indexOf(task));
        for (ITaskModelChangeListener listener : listeners.getListeners(ITaskModelChangeListener.class)) {
            listener.handlePreChangeTaskEvent(event);
        }
    }

    private void fireChangeTaskEvent(ITask task) {
        TaskChangeEvent event = new TaskChangeEvent(task.getParent(), task, task.getParent() == null ? tasks.indexOf(task) : task.getParent().indexOf(task));
        for (ITaskModelChangeListener listener : listeners.getListeners(ITaskModelChangeListener.class)) {
            listener.handleChangeTaskEvent(event);
        }
    }
}