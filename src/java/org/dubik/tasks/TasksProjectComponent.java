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

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.treeStructure.Tree;
import org.dubik.tasks.model.ITaskModel;
import org.dubik.tasks.settings.TaskSettings;
import org.dubik.tasks.settings.TaskSettingsService;
import org.dubik.tasks.utils.UIUtil;
import org.dubik.tasks.ui.tree.TaskTreeModel;
import org.dubik.tasks.ui.tree.TasksTree;
import org.dubik.tasks.ui.tree.TaskTreeController;
import org.dubik.tasks.utils.TaskTimer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Tasks project component. Responsible for creating task tree, registering intention.
 *
 * @author Sergiy Dubovik
 */
public class TasksProjectComponent implements ProjectComponent {
    private static final String TASKS_ID = TasksBundle.message("toolwindow.title");

    private Project project;
    private ITaskModel taskModel;
    private TaskSettings settings;

    private JComponent tasksContainer;

    private TaskController taskController;
    private TaskTreeController taskTreeController;
    private PropertyChangeListener settingsChangeListener;

    public TasksProjectComponent(Project project, ITaskModel taskModel) {
        this.project = project;
        this.taskModel = taskModel;
        this.settings = TaskSettingsService.getSettings();
    }

    public void initComponent() {
        taskController = new TaskController(taskModel);

        if (tasksContainer == null) {
            tasksContainer = new JPanel(new BorderLayout(1, 1));
            tasksContainer.setBorder(null);

            TaskTreeModel treeModel = new TaskTreeModel(taskModel);

            Tree tasksTree = new TasksTree(taskController, treeModel);
            tasksContainer.add(new JBScrollPane(tasksTree), BorderLayout.CENTER);

            taskTreeController = new TaskTreeController(treeModel, tasksTree);
            settingsChangeListener = new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    taskTreeController.changedTree();
                }
            };
            settings.addPropertyChangeListener(settingsChangeListener);
        }
    }

    public void disposeComponent() {
        if (settingsChangeListener != null) {
            settings.removePropertyChangeListener(settingsChangeListener);
        }

        TaskTimer.stopAllTimers();
    }

    @NotNull
    public String getComponentName() {
        return "TasksProjectComponent";
    }

    public void projectOpened() {
        registerToolWindow();
        registerActions();
    }

    private void registerToolWindow() {
        ToolWindow tasksToolWindow = ToolWindowManager.getInstance(project).registerToolWindow(TasksProjectComponent.TASKS_ID, false, ToolWindowAnchor.BOTTOM);
        tasksToolWindow.getContentManager().addContent(ContentFactory.SERVICE.getInstance().createContent(tasksContainer, TasksBundle.message("toolwindow.globaltasks"), true));
        tasksToolWindow.setIcon(IconLoader.getIcon(UIUtil.ICON_TASK));
    }

    private void registerActions() {
        ActionGroup actionGroup = (ActionGroup) ActionManager.getInstance().getAction("TasksActionGroup");
        ActionToolbar toolBar = ActionManager.getInstance().createActionToolbar("TasksActionGroupPlace", actionGroup, false);

        ActionGroup additionalActionGroup = (ActionGroup) ActionManager.getInstance().getAction("TasksAdditionalToolBarGroup");
        ActionToolbar additionalToolbar = ActionManager.getInstance().createActionToolbar("TasksActionGroupPlace", additionalActionGroup, false);

        JPanel toolBarPanel = new JPanel(new BorderLayout(1, 1));
        toolBarPanel.add(toolBar.getComponent(), BorderLayout.WEST);
        toolBarPanel.add(additionalToolbar.getComponent(), BorderLayout.CENTER);
        toolBarPanel.setBorder(null);
        tasksContainer.add(toolBarPanel, BorderLayout.WEST);
    }

    public void projectClosed() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        toolWindowManager.unregisterToolWindow(TasksProjectComponent.TASKS_ID);
    }

    public TaskController getTaskController() {
        return taskController;
    }

    public TaskTreeController getTaskTreeController() {
        return taskTreeController;
    }

}