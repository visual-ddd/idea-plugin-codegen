package com.wk.paas.config;

import com.wk.paas.window.setting.LoginAccountInfoSettings;
import org.apache.commons.lang3.StringUtils;

public class PlatformServiceConfig {

    public static final String DefaultHost = "https://ddd.wakedata.com";

    public static final String SPRING_CONTEXT = "/wd/visual";


    public static String getUrlPrefix() {
        LoginAccountInfoSettings instance = LoginAccountInfoSettings.getInstance();
        String userHost = instance.getHost();
        return (StringUtils.isBlank(userHost) ? DefaultHost : userHost) + SPRING_CONTEXT;
    }

}