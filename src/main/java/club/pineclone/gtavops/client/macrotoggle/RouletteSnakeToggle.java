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
import javafx.beans.property.ObjectProperty;

import java.util.UUID;

/* 回血增强 Tab按键 + 滚轮增强 roulette snake */
public class RouletteSnakeToggle extends MacroToggle {

    private UUID macroId;

    public RouletteSnakeToggle(ObjectProperty<ExtendedI18n> i18n) {
        super(i18n, i -> i.rouletteSnake.title, RSSettingStage::new);
    }

    @Override
    protected void onFeatureEnable() {
        macroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.ROULETTE_SNAKE_MACRO_CREATION_STRATEGY);
        MacroRegistry.getInstance().launchMacro(macroId);
    }

    @Override
    protected void onFeatureDisable() {
        MacroRegistry.getInstance().terminateMacro(macroId);
    }

    @Override
    public void onUIInit() {
        selectedProperty().set(MacroConfigLoader.get().rouletteSnake.baseSetting.enable);
    }

    @Override
    public void onUIDispose() {
        MacroConfigLoader.get().rouletteSnake.baseSetting.enable = selectedProperty().get();
    }

    private static class RSSettingStage
            extends MacroSettingStage
            implements ResourceHolder {

        private final MacroConfig.RouletteSnake qsConfig = getConfig().rouletteSnake;

        private static final int FLAG_WITH_KEY_AND_MOUSE = I18nKeyChooser.FLAG_WITH_KEY  | I18nKeyChooser.FLAG_WITH_MOUSE;
        private static final int FLAG_WITH_ALL = FLAG_WITH_KEY_AND_MOUSE | I18nKeyChooser.FLAG_WITH_WHEEL_SCROLL;

        private final I18nKeyChooseButton snakeKeyBtn = new I18nKeyChooseButton(i18n);
        private final I18nKeyChooseButton activateKeyBtn = new I18nKeyChooseButton(i18n, FLAG_WITH_KEY_AND_MOUSE);
        private final I18nKeyChooseButton weaponWheelKeyBtn = new I18nKeyChooseButton(i18n, FLAG_WITH_ALL);
        private final ForkedSlider triggerIntervalSlider = new ForkedSlider() {{
            setLength(200);
            setRange(1, 100);
        }};

        public RSSettingStage(ObjectProperty<ExtendedI18n> i18n) {
            super(i18n, i -> i.rouletteSnake.title);
            getContent().getChildren().addAll(contentBuilder()
                            .divide(i -> i.rouletteSnake.baseSetting.title)
                            .button(i -> i.rouletteSnake.baseSetting.weaponWheelKey, weaponWheelKeyBtn)
                            .button(i -> i.rouletteSnake.baseSetting.activateKey, activateKeyBtn)
                            .button(i -> i.rouletteSnake.baseSetting.snakeKey, snakeKeyBtn)
                            .slider(i -> i.rouletteSnake.baseSetting.triggerInterval, triggerIntervalSlider)
                            .build()
            );
        }

        @Override
        public void onVSettingStageInit() {
            super.onVSettingStageInit();
            activateKeyBtn.keyProperty().set(qsConfig.baseSetting.activatekey);
            snakeKeyBtn.keyProperty().set(qsConfig.baseSetting.snakeKey);
            weaponWheelKeyBtn.keyProperty().set(qsConfig.baseSetting.weaponWheel);
            triggerIntervalSlider.setValue(qsConfig.baseSetting.triggerInterval);
        }

        @Override
        public void onVSettingStageExit() {
            qsConfig.baseSetting.activatekey = activateKeyBtn.keyProperty().get();
            qsConfig.baseSetting.snakeKey = snakeKeyBtn.keyProperty().get();
            qsConfig.baseSetting.weaponWheel = weaponWheelKeyBtn.keyProperty().get();
            qsConfig.baseSetting.triggerInterval = triggerIntervalSlider.valueProperty().get();
        }
    }
}
