package icons;

import com.intellij.openapi.util.IconLoader;
import javax.swing.Icon;

public final class GlobalIcons {

    private GlobalIcons() {
        throw new AssertionError("GlobalIcons instances for you!");
    }

    public static final Icon LOGO_ICON = IconLoader.getIcon("icons/logo.svg", GlobalIcons.class);

}
