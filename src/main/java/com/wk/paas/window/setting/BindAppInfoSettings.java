package com.wk.paas.window.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.wk.paas.service.dto.ApplicationDTO;
import com.wk.paas.service.dto.ApplicationVersionDTO;
import com.wk.paas.service.dto.TeamDTO;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

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

    @Nullable
    @Override
    public BindAppInfoSettings getState() {
        return this;
    }
 
    @Override
    public void loadState(@NotNull BindAppInfoSettings optionalSettings) {
        XmlSerializerUtil.copyBean(optionalSettings, this);
    }
 
}