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
package org.dubik.tasks.ui.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.dubik.tasks.ui.tree.TreeController;

/**
 * @author Sergiy Dubovik
 */
public class ToggleCompletedTaskVisibleAction extends BaseToggleTaskAction {

    @Override
    public boolean isSelected(AnActionEvent e) {
        TreeController treeController = getTreeController(e);
        return treeController != null && treeController.isHideCompletedTasks();

    }

    @Override
    public void setSelected(AnActionEvent e, boolean state) {
        TreeController treeController = getTreeController(e);
        if (state) {
            treeController.hideCompletedTasks(true);
        }
        else {
            treeController.hideCompletedTasks(false);
        }

    }

}

