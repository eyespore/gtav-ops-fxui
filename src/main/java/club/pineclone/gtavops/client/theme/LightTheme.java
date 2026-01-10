package club.pineclone.gtavops.client.theme;

import club.pineclone.gtavops.client.utils.ColorUtils;
import io.vproxy.vfx.theme.impl.DarkTheme;
import javafx.scene.paint.Color;

public class LightTheme extends DarkTheme {

    @Override
    public Color sceneBackgroundColor() {
        return ColorUtils.hexToNormalizedColor("#ffffff");
    }
}
