package com.wk.paas.service.dto;

import lombok.Data;

/**
 * 业务域版本信息
 *
 * @author zhuxueliang
 */
@Data
public class DomainDesignVersionDTO {

    private Long id;

    private Long domainDesignId;

    private DomainDesignDTO domainDesignDTO;

    private String startVersion;

    private String currentVersion;

    private String domainDesignDsl;

    private String graphDsl;

    private String description;

    private Integer state;
}