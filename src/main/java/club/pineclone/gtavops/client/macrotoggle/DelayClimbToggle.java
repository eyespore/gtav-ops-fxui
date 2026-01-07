package club.pineclone.gtavops.client.macrotoggle;

import club.pineclone.gtavops.common.ResourceHolder;
import club.pineclone.gtavops.config.MacroConfig;
import club.pineclone.gtavops.config.MacroConfigLoader;
import club.pineclone.gtavops.client.component.I18nKeyChooseButton;
import club.pineclone.gtavops.client.forked.I18nKeyChooser;
import club.pineclone.gtavops.client.forked.ForkedSlider;
import club.pineclone.gtavops.client.i18n.ExtendedI18n;
import club.pineclone.gtavops.macro.MacroCreationStrategies;
import club.pineclone.gtavops.macro.MacroRegistry;
import io.vproxy.vfx.ui.toggle.ToggleSwitch;
import javafx.beans.property.ObjectProperty;

import java.util.UUID;

/* 延迟攀爬 */
public class DelayClimbToggle extends MacroToggle {

    private UUID macroId;

    public DelayClimbToggle(ObjectProperty<ExtendedI18n> i18n) {
        super(i18n, i -> i.delayClimb.title, DCSettingStage::new);
    }

    @Override
    protected void onFeatureEnable() {
        macroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.DELAY_CLIMB_MACRO_CREATION_STRATEGY);
        MacroRegistry.getInstance().launchMacro(macroId);
    }

    @Override
    protected void onFeatureDisable() {
        MacroRegistry.getInstance().terminateMacro(macroId);
    }

    @Override
    public void onUIInit() {
        selectedProperty().set(MacroConfigLoader.get().delayClimb.baseSetting.enable);
    }

    @Override
    public void onUIDispose() {
        log.debug("Save config to file: {}", selectedProperty().get());
        MacroConfigLoader.get().delayClimb.baseSetting.setEnable(selectedProperty().get());
    }

    private static class DCSettingStage
            extends MacroSettingStage
            implements ResourceHolder {

        private final MacroConfig config = getConfig();
        private final MacroConfig.DelayClimb dcConfig = config.delayClimb;

        private static final int FLAG_WITH_KEY_AND_MOUSE = I18nKeyChooser.FLAG_WITH_KEY  | I18nKeyChooser.FLAG_WITH_MOUSE;

        private final I18nKeyChooseButton toggleDelayClimbKey = new I18nKeyChooseButton(i18n, FLAG_WITH_KEY_AND_MOUSE);
        private final I18nKeyChooseButton usePhoneKey = new I18nKeyChooseButton(i18n, FLAG_WITH_KEY_AND_MOUSE);
        private final I18nKeyChooseButton hideInCoverKey = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);

        private final ToggleSwitch hideInCoverOnExitToggle = new ToggleSwitch();  /* 是否在结束时尝试躲入掩体 */

        private final ForkedSlider timeUtilCameraExitedSlider = new ForkedSlider() {{
            setLength(300);
            setRange(200, 2000);
        }};

        private final ForkedSlider timeUtilCameraLoaded1Slider = new ForkedSlider() {{
            setLength(300);
            setRange(1000, 4000);
        }};

        private final ForkedSlider timeUtilCameraLoaded2Slider = new ForkedSlider() {{
            setLength(300);
            setRange(500, 3000);
        }};

        private final ForkedSlider triggerIntervalSlider = new ForkedSlider() {{
            setLength(300);
            setRange(1000, 4000);
        }};

        public DCSettingStage(ObjectProperty<ExtendedI18n> i18n) {
            super(i18n, i -> i.delayClimb.title);
            getContent().getChildren().addAll(contentBuilder()
                    .divide(i -> i.delayClimb.baseSetting.title)
                    .button(i -> i.delayClimb.baseSetting.usePhoneKey, usePhoneKey)
                    .button(i -> i.delayClimb.baseSetting.hideInCoverKey, hideInCoverKey)
                    .button(i -> i.delayClimb.baseSetting.toggleDelayClimbKey, toggleDelayClimbKey)
                    .slider(i -> i.delayClimb.baseSetting.triggerInterval, triggerIntervalSlider)
                    .slider(i -> i.delayClimb.baseSetting.timeUtilCameraExited, timeUtilCameraExitedSlider)
                    .slider(i -> i.delayClimb.baseSetting.timeUtilCameraLoaded1, timeUtilCameraLoaded1Slider)
                    .slider(i -> i.delayClimb.baseSetting.timeUtilCameraLoaded2, timeUtilCameraLoaded2Slider)
                    .toggle(i -> i.delayClimb.baseSetting.hideInCoverOnExit, hideInCoverOnExitToggle)
                    .build());
        }

        @Override
        public void onVSettingStageInit() {
            toggleDelayClimbKey.keyProperty().set(dcConfig.baseSetting.toggleDelayClimbKey);
            usePhoneKey.keyProperty().set(dcConfig.baseSetting.usePhoneKey);
            hideInCoverKey.keyProperty().set(dcConfig.baseSetting.hideInCoverKey);
            triggerIntervalSlider.setValue(dcConfig.baseSetting.triggerInterval);
            timeUtilCameraExitedSlider.setValue(dcConfig.baseSetting.timeUtilCameraExited);
            timeUtilCameraLoaded1Slider.setValue(dcConfig.baseSetting.timeUtilCameraLoaded1);
            timeUtilCameraLoaded2Slider.setValue(dcConfig.baseSetting.timeUtilCameraLoaded2);
            hideInCoverOnExitToggle.selectedProperty().set(dcConfig.baseSetting.hideInCoverOnExit);
        }

        @Override
        public void onVSettingStageExit() {
            dcConfig.baseSetting.toggleDelayClimbKey = toggleDelayClimbKey.keyProperty().get();
            dcConfig.baseSetting.usePhoneKey = usePhoneKey.keyProperty().get();
            dcConfig.baseSetting.hideInCoverKey = hideInCoverKey.keyProperty().get();
            dcConfig.baseSetting.triggerInterval = triggerIntervalSlider.valueProperty().get();
            dcConfig.baseSetting.timeUtilCameraExited = timeUtilCameraExitedSlider.valueProperty().get();
            dcConfig.baseSetting.timeUtilCameraLoaded1 = timeUtilCameraLoaded1Slider.valueProperty().get();
            dcConfig.baseSetting.timeUtilCameraLoaded2 = timeUtilCameraLoaded2Slider.valueProperty().get();
            dcConfig.baseSetting.hideInCoverOnExit = hideInCoverOnExitToggle.selectedProperty().get();
        }
    }
}
