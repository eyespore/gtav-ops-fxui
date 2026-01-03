package club.pineclone.gtavops.gui.feature;

import club.pineclone.gtavops.common.ResourceHolder;
import club.pineclone.gtavops.config.Config;
import club.pineclone.gtavops.gui.component.VKeyChooseButton;
import club.pineclone.gtavops.gui.component.VSettingStage;
import club.pineclone.gtavops.gui.forked.ForkedKeyChooser;
import club.pineclone.gtavops.gui.forked.ForkedSlider;
import club.pineclone.gtavops.i18n.ExtendedI18n;
import club.pineclone.gtavops.macro.MacroContextHolder;
import club.pineclone.gtavops.macro.action.Action;
import club.pineclone.gtavops.macro.action.impl.bettermmenu.AutoSnakeAction;
import club.pineclone.gtavops.macro.action.impl.bettermmenu.StartEngineAction;
import club.pineclone.gtavops.macro.trigger.Trigger;
import club.pineclone.gtavops.macro.trigger.TriggerFactory;
import club.pineclone.gtavops.macro.trigger.TriggerIdentity;
import io.vproxy.vfx.entity.input.Key;
import io.vproxy.vfx.ui.toggle.ToggleSwitch;

import java.util.UUID;

public class _05BetterMMenuFeatureTogglePane
        extends FeatureTogglePaneTemplate
        implements ResourceHolder {

    public _05BetterMMenuFeatureTogglePane() {
        super(new BMMFeatureContext(), new BMMSettingStage());
    }

    @Override
    protected String getTitle() {
        return getI18n().betterMMenu.title;
    }

    @Override
    public boolean init() {
        return getConfig().betterMMenu.baseSetting.enable;
    }

    @Override
    public void stop(boolean enabled) {
        getConfig().betterMMenu.baseSetting.enable = enabled;
    }

    private static class BMMFeatureContext
            extends FeatureContext
            implements ResourceHolder, MacroContextHolder {

        private UUID startEngineMacroId;
        private UUID autoSnakeMacroId;

        private final Config config = getConfig();
        private final Config.BetterMMenu bmmConfig = config.betterMMenu;

        @Override
        protected void activate() {
            long mouseScrollInterval = (long) (Math.floor(bmmConfig.baseSetting.mouseScrollInterval));
            long keyPressInterval = (long) (Math.floor(bmmConfig.baseSetting.keyPressInterval));
            long timeUtilMMenuLoaded = (long) (Math.floor(bmmConfig.baseSetting.timeUtilMMenuLoaded));
            Key menuKey = bmmConfig.baseSetting.menuKey;

            /* 快速点火 */
            if (bmmConfig.startEngine.enable) {
                Key activateKey = bmmConfig.startEngine.activateKey;

                boolean enableDoubleClickToOpenDoor = bmmConfig.startEngine.enableDoubleClickToOpenDoor;
                long doubleClickInterval = (long) (Math.floor(bmmConfig.startEngine.doubleClickInterval));

                Trigger trigger;
                if (enableDoubleClickToOpenDoor) trigger = TriggerFactory.simple(TriggerIdentity.ofDoubleClick(doubleClickInterval, activateKey));  // 启用双击触发
                else trigger = TriggerFactory.simple(TriggerIdentity.ofClick(activateKey));  // 仅启用单击触发

                Action action = new StartEngineAction(menuKey, mouseScrollInterval, keyPressInterval, timeUtilMMenuLoaded, enableDoubleClickToOpenDoor);

                startEngineMacroId = MACRO_FACTORY.createSimpleMacro(trigger, action);
                MACRO_REGISTRY.install(startEngineMacroId);
            }

            /* 自动零食 */
            if (bmmConfig.autoSnake.enable) {
//                Logger.lowLevelDebug("enable auto snake");
                Key activateKey = bmmConfig.autoSnake.activateKey;
                boolean refillVest = bmmConfig.autoSnake.refillVest;
                boolean keepMMenu = bmmConfig.autoSnake.keepMMenu;

                Trigger trigger = TriggerFactory.simple(TriggerIdentity.ofHold(activateKey));
                Action action = new AutoSnakeAction(
                        menuKey, mouseScrollInterval, keyPressInterval, timeUtilMMenuLoaded, refillVest, keepMMenu);

                autoSnakeMacroId = MACRO_FACTORY.createSimpleMacro(trigger, action);
                MACRO_REGISTRY.install(autoSnakeMacroId);
            }
        }

        @Override
        protected void deactivate() {
            MACRO_REGISTRY.uninstall(startEngineMacroId);
            MACRO_REGISTRY.uninstall(autoSnakeMacroId);
        }
    }

    private static class BMMSettingStage
            extends VSettingStage
            implements ResourceHolder {

        private final ExtendedI18n i18n = getI18n();
        private final ExtendedI18n.BetterMMenu bmmI18n = i18n.betterMMenu;

        private final Config config = getConfig();
        private final Config.BetterMMenu bmmConfig = config.betterMMenu;

        private static final int FLAG_WITH_KEY_AND_MOUSE = ForkedKeyChooser.FLAG_WITH_KEY  | ForkedKeyChooser.FLAG_WITH_MOUSE;
        private static final int FLAG_WITH_ALL = FLAG_WITH_KEY_AND_MOUSE | ForkedKeyChooser.FLAG_WITH_WHEEL_SCROLL;

        private final VKeyChooseButton menuKeyBtn = new VKeyChooseButton(ForkedKeyChooser.FLAG_WITH_KEY);
        private final ForkedSlider arrowKeyIntervalSlider = new ForkedSlider() {{
            setLength(200);
            setRange(10, 100);
        }};
        private final ForkedSlider enterKeyIntervalSlider = new ForkedSlider() {{
            setLength(200);
            setRange(10, 100);
        }};

        private final ForkedSlider timeUtilMMenuLoadedSlider = new ForkedSlider() {{
            setLength(400);
            setRange(10, 2000);
        }};

        /* start engine */
        private final ToggleSwitch enableStartEngineToggle = new ToggleSwitch();
        private final VKeyChooseButton startEngineActivateKeyBtn = new VKeyChooseButton(FLAG_WITH_ALL);
        private final ToggleSwitch enableDoubleClickToOpenDoorToggle = new ToggleSwitch();
        private final ForkedSlider doubleClickIntervalSlider = new ForkedSlider() {{
            setLength(200);
            setRange(50, 500);
        }};

        /* auto snake */
        private final ToggleSwitch enableAutoSnakeToggle = new ToggleSwitch();
        private final VKeyChooseButton autoSnakeActivateKeyBtn = new VKeyChooseButton(FLAG_WITH_KEY_AND_MOUSE);
        private final ToggleSwitch autoSnakeKeepMMenuToggle = new ToggleSwitch();
        private final ToggleSwitch autoSnakeRefillVestToggle = new ToggleSwitch();

        public BMMSettingStage() {
            getContent().getChildren().addAll(contentBuilder()
                    .divide(bmmI18n.baseSetting.title)
                    .button(bmmI18n.baseSetting.menuKey, menuKeyBtn)
                    .slider(bmmI18n.baseSetting.mouseScrollInterval, arrowKeyIntervalSlider)
                    .slider(bmmI18n.baseSetting.keyPressInterval, enterKeyIntervalSlider)
                    .slider(bmmI18n.baseSetting.timeUtilMMenuLoaded, timeUtilMMenuLoadedSlider)
                    .divide(bmmI18n.startEngine.title)
                    .toggle(bmmI18n.startEngine.enableStartEngine, enableStartEngineToggle)
                    .button(bmmI18n.startEngine.activateKey, startEngineActivateKeyBtn)
                    .toggle(bmmI18n.startEngine.enableDoubleClickToOpenDoor, enableDoubleClickToOpenDoorToggle)
                    .slider(bmmI18n.startEngine.doubleClickInterval, doubleClickIntervalSlider)
                    .divide(bmmI18n.autoSnake.title)
                            .toggle(bmmI18n.autoSnake.enableAutoSnake, enableAutoSnakeToggle)
                            .button(bmmI18n.autoSnake.activateKey, autoSnakeActivateKeyBtn)
                            .toggle(bmmI18n.autoSnake.refillVest, autoSnakeRefillVestToggle)
                            .toggle(bmmI18n.autoSnake.keepMMenu, autoSnakeKeepMMenuToggle)
                    .build());
        }

        @Override
        public String getTitle() {
            return bmmI18n.title;
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
