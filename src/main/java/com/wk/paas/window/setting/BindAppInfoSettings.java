package com.wk.paas.window.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.wakedt.visual.client.organization.dto.TeamDTO;
import com.wk.paas.service.dto.ApplicationDTO;
import com.wk.paas.service.dto.ApplicationVersionDTO;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 存储绑定应用的信息
 */
@Data
@State(name = "BindAppInfoSettings", storages = {@Storage("bindAppInfo.xml")})
public class BindAppInfoSettings implements PersistentStateComponent<BindAppInfoSettings> {

    private TeamDTO teamDTO;

    private ApplicationDTO applicationDTO;

    private ApplicationVersionDTO applicationVersionDTO;

    private Long bindAppVersionId;


    public static BindAppInfoSettings getInstance(Project project){
        return project.getService(BindAppInfoSettings.class);
    }

    @Override
    public BindAppInfoSettings getState() {
        return this;
    }
 
    @Override
    public void loadState(@NotNull BindAppInfoSettings optionalSettings) {
        XmlSerializerUtil.copyBean(optionalSettings, this);
    }
 
}