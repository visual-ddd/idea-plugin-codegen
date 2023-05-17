package com.wk.paas.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.unfbx.chatgpt.entity.completions.Completion;
import com.wakedata.common.chatgpt.ChatGptStreamClient;
import com.wk.paas.util.ReadJavaFile;
import com.wk.paas.util.UnintTestEventSourceListener;
import com.wk.paas.window.ProgressDialog;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class GenerateTestCase extends AnAction {

    @SneakyThrows
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile sourceFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (sourceFile == null) {
            return;
        }

        // 获取当前项目
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        project.getBaseDir().refresh(false, true);

        String sourceClassName = sourceFile.getNameWithoutExtension();
        String sourceClassPath = sourceFile.getPath();
        String sourceClassContent = ReadJavaFile.readFile(sourceClassPath);

        String testFilePath = sourceClassPath
                .replace("src/main/java", "src/test/java")
                .replace(sourceClassName, sourceClassName + "Test");

        File file = new File(testFilePath);
        if (file.exists()) {
            Messages.showMessageDialog(project, "测试类[" + file + "]已存在!", "系统错误", Messages.getErrorIcon());
            return;
        }

        // 如果测试类不存在
        ReadJavaFile.createFolder(testFilePath);

        ProgressDialog progressDialog = new ProgressDialog(null, "正在生成测试用例...");
        Thread thread = new Thread(() -> {

            UnintTestEventSourceListener eventSourceListener = new UnintTestEventSourceListener(testFilePath, progressDialog);
            Completion q = Completion.builder()
                    .prompt(prompt(sourceClassContent))
                    .temperature(0.5)
                    .stream(true)
                    .build();

            ChatGptStreamClient.buildInstance().streamCompletions(q, eventSourceListener);
        });

        thread.start();
        progressDialog.setVisible(true);
        thread.join(1000);
    }


    public String prompt(String input) {
        return "Title: Generating Unit Tests Using Mockito and JUnit4\n" +
                "Background: To improve coding quality and reduce logical coding errors.\n" +
                "Constraints:\n" +
                "- optimizing imports.\n" +
                "- Cover all logical code and scenarios within functions.\n" +
                "- Cover every line of code within a method.\n" +
                "- Use testCase output.\n" +
                "- Do not initialize or assign values to attributes not in the code.\n" +
                "- Only output Java code.\n" +
                "- Include package path.\n" +
                "Input data:" + input;
    }
}