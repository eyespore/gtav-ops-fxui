package club.pineclone.gtavops.client.forked;

import club.pineclone.gtavops.client.i18n.ExtendedI18n;
import io.vproxy.vfx.ui.button.FusionButton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

import java.util.function.Function;
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
        public I18nDialogButton(T value, ObjectProperty<ExtendedI18n> i18n, Function<ExtendedI18n, String> textProvider) {
            super(value);
            textProperty().bind(Bindings.createStringBinding(() -> textProvider.apply(i18n.get()), i18n));
        }
    }
}