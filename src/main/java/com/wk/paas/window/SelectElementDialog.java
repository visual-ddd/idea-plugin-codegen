package com.wk.paas.window;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.wk.paas.service.GenerateCodeService;
import com.wk.paas.service.LoginService;
import com.wk.paas.service.QueryAppVersionService;
import com.wk.paas.service.dto.ApplicationDTO;
import com.wk.paas.service.dto.ApplicationVersionDTO;
import com.wk.paas.service.dto.BusinessSceneVersionDTO;
import com.wk.paas.service.dto.DomainDesignVersionDTO;
import com.wk.paas.window.cell.BusinessListCellRenderer;
import com.wk.paas.window.cell.DomainListCellRenderer;
import com.wk.paas.window.setting.BindAppInfoSettings;
import com.wk.paas.window.setting.CodeGenerateConfiguration;
import com.wk.paas.window.setting.LoginAccountInfoSettings;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class SelectElementDialog extends JDialog {

    private final Project project;
    private CodeGenerateConfiguration config;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<DomainDesignVersionDTO> listBindDomain;
    private JList<BusinessSceneVersionDTO> listBindBusiness;
    private JButton refreshButton;
    private JTextField textFieldOutputPath;
    private JButton pathButton;
    private JTextField textProjectPackage;
    private JCheckBox overridePackageCheckBox;
    private JTextField textProjectVersion;
    private JCheckBox overrideVersionCheckBox;
    private JTextField textProjectIdentity;
    private JCheckBox overrideIdentityCheckBox;
    private JButton copyDSLButton;
    private JRadioButton initCodeRadioButton;
    private JRadioButton updateCodeRadioButton;
    private JCheckBox isInitProjectStructCheckBox;
    private JRadioButton colaRadioButton;
    private JRadioButton colaSingleRadioButton;
    private JButton saveButton;
    private JButton bindProjectButton;

    @SneakyThrows
    private void updateListData() {
        LoginAccountInfoSettings instance = LoginAccountInfoSettings.getInstance();
        String mail = instance.getAccount();
        String password = instance.catchPassword();
        if (StringUtils.isBlank(mail) || StringUtils.isBlank(password)) {
            Messages.showMessageDialog(this, "请先登录", "系统警告", Messages.getWarningIcon());
            new LoginDialog();
            return;
        }

        BindAppInfoSettings projectConfig = BindAppInfoSettings.getInstance(project);
        ApplicationDTO applicationDTO = projectConfig.getApplicationDTO();
        ApplicationVersionDTO applicationVersionDTO = projectConfig.getApplicationVersionDTO();
        if (applicationDTO == null || applicationVersionDTO == null) {
            Messages.showMessageDialog("请先关联一个平台应用", "系统警告", Messages.getWarningIcon());
            new BindAppVersion(project);
            return;
        }

        // 更新项目信息
        textProjectIdentity.setText(applicationDTO.getIdentity());
        textProjectPackage.setText(applicationDTO.getPackageName());
        textProjectVersion.setText(applicationVersionDTO.getCurrentVersion());

        ProgressDialog progressDialog = new ProgressDialog(SelectElementDialog.this, "准备加载平台模型...");

        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    new LoginService().login(mail, password);
                    publish(10);
                    publish(20);

                    progressDialog.setTitle("正在加载应用模型...");
                    ApplicationVersionDTO applicationVersionDetail = new QueryAppVersionService().detailQuery(String.valueOf(applicationVersionDTO.getId()));
                    publish(30);
                    publish(40);

                    progressDialog.setTitle("正在加载业务域模型...");
                    List<DomainDesignVersionDTO> domainDesignVersionDTOList = Optional.ofNullable(applicationVersionDetail.getDomainDesignVersionDTOList()).orElse(new ArrayList<>());
                    listBindDomain.setListData(domainDesignVersionDTOList.toArray(new DomainDesignVersionDTO[0]));
                    publish(60);
                    publish(70);

                    progressDialog.setTitle("正在加载业务场景模型...");
                    List<BusinessSceneVersionDTO> businessSceneVersionDTOList = Optional.ofNullable(applicationVersionDetail.getBusinessSceneVersionDTOList()).orElse(new ArrayList<>());
                    listBindBusiness.setListData(businessSceneVersionDTOList.toArray(new BusinessSceneVersionDTO[0]));
                    publish(80);
                    publish(100);

                } catch (Exception exception) {
                    ApplicationManager.getApplication().invokeLater(() -> {
                        Messages.showMessageDialog(project, exception.getMessage(), "更新模块列表失败", Messages.getErrorIcon());
                    });
                }
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                for (int value : chunks) {
                    progressDialog.setValue(value);
                }
            }

            @Override
            protected void done() {
                progressDialog.dispose();
            }
        };

        worker.execute();
        progressDialog.setVisible(true);

    }

    public SelectElementDialog(Project project) {
        this.project = project;
        $$$setupUI$$$();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // 创建一个按钮组并将JRadioButton添加到其中
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(initCodeRadioButton);
        buttonGroup.add(updateCodeRadioButton);

        ButtonGroup buttonGroup2 = new ButtonGroup();
        buttonGroup2.add(colaRadioButton);
        buttonGroup2.add(colaSingleRadioButton);


        // 配置信息
        String overrideProjectIdentity = null;
        String overrideProjectPackage = null;
        String overrideProjectVersion = null;

        config = CodeGenerateConfiguration.getInstance(project);
        if (config != null) {
            initCodeRadioButton.setSelected(config.isInitCodeRadioButton());
            updateCodeRadioButton.setSelected(config.isUpdateCodeRadioButton());
            colaRadioButton.setSelected(config.isColaRadioButton());
            colaSingleRadioButton.setSelected(config.isColaSingleRadioButton());
            isInitProjectStructCheckBox.setSelected(config.isInitProjectStructCheckBox());
            textFieldOutputPath.setText(config.getOutPath());

            DefaultListModel<DomainDesignVersionDTO> domainListModel = new DefaultListModel<>();
            Optional.ofNullable(config.getDomainList())
                    .ifPresent(domainList -> domainList.forEach(domainListModel::addElement));
            listBindDomain.setModel(domainListModel);

            DefaultListModel<BusinessSceneVersionDTO> sceneListModel = new DefaultListModel<>();
            Optional.ofNullable(config.getSceneList())
                    .ifPresent(sceneList -> sceneList.forEach(sceneListModel::addElement));
            listBindBusiness.setModel(sceneListModel);


            Optional.ofNullable(config.getDomainSelectedList())
                    .ifPresent(selectedDomainList -> {
                        ListModel<DomainDesignVersionDTO> model = listBindDomain.getModel();
                        int[] selectedIndices = IntStream.range(0, model.getSize())
                                .filter(i -> selectedDomainList.contains(model.getElementAt(i)))
                                .toArray();
                        if (selectedIndices.length > 0) {
                            listBindDomain.getSelectionModel().setSelectionInterval(selectedIndices[0], selectedIndices[selectedIndices.length - 1]);
                        }
                    });


            Optional.ofNullable(config.getSceneSelectedList())
                    .ifPresent(selectedSceneList -> {
                        int[] selectedIndices = IntStream.range(0, listBindBusiness.getModel().getSize())
                                .filter(i -> selectedSceneList.contains(listBindBusiness.getModel().getElementAt(i)))
                                .toArray();
                        if (selectedIndices.length > 0) {
                            listBindBusiness.setSelectedIndices(selectedIndices);
                        }
                    });

            overrideProjectIdentity = config.getOverrideProjectIdentity();
            overrideProjectPackage = config.getOverrideProjectPackage();
            overrideProjectVersion = config.getOverrideProjectVersion();
        }

        // 应用绑定信息
        BindAppInfoSettings projectConfig = BindAppInfoSettings.getInstance(project);
        ApplicationDTO applicationDTO;
        ApplicationVersionDTO applicationVersionDTO;
        if (projectConfig != null) {
            applicationDTO = projectConfig.getApplicationDTO();
            applicationVersionDTO = projectConfig.getApplicationVersionDTO();

            textProjectIdentity.setText(overrideProjectIdentity == null ? applicationDTO.getIdentity() : overrideProjectIdentity);
            textProjectPackage.setText(overrideProjectPackage == null ? applicationDTO.getPackageName() : overrideProjectPackage);
            textProjectVersion.setText(overrideProjectVersion == null ? applicationVersionDTO.getCurrentVersion() : overrideProjectVersion);
        } else {
            applicationVersionDTO = null;
            applicationDTO = null;
        }

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
        refreshButton.addActionListener(e -> onRefresh());
        saveButton.addActionListener(e -> onSave());
        bindProjectButton.addActionListener(e -> onBindProject());

        listBindDomain.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listBindBusiness.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listBindDomain.setCellRenderer(new DomainListCellRenderer());
        listBindBusiness.setCellRenderer(new BusinessListCellRenderer());

        textFieldOutputPath.setText(project.getBasePath());
        pathButton.addActionListener(e -> {
            File selectedDirectory = null;
            String initialDirectoryPath = textFieldOutputPath.getText();
            if (!initialDirectoryPath.isEmpty()) {
                selectedDirectory = new File(initialDirectoryPath);
            }

            FileDialog fileDialog = new FileDialog((Frame) null, "Select Folder", FileDialog.LOAD);
            fileDialog.setMode(FileDialog.LOAD);
            fileDialog.setDirectory(selectedDirectory != null ? selectedDirectory.getAbsolutePath() : null);
            fileDialog.setVisible(true);

            String selectedFilePath = fileDialog.getFile();
            if (selectedFilePath != null) {
                File file = new File(fileDialog.getDirectory(), selectedFilePath);
                textFieldOutputPath.setText(file.getAbsolutePath());
            }
        });
        overrideIdentityCheckBox.addActionListener(e -> {
            if (overrideIdentityCheckBox.isSelected()) {
                textProjectIdentity.setEnabled(true);
            } else {
                String text = Optional.ofNullable(applicationDTO).map(ApplicationDTO::getIdentity).orElse(StringUtils.EMPTY);
                textProjectIdentity.setText(text);
                textProjectIdentity.setEnabled(false);
            }
        });
        overridePackageCheckBox.addActionListener(e -> {
            if (overridePackageCheckBox.isSelected()) {
                textProjectPackage.setEnabled(true);
            } else {
                String text = Optional.ofNullable(applicationDTO).map(ApplicationDTO::getPackageName).orElse(StringUtils.EMPTY);
                textProjectPackage.setText(text);
                textProjectPackage.setEnabled(false);
            }
        });
        overrideVersionCheckBox.addActionListener(e -> {
            if (overrideVersionCheckBox.isSelected()) {
                textProjectVersion.setEnabled(true);
            } else {
                String text = Optional.ofNullable(applicationVersionDTO).map(ApplicationVersionDTO::getCurrentVersion).orElse(StringUtils.EMPTY);
                textProjectVersion.setText(text);
                textProjectVersion.setEnabled(false);
            }
        });
//        copyDSLButton.addActionListener(e -> {
//            String appDSLJson = buildAppDSLJson();
//            setClipboardString(appDSLJson);
//        });
    }

    private void onRefresh() {
        refreshButton.setEnabled(false);

        // 执行刷新操作的代码...
        updateListData();

        // 刷新操作完成后，重新启用刷新按钮
        refreshButton.setEnabled(true);
    }

    private void onBindProject() {
        new BindAppVersion(project);
        updateListData();
    }

    private void onSave() {
        if (StringUtils.isBlank(textFieldOutputPath.getText())) {
            Messages.showMessageDialog("输出路径不能为空！", "参数错误", Messages.getWarningIcon());
            return;
        }

        if (StringUtils.isBlank(textProjectIdentity.getText())) {
            Messages.showMessageDialog("项目唯一标识不能为空！", "参数错误", Messages.getWarningIcon());
            return;
        }

        if (StringUtils.isBlank(textProjectPackage.getText())) {
            Messages.showMessageDialog("项目包路径不能为空！", "参数错误", Messages.getWarningIcon());
            return;
        }
        if (StringUtils.isBlank(textProjectVersion.getText())) {
            Messages.showMessageDialog("项目版本号不能为空！", "参数错误", Messages.getWarningIcon());
            return;
        }

        // 在用户点击 OK 按钮时保存配置信息
        if (config != null) {
            config.setInitCodeRadioButton(initCodeRadioButton.isSelected());
            config.setUpdateCodeRadioButton(updateCodeRadioButton.isSelected());
            config.setColaRadioButton(colaRadioButton.isSelected());
            config.setColaSingleRadioButton(colaSingleRadioButton.isSelected());
            config.setInitProjectStructCheckBox(isInitProjectStructCheckBox.isSelected());
            config.setOutPath(textFieldOutputPath.getText());

            config.setOverrideProjectIdentity(textProjectIdentity.getText());
            config.setOverrideProjectPackage(textProjectPackage.getText());
            config.setOverrideProjectVersion(textProjectVersion.getText());

            // 更新查询结果列表
            ListModel<DomainDesignVersionDTO> domainListModel = listBindDomain.getModel();
            List<DomainDesignVersionDTO> domainList = new ArrayList<>(domainListModel.getSize());
            for (int i = 0; i < domainListModel.getSize(); i++) {
                domainList.add(domainListModel.getElementAt(i));
            }
            config.setDomainList(domainList);

            ListModel<BusinessSceneVersionDTO> businessListModel = listBindBusiness.getModel();
            List<BusinessSceneVersionDTO> businessList = new ArrayList<>(businessListModel.getSize());
            for (int i = 0; i < businessListModel.getSize(); i++) {
                businessList.add(businessListModel.getElementAt(i));
            }
            config.setSceneList(businessList);

            config.setDomainSelectedList(listBindDomain.getSelectedValuesList());
            config.setSceneSelectedList(listBindBusiness.getSelectedValuesList());

            dispose();
        }
    }

    private void onOK() {

        // 保存配置信息
        onSave();

        int isStart = JOptionPane.showOptionDialog(this, "是否开始执行", "执行",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                Messages.getInformationIcon(), new String[]{"确认", "退出"}, "确认");
        if (isStart == 1) {
            return;
        }

        // 执行代码生成
        new GenerateCodeService(project).execute();
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    /**
     * 把文本设置到剪贴板（复制）
     */
    public static void setClipboardString(String text) {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 封装文本内容
        Transferable trans = new StringSelection(text);
        // 把文本内容设置到系统剪贴板
        clipboard.setContents(trans, null);
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
        contentPane.setLayout(new GridLayoutManager(4, 3, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("生成");
        panel2.add(buttonOK, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("取消");
        panel2.add(buttonCancel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        refreshButton = new JButton();
        refreshButton.setText("刷新");
        panel2.add(refreshButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("业务场景");
        panel3.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("业务域");
        panel3.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listBindDomain = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        listBindDomain.setModel(defaultListModel1);
        scrollPane1.setViewportView(listBindDomain);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel3.add(scrollPane2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listBindBusiness = new JList();
        scrollPane2.setViewportView(listBindBusiness);
        final JLabel label3 = new JLabel();
        label3.setText("输出路径：");
        contentPane.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(2);
        label4.setHorizontalTextPosition(2);
        label4.setText("模块列表");
        label4.setVerticalAlignment(0);
        contentPane.add(label4, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldOutputPath = new JTextField();
        textFieldOutputPath.setText("");
        contentPane.add(textFieldOutputPath, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        pathButton = new JButton();
        pathButton.setText("选择路径");
        contentPane.add(pathButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
