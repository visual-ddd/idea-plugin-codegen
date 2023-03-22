package com.wk.paas.window;

import com.wk.paas.service.LoginService;
import com.wk.paas.window.setting.LoginAccountInfoSettings;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {


    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldPassword;
    private JTextField textFieldMail;

    public LoginDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        LoginAccountInfoSettings instance = LoginAccountInfoSettings.getInstance();
        textFieldMail.setText(instance.getAccount());
        textFieldPassword.setText(instance.catchPassword());

        setTitle("登录");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void onOK() {
        String mailText = this.textFieldMail.getText();
        String passwordText = this.textFieldPassword.getText();

        if (StringUtils.isBlank(mailText) || StringUtils.isBlank(passwordText)) {
            JOptionPane.showMessageDialog(this, "账号和密码不能为空！", "参数错误", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LoginService loginService = new LoginService();
        try {
            loginService.login(mailText, passwordText);
        } catch (IllegalStateException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(), "系统错误", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(), "登录错误", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "成功！", "登录成功", JOptionPane.INFORMATION_MESSAGE);

        LoginAccountInfoSettings instance = LoginAccountInfoSettings.getInstance();
        instance.setAccount(mailText);
        instance.storePassword(passwordText);

        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        new LoginDialog();
    }
}