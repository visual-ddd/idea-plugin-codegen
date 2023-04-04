package com.wk.paas.window;

import com.intellij.openapi.ui.Messages;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.wk.paas.service.LoginService;
import com.wk.paas.service.QueryAppService;
import com.wk.paas.service.QueryAppVersionService;
import com.wk.paas.service.QueryTeamService;
import com.wk.paas.service.dto.ApplicationDTO;
import com.wk.paas.service.dto.ApplicationVersionDTO;
import com.wk.paas.service.dto.TeamDTO;
import com.wk.paas.window.cell.AppListCellRenderer;
import com.wk.paas.window.cell.AppVersionListCellRenderer;
import com.wk.paas.window.cell.TeamListCellRenderer;
import com.wk.paas.window.setting.BindAppInfoSettings;
import com.wk.paas.window.setting.LoginAccountInfoSettings;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Optional;

public class BindAppVersion extends JDialog {

    private TeamListCellRenderer teamListCellRenderer;
    private AppListCellRenderer appListCellRenderer;
    private AppVersionListCellRenderer appVersionListCellRenderer;
    private final DefaultComboBoxModel<TeamDTO> teamInfos = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<ApplicationDTO> appInfos = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<ApplicationVersionDTO> appVersionInfos = new DefaultComboBoxModel<>();
    private JPanel contentPane;
    private JButton buttonOK, buttonCancel;
    private JComboBox<TeamDTO> boxTeam;
    private JComboBox<ApplicationDTO> boxApp;
    private JComboBox<ApplicationVersionDTO> boxAppVersion;
    private JLabel labelAppVersion, labelApp, labelTeam;

    private void updateTeamListData() {
        String mail;
        String password;

        LoginAccountInfoSettings instance = LoginAccountInfoSettings.getInstance();
        mail = instance.getAccount();
        password = instance.catchPassword();
        if (StringUtils.isBlank(mail) || StringUtils.isBlank(password)) {
            Messages.showMessageDialog(this, "请先登录", "系统警告", Messages.getWarningIcon());
            new LoginDialog();
            return;
        }

        teamInfos.removeAllElements();
        List<TeamDTO> teamDTOList;
        try {
            new LoginService().login(mail, password);
            teamDTOList = new QueryTeamService().query();
        } catch (Exception exception) {
            Messages.showMessageDialog(this, exception.getMessage(), "系统错误", Messages.getErrorIcon());
            return;
        }
        teamInfos.addAll(teamDTOList);
        boxTeam.setModel(teamInfos);
    }

    private void updateAppListData(Long teamId) {
        appInfos.removeAllElements();
        List<ApplicationDTO> applicationDTOList;
        try {
            applicationDTOList = new QueryAppService().queryByTeamId(String.valueOf(teamId));
        } catch (Exception exception) {
            Messages.showMessageDialog(exception.getMessage(), "系统错误", Messages.getErrorIcon());
            return;
        }
        appInfos.addAll(applicationDTOList);
        boxApp.setModel(appInfos);
    }

    private void updateAppVersionListData(Long appId) {
        appVersionInfos.removeAllElements();
        List<ApplicationVersionDTO> applicationVersionDTOList;
        try {
            applicationVersionDTOList = new QueryAppVersionService().queryByAppId(String.valueOf(appId));
        } catch (Exception exception) {
            Messages.showMessageDialog(exception.getMessage(), "系统错误", Messages.getErrorIcon());
            return;
        }
        appVersionInfos.addAll(applicationVersionDTOList);
        boxAppVersion.setModel(appVersionInfos);
    }

    public BindAppVersion() {
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

        boxTeam.addItemListener(e -> {
            boxApp.removeAllItems();
            boxAppVersion.removeAllItems();
            TeamDTO selectedItem = (TeamDTO) e.getItem();
            updateAppListData(selectedItem.getId());
        });
        boxApp.addItemListener(e -> {
            boxAppVersion.removeAllItems();
            ApplicationDTO selectedItem = (ApplicationDTO) e.getItem();
            updateAppVersionListData(selectedItem.getId());
        });

        teamListCellRenderer = new TeamListCellRenderer();
        appListCellRenderer = new AppListCellRenderer();
        appVersionListCellRenderer = new AppVersionListCellRenderer();
        boxTeam.setRenderer(teamListCellRenderer);
        boxApp.setRenderer(appListCellRenderer);
        boxAppVersion.setRenderer(appVersionListCellRenderer);

        updateTeamListData();

        BindAppInfoSettings instance = BindAppInfoSettings.getInstance();
        boxTeam.setSelectedItem(instance.getTeamDTO());
        boxApp.setSelectedItem(instance.getApplicationDTO());
        boxAppVersion.setSelectedItem(instance.getApplicationVersionDTO());

        setTitle("配置项目关联应用信息");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void onOK() {
        BindAppInfoSettings bindAppInfoSettings = BindAppInfoSettings.getInstance();
        Object teamSelectItem;
        Object appSelectedItem;
        Object appVersionSelectedItem;
        try {
            teamSelectItem = Optional.ofNullable(boxTeam.getSelectedItem())
                    .orElseThrow(() -> new IllegalStateException("请选择一个团队"));
            appSelectedItem = Optional.ofNullable(boxApp.getSelectedItem())
                    .orElseThrow(() -> new IllegalStateException("请选择一个应用"));
            appVersionSelectedItem = Optional.ofNullable(boxAppVersion.getSelectedItem())
                    .orElseThrow(() -> new IllegalStateException("请选择一个应用版本"));
        } catch (Exception e) {
            Messages.showMessageDialog(this, e.getMessage(), "系统警告", Messages.getWarningIcon());
            return;
        }

        bindAppInfoSettings.setTeamDTO((TeamDTO) teamSelectItem);
        bindAppInfoSettings.setApplicationDTO((ApplicationDTO) appSelectedItem);
        bindAppInfoSettings.setApplicationVersionDTO((ApplicationVersionDTO) appVersionSelectedItem);

        dispose();
    }

    private void onCancel() {
        dispose();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("确定");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("取消");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setIconTextGap(4);
        label1.setText("关联平台信息");
        contentPane.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel3.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelApp = new JLabel();
        labelApp.setText("关联应用：");
        panel4.add(labelApp);
        boxApp = new JComboBox();
        boxApp.setPreferredSize(new Dimension(278, 30));
        panel4.add(boxApp);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel3.add(panel5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelAppVersion = new JLabel();
        labelAppVersion.setText("应用版本号：");
        panel5.add(labelAppVersion);
        boxAppVersion = new JComboBox();
        boxAppVersion.setOpaque(true);
        boxAppVersion.setPreferredSize(new Dimension(278, 30));
        panel5.add(boxAppVersion);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel3.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelTeam = new JLabel();
        labelTeam.setText("所属团队：");
        panel6.add(labelTeam);
        boxTeam = new JComboBox();
        boxTeam.setLightWeightPopupEnabled(false);
        boxTeam.setMaximumRowCount(8);
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        boxTeam.setModel(defaultComboBoxModel1);
        boxTeam.setOpaque(false);
        boxTeam.setPopupVisible(false);
        boxTeam.setPreferredSize(new Dimension(278, 30));
        boxTeam.setToolTipText("请关联一个团队");
        panel6.add(boxTeam);
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
