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
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class GenerateTestCase extends AnAction {

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
                .replace("main", "test")
                .replace(sourceClassName, sourceClassName + "Test");

        File file = new File(testFilePath);
        if (file.exists()) {
            Messages.showMessageDialog(project, "测试类[" + file + "]已存在!", "系统错误", Messages.getErrorIcon());
            return;
        }

        // 如果测试类不存在
        ReadJavaFile.createFolder(testFilePath);

        completions(sourceClassContent, testFilePath);
    }

    public void completions(String sourceContent, String testFilePath) {
        UnintTestEventSourceListener eventSourceListener = new UnintTestEventSourceListener(testFilePath);
        Completion q = Completion.builder()
                .prompt(prompt(sourceContent))
                .stream(true)
                .build();
        ChatGptStreamClient.buildInstance().streamCompletions(q, eventSourceListener);
    }


    public String prompt(String input) {
        return "标题： 使用Mockito，JUnit4 生成单元测试\n" +
                "背景： 为了提高编码质量，减少逻辑编码错误\n" +
                "限制：  \n" +
                " 1、使用java 语言\n" +
                " 2、移除多余的引用import" +
                " 3、覆盖函数内所有代码的逻辑,覆盖函数的所有场景\n" +
                " 4、使用testCase输出\n" +
                " 5、若是在代码中没有出现的属性请不要进行初始化赋值 \n" +
                " 6、只输出java的代码部分\n" +
                " 7、包含package包路径" +
                "\t\n" +
                "输入数据：\n" + input;
    }
}