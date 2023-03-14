/*
 * Created by JFormDesigner on Fri Mar 03 23:45:34 CST 2023
 */

package com.wk.paas.window;

import javax.swing.*;
import java.awt.*;

/**
 * @author shimmer
 */
public class CodeGenerateTool extends JFrame {

    public CodeGenerateTool() {
        initComponents();
    }

    public static void main(String[] args) {
        CodeGenerateTool codeGenerateTool = new CodeGenerateTool();
        codeGenerateTool.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off

        //======== this ========
        setTitle("\u4ee3\u7801\u751f\u6210\u5de5\u5177");
        setAlwaysOnTop(true);
        setMinimumSize(new Dimension(800, 500));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
