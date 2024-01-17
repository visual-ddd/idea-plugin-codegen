package com.wk.paas.service.dto;

import lombok.Data;

import java.util.List;

/**
 * @author WangChenSheng
 * date 2022/12/30 11:11
 */
@Data
public class AccountRoleDTO {

    private Long id;

    private Boolean isSysAdmin;

    private List<AccountTeamInfo> accountTeamInfoList;

    private List<AccountOrganizationInfo> accountOrganizationInfoList;

}
