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

import org.dubik.tasks.model.ITask;

import java.awt.datatransfer.*;
import java.util.*;

/**
 * @author Sergiy Dubovik
 */
public class TransferableTask implements Transferable {
    public static final DataFlavor TASK_FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, "Task");
    private static final DataFlavor[] flavors = {TASK_FLAVOR};

    private List<ITask> tasks;

    public TransferableTask(List<ITask> tasks) {
        this.tasks = new ArrayList<ITask>(tasks);
    }

    public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (flavor == TASK_FLAVOR) {
            return tasks;
        }
        else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return Arrays.asList(flavors).contains(flavor);
    }


}
