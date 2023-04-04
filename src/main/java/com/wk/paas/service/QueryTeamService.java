package com.wk.paas.service;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.ui.Messages;
import com.wk.paas.config.PlatformServiceConfig;
import com.wk.paas.service.dto.ResultDTO;
import com.wk.paas.service.dto.TeamDTO;

import java.util.ArrayList;
import java.util.List;

public class QueryTeamService {

    public static final String API_TEAM_QUERY = "https://" + PlatformServiceConfig.HOST + "/wd/visual/web/team/team-page-query";

    public List<TeamDTO> query() {
        HttpRequest httpRequest = HttpRequest.get(API_TEAM_QUERY);
        HttpResponse response;
        try {
            response = httpRequest.execute();
        } catch (Exception exception) {
            Messages.showMessageDialog(exception.getMessage(), "系统错误", Messages.getErrorIcon());
            return new ArrayList<>();
        }
        String result = response.body();
        ResultDTO<List<TeamDTO>> resultDTO = JSONUtil.toBean(result, new TypeReference<ResultDTO<List<TeamDTO>>>() {}.getType(), true);
        if (!resultDTO.isSuccess()) {
            throw new IllegalStateException(resultDTO.getMsg());
        }
        return resultDTO.getData();
    }
}
