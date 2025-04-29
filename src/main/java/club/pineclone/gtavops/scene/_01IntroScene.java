package club.pineclone.gtavops.scene;

import club.pineclone.gtavops.config.AppConfig;
import io.vproxy.vfx.manager.font.FontManager;
import io.vproxy.vfx.ui.layout.VPadding;
import io.vproxy.vfx.ui.scene.VSceneRole;
import io.vproxy.vfx.ui.wrapper.ThemeLabel;
import io.vproxy.vfx.util.FXUtils;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;

public class _01IntroScene extends SceneTemplate {

    public _01IntroScene() {
        super(VSceneRole.MAIN);
        enableAutoContentWidthHeight();
        var pane = new VBox(
                new ThemeLabel("Coded By Pineclone") {{
                    FontManager.get().setFont(this, settings -> settings.setSize(40));
                }},
                new VPadding(10),
                new ThemeLabel("version: " + AppConfig.APPLICATION_VERSION) {{
                    FontManager.get().setFont(this, settings -> settings.setSize(28));
                }},
                new VPadding(30),
                new ThemeLabel("Focus on supplying simple marco game enhanced in Grand Thief Auto V!") {{
                    FontManager.get().setFont(this, settings -> settings.setSize(30));
                }},
                new VPadding(10),
                new ThemeLabel("UI design powered by wkgcass — thanks a lot to the original author :D.") {{
                    FontManager.get().setFont(this, settings -> settings.setSize(30).setWeight(FontWeight.BOLD));
                }}
        ) {{
            setAlignment(Pos.CENTER);
        }};
        getContentPane().getChildren().add(pane);
        FXUtils.observeWidthHeightCenter(getContentPane(), pane);
    }

    @Override
    public String getTitle() {
        return "Intro";
    }
}
