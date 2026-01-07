package club.pineclone.gtavops.client.macrotoggle;

import club.pineclone.gtavops.common.ResourceHolder;
import club.pineclone.gtavops.config.MacroConfig;
import club.pineclone.gtavops.config.MacroConfigLoader;
import club.pineclone.gtavops.client.component.I18nTriggerModeChooseButton;
import club.pineclone.gtavops.client.forked.I18nKeyChooser;
import club.pineclone.gtavops.client.component.I18nKeyChooseButton;
import club.pineclone.gtavops.client.forked.ForkedSlider;
import club.pineclone.gtavops.client.i18n.ExtendedI18n;
import club.pineclone.gtavops.macro.MacroCreationStrategies;
import club.pineclone.gtavops.macro.MacroRegistry;
import io.vproxy.vfx.ui.toggle.ToggleSwitch;
import javafx.beans.property.ObjectProperty;

import java.text.MessageFormat;
import java.util.UUID;

/* 切枪偷速 */
public class SwapGlitchToggle extends MacroToggle {

    private UUID macroId;

    public SwapGlitchToggle(ObjectProperty<ExtendedI18n> i18n) {
        super(i18n, i -> i.swapGlitch.title, SGSettingStage::new);
    }

    @Override
    protected void onFeatureEnable() {
        macroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.SWAP_GLITCH_MACRO_CREATION_STRATEGY);
        MacroRegistry.getInstance().launchMacro(macroId);  /* 注册宏执行器 */
    }

    @Override
    protected void onFeatureDisable() {
        MacroRegistry.getInstance().terminateMacro(macroId);  /* 注销宏执行器 */
    }

    // TODO: 将config抽离到FeatureTogglePaneTemplate作为字段，通过构造器传入
    @Override
    public void onUIInit() {
        selectedProperty().set(MacroConfigLoader.get().swapGlitch.baseSetting.enable);
    }

    @Override
    public void onUIDispose() {
        MacroConfigLoader.get().swapGlitch.baseSetting.enable = selectedProperty().get();
    }

    private static class SGSettingStage extends MacroSettingStage implements ResourceHolder {

        private static final int FLAG_WITH_KEY_AND_MOUSE = I18nKeyChooser.FLAG_WITH_KEY  | I18nKeyChooser.FLAG_WITH_MOUSE;
        private static final int FLAG_WITH_ALL = FLAG_WITH_KEY_AND_MOUSE | I18nKeyChooser.FLAG_WITH_WHEEL_SCROLL;

        private final I18nKeyChooseButton activateKeyBtn = new I18nKeyChooseButton(i18n, FLAG_WITH_KEY_AND_MOUSE);
        private final I18nKeyChooseButton weaponWheelKeyBtn = new I18nKeyChooseButton(i18n, FLAG_WITH_ALL);
        private final I18nKeyChooseButton meleeKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);

        private final ToggleSwitch enableSwapMeleeToggle = new ToggleSwitch();

        private final MacroConfig config = getConfig();
        private final MacroConfig.SwapGlitch sgConfig = config.swapGlitch;

        private final I18nTriggerModeChooseButton activateMethodBtn = new I18nTriggerModeChooseButton(i18n,
                I18nTriggerModeChooseButton.FLAG_WITH_HOLD | I18nTriggerModeChooseButton.FLAG_WITH_TOGGLE);

        private final ForkedSlider triggerIntervalSlider = new ForkedSlider() {{
            setLength(200);
            setRange(10, 100);
        }};

        private final ForkedSlider postSwapMeleeDelaySlider = new ForkedSlider() {{
            setLength(200);
            setRange(10, 200);
        }};

        private final ToggleSwitch enableSwapRangedToggle = new ToggleSwitch();  /* 切出偷速时切换远程武器 */
        private final ToggleSwitch swapDefaultRangedWeaponOnEmptyToggle = new ToggleSwitch();  /* 没有选中任何远程武器时自动切换默认远程武器 */
        private final I18nKeyChooseButton defaultRangedWeaponKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);  /* 默认远程武器键位 */

        private final ToggleSwitch mapping1Toggle = new ToggleSwitch();  /* 监听远程武器映射1 */
        private final I18nKeyChooseButton mapping1SourceKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射1主键 */
        private final I18nKeyChooseButton mapping1TargetKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射1目标键 */

        private final ToggleSwitch mapping2Toggle = new ToggleSwitch();  /* 监听远程武器映射1 */
        private final I18nKeyChooseButton mapping2SourceKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射2主键 */
        private final I18nKeyChooseButton mapping2TargetKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射2目标键 */

        private final ToggleSwitch mapping3Toggle = new ToggleSwitch();  /* 监听远程武器映射1 */
        private final I18nKeyChooseButton mapping3SourceKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射3主键 */
        private final I18nKeyChooseButton mapping3TargetKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射3目标键 */

        private final ToggleSwitch mapping4Toggle = new ToggleSwitch();  /* 监听远程武器映射4 */
        private final I18nKeyChooseButton mapping4SourceKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射4主键 */
        private final I18nKeyChooseButton mapping4TargetKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射4目标键 */

        private final ToggleSwitch mapping5Toggle = new ToggleSwitch();  /* 监听远程武器映射5 */
        private final I18nKeyChooseButton mapping5SourceKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射5主键 */
        private final I18nKeyChooseButton mapping5TargetKeyBtn = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射5目标键 */

        private final ToggleSwitch swapRangedClearKeyToggle = new ToggleSwitch();  /* 启用屏蔽切换远程武器 */
        private final I18nKeyChooseButton swapRangedClearKeyBtn = new I18nKeyChooseButton(i18n);  /* 屏蔽切换远程武器键 */

        public SGSettingStage(ObjectProperty<ExtendedI18n> i18n) {
            super(i18n, i -> i.swapGlitch.title);
            getContent().getChildren().addAll(contentBuilder()
                    /* 基础设置 */
                    .divide(i -> i.swapGlitch.baseSetting.title)
                    .button(i -> i.swapGlitch.baseSetting.activateMethod, activateMethodBtn)
                    .button(i -> i.swapGlitch.baseSetting.targetWeaponWheelKey, weaponWheelKeyBtn)
                    .button(i -> i.swapGlitch.baseSetting.activateKey, activateKeyBtn)
                    .slider(i -> i.swapGlitch.baseSetting.triggerInterval, triggerIntervalSlider)
                    /* 切换近战武器设置 */
                    .divide(i -> i.swapGlitch.swapMeleeSetting.title)
                    .toggle(i -> i.swapGlitch.swapMeleeSetting.enable, enableSwapMeleeToggle)
                    .button(i -> i.swapGlitch.swapMeleeSetting.meleeKey, meleeKeyBtn)
                    .slider(i -> i.swapGlitch.swapMeleeSetting.postSwapMeleeDelay, postSwapMeleeDelaySlider)
                    /* 切换远程武器设置 */
                    .divide(i -> i.swapGlitch.swapRangedSetting.title)
                    .toggle(i -> i.swapGlitch.swapRangedSetting.enable, enableSwapRangedToggle)
                    .buttonToggle(i -> i.swapGlitch.swapRangedSetting.defaultRangedWeaponKey, swapDefaultRangedWeaponOnEmptyToggle, defaultRangedWeaponKeyBtn)
                    .buttonToggle(i -> MessageFormat.format(i.swapGlitch.swapRangedSetting.listenRangedWeaponMapping, 1), mapping1Toggle, mapping1SourceKeyBtn, mapping1TargetKeyBtn)
                    .buttonToggle(i -> MessageFormat.format(i.swapGlitch.swapRangedSetting.listenRangedWeaponMapping, 2), mapping2Toggle, mapping2SourceKeyBtn, mapping2TargetKeyBtn)
                    .buttonToggle(i -> MessageFormat.format(i.swapGlitch.swapRangedSetting.listenRangedWeaponMapping, 3), mapping3Toggle, mapping3SourceKeyBtn, mapping3TargetKeyBtn)
                    .buttonToggle(i -> MessageFormat.format(i.swapGlitch.swapRangedSetting.listenRangedWeaponMapping, 4), mapping4Toggle, mapping4SourceKeyBtn, mapping4TargetKeyBtn)
                    .buttonToggle(i -> MessageFormat.format(i.swapGlitch.swapRangedSetting.listenRangedWeaponMapping, 5), mapping5Toggle, mapping5SourceKeyBtn, mapping5TargetKeyBtn)
                    .gap()
                    .toggle(i -> i.swapGlitch.swapRangedSetting.enableClearKey, swapRangedClearKeyToggle)
                    .button(i -> i.swapGlitch.swapRangedSetting.clearKey, swapRangedClearKeyBtn)
//                    .slider(sgI18n.swapRangedSetting.blockDuration, swapRangedBlockDurationSlider)
                    .build()
            );
        }

        @Override
        public void onVSettingStageInit() {
            activateKeyBtn.keyProperty().set(sgConfig.baseSetting.activatekey);
            weaponWheelKeyBtn.keyProperty().set(sgConfig.baseSetting.targetWeaponWheelKey);
            meleeKeyBtn.keyProperty().set(sgConfig.swapMeleeSetting.meleeWeaponKey);

            postSwapMeleeDelaySlider.setValue(sgConfig.swapMeleeSetting.postSwapMeleeDelay);

            activateMethodBtn.triggerModeProperty().set(sgConfig.baseSetting.activateMethod);
            triggerIntervalSlider.setValue(sgConfig.baseSetting.triggerInterval);

            enableSwapMeleeToggle.selectedProperty().set(sgConfig.swapMeleeSetting.enableSwapMelee);

            enableSwapRangedToggle.selectedProperty().set(sgConfig.swapRangedSetting.enableSwapRanged);
            swapDefaultRangedWeaponOnEmptyToggle.selectedProperty().set(sgConfig.swapRangedSetting.swapDefaultRangedWeaponOnEmpty);
            defaultRangedWeaponKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.defaultRangedWeaponKey);

            mapping1Toggle.selectedProperty().set(sgConfig.swapRangedSetting.enableMapping1);
            mapping1SourceKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping1SourceKey);
            mapping1TargetKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping1TargetKey);

            mapping2Toggle.selectedProperty().set(sgConfig.swapRangedSetting.enableMapping2);
            mapping2SourceKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping2SourceKey);
            mapping2TargetKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping2TargetKey);

            mapping3Toggle.selectedProperty().set(sgConfig.swapRangedSetting.enableMapping3);
            mapping3SourceKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping3SourceKey);
            mapping3TargetKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping3TargetKey);

            mapping4Toggle.selectedProperty().set(sgConfig.swapRangedSetting.enableMapping4);
            mapping4SourceKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping4SourceKey);
            mapping4TargetKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping4TargetKey);

            mapping5Toggle.selectedProperty().set(sgConfig.swapRangedSetting.enableMapping5);
            mapping5SourceKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping5SourceKey);
            mapping5TargetKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping5TargetKey);

            swapRangedClearKeyToggle.selectedProperty().set(sgConfig.swapRangedSetting.enableClearKey);
            swapRangedClearKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.clearKey);
        }

        @Override
        public void onVSettingStageExit() {
            sgConfig.baseSetting.targetWeaponWheelKey = weaponWheelKeyBtn.keyProperty().get();
            sgConfig.swapMeleeSetting.meleeWeaponKey = meleeKeyBtn.keyProperty().get();

            sgConfig.baseSetting.activatekey = activateKeyBtn.keyProperty().get();
            sgConfig.baseSetting.activateMethod = activateMethodBtn.triggerModeProperty().get();
            sgConfig.baseSetting.triggerInterval = triggerIntervalSlider.valueProperty().get();
            sgConfig.swapMeleeSetting.postSwapMeleeDelay = postSwapMeleeDelaySlider.valueProperty().get();
            sgConfig.swapMeleeSetting.enableSwapMelee = enableSwapMeleeToggle.selectedProperty().get();

            MacroConfig.SwapGlitch.SwapRangedSetting srSetting = sgConfig.swapRangedSetting;
            srSetting.enableSwapRanged = enableSwapRangedToggle.selectedProperty().get();
            srSetting.swapDefaultRangedWeaponOnEmpty = swapDefaultRangedWeaponOnEmptyToggle.selectedProperty().get();
            srSetting.defaultRangedWeaponKey = defaultRangedWeaponKeyBtn.keyProperty().get();

            srSetting.enableMapping1 = mapping1Toggle.selectedProperty().get();
            srSetting.mapping1SourceKey = mapping1SourceKeyBtn.keyProperty().get();
            srSetting.mapping1TargetKey = mapping1TargetKeyBtn.keyProperty().get();

            srSetting.enableMapping2 = mapping2Toggle.selectedProperty().get();
            srSetting.mapping2SourceKey = mapping2SourceKeyBtn.keyProperty().get();
            srSetting.mapping2TargetKey = mapping2TargetKeyBtn.keyProperty().get();

            srSetting.enableMapping3 = mapping3Toggle.selectedProperty().get();
            srSetting.mapping3SourceKey = mapping3SourceKeyBtn.keyProperty().get();
            srSetting.mapping3TargetKey = mapping3TargetKeyBtn.keyProperty().get();

            srSetting.enableMapping4 = mapping4Toggle.selectedProperty().get();
            srSetting.mapping4SourceKey = mapping4SourceKeyBtn.keyProperty().get();
            srSetting.mapping4TargetKey = mapping4TargetKeyBtn.keyProperty().get();

            srSetting.enableMapping5 = mapping5Toggle.selectedProperty().get();
            srSetting.mapping5SourceKey = mapping5SourceKeyBtn.keyProperty().get();
            srSetting.mapping5TargetKey = mapping5TargetKeyBtn.keyProperty().get();

            srSetting.enableClearKey = swapRangedClearKeyToggle.selectedProperty().get();
            srSetting.clearKey = swapRangedClearKeyBtn.keyProperty().get();
        }
    }
}
