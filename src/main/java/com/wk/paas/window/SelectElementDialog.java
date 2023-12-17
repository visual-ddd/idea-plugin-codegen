package com.wk.paas.window;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.wd.paas.generator.CodeGenerateService;
import com.wd.paas.generator.common.enums.GenerateOperationTypeEnum;
import com.wd.paas.generator.common.enums.ProjectTemplateType;
import com.wd.paas.generator.generate.visitor.velocitytemplate.TemplateContext;
import com.wd.paas.generator.generate.visitor.velocitytemplate.TemplateVisitor;
import com.wk.paas.service.LoginService;
import com.wk.paas.service.QueryAppVersionService;
import com.wk.paas.service.QueryBusinessInfoService;
import com.wk.paas.service.QueryDomainInfoService;
import com.wk.paas.service.dto.*;
import com.wk.paas.window.cell.BusinessListCellRenderer;
import com.wk.paas.window.cell.DomainListCellRenderer;
import com.wk.paas.window.setting.AppDSLBuilder;
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
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class SelectElementDialog extends JDialog {

    private Project project;
    private DomainListCellRenderer domainListCellRenderer;
    private BusinessListCellRenderer businessListCellRenderer;
    private DefaultListModel domainList = new DefaultListModel();
    private DefaultListModel businessList = new DefaultListModel();

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList listBindDomain;
    private JList listBindBusiness;
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

    private ApplicationDTO applicationDTO;
    private ApplicationVersionDTO applicationVersionDTO;
    private List<DomainDesignVersionDTO> domainDesignVersionDTOList;
    private List<BusinessSceneVersionDTO> businessSceneVersionDTOList;

    // 配置信息
    private final CodeGenerateConfiguration config;
    private BindAppInfoSettings projectConfig;

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

        projectConfig = BindAppInfoSettings.getInstance(project);
        applicationDTO = projectConfig.getApplicationDTO();
        applicationVersionDTO = projectConfig.getApplicationVersionDTO();
        if (applicationDTO == null || applicationVersionDTO == null) {
            Messages.showMessageDialog("请先关联一个平台应用", "系统警告", Messages.getWarningIcon());
            new BindAppVersion(project);
            return;
        }

        // 更新项目信息
        textProjectIdentity.setText(applicationDTO.getIdentity());
        textProjectPackage.setText(applicationDTO.getPackageName());
        textProjectVersion.setText(applicationVersionDTO.getCurrentVersion());

        domainList.clear();
        businessList.clear();

        AtomicReference<ApplicationVersionDTO> applicationVersionInfo = new AtomicReference<>();


        ProgressDialog progressDialog = new ProgressDialog(SelectElementDialog.this, "正在加载平台模型...");
        Thread thread = new Thread(() -> {
            try {
                new LoginService().login(mail, password);
                progressDialog.setValue(10);
                progressDialog.setValue(20);

                applicationVersionInfo.set(new QueryAppVersionService().detailQuery(String.valueOf(this.applicationVersionDTO.getId())));
                progressDialog.setValue(30);
                progressDialog.setValue(40);

                domainDesignVersionDTOList = Optional.ofNullable(applicationVersionInfo.get().getDomainDesignVersionDTOList()).orElse(new ArrayList<>());
                businessSceneVersionDTOList = Optional.ofNullable(applicationVersionInfo.get().getBusinessSceneVersionDTOList()).orElse(new ArrayList<>());

                for (DomainDesignVersionDTO domainDesignVersionDTO : domainDesignVersionDTOList) {
                    Long domainDesignId = domainDesignVersionDTO.getDomainDesignId();
                    DomainDesignDTO domainDesignDTO = new QueryDomainInfoService().queryByDomainId(domainDesignId);
                    domainDesignVersionDTO.setDomainDesignDTO(domainDesignDTO);
                }
                progressDialog.setValue(60);
                progressDialog.setValue(70);

                for (BusinessSceneVersionDTO businessSceneVersionDTO : businessSceneVersionDTOList) {
                    Long businessSceneId = businessSceneVersionDTO.getBusinessSceneId();
                    BusinessSceneDTO businessSceneDTO = new QueryBusinessInfoService().queryByBusinessId(businessSceneId);
                    businessSceneVersionDTO.setBusinessSceneDTO(businessSceneDTO);
                }
                progressDialog.setValue(80);
                progressDialog.setValue(100); // 将进度条的值设置为 100%

            } catch (Exception exception) {
                Messages.showMessageDialog(project, exception.getMessage(), "系统错误", Messages.getErrorIcon());
            } finally {
                progressDialog.dispose();
            }
        });
        thread.start();
        progressDialog.setVisible(true);
        thread.join(1000);

        domainList.addAll(domainDesignVersionDTOList);
        businessList.addAll(businessSceneVersionDTOList);
        listBindDomain.setListData(domainList.toArray());
        listBindBusiness.setListData(businessList.toArray());
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

        // 获取配置信息
        config = CodeGenerateConfiguration.getInstance(project);
        if (config != null) {
            initCodeRadioButton.setSelected(config.isInitCodeRadioButton());
            updateCodeRadioButton.setSelected(config.isUpdateCodeRadioButton());
            colaRadioButton.setSelected(config.isColaRadioButton());
            colaSingleRadioButton.setSelected(config.isColaSingleRadioButton());
            isInitProjectStructCheckBox.setSelected(config.isInitProjectStructCheckBox());
        }

        projectConfig = BindAppInfoSettings.getInstance(project);
        if (projectConfig != null) {
            applicationDTO = projectConfig.getApplicationDTO();
            applicationVersionDTO = projectConfig.getApplicationVersionDTO();

            textProjectIdentity.setText(applicationDTO.getIdentity());
            textProjectPackage.setText(applicationDTO.getPackageName());
            textProjectVersion.setText(applicationVersionDTO.getCurrentVersion());
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

        domainListCellRenderer = new DomainListCellRenderer();
        businessListCellRenderer = new BusinessListCellRenderer();
        listBindDomain.setCellRenderer(domainListCellRenderer);
        listBindBusiness.setCellRenderer(businessListCellRenderer);
        refreshButton.addActionListener(e -> onRefresh());
        listBindDomain.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (index0 == index1 && (isSelectedIndex(index0))) {
                    removeSelectionInterval(index0, index0);
                    return;
                }
                super.setSelectionInterval(index0, index1);
            }

            @Override
            public void addSelectionInterval(int index0, int index1) {
                if (index0 == index1) {
                    if (isSelectedIndex(index0)) {
                        removeSelectionInterval(index0, index0);
                        return;
                    }
                    super.addSelectionInterval(index0, index1);
                }
            }
        });
        listBindBusiness.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (index0 == index1 && (isSelectedIndex(index0))) {
                    removeSelectionInterval(index0, index0);
                    return;
                }
                super.setSelectionInterval(index0, index1);
            }

            @Override
            public void addSelectionInterval(int index0, int index1) {
                if (index0 == index1) {
                    if (isSelectedIndex(index0)) {
                        removeSelectionInterval(index0, index0);
                        return;
                    }
                    super.addSelectionInterval(index0, index1);
                }
            }
        });
        textFieldOutputPath.setText(project.getBasePath());
        pathButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(textFieldOutputPath.getText());
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                textFieldOutputPath.setText(file.toString());
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
//        listBindDomain.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//        listBindBusiness.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        copyDSLButton.addActionListener(e -> {
            String appDSLJson = buildAppDSLJson();
            setClipboardString(appDSLJson);
        });
    }

    private void onRefresh() {
        updateListData();
    }

    private void onOK() {
        String outPath = textFieldOutputPath.getText();
        if (StringUtils.isBlank(outPath)) {
            Messages.showMessageDialog("输出路径不能为空！", "参数错误", Messages.getWarningIcon());
            return;
        }
        if (!FileUtil.isDirectory(outPath)) {
            Messages.showMessageDialog("输出路径不是一个文件夹", "参数错误", Messages.getWarningIcon());
            return;
        }

        if (applicationDTO == null || applicationVersionDTO == null) {
            Messages.showMessageDialog("请先关联一个平台应用", "系统错误", Messages.getWarningIcon());
            new BindAppVersion(project);
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

        String applicationDSL = buildAppDSLJson();

        int isUpdateCode;
        int projectType;
        int isGenerateProject;

//        int isUpdateCode = JOptionPane.showOptionDialog(this, "选择操作类型", "操作类型",
//                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
//                Messages.getInformationIcon(), new String[]{"同步局部代码", "初始化代码"}, "同步局部代码");
//
//        int projectType = JOptionPane.showOptionDialog(this, "选择项目架构", "项目架构",
//                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
//                Messages.getInformationIcon(), new String[]{"COLA-分层架构", "COLA-单体架构"}, "COLA-分层架构");
//
//        int isGenerateProject = JOptionPane.showOptionDialog(this, "项目框架生成", "项目框架",
//                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
//                Messages.getInformationIcon(), new String[]{"跳过", "生成"}, "跳过");

        // 操作类型
        if (initCodeRadioButton.isSelected()) {
            isUpdateCode = 1;
        } else if (updateCodeRadioButton.isSelected()) {
            isUpdateCode = 0;
        } else {
            isUpdateCode = 0;
        }

        // 项目架构
        if (colaRadioButton.isSelected()) {
            projectType = 0;
        } else if (colaSingleRadioButton.isSelected()) {
            projectType = 1;
        } else {
            projectType = 0;
        }

        // 项目框架生成
        if (isInitProjectStructCheckBox.isSelected()) {
            isGenerateProject = 1;
        } else {
            isGenerateProject = 0;
        }

        // 在用户点击 OK 按钮时保存配置信息
        if (config != null) {
            config.setInitCodeRadioButton(initCodeRadioButton.isSelected());
            config.setUpdateCodeRadioButton(updateCodeRadioButton.isSelected());
            config.setColaRadioButton(colaRadioButton.isSelected());
            config.setColaSingleRadioButton(colaSingleRadioButton.isSelected());
            config.setInitProjectStructCheckBox(isInitProjectStructCheckBox.isSelected());
            // 保存其他的配置信息...
        }


        int isStart = JOptionPane.showOptionDialog(this, "是否开始执行", "执行",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                Messages.getInformationIcon(), new String[]{"确认", "退出"}, "确认");
        if (isStart == 1) {
            return;
        }

        CodeGenerateService codeGenerateService = new CodeGenerateService(applicationDSL);
        TemplateContext templateContext = new TemplateContext(outPath);
        templateContext.setIsGenerateProjectFrame(isGenerateProject == 0);
        templateContext.setProjectTemplateType(projectType == 0 ? ProjectTemplateType.COLA : ProjectTemplateType.COLA_SINGLE);
        templateContext.setOperationTypeEnum(isUpdateCode == 0 ? GenerateOperationTypeEnum.UPDATE_CODE : GenerateOperationTypeEnum.INIT_CODE);

        TemplateVisitor templateVisitor = new TemplateVisitor(templateContext);
        try {
            codeGenerateService.run(templateVisitor);
            Messages.showMessageDialog("代码生成完成！", "执行成功", Messages.getInformationIcon());
        } catch (Exception e) {
            Messages.showMessageDialog(e.getMessage(), "代码生成服务异常", Messages.getErrorIcon());
        } finally {
            dispose();
            VirtualFileManager.getInstance().syncRefresh();
        }
    }

    private String buildAppDSLJson() {
        applicationDTO.setIdentity(textProjectIdentity.getText());
        applicationDTO.setPackageName(textProjectPackage.getText());
        applicationVersionDTO.setCurrentVersion(textProjectVersion.getText());

        AppDSLBuilder appDSLBuilder = new AppDSLBuilder();
        appDSLBuilder.buildAppInfo(applicationDTO);
        appDSLBuilder.buildAppVersionInfo(applicationVersionDTO);
        return buildApplicationDSL(appDSLBuilder.getApplicationJson());
    }

    private String buildApplicationDSL(JSONObject applicationJson) {
        String mail;
        String password;

        LoginAccountInfoSettings instance = LoginAccountInfoSettings.getInstance();
        mail = instance.getAccount();
        password = instance.catchPassword();
        if (StringUtils.isBlank(mail) || StringUtils.isBlank(password)) {
            Messages.showMessageDialog(this, "请先登录", "系统警告", Messages.getWarningIcon());
        }
        new LoginService().login(mail, password);

        List<DomainDesignVersionDTO> domainDesignVersionDTO = listBindDomain.getSelectedValuesList();
        List<BusinessSceneVersionDTO> businessSceneVersionDTOList = listBindBusiness.getSelectedValuesList();

        JSONArray domainDesignArray = JSONUtil.createArray();
        for (DomainDesignVersionDTO domainDesignVersion : domainDesignVersionDTO) {
            JSONObject domainDesignJson = JSONUtil.parseObj(domainDesignVersion.getDomainDesignDsl());
            domainDesignArray.add(domainDesignJson);

            DomainDesignDTO domainDesignDTO = new QueryDomainInfoService().queryByDomainId(domainDesignVersion.getDomainDesignId());
            domainDesignJson.putAll(getDomainDesignInfoMap(domainDesignVersion, domainDesignDTO));
        }

        JSONArray businessSceneArray = JSONUtil.createArray();
        for (BusinessSceneVersionDTO businessSceneVersionDTO : businessSceneVersionDTOList) {
            JSONObject domainDesignJson = JSONUtil.parseObj(businessSceneVersionDTO.getDsl());
            businessSceneArray.add(domainDesignJson);

            BusinessSceneDTO businessSceneDTO = new QueryBusinessInfoService().queryByBusinessId(businessSceneVersionDTO.getBusinessSceneId());
            domainDesignJson.putAll(getBusinessSceneInfoMap(businessSceneVersionDTO, businessSceneDTO));
        }

        applicationJson.remove("businessDomains");
        applicationJson.remove("businessScenarios");
        applicationJson.putOnce("businessDomains", domainDesignArray);
        applicationJson.putOnce("businessScenarios", businessSceneArray);

        return applicationJson.toString();
    }

    private static Map<String, Object> getDomainDesignInfoMap(DomainDesignVersionDTO domainDesignVersionDO, DomainDesignDTO domainDesignDO) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", domainDesignDO.getIdentity());
        map.put("title", domainDesignDO.getName());
        map.put("description", domainDesignDO.getDescription());
        map.put("version", domainDesignVersionDO.getCurrentVersion());
        return map;
    }

    private static Map<String, Object> getBusinessSceneInfoMap(BusinessSceneVersionDTO businessSceneVersionDO, BusinessSceneDTO businessSceneDO) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", businessSceneDO.getIdentity());
        map.put("title", businessSceneDO.getName());
        map.put("description", businessSceneDO.getDescription());
        map.put("version", businessSceneVersionDO.getCurrentVersion());
        return map;
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        SelectElementDialog dialog = new SelectElementDialog(null);
        dialog.setSize(650, 400);
        dialog.setTitle("代码生成器");
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
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
