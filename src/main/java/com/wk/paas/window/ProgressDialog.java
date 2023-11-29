package com.wk.paas.window;

import javax.swing.*;
import java.awt.*;

public class ProgressDialog extends JDialog {

    private JProgressBar progressBar;
    private JPanel panel;

    public ProgressDialog(Dialog parent, String title) {
        super(parent, title, true);
        progressBar.setIndeterminate(true); // 设置进度条为不确定模式，因为我们不知道这个代码块要执行多长时间
        getContentPane().add(panel, BorderLayout.CENTER);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 75);
        setLocationRelativeTo(parent);
    }

    public void setValue(int value) {
        progressBar.setIndeterminate(false);// 设置进度条为确定模式，因为我们知道这个代码块已经执行了一部分
        progressBar.setValue(value);
    }
}