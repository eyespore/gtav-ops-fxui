package club.pineclone.gtavops.scene.macrotoggle;

import club.pineclone.gtavops.forked.I18nKeyChooser;
import club.pineclone.gtavops.i18n.I18nContext;
import club.pineclone.gtavops.i18n.I18nText;
import club.pineclone.gtavops.utils.ConfigContentBuilder;
import club.pineclone.gtavops.common.TriggerMode;
import io.vproxy.vfx.entity.input.Key;
import javafx.beans.property.*;

import java.util.UUID;

import static club.pineclone.gtavops.component.I18nTriggerModeChooseButton.FLAG_WITH_HOLD;
import static club.pineclone.gtavops.component.I18nTriggerModeChooseButton.FLAG_WITH_TOGGLE;

public class ADSwingToggle extends MacroToggle {

    private UUID macroId;

    public ADSwingToggle(I18nContext i18nContext) {
        super(i18nContext, I18nText.of(i -> i.adSwing.title), ADWSettingStage::new);
    }

    @Override
    protected void onFeatureEnable() {
//        macroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.AD_SWING_MACRO_CREATION_STRATEGY);
//        MacroRegistry.getInstance().launchMacro(macroId);
    }

    @Override
    protected void onFeatureDisable() {
//        MacroRegistry.getInstance().terminateMacro(macroId);
    }

    @Override
    public void onUIInit() {
//        selectedProperty().set(MacroConfigLoader.get().adSwing.baseSetting.enable);
    }

    @Override
    public void onUIDispose() {
//        MacroConfigLoader.get().adSwing.baseSetting.enable = selectedProperty().get();
    }

    private static class ADWSettingStage extends MacroSettingStage {
        private static final int FLAG_WITH_KEY_AND_MOUSE = I18nKeyChooser.FLAG_WITH_KEY  | I18nKeyChooser.FLAG_WITH_MOUSE;
        private static final int FLAG_WITH_ALL = FLAG_WITH_KEY_AND_MOUSE | I18nKeyChooser.FLAG_WITH_WHEEL_SCROLL;

//        MacroConfig configNode = getConfig();
//        MacroConfig.ADSwing adwConfig = configNode.adSwing;

        private final ObjectProperty<TriggerMode> activateMethod = new SimpleObjectProperty<>();
        private final ObjectProperty<Key> activateKey = new SimpleObjectProperty<>();
        private final DoubleProperty triggerInterval = new SimpleDoubleProperty();
        private final ObjectProperty<Key> moveLeftKey = new SimpleObjectProperty<>();
        private final ObjectProperty<Key> moveRightKey = new SimpleObjectProperty<>();
        private final BooleanProperty enableSafetyKey = new SimpleBooleanProperty();
        private final ObjectProperty<Key> safetyKey = new SimpleObjectProperty<>();

        public ADWSettingStage(I18nContext i18nContext, I18nText introTitle) {
            super(i18nContext, introTitle);
            this.content = ConfigContentBuilder.defaultLine(i18nContext)
                    .dividerRow(i -> i.adSwing.baseSetting.title)
                    .triggerModeChooseButtonRow(i -> i.adSwing.baseSetting.activateMethod, FLAG_WITH_HOLD | FLAG_WITH_TOGGLE, activateMethod)
                    .keyChooseButtonRow(i -> i.adSwing.baseSetting.activateKey, FLAG_WITH_ALL, activateKey)
                    .sliderRow(i -> i.adSwing.baseSetting.triggerInterval, 200, 10, 50, triggerInterval)
                    .keyChooseButtonRow(i -> i.adSwing.baseSetting.moveLeftKey, moveLeftKey)
                    .keyChooseButtonRow(i -> i.adSwing.baseSetting.moveRightKey, moveRightKey)
                    .toggleButtonRow(i -> i.adSwing.baseSetting.enableSafetyKey, enableSafetyKey)
                    .keyChooseButtonRow(i -> i.adSwing.baseSetting.safetyKey, safetyKey).build();
        }

        @Override
        public void onVSettingStageInit() {
//            activateMethod.set(adwConfig.baseSetting.activateMethod);
//            activateKey.set(adwConfig.baseSetting.activatekey);
//            triggerInterval.set(adwConfig.baseSetting.triggerInterval);
//            moveLeftKey.set(adwConfig.baseSetting.moveLeftKey);
//            moveRightKey.set(adwConfig.baseSetting.moveRightKey);
//            enableSafetyKey.set(adwConfig.baseSetting.enableSafetyKey);
//            safetyKey.set(adwConfig.baseSetting.safetyKey);
        }

        @Override
        public void onVSettingStageExit() {
//            adwConfig.baseSetting.activateMethod = activateMethod.get();
//            adwConfig.baseSetting.activatekey = activateKey.get();
//            adwConfig.baseSetting.triggerInterval = triggerInterval.get();
//            adwConfig.baseSetting.moveLeftKey = moveLeftKey.get();
//            adwConfig.baseSetting.moveRightKey = moveRightKey.get();
//            adwConfig.baseSetting.enableSafetyKey = enableSafetyKey.get();
//            adwConfig.baseSetting.safetyKey = safetyKey.get();
        }
    }
}
