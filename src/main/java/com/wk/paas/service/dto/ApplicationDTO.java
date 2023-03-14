package com.wk.paas.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 应用信息
 *
 * @author zhuxueliang
 */
@Data
public class ApplicationDTO implements Serializable {

    private Long id;

    private Long teamId;

    private String name;

    private String identity;

    private String packageName;

    private String description;
}