package club.pineclone.gtavops.client.scene;

import club.pineclone.gtavops.client.component.VOptionButton;
import club.pineclone.gtavops.client.config.ClientConfig;
import club.pineclone.gtavops.client.forked.ForkedThemeLabel;
import club.pineclone.gtavops.client.i18n.I18nLoader;
import club.pineclone.gtavops.client.theme.ExtendedFontUsages;
import club.pineclone.gtavops.client.i18n.ExtendedI18n;
import io.vproxy.vfx.manager.font.FontManager;
import io.vproxy.vfx.util.FXUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.stream.Collectors;

public class ConfigScene extends SceneTemplate {

    private final VOptionButton<String> languageOptionButton;  /* 语言选择按钮 */
    private final ObjectProperty<ClientConfig> config;

    public ConfigScene(ObjectProperty<ExtendedI18n> i18n, ObjectProperty<ClientConfig> config) {
        super(i18n);
        this.config = config;
        enableAutoContentWidthHeight();

        ForkedThemeLabel headerLabel = new ForkedThemeLabel();
        headerLabel.textProperty().bind(Bindings.createStringBinding(() -> i18n.get().configScene.header, i18n));
        FXUtils.observeWidthCenter(getContentPane(), headerLabel);
        headerLabel.setLayoutY(40);

        VBox content = new VBox();
        content.setLayoutY(60);

        /* --------------------- 外观设置 --------------------- */
        /* 语言设置 */
        HBox appearanceSettingDivider = createDivider(Bindings.createStringBinding(() -> i18n.get().configScene.appearanceSetting.title, i18n));
        HBox hBox = getBaseConfigContent(new Insets(24, 7, 0, 20));
        Region spacer = getSpacer();

        ForkedThemeLabel label = new ForkedThemeLabel();
        label.textProperty().bind(Bindings.createStringBinding(() -> i18n.get().configScene.appearanceSetting.language, i18n));
        label.setPadding(new Insets(6, 0, 0, 0));

        Map<String, I18nLoader.I18nItem> i18nMap = I18nLoader.getInstance().getI18nLoaderMap();
        languageOptionButton = new VOptionButton<>(i18nMap.keySet().stream().map(lang -> new VOptionButton.UnstableOptionItem<>(lang, i18nMap.get(lang).getName())).toList());
        languageOptionButton.optionProperty().addListener((obs, oldVal, newVal) -> {
            i18n.set(I18nLoader.getInstance().load(newVal.getOption()));
        });

        hBox.getChildren().addAll(label, spacer, languageOptionButton);

        content.getChildren().addAll(
                appearanceSettingDivider,
                hBox
        );

        FXUtils.observeWidthCenter(getContentPane(), content);
        getContentPane().getChildren().addAll(
                headerLabel,
                content
        );

        /* TODO: 日志popup窗口  */
    }

    private Region getSpacer() {
        Region spacer = new Region();
        spacer.setMinWidth(10);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private HBox getBaseConfigContent(Insets insets) {
        HBox hBox = new HBox(10);
        hBox.setPadding(insets);
        hBox.setPrefWidth(650);
        return hBox;
    }

    protected HBox createDivider(StringBinding intro) {
        HBox hBox = getBaseConfigContent(new Insets(0, 0, 0, 0));
        hBox.setStyle("-fx-border-color: transparent transparent lightblue transparent; -fx-border-width: 0 0 1 0;");

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.BOTTOM_LEFT);
        vBox.setPrefHeight(70);

        Region spacer = getSpacer();
        ForkedThemeLabel label = new ForkedThemeLabel();
        label.textProperty().bind(intro);
        FontManager.get().setFont(ExtendedFontUsages.dividerText, label);
        label.setStyle("-fx-text-fill: lightblue");
        vBox.getChildren().add(label);

        hBox.getChildren().addAll(vBox, spacer);
        return hBox;
    }

    @Override
    public void onUIInit() {  /* 加载所有的配置项 */
        languageOptionButton.setOption(config.get().lang);  /* 客户端语言 */
    }

    @Override
    public void onUIDispose() {  /* 将配置项注入客户端配置中，让修改后的配置跟随FX进程写入文件中保存 */
        config.get().lang = languageOptionButton.getOption();
    }
}
