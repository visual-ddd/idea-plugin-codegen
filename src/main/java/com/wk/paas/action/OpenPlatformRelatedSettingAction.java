package com.wk.paas.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.wk.paas.window.BindAppVersion;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author shimmer
 */
public class OpenPlatformRelatedSettingAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        SwingUtilities.invokeLater(() -> new BindAppVersion(project));
    }
}
