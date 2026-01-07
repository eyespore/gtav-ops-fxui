package club.pineclone.gtavops.client.forked;

import io.vproxy.vfx.manager.font.FontManager;
import io.vproxy.vfx.theme.Theme;
import javafx.scene.control.Label;

public class ForkedThemeLabel extends Label {

    public ForkedThemeLabel() {
        setTextFill(Theme.current().normalTextColor());
        FontManager.get().setFont(this);
    }

    public ForkedThemeLabel(String text) {
        super(text);
        setTextFill(Theme.current().normalTextColor());
        FontManager.get().setFont(this);
    }

}
