package com.wk.paas.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.wk.paas.window.ProgressDialog;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 单元测试相关
 *
 * @Author focus
 * @Date 2023/3/30
 */
@Slf4j
public class UnintTestEventSourceListener extends EventSourceListener {

    private final StringBuilder result = new StringBuilder();
    private final String testFilePath;

    private final ProgressDialog progressDialog;

    public UnintTestEventSourceListener(String testFilePath, ProgressDialog progressDialog) {
        this.testFilePath = testFilePath;
        this.progressDialog = progressDialog;
    }

    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
        log.info("OpenAI建立sse连接...");
    }

    @SneakyThrows
    @Override
    public void onEvent(@NotNull EventSource eventSource, String id, String type, String data) {
        if (data.equals("[DONE]")) {
            String content = result.toString();
            log.info(content);
            log.info("OpenAI返回数据结束了");

            int beginIndex = content.indexOf("package");
            String classContent = beginIndex > -1 ? content.substring(beginIndex) : content;
            ReadJavaFile.writeFile(classContent, testFilePath);

            progressDialog.setValue(100);
            progressDialog.dispose();

            // 刷新
            //在UI线程中显示进度条
            ApplicationManager.getApplication().invokeAndWait(() -> {
                VirtualFileManager.getInstance().syncRefresh();
                Messages.showMessageDialog("成功生成测试用例！", "执行成功", Messages.getInformationIcon());
            });

            return;
        }

        JSONObject json = JSONUtil.parseObj(data);
        json.getJSONArray("choices").forEach(e -> result.append(JSONUtil.parseObj(e).get("text")));
    }

    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        log.info("OpenAI关闭sse连接...");
    }

    @SneakyThrows
    @Override
    public void onFailure(@NotNull EventSource eventSource, Throwable t, Response response) {
        if (Objects.isNull(response)) {
            log.error("OpenAI  sse连接异常:{}", t);

            ApplicationManager.getApplication().invokeAndWait(() -> Messages.showMessageDialog("服务连接超时，请稍后重试！", "执行失败", Messages.getErrorIcon()));

            eventSource.cancel();
            return;
        }

        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", body.string(), t);
        } else {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", response, t);
        }
        eventSource.cancel();

        ApplicationManager.getApplication().invokeAndWait(() -> Messages.showMessageDialog("解析错误警告，请联系管理员！", "执行失败", Messages.getWarningIcon()));

    }
}
