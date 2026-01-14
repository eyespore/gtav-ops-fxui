package club.pineclone.gtavops.theme;

import club.pineclone.gtavops.utils.ColorUtils;
import io.vproxy.vfx.theme.impl.DarkTheme;
import javafx.scene.paint.Color;

public class LightTheme extends DarkTheme {

    @Override
    public Color sceneBackgroundColor() {
        return ColorUtils.hexToNormalizedColor("#ffffff");
    }
}
