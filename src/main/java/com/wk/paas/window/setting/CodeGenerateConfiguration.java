package com.wk.paas.window.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.wk.paas.service.dto.ApplicationDTO;
import com.wk.paas.service.dto.ApplicationVersionDTO;
import com.wk.paas.service.dto.BusinessSceneVersionDTO;
import com.wk.paas.service.dto.DomainDesignVersionDTO;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@State(name = "CodeGenerateConfiguration", storages = {@Storage("CodeGenerateConfiguration.xml")})
public class CodeGenerateConfiguration implements PersistentStateComponent<CodeGenerateConfiguration> {

    private boolean initCodeRadioButton;
    private boolean updateCodeRadioButton;
    private boolean colaRadioButton;
    private boolean colaSingleRadioButton;
    private boolean isInitProjectStructCheckBox;

    private ApplicationDTO applicationDTO;
    private ApplicationVersionDTO applicationVersionDTO;
    private List<DomainDesignVersionDTO> domainDesignVersionDTOList;
    private List<BusinessSceneVersionDTO> businessSceneVersionDTOList;

    public static CodeGenerateConfiguration getInstance(Project project){
        return project.getService(CodeGenerateConfiguration.class);
    }

    @Nullable
    @Override
    public CodeGenerateConfiguration getState() {
        return this;
    }

    @Override
    public void loadState(CodeGenerateConfiguration state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
