package com.wk.paas.service;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.ui.Messages;
import com.wk.paas.service.dto.ApplicationDTO;
import com.wk.paas.service.dto.DomainDesignDTO;
import com.wk.paas.service.dto.ResultDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryDomainInfoService {

    public static final String API_LOGIN = "https://ddd.wakedt.cn/wd/visual/web/domain-design/domain-design-detail-query?";

    public DomainDesignDTO queryByDomainId(Long id) {
        HttpRequest httpRequest = HttpRequest.get(API_LOGIN.concat("id=").concat(String.valueOf(id)));

        HttpResponse response;
        try {
            response = httpRequest.execute();
        } catch (Exception exception) {
            throw new IllegalStateException(exception.getMessage());
        }
        String result = response.body();
        System.out.println(result);

        ResultDTO<DomainDesignDTO> resultDTO = JSONUtil.toBean(result, new TypeReference<ResultDTO<DomainDesignDTO>>() {}.getType(), true);
        if (!resultDTO.isSuccess()) {
            throw new IllegalStateException(resultDTO.getMsg());
        }
        return resultDTO.getData();
    }
}
