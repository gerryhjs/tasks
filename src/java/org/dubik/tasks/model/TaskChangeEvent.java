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
package org.dubik.tasks.model;

/**
 * Represents change event made to a task model or a task.
 *
 * @author Sergiy Dubovik
 */
public class TaskChangeEvent {

    private ITask parent;
    private ITask task;
    private int index;

    public TaskChangeEvent(ITask parent, ITask task, int index) {
        this.parent = parent;
        this.task = task;
        this.index = index;
    }

    /**
     * Returns task associated with change event.
     *
     * @return task
     */
    public ITask getTask() {
        return task;
    }

    public int getIndex() {
        return index;
    }

    public ITask getParent() {
        return parent;
    }
}
