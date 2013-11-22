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

package org.dubik.tasks.utils;

import org.dubik.tasks.model.ITask;
import org.dubik.tasks.model.impl.Task;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TaskTimer {
    private static Map<ITask, TaskTimer.Runner> timers = new HashMap<ITask, TaskTimer.Runner>();

    public static void startTask(ITask task) {
        Runner runner = new Runner(task);
        runner.setRepeats(true);
        timers.put(task, runner);
        runner.start();

    }

    public static void stopTask(ITask task) {
        Runner runner = timers.get(task);
        if (runner != null) {
            runner.stop();
            timers.remove(task);
        }
    }

    public static void stopAllTimers() {
        for (Iterator<Map.Entry<ITask, Runner>> iterator = timers.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<ITask, Runner> entry = iterator.next();
            entry.getValue().stop();
            iterator.remove();
        }
    }

    private static class Runner extends Timer {
        public Runner(final ITask task) {
            super(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ((Task)task).setActualTime( task.getActualTime() + 1);
                }
            });
        }


    }



}
