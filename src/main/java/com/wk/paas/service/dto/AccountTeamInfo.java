package com.wk.paas.service.dto;

import lombok.Data;

/**
 * @author WangChenSheng
 * date 2022/12/30 11:11
 */
@Data
public class AccountTeamInfo {

    private TeamDTO teamDTO;

    private Boolean isTeamAdmin;

}