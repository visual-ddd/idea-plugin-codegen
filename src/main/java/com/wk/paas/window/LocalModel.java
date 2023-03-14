/*
 * Created by JFormDesigner on Wed Feb 01 13:14:55 CST 2023
 */

package com.wk.paas.window;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.*;
import com.intellij.ui.treeStructure.*;
import com.intellij.uiDesigner.core.*;
import org.jdesktop.swingx.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author shimmer
 */
public class LocalModel extends JPanel {

    private Project project;

    public LocalModel(@NotNull Project project) {
        initComponents();
        this.project = project;
    }

    private void scan(ActionEvent e) {
        System.out.println(project.getBasePath());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        menuBar1 = new JMenuBar();
        scanButton = new JButton();
        pushButton = new JButton();
        scrollPane1 = new JBScrollPane();
        modelTree = new Tree();

        //======== this ========
        setLayout(new BorderLayout());

        //======== menuBar1 ========
        {

            //---- scanButton ----
            scanButton.setText("\u626b\u63cf\u672c\u5730\u6a21\u578b");
            scanButton.setIcon(new ImageIcon(getClass().getResource("/gutter/colors.png")));
            scanButton.addActionListener(e -> scan(e));
            menuBar1.add(scanButton);

            //---- pushButton ----
            pushButton.setText("\u4e0a\u4f20\u6a21\u578b\u53d8\u66f4");
            pushButton.setIcon(new ImageIcon(getClass().getResource("/vcs/push.png")));
            menuBar1.add(pushButton);
        }
        add(menuBar1, BorderLayout.NORTH);

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(modelTree);
        }
        add(scrollPane1, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JMenuBar menuBar1;
    private JButton scanButton;
    private JButton pushButton;
    private JBScrollPane scrollPane1;
    private Tree modelTree;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
