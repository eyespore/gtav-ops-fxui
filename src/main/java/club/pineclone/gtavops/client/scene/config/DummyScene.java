package club.pineclone.gtavops.client.scene.config;

import club.pineclone.gtavops.client.forked.ForkedThemeLabel;
import club.pineclone.gtavops.client.i18n.ExtendedI18n;
import club.pineclone.gtavops.client.i18n.I18nContext;
import club.pineclone.gtavops.client.scene.SceneTemplate;
import javafx.beans.property.ObjectProperty;

public class DummyScene extends SceneTemplate {
    public DummyScene(I18nContext i18n) {
        super(i18n);
        getContentPane().getChildren().addAll(new ForkedThemeLabel("test"));
    }
}
