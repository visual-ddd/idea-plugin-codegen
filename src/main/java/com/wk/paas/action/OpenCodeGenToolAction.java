package com.wk.paas.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.wk.paas.window.SelectElementDialog;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class OpenCodeGenToolAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        assert project != null;
        SwingUtilities.invokeLater(() -> new SelectElementDialog(project));
    }
}
