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
import org.jetbrains.annotations.NotNull;

public final class TaskBuilder {

    private Task task;

    public TaskBuilder() {
        task = new Task();
    }

    public TaskBuilder setActualTime(long actualTime) {
        task.setActualTime(actualTime);
        return this;
    }

    public TaskBuilder setCompleted(boolean completed) {
        task.setCompleted(completed);
        return this;
    }

    public TaskBuilder setCreationTime(long creationTime) {
        task.setCreationTime(creationTime);
        return this;
    }

    public TaskBuilder setEstimatedTime(long estimatedTime) {
        task.setEstimatedTime(estimatedTime);
        return this;
    }

    public TaskBuilder setDescription(String description) {
        task.setDescription(description);
        return this;
    }

    public TaskBuilder setHighlighted(boolean highlighted) {
        task.setHighlighted(highlighted);
        return this;
    }

    public TaskBuilder setHighlightingType(@NotNull TaskHighlightingType hightlightingType) {
        task.setHighlightingType(hightlightingType);
        return this;
    }

    public TaskBuilder setParent(ITask parent) {
        task.setParent(parent);
        return this;
    }

    public TaskBuilder setPriority(@NotNull TaskPriority priority) {
        task.setPriority(priority);
        return this;
    }

    public TaskBuilder setTitle(String title) {
        task.setTitle(title);
        return this;
    }

    public Task build() {
        return task;
    }
}
