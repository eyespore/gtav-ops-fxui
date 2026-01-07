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
import javafx.beans.property.ObjectProperty;

import java.util.UUID;

/* 连发RPG */
public class AutoFireFeatureToggle extends MacroToggle {

    private UUID macroId;  /* 宏ID */

    public AutoFireFeatureToggle(ObjectProperty<ExtendedI18n> i18n) {
        super(i18n, i -> i.autoFire.title, AutoFireSettingStage::new);
    }

    @Override
    protected void onFeatureEnable() {
        macroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.AUTO_FIRE_MACRO_CREATION_STRATEGY);
        MacroRegistry.getInstance().launchMacro(macroId);
    }

    @Override
    protected void onFeatureDisable() {
        MacroRegistry.getInstance().terminateMacro(macroId);
    }

    @Override
    public void onUIInit() {
        selectedProperty().set(MacroConfigLoader.get().autoFire.baseSetting.enable);
    }

    @Override
    public void onUIDispose() {
        MacroConfigLoader.get().autoFire.baseSetting.enable = selectedProperty().get();
    }

    private static class AutoFireSettingStage
            extends MacroSettingStage implements ResourceHolder {

        private final MacroConfig config = getConfig();
        private final MacroConfig.AutoFire aRpgConfig = config.autoFire;

        private static final int FLAG_WITH_KEY_AND_MOUSE = I18nKeyChooser.FLAG_WITH_KEY  | I18nKeyChooser.FLAG_WITH_MOUSE;

        /* Join A New Session */
        private final I18nKeyChooseButton activateKeyBtn = new I18nKeyChooseButton(i18n, FLAG_WITH_KEY_AND_MOUSE);
        private final I18nKeyChooseButton heavyWeaponKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);
        private final I18nKeyChooseButton specialWeaponKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);

        private final I18nTriggerModeChooseButton activateMethodBtn = new I18nTriggerModeChooseButton(i18n, I18nTriggerModeChooseButton.FLAG_WITH_TOGGLE | I18nTriggerModeChooseButton.FLAG_WITH_HOLD);

        private final ForkedSlider triggerIntervalSlider = new ForkedSlider() {{
            setLength(300);
            setRange(200, 1200);
        }};

        private final ForkedSlider mousePressIntervalSlider = new ForkedSlider() {{
            setLength(300);
            setRange(200, 1000);
        }};

        public AutoFireSettingStage(ObjectProperty<ExtendedI18n> i18n) {
            super(i18n, i -> i.autoFire.title);
            getContent().getChildren().addAll(contentBuilder()
                    .divide(i -> i.autoFire.baseSetting.title)
                    .button(i -> i.autoFire.baseSetting.activateMethod, activateMethodBtn)
                    .button(i -> i.autoFire.baseSetting.activateKey, activateKeyBtn)
                    .button(i -> i.autoFire.baseSetting.heavyWeaponKey, heavyWeaponKeyBtn)
                    .button(i -> i.autoFire.baseSetting.specialWeaponKey, specialWeaponKeyBtn)
                    .slider(i -> i.autoFire.baseSetting.triggerInterval, triggerIntervalSlider)
                    .slider(i -> i.autoFire.baseSetting.mousePressInterval, mousePressIntervalSlider)
                    .build());
        }

        @Override
        public void onVSettingStageInit() {
            activateMethodBtn.triggerModeProperty().set(aRpgConfig.baseSetting.activateMethod);
            heavyWeaponKeyBtn.keyProperty().set(aRpgConfig.baseSetting.heavyWeaponKey);
            specialWeaponKeyBtn.keyProperty().set(aRpgConfig.baseSetting.specialWeaponKey);
            activateKeyBtn.keyProperty().set(aRpgConfig.baseSetting.activateKey);
            triggerIntervalSlider.setValue(aRpgConfig.baseSetting.triggerInterval);
            mousePressIntervalSlider.setValue(aRpgConfig.baseSetting.mousePressInterval);
        }

        @Override
        public void onVSettingStageExit() {
            aRpgConfig.baseSetting.activateMethod = activateMethodBtn.triggerModeProperty().get();
            aRpgConfig.baseSetting.activateKey = activateKeyBtn.keyProperty().get();
            aRpgConfig.baseSetting.heavyWeaponKey = heavyWeaponKeyBtn.keyProperty().get();
            aRpgConfig.baseSetting.specialWeaponKey = specialWeaponKeyBtn.keyProperty().get();
            aRpgConfig.baseSetting.triggerInterval = triggerIntervalSlider.valueProperty().get();
            aRpgConfig.baseSetting.mousePressInterval = mousePressIntervalSlider.valueProperty().get();
        }
    }
}
