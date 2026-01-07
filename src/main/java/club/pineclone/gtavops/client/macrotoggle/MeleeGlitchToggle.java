package club.pineclone.gtavops.client.macrotoggle;

import club.pineclone.gtavops.common.ResourceHolder;
import club.pineclone.gtavops.config.MacroConfig;
import club.pineclone.gtavops.config.MacroConfigLoader;
import club.pineclone.gtavops.client.component.I18nTriggerModeChooseButton;
import club.pineclone.gtavops.client.component.I18nKeyChooseButton;
import club.pineclone.gtavops.client.forked.I18nKeyChooser;
import club.pineclone.gtavops.client.forked.ForkedSlider;
import club.pineclone.gtavops.client.i18n.ExtendedI18n;
import club.pineclone.gtavops.macro.MacroCreationStrategies;
import club.pineclone.gtavops.macro.MacroRegistry;
import io.vproxy.vfx.ui.toggle.ToggleSwitch;
import javafx.beans.property.ObjectProperty;

import java.util.UUID;

/* 近战偷速 */
public class MeleeGlitchToggle extends MacroToggle {

    private UUID macroId;

    public MeleeGlitchToggle(ObjectProperty<ExtendedI18n> i18n) {
        super(i18n, i -> i.meleeGlitch.title, MGSettingStage::new);
    }

    @Override
    protected void onFeatureEnable() {
        macroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.MELEE_GLITCH_MACRO_CREATION_STRATEGY);
        MacroRegistry.getInstance().launchMacro(macroId);
    }

    @Override
    protected void onFeatureDisable() {
        MacroRegistry.getInstance().terminateMacro(macroId);
    }

    @Override
    public void onUIInit() {
        selectedProperty().set(MacroConfigLoader.get().meleeGlitch.baseSetting.enable);
    }

    @Override
    public void onUIDispose() {
        MacroConfigLoader.get().meleeGlitch.baseSetting.enable = selectedProperty().get();
    }

    private static class MGSettingStage extends MacroSettingStage implements ResourceHolder {

        private final MacroConfig config = getConfig();
        private final MacroConfig.MeleeGlitch mgConfig = config.meleeGlitch;

        private static final int FLAG_WITH_KEY_AND_MOUSE = I18nKeyChooser.FLAG_WITH_KEY | I18nKeyChooser.FLAG_WITH_MOUSE;
        private static final int FLAG_WITH_ALL = FLAG_WITH_KEY_AND_MOUSE | I18nKeyChooser.FLAG_WITH_WHEEL_SCROLL;

        private final I18nTriggerModeChooseButton activateMethodBtn = new I18nTriggerModeChooseButton(i18n,
                I18nTriggerModeChooseButton.FLAG_WITH_HOLD | I18nTriggerModeChooseButton.FLAG_WITH_TOGGLE);
        private final I18nKeyChooseButton activateKeyBtn = new I18nKeyChooseButton(i18n, FLAG_WITH_ALL);
        private final I18nKeyChooseButton meleeSnakeScrollKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_WHEEL_SCROLL);
        private final ForkedSlider triggerIntervalSlider = new ForkedSlider() {{
            setLength(200);
            setRange(10, 50);
        }};

        private final ToggleSwitch enableSafetyKeyToggle = new ToggleSwitch();
        private final I18nKeyChooseButton safetyKeyBtn = new I18nKeyChooseButton(i18n, FLAG_WITH_ALL);

        public MGSettingStage(ObjectProperty<ExtendedI18n> i18n) {
            super(i18n, i -> i.meleeGlitch.title);
            getContent().getChildren().addAll(contentBuilder()
                    .divide(i -> i.meleeGlitch.baseSetting.title)
                    .button(i -> i.meleeGlitch.baseSetting.activateMethod, activateMethodBtn)
                    .button(i -> i.meleeGlitch.baseSetting.activateKey, activateKeyBtn)
                    .slider(i -> i.meleeGlitch.baseSetting.triggerInterval, triggerIntervalSlider)
                    .button(i -> i.meleeGlitch.baseSetting.meleeSnakeScrollKey, meleeSnakeScrollKeyBtn)
                    .toggle(i -> i.meleeGlitch.baseSetting.enableSafetyKey, enableSafetyKeyToggle)
                    .button(i -> i.meleeGlitch.baseSetting.safetyKey, safetyKeyBtn)
                    .build());
        }


        @Override
        public void onVSettingStageInit() {
            activateMethodBtn.triggerModeProperty().set(mgConfig.baseSetting.activateMethod);
            activateKeyBtn.keyProperty().set(mgConfig.baseSetting.activatekey);
            meleeSnakeScrollKeyBtn.keyProperty().set(mgConfig.baseSetting.meleeSnakeScrollKey);
            triggerIntervalSlider.setValue(mgConfig.baseSetting.triggerInterval);

            enableSafetyKeyToggle.selectedProperty().set(mgConfig.baseSetting.enableSafetyKey);
            safetyKeyBtn.keyProperty().set(mgConfig.baseSetting.safetyKey);
        }

        @Override
        public void onVSettingStageExit() {
            mgConfig.baseSetting.activateMethod = activateMethodBtn.triggerModeProperty().get();
            mgConfig.baseSetting.activatekey = activateKeyBtn.keyProperty().get();
            mgConfig.baseSetting.triggerInterval = triggerIntervalSlider.valueProperty().get();
            mgConfig.baseSetting.meleeSnakeScrollKey = meleeSnakeScrollKeyBtn.keyProperty().get();

            mgConfig.baseSetting.enableSafetyKey = enableSafetyKeyToggle.selectedProperty().get();
            mgConfig.baseSetting.safetyKey = safetyKeyBtn.keyProperty().get();
        }
    }
}
