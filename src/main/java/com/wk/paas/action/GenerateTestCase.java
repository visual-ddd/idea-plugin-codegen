//package com.wk.paas.action;
//
//import com.intellij.openapi.actionSystem.AnAction;
//import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.actionSystem.CommonDataKeys;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.ui.Messages;
//import com.intellij.openapi.vfs.VirtualFile;
//import com.unfbx.chatgpt.entity.chat.ChatCompletion;
//import com.unfbx.chatgpt.entity.chat.Message;
//import com.wakedata.common.chatgpt.ChatGptStreamClient;
//import com.wakedata.common.chatgpt.config.ChatGptProperties;
//import com.wk.paas.util.ConsoleEventSourceListenerV2;
//import com.wk.paas.util.ReadJavaFile;
//import com.wk.paas.window.ProgressDialog;
//import lombok.SneakyThrows;
//import org.jetbrains.annotations.NotNull;
//
//import java.io.File;
//import java.util.List;
//
//public class GenerateTestCase extends AnAction {
//
//    @SneakyThrows
//    @Override
//    public void actionPerformed(@NotNull AnActionEvent e) {
//        VirtualFile sourceFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
//        if (sourceFile == null) {
//            return;
//        }
//
//        // 获取当前项目
//        Project project = e.getProject();
//        if (project == null) {
//            return;
//        }
//        project.getBaseDir().refresh(false, true);
//
//        String sourceClassName = sourceFile.getNameWithoutExtension();
//        String sourceClassPath = sourceFile.getPath();
//        String sourceClassContent = ReadJavaFile.readFile(sourceClassPath);
//
//        String testFilePath = sourceClassPath
//                .replace("src/main/java", "src/test/java")
//                .replace(sourceClassName, sourceClassName + "Test");
//
//        File file = new File(testFilePath);
//        if (file.exists()) {
//            Messages.showMessageDialog(project, "测试类[" + file + "]已存在!", "系统错误", Messages.getErrorIcon());
//            return;
//        }
//
//        // 如果测试类不存在
//        ReadJavaFile.createFolder(testFilePath);
//
//        ProgressDialog progressDialog = new ProgressDialog(null, "正在生成测试用例...");
//        Thread thread = new Thread(() -> {
//            ConsoleEventSourceListenerV2 eventSourceListener = new ConsoleEventSourceListenerV2(testFilePath, progressDialog);
//
//            Message message = Message.builder().role(Message.Role.USER).content(prompt(sourceClassContent)).build();
//
//            ChatCompletion q = ChatCompletion
//                    .builder()
//                    .messages(List.of(message))
//                    .model(ChatCompletion.Model.GPT_4.getName())
//                    .temperature(0)
//                    .build();
//
//            ChatGptProperties properties = new ChatGptProperties();
//            properties.setApiKey("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");
//            properties.setProxyApiHost("https://ai-proxy.wakedt.cn/");
//            ChatGptStreamClient chatGptStreamClient = new ChatGptStreamClient(properties);
//            chatGptStreamClient.buildInstance().streamChatCompletion(q, eventSourceListener);
//        });
//
//        thread.start();
//        progressDialog.setVisible(true);
//        thread.join(1000);
//    }
//
//
//    public String prompt(String input) {
//        return "Your task is to Generating Unit Tests Using Mockito and JUnit4;\n" +
//                "Generating Unit Tests Using Mockito and JUnit4;\n" +
//                "optimizing imports;\n" +
//                "Cover all logical code and scenarios within functions;\n" +
//                "Cover every line of code within a method;\n" +
//                "Use testCase output;\n" +
//                "Do not initialize or assign values to attributes not in the code;\n" +
//                "Only output Java code;\n" +
//                "Include package path;\n" +
//                "Give the code in the following format:\n" +
//                "Code: <Generate Code enclosed in three backticks ```>" +
//                "```java\n" + input + "```";
////        return "Title: Generating Unit Tests Using Mockito and JUnit4\n" +
////                "Background: To improve coding quality and reduce logical coding errors.\n" +
////                "Constraints:\n" +
////                "- optimizing imports\n" +
////                "- Cover all logical code and scenarios within functions\n" +
////                "- Cover every line of code within a method\n" +
////                "- Use testCase output\n" +
////                "- Do not initialize or assign values to attributes not in the code\n" +
////                "- Only output Java code\n" +
////                "- Include package path\n" +
////                "Input data:" + input;
//    }
//}