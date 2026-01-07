package club.pineclone.gtavops.client.component;

import club.pineclone.gtavops.client.i18n.ExtendedI18n;
import io.vproxy.vfx.ui.button.FusionButton;
import io.vproxy.vfx.util.FXUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseButton;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * 循环按钮，通过传入一组OptionItem，单击鼠标左键或右键进行循环式选择，左键可以切换到下一个选项，右键
 * 则可以切换到上一个选项
 */
public class VOptionButton<T> extends FusionButton {

    private final ObjectProperty<UnstableOptionItem<T>> optionProperty = new SimpleObjectProperty<>();
    private final Map<T, UnstableOptionItem<T>> itemMap = new HashMap<>();

    public VOptionButton(List<UnstableOptionItem<T>> options) {
        /* 使大小跟随内容动态变化，避免内容超出按钮本身大小 */
        getTextNode().textProperty().addListener((observable, oldValue, newValue) -> {
            Rectangle2D textBounds = FXUtils.calculateTextBounds(getTextNode());
            setPrefWidth(Math.max(textBounds.getWidth() + 40, 120));
        });
        setPrefHeight(40);

        options.forEach(item -> itemMap.put(item.getOption(), item));

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
        optionProperty.addListener(
                (obs, oldVal, newVal) ->
                setText(newVal == null ? "" : newVal.textProperty().get()));

        optionProperty.set(options.get(0));  /* 将选项至于0号位 */
    }

    /**
     * 这个方法应该仅被用于绑定监听器，避免使用这个方法来对optionProperty字段设置或获取值，
     * 后者应该使用setOption和getOption来完成
     */
    public ObjectProperty<UnstableOptionItem<T>> optionProperty() {
        return optionProperty;
    }

    public void setOption(T t) {
        optionProperty.set(itemMap.get(t));
    }

    public T getOption() {
        return optionProperty.get().getOption();
    }

    public static class UnstableOptionItem<T> {
        protected final StringProperty textProperty;
        @Getter private final T option;

        public UnstableOptionItem(T option) {
            this.textProperty = new SimpleStringProperty();
            this.option = option;
        }

        public UnstableOptionItem(T option, String defaultText) {
            this.textProperty = new SimpleStringProperty(defaultText);
            this.option = option;
        }

        public StringProperty textProperty() {
            return textProperty;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            UnstableOptionItem<?> that = (UnstableOptionItem<?>) o;
            return Objects.equals(option, that.option);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(option);
        }
    }

    public static class I18nOptionItem<T> extends UnstableOptionItem<T> {
        public I18nOptionItem(T option, ObjectProperty<ExtendedI18n> i18n, Function<ExtendedI18n, String> textProvider) {
            super(option);
            textProperty.bind(Bindings.createStringBinding(() -> textProvider.apply(i18n.get()), i18n));
        }
    }
}
