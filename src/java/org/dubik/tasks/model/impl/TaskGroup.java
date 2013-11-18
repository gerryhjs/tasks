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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergiy Dubovik
 */
public class TaskGroup implements ITaskGroup {
    private String title;
    private List<ITaskGroup> taskGroups;
    private ITaskFilter filter;

    private ITaskModel model;

    public TaskGroup(String name) {
        taskGroups = new ArrayList<ITaskGroup>();
        this.title = name;
    }

    public void add(ITaskGroup taskGroup) {
        if (model == null) {
            taskGroups.add(taskGroup);
        }
    }

    public int size() {
        if (model != null) {
            return sizeOfModel();
        }

        return taskGroups.size();
    }

    public ITask get(int index) {
        if (model != null) {
            return getFromModel(index);
        }

        return taskGroups.get(index);
    }

    public ITask getParent() {
        return null;
    }

    public int indexOf(ITask subTask) {
        return -1;
    }

    public void moveUp(ITask task) {
    }

    public void moveDown(ITask task) {
    }

    public void setTaskFilter(ITaskFilter filter) {
        this.filter = filter;
    }

    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return null; //groups don't have descriptions
    }

    public void setTaskModel(ITaskModel model) {
        this.model = model;
    }

    @NotNull
    public TaskPriority getPriority() {
        return TaskPriority.Normal;
    }

    public long getEstimatedTime() {
        return 0;
    }

    public long getActualTime() {
        return 0;
    }

    public long getCreationTime() {
        return 0;
    }

    public boolean isCompleted() {
        return false;
    }

    public boolean isHighlighted() {
        return false;
    }

    @NotNull
    public TaskHighlightingType getHighlightingType() {
        return TaskHighlightingType.Red;
    }

    public int getCompletionRatio() {
        return 0;
    }

    public void add(@NotNull ITask task) {
        //noop
    }

    public void add(int index, @NotNull ITask task) {
        //noop
    }

    public String toString() {
        return title;
    }

    private int sizeOfModel() {
        if (model == null) {
            throw new IllegalStateException("taskmodel is not set, you are trying to access it");
        }

        int size = 0;
        if (filter != null) {
            for (int i = 0; i < model.size(); i++) {
                if (filter.accept(model.getTask(i))) {
                    ++size;
                }
            }
            return size;
        }
        else {
            return model.size();
        }
    }

    private ITask getFromModel(int index) {
        if (model == null) {
            throw new IllegalStateException("taskmodel is not set, you are trying to access it");
        }

        if (filter != null) {
            for (int i = 0; i < model.size(); i++) {
                if (filter.accept(model.getTask(i))) {
                    if (index == 0) {
                        return model.getTask(i);
                    }

                    --index;
                }
            }
        }
        else {
            return model.getTask(index);
        }

        return null;
    }
}