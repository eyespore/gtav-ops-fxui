package club.pineclone.gtavops.client.scene.macrotoggle;

import club.pineclone.gtavops.client.forked.I18nKeyChooser;
import club.pineclone.gtavops.client.i18n.I18nContext;
import club.pineclone.gtavops.client.i18n.I18nText;
import club.pineclone.gtavops.client.utils.ConfigContentBuilder;
import io.vproxy.vfx.entity.input.Key;
import javafx.beans.property.*;

import java.util.UUID;

public class BetterMMenuToggle extends MacroToggle {

    private UUID startEngineMacroId;
    private UUID autoSnakeMacroId;

    public BetterMMenuToggle(I18nContext i18nContext) {
        super(i18nContext, I18nText.of(i -> i.betterMMenu.title), BMMSettingStage::new);
    }

    @Override
    protected void onFeatureEnable() {
//        MacroConfig.BetterMMenu bmmConfig = MacroConfigLoader.get().betterMMenu;
        /* 快速点火宏启用 */
//        if (bmmConfig.startEngine.enable) {
//            startEngineMacroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.START_ENGINE_MACRO_CREATION_STRATEGY);
//            MacroRegistry.getInstance().launchMacro(startEngineMacroId);
//        }
        /* 自动零食宏启用 */
//        if (bmmConfig.autoSnake.enable) {
//            autoSnakeMacroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.AUTO_SNAKE_MACRO_CREATION_STRATEGY);
//            MacroRegistry.getInstance().launchMacro(autoSnakeMacroId);
//        }
    }

    @Override
    protected void onFeatureDisable() {
//        MacroRegistry.getInstance().terminateMacro(startEngineMacroId);
//        MacroRegistry.getInstance().terminateMacro(autoSnakeMacroId);
    }

    @Override
    public void onUIInit() {
//        selectedProperty().set(MacroConfigLoader.get().betterMMenu.baseSetting.enable);
    }

    @Override
    public void onUIDispose() {
//        MacroConfigLoader.get().betterMMenu.baseSetting.enable = selectedProperty().get();
    }

    private static class BMMSettingStage extends MacroSettingStage {

//        private final MacroConfig config = getConfig();
//        private final MacroConfig.BetterMMenu bmmConfig = config.betterMMenu;

        private static final int FLAG_WITH_KEY_AND_MOUSE = I18nKeyChooser.FLAG_WITH_KEY  | I18nKeyChooser.FLAG_WITH_MOUSE;
        private static final int FLAG_WITH_ALL = FLAG_WITH_KEY_AND_MOUSE | I18nKeyChooser.FLAG_WITH_WHEEL_SCROLL;

        private final ObjectProperty<Key> menuKey = new SimpleObjectProperty<>();
        private final DoubleProperty mouseScrollInterval = new SimpleDoubleProperty();
        private final DoubleProperty keyPressInterval = new SimpleDoubleProperty();
        private final DoubleProperty timeUtilMMenuLoaded = new SimpleDoubleProperty();

        private final BooleanProperty enableStartEngine = new SimpleBooleanProperty();
        private final ObjectProperty<Key> startEngineActivateKey = new SimpleObjectProperty<>();
        private final BooleanProperty enableDoubleClickToOpenDoor = new SimpleBooleanProperty();
        private final DoubleProperty doubleClickDetectInterval = new SimpleDoubleProperty();

        private final BooleanProperty enableAutoSnake = new SimpleBooleanProperty();
        private final ObjectProperty<Key> autoSnakeActivateKey = new SimpleObjectProperty<>();
        private final BooleanProperty autoSnakeRefillVest = new SimpleBooleanProperty();
        private final BooleanProperty autoSnakeKeepMMenu = new SimpleBooleanProperty();

