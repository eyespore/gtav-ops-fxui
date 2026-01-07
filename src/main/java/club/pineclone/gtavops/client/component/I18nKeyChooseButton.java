package club.pineclone.gtavops.client.component;

import club.pineclone.gtavops.client.forked.I18nKeyChooser;
import club.pineclone.gtavops.client.i18n.ExtendedI18n;
import club.pineclone.gtavops.utils.KeyUtils;
import io.vproxy.vfx.entity.input.Key;
import io.vproxy.vfx.ui.button.FusionButton;
import io.vproxy.vfx.util.FXUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 支持按键选择的按钮，按下按钮之后弹出弹窗等待用户按下按钮，可用于设定快捷键，仅支持单按键，
 * 支持滚轮、双侧键、鼠标按键以及键盘按键
 */
public class I18nKeyChooseButton extends FusionButton {

    private boolean nullable = false;  /* 默认不支持空值 */
    private final ObjectProperty<Key> keyProperty = new SimpleObjectProperty<>();  /* 当前按键 */
    private final I18nKeyChooser keyChooser;

    public I18nKeyChooseButton(ObjectProperty<ExtendedI18n> i18n) {
        this(i18n, I18nKeyChooser.FLAG_WITH_KEY);
    }

    public I18nKeyChooseButton(ObjectProperty<ExtendedI18n> i18n, int flags) {
        this.keyChooser = new I18nKeyChooser(i18n, flags);
        keyChooser.getStage().getStage().initModality(Modality.APPLICATION_MODAL);

        StringProperty textProperty = new SimpleStringProperty();
        textProperty.addListener((observable, oldValue, newValue) -> {
            Rectangle2D textBounds = FXUtils.calculateTextBounds(getTextNode());
            setPrefWidth(Math.max(textBounds.getWidth() + 40, 120));
        });  /* 按钮大小自适应变化 */
        setPrefHeight(40);

        textProperty.bind(Bindings.createStringBinding(() -> {
            Key key = keyProperty.get();
            if (key == null) return i18n.get().vfxComponent.keyChooser.unset;
            if (key.button != null) {  /* 鼠标键位 */
                return switch (key.button) {
                    case NONE -> null;
                    case PRIMARY -> i18n.get().vfxComponent.keyChooser.primaryMouseButton;  /* 左键 */
                    case SECONDARY -> i18n.get().vfxComponent.keyChooser.secondaryMouseButton;  /* 右键 */
                    case MIDDLE -> i18n.get().vfxComponent.keyChooser.middleMouseButton;  /* 中键 */
                    case FORWARD -> i18n.get().vfxComponent.keyChooser.forwardMouseButton;  /* 前侧键 */
                    case BACK -> i18n.get().vfxComponent.keyChooser.backwardMouseButton;  /* 后侧键 */
                };
            }

            if (key.scroll != null) {  /* 滚轮键位 */
                return switch (key.scroll.direction) {
                    case UP -> i18n.get().vfxComponent.keyChooser.mouseScrollUp;  /* 上滚轮 */
                    case DOWN -> i18n.get().vfxComponent.keyChooser.mouseScrollDown;  /* 下滚轮 */
                };
            }
            return KeyUtils.toString(key);
        }, keyProperty));

        getTextNode().textProperty().bind(textProperty);

        setOnMouseClicked(event -> {
            Optional<Key> key = keyChooser.choose();
            key.ifPresentOrElse(keyProperty::set, () -> {
                if (nullable) keyProperty.set(null);
            });
        });
    }

    public ObjectProperty<Key> keyProperty() {
        return keyProperty;
    }

    // 是否支持空值
    public void setNullable(final boolean nullable) {
        this.nullable = nullable;
    }

}
