package com.wk.paas.service.dto;

import lombok.Data;

/**
 * @author WangChenSheng
 * date 2022/12/30 11:11
 */
@Data
public class AccountOrganizationInfo {

    private OrganizationDTO organizationDTO;

    private Boolean isOrganizationAdmin;

}
