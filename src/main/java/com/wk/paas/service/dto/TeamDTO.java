package com.wk.paas.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 团队信息
 *
 * @author zhuxueliang
 */
@Data
public class TeamDTO implements Serializable {

    private Long id;

    private Long organizationId;

    private String name;

    private String description;

    private Long teamManagerId;

    private String teamManagerName;

}