//package com.wk.paas.util;
//
//import cn.hutool.json.JSONUtil;
//import com.intellij.openapi.application.ApplicationManager;
//import com.intellij.openapi.ui.Messages;
//import com.intellij.openapi.vfs.VirtualFileManager;
//import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
//import com.wk.paas.window.ProgressDialog;
//import lombok.Getter;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//import okhttp3.sse.EventSource;
//import okhttp3.sse.EventSourceListener;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Objects;
//import java.util.OptionalInt;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * 描述： sse
// *
// * @author https:www.unfbx.com
// * 2023-06-15
// */
//@Slf4j
//public class ConsoleEventSourceListenerV2 extends EventSourceListener {
//    @Getter
//    StringBuilder result = new StringBuilder();
//
//    private final String testFilePath;
//
//    private final ProgressDialog progressDialog;
//
//    public ConsoleEventSourceListenerV2(String testFilePath, ProgressDialog progressDialog) {
//        this.testFilePath = testFilePath;
//        this.progressDialog = progressDialog;
//    }
//
//    @Override
//    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
//        log.info("OpenAI建立sse连接...");
//    }
//
//    @Override
//    public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String data) {
//        log.info("OpenAI返回数据：{}", data);
//        if (data.equals("[DONE]")) {
//
//            String classContent = result.toString();
//            String pattern = "```java([\\s\\S]*?)```";
//
//            Pattern r = Pattern.compile(pattern);
//            Matcher m = r.matcher(classContent);
//            if (m.find( )) {
//                classContent = m.group(1);
//            }
//
//            ReadJavaFile.writeFile(classContent, testFilePath);
//
//            progressDialog.setValue(100);
//            progressDialog.dispose();
//
//            // 刷新
//            //在UI线程中显示进度条
//            ApplicationManager.getApplication().invokeAndWait(() -> {
//                VirtualFileManager.getInstance().syncRefresh();
//                Messages.showMessageDialog("成功生成测试用例！", "执行成功", Messages.getInformationIcon());
//            });
//
//            log.info("OpenAI返回数据结束了");
//            return;
//        }
//        ChatCompletionResponse chatCompletionResponse = JSONUtil.toBean(data, ChatCompletionResponse.class);
//        String content = chatCompletionResponse.getChoices().get(0).getDelta().getContent();
//        if (content != null) {
//            result.append(content);
//        }
//    }
//
//    @Override
//    public void onClosed(EventSource eventSource) {
//        log.info("OpenAI关闭sse连接...");
//    }
//
//    @SneakyThrows
//    @Override
//    public void onFailure(EventSource eventSource, Throwable t, Response response) {
//        if (Objects.isNull(response)) {
//            log.error("OpenAI  sse连接异常:{}", t);
//
//            ApplicationManager.getApplication().invokeAndWait(() -> Messages.showMessageDialog("服务连接超时，请稍后重试！", "执行失败", Messages.getErrorIcon()));
//            eventSource.cancel();
//            return;
//        }
//
//        ResponseBody body = response.body();
//        if (Objects.nonNull(body)) {
//            log.error("OpenAI  sse连接异常data：{}，异常：{}", body.string(), t);
//        } else {
//            log.error("OpenAI  sse连接异常data：{}，异常：{}", response, t);
//        }
//        eventSource.cancel();
//        ApplicationManager.getApplication().invokeAndWait(() -> Messages.showMessageDialog("解析错误警告，请联系管理员！", "执行失败", Messages.getWarningIcon()));
//    }
//}