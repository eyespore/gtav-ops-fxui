package club.pineclone.gtavops.client.scene.macrotoggle;

import club.pineclone.gtavops.client.i18n.I18nContext;
import club.pineclone.gtavops.client.i18n.I18nText;
import club.pineclone.gtavops.client.utils.ConfigContentBuilder;
import club.pineclone.gtavops.common.ResourceHolder;
import club.pineclone.gtavops.common.TriggerMode;
import club.pineclone.gtavops.config.MacroConfig;
import club.pineclone.gtavops.config.MacroConfigLoader;
import club.pineclone.gtavops.macro.MacroCreationStrategies;
import club.pineclone.gtavops.macro.MacroRegistry;
import io.vproxy.vfx.entity.input.Key;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.UUID;

import static club.pineclone.gtavops.client.component.I18nTriggerModeChooseButton.FLAG_WITH_HOLD;
import static club.pineclone.gtavops.client.component.I18nTriggerModeChooseButton.FLAG_WITH_TOGGLE;

/* 连发RPG */
public class AutoFireFeatureToggle extends MacroToggle {

    private UUID macroId;  /* 宏ID */

    public AutoFireFeatureToggle(I18nContext i18nContext) {
        super(i18nContext, I18nText.of(i -> i.autoFire.title), AutoFireSettingStage::new);
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

        /* Join A New Session */
        private final ObjectProperty<TriggerMode> activateMethod = new SimpleObjectProperty<>();
        private final ObjectProperty<Key> activateKey = new SimpleObjectProperty<>();
        private final ObjectProperty<Key> heavyWeaponKey = new SimpleObjectProperty<>();
        private final ObjectProperty<Key> specialWeaponKey = new SimpleObjectProperty<>();
        private final DoubleProperty triggerInterval = new SimpleDoubleProperty();
        private final DoubleProperty mousePressInterval = new SimpleDoubleProperty();

        public AutoFireSettingStage(I18nContext i18nContext, I18nText introTitle) {
            super(i18nContext, introTitle);
            this.content = ConfigContentBuilder.defaultLine(i18nContext)
                    .dividerRow(i -> i.autoFire.baseSetting.title)
                    .triggerModeChooseButtonRow(i -> i.autoFire.baseSetting.activateMethod, FLAG_WITH_TOGGLE | FLAG_WITH_HOLD, activateMethod)
                    .keyChooseButtonRow(i -> i.autoFire.baseSetting.activateKey, activateKey)
                    .keyChooseButtonRow(i -> i.autoFire.baseSetting.heavyWeaponKey, heavyWeaponKey)
                    .keyChooseButtonRow(i -> i.autoFire.baseSetting.specialWeaponKey, specialWeaponKey)
                    .sliderRow(i -> i.autoFire.baseSetting.triggerInterval, 300, 200, 1200, triggerInterval)
                    .sliderRow(i -> i.autoFire.baseSetting.mousePressInterval, 300, 200, 1000, mousePressInterval).build();
        }

        @Override
        public void onVSettingStageInit() {
            activateMethod.set(aRpgConfig.baseSetting.activateMethod);
            activateKey.set(aRpgConfig.baseSetting.activateKey);
            heavyWeaponKey.set(aRpgConfig.baseSetting.heavyWeaponKey);
            specialWeaponKey.set(aRpgConfig.baseSetting.specialWeaponKey);
            triggerInterval.set(aRpgConfig.baseSetting.triggerInterval);
            mousePressInterval.set(aRpgConfig.baseSetting.mousePressInterval);
        }

        @Override
        public void onVSettingStageExit() {
            aRpgConfig.baseSetting.activateMethod = activateMethod.get();
            aRpgConfig.baseSetting.activateKey = activateKey.get();
            aRpgConfig.baseSetting.heavyWeaponKey = heavyWeaponKey.get();
            aRpgConfig.baseSetting.specialWeaponKey = specialWeaponKey.get();
            aRpgConfig.baseSetting.triggerInterval = triggerInterval.get();
            aRpgConfig.baseSetting.mousePressInterval = mousePressInterval.get();
        }
    }
}
