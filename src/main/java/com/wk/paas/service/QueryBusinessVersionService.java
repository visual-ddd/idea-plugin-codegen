package com.wk.paas.service;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.wk.paas.config.PlatformServiceConfig;
import com.wk.paas.service.dto.BusinessSceneVersionDTO;
import com.wk.paas.service.dto.ResultDTO;

/**
 * 查询业务场景版本
 *
 * @author shimmer
 */
public class QueryBusinessVersionService {

    public static final String API_LOGIN = PlatformServiceConfig.URL_PREFIX + "/web/business-scene-version/business-scene-version-detail-query?";

    public BusinessSceneVersionDTO queryById(Long id) {
        HttpRequest httpRequest = HttpRequest.get(API_LOGIN.concat("id=").concat(String.valueOf(id)));

        HttpResponse response;
        try {
            response = httpRequest.execute();
        } catch (Exception exception) {
            throw new IllegalStateException(exception.getMessage());
        }
        String result = response.body();

        ResultDTO<BusinessSceneVersionDTO> resultDTO = JSONUtil.toBean(result, new TypeReference<ResultDTO<BusinessSceneVersionDTO>>() {
        }.getType(), true);
        if (!resultDTO.isSuccess()) {
            throw new IllegalStateException(resultDTO.getMsg());
        }
        return resultDTO.getData();
    }
}
