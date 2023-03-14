package com.wk.paas.menu;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.xmlb.annotations.Attribute;
import com.wk.paas.window.CodeGenerateTool;
import com.wk.paas.window.LocalModel;
import com.wk.paas.window.RemoteModel;
import org.jetbrains.annotations.NotNull;

/**
 * @author shimmer
 */
@Attribute
public class OpenToolWindowMainMenu implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        LocalModel localModel = new LocalModel(project);
        RemoteModel remoteModel = new RemoteModel(project);

        Content localContent = ContentFactory.SERVICE.getInstance()
                .createContent(localModel, "本地模型", false);
        Content remoteContent = ContentFactory.SERVICE.getInstance()
                .createContent(remoteModel, "远程模型", false);

        toolWindow.getContentManager().addContent(remoteContent);
        toolWindow.getContentManager().addContent(localContent);
        toolWindow.setType(ToolWindowType.DOCKED, null);
    }
}
