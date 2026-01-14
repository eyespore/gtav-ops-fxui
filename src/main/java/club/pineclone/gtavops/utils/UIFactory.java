package club.pineclone.gtavops.utils;

import club.pineclone.gtavops.component.I18nKeyChooseButton;
import club.pineclone.gtavops.component.I18nOptionButton;
import club.pineclone.gtavops.component.I18nTriggerModeChooseButton;
import club.pineclone.gtavops.forked.ForkedSlider;
import club.pineclone.gtavops.forked.ForkedThemeLabel;
import club.pineclone.gtavops.forked.I18nKeyChooser;
import club.pineclone.gtavops.i18n.I18nContext;
import club.pineclone.gtavops.i18n.I18nText;
import club.pineclone.gtavops.common.TriggerMode;
import io.vproxy.vfx.entity.input.Key;
import io.vproxy.vfx.ui.button.FusionButton;
import io.vproxy.vfx.ui.toggle.ToggleSwitch;
import io.vproxy.vfx.util.FXUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 创建配置项目
 */
public class UIFactory {

    private static final Logger log = LoggerFactory.getLogger(UIFactory.class);

    protected static VBox createVContent() {
        return createVContent(Insets.EMPTY, 0);
    }

    protected static VBox createVContent(double spacing) {
        return createVContent(Insets.EMPTY, spacing);
    }

    protected static VBox createVContent(Insets insets, double spacing) {
        VBox vBox = new VBox(spacing);
        vBox.setPadding(insets);
        return vBox;
    }

    protected static HBox createHContent() {
        return createHContent(Insets.EMPTY, 0);
    }

    protected static HBox createHContent(double spacing) {
        return createHContent(Insets.EMPTY, spacing);
    }

    protected static HBox createHContent(Insets insets, double spacing) {
        HBox hBox = new HBox(spacing);
        hBox.setPadding(insets);
        return hBox;
    }

