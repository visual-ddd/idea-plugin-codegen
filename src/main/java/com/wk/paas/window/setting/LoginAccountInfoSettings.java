package com.wk.paas.window.setting;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 存储账号的信息
 */
@Data
@State(name = "LoginAccountInfoSettings", storages = {@Storage("accountInfo.xml")})
public class LoginAccountInfoSettings implements PersistentStateComponent<LoginAccountInfoSettings> {

    private String account;

    public static LoginAccountInfoSettings getInstance(){
        return ServiceManager.getService(LoginAccountInfoSettings.class);
    }

    public String catchPassword() {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        CredentialAttributes credentialAttr = getCredentialAttr(this.account);
        return PasswordSafe.getInstance().getPassword(credentialAttr);
    }

    public void storePassword(String password) {
        if (StringUtils.isBlank(account)) {
            return;
        }
        CredentialAttributes credentialAttr = getCredentialAttr(this.account);
        PasswordSafe.getInstance().setPassword(credentialAttr, password);
    }

    public CredentialAttributes getCredentialAttr(@NotNull String account) {
        return new CredentialAttributes(CredentialAttributesKt.generateServiceName("Login", account));
    }

    @Override
    public LoginAccountInfoSettings getState() {
        return this;
    }
 
    @Override
    public void loadState(@NotNull LoginAccountInfoSettings optionalSettings) {
        XmlSerializerUtil.copyBean(optionalSettings, this);
    }
}