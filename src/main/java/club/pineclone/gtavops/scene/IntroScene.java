package club.pineclone.gtavops.scene;

import club.pineclone.gtavops.forked.ForkedThemeLabel;
import club.pineclone.gtavops.i18n.I18nContext;
import club.pineclone.gtavops.i18n.I18nText;
import club.pineclone.gtavops.utils.ImageUtils;
import io.vproxy.vfx.manager.font.FontManager;
import io.vproxy.vfx.ui.layout.VPadding;
import io.vproxy.vfx.ui.wrapper.ThemeLabel;
import io.vproxy.vfx.util.FXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class IntroScene extends SceneTemplate {

    public IntroScene(I18nContext i18nContext) {
        super(i18nContext);
        enableAutoContentWidthHeight();
        setBackgroundImage(ImageUtils.loadImage("/img/bg1.png"));

        // TODO: 通过向后端请求获取version而不是读取配置文件
        String version = "unknown";
        String versionFile = "/version.txt";
        try (InputStream in = getClass().getResourceAsStream(versionFile)) {
            if (in != null) {
                version = new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final String finalVersion = version;
        var vBox = new VBox(
                // TODO: 优化为I18nText

                new ForkedThemeLabel() {{
                    FontManager.get().setFont(this, settings -> settings.setSize(30));
                    textProperty().bind(I18nText.of(i -> i.introScene.header).binding(i18nContext));
                }},
                new VPadding(10),
                new ThemeLabel() {{
                    FontManager.get().setFont(this, settings -> settings.setSize(23));
                    textProperty().bind(I18nText.of(i -> i.introScene.coreVersionLabel + ": " + finalVersion).binding(i18nContext));
                }},
                new VPadding(10),
                new ThemeLabel() {{
                    FontManager.get().setFont(this, settings -> settings.setSize(23).setWeight(FontWeight.BOLD));
                    textProperty().bind(I18nText.of(i -> i.introScene.acknowledgement).binding(i18nContext));
                }}
        ) {{
            setAlignment(Pos.CENTER);
        }};

        vBox.setPadding(new Insets(0, 470, 0, 0));
        getContentPane().getChildren().add(vBox);
        FXUtils.observeWidthHeightCenter(getContentPane(), vBox);
    }
}
