package club.pineclone.gtavops.client.macrotoggle;

import club.pineclone.gtavops.client.forked.ForkedSlider;
import club.pineclone.gtavops.client.forked.ForkedThemeLabel;
import club.pineclone.gtavops.client.theme.ExtendedFontUsages;
import club.pineclone.gtavops.client.i18n.ExtendedI18n;
import io.vproxy.vfx.manager.font.FontManager;
import io.vproxy.vfx.ui.button.FusionButton;
import io.vproxy.vfx.ui.layout.HPadding;
import io.vproxy.vfx.ui.layout.VPadding;
import io.vproxy.vfx.ui.stage.VStage;
import io.vproxy.vfx.ui.stage.VStageInitParams;
import io.vproxy.vfx.ui.toggle.ToggleSwitch;
import io.vproxy.vfx.ui.wrapper.ThemeLabel;
import io.vproxy.vfx.util.FXUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public abstract class MacroSettingStage {

    private final VStage vStage;
    private final VBox content;
    protected final ObjectProperty<ExtendedI18n> i18n;  /* 本地化 */

    public MacroSettingStage(ObjectProperty<ExtendedI18n> i18n, Function<ExtendedI18n, String> titleProvider) {
        this.i18n = i18n;
        vStage = new VStage(new VStageInitParams()
                .setMaximizeAndResetButton(false)
                .setIconifyButton(false)
        );

        vStage.getInitialScene().enableAutoContentWidthHeight();
        vStage.getStage().setWidth(700);
        vStage.getStage().setHeight(500);
        vStage.getStage().initModality(Modality.APPLICATION_MODAL);
        content = new VBox();

        i18n.addListener((obs, oldVal, newVal) -> {
            vStage.setTitle(titleProvider.apply(newVal));
        });

        /* 由于设置面板并非应用启动后第一时间加载，因此不会得到 i18n 更新的触发，此处需要手动更新一次I18n */
        vStage.setTitle(titleProvider.apply(i18n.get()));
    }

    protected VBox getContent() {
        return content;
    }

    private VStage getStage() {
        return vStage;
    }

    @Deprecated
    private void show() {
        FXUtils.observeWidth(getStage().getInitialScene().getNode(), getContent(), -30);
        HBox hbox = new HBox(new HPadding(10), content);
        vStage.getInitialScene().getScrollPane().setContent(hbox);
        vStage.show();
    }

    public void showAndWait() {
        FXUtils.observeWidth(getStage().getInitialScene().getNode(), getContent(), -30);
        HBox hbox = new HBox(new HPadding(10), content);
        vStage.getInitialScene().getScrollPane().setContent(hbox);
        vStage.showAndWait();
    }

    protected void onVSettingStageInit() {}

    protected void onVSettingStageExit() {}

    public void initVSettingStage() {
        onVSettingStageInit();
    }

    public void exitVSettingStage() {
        onVSettingStageExit();
    }

    private StringBinding createStringBinding(Function<ExtendedI18n, String> provider) {
        return Bindings.createStringBinding(() -> provider.apply(i18n.get()), i18n);
    }

    protected HBox createToggle(Function<ExtendedI18n, String> introProvider, ToggleSwitch toggle) {
        HBox hBox = getBaseConfigContent(new Insets(22, 7, 0, 20));
        hBox.setPrefHeight(60);
        Region spacer = getSpacer();

        ForkedThemeLabel label = new ForkedThemeLabel();
        label.textProperty().bind(createStringBinding(introProvider));
        label.setPadding(new Insets(4, 0, 0, 0));
        hBox.getChildren().addAll(label, spacer, toggle.getNode());
        return hBox;
    }

    protected HBox createButton(Function<ExtendedI18n, String> introProvider, FusionButton... buttons) {
        HBox hBox = getBaseConfigContent(new Insets(24, 7, 0, 20));
        Region spacer = getSpacer();

        ForkedThemeLabel label = new ForkedThemeLabel();
        label.textProperty().bind(createStringBinding(introProvider));
        label.setPadding(new Insets(7, 0, 0, 0));
        hBox.getChildren().addAll(label, spacer);
        Arrays.stream(buttons).forEach(hBox.getChildren()::addAll);
        return hBox;
    }

    protected HBox createDivider(Function<ExtendedI18n, String> introProvider) {
        HBox hBox = getBaseConfigContent(new Insets(0, 0, 0, 0));
        hBox.setStyle("-fx-border-color: transparent transparent lightblue transparent; " +
                "-fx-border-width: 0 0 1 0;");

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.BOTTOM_LEFT);
        vBox.setPrefHeight(70);

        Region spacer = getSpacer();
        ForkedThemeLabel label = new ForkedThemeLabel();
        label.textProperty().bind(createStringBinding(introProvider));
        FontManager.get().setFont(ExtendedFontUsages.dividerText, label);
        label.setStyle("-fx-text-fill: lightblue");
        vBox.getChildren().add(label);

        hBox.getChildren().addAll(vBox, spacer);
        return hBox;
    }

    protected HBox createSlider(Function<ExtendedI18n, String> introProvider, ForkedSlider slider) {
        HBox hBox = getBaseConfigContent(new Insets(24, 7, 0, 20));
        hBox.setPrefHeight(70);
        Region spacer = getSpacer();

        ForkedThemeLabel label = new ForkedThemeLabel();
        label.textProperty().bind(createStringBinding(introProvider));
        label.setPadding(new Insets(4, 0, 0, 0));
        hBox.getChildren().addAll(label, spacer, slider);
        return hBox;
    }

    protected HBox createButtonToggle(Function<ExtendedI18n, String> introProvider, ToggleSwitch toggle, FusionButton... buttons) {
        HBox baseContent = getBaseConfigContent(new Insets(0, 0, 0, 0));
        baseContent.setPrefHeight(60);

        HBox labelContent = new HBox(10);
        labelContent.setPadding(new Insets(22, 7, 0, 20));
        Region spacer = getSpacer();
        ForkedThemeLabel label = new ForkedThemeLabel();
        label.textProperty().bind(createStringBinding(introProvider));
        label.setPadding(new Insets(7, 0, 0, 0));
        labelContent.getChildren().addAll(label);

        HBox componentContent = new HBox(20);
        componentContent.setPadding(new Insets(24, 7, 0, 20));

        componentContent.getChildren().add(new HBox(20) {{
            Arrays.stream(buttons).forEach(this.getChildren()::addAll);
        }});

        componentContent.getChildren().add(new HBox() {{
            setPadding(new Insets(3, 0, 0, 0));
            getChildren().add(toggle.getNode());
        }});

        baseContent.getChildren().addAll(labelContent, spacer, componentContent);
        return baseContent;
    }

    protected HBox createGap(double height) {
        HBox hBox = getBaseConfigContent(new Insets(0, 0, 0, 0));
        hBox.setPrefHeight(height);
        return hBox;
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

    protected ContentBuilder contentBuilder() {
        return new ContentBuilder();
    }

    protected class ContentBuilder {
        private final List<Node> items = new ArrayList<>();

        /* 开关按钮 */
        public ContentBuilder toggle(Function<ExtendedI18n, String> introProvider, ToggleSwitch toggle) {
            items.add(createToggle(introProvider, toggle));
            return this;
        }

        /* 一般按钮 */
        public ContentBuilder button(Function<ExtendedI18n, String> introProvider, FusionButton... buttons) {
            items.add(createButton(introProvider, buttons));
            return this;
        }

        /* 按钮以及开关 */
        public ContentBuilder buttonToggle(Function<ExtendedI18n, String> introProvider, ToggleSwitch toggle, FusionButton... buttons) {
            items.add(createButtonToggle(introProvider, toggle, buttons));
            return this;
        }

        /* 拖动条 */
        public ContentBuilder slider(Function<ExtendedI18n, String> introProvider, ForkedSlider slider) {
            items.add(createSlider(introProvider, slider));
            return this;
        }

        /* 自定义高度间隔 */
        public ContentBuilder gap(double height) {
            items.add(createGap(height));
            return this;
        }

        /* 标准高度间隔 */
        public ContentBuilder gap() {
            items.add(createGap(40));
            return this;
        }

        /* 分割线 */
        public ContentBuilder divide(Function<ExtendedI18n, String> introProvider) {
            items.add(createDivider(introProvider));
            return this;
        }

        public List<Node> build() {
            items.add(new VPadding(20));
            return items;
        }
    }
}
