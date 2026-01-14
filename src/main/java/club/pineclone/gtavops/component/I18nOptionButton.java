package club.pineclone.gtavops.component;

import club.pineclone.gtavops.i18n.I18nContext;
import club.pineclone.gtavops.i18n.I18nText;
import io.vproxy.vfx.ui.button.FusionButton;
import io.vproxy.vfx.util.FXUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 循环按钮，通过传入一组OptionItem，单击鼠标左键或右键进行循环式选择，左键可以切换到下一个选项，右键
 * 则可以切换到上一个选项
 */
public class I18nOptionButton<T> extends FusionButton {

    private final ObjectProperty<T> optionProperty = new SimpleObjectProperty<>();
    private final List<T> options;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public I18nOptionButton(I18nContext i18nContext, LinkedHashMap<T, I18nText> map) {
        /* 使大小跟随内容动态变化，避免内容超出按钮本身大小 */
        getTextNode().textProperty().addListener((observable, oldValue, newValue) -> {
            Rectangle2D textBounds = FXUtils.calculateTextBounds(getTextNode());
            setPrefWidth(Math.max(textBounds.getWidth() + 40, 120));
        });
        setPrefHeight(40);
        this.options = map.keySet().stream().toList();

        /* 处理鼠标点击事件，鼠标点击触发索引变化 */
        setOnMouseClicked(event -> {
            if (options.size() <= 1) return;  /* 若仅有一个选项，那么忽略点击事件 */

            int currentIndex;
            if (optionProperty.get() == null) currentIndex = 0;
            else currentIndex = options.indexOf(optionProperty.get());

            int targetIndex = 0;
            if (event.getButton().equals(MouseButton.PRIMARY)) {  /* 鼠标左键点击，切换到下一个CycleOption */
                targetIndex = currentIndex == options.size() - 1 ? 0 : currentIndex + 1;
            } else if (event.getButton().equals(MouseButton.SECONDARY)) {  /* 鼠标右键点击，切换到上一个CycleOption */
                targetIndex = currentIndex == 0 ? options.size() - 1 : currentIndex - 1;
            }

            optionProperty.set(options.get(targetIndex));
        });

        /* 选项变化处理逻辑，索引变化触发按钮文本以及所持有的CycleOption变化 */
        getTextNode().textProperty().bind(Bindings.createStringBinding(() -> {
            T opt = optionProperty.get();
//            log.debug("Receive new option: {}", opt);
            /* 由于双向绑定的触发，会导致空值注入，此处需要进行非空判断来避免抛出NPE */
            if (opt == null) return I18nText.of(i -> i.vfxComponent.unset).resolve(i18nContext);
            return map.get(opt).resolve(i18nContext);
        }, optionProperty));
    }

    public ObjectProperty<T> optionProperty() {
        return optionProperty;
    }
}
