package com.wk.paas.window.setting;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wk.paas.service.dto.ApplicationDTO;
import com.wk.paas.service.dto.ApplicationVersionDTO;

public class AppDSLBuilder {

    private final JSONObject applicationJson;

    public JSONObject getApplicationJson() {
        return applicationJson;
    }

    public AppDSLBuilder() {
        this.applicationJson = JSONUtil.createObj();
    }

    public void buildAppInfo(ApplicationDTO application) {
        applicationJson.set("name", application.getIdentity());
        applicationJson.set("title", application.getName());
        applicationJson.set("description", application.getDescription());
        applicationJson.set("package", application.getPackageName());
    }

    public void buildAppVersionInfo(ApplicationVersionDTO applicationVersion) {
        applicationJson.set("version", applicationVersion.getCurrentVersion());
    }

}
