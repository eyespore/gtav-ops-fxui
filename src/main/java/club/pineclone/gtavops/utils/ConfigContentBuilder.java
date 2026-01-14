package club.pineclone.gtavops.utils;

import club.pineclone.gtavops.component.I18nKeyChooseButton;
import club.pineclone.gtavops.component.I18nOptionButton;
import club.pineclone.gtavops.component.I18nTriggerModeChooseButton;
import club.pineclone.gtavops.forked.I18nKeyChooser;
import club.pineclone.gtavops.i18n.ExtendedI18n;
import club.pineclone.gtavops.i18n.I18nContext;
import club.pineclone.gtavops.i18n.I18nText;
import club.pineclone.gtavops.common.TriggerMode;
import io.vproxy.base.util.anno.Nullable;
import io.vproxy.vfx.entity.input.Key;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 配置面板构建器，提供多种布局设计配置面板
 */
public class ConfigContentBuilder {

    private static final double DEF_FORM_PREF_HEIGHT = 60;
    private static final double DEF_FORM_PREF_WIDTH = 650;
    private static final double DEF_FORM_SPACING = 15;
    private static final Insets DEF_FORM_PADDING = new Insets(20, 0, 20, 0);

    public static final Insets DEF_ROW_PADDING = new Insets(0, 7, 0, 20);

    private static final double DEF_BTN_PREF_HEIGHT = 35;

    protected final Pane content;
    protected final I18nContext i18nContext;

    @Nullable protected ConfigContentBuilder parent;

    protected double prefHeight = -1;
    protected double prefWidth = -1;
    protected Insets padding = Insets.EMPTY;

    private ConfigContentBuilder(I18nContext i18nContext, Pane content) {
        this.content = content;
        this.content.setPadding(padding);
        this.i18nContext = i18nContext;
    }

    private ConfigContentBuilder(ConfigContentBuilder parent, Pane content) {
        this.content = content;
        this.i18nContext = parent.i18nContext;
        this.parent = parent;
    }

    /* 属性 */
    public ConfigContentBuilder padding(Insets padding) {
        this.content.setPadding(padding);
        return this;
    }

    public ConfigContentBuilder style(String css) {
        this.content.setStyle(css);
        return this;
    }

    /* 封装方法 */
    public ConfigContentBuilder toggleButtonRow(Function<ExtendedI18n, String> labelTextProvider, BooleanProperty selectedProperty) {
        return this.row().padding(DEF_ROW_PADDING)
                .themeLabel().text(I18nText.of(labelTextProvider)).buildForContent()
                .hSpacer()
                .toggleButton().bindSelected(selectedProperty).buildForContent()
                .buildForParent();
    }

    public ConfigContentBuilder dividerRow(Function<ExtendedI18n, String> labelTextProvider) {
        return this.line().themeLabel().text(I18nText.of(labelTextProvider)).buildForContent()
                .divider().buildForContent().buildForParent();
    }

    public ConfigContentBuilder triggerModeChooseButtonRow(Function<ExtendedI18n, String> labelTextProvider, int flags, ObjectProperty<TriggerMode> triggerModeProperty) {
        return this.row().padding(DEF_ROW_PADDING)
                .line().alignment(Pos.CENTER)
                    .themeLabel().text(I18nText.of(labelTextProvider)).buildForContent()
                .buildForParent()
                .hSpacer()
                .triggerModeChoose(flags, triggerModeProperty)
                .buildForParent();
    }

    public <T> ConfigContentBuilder listConfigEditorRow(Function<ExtendedI18n, String> emptyTextProvider, ObservableList<T> listProperty, Function<T, Pane> contentProvider) {
        return this.listConfigEditorRow(emptyTextProvider, listProperty, contentProvider, true);
    }

    public <T> ConfigContentBuilder listConfigEditorRow(Function<ExtendedI18n, String> emptyTextProvider, ObservableList<T> listProperty, Function<T, Pane> contentProvider, boolean deletable) {
        return this.line().padding(DEF_ROW_PADDING)
                .divider().buildForContent().gap(10)
                .listConfigEditor(listProperty, contentProvider).deletable(deletable).emptyText(emptyTextProvider).buildForContent()
                .gap(10)
                .divider().buildForContent()
                .buildForParent();
    }

    public <T> ConfigContentBuilder listConfigEditorRow(
            Function<ExtendedI18n, String> labelText,
            Function<ExtendedI18n, String> emptyTextProvider,
            ObservableList<T> listProperty,
            Function<T, Pane> contentProvider,
            Supplier<T> defaultValueProvider) {

        return this.line().padding(DEF_ROW_PADDING)
                .row()
                    .line().alignment(Pos.CENTER)
                        .themeLabel().text(I18nText.of(labelText)).buildForContent()
                    .buildForParent()
                    .hSpacer()
                    .fusionButton().text(I18nText.of(i -> i.vfxComponent.listConfigEditor.addDefaultValue))
                    .onMouseClick(e -> listProperty.add(defaultValueProvider.get())).buildForContent()
                .buildForParent().gap(10)  /* 间隔控件与列表 */
                .divider().buildForContent().gap(10)
                .listConfigEditor(listProperty, contentProvider).spacing(15).emptyText(emptyTextProvider).buildForContent()
                .gap(10)
                .divider().buildForContent()
                .buildForParent();
    }

    public ConfigContentBuilder keyChooseButtonRow(Function<ExtendedI18n, String> labelTextProvider, ObjectProperty<Key> keyProperty) {
        return this.keyChooseButtonRow(labelTextProvider, I18nKeyChooser.FLAG_WITH_KEY, keyProperty);
    }

    public ConfigContentBuilder keyChooseButtonRow(Function<ExtendedI18n, String> labelTextProvider, int flags, ObjectProperty<Key> keyProperty) {
        return this.row().padding(DEF_ROW_PADDING)
                .line().alignment(Pos.CENTER)
                    .themeLabel().text(I18nText.of(labelTextProvider)).buildForContent()
                .buildForParent()
                .hSpacer()
                .keyChooseButton(flags, keyProperty)
                .buildForParent();
    }

    public ConfigContentBuilder sliderRow(Function<ExtendedI18n, String> labelTextProvider, double length, int min, int max, DoubleProperty percentageProperty) {
        return this.row().padding(DEF_ROW_PADDING)
                .line().alignment(Pos.CENTER)
                    .themeLabel().text(I18nText.of(labelTextProvider)).buildForContent()
                .buildForParent()
                .hSpacer()
                .slider(length, min, max, percentageProperty)
                .buildForParent();
    }

    public <T> ConfigContentBuilder optionButtonRow(Function<ExtendedI18n, String> labelTextProvider, LinkedHashMap<T, I18nText> i18Map, ObjectProperty<T> optionProperty) {
        return this.row().padding(DEF_ROW_PADDING)
                .line().alignment(Pos.CENTER)
                    .themeLabel().text(I18nText.of(labelTextProvider)).buildForContent()
                .buildForParent()
                .hSpacer()
                .optionButton(i18Map, optionProperty)
                .buildForParent();
    }

    /* 原子方法 */
    public ComponentBuilder.ThemeLabelBuilder themeLabel() {
        return ComponentBuilder.ThemeLabelBuilder.builder(this);
    }

    public ComponentBuilder.FusionButtonBuilder fusionButton() {
        return ComponentBuilder.FusionButtonBuilder.builder(this).prefHeight(35);
    }

    public ConfigContentBuilder hSpacer() {
        content.getChildren().add(UIFactory.createHSpacer());
        return this;
    }

    public ComponentBuilder.ToggleSwitchBuilder toggleButton() {
//        content.getChildren().add(UIFactory.createToggleSwitch(selectedProperty).getNode());
        return ComponentBuilder.ToggleSwitchBuilder.builder(this);
    }

    public ConfigContentBuilder triggerModeChoose(int flags, ObjectProperty<TriggerMode> triggerModeProperty) {
        I18nTriggerModeChooseButton btn = UIFactory.createTriggerModeChooseButton(i18nContext, flags, triggerModeProperty);
        btn.setPrefHeight(DEF_BTN_PREF_HEIGHT);  // TODO: 改造到工厂
        content.getChildren().add(btn);
        return this;
    }

    public ConfigContentBuilder keyChooseButton(ObjectProperty<Key> keyProperty) {
        return this.keyChooseButton(I18nKeyChooser.FLAG_WITH_KEY, keyProperty);
    }

    public ConfigContentBuilder keyChooseButton(int flags, ObjectProperty<Key> keyProperty) {
        I18nKeyChooseButton btn = UIFactory.createKeyChooseButton(i18nContext, flags, keyProperty);
        btn.setPrefHeight(DEF_BTN_PREF_HEIGHT);  // TODO: 改造到工厂
        content.getChildren().add(btn);
        return this;
    }

    public ConfigContentBuilder keyChooseButton(ObjectProperty<Key> keyProperty, boolean disableAnimation) {
        I18nKeyChooseButton btn = UIFactory.createKeyChooseButton(i18nContext, keyProperty);
        btn.setPrefHeight(DEF_BTN_PREF_HEIGHT);  // TODO: 改造到工厂
        btn.setDisableAnimation(disableAnimation);
        content.getChildren().add(btn);
        return this;
    }

    public ConfigContentBuilder slider(double length, int min, int max, DoubleProperty percentageProperty) {
        content.getChildren().add(UIFactory.createSlider(length, min, max, percentageProperty));
        return this;
    }

    public <T> ConfigContentBuilder optionButton(LinkedHashMap<T, I18nText> i18nMap, ObjectProperty<T> optionProperty) {
        I18nOptionButton<T> btn = UIFactory.createOptionButton(i18nContext, i18nMap, optionProperty);
        btn.setPrefHeight(DEF_BTN_PREF_HEIGHT);  // TODO: 改造到工厂
        content.getChildren().add(btn);
        return this;
    }

    public ConfigContentBuilder prefHeight(double height) {
        this.prefHeight = height;
        return this;
    }

    protected <T> ComponentBuilder.ListConfigEditorBuilder<T> listConfigEditor(ObservableList<T> listProperty, Function<T, Pane> contentProvider) {
        return ComponentBuilder.ListConfigEditorBuilder.builder(this, listProperty, contentProvider);
    }

    public ComponentBuilder.DividerBuilder divider() {
        return ComponentBuilder.DividerBuilder.builder(this);
    }

    public ConfigContentBuilder gap(double height) {
        HBox gap = UIFactory.createHContent();
        gap.setMinHeight(height);
        content.getChildren().add(gap);
        return this;
    }

    /* 行布局配置面板 */
    public ConfigRowBuilder row() {
        return ConfigContentBuilder.ConfigRowBuilder.builder(this);
    }

    /* 列布局配置面板 */
    public ConfigLineBuilder line() {
        return ConfigContentBuilder.ConfigLineBuilder.builder(this);
    }

    public static ConfigContentBuilder.ConfigRowBuilder defaultRow(I18nContext i18nContext) {
        return (ConfigContentBuilder.ConfigRowBuilder) ConfigContentBuilder.ConfigRowBuilder
                .builder(i18nContext).padding(DEF_ROW_PADDING);
    }

    /* 配置入口点 */
    /* 列布局配置面板 */
    public static ConfigContentBuilder.ConfigLineBuilder defaultLine(I18nContext i18nContext) {
        return (ConfigContentBuilder.ConfigLineBuilder)ConfigContentBuilder.ConfigLineBuilder
                .builder(i18nContext).spacing(ConfigContentBuilder.DEF_FORM_SPACING).padding(DEF_FORM_PADDING);
    }


    public ConfigContentBuilder buildForParent() {  /* 用于实现 DSL 链式调用，实现从上层组件下沉 */
        parent.content.getChildren().add(build());
        return parent;
    }

    public Pane build() {
        if (prefHeight != -1) content.setPrefHeight(prefHeight);
        if (prefWidth != -1) content.setPrefWidth(prefWidth);
        return content;
    }

    /* 基于 HBox 的配置面板  */
    public static class ConfigRowBuilder extends ConfigContentBuilder {
        private ConfigRowBuilder(I18nContext i18nContext) {
            super(i18nContext, UIFactory.createHContent());
        }

        private ConfigRowBuilder(ConfigContentBuilder parent) {
            super(parent, UIFactory.createHContent());
        }

        public static ConfigRowBuilder builder(I18nContext i18nContext) {
            return new ConfigRowBuilder(i18nContext);
        }

        protected static ConfigRowBuilder builder(ConfigContentBuilder parent) {
            return new ConfigRowBuilder(parent);
        }

        public ConfigRowBuilder spacing(double space) {
            ((HBox) this.content).setSpacing(space);
            return this;
        }

        public ConfigRowBuilder alignment(Pos pos) {
            ((HBox) this.content).setAlignment(pos);
            return this;
        }
    }

    /* 基于 VBox 的配置面板 */
    public static class ConfigLineBuilder extends ConfigContentBuilder {
        private ConfigLineBuilder(I18nContext i18nContext) {
            super(i18nContext, UIFactory.createVContent());
        }

        private ConfigLineBuilder(ConfigContentBuilder parent) {
            super(parent, UIFactory.createVContent());
        }

        public static ConfigLineBuilder builder(I18nContext i18nContext) {
            return new ConfigLineBuilder(i18nContext);
        }

        protected static ConfigLineBuilder builder(ConfigContentBuilder parent) {
            return new ConfigLineBuilder(parent);
        }

        public ConfigLineBuilder spacing(double space) {
            ((VBox) this.content).setSpacing(space);
            return this;
        }

        public ConfigLineBuilder alignment(Pos pos) {
            ((VBox) this.content).setAlignment(pos);
            return this;
        }
    }
}
