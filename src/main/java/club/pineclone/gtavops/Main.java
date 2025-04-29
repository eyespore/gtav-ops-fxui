package club.pineclone.gtavops;

import club.pineclone.gtavops.theme.PinecloneTheme;
import club.pineclone.gtavops.utils.JLibLocator;
import io.vproxy.vfx.theme.Theme;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        JLibLocator.setAsDefaultLocator();
        Theme.setTheme(new PinecloneTheme());
        Application.launch(MainFX.class, args);
    }
}