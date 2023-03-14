package com.wk.paas.service.dto;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import lombok.Data;

@Data
public class AccountInfo {

    private String accountNo;

    private String password;
}