        public BMMSettingStage(I18nContext i18nContext, I18nText introTitle) {
            super(i18nContext, introTitle);
            this.content = ConfigContentBuilder.defaultLine(i18nContext)
                    .dividerRow(i -> i.betterMMenu.baseSetting.title)
                    .keyChooseButtonRow(i -> i.betterMMenu.baseSetting.mouseScrollInterval, menuKey)
                    .sliderRow(i -> i.betterMMenu.baseSetting.mouseScrollInterval, 200, 10, 100, mouseScrollInterval)
                    .sliderRow(i -> i.betterMMenu.baseSetting.keyPressInterval, 200, 10, 100, keyPressInterval)
                    .sliderRow(i -> i.betterMMenu.baseSetting.timeUtilMMenuLoaded, 200, 10, 100, timeUtilMMenuLoaded)
                    .dividerRow(i -> i.betterMMenu.startEngine.title)
                    .toggleButtonRow(i -> i.betterMMenu.startEngine.enableStartEngine, enableStartEngine)
                    .keyChooseButtonRow(i -> i.betterMMenu.startEngine.activateKey, FLAG_WITH_ALL, startEngineActivateKey)
                    .toggleButtonRow(i -> i.betterMMenu.startEngine.enableDoubleClickToOpenDoor, enableDoubleClickToOpenDoor)
                    .sliderRow(i -> i.betterMMenu.startEngine.doubleClickInterval, 200, 50, 500, doubleClickDetectInterval)
                    .dividerRow(i -> i.betterMMenu.autoSnake.title)
                    .toggleButtonRow(i -> i.betterMMenu.autoSnake.enableAutoSnake, enableAutoSnake)
                    .keyChooseButtonRow(i -> i.betterMMenu.autoSnake.activateKey, FLAG_WITH_KEY_AND_MOUSE, autoSnakeActivateKey)
                    .toggleButtonRow(i -> i.betterMMenu.autoSnake.refillVest, autoSnakeRefillVest)
                    .toggleButtonRow(i -> i.betterMMenu.autoSnake.keepMMenu, autoSnakeKeepMMenu)
                    .build();
        }

        @Override
        public void onVSettingStageInit() {
//            /* base settings */
//            menuKey.set(bmmConfig.baseSetting.menuKey);
//            mouseScrollInterval.set(bmmConfig.baseSetting.mouseScrollInterval);
//            keyPressInterval.set(bmmConfig.baseSetting.keyPressInterval);
//            timeUtilMMenuLoaded.set(bmmConfig.baseSetting.timeUtilMMenuLoaded);
//
//            /* start engine */
//            enableStartEngine.set(bmmConfig.startEngine.enable);
//            startEngineActivateKey.set(bmmConfig.startEngine.activateKey);
//            enableDoubleClickToOpenDoor.set(bmmConfig.startEngine.enableDoubleClickToOpenDoor);
//            doubleClickDetectInterval.set(bmmConfig.startEngine.doubleClickInterval);
//
//            /* auto snake */
//            enableAutoSnake.set(bmmConfig.autoSnake.enable);
//            autoSnakeActivateKey.set(bmmConfig.autoSnake.activateKey);
//            autoSnakeRefillVest.set(bmmConfig.autoSnake.refillVest);
//            autoSnakeKeepMMenu.set(bmmConfig.autoSnake.keepMMenu);
        }

        @Override
        public void onVSettingStageExit() {
//            /* base settings */
//            bmmConfig.baseSetting.menuKey = menuKey.get();
//            bmmConfig.baseSetting.mouseScrollInterval = mouseScrollInterval.get();
//            bmmConfig.baseSetting.keyPressInterval = keyPressInterval.get();
//            bmmConfig.baseSetting.timeUtilMMenuLoaded = timeUtilMMenuLoaded.get();
//
//            /* start engine */
//            bmmConfig.startEngine.enable = enableStartEngine.get();
//            bmmConfig.startEngine.activateKey = startEngineActivateKey.get();
//            bmmConfig.startEngine.enableDoubleClickToOpenDoor = enableDoubleClickToOpenDoor.get();
//            bmmConfig.startEngine.doubleClickInterval = doubleClickDetectInterval.get();
//
//
//            /* auto snake */
//            bmmConfig.autoSnake.enable = enableAutoSnake.get();
//            bmmConfig.autoSnake.activateKey = autoSnakeActivateKey.get();
//            bmmConfig.autoSnake.refillVest = autoSnakeRefillVest.get();
//            bmmConfig.autoSnake.keepMMenu = autoSnakeKeepMMenu.get();
        }
    }
}
