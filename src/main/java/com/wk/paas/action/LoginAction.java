package com.wk.paas.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.wk.paas.window.LoginDialog;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author shimmer
 */
public class LoginAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        SwingUtilities.invokeLater(LoginDialog::new);
    }

}