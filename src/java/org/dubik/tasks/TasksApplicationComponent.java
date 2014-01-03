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
package org.dubik.tasks;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.util.*;
import org.dubik.tasks.model.ITaskModel;
import org.dubik.tasks.utils.SerializeSupport;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Task application component.
 * Responsible for tasks and ui settings persistancy.
 * Contributes option dialog to IntelliJ global options.
 * It contains task model, since task model is global and
 * there is always only one instance of it.
 *
 * @author Sergiy Dubovik
 */

public class TasksApplicationComponent implements ApplicationComponent, NamedJDOMExternalizable{

    private final ITaskModel taskModel;

    /**
     * Creates TasksApplicationComponent.
     */
    public TasksApplicationComponent(ITaskModel taskModel) {
        this.taskModel = taskModel;
    }

    /**
     * Initializes component.
     */
    public void initComponent() {
    }

    /**
     * Destroys component.
     */
    public void disposeComponent() {
    }

    /**
     * Returns component name.
     *
     * @return component name
     */
    @NotNull
    public String getComponentName() {
        //this name is registered in the tasks.xml so we cannot change it for backwards compatibility
        return "TaskStorage";
    }

    @NonNls
    public String getExternalFileName() {
        return "tasks";
    }

    public void readExternal(Element element) throws InvalidDataException {
        SerializeSupport.readExternal(taskModel, element);
    }

    public void writeExternal(Element element) throws WriteExternalException {
        SerializeSupport.writeExternal(taskModel, element);
    }
}