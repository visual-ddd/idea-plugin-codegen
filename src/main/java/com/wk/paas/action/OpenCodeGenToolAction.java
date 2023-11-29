package com.wk.paas.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.wk.paas.window.SelectElementDialog;
import org.jetbrains.annotations.NotNull;

public class OpenCodeGenToolAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        assert project != null;
        SelectElementDialog dialog = new SelectElementDialog(project);
        dialog.setTitle("代码生成器");
        dialog.setSize(700, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