    /**
     * 列表编辑，适用于基于List的动态配置项，允许自定义添加和删除配置项，应该通过ConfigUIBuilder构建一个列表编辑项
     * 而不是直接使用ConfigUIFactory
     * @param i18nContext 本地化上下文
     * @param emptyText 当不存在任何配置项时向用户展示的文本
     * @param listProperty 外部使用的 MVVM 属性，该属性会跟随组件中的值增加或删除
     * @param <T> 任意配置值，配置值可以包含更多的Property，适用于配置复杂的列表配置项
     */
    protected static <T> void applyListConfigEditor(VBox listConfigEditor,
                                                    I18nContext i18nContext,
                                                    I18nText emptyText,
                                                    ObservableList<T> listProperty,
                                                    Function<T, Pane> contentProvider,
                                                    boolean deletable) {
        double rowContentHeight = 30;  /* 避免 VFX 自动计算大小导致空值UI填充和实际值存在时大小不符 */
        HBox emptyRow = createHContent();
        emptyRow.setMinHeight(rowContentHeight);
        emptyRow.setAlignment(Pos.CENTER);
        emptyRow.getChildren().add(createThemeLabel(i18nContext, emptyText));

        /* 空行刷新 */
        Consumer<Void> refreshEmptyRow = v -> {
            listConfigEditor.getChildren().remove(emptyRow);
            if (listProperty.isEmpty()) {
                listConfigEditor.getChildren().add(emptyRow);
            }
        };

        Consumer<T> addItem = v -> {
            /* 行整体 */
            Pane content = createHContent(5);  /* 删除按钮和后部 Content 之间保留一定间隙 */
            content.setUserData(v);
            int wDelta = 0;
            if (deletable) {  /* 允许删除 */
                FusionButton delBtn = createFusionButton("-");
                delBtn.setOnMouseClicked(e -> listProperty.remove(v));  /* 按钮回调在 FX 线程中执行，不会引起ConcurrentModifiedException */
                delBtn.setPrefWidth(50);
                delBtn.setMaxHeight(25);
                delBtn.setDisableAnimation(true);  /* 关闭动画，由于按钮创建时机不一致，会导致动画播放混乱，观感较差 */
                content.getChildren().add(new VBox(delBtn) {{
                    setAlignment(Pos.CENTER);
                }});
                wDelta = -20;
            }

            Pane pane = contentProvider.apply(v);
            FXUtils.observeWidth(content, pane, wDelta);  /* 通过 listEditor -> content -> pane 监听动态更新宽度 */
            content.getChildren().add(pane);
            FXUtils.observeWidth(listConfigEditor, content);
//            content.setBackground(background);

            listConfigEditor.getChildren().add(content);
        };

        listProperty.addListener((ListChangeListener<T>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (T value : change.getAddedSubList()) {  /* 元素追加 */
                        addItem.accept(value);
                    }
                }

                if (change.wasRemoved()) {
                    for (T value :change.getRemoved()){  /* 元素删除 */
                        listConfigEditor.getChildren().removeIf(node -> value.equals(node.getUserData()));
                    }
                }
            }
            refreshEmptyRow.accept(null);  /* 更新完成后检查列表是否为空 */
        });

        listProperty.forEach(addItem);  /* 追加初始元素 */
        refreshEmptyRow.accept(null);  /* 初始化时刷新 */
    }

    protected static FusionButton createFusionButton(String text) {
        FusionButton fusionButton = new FusionButton();
        fusionButton.getTextNode().textProperty().addListener((observable, oldValue, newValue) -> {
            Rectangle2D textBounds = FXUtils.calculateTextBounds(fusionButton.getTextNode());
            fusionButton.setPrefWidth(Math.max(textBounds.getWidth() + 40, 120));
        });

        fusionButton.setPrefHeight(40);
        fusionButton.getTextNode().textProperty().set(text);

        return fusionButton;
    }

    /* 触发模式按钮 */
    protected static I18nTriggerModeChooseButton createTriggerModeChooseButton(I18nContext i18nContext, int flags, ObjectProperty<TriggerMode> triggerModeProperty) {
        I18nTriggerModeChooseButton triggerModeChooseButton = new I18nTriggerModeChooseButton(i18nContext, flags);
        triggerModeChooseButton.optionProperty().bindBidirectional(triggerModeProperty);
        return triggerModeChooseButton;
    }

    /* 按键选择按钮 */
    protected static I18nKeyChooseButton createKeyChooseButton(I18nContext i18nContext, ObjectProperty<Key> keyProperty) {
        return createKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY, keyProperty);
    }

    protected static I18nKeyChooseButton createKeyChooseButton(I18nContext i18nContext, int flags, ObjectProperty<Key> keyProperty) {
        I18nKeyChooseButton keyChooseButton = new I18nKeyChooseButton(i18nContext, flags);
        keyChooseButton.keyProperty().bindBidirectional(keyProperty);
        return keyChooseButton;
    }

    /* 多选按钮 */
    protected static <T> I18nOptionButton<T> createOptionButton(I18nContext i18nContext, LinkedHashMap<T, I18nText> map, ObjectProperty<T> optionProperty) {
        I18nOptionButton<T> optionButton = new I18nOptionButton<>(i18nContext, map);
        optionButton.optionProperty().bindBidirectional(optionProperty);
        return optionButton;
    }

    /* 拖动条 */
    protected static ForkedSlider createSlider(double length, int min, int max, DoubleProperty percentageProperty) {
        ForkedSlider slider = new ForkedSlider();
        slider.setLength(length);
        slider.setRange(min, max);
        slider.percentageProperty().bindBidirectional(percentageProperty);
        return slider;
    }

    /* 弹簧 */
    protected static Region createHSpacer() {
        Region spacer = new Region();
        spacer.setMinWidth(10);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    /* 文本栏 */
    protected static Label createThemeLabel(I18nContext i18nContext, I18nText labelText) {
        return createThemeLabel(i18nContext, labelText, Insets.EMPTY);
    }

    protected static Label createThemeLabel(I18nContext i18nContext, I18nText labelText, Insets insets) {
        ForkedThemeLabel label = new ForkedThemeLabel();
        label.textProperty().bind(labelText.binding(i18nContext));
        label.setPadding(insets);
        return label;
    }

    protected static ToggleSwitch createToggleSwitch(BooleanProperty selectedProperty) {
        ToggleSwitch toggleSwitch = new ToggleSwitch();
        toggleSwitch.selectedProperty().bindBidirectional(selectedProperty);
        return toggleSwitch;
    }
}
