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

import org.jetbrains.annotations.NotNull;

/**
 * Task model contains all tasks (however sub tasks can not be accessed by this interface)
 *
 * @author Sergiy Dubovik
 */
public interface ITaskModel {
    /**
     * Returns size of the model.
     *
     * @return amount of tasks
     */
    public int size();

    /**
     * Returns task at specified index.
     *
     * @param index index of a task
     * @return task
     */
    public
    @NotNull
    ITask getTask(int index);

    /**
     * Updates task at specified index. Existing task will be replaced.
     *
     * @param index index of task to update
     * @param task  task which contains update
     */
    public void setTask(int index, @NotNull ITask task);

    /**
     * Adds change listener.
     *
     * @param listener change listener
     */
    public void addChangeListener(ITaskModelChangeListener listener);

    /**
     * Removes change listener.
     *
     * @param listener already registered change listener
     */
    public void removeChangeListener(ITaskModelChangeListener listener);

    /**
     * Deletes specified task from model. It also can delete sub tasks.
     *
     * @param task task to delete
     */
    public void deleteTask(ITask task);

    /**
     * Completes specified task. Note, it will be up to a task if it will be completed
     * or not. If it has sub tasks this call will have no effect.
     *
     * @param task task to complete
     */
    public void completeTask(ITask task);

   public ITask addTask(ITask parent, ITask task);
    /**
     * Update specified task.
     *
     * @param task          task to update
     * @param parent        new parent
     * @param title         new title
     * @param priority      new priority
     * @param estimatedTime new estimated time
     */
    void updateTask(ITask task, ITask parent, String title, String description, TaskPriority priority, long estimatedTime);

    void moveTask(ITask task, ITask parent, int index);
    /**
     * Sets task to uncomplete state.
     *
     * @param task task which must be set to uncomplete
     */
    void uncompleteTask(ITask task);

    /**
     * Highlight specified task.
     *
     * @param task task to highlight
     */
    void highlightTask(ITask task);

    /**
     * Unhighlight specified task.
     *
     * @param task task to unhighlight
     */
    void unhighlightTask(ITask task);

    /**
     * Updates task actual time.
     *
     * @param task       task which must be updated
     * @param actualTime new real time spent on task
     */
    void updateActualTime(ITask task, long actualTime);

    boolean canMoveUp(@NotNull ITask task);

    boolean canMoveDown(@NotNull ITask task);

    void moveUp(@NotNull ITask task);

    void moveDown(@NotNull ITask task);

    void setTaskHighlightingType(ITask task, TaskHighlightingType hightlightingType);




}
