/*
 * Created by JFormDesigner on Fri Mar 03 19:04:09 CST 2023
 */

package com.wk.paas.window;

import java.awt.event.*;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * @author shimmer
 */
public class RemoteModel extends JPanel {

    private final Project project;

    public RemoteModel(@NotNull Project project) {
        initComponents();
        this.project = project;
    }

    private void scan(ActionEvent e) {
        System.out.println(project.getBasePath());
    }

    private void openCodeGenToolButtonMouseClicked(MouseEvent e) {
        CodeGenerateTool dialog = new CodeGenerateTool();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        this2 = new JPanel();
        menuBar1 = new JMenuBar();
        openCodeGenToolButton = new JButton();
        scanButton = new JButton();
        scrollPane1 = new JBScrollPane();
        modelTree = new Tree();

        //======== this ========
        setLayout(new BorderLayout());

        //======== this2 ========
        {
            this2.setLayout(new BorderLayout());

            //======== menuBar1 ========
            {

                //---- openCodeGenToolButton ----
                openCodeGenToolButton.setText("\u4ee3\u7801\u751f\u6210\u5de5\u5177");
                openCodeGenToolButton.setIcon(new ImageIcon(getClass().getResource("/vcs/merge_dark.png")));
                openCodeGenToolButton.addActionListener(e -> scan(e));
                openCodeGenToolButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        openCodeGenToolButtonMouseClicked(e);
                    }
                });
                menuBar1.add(openCodeGenToolButton);

                //---- scanButton ----
                scanButton.setText("\u62c9\u53d6\u8fdc\u7a0b\u6a21\u578b");
                scanButton.setIcon(new ImageIcon(getClass().getResource("/vcs/shelveSilent.png")));
                scanButton.addActionListener(e -> scan(e));
                menuBar1.add(scanButton);
            }
            this2.add(menuBar1, BorderLayout.NORTH);

            //======== scrollPane1 ========
            {
                scrollPane1.setViewportView(modelTree);
            }
            this2.add(scrollPane1, BorderLayout.CENTER);
        }
        add(this2, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel this2;
    private JMenuBar menuBar1;
    private JButton openCodeGenToolButton;
    private JButton scanButton;
    private JBScrollPane scrollPane1;
    private Tree modelTree;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
