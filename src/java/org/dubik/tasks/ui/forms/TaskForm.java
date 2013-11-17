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
package org.dubik.tasks.ui.forms;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.ColoredListCellRenderer;
import org.dubik.tasks.TaskSettings;
import org.dubik.tasks.TasksBundle;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.model.TaskPriority;
import org.dubik.tasks.ui.TasksUIManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * @author Sergiy Dubovik
 */
public class TaskForm extends DialogWrapper {
    private static long ONE_MINUTE = 60 * 1000L;
    private JTextField titleTextField;
    private JComboBox<TaskPriority> priorityComboBox;
    private JSpinner minutesSpinner;
    private JPanel container;
    private JComboBox<ITask> parentTasksComboBox;
    private JSpinner actualMinutesSpinner;
    private JLabel actualTimeLabel;
    private JLabel actualMinutesLabel;
    private JTextPane description;
    private ITask selectedParentTask;
    private Action addAction;
    private Action addToRootAction;
    private boolean addToRoot = false;

    public TaskForm(Project project, TaskSettings settings) {
        super(project, false);

        setValidationDelay(1);
        setTitle(TasksBundle.message("form.task.title"));

        SpinnerModel minutesSpinnerModel = new SpinnerNumberModel(0, 0, 9000, 15);
        minutesSpinner.setModel(minutesSpinnerModel);

        TaskPriority[] priorities = TaskPriority.values();
        priorityComboBox.setRenderer(new PriorityComboBoxRenderer());
        for (TaskPriority priority : priorities) {
            priorityComboBox.addItem(priority);
        }

        priorityComboBox.setSelectedItem(TaskPriority.Normal);

        parentTasksComboBox.setRenderer(new TaskComboBoxRenderer());

        SpinnerModel actualMinutesSpinnerModel = new SpinnerNumberModel(0, 0, 9000, 15);
        actualMinutesSpinner.setModel(actualMinutesSpinnerModel);

        setActualsVisible(settings.isEnableActualTime());

        init();

        getOKAction().setEnabled(false);
        doValidate();

    }

    @Nullable
    protected JComponent createCenterPanel() {
        return container;
    }

    protected void createDefaultActions() {
        super.createDefaultActions();
        addAction = new AddAction();
        addToRootAction = new AddToRootAction();
    }

    public boolean isAddToRoot() {
        return addToRoot;
    }

    @NotNull
    protected Action[] createActions() {
        return new Action[]{addToRootAction, addAction, getCancelAction()};
    }

    public JComponent getPreferredFocusedComponent() {
        return titleTextField;
    }

    public String getTaskTitle() {
        return titleTextField.getText();
    }

    public void setTaskTitle(String title) {
        titleTextField.setText(title);
    }

    public String getTaskDescription() {
        return description.getText();
    }

    public void setTaskDescription(String description) {
        this.description.setText(description);
    }

    public long getEstimatedTime() {
        int minutes = (Integer) minutesSpinner.getValue();

        return minutes * ONE_MINUTE;
    }

    public void setEstimatedTime(long time) {
        minutesSpinner.setValue((int) (time / ONE_MINUTE));
    }

    public long getActualTime() {
        int minutes = (Integer) actualMinutesSpinner.getValue();

        return minutes * ONE_MINUTE;
    }

    public void setActualTime(long actualTime) {
        actualMinutesSpinner.setValue((int) (actualTime / ONE_MINUTE));
    }

    public TaskPriority getPriority() {
        return (TaskPriority) priorityComboBox.getSelectedItem();
    }

    public void setPriority(TaskPriority priority) {
        priorityComboBox.setSelectedItem(priority);
    }

    public JPanel getContainer() {
        return container;
    }

    public void setParentTasksList(ITask rootTask, List<ITask> parentTaskList) {
        parentTasksComboBox.addItem(rootTask);

        for (ITask task : parentTaskList) {
            parentTasksComboBox.addItem(task);
        }

        parentTasksComboBox.setSelectedItem(selectedParentTask);
    }

    public ITask getSelectedParent() {
        Object selectedParent = parentTasksComboBox.getSelectedItem();
        if (selectedParent instanceof ITask) {
            return (ITask) selectedParent;
        }

        return null;
    }

    public void setSelectedParentTask(ITask selectedTask) {
        selectedParentTask = selectedTask;
    }

    public void setActualsVisible(boolean visible) {
        actualMinutesLabel.setVisible(visible);
        actualTimeLabel.setVisible(visible);
        actualMinutesSpinner.setVisible(visible);
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (titleTextField.getText().trim().isEmpty()) {
            return new ValidationInfo(TasksBundle.message("error.no-title", titleTextField));
        }

        return null;
    }

    /**
     * Combo box priority cell renderer.
     */
    class PriorityComboBoxRenderer extends ColoredListCellRenderer {
        public PriorityComboBoxRenderer() {
            setOpaque(true);
        }

        @Override
        protected void customizeCellRenderer(JList list, Object value, int index, boolean selected, boolean hasFocus) {
            TaskPriority priority = (TaskPriority) value;
            setIcon(TasksUIManager.findIcon(priority));
            append(priority.toString());
        }
    }

    /**
     * Combo box tasks cell renderer.
     */
    class TaskComboBoxRenderer extends ColoredListCellRenderer {
        public TaskComboBoxRenderer() {
            setOpaque(true);
        }

        @Override
        protected void customizeCellRenderer(JList list, Object value, int index, boolean selected, boolean hasFocus) {
            ITask task = (ITask) value;
            append(task.getTitle());
            setIcon(TasksUIManager.createIcon(task));
        }
    }

    private class AddAction extends AbstractAction {
        public AddAction() {
            putValue(Action.NAME, TasksBundle.message("actions.add"));
            putValue(DEFAULT_ACTION, Boolean.TRUE);
        }

        public void actionPerformed(ActionEvent event) {
            addToRoot = false;
            doOKAction();
        }
    }

    private class AddToRootAction extends AbstractAction {
        public AddToRootAction() {
            putValue(Action.NAME, TasksBundle.message("actions.add-to-root"));
        }

        public void actionPerformed(ActionEvent event) {
            addToRoot = true;
            doOKAction();
        }
    }
}
