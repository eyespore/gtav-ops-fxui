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

public class BetterMMenuToggle extends MacroToggle {

    private UUID startEngineMacroId;
    private UUID autoSnakeMacroId;

    public BetterMMenuToggle(ObjectProperty<ExtendedI18n> i18n) {
        super(i18n, i -> i.betterMMenu.title, BMMSettingStage::new);
    }

    @Override
    protected void onFeatureEnable() {
        MacroConfig.BetterMMenu bmmConfig = MacroConfigLoader.get().betterMMenu;
        /* 快速点火宏启用 */
        if (bmmConfig.startEngine.enable) {
            startEngineMacroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.START_ENGINE_MACRO_CREATION_STRATEGY);
            MacroRegistry.getInstance().launchMacro(startEngineMacroId);
        }
        /* 自动零食宏启用 */
        if (bmmConfig.autoSnake.enable) {
            autoSnakeMacroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.AUTO_SNAKE_MACRO_CREATION_STRATEGY);
            MacroRegistry.getInstance().launchMacro(autoSnakeMacroId);
        }
    }

    @Override
    protected void onFeatureDisable() {
        MacroRegistry.getInstance().terminateMacro(startEngineMacroId);
        MacroRegistry.getInstance().terminateMacro(autoSnakeMacroId);
    }

    @Override
    public void onUIInit() {
        selectedProperty().set(MacroConfigLoader.get().betterMMenu.baseSetting.enable);
    }

    @Override
    public void onUIDispose() {
        MacroConfigLoader.get().betterMMenu.baseSetting.enable = selectedProperty().get();
    }

    private static class BMMSettingStage
            extends MacroSettingStage
            implements ResourceHolder {

        private final MacroConfig config = getConfig();
        private final MacroConfig.BetterMMenu bmmConfig = config.betterMMenu;

        private static final int FLAG_WITH_KEY_AND_MOUSE = I18nKeyChooser.FLAG_WITH_KEY  | I18nKeyChooser.FLAG_WITH_MOUSE;
        private static final int FLAG_WITH_ALL = FLAG_WITH_KEY_AND_MOUSE | I18nKeyChooser.FLAG_WITH_WHEEL_SCROLL;

        private final I18nKeyChooseButton menuKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);
        private final ForkedSlider arrowKeyIntervalSlider = new ForkedSlider() {{
            setLength(200);
            setRange(10, 100);
        }};
        private final ForkedSlider enterKeyIntervalSlider = new ForkedSlider() {{
            setLength(200);
            setRange(10, 100);
        }};

        private final ForkedSlider timeUtilMMenuLoadedSlider = new ForkedSlider() {{
            setLength(250);
            setRange(10, 2000);
        }};

        /* start engine */
        private final ToggleSwitch enableStartEngineToggle = new ToggleSwitch();
        private final I18nKeyChooseButton startEngineActivateKeyBtn = new I18nKeyChooseButton(i18n, FLAG_WITH_ALL);
        private final ToggleSwitch enableDoubleClickToOpenDoorToggle = new ToggleSwitch();
        private final ForkedSlider doubleClickIntervalSlider = new ForkedSlider() {{
            setLength(200);
            setRange(50, 500);
        }};

        /* auto snake */
        private final ToggleSwitch enableAutoSnakeToggle = new ToggleSwitch();
        private final I18nKeyChooseButton autoSnakeActivateKeyBtn = new I18nKeyChooseButton(i18n, FLAG_WITH_KEY_AND_MOUSE);
        private final ToggleSwitch autoSnakeKeepMMenuToggle = new ToggleSwitch();
        private final ToggleSwitch autoSnakeRefillVestToggle = new ToggleSwitch();

        public BMMSettingStage(ObjectProperty<ExtendedI18n> i18n) {
            super(i18n, i -> i.betterMMenu.title);
            getContent().getChildren().addAll(contentBuilder()
                    .divide(i -> i.betterMMenu.baseSetting.title)
                    .button(i -> i.betterMMenu.baseSetting.menuKey, menuKeyBtn)
                    .slider(i -> i.betterMMenu.baseSetting.mouseScrollInterval, arrowKeyIntervalSlider)
                    .slider(i -> i.betterMMenu.baseSetting.keyPressInterval, enterKeyIntervalSlider)
                    .slider(i -> i.betterMMenu.baseSetting.timeUtilMMenuLoaded, timeUtilMMenuLoadedSlider)
                    .divide(i -> i.betterMMenu.startEngine.title)
                    .toggle(i -> i.betterMMenu.startEngine.enableStartEngine, enableStartEngineToggle)
                    .button(i -> i.betterMMenu.startEngine.activateKey, startEngineActivateKeyBtn)
                    .toggle(i -> i.betterMMenu.startEngine.enableDoubleClickToOpenDoor, enableDoubleClickToOpenDoorToggle)
                    .slider(i -> i.betterMMenu.startEngine.doubleClickInterval, doubleClickIntervalSlider)
                    .divide(i -> i.betterMMenu.autoSnake.title)
                    .toggle(i -> i.betterMMenu.autoSnake.enableAutoSnake, enableAutoSnakeToggle)
                    .button(i -> i.betterMMenu.autoSnake.activateKey, autoSnakeActivateKeyBtn)
                    .toggle(i -> i.betterMMenu.autoSnake.refillVest, autoSnakeRefillVestToggle)
                    .toggle(i -> i.betterMMenu.autoSnake.keepMMenu, autoSnakeKeepMMenuToggle)
                    .build());
        }

        @Override
        public void onVSettingStageInit() {
            /* base settings */
            menuKeyBtn.keyProperty().set(bmmConfig.baseSetting.menuKey);
            arrowKeyIntervalSlider.setValue(bmmConfig.baseSetting.mouseScrollInterval);
            enterKeyIntervalSlider.setValue(bmmConfig.baseSetting.keyPressInterval);
            timeUtilMMenuLoadedSlider.setValue(bmmConfig.baseSetting.timeUtilMMenuLoaded);

            /* start engine */
            enableStartEngineToggle.selectedProperty().set(bmmConfig.startEngine.enable);
            startEngineActivateKeyBtn.keyProperty().set(bmmConfig.startEngine.activateKey);
            enableDoubleClickToOpenDoorToggle.selectedProperty().set(bmmConfig.startEngine.enableDoubleClickToOpenDoor);
            doubleClickIntervalSlider.setValue(bmmConfig.startEngine.doubleClickInterval);

            /* auto snake */
            enableAutoSnakeToggle.selectedProperty().set(bmmConfig.autoSnake.enable);
            autoSnakeActivateKeyBtn.keyProperty().set(bmmConfig.autoSnake.activateKey);
            autoSnakeRefillVestToggle.selectedProperty().set(bmmConfig.autoSnake.refillVest);
            autoSnakeKeepMMenuToggle.selectedProperty().set(bmmConfig.autoSnake.keepMMenu);
        }

        @Override
        public void onVSettingStageExit() {
            /* base settings */
            bmmConfig.baseSetting.menuKey = menuKeyBtn.keyProperty().get();
            bmmConfig.baseSetting.mouseScrollInterval = arrowKeyIntervalSlider.valueProperty().get();
            bmmConfig.baseSetting.keyPressInterval = enterKeyIntervalSlider.valueProperty().get();
            bmmConfig.baseSetting.timeUtilMMenuLoaded = timeUtilMMenuLoadedSlider.valueProperty().get();

            /* start engine */
            bmmConfig.startEngine.activateKey = startEngineActivateKeyBtn.keyProperty().get();
            bmmConfig.startEngine.enable = enableStartEngineToggle.selectedProperty().get();
            bmmConfig.startEngine.enableDoubleClickToOpenDoor = enableDoubleClickToOpenDoorToggle.selectedProperty().get();
            bmmConfig.startEngine.doubleClickInterval = doubleClickIntervalSlider.valueProperty().get();

            /* auto snake */
            bmmConfig.autoSnake.enable = enableAutoSnakeToggle.selectedProperty().get();
            bmmConfig.autoSnake.activateKey = autoSnakeActivateKeyBtn.keyProperty().get();
            bmmConfig.autoSnake.refillVest = autoSnakeRefillVestToggle.selectedProperty().get();
            bmmConfig.autoSnake.keepMMenu = autoSnakeKeepMMenuToggle.selectedProperty().get();
        }
    }
}
