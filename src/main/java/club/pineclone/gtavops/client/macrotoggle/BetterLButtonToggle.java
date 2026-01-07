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

public class BetterLButtonToggle extends MacroToggle {

    private UUID holdLButtonMacroId;
    private UUID rapidlyClickLButtonMacroId;
    private UUID remapLButtonMacroId;

    public BetterLButtonToggle(ObjectProperty<ExtendedI18n> i18n) {
        super(i18n, i -> i.betterLButton.title, BLBSettingStage::new);
    }

    @Override
    protected void onFeatureEnable() {
        MacroConfig.BetterLButton blbConfig = MacroConfigLoader.get().betterLButton;
        /* 辅助按住鼠标左键 */
        if (blbConfig.holdLButtonSetting.enable) {
            holdLButtonMacroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.HOLD_LEFT_BUTTON_MACRO_CREATION_STRATEGY);
            MacroRegistry.getInstance().launchMacro(holdLButtonMacroId);
        }
        /* 辅助点按鼠标左键 */
        if (blbConfig.rapidlyClickLButtonSetting.enable) {
            rapidlyClickLButtonMacroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.RAPIDLY_CLICK_LEFT_BUTTON_MACRO_CREATION_STRATEGY);
            MacroRegistry.getInstance().launchMacro(rapidlyClickLButtonMacroId);
        }
        /* 鼠标左键重映射 */
        if (blbConfig.remapLButtonSetting.enable) {
            remapLButtonMacroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.RAPIDLY_CLICK_LEFT_BUTTON_MACRO_CREATION_STRATEGY);
            MacroRegistry.getInstance().launchMacro(remapLButtonMacroId);
        }
    }

    @Override
    protected void onFeatureDisable() {
        MacroRegistry.getInstance().terminateMacro(holdLButtonMacroId);
        MacroRegistry.getInstance().terminateMacro(rapidlyClickLButtonMacroId);
        MacroRegistry.getInstance().terminateMacro(remapLButtonMacroId);
    }

    @Override
    public void onUIInit() {
        selectedProperty().set(MacroConfigLoader.get().betterLButton.baseSetting.enable);
    }

    @Override
    public void onUIDispose() {
        MacroConfigLoader.get().betterLButton.baseSetting.enable = selectedProperty().get();
    }

    private static class BLBSettingStage extends MacroSettingStage implements ResourceHolder {

        private final MacroConfig config = getConfig();
        private final MacroConfig.BetterLButton blbConfig = config.betterLButton;

        private static final int FLAG_WITH_KEY_AND_MOUSE = I18nKeyChooser.FLAG_WITH_KEY  | I18nKeyChooser.FLAG_WITH_MOUSE;
        private static final int FLAG_WITH_ALL = FLAG_WITH_KEY_AND_MOUSE | I18nKeyChooser.FLAG_WITH_WHEEL_SCROLL;

        private final ToggleSwitch enableHoldLButtonToggle = new ToggleSwitch();
        private final I18nKeyChooseButton holdLButtonActivateKeyBtn = new I18nKeyChooseButton(i18n, FLAG_WITH_ALL);
        private final I18nTriggerModeChooseButton holdLButtonActivateMethodBtn = new I18nTriggerModeChooseButton(i18n,
                I18nTriggerModeChooseButton.FLAG_WITH_HOLD | I18nTriggerModeChooseButton.FLAG_WITH_TOGGLE);

        private final ToggleSwitch enableRapidlyClickLButtonToggle = new ToggleSwitch();
        private final I18nKeyChooseButton rapidlyClickLButtonActivateKeyBtn = new I18nKeyChooseButton(i18n, FLAG_WITH_ALL);
        private final I18nTriggerModeChooseButton rapidlyClickLButtonActivateMethodBtn = new I18nTriggerModeChooseButton(i18n,
                I18nTriggerModeChooseButton.FLAG_WITH_HOLD | I18nTriggerModeChooseButton.FLAG_WITH_TOGGLE);
        private final ForkedSlider rapidlyClickLButtonTriggerInterval = new ForkedSlider() {{
            setLength(200);
            setRange(10, 100);
        }};

        private final ToggleSwitch enableRemapLButtonToggle = new ToggleSwitch();
        public final I18nKeyChooseButton remapLButtonActivateKeyBtn = new I18nKeyChooseButton(i18n);

        public BLBSettingStage(ObjectProperty<ExtendedI18n> i18n) {
            super(i18n, i -> i.betterLButton.title);
            getContent().getChildren().addAll(contentBuilder()
                    .divide(i -> i.betterLButton.holdLButtonSetting.title)
                    .toggle(i -> i.betterLButton.holdLButtonSetting.enable, enableHoldLButtonToggle)
                    .button(i -> i.betterLButton.holdLButtonSetting.activateMethod, holdLButtonActivateMethodBtn)
                    .button(i -> i.betterLButton.holdLButtonSetting.activateKey, holdLButtonActivateKeyBtn)
                    .divide(i -> i.betterLButton.rapidlyClickLButtonSetting.title)
                    .toggle(i -> i.betterLButton.rapidlyClickLButtonSetting.enable, enableRapidlyClickLButtonToggle)
                    .button(i -> i.betterLButton.rapidlyClickLButtonSetting.activateMethod, rapidlyClickLButtonActivateMethodBtn)
                    .button(i -> i.betterLButton.rapidlyClickLButtonSetting.activateKey, rapidlyClickLButtonActivateKeyBtn)
                    .slider(i -> i.betterLButton.rapidlyClickLButtonSetting.triggerInterval, rapidlyClickLButtonTriggerInterval)
                    .divide(i -> i.betterLButton.remapLButtonSetting.title)
                    .toggle(i -> i.betterLButton.remapLButtonSetting.enable, enableRemapLButtonToggle)
                    .button(i -> i.betterLButton.remapLButtonSetting.activateKey, remapLButtonActivateKeyBtn)
                    .build());
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
