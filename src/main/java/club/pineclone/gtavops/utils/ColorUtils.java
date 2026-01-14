package club.pineclone.gtavops.utils;

import javafx.scene.paint.Color;

public class ColorUtils {

    private ColorUtils() {}

    public static String formatAsHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255)
        );
    }

    public static Color hexToNormalizedColor(String hex) {
        if (hex.startsWith("#")) hex = hex.substring(1);
        if (hex.length() != 6) {
            throw new IllegalArgumentException("Invalid HEX color: " + hex);
        }

        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return Color.color(r / 255.0, g / 255.0, b / 255.0, 1);  // 归一化
    }
}
