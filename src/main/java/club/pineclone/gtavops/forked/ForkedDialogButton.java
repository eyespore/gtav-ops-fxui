package club.pineclone.gtavops.forked;

import club.pineclone.gtavops.i18n.I18nContext;
import club.pineclone.gtavops.i18n.I18nText;
import io.vproxy.vfx.ui.button.FusionButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

import java.util.function.Supplier;

public class ForkedDialogButton<T> {
    private final StringProperty textProperty;
    public final Supplier<T> provider;
    @Getter FusionButton button;

    public ForkedDialogButton(T value) {
        this("", () -> value);
    }

    public ForkedDialogButton(String defaultText, T value) {
        this(defaultText, () -> value);
    }

    public ForkedDialogButton(String defaultText, Supplier<T> provider) {
        this.textProperty = new SimpleStringProperty(defaultText);
        this.provider = provider;
    }

    public StringProperty textProperty() {
        return textProperty;
    }

    public static class I18nDialogButton<T> extends ForkedDialogButton<T> {

        public I18nDialogButton(T value, I18nContext i18nContext, I18nText buttonText) {
            super(value);
            textProperty().bind(buttonText.binding(i18nContext));
        }
    }
}