package club.pineclone.gtavops.gui.feature;

import club.pineclone.gtavops.common.ResourceHolder;
import club.pineclone.gtavops.config.Config;
import club.pineclone.gtavops.gui.component.VTriggerModeChooseButton;
import club.pineclone.gtavops.gui.component.VKeyChooseButton;
import club.pineclone.gtavops.gui.component.VSettingStage;
import club.pineclone.gtavops.gui.forked.ForkedKeyChooser;
import club.pineclone.gtavops.gui.forked.ForkedSlider;
import club.pineclone.gtavops.i18n.ExtendedI18n;
import club.pineclone.gtavops.macro.MacroContextHolder;
import club.pineclone.gtavops.macro.action.Action;
import club.pineclone.gtavops.macro.action.impl.betterlbutton.HoldLButtonAction;
import club.pineclone.gtavops.macro.action.impl.betterlbutton.RapidlyClickLButtonAction;
import club.pineclone.gtavops.macro.action.impl.betterlbutton.RemapLButtonAction;
import club.pineclone.gtavops.macro.trigger.Trigger;
import club.pineclone.gtavops.macro.trigger.TriggerFactory;
import club.pineclone.gtavops.macro.trigger.TriggerIdentity;
import club.pineclone.gtavops.macro.trigger.TriggerMode;
import io.vproxy.vfx.entity.input.Key;
import io.vproxy.vfx.ui.toggle.ToggleSwitch;

import java.util.UUID;

