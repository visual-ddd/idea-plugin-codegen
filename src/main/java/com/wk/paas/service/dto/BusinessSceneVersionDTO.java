package com.wk.paas.service.dto;

import lombok.Data;

/**
 * 业务场景版本信息
 *
 * @author zhuxueliang
 */
@Data
public class BusinessSceneVersionDTO {

    private Long id;

    private Long businessSceneId;

    private BusinessSceneDTO businessSceneDTO;

    private String startVersion;

    private String currentVersion;

    private String dsl;

    private String graphDsl;

    private String description;

    private Integer state;
}