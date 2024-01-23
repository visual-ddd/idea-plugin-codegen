package com.wk.paas.service;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.wk.paas.config.PlatformServiceConfig;
import com.wk.paas.service.dto.DomainDesignVersionDTO;
import com.wk.paas.service.dto.ResultDTO;

/**
 * 查询领域模型版本
 *
 * @author ZhuXueliang
 */
public class QueryDomainVersionService {

    public DomainDesignVersionDTO queryById(Long id) {
        HttpRequest httpRequest = HttpRequest.get((PlatformServiceConfig.getUrlPrefix() + "/web/domain-design-version/domain-design-version-detail-query?").concat("id=").concat(String.valueOf(id)));

        HttpResponse response;
        try {
            response = httpRequest.execute();
        } catch (Exception exception) {
            throw new IllegalStateException(exception.getMessage());
        }
        String result = response.body();

        ResultDTO<DomainDesignVersionDTO> resultDTO = JSONUtil.toBean(result,
                new TypeReference<ResultDTO<DomainDesignVersionDTO>>() {
                }.getType(), true);
        if (!resultDTO.isSuccess()) {
            throw new IllegalStateException(resultDTO.getMsg());
        }
        return resultDTO.getData();
    }
}