public class _06BetterLButtonFeatureTogglePane
        extends FeatureTogglePaneTemplate
        implements ResourceHolder {

    public _06BetterLButtonFeatureTogglePane() {
        super(new BLBFeatureContext(), new BLBSettingStage());
    }

    @Override
    protected String getTitle() {
        return getI18n().betterLButton.title;
    }

    @Override
    public boolean init() {
        return getConfig().betterLButton.baseSetting.enable;
    }

    @Override
    public void stop(boolean enabled) {
        getConfig().betterLButton.baseSetting.enable = enabled;
    }

    private static class BLBFeatureContext
            extends FeatureContext
            implements ResourceHolder, MacroContextHolder {

        private final Config config = getConfig();
        private final Config.BetterLButton blbConfig = config.betterLButton;

        private UUID holdLButtonMacroId;
        private UUID rapidlyClickLButtonMacroId;
        private UUID remapLButtonMacroId;

        @Override
        protected void activate() {
            /* 辅助按住鼠标左键 */
            if (blbConfig.holdLButtonSetting.enable) {
                TriggerMode mode = blbConfig.holdLButtonSetting.activateMethod;
                Key activateKey = blbConfig.holdLButtonSetting.activateKey;
                Trigger trigger = TriggerFactory.simple(TriggerIdentity.of(mode, activateKey));

                Action action = new HoldLButtonAction();
                holdLButtonMacroId = MACRO_FACTORY.createSimpleMacro(trigger, action);
                MACRO_REGISTRY.install(holdLButtonMacroId);
            }

            /* 辅助点按鼠标左键 */
            if (blbConfig.rapidlyClickLButtonSetting.enable) {
                TriggerMode mode = blbConfig.rapidlyClickLButtonSetting.activateMethod;
                Key activateKey = blbConfig.rapidlyClickLButtonSetting.activateKey;
                long triggerInterval = (long) (Math.floor(blbConfig.rapidlyClickLButtonSetting.triggerInterval));

                Trigger trigger = TriggerFactory.simple(TriggerIdentity.of(mode, activateKey));
                Action action = new RapidlyClickLButtonAction(triggerInterval);

                rapidlyClickLButtonMacroId = MACRO_FACTORY.createSimpleMacro(trigger, action);
                MACRO_REGISTRY.install(rapidlyClickLButtonMacroId);
            }

            /* 鼠标左键重映射 */
            if (blbConfig.remapLButtonSetting.enable) {
                Key activateKey = blbConfig.remapLButtonSetting.activateKey;
                Action action = new RemapLButtonAction();
                Trigger trigger = TriggerFactory.simple(TriggerIdentity.of(TriggerMode.HOLD, activateKey));

                remapLButtonMacroId = MACRO_FACTORY.createSimpleMacro(trigger, action);
                MACRO_REGISTRY.install(remapLButtonMacroId);
            }
        }

        @Override
        protected void deactivate() {
            MACRO_REGISTRY.uninstall(holdLButtonMacroId);
            MACRO_REGISTRY.uninstall(rapidlyClickLButtonMacroId);
            MACRO_REGISTRY.uninstall(remapLButtonMacroId);
        }
    }

    private static class BLBSettingStage
            extends VSettingStage
            implements ResourceHolder {

        private final ExtendedI18n i18n = getI18n();
        private final ExtendedI18n.BetterLButton blbI18n = i18n.betterLButton;

        private final Config config = getConfig();
        private final Config.BetterLButton blbConfig = config.betterLButton;

        private static final int FLAG_WITH_KEY_AND_MOUSE = ForkedKeyChooser.FLAG_WITH_KEY  | ForkedKeyChooser.FLAG_WITH_MOUSE;
        private static final int FLAG_WITH_ALL = FLAG_WITH_KEY_AND_MOUSE | ForkedKeyChooser.FLAG_WITH_WHEEL_SCROLL;

        private final ToggleSwitch enableHoldLButtonToggle = new ToggleSwitch();
        private final VKeyChooseButton holdLButtonActivateKeyBtn = new VKeyChooseButton(FLAG_WITH_ALL);
        private final VTriggerModeChooseButton holdLButtonActivateMethodBtn = new VTriggerModeChooseButton(
                VTriggerModeChooseButton.FLAG_WITH_HOLD | VTriggerModeChooseButton.FLAG_WITH_TOGGLE);

        private final ToggleSwitch enableRapidlyClickLButtonToggle = new ToggleSwitch();
        private final VKeyChooseButton rapidlyClickLButtonActivateKeyBtn = new VKeyChooseButton(FLAG_WITH_ALL);
        private final VTriggerModeChooseButton rapidlyClickLButtonActivateMethodBtn = new VTriggerModeChooseButton(
                VTriggerModeChooseButton.FLAG_WITH_HOLD | VTriggerModeChooseButton.FLAG_WITH_TOGGLE);
        private final ForkedSlider rapidlyClickLButtonTriggerInterval = new ForkedSlider() {{
            setLength(200);
            setRange(10, 100);
        }};

        private final ToggleSwitch enableRemapLButtonToggle = new ToggleSwitch();
        public final VKeyChooseButton remapLButtonActivateKeyBtn = new VKeyChooseButton();

        public BLBSettingStage() {
            getContent().getChildren().addAll(contentBuilder()
                    .divide(blbI18n.holdLButtonSetting.title)
                    .toggle(blbI18n.holdLButtonSetting.enable, enableHoldLButtonToggle)
                    .button(blbI18n.holdLButtonSetting.activateMethod, holdLButtonActivateMethodBtn)
                    .button(blbI18n.holdLButtonSetting.activateKey, holdLButtonActivateKeyBtn)
                    .divide(blbI18n.rapidlyClickLButtonSetting.title)
                    .toggle(blbI18n.rapidlyClickLButtonSetting.enable, enableRapidlyClickLButtonToggle)
                    .button(blbI18n.rapidlyClickLButtonSetting.activateMethod, rapidlyClickLButtonActivateMethodBtn)
                    .button(blbI18n.rapidlyClickLButtonSetting.activateKey, rapidlyClickLButtonActivateKeyBtn)
                    .slider(blbI18n.rapidlyClickLButtonSetting.triggerInterval, rapidlyClickLButtonTriggerInterval)
                    .divide(blbI18n.remapLButtonSetting.title)
                    .toggle(blbI18n.remapLButtonSetting.enable, enableRemapLButtonToggle)
                    .button(blbI18n.remapLButtonSetting.activateKey, remapLButtonActivateKeyBtn)
                    .build());
        }

        @Override
        public String getTitle() {
            return blbI18n.title;
        }

        @Override
        public void onVSettingStageInit() {
            enableHoldLButtonToggle.selectedProperty().set(blbConfig.holdLButtonSetting.enable);
            holdLButtonActivateMethodBtn.triggerModeProperty().set(blbConfig.holdLButtonSetting.activateMethod);
            holdLButtonActivateKeyBtn.keyProperty().set(blbConfig.holdLButtonSetting.activateKey);

            enableRapidlyClickLButtonToggle.selectedProperty().set(blbConfig.rapidlyClickLButtonSetting.enable);
            rapidlyClickLButtonActivateMethodBtn.triggerModeProperty().set(blbConfig.rapidlyClickLButtonSetting.activateMethod);
            rapidlyClickLButtonActivateKeyBtn.keyProperty().set(blbConfig.rapidlyClickLButtonSetting.activateKey);
            rapidlyClickLButtonTriggerInterval.setValue(blbConfig.rapidlyClickLButtonSetting.triggerInterval);

            enableRemapLButtonToggle.selectedProperty().set(blbConfig.remapLButtonSetting.enable);
            remapLButtonActivateKeyBtn.keyProperty().set(blbConfig.remapLButtonSetting.activateKey);
        }

        @Override
        public void onVSettingStageExit() {
            blbConfig.holdLButtonSetting.enable = enableHoldLButtonToggle.selectedProperty().get();
            blbConfig.holdLButtonSetting.activateMethod = holdLButtonActivateMethodBtn.triggerModeProperty().get();
            blbConfig.holdLButtonSetting.activateKey = holdLButtonActivateKeyBtn.keyProperty().get();

            blbConfig.rapidlyClickLButtonSetting.enable = enableRapidlyClickLButtonToggle.selectedProperty().get();
            blbConfig.rapidlyClickLButtonSetting.activateMethod = rapidlyClickLButtonActivateMethodBtn.triggerModeProperty().get();
            blbConfig.rapidlyClickLButtonSetting.activateKey = rapidlyClickLButtonActivateKeyBtn.keyProperty().get();
            blbConfig.rapidlyClickLButtonSetting.triggerInterval = rapidlyClickLButtonTriggerInterval.valueProperty().get();

            blbConfig.remapLButtonSetting.enable = enableRemapLButtonToggle.selectedProperty().get();
            blbConfig.remapLButtonSetting.activateKey = remapLButtonActivateKeyBtn.keyProperty().get();
        }
    }
}
