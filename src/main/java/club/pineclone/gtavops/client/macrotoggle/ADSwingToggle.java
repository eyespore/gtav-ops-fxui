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

public class ADSwingToggle extends MacroToggle {

    private UUID macroId;

    public ADSwingToggle(ObjectProperty<ExtendedI18n> i18n) {
        super(i18n, i -> i.adSwing.title, ADWSettingStage::new);
    }

    @Override
    protected void onFeatureEnable() {
        macroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.AD_SWING_MACRO_CREATION_STRATEGY);
        MacroRegistry.getInstance().launchMacro(macroId);
    }

    @Override
    protected void onFeatureDisable() {
        MacroRegistry.getInstance().terminateMacro(macroId);
    }

    @Override
    public void onUIInit() {
        selectedProperty().set(MacroConfigLoader.get().adSwing.baseSetting.enable);
    }

    @Override
    public void onUIDispose() {
        MacroConfigLoader.get().adSwing.baseSetting.enable = selectedProperty().get();
    }

    private static class ADWSettingStage
            extends MacroSettingStage implements ResourceHolder {
        private static final int FLAG_WITH_KEY_AND_MOUSE = I18nKeyChooser.FLAG_WITH_KEY  | I18nKeyChooser.FLAG_WITH_MOUSE;
        private static final int FLAG_WITH_ALL = FLAG_WITH_KEY_AND_MOUSE | I18nKeyChooser.FLAG_WITH_WHEEL_SCROLL;

        MacroConfig config = getConfig();
        MacroConfig.ADSwing adwConfig = config.adSwing;

        private final I18nTriggerModeChooseButton activateMethodBtn = new I18nTriggerModeChooseButton(i18n,
                I18nTriggerModeChooseButton.FLAG_WITH_HOLD | I18nTriggerModeChooseButton.FLAG_WITH_TOGGLE);

        private final I18nKeyChooseButton activateKeyBtn = new I18nKeyChooseButton(i18n, FLAG_WITH_ALL);
        private final I18nKeyChooseButton moveLeftKeyBtn = new I18nKeyChooseButton(i18n);
        private final I18nKeyChooseButton moveRightKeyBtn = new I18nKeyChooseButton(i18n);
        private final ForkedSlider triggerIntervalSlider = new ForkedSlider() {{
            setLength(200);
            setRange(10, 50);
        }};

        private final ToggleSwitch enableSafetyKeyToggle = new ToggleSwitch();
        private final I18nKeyChooseButton safetyKeyBtn = new I18nKeyChooseButton(i18n, FLAG_WITH_ALL);

        public ADWSettingStage(ObjectProperty<ExtendedI18n> i18n) {
            super(i18n, i -> i.adSwing.title);
            getContent().getChildren().addAll(contentBuilder()
                    .divide(i -> i.adSwing.baseSetting.title)
                    .button(i -> i.adSwing.baseSetting.activateMethod, activateMethodBtn)
                    .button(i -> i.adSwing.baseSetting.activateKey, activateKeyBtn)
                    .slider(i -> i.adSwing.baseSetting.triggerInterval, triggerIntervalSlider)
                    .button(i -> i.adSwing.baseSetting.moveLeftKey, moveLeftKeyBtn)
                    .button(i -> i.adSwing.baseSetting.moveRightKey, moveRightKeyBtn)
                    .toggle(i -> i.adSwing.baseSetting.enableSafetyKey, enableSafetyKeyToggle)
                    .button(i -> i.adSwing.baseSetting.safetyKey, safetyKeyBtn)
                    .build());
        }

        @Override
        public void onVSettingStageInit() {
            activateMethodBtn.triggerModeProperty().set(adwConfig.baseSetting.activateMethod);
            activateKeyBtn.keyProperty().set(adwConfig.baseSetting.activatekey);
            moveLeftKeyBtn.keyProperty().set(adwConfig.baseSetting.moveLeftKey);
            moveRightKeyBtn.keyProperty().set(adwConfig.baseSetting.moveRightKey);
            triggerIntervalSlider.setValue(adwConfig.baseSetting.triggerInterval);

            enableSafetyKeyToggle.selectedProperty().set(adwConfig.baseSetting.enableSafetyKey);
            safetyKeyBtn.keyProperty().set(adwConfig.baseSetting.safetyKey);
        }

        @Override
        public void onVSettingStageExit() {
            adwConfig.baseSetting.activateMethod = activateMethodBtn.triggerModeProperty().get();
            adwConfig.baseSetting.activatekey = activateKeyBtn.keyProperty().get();
            adwConfig.baseSetting.moveLeftKey = moveLeftKeyBtn.keyProperty().get();
            adwConfig.baseSetting.moveRightKey = moveRightKeyBtn.keyProperty().get();
            adwConfig.baseSetting.triggerInterval = triggerIntervalSlider.valueProperty().get();

            adwConfig.baseSetting.enableSafetyKey = enableSafetyKeyToggle.selectedProperty().get();
            adwConfig.baseSetting.safetyKey = safetyKeyBtn.keyProperty().get();
        }
    }
}
