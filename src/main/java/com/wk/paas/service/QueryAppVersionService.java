package com.wk.paas.service;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.ui.Messages;
import com.wk.paas.config.PlatformServiceConfig;
import com.wk.paas.service.dto.ApplicationVersionDTO;
import com.wk.paas.service.dto.ResultDTO;

import java.util.ArrayList;
import java.util.List;

public class QueryAppVersionService {

    public static final String API_APPLICATION_VERSION_PAGE_QUERY = "/web/application-version/application-version-page-query?";
    public static final String API_APPLICATION_VERSION_DETAIL_QUERY = "/web/application-version/application-version-detail-query-exclude-dsl?";

    public List<ApplicationVersionDTO> queryByAppId(String appId) {
        HttpRequest httpRequest = HttpRequest.get(PlatformServiceConfig.getUrlPrefix() + API_APPLICATION_VERSION_PAGE_QUERY.concat("applicationId=").concat(appId));

        HttpResponse response;
        try {
            response = httpRequest.execute();
        } catch (Exception exception) {
            Messages.showMessageDialog(exception.getMessage(), "系统错误", Messages.getErrorIcon());
            return new ArrayList<>();
        }
        String result = response.body();

        ResultDTO<List<ApplicationVersionDTO>> resultDTO = JSONUtil.toBean(result, new TypeReference<ResultDTO<List<ApplicationVersionDTO>>>() {}.getType(), true);
        if (!resultDTO.isSuccess()) {
            throw new IllegalStateException(resultDTO.getMsg());
        }
        return resultDTO.getData();
    }

    public ApplicationVersionDTO detailQuery(String appVersionId) {
        HttpRequest httpRequest = HttpRequest.get(PlatformServiceConfig.getUrlPrefix() + API_APPLICATION_VERSION_DETAIL_QUERY.concat("id=").concat(appVersionId));

        HttpResponse response;
        try {
            response = httpRequest.execute();
        } catch (Exception exception) {
            throw new IllegalStateException(exception.getMessage());
        }
        String result = response.body();

        ResultDTO<ApplicationVersionDTO> resultDTO = JSONUtil.toBean(result, new TypeReference<ResultDTO<ApplicationVersionDTO>>() {}.getType(), true);
        if (!resultDTO.isSuccess()) {
            throw new IllegalStateException("拉取应用版本信息失败！");
        }
        return resultDTO.getData();
    }
}
