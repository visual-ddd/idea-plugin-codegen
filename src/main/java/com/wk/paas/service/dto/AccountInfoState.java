package com.wk.paas.service.dto;
 
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
 
@State(name = "account-info",storages = {
        @Storage(value = "accountInfo.xml")
})
public class AccountInfoState implements PersistentStateComponent<AccountInfoState> {
 
    private AccountInfo accountInfo;
 
    public static AccountInfoState getInstance(){
        return ServiceManager.getService(AccountInfoState.class);
    }
 
    @Nullable
    @Override
    public AccountInfoState getState() {
        return this;
    }
 
    @Override
    public void loadState(@NotNull AccountInfoState myState) {
        XmlSerializerUtil.copyBean(myState, this);
    }
 
    public AccountInfo getAccountInfo() {
        if (null == accountInfo) {
            accountInfo = new AccountInfo();
        }
        return accountInfo;
    }
 
    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }
}