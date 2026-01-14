package club.pineclone.gtavops.i18n;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class I18nContext {

    private final ObjectProperty<ExtendedI18n> i18nProperty;

    public I18nContext() {
        this.i18nProperty = new SimpleObjectProperty<>(ExtendedI18n.EMPTY);
    }

    public ReadOnlyObjectProperty<ExtendedI18n> i18nProperty() {
        return i18nProperty;
    }

    public ExtendedI18n get() {
        return i18nProperty.get();
    }

    public void set(ExtendedI18n next) {
        i18nProperty.set(next);
    }
}
