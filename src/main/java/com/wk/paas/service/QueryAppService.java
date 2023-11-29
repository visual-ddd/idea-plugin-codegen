package com.wk.paas.service;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.ui.Messages;
import com.wk.paas.config.PlatformServiceConfig;
import com.wk.paas.service.dto.ApplicationDTO;
import com.wk.paas.service.dto.ResultDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryAppService {

    public static final String API_LOGIN = PlatformServiceConfig.HOST + "/wd/visual/web/application/application-page-query?";

    public List<ApplicationDTO> queryByTeamId(String teamId) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Content-Type", "application/json;charset=UTF-8");
        HttpRequest httpRequest = HttpRequest.get(API_LOGIN.concat("teamId=").concat(teamId))
                .headerMap(heads, false)
                .timeout(3 * 1000);
        HttpResponse response;
        try {
            response = httpRequest.execute();
        } catch (Exception exception) {
            Messages.showMessageDialog(exception.getMessage(), "系统错误", Messages.getErrorIcon());
            return new ArrayList<>();
        }
        String result = response.body();
        System.out.println(result);

        ResultDTO<List<ApplicationDTO>> resultDTO = JSONUtil.toBean(result, new TypeReference<ResultDTO<List<ApplicationDTO>>>() {}.getType(), true);
        if (!resultDTO.isSuccess()) {
            throw new IllegalStateException(resultDTO.getMsg());
        }
        return resultDTO.getData();
    }

    public static void main(String[] args) {
        new LoginService().login("1", "1");
        List<ApplicationDTO> applicationDTOS = new QueryAppService().queryByTeamId("1");
        System.out.println(applicationDTOS);
    }
}
