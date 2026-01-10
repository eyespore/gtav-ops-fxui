package club.pineclone.gtavops.client.scene.macrotoggle;

import club.pineclone.gtavops.client.i18n.I18nContext;
import club.pineclone.gtavops.client.i18n.I18nText;
import club.pineclone.gtavops.client.utils.ConfigContentBuilder;
import club.pineclone.gtavops.common.ResourceHolder;
import club.pineclone.gtavops.config.MacroConfig;
import club.pineclone.gtavops.config.MacroConfigLoader;
import club.pineclone.gtavops.macro.MacroCreationStrategies;
import club.pineclone.gtavops.macro.MacroRegistry;
import io.vproxy.vfx.entity.input.Key;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.UUID;

/* 回血增强 Tab按键 + 滚轮增强 roulette snake */
public class RouletteSnakeToggle extends MacroToggle {

    private UUID macroId;

    public RouletteSnakeToggle(I18nContext i18nContext) {
        super(i18nContext, I18nText.of(i -> i.rouletteSnake.title), RSSettingStage::new);
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

    private static class RSSettingStage extends MacroSettingStage implements ResourceHolder {

        private final MacroConfig.RouletteSnake qsConfig = getConfig().rouletteSnake;

        private final ObjectProperty<Key> weaponWheelKey = new SimpleObjectProperty<>();
        private final ObjectProperty<Key> activateKey = new SimpleObjectProperty<>();
        private final ObjectProperty<Key> snakeKey = new SimpleObjectProperty<>();
        private final DoubleProperty triggerInterval = new SimpleDoubleProperty();

        public RSSettingStage(I18nContext i18nContext, I18nText introTitle) {
            super(i18nContext, introTitle);
            this.content = ConfigContentBuilder.defaultLine(i18nContext)
                            .dividerRow(i -> i.rouletteSnake.baseSetting.title)
                            .keyChooseButtonRow(i -> i.rouletteSnake.baseSetting.weaponWheelKey, weaponWheelKey)
                            .keyChooseButtonRow(i -> i.rouletteSnake.baseSetting.activateKey, activateKey)
                            .keyChooseButtonRow(i -> i.rouletteSnake.baseSetting.snakeKey, snakeKey)
                            .sliderRow(i -> i.rouletteSnake.baseSetting.triggerInterval, 200, 1, 100, triggerInterval)
                            .build();
        }

        @Override
        public void onVSettingStageInit() {
            activateKey.set(qsConfig.baseSetting.activatekey);
            snakeKey.set(qsConfig.baseSetting.snakeKey);
            weaponWheelKey.set(qsConfig.baseSetting.weaponWheel);
            triggerInterval.set(qsConfig.baseSetting.triggerInterval);
        }

        @Override
        public void onVSettingStageExit() {
            qsConfig.baseSetting.activatekey = activateKey.get();
            qsConfig.baseSetting.snakeKey = snakeKey.get();
            qsConfig.baseSetting.weaponWheel = weaponWheelKey.get();
            qsConfig.baseSetting.triggerInterval = triggerInterval.get();
        }
    }
}
