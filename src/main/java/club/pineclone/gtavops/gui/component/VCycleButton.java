package club.pineclone.gtavops.gui.component;

import club.pineclone.gtavops.i18n.I18nHolder;
import io.vproxy.vfx.ui.button.FusionButton;
import io.vproxy.vfx.util.FXUtils;
import javafx.beans.property.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseButton;

import java.util.List;

/**
 * 循环按钮，通过传入一组OptionItem，单击鼠标左键或右键进行循环式选择，左键可以切换到下一个选项，右键
 * 则可以切换到上一个选项
 */
public class VCycleButton<T> extends FusionButton {

    private final ObjectProperty<T> optionProperty = new SimpleObjectProperty<>();

    public VCycleButton(List<T> options) {
        /* 使大小跟随内容动态变化，避免内容超出按钮本身大小 */
        getTextNode().textProperty().addListener((observable, oldValue, newValue) -> {
            Rectangle2D textBounds = FXUtils.calculateTextBounds(getTextNode());
            setPrefWidth(Math.max(textBounds.getWidth() + 40, 120));
        });
        setPrefHeight(40);

        /* 处理鼠标点击事件，鼠标点击触发索引变化 */
        setOnMouseClicked(event -> {
            if (options.size() == 1) return;  /* 若仅有一个选项，那么忽略点击事件 */
            MouseButton button = event.getButton();

            int currentIndex = options.indexOf(optionProperty.get());
            int targetIndex = 0;

            if (button.equals(MouseButton.PRIMARY)) {  /* 鼠标左键点击，切换到下一个CycleOption */
                targetIndex = currentIndex == options.size() - 1 ? 0 : currentIndex + 1;

            } else if (button.equals(MouseButton.SECONDARY)) {  /* 鼠标右键点击，切换到上一个CycleOption */
                targetIndex = currentIndex == 0 ? options.size() - 1 : currentIndex - 1;
            }

            optionProperty.set(options.get(targetIndex));
        });

        /* 选项变化处理逻辑，索引变化触发按钮文本以及所持有的CycleOption变化 */
        optionProperty.addListener((obs, oldVal, newVal) -> {
            getTextNode().textProperty().set(newVal.toString());
        });
    }

    /* 未来可能会需要动态添加Option? 暂且保留该方法 */
    @Deprecated
    public void addOptionalItem(String text) {}

    public ObjectProperty<T> itemProperty() {
        return optionProperty;
    }
}
