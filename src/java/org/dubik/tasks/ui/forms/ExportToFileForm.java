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

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.dubik.tasks.TasksBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * @author Sergiy Dubovik
 */
public class ExportToFileForm extends DialogWrapper {
    private Project project;
    private JTextArea previewTextArea;
    private JPanel container;
    private JLabel errorLabel;
    private TextFieldWithBrowseButton browse;
    private Action exportToClipboardAction;
    private boolean exportToClipboard;

    public ExportToFileForm(Project project) {
        super(project, false);

        this.project = project;

        setTitle(TasksBundle.message("form.export.title"));
        init();
        setSize(640, 580);
    }

    private void createUIComponents() {
        browse = new TextFieldWithBrowseButton();
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, true, false, false, false, false);
        browse.addBrowseFolderListener(TasksBundle.message("form.export.title"),
                TasksBundle.message("form.export.browsefolder.description"), project, descriptor);

        browse.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validateForm();
            }

            public void removeUpdate(DocumentEvent e) {
                validateForm();
            }

            public void changedUpdate(DocumentEvent e) {
                validateForm();
            }
        });
    }

    private void validateForm() {
        File file = getFile();
        if (file.isDirectory()) {
            errorLabel.setText( TasksBundle.message("form.export.not-valid"));
            getOKAction().setEnabled(false);
        }
        else {
            errorLabel.setText("");
            getOKAction().setEnabled(true);
        }
    }

    public boolean isExportToClipboard() {
        return exportToClipboard;
    }

    @Nullable
    protected JComponent createCenterPanel() {
        return container;
    }

    public void setPreview(@Nullable String preview) {
        previewTextArea.setText(preview);
    }

    private void exportToClipboard() {
        exportToClipboard = true;
    }

    protected void createDefaultActions() {
        super.createDefaultActions();

        exportToClipboardAction = new ExportToClipboardAction();
    }

    @NotNull
    protected Action[] createActions() {
        return new Action[]{exportToClipboardAction, getOKAction(), getCancelAction()};
    }

    public File getFile() {
        return new File(browse.getText());
    }

    class ExportToClipboardAction extends AbstractAction {
        public ExportToClipboardAction() {
            super(TasksBundle.message("form.export.copy-to-clipboard"));
        }

        public void actionPerformed(ActionEvent event) {
            exportToClipboard();
            doOKAction();
        }
    }
}
