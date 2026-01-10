package club.pineclone.gtavops.client.scene.macrotoggle;

import club.pineclone.gtavops.client.forked.I18nKeyChooser;
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
import javafx.beans.property.*;

import java.util.UUID;

import static club.pineclone.gtavops.client.component.I18nTriggerModeChooseButton.FLAG_WITH_HOLD;
import static club.pineclone.gtavops.client.component.I18nTriggerModeChooseButton.FLAG_WITH_TOGGLE;

public class BetterLButtonToggle extends MacroToggle {

    private UUID holdLButtonMacroId;
    private UUID rapidlyClickLButtonMacroId;
    private UUID remapLButtonMacroId;

    public BetterLButtonToggle(I18nContext i18nContext) {
        super(i18nContext, I18nText.of(i -> i.betterLButton.title), BLBSettingStage::new);
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

        private final BooleanProperty enableHoldLeftButton = new SimpleBooleanProperty();
        private final ObjectProperty<TriggerMode> holdLeftButtonActivateMethod = new SimpleObjectProperty<>();
        private final ObjectProperty<Key> holdLeftButtonActivateKey = new SimpleObjectProperty<>();

        private final BooleanProperty enableRapidlyClickLeftButton = new SimpleBooleanProperty();
        private final ObjectProperty<TriggerMode> rapidlyClickLeftButtonActivateMethod = new SimpleObjectProperty<>();
        private final ObjectProperty<Key> rapidlyClickLeftButtonActivateKey = new SimpleObjectProperty<>();
        private final DoubleProperty rapidlyClickLeftButtonTriggerInterval = new SimpleDoubleProperty();

        private final BooleanProperty enableRemapLeftButton = new SimpleBooleanProperty();
        private final ObjectProperty<Key> remapLButtonActivateKey = new SimpleObjectProperty<>();

        public BLBSettingStage(I18nContext i18nContext, I18nText introTitle) {
            super(i18nContext, introTitle);
            this.content = ConfigContentBuilder.defaultLine(i18nContext)
                    .dividerRow(i -> i.betterLButton.holdLButtonSetting.title)
                    .toggleButtonRow(i -> i.betterLButton.holdLButtonSetting.enable, enableHoldLeftButton)
                    .triggerModeChooseButtonRow(i -> i.betterLButton.holdLButtonSetting.activateMethod, FLAG_WITH_HOLD | FLAG_WITH_TOGGLE, holdLeftButtonActivateMethod)
                    .keyChooseButtonRow(i -> i.betterLButton.holdLButtonSetting.activateKey, FLAG_WITH_ALL, holdLeftButtonActivateKey)
                    .dividerRow(i -> i.betterLButton.rapidlyClickLButtonSetting.title)
                    .toggleButtonRow(i -> i.betterLButton.rapidlyClickLButtonSetting.enable, enableRapidlyClickLeftButton)
                    .triggerModeChooseButtonRow(i -> i.betterLButton.rapidlyClickLButtonSetting.activateMethod, FLAG_WITH_HOLD | FLAG_WITH_TOGGLE, rapidlyClickLeftButtonActivateMethod)
                    .keyChooseButtonRow(i -> i.betterLButton.rapidlyClickLButtonSetting.activateKey, FLAG_WITH_ALL, rapidlyClickLeftButtonActivateKey)
                    .sliderRow(i -> i.betterLButton.rapidlyClickLButtonSetting.triggerInterval, 200, 10, 100, rapidlyClickLeftButtonTriggerInterval)
                    .dividerRow(i -> i.betterLButton.remapLButtonSetting.title)
                    .toggleButtonRow(i -> i.betterLButton.remapLButtonSetting.enable, enableRemapLeftButton)
                    .keyChooseButtonRow(i -> i.betterLButton.remapLButtonSetting.activateKey, remapLButtonActivateKey).build();
        }

        @Override
        public void onVSettingStageInit() {
            enableHoldLeftButton.set(blbConfig.holdLButtonSetting.enable);
            holdLeftButtonActivateMethod.set(blbConfig.holdLButtonSetting.activateMethod);
            holdLeftButtonActivateKey.set(blbConfig.holdLButtonSetting.activateKey);
            enableRapidlyClickLeftButton.set(blbConfig.rapidlyClickLButtonSetting.enable);
            rapidlyClickLeftButtonActivateMethod.set(blbConfig.rapidlyClickLButtonSetting.activateMethod);
            rapidlyClickLeftButtonActivateKey.set(blbConfig.rapidlyClickLButtonSetting.activateKey);
            rapidlyClickLeftButtonTriggerInterval.set(blbConfig.rapidlyClickLButtonSetting.triggerInterval);
            enableRemapLeftButton.set(blbConfig.remapLButtonSetting.enable);
            remapLButtonActivateKey.set(blbConfig.remapLButtonSetting.activateKey);
        }

        @Override
        public void onVSettingStageExit() {
            blbConfig.holdLButtonSetting.enable = enableHoldLeftButton.get();
            blbConfig.holdLButtonSetting.activateMethod = holdLeftButtonActivateMethod.get();
            blbConfig.holdLButtonSetting.activateKey = holdLeftButtonActivateKey.get();

            blbConfig.rapidlyClickLButtonSetting.enable = enableRapidlyClickLeftButton.get();
            blbConfig.rapidlyClickLButtonSetting.activateMethod = rapidlyClickLeftButtonActivateMethod.get();
            blbConfig.rapidlyClickLButtonSetting.activateKey = rapidlyClickLeftButtonActivateKey.get();
            blbConfig.rapidlyClickLButtonSetting.triggerInterval = rapidlyClickLeftButtonTriggerInterval.get();

            blbConfig.remapLButtonSetting.enable = enableRemapLeftButton.get();
            blbConfig.remapLButtonSetting.activateKey = remapLButtonActivateKey.get();
        }
    }
}
