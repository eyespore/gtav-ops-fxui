package club.pineclone.gtavops.scene;

import club.pineclone.gtavops.component.I18nOptionButton;
import club.pineclone.gtavops.config.ClientConfig;
import club.pineclone.gtavops.forked.ForkedThemeLabel;
import club.pineclone.gtavops.i18n.I18nContext;
import club.pineclone.gtavops.i18n.I18nLoader;
import club.pineclone.gtavops.i18n.I18nText;
import club.pineclone.gtavops.theme.ExtendedFontUsages;
import io.vproxy.vfx.manager.font.FontManager;
import io.vproxy.vfx.ui.button.FusionButton;
import io.vproxy.vfx.ui.scene.VSceneGroup;
import io.vproxy.vfx.util.FXUtils;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigScene extends SceneTemplate {

    private final I18nOptionButton<String> languageOptionButton;  /* 语言选择按钮 */
    private final ObjectProperty<ClientConfig> config;

    private final VBox navigateButtonsContent;

    private final List<SceneTemplate> mainScenes = new ArrayList<>();  // TODO: 应用配置Scene
    private VSceneGroup sceneGroup;

    public ConfigScene(I18nContext i18nContext, ObjectProperty<ClientConfig> config) {
        super(i18nContext);
        this.config = config;
        enableAutoContentWidthHeight();

        ForkedThemeLabel headerLabel = new ForkedThemeLabel();
        headerLabel.textProperty().bind(I18nText.of(i -> i.configScene.header).binding(i18nContext));
        FXUtils.observeWidthCenter(getContentPane(), headerLabel);
        headerLabel.setLayoutY(40);

        BorderPane content = new BorderPane();
        content.setLayoutY(60);

        VBox settingContent = new VBox();
//        settingContent.setLayoutY(60);
        settingContent.setPadding(new Insets(0, 30, 0, 30));

        /* --------------------- 外观设置 --------------------- */
        /* 语言设置 */
        HBox appearanceSettingDivider = createDivider(I18nText.of(i -> i.configScene.appearanceSetting.title).binding(i18nContext));
        HBox appearanceSettingContent = getBaseConfigContent(new Insets(24, 7, 0, 20));

        ForkedThemeLabel languageSettingLabel = new ForkedThemeLabel();
        languageSettingLabel.textProperty().bind(I18nText.of(i -> i.configScene.appearanceSetting.language).binding(i18nContext));
        languageSettingLabel.setPadding(new Insets(6, 0, 0, 0));

        Map<String, I18nLoader.I18nItem> i18nMap = I18nLoader.getInstance().getI18nLoaderMap();

        LinkedHashMap<String, I18nText> optionMap = new LinkedHashMap<>();
        i18nMap.forEach((lang, item) -> optionMap.put(lang, I18nText.of(item.getName())));
        languageOptionButton = new I18nOptionButton<>(i18nContext, optionMap);

        languageOptionButton.optionProperty().addListener((obs, oldVal, newVal) -> {
            i18nContext.set(I18nLoader.getInstance().load(newVal));
        });

        appearanceSettingContent.getChildren().addAll(languageSettingLabel, getSpacer(), languageOptionButton);

        navigateButtonsContent = new VBox();
        navigateButtonsContent.setSpacing(20);
        navigateButtonsContent.setAlignment(Pos.TOP_LEFT);  // 顶部左对齐
        navigateButtonsContent.setPadding(new Insets(20, 0, 0, 0));
        navigateButtonsContent.getChildren().addAll(
                createNavigateButton(I18nText.of(i -> i.configScene.appearanceSetting.title), 0),
                createNavigateButton(I18nText.of(i -> i.configScene.appearanceSetting.title), 1)
        );

        settingContent.getChildren().addAll(
                appearanceSettingDivider,
                appearanceSettingContent
        );

        content.setLeft(navigateButtonsContent);
        content.setCenter(settingContent);
        content.setBackground(new Background(new BackgroundFill(
                new Color(0x8f / 255d, 0xb8 / 255d, 0xd8 / 255d, 1), //
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
//        content.getChildren().addAll(navigateButtonsContent, settingContent);

//        FXUtils.observeWidthCenter(getContentPane(), content);
        FXUtils.observeWidth(getContentPane(), content);
        getContentPane().getChildren().addAll(
                headerLabel,
                content
        );

        /* TODO: 日志popup窗口  */
    }

    @Deprecated
    private Region getSpacer() {
        Region spacer = new Region();
        spacer.setMinWidth(10);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    @Deprecated
    private HBox getBaseConfigContent(Insets insets) {
        HBox hBox = new HBox(10);
        hBox.setPadding(insets);
        hBox.setPrefWidth(650);
        return hBox;
    }

    @Deprecated
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

    // TODO: 将创建逻辑迁移到UI Factory

    @Deprecated
    private FusionButton createNavigateButton(I18nText labelText, int index) {
        return new FusionButton() {{
            setPrefWidth(150);
            setPrefHeight(40);  /* 40 */
            setDisableAnimation(true);
            getTextNode().textProperty().bind(labelText.binding(i18nContext));

//            setOnAction(e -> {
//                if (isSwitch) return;  /* Scene 正在切换，禁用事件点击 */
//
//                var current = sceneGroup.getCurrentMainScene();
//                //noinspection SuspiciousMethodCalls
//                int currentIndex = mainScenes.indexOf(current);
//                if (currentIndex == index) return;
//
//                VSceneShowMethod method = index > currentIndex ?
//                        VSceneShowMethod.FROM_RIGHT : VSceneShowMethod.FROM_LEFT;
//
//                targetIndex = index;
//                isSwitch = true;
//                navigatorButtons.forEach(b -> b.setDisable(true));
//                SceneTemplate newScene = mainScenes.get(index);
//                sceneGroup.show(newScene, VSceneShowMethod.FADE_IN);
//            });
        }};
    }

    @Override
    public void onUIInit() {  /* 加载所有的配置项 */
        languageOptionButton.optionProperty().set(config.get().lang);  /* 客户端语言 */
    }

    @Override
    public void onUIDispose() {  /* 将配置项注入客户端配置中，让修改后的配置跟随FX进程写入文件中保存 */
        config.get().lang = languageOptionButton.optionProperty().get();
    }
}
