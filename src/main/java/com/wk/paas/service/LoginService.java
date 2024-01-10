package com.wk.paas.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wk.paas.config.PlatformServiceConfig;
import com.wk.paas.service.dto.ResultDTO;

import java.util.HashMap;
import java.util.Map;

public class LoginService {

    public static final String API_LOGIN = PlatformServiceConfig.URL_PREFIX + "/web/account/login/login";

    public Boolean login(String mailText, String passwordText) {

        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.set("accountNo", mailText);
        jsonObject.set("password", passwordText);

        Map<String, String> heads = new HashMap<>();
        heads.put("Content-Type", "application/json;charset=UTF-8");
        HttpRequest httpRequest = HttpRequest.post(API_LOGIN)
                .headerMap(heads, false)
                .body(String.valueOf(jsonObject))
                .timeout(3 * 1000);
        HttpResponse response;
        try {
            response = httpRequest.execute();
        } catch (Exception exception) {
            throw new IllegalStateException(exception.getMessage());
        }

        String result = response.body();
        System.out.println(result);

        ResultDTO resultDTO = JSONUtil.toBean(result, ResultDTO.class, true);
        if (!resultDTO.isSuccess()) {
            throw new IllegalArgumentException("账号或密码错误！");
        }
        return Boolean.TRUE;
    }
}
