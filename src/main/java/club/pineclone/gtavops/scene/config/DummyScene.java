package club.pineclone.gtavops.scene.config;

import club.pineclone.gtavops.forked.ForkedThemeLabel;
import club.pineclone.gtavops.i18n.I18nContext;
import club.pineclone.gtavops.scene.SceneTemplate;

public class DummyScene extends SceneTemplate {
    public DummyScene(I18nContext i18n) {
        super(i18n);
        getContentPane().getChildren().addAll(new ForkedThemeLabel("test"));
    }
}
