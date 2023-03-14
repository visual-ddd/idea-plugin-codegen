package com.wk.paas.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 应用版本信息
 *
 * @author zhuxueliang
 */
@Data
public class ApplicationVersionDTO implements Serializable {

    private Long id;

    private Long applicationId;

    private String startVersion;

    private String currentVersion;

    private List<Long> businessSceneVersionIds;

    private List<BusinessSceneVersionDTO> businessSceneVersionDTOList;

    private List<Long> domainDesignVersionIds;

    private List<DomainDesignVersionDTO> domainDesignVersionDTOList;

    private String dsl;

    private String description;

    private Integer state;
}