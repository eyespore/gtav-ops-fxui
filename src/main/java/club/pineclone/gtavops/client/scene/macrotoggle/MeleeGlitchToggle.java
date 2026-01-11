package club.pineclone.gtavops.client.scene.macrotoggle;

import club.pineclone.gtavops.client.forked.I18nKeyChooser;
import club.pineclone.gtavops.client.i18n.I18nContext;
import club.pineclone.gtavops.client.i18n.I18nText;
import club.pineclone.gtavops.client.utils.ConfigContentBuilder;
import club.pineclone.gtavops.common.TriggerMode;
import io.vproxy.vfx.entity.input.Key;
import javafx.beans.property.*;

import java.util.UUID;

import static club.pineclone.gtavops.client.component.I18nTriggerModeChooseButton.FLAG_WITH_HOLD;
import static club.pineclone.gtavops.client.component.I18nTriggerModeChooseButton.FLAG_WITH_TOGGLE;

/* 近战偷速 */
public class MeleeGlitchToggle extends MacroToggle {

    private UUID macroId;

    public MeleeGlitchToggle(I18nContext i18nContext) {
        super(i18nContext, I18nText.of(i -> i.meleeGlitch.title), MGSettingStage::new);
    }

    @Override
    protected void onFeatureEnable() {
//        macroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.MELEE_GLITCH_MACRO_CREATION_STRATEGY);
//        MacroRegistry.getInstance().launchMacro(macroId);
    }

    @Override
    protected void onFeatureDisable() {
//        MacroRegistry.getInstance().terminateMacro(macroId);
    }

    @Override
    public void onUIInit() {
//        selectedProperty().set(MacroConfigLoader.get().meleeGlitch.baseSetting.enable);
    }

    @Override
    public void onUIDispose() {
//        MacroConfigLoader.get().meleeGlitch.baseSetting.enable = selectedProperty().get();
    }

    private static class MGSettingStage extends MacroSettingStage {

//        private final MacroConfig config = getConfig();
//        private final MacroConfig.MeleeGlitch mgConfig = config.meleeGlitch;

        private static final int FLAG_WITH_KEY_AND_MOUSE = I18nKeyChooser.FLAG_WITH_KEY | I18nKeyChooser.FLAG_WITH_MOUSE;
        private static final int FLAG_WITH_ALL = FLAG_WITH_KEY_AND_MOUSE | I18nKeyChooser.FLAG_WITH_WHEEL_SCROLL;

        private final ObjectProperty<TriggerMode> activateMethodBtn = new SimpleObjectProperty<>();
        private final ObjectProperty<Key> activateKeyBtn = new SimpleObjectProperty<>();
        private final DoubleProperty triggerInterval = new SimpleDoubleProperty();
        private final ObjectProperty<Key> meleeSnakeScrollKey = new SimpleObjectProperty<>();
        private final BooleanProperty enableSafetyKey = new SimpleBooleanProperty();
        private final ObjectProperty<Key> safetyKeyBtn = new SimpleObjectProperty<>();

        public MGSettingStage(I18nContext i18nContext, I18nText introTitle) {
            super(i18nContext, introTitle);
            this.content = ConfigContentBuilder.defaultLine(i18nContext)
                    .dividerRow(i -> i.meleeGlitch.baseSetting.title)
                    .triggerModeChooseButtonRow(i -> i.meleeGlitch.baseSetting.activateMethod, FLAG_WITH_HOLD | FLAG_WITH_TOGGLE, activateMethodBtn)
                    .keyChooseButtonRow(i -> i.meleeGlitch.baseSetting.activateKey, FLAG_WITH_ALL, activateKeyBtn)
                    .sliderRow(i -> i.meleeGlitch.baseSetting.triggerInterval, 200, 10, 50, triggerInterval)
                    .keyChooseButtonRow(i -> i.meleeGlitch.baseSetting.meleeSnakeScrollKey, I18nKeyChooser.FLAG_WITH_WHEEL_SCROLL, meleeSnakeScrollKey)
                    .toggleButtonRow(i -> i.meleeGlitch.baseSetting.enableSafetyKey, enableSafetyKey)
                    .keyChooseButtonRow(i -> i.meleeGlitch.baseSetting.safetyKey, FLAG_WITH_ALL, safetyKeyBtn)
                    .build();
        }


        @Override
        public void onVSettingStageInit() {
//            activateMethodBtn.set(mgConfig.baseSetting.activateMethod);
//            activateKeyBtn.set(mgConfig.baseSetting.activatekey);
//            meleeSnakeScrollKey.set(mgConfig.baseSetting.meleeSnakeScrollKey);
//            triggerInterval.set(mgConfig.baseSetting.triggerInterval);
//            enableSafetyKey.set(mgConfig.baseSetting.enableSafetyKey);
//            safetyKeyBtn.set(mgConfig.baseSetting.safetyKey);
        }

        @Override
        public void onVSettingStageExit() {
//            mgConfig.baseSetting.activateMethod = activateMethodBtn.get();
//            mgConfig.baseSetting.activatekey = activateKeyBtn.get();
//            mgConfig.baseSetting.triggerInterval = triggerInterval.get();
//            mgConfig.baseSetting.meleeSnakeScrollKey = meleeSnakeScrollKey.get();
//            mgConfig.baseSetting.enableSafetyKey = enableSafetyKey.get();
//            mgConfig.baseSetting.safetyKey = safetyKeyBtn.get();
        }
    }
}
