package com.wk.paas.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.wk.paas.window.BindAppVersion;
import org.jetbrains.annotations.NotNull;

/**
 * @author shimmer
 */
public class OpenPlatformRelatedSettingAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        BindAppVersion dialog = new BindAppVersion();
    }
}
