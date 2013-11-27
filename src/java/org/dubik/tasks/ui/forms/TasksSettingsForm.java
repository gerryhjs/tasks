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
package org.dubik.tasks.ui.forms;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.dubik.tasks.TasksBundle;
import org.dubik.tasks.settings.TaskSettings;
import org.dubik.tasks.settings.TaskSettingsService;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Sergiy Dubovik
 */
public class TasksSettingsForm implements Configurable {
    private JPanel container;

    private JCheckBox enableTaskScopesCheckBox;
    private JCheckBox propagatePriorityCheckBox;
    private JCheckBox oneLevelOnlyCheckBox;
    private JCheckBox enableActualTimeFeatureCheckBox;
    private JCheckBox showEnterActualTimeCheckBox;

    private TaskSettings data = TaskSettingsService.getSettings();

    public TasksSettingsForm() {
        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                updateControls();
            }
        };

        enableActualTimeFeatureCheckBox.addChangeListener(changeListener);
        propagatePriorityCheckBox.addChangeListener(changeListener);

        updateControls();
    }


    private void updateControls() {
        showEnterActualTimeCheckBox.setEnabled(enableActualTimeFeatureCheckBox.isSelected());
        oneLevelOnlyCheckBox.setEnabled(propagatePriorityCheckBox.isSelected());
    }

    @Nls
    public String getDisplayName() {
        return TasksBundle.message("application.component.displayname");
    }

    @Nullable
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    public JComponent createComponent() {
        return container;
    }

    public boolean isModified() {
        return data.isAskActualWhenCompleteTask() != showEnterActualTimeCheckBox.isSelected()
               || data.isEnableActualTime() != enableActualTimeFeatureCheckBox.isSelected()
               || data.isPropagatePriority() != propagatePriorityCheckBox.isSelected()
               || data.isPriorityPropagatedOneLevelOnly() != oneLevelOnlyCheckBox.isSelected();
    }

    public void apply() throws ConfigurationException {
        data.setEnableActualTime(enableActualTimeFeatureCheckBox.isSelected());
        data.setAskActualWhenCompleteTask(showEnterActualTimeCheckBox.isSelected());
        data.setPropagatePriority(propagatePriorityCheckBox.isSelected());
        data.setPriorityPropagatedOneLevelOnly(oneLevelOnlyCheckBox.isSelected());

    }

    public void reset() {
        enableActualTimeFeatureCheckBox.setSelected(data.isEnableActualTime());
        showEnterActualTimeCheckBox.setSelected(data.isAskActualWhenCompleteTask());
        propagatePriorityCheckBox.setSelected(data.isPropagatePriority());
        oneLevelOnlyCheckBox.setSelected(data.isPriorityPropagatedOneLevelOnly());
        updateControls();
    }

    public void disposeUIResources() {

    }
}
