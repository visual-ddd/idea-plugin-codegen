package com.wk.paas.service.dto;

import lombok.Data;

/**
 * DomainDesign信息
 *
 * @author zhuxueliang
 */
@Data
public class DomainDesignDTO {

    private Long id;

    private Long teamId;

    private String name;

    private String identity;

    private String description;

    private DomainDesignVersionDTO domainDesignLatestVersion;
}