package com.wk.paas.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.wd.paas.generator.CodeGenerateService;
import com.wd.paas.generator.common.enums.GenerateOperationTypeEnum;
import com.wd.paas.generator.common.enums.ProjectTemplateType;
import com.wd.paas.generator.generate.visitor.velocitytemplate.TemplateContext;
import com.wd.paas.generator.generate.visitor.velocitytemplate.TemplateVisitor;
import com.wk.paas.service.dto.*;
import com.wk.paas.window.BindAppVersion;
import com.wk.paas.window.SelectElementDialog;
import com.wk.paas.window.setting.AppDSLBuilder;
import com.wk.paas.window.setting.BindAppInfoSettings;
import com.wk.paas.window.setting.CodeGenerateConfiguration;
import com.wk.paas.window.setting.LoginAccountInfoSettings;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class GenerateCodeService {

    private final Project project;

    public GenerateCodeService(Project project) {
        this.project = project;
    }

    public void execute() {
        // 获取应用信息
        BindAppInfoSettings projectConfig = BindAppInfoSettings.getInstance(project);
        ApplicationDTO applicationDTO = projectConfig.getApplicationDTO();
        ApplicationVersionDTO applicationVersionDTO = projectConfig.getApplicationVersionDTO();
        if (applicationDTO == null || applicationVersionDTO == null) {
            Messages.showMessageDialog("请先关联一个平台应用", "系统警告", Messages.getWarningIcon());
            new BindAppVersion(project);
            return;
        }

        // 获取生成配置信息
        CodeGenerateConfiguration config = CodeGenerateConfiguration.getInstance(project);
        if (config == null) {
            Messages.showMessageDialog("请先配置生成信息", "系统警告", Messages.getWarningIcon());
            new SelectElementDialog(project);
            return;
        }

        List<DomainDesignVersionDTO> domainSelectedList = config.getDomainSelectedList();
        List<BusinessSceneVersionDTO> sceneSelectedList = config.getSceneSelectedList();
        if (CollectionUtils.isEmpty(domainSelectedList) && CollectionUtils.isEmpty(sceneSelectedList)) {
            Messages.showMessageDialog("至少选择一个生成的模块", "系统警告", Messages.getWarningIcon());
            new SelectElementDialog(project);
            return;
        }

        String outputPathText = config.getOutPath();
        boolean isInitCode = config.isInitCodeRadioButton();
        boolean isColaSingle = config.isColaSingleRadioButton();
        boolean isInitProjectStruct = config.isInitProjectStructCheckBox();

        // 重写应用信息
        applicationDTO.setIdentity(config.getOverrideProjectIdentity());
        applicationDTO.setPackageName(config.getOverrideProjectPackage());
        applicationVersionDTO.setCurrentVersion(config.getOverrideProjectVersion());

        // 操作类型
        GenerateOperationTypeEnum executeType = isInitCode ? GenerateOperationTypeEnum.INIT_CODE : GenerateOperationTypeEnum.UPDATE_CODE;

        // 项目架构
        ProjectTemplateType projectType = isColaSingle ? ProjectTemplateType.COLA_SINGLE : ProjectTemplateType.COLA;

        // 实时解析DSL
        AppDSLBuilder appDSLBuilder = new AppDSLBuilder();
        appDSLBuilder.buildAppInfo(applicationDTO);
        appDSLBuilder.buildAppVersionInfo(applicationVersionDTO);
        String applicationDSL = buildAppDSLJson(appDSLBuilder.getApplicationJson(), config);

        CodeGenerateService codeGenerateService = new CodeGenerateService(applicationDSL);
        TemplateContext templateContext = new TemplateContext(outputPathText);
        templateContext.setIsGenerateProjectFrame(isInitProjectStruct ? Boolean.TRUE : Boolean.FALSE);
        templateContext.setProjectTemplateType(projectType);
        templateContext.setOperationTypeEnum(executeType);

        TemplateVisitor templateVisitor = new TemplateVisitor(templateContext);
        try {
            codeGenerateService.run(templateVisitor);
            Messages.showMessageDialog("代码生成完成！", "执行成功", Messages.getInformationIcon());
        } catch (Exception e) {
            Messages.showMessageDialog(e.getMessage(), "代码生成服务异常", Messages.getErrorIcon());
        } finally {
            VirtualFileManager.getInstance().syncRefresh();
        }
    }

    private String buildAppDSLJson(JSONObject applicationJson, CodeGenerateConfiguration config) {

        LoginAccountInfoSettings instance = LoginAccountInfoSettings.getInstance();
        String mail = instance.getAccount();
        String password = instance.catchPassword();
        if (StringUtils.isBlank(mail) || StringUtils.isBlank(password)) {
            Messages.showMessageDialog(project, "请先登录", "系统警告", Messages.getWarningIcon());
        }
        new LoginService().login(mail, password);

        List<DomainDesignVersionDTO> domainSelectedList = Optional.ofNullable(config.getDomainSelectedList()).orElse(new ArrayList<>());
        List<BusinessSceneVersionDTO> sceneSelectedList = Optional.ofNullable(config.getSceneSelectedList()).orElse(new ArrayList<>());

        // 实时加载DSL
        JSONArray domainDesignArray = JSONUtil.createArray();
        for (DomainDesignVersionDTO domainDesignVersionDTO : domainSelectedList) {
            DomainDesignVersionDTO designVersionDTO = new QueryDomainVersionService().queryById(domainDesignVersionDTO.getId());
            JSONObject domainDesignJson = JSONUtil.parseObj(designVersionDTO.getDomainDesignDsl());
            domainDesignJson.putAll(getDomainDesignInfoMap(domainDesignVersionDTO, domainDesignVersionDTO.getDomainDesignDTO()));
            domainDesignArray.add(domainDesignJson);
        }

        JSONArray businessSceneArray = JSONUtil.createArray();
        for (BusinessSceneVersionDTO businessSceneVersionDTO : sceneSelectedList) {
            BusinessSceneVersionDTO sceneVersionDTO = new QueryBusinessVersionService().queryById(businessSceneVersionDTO.getId());
            JSONObject businessSceneJson = JSONUtil.parseObj(sceneVersionDTO.getDsl());
            businessSceneJson.putAll(getBusinessSceneInfoMap(businessSceneVersionDTO, businessSceneVersionDTO.getBusinessSceneDTO()));
            businessSceneArray.add(businessSceneJson);
        }

        // 更新为用户勾选的模块
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
}
