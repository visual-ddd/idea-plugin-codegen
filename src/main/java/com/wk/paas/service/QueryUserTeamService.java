package com.wk.paas.service;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.ui.Messages;
import com.wakedt.visual.client.organization.dto.TeamDTO;
import com.wakedt.visual.client.user.dto.AccountRoleDTO;
import com.wakedt.visual.client.user.dto.AccountTeamInfo;
import com.wk.paas.config.PlatformServiceConfig;
import com.wk.paas.service.dto.ResultDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 查询用户所在团队
 */
public class QueryUserTeamService {

    public List<TeamDTO> query() {
        HttpRequest httpRequest = HttpRequest.post(PlatformServiceConfig.getUrlPrefix() + "/web/account/login/get-account-role");
        HttpResponse response;
        try {
            response = httpRequest.execute();
        } catch (Exception exception) {
            Messages.showMessageDialog(exception.getMessage(), "系统错误", Messages.getErrorIcon());
            return new ArrayList<>();
        }
        String result = response.body();
        ResultDTO<AccountRoleDTO> resultDTO = JSONUtil.toBean(result, new TypeReference<ResultDTO<AccountRoleDTO>>() {}.getType(), true);
        if (resultDTO == null || !resultDTO.isSuccess()) {
            throw new IllegalStateException(Optional.ofNullable(resultDTO).map(ResultDTO::getMsg).orElse("系统错误"));
        }
        AccountRoleDTO accountRoleDTO = resultDTO.getData();
        if (accountRoleDTO == null) {
            return new ArrayList<>();
        }

        List<AccountTeamInfo> accountTeamInfoList = Optional.ofNullable(accountRoleDTO.getAccountTeamInfoList()).orElse(new ArrayList<>());
        return accountTeamInfoList.stream()
                .map(AccountTeamInfo::getTeamDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
