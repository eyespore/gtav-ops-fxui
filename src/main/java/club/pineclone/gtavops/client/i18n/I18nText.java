package club.pineclone.gtavops.client.i18n;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableValue;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.function.Function;

/**
 * 本地化文本
 */
public class I18nText {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Getter private final Function<ExtendedI18n, String> textProvider;
    private final Object[] args;  /* 本地化插值 */

   private I18nText(Function<ExtendedI18n, String> textProvider, Object... args) {
       this.textProvider = textProvider;
       this.args = args;
   }

   public static I18nText of(Function<ExtendedI18n, String> i18nPattern) {
       return new I18nText(i18nPattern);
   }

   public static I18nText of(Function<ExtendedI18n, String> i18nPattern, Object... args) {
        return new I18nText(i18nPattern, args);
   }

   public static I18nText of(String stableText) {
       return new I18nText(i -> stableText);
   }

   public StringBinding binding(I18nContext i18nContext) {
       return Bindings.createStringBinding(() -> resolve(i18nContext), i18nContext.i18nProperty());
   }

   public String resolve(I18nContext i18nContext) {
       return resolve(i18nContext.get());  /* 插值 */
   }

   public String resolve(ExtendedI18n i18n) {
       String pattern = textProvider.apply(i18n);
       if (args == null || args.length == 0) {
           return pattern;
       }
//       log.debug("Localize with arguments: {}", args);
       return MessageFormat.format(pattern, args);
   }
}
