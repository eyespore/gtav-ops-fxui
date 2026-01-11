package club.pineclone.gtavops.client.scene.macrotoggle;

import club.pineclone.gtavops.client.forked.I18nKeyChooser;
import club.pineclone.gtavops.client.i18n.I18nContext;
import club.pineclone.gtavops.client.i18n.I18nText;
import club.pineclone.gtavops.client.utils.ConfigContentBuilder;
import io.vproxy.vfx.entity.input.Key;
import javafx.beans.property.*;

import java.util.UUID;

/* 延迟攀爬 */
public class DelayClimbToggle extends MacroToggle {

    private UUID macroId;

    public DelayClimbToggle(I18nContext i18nContext) {
        super(i18nContext, I18nText.of(i -> i.delayClimb.title), DCSettingStage::new);
    }

    @Override
    protected void onFeatureEnable() {
//        macroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.DELAY_CLIMB_MACRO_CREATION_STRATEGY);
//        MacroRegistry.getInstance().launchMacro(macroId);
    }

    @Override
    protected void onFeatureDisable() {
//        MacroRegistry.getInstance().terminateMacro(macroId);
    }

    @Override
    public void onUIInit() {
//        selectedProperty().set(MacroConfigLoader.get().delayClimb.baseSetting.enable);
    }

    @Override
    public void onUIDispose() {
//        log.debug("Save config to file: {}", selectedProperty().get());
//        MacroConfigLoader.get().delayClimb.baseSetting.setEnable(selectedProperty().get());
    }

    private static class DCSettingStage extends MacroSettingStage {

//        private final MacroConfig config = getConfig();
//        private final MacroConfig.DelayClimb dcConfig = config.delayClimb;

        private static final int FLAG_WITH_KEY_AND_MOUSE = I18nKeyChooser.FLAG_WITH_KEY  | I18nKeyChooser.FLAG_WITH_MOUSE;
        private final ObjectProperty<Key> usePhoneKey = new SimpleObjectProperty<>();
        private final ObjectProperty<Key> hideInCoverKey = new SimpleObjectProperty<>();
        private final ObjectProperty<Key> toggleDelayClimbKey = new SimpleObjectProperty<>();
        private final DoubleProperty triggerInterval = new SimpleDoubleProperty();
        private final DoubleProperty timeUtilCameraExited = new SimpleDoubleProperty();
        private final DoubleProperty timeUtilCameraLoaded1 = new SimpleDoubleProperty();
        private final DoubleProperty timeUtilCameraLoaded2 = new SimpleDoubleProperty();
        private final BooleanProperty hideInCoverOnExit = new SimpleBooleanProperty();  /* 是否在结束时尝试躲入掩体 */

        public DCSettingStage(I18nContext i18nContext, I18nText introTitle) {
            super(i18nContext, introTitle);
            this.content = ConfigContentBuilder.defaultLine(i18nContext)
                    .dividerRow(i -> i.delayClimb.baseSetting.title)
                    .keyChooseButtonRow(i -> i.delayClimb.baseSetting.usePhoneKey, FLAG_WITH_KEY_AND_MOUSE, usePhoneKey)
                    .keyChooseButtonRow(i -> i.delayClimb.baseSetting.hideInCoverKey, hideInCoverKey)
                    .keyChooseButtonRow(i -> i.delayClimb.baseSetting.toggleDelayClimbKey, FLAG_WITH_KEY_AND_MOUSE, toggleDelayClimbKey)
                    .sliderRow(i -> i.delayClimb.baseSetting.triggerInterval, 300, 1000, 4000, triggerInterval)
                    .sliderRow(i -> i.delayClimb.baseSetting.timeUtilCameraExited, 300, 200, 2000, timeUtilCameraExited)
                    .sliderRow(i -> i.delayClimb.baseSetting.timeUtilCameraLoaded1, 300, 1000, 4000, timeUtilCameraLoaded1)
                    .sliderRow(i -> i.delayClimb.baseSetting.timeUtilCameraLoaded2, 300, 500, 3000, timeUtilCameraLoaded2)
                    .toggleButtonRow(i -> i.delayClimb.baseSetting.hideInCoverOnExit, hideInCoverOnExit)
                    .build();
        }

        @Override
        public void onVSettingStageInit() {
//            toggleDelayClimbKey.set(dcConfig.baseSetting.toggleDelayClimbKey);
//            usePhoneKey.set(dcConfig.baseSetting.usePhoneKey);
//            hideInCoverKey.set(dcConfig.baseSetting.hideInCoverKey);
//            triggerInterval.set(dcConfig.baseSetting.triggerInterval);
//            timeUtilCameraExited.set(dcConfig.baseSetting.timeUtilCameraExited);
//            timeUtilCameraLoaded1.set(dcConfig.baseSetting.timeUtilCameraLoaded1);
//            timeUtilCameraLoaded2.set(dcConfig.baseSetting.timeUtilCameraLoaded2);
//            hideInCoverOnExit.set(dcConfig.baseSetting.hideInCoverOnExit);
        }

        @Override
        public void onVSettingStageExit() {
//            dcConfig.baseSetting.toggleDelayClimbKey = toggleDelayClimbKey.get();
//            dcConfig.baseSetting.usePhoneKey = usePhoneKey.get();
//            dcConfig.baseSetting.hideInCoverKey = hideInCoverKey.get();
//            dcConfig.baseSetting.triggerInterval = triggerInterval.get();
//            dcConfig.baseSetting.timeUtilCameraExited = timeUtilCameraExited.get();
//            dcConfig.baseSetting.timeUtilCameraLoaded1 = timeUtilCameraLoaded1.get();
//            dcConfig.baseSetting.timeUtilCameraLoaded2 = timeUtilCameraLoaded2.get();
//            dcConfig.baseSetting.hideInCoverOnExit = hideInCoverOnExit.get();
        }
    }
}
