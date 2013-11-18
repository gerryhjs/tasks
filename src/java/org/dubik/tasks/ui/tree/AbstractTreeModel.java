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

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;

/**
 * Provides support for JTree.
 *
 * @author Sergiy Dubovik
 */
abstract public class AbstractTreeModel implements TreeModel {
    private EventListenerList listeners = new EventListenerList();

    public void addTreeModelListener(TreeModelListener listener) {
        listeners.add(TreeModelListener.class, listener);
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        listeners.remove(TreeModelListener.class, listener);
    }

    public void fireTreeNodesChanged(TreeModelEvent e) {
        for (TreeModelListener listener : listeners.getListeners(TreeModelListener.class)) {
            listener.treeNodesChanged(e);
        }
    }

    void fireTreeNodesInserted(TreeModelEvent e) {
        for (TreeModelListener listener : listeners.getListeners(TreeModelListener.class)) {
            listener.treeNodesInserted(e);
        }
    }

    void fireTreeNodesRemoved(TreeModelEvent e) {
        for (TreeModelListener listener : listeners.getListeners(TreeModelListener.class)) {
            listener.treeNodesRemoved(e);
        }
    }

    void fireTreeStructureChanged(TreeModelEvent e) {
        for (TreeModelListener listener : listeners.getListeners(TreeModelListener.class)) {
            listener.treeStructureChanged(e);
        }
    }
}
