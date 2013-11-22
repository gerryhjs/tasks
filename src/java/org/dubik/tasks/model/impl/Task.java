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

import org.dubik.tasks.model.ITask;
import org.dubik.tasks.model.TaskHighlightingType;
import org.dubik.tasks.model.TaskPriority;
import org.dubik.tasks.utils.TaskTimer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergiy Dubovik
 */
public class Task implements ITask {
    private String title;
    private String description;
    private TaskPriority priority = TaskPriority.Normal;
    private long estimatedTime;
    private long actualTime;
    private long creationTime = System.currentTimeMillis();
    private boolean completed;
    private boolean highlighted;
    private TaskHighlightingType highlightingType = TaskHighlightingType.Red;
    private List<ITask> subTasks = new ArrayList<ITask>();
    private ITask parent;
    private boolean isRunning;

    public Task() {
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Task(String title, String description, TaskPriority priority) {
        this(title, description);
        this.priority = priority;
    }

    public Task(String title, String description, TaskPriority priority, long estimatedTime) {
        this(title, description, priority);
        this.estimatedTime = estimatedTime;
    }

    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull
    public TaskPriority getPriority() {
        if (priority == null) {
            return TaskPriority.Normal;
        }

        return priority;
    }

    public void setPriority(@NotNull TaskPriority priority) {
        this.priority = priority;
    }

    public long getEstimatedTime() {
        long estimated = 0;
        if (subTasks.size() > 0) {
            for (ITask task : subTasks) {
                estimated += task.getEstimatedTime();
            }
        }
        else {
            estimated = estimatedTime;
        }

        return estimated;
    }

    public long getActualTime() {
        long actual = 0;
        if (subTasks.size() > 0) {
            for (ITask task : subTasks) {
                actual += task.getActualTime();
            }
        }
        else {
            actual = actualTime;
        }

        return actual;
    }

    public void setActualTime(long actualTime) {
        this.actualTime = actualTime;
    }

    public void setEstimatedTime(long estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public boolean isCompleted() {
        boolean compl;
        if (subTasks.size() > 0) {
            compl = true;
            for (ITask task : subTasks) {
                compl = compl && task.isCompleted();
            }
            return compl;
        }
        else {
            compl = completed;
        }

        return compl;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    @NotNull
    public TaskHighlightingType getHighlightingType() {
        return highlightingType;
    }

    public void setHighlightingType(@NotNull TaskHighlightingType hightlightingType) {
        this.highlightingType = hightlightingType;
    }

    public int getCompletionRatio() {
        int totalTasks = subTasks.size();
        if (totalTasks == 0) {
            return isCompleted() ? 100 : 0;
        }

        int cumCompletionRatio = 0;
        for (ITask task : subTasks) {
            cumCompletionRatio += task.getCompletionRatio();
        }

        return cumCompletionRatio / totalTasks;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        stop();
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public String toString() {
        return title;
    }

    public void add(@NotNull ITask task) {
        subTasks.add(task);
        ((Task)task).setParent(this);
    }

    public void add(int index, @NotNull ITask task) {
        subTasks.add(index, task);
        ((Task) task).setParent(this);
    }

    public int size() {
        return subTasks.size();
    }

    public ITask get(int index) {
        return subTasks.get(index);
    }

    public void setParent(ITask parent) {
        this.parent = parent;
    }

    public ITask getParent() {
        return parent;
    }

    public void remove(ITask task) {
        subTasks.remove(task);
    }

    public int indexOf(ITask subTask) {
        assert subTask != null;

        return subTasks.indexOf(subTask);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void start() {
        isRunning = true;
        TaskTimer.startTask(this);
    }

    public void stop() {
        isRunning = false;
        TaskTimer.stopTask(this);
    }
}