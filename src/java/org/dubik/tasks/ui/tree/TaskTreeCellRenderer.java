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
package org.dubik.tasks.ui.tree;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleTextAttributes;
import org.dubik.tasks.TaskSettings;
import org.dubik.tasks.TasksApplicationComponent;
import org.dubik.tasks.TasksBundle;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.model.ITaskGroup;
import org.dubik.tasks.model.TaskPriority;
import org.dubik.tasks.ui.TasksUIManager;

import javax.swing.*;

/**
 * Renders items in task tree.
 *
 * @author Sergiy Dubovik
 */
public class TaskTreeCellRenderer extends ColoredTreeCellRenderer {
    private static final SimpleTextAttributes STRIKEOUT_REGULAR_ATTRIBUTES = new SimpleTextAttributes(SimpleTextAttributes.STYLE_STRIKEOUT, JBColor.GRAY);
    private static final SimpleTextAttributes STRIKEOUT_GRAY_ATTRIBUTES = new SimpleTextAttributes(SimpleTextAttributes.STYLE_STRIKEOUT, JBColor.GRAY);
    private static final SimpleTextAttributes BOLD = new SimpleTextAttributes(SimpleTextAttributes.STYLE_BOLD,
            SimpleTextAttributes.REGULAR_ATTRIBUTES.getFgColor());

    private TaskSettings settings;

    public TaskTreeCellRenderer() {
        TasksApplicationComponent application =
                ApplicationManager.getApplication().getComponent(TasksApplicationComponent.class);
        settings = application.getSettings();
    }

    public void customizeCellRenderer(JTree tree, Object value,
                                      boolean selected, boolean expanded,
                                      boolean leaf, int row, boolean hasFocus) {
        if (!(value instanceof ITask)) {
            return;
        }

        ITask task = (ITask) value;

        SimpleTextAttributes titleAttr = SimpleTextAttributes.REGULAR_ATTRIBUTES;
        SimpleTextAttributes restAttr = SimpleTextAttributes.GRAY_ATTRIBUTES;
        SimpleTextAttributes groupTitleAttr = SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES;

        if (task.isRunning()) {
            titleAttr = SimpleTextAttributes.merge(titleAttr, BOLD);
        }

        if (task.isCompleted()) {
            titleAttr = STRIKEOUT_REGULAR_ATTRIBUTES;
            restAttr = STRIKEOUT_GRAY_ATTRIBUTES;
        }

        if (value instanceof ITaskGroup) {
            ITaskGroup taskGroup = (ITaskGroup) value;
            try {
                TaskPriority groupPriority = TaskPriority.parse(taskGroup.getTitle());
                setIcon(TasksUIManager.findIcon(groupPriority));
            }
            catch (IllegalArgumentException e) {
                setIcon(TasksUIManager.getIcon(TasksUIManager.ICON_TASK));
            }

            append(task.getTitle(), groupTitleAttr);
            append(" ", titleAttr);
            String details = makeDetailsForGroup(taskGroup);
            append(details, restAttr);
        }
        else {
            setIcon(TasksUIManager.createIcon(task));
            append(task.getTitle(), titleAttr);
            String details = makeDetailsForTask(task);
            if (details.length() != 0) {
                append(" ", restAttr);
                append(details, restAttr);
            }
        }

        String tooltip = makeTooltipFromTask(task);
        setToolTipText(tooltip);

        setIconTextGap(3);

        boolean isDropCell = false;

        JTree.DropLocation dropLocation = tree.getDropLocation();
        if (dropLocation != null && dropLocation.getChildIndex() == -1 && tree.getRowForPath(dropLocation.getPath()) == row) {
            isDropCell = true;
        }

        setBorder(isDropCell ? BorderFactory.createLineBorder(JBColor.RED) : null);

    }

    private String makeDetailsForTask(ITask task) {
        int totalTasks = task.size();
        long estimated = task.getEstimatedTime();
        StringBuilder details = new StringBuilder();
        long actual = task.getActualTime();
        if (totalTasks == 0) {
            if (settings.isEnableActualTime()) {
                if (estimated != 0 || actual != 0) {
                    details.append("(");
                }

                if (estimated != 0) {
                    details.append("Estimated: ");
                    details.append(makeStringFromTime(estimated));
                    if (actual != 0) {
                        details.append(", ");
                    }
                }

                if (actual != 0) {
                    details.append("Actual: ");
                    details.append(makeStringFromTime(actual));
                }

                if (estimated != 0 || actual != 0) {
                    details.append(")");
                }

            }
            else {
                if (estimated != 0) {
                    details.append("(");
                    details.append(makeStringFromTime(estimated));
                    details.append(")");
                }
            }
        }
        else {
            details.append("(").append(totalTasks).append(" Tasks, ").append(task.getCompletionRatio()).append("% Completed");
            if (estimated != 0) {
                details.append(", Estimated: ").append(makeStringFromTime(estimated));
            }

            if (settings.isEnableActualTime() && actual != 0) {
                details.append(", Actual: ").append(makeStringFromTime(actual));
            }

            details.append(")");
        }

        return details.toString();
    }

    private String makePercentageForTask(ITask task) {
        StringBuilder buffer = new StringBuilder();
        int complInPerc = task.getCompletionRatio();
        buffer.append(Integer.toString(complInPerc));
        return buffer.toString();
    }

    private String makeDetailsForGroup(ITaskGroup taskGroup) {
        int totalTasks = taskGroup.size();
        int completedTasks = 0;
        for (int i = 0; i < totalTasks; i++) {
            ITask task = taskGroup.get(i);
            if (task.isCompleted()) {
                completedTasks++;
            }
        }

        StringBuilder details = new StringBuilder();
        details.append("(");
        details.append(totalTasks);
        details.append(" Tasks, ");
        int complInPerc = 0;
        if (totalTasks > 0) {
            complInPerc = completedTasks * 100 / totalTasks;
        }
        details.append(complInPerc);
        details.append("% Completed)");

        return details.toString();
    }

    private String makeStringFromTime(long seconds) {
        int inMinutes = (int) (seconds / 60);
        int hours = inMinutes / 60;
        int minutes = inMinutes % 60;

        StringBuilder timeStr = new StringBuilder();

        if (hours != 0) {
            timeStr.append(Integer.toString(hours));
            timeStr.append("h");
        }

        timeStr.append(Integer.toString(minutes));
        timeStr.append("m");

        return timeStr.toString();
    }

    private String makeTooltipFromTask(ITask task) {
        if (task.size() != 0) {
            int subTasks = task.size();
            int complete = completed(task);
            int incomplete = subTasks - complete;
            return TasksBundle.message("tree.tooltip.template", task.getTitle(), task.size(), complete, incomplete);
        }
        return null;
    }

    private int completed(ITask task) {
        int completed = 0;
        for (int i = 0; i < task.size(); i++) {
            if (task.get(i).isCompleted()) {
                completed++;
            }
        }

        return completed;
    }
}