package com.wk.paas.service.dto;

import lombok.Data;

/**
 * 业务场景信息
 *
 * @author zhuxueliang
 */
@Data
public class BusinessSceneDTO {

    private Long id;

    private Long teamId;

    private String name;

    private String identity;

    private String description;

    private BusinessSceneVersionDTO businessSceneLatestVersion;
}