package com.wk.paas.service.dto;

import lombok.Data;

/**
 * 组织信息
 *
 * @author zhuxueliang
 */
@Data
public class OrganizationDTO {

    private Long id;

    private Long organizationManagerId;

    private String organizationManagerName;

    private String name;

    private String description;

}