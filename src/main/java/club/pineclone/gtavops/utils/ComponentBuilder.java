package club.pineclone.gtavops.utils;

import club.pineclone.gtavops.forked.ForkedThemeLabel;
import club.pineclone.gtavops.i18n.ExtendedI18n;
import club.pineclone.gtavops.i18n.I18nContext;
import club.pineclone.gtavops.i18n.I18nText;
import io.vproxy.vfx.manager.font.FontManager;
import io.vproxy.vfx.manager.font.FontUsage;
import io.vproxy.vfx.ui.button.FusionButton;
import io.vproxy.vfx.ui.toggle.ToggleSwitch;
import io.vproxy.vfx.util.FXUtils;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.function.Function;

public class ComponentBuilder<T extends Node> {
    protected ConfigContentBuilder parent;
    protected final I18nContext i18nContext;
    protected final T node;

    private ComponentBuilder(ConfigContentBuilder parent, T node) {
        this.parent = parent;
        this.i18nContext = parent.i18nContext;
        this.node = node;
    }

    private ComponentBuilder(I18nContext i18nContext, T node) {
        this.i18nContext = i18nContext;
        this.node = node;
    }

    public Node build() {
        return this.node;
    }

    public ConfigContentBuilder buildForContent() {
        parent.content.getChildren().add(build());
        return parent;
    }

    public ComponentBuilder<T> style(String css) {
        this.node.setStyle(css);
        return this;
    }

    public static class DividerBuilder extends ComponentBuilder<HBox> {

        private Color color = Color.LIGHTBLUE;

        private DividerBuilder(ConfigContentBuilder parent) {
            super(parent, UIFactory.createHContent());
            node.setPrefHeight(1);
            node.setMinHeight(1);
            node.setMaxHeight(1);
        }

        private DividerBuilder(I18nContext i18nContext) {
            super(i18nContext, UIFactory.createHContent());
        }

        protected static DividerBuilder builder(ConfigContentBuilder parent) {
            return new DividerBuilder(parent);
        }

        public static DividerBuilder builder(I18nContext i18nContext) {
            return new DividerBuilder(i18nContext);
        }

        public DividerBuilder color(Color color) {
            this.color = color;
            return this;
        }

        public DividerBuilder prefHeight(double height) {
            this.node.setPrefHeight(height);
            return this;
        }

        public DividerBuilder maxWidth(double width) {
            this.node.setMaxWidth(width);
            return this;
        }

        public DividerBuilder prefWidth(double width) {
            this.node.setPrefWidth(width);
            return this;
        }

        public DividerBuilder maxHeight(double height) {
            this.node.setMaxHeight(height);
            return this;
        }

        @Override
        public Node build() {
            node.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
            return this.node;
        }
    }

    public static class ThemeLabelBuilder extends ComponentBuilder<ForkedThemeLabel> {

        private ThemeLabelBuilder(ConfigContentBuilder parent) {
            super(parent, new ForkedThemeLabel());
        }

        private ThemeLabelBuilder(I18nContext i18nContext) {
            super(i18nContext, new ForkedThemeLabel());
        }

        protected static ThemeLabelBuilder builder(ConfigContentBuilder parent) {
            return new ThemeLabelBuilder(parent);
        }

        public static ThemeLabelBuilder builder(I18nContext i18nContext) {
            return new ThemeLabelBuilder(i18nContext);
        }

        public ThemeLabelBuilder text(I18nText text) {
            this.node.textProperty().bind(text.binding(i18nContext));
            return this;
        }

        public ThemeLabelBuilder text(String text) {
            this.node.textProperty().set(text);
            return this;
        }

        public ThemeLabelBuilder padding(Insets padding) {
            this.node.setPadding(padding);
            return this;
        }

        public ThemeLabelBuilder font(FontUsage fontUsage) {
            FontManager.get().setFont(fontUsage, node);
            return this;
        }

        public ThemeLabelBuilder style(String css) {
            this.node.setStyle(css);
            return this;
        }
    }

    public static class ListConfigEditorBuilder<T> extends ComponentBuilder<VBox> {

        private Function<ExtendedI18n, String> emptyTextProvider = i -> i.vfxComponent.listConfigEditor.noDataDescription;
        private boolean deletable = true;

        private final ObservableList<T> listProperty;
        private final Function<T, Pane> contentProvider;

        private ListConfigEditorBuilder(ConfigContentBuilder parent, VBox node, ObservableList<T> listProperty, Function<T, Pane> contentProvider) {
            super(parent, node);
            this.listProperty = listProperty;
            this.contentProvider = contentProvider;
        }

        private ListConfigEditorBuilder(I18nContext i18nContext, VBox node, ObservableList<T> listProperty, Function<T, Pane> contentProvider) {
            super(i18nContext, node);
            this.listProperty = listProperty;
            this.contentProvider = contentProvider;
        }

        protected static <T> ListConfigEditorBuilder<T> builder(ConfigContentBuilder parent, ObservableList<T> listProperty, Function<T, Pane> contentProvider) {
            return new ListConfigEditorBuilder<>(parent, UIFactory.createVContent(10), listProperty, contentProvider);
        }

        public static <T> ListConfigEditorBuilder<T> builder(I18nContext i18nContext, ObservableList<T> listProperty, Function<T, Pane> contentProvider) {
            return new ListConfigEditorBuilder<>(i18nContext, UIFactory.createVContent(10), listProperty, contentProvider);
        }

        public ListConfigEditorBuilder<T> spacing(double spacing) {
            this.node.setSpacing(spacing);
            return this;
        }

        public ListConfigEditorBuilder<T> deletable(boolean deletable) {
            this.deletable = deletable;
            return this;
        }

        public ListConfigEditorBuilder<T> emptyText(Function<ExtendedI18n, String> emptyTextProvider) {
            this.emptyTextProvider = emptyTextProvider;
            return this;
        }

        @Override
        public Node build() {
            UIFactory.applyListConfigEditor(this.node, i18nContext, I18nText.of(emptyTextProvider), listProperty, contentProvider, deletable);
            return this.node;
        }

        @Override
        public ConfigContentBuilder buildForContent() {
            VBox listConfigEditor = (VBox) build();
            FXUtils.observeWidth(parent.content, listConfigEditor);  /* 向下继续监听实现动态宽度变化 */
            parent.content.getChildren().add(listConfigEditor);
            return parent;
        }
    }

    public static class FusionButtonBuilder extends ComponentBuilder<FusionButton> {

        private FusionButtonBuilder(ConfigContentBuilder parent) {
            super(parent, new FusionButton());
            node.getTextNode().textProperty().addListener((observable, oldValue, newValue) -> {
                Rectangle2D textBounds = FXUtils.calculateTextBounds(node.getTextNode());
                node.setPrefWidth(Math.max(textBounds.getWidth() + 40, 120));
            });
        }

        private FusionButtonBuilder(I18nContext i18nContext) {
            super(i18nContext, new FusionButton());
            node.getTextNode().textProperty().addListener((observable, oldValue, newValue) -> {
                Rectangle2D textBounds = FXUtils.calculateTextBounds(node.getTextNode());
                node.setPrefWidth(Math.max(textBounds.getWidth() + 40, 120));
            });
        }

        protected static FusionButtonBuilder builder(ConfigContentBuilder parent) {
            return new FusionButtonBuilder(parent);
        }

        public static FusionButtonBuilder builder(I18nContext i18nContext) {
            return new FusionButtonBuilder(i18nContext);
        }

        public FusionButtonBuilder prefHeight(double height) {
            this.node.setPrefHeight(height);
            return this;
        }

        public FusionButtonBuilder prefWidth(double width) {
            this.node.setPrefWidth(width);
            return this;
        }

        public FusionButtonBuilder maxWidth(double width) {
            this.node.setMaxWidth(width);
            return this;
        }

        public FusionButtonBuilder maxHeight(double width) {
            this.node.setMaxHeight(width);
            return this;
        }

        public FusionButtonBuilder text(I18nText text) {
            this.node.getTextNode().textProperty().bind(text.binding(i18nContext));
            return this;
        }

        public FusionButtonBuilder text(String text) {
            node.getTextNode().textProperty().set(text);
            return this;
        }

        public FusionButtonBuilder onMouseClick(EventHandler<MouseEvent> handler) {
            this.node.setOnMouseClicked(handler);
            return this;
        }

        @Override
        public Node build() {
//            if (this.text != null);  /* 如果通过 text 设置值那么需要手动触发更新，让监听器调整按钮大小 */
            return this.node;
        }
    }

    public static class ToggleSwitchBuilder extends ComponentBuilder<Pane> {
        private double radius = 15;
        private double length = 60;
        private BooleanProperty selectedProperty = null;

        private ToggleSwitchBuilder(ConfigContentBuilder parent) {
            super(parent, new HBox());
        }

        private ToggleSwitchBuilder(I18nContext i18nContext) {
            super(i18nContext, new HBox());
        }

        public ToggleSwitchBuilder radius(double radius) {
            this.radius = radius;
            return this;
        }

        public ToggleSwitchBuilder length(double length) {
            this.length = length;
            return this;
        }

        public ToggleSwitchBuilder bindSelected(BooleanProperty booleanProperty) {
            this.selectedProperty = booleanProperty;
            return this;
        }

        protected static ToggleSwitchBuilder builder(ConfigContentBuilder parent) {
            return new ToggleSwitchBuilder(parent);
        }

        public static ToggleSwitchBuilder builder(I18nContext i18nContext) {
            return new ToggleSwitchBuilder(i18nContext);
        }

        @Override
        public Node build() {
            ToggleSwitch toggleSwitch = new ToggleSwitch(radius, length);
            if (selectedProperty != null) toggleSwitch.selectedProperty().bindBidirectional(selectedProperty);
            return toggleSwitch.getNode();
        }
    }
}
