package com.wk.paas.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.wk.paas.service.GenerateCodeService;
import org.jetbrains.annotations.NotNull;

/**
 * 一键生成代码
 *
 * @author shimmer
 */
public class GenerateCodeAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        new GenerateCodeService(project).execute();
    }
}
