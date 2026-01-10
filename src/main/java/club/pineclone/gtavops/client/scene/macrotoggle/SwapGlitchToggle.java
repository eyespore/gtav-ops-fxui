package club.pineclone.gtavops.client.scene.macrotoggle;

import club.pineclone.gtavops.client.i18n.I18nContext;
import club.pineclone.gtavops.client.i18n.I18nText;
import club.pineclone.gtavops.client.utils.ConfigContentBuilder;
import club.pineclone.gtavops.common.ResourceHolder;
import club.pineclone.gtavops.common.TriggerMode;
import club.pineclone.gtavops.config.MacroConfig;
import club.pineclone.gtavops.config.MacroConfigLoader;
import club.pineclone.gtavops.client.forked.I18nKeyChooser;
import club.pineclone.gtavops.macro.MacroCreationStrategies;
import club.pineclone.gtavops.macro.MacroRegistry;
import io.vproxy.vfx.entity.input.Key;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;

import java.util.UUID;

import static club.pineclone.gtavops.client.component.I18nTriggerModeChooseButton.FLAG_WITH_HOLD;
import static club.pineclone.gtavops.client.component.I18nTriggerModeChooseButton.FLAG_WITH_TOGGLE;
import static club.pineclone.gtavops.client.utils.ConfigContentBuilder.DEF_ROW_PADDING;

/* 切枪偷速 */
public class SwapGlitchToggle extends MacroToggle {

    private UUID macroId;

    public SwapGlitchToggle(I18nContext i18nContext) {
        super(i18nContext, I18nText.of(i -> i.swapGlitch.title), SGSettingStage::new);
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

//        private final I18nKeyChooseButton activateKeyBtn = new I18nKeyChooseButton(i18nContext, FLAG_WITH_KEY_AND_MOUSE);
//        private final I18nKeyChooseButton weaponWheelKeyBtn = new I18nKeyChooseButton(i18nContext, FLAG_WITH_ALL);
        private final ObjectProperty<Key> targetWeaponWheelKey = new SimpleObjectProperty<>();
        private final ObjectProperty<Key> activateKey = new SimpleObjectProperty<>();

//        private final I18nKeyChooseButton meleeKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);
        private final ObjectProperty<Key> meleeWeaponKey = new SimpleObjectProperty<>();  /* 近战武器键 */
//        private final ToggleSwitch enableSwapMeleeToggle = new ToggleSwitch();
        private final BooleanProperty enableSwapMelee = new SimpleBooleanProperty();  /* 切换近战武器 */

        private final MacroConfig config = getConfig();
        private final MacroConfig.SwapGlitch sgConfig = config.swapGlitch;

//        private final I18nTriggerModeChooseButton activateMethodBtn = new I18nTriggerModeChooseButton(i18nContext, FLAG_WITH_HOLD | FLAG_WITH_TOGGLE);
        private final ObjectProperty<TriggerMode> activateMethod = new SimpleObjectProperty<>();

//        private final ForkedSlider triggerIntervalSlider = new ForkedSlider() {{
//            setLength(200);
//            setRange(10, 100);
//        }};
        private final DoubleProperty triggerInterval = new SimpleDoubleProperty();

//        private final ForkedSlider postSwapMeleeDelaySlider = new ForkedSlider() {{
//            setLength(200);
//            setRange(10, 200);
//        }};
        private final DoubleProperty postSwapMeleeDelay = new SimpleDoubleProperty();  /* 切换近战武器之后等待延时 */

//        private final ToggleSwitch enableSwapRangedToggle = new ToggleSwitch();
        private final BooleanProperty enableSwapRanged = new SimpleBooleanProperty();  /* 切出偷速时切换远程武器 */

//        private final ToggleSwitch swapDefaultRangedWeaponOnEmptyToggle = new ToggleSwitch();  /* 没有选中任何远程武器时自动切换默认远程武器 */
//        private final I18nKeyChooseButton defaultRangedWeaponKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 默认远程武器键位 */
//
//        private final ToggleSwitch mapping1Toggle = new ToggleSwitch();  /* 监听远程武器映射1 */
//        private final I18nKeyChooseButton mapping1SourceKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射1主键 */
//        private final I18nKeyChooseButton mapping1TargetKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射1目标键 */
//
//        private final ToggleSwitch mapping2Toggle = new ToggleSwitch();  /* 监听远程武器映射1 */
//        private final I18nKeyChooseButton mapping2SourceKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射2主键 */
//        private final I18nKeyChooseButton mapping2TargetKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射2目标键 */
//
//        private final ToggleSwitch mapping3Toggle = new ToggleSwitch();  /* 监听远程武器映射1 */
//        private final I18nKeyChooseButton mapping3SourceKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射3主键 */
//        private final I18nKeyChooseButton mapping3TargetKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射3目标键 */
//
//        private final ToggleSwitch mapping4Toggle = new ToggleSwitch();  /* 监听远程武器映射4 */
//        private final I18nKeyChooseButton mapping4SourceKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射4主键 */
//        private final I18nKeyChooseButton mapping4TargetKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射4目标键 */
//
//        private final ToggleSwitch mapping5Toggle = new ToggleSwitch();  /* 监听远程武器映射5 */
//        private final I18nKeyChooseButton mapping5SourceKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射5主键 */
//        private final I18nKeyChooseButton mapping5TargetKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射5目标键 */
//
//        private final ToggleSwitch swapRangedClearKeyToggle = new ToggleSwitch();  /* 启用屏蔽切换远程武器 */
        private final BooleanProperty enableClearKey = new SimpleBooleanProperty();
        private final ObjectProperty<Key> clearKey = new SimpleObjectProperty<>();
        private final BooleanProperty swapDefaultRangedWeaponOnEmpty = new SimpleBooleanProperty();  /* 结束偷速时切换到默认远程武器 */
        private final ObjectProperty<Key> defaultRangedWeaponKey = new SimpleObjectProperty<>();  /* 默认远程武器键位 */

//        private final I18nKeyChooseButton swapRangedClearKeyBtn = new I18nKeyChooseButton(i18nContext);  /* 屏蔽切换远程武器键 */

        /* 按键映射设置 */
        private static class KeyMapSettingViewModel {
            public final BooleanProperty enabled = new SimpleBooleanProperty();  /* 是否启用 */
            public final ObjectProperty<Key> sourceKey = new SimpleObjectProperty<>();  /* 源键 */
            public final ObjectProperty<Key> targetKey = new SimpleObjectProperty<>();  /* 映射键 */
        }

        private final ObservableList<KeyMapSettingViewModel> keyMapSettingViewModels = FXCollections.observableArrayList();

        public SGSettingStage(I18nContext i18nContext, I18nText introTitle) {
            super(i18nContext, introTitle);
            this.content = ConfigContentBuilder.defaultLine(i18nContext)
                    /* 基础设置 */
                    .dividerRow(i -> i.swapGlitch.baseSetting.title)
                    .triggerModeChooseButtonRow(i -> i.swapGlitch.baseSetting.activateMethod, FLAG_WITH_HOLD | FLAG_WITH_TOGGLE, activateMethod)
                    .keyChooseButtonRow(i -> i.swapGlitch.baseSetting.targetWeaponWheelKey, FLAG_WITH_ALL, targetWeaponWheelKey)
                    .keyChooseButtonRow(i -> i.swapGlitch.baseSetting.activateKey, activateKey)
                    .sliderRow(i -> i.swapGlitch.baseSetting.triggerInterval, 200, 10, 100, triggerInterval)
                    /* 切换近战武器设置 */
                    .dividerRow(i -> i.swapGlitch.swapMeleeSetting.title)
                    .toggleButtonRow(i -> i.swapGlitch.swapMeleeSetting.enable, enableSwapMelee)
                    .keyChooseButtonRow(i -> i.swapGlitch.swapMeleeSetting.meleeKey, meleeWeaponKey)
                    .sliderRow(i -> i.swapGlitch.swapMeleeSetting.postSwapMeleeDelay, 200, 10, 200, postSwapMeleeDelay)
                    /* 切换远程武器设置 */
                    .dividerRow(i -> i.swapGlitch.swapRangedSetting.title)
                    .toggleButtonRow(i -> i.swapGlitch.swapRangedSetting.enable, enableSwapRanged)
                    .row().spacing(10).padding(DEF_ROW_PADDING)
                    .line().alignment(Pos.CENTER)
                        .themeLabel().text(I18nText.of(i -> i.swapGlitch.swapRangedSetting.defaultRangedWeaponKey)).buildForContent().buildForParent()
                        .hSpacer()
                        .keyChooseButton(defaultRangedWeaponKey)
                        .toggleButton().bindSelected(swapDefaultRangedWeaponOnEmpty).buildForContent()
                    .buildForParent()
                    .listConfigEditorRow(i -> i.quickSwap.baseSetting.keyMapListEditor, i -> i.quickSwap.baseSetting.keyMapNotFount,
                            keyMapSettingViewModels,
                            k -> ConfigContentBuilder.defaultRow(i18nContext).spacing(10)
                                    .line().alignment(Pos.CENTER)
                                    .themeLabel().text(I18nText.of(i -> i.quickSwap.baseSetting.keyMapIntro, keyMapSettingViewModels.indexOf(k) + 1)).buildForContent()
                                    .buildForParent()
                                    .hSpacer()
                                    .keyChooseButton(k.sourceKey, true)
                                    .keyChooseButton(k.targetKey, true)
                                    .toggleButton().bindSelected(k.enabled).buildForContent()
                                    .build(), KeyMapSettingViewModel::new)
                    .toggleButtonRow(i -> i.swapGlitch.swapRangedSetting.enableClearKey, enableClearKey)
                    .keyChooseButtonRow(i -> i.swapGlitch.swapRangedSetting.clearKey, clearKey)
                    .build();

//                    .buttonToggle(i -> MessageFormat.format(i.swapGlitch.swapRangedSetting.listenRangedWeaponMapping, 1), mapping1Toggle, mapping1SourceKeyBtn, mapping1TargetKeyBtn)
//                    .buttonToggle(i -> MessageFormat.format(i.swapGlitch.swapRangedSetting.listenRangedWeaponMapping, 2), mapping2Toggle, mapping2SourceKeyBtn, mapping2TargetKeyBtn)
//                    .buttonToggle(i -> MessageFormat.format(i.swapGlitch.swapRangedSetting.listenRangedWeaponMapping, 3), mapping3Toggle, mapping3SourceKeyBtn, mapping3TargetKeyBtn)
//                    .buttonToggle(i -> MessageFormat.format(i.swapGlitch.swapRangedSetting.listenRangedWeaponMapping, 4), mapping4Toggle, mapping4SourceKeyBtn, mapping4TargetKeyBtn)
//                    .buttonToggle(i -> MessageFormat.format(i.swapGlitch.swapRangedSetting.listenRangedWeaponMapping, 5), mapping5Toggle, mapping5SourceKeyBtn, mapping5TargetKeyBtn)
//                    .gap()

//                    .slider(sgI18n.swapRangedSetting.blockDuration, swapRangedBlockDurationSlider)


        }

        @Override
        public void onVSettingStageInit() {
            activateKey.set(sgConfig.baseSetting.activatekey);
            targetWeaponWheelKey.set(sgConfig.baseSetting.targetWeaponWheelKey);
            meleeWeaponKey.set(sgConfig.swapMeleeSetting.meleeWeaponKey);

            postSwapMeleeDelay.set(sgConfig.swapMeleeSetting.postSwapMeleeDelay);

            activateMethod.set(sgConfig.baseSetting.activateMethod);
            triggerInterval.set(sgConfig.baseSetting.triggerInterval);

            enableSwapMelee.set(sgConfig.swapMeleeSetting.enableSwapMelee);
            enableSwapRanged.set(sgConfig.swapRangedSetting.enableSwapRanged);

            swapDefaultRangedWeaponOnEmpty.set(sgConfig.swapRangedSetting.swapDefaultRangedWeaponOnEmpty);
            defaultRangedWeaponKey.set(sgConfig.swapRangedSetting.defaultRangedWeaponKey);

//            mapping1Toggle.selectedProperty().set(sgConfig.swapRangedSetting.enableMapping1);
//            mapping1SourceKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping1SourceKey);
//            mapping1TargetKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping1TargetKey);
//
//            mapping2Toggle.selectedProperty().set(sgConfig.swapRangedSetting.enableMapping2);
//            mapping2SourceKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping2SourceKey);
//            mapping2TargetKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping2TargetKey);
//
//            mapping3Toggle.selectedProperty().set(sgConfig.swapRangedSetting.enableMapping3);
//            mapping3SourceKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping3SourceKey);
//            mapping3TargetKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping3TargetKey);
//
//            mapping4Toggle.selectedProperty().set(sgConfig.swapRangedSetting.enableMapping4);
//            mapping4SourceKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping4SourceKey);
//            mapping4TargetKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping4TargetKey);
//
//            mapping5Toggle.selectedProperty().set(sgConfig.swapRangedSetting.enableMapping5);
//            mapping5SourceKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping5SourceKey);
//            mapping5TargetKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping5TargetKey);

            enableClearKey.set(sgConfig.swapRangedSetting.enableClearKey);
            clearKey.set(sgConfig.swapRangedSetting.clearKey);
        }

        @Override
        public void onVSettingStageExit() {
            sgConfig.baseSetting.targetWeaponWheelKey = targetWeaponWheelKey.get();
            sgConfig.swapMeleeSetting.meleeWeaponKey = meleeWeaponKey.get();

            sgConfig.baseSetting.activatekey = activateKey.get();
            sgConfig.baseSetting.activateMethod = activateMethod.get();
            sgConfig.baseSetting.triggerInterval = triggerInterval.get();
            sgConfig.swapMeleeSetting.postSwapMeleeDelay = postSwapMeleeDelay.get();
            sgConfig.swapMeleeSetting.enableSwapMelee = enableSwapMelee.get();

            MacroConfig.SwapGlitch.SwapRangedSetting srSetting = sgConfig.swapRangedSetting;
            srSetting.enableSwapRanged = enableSwapRanged.get();
            srSetting.swapDefaultRangedWeaponOnEmpty = swapDefaultRangedWeaponOnEmpty.get();
            srSetting.defaultRangedWeaponKey = defaultRangedWeaponKey.get();

//            srSetting.enableMapping1 = mapping1Toggle.selectedProperty().get();
//            srSetting.mapping1SourceKey = mapping1SourceKeyBtn.keyProperty().get();
//            srSetting.mapping1TargetKey = mapping1TargetKeyBtn.keyProperty().get();
//
//            srSetting.enableMapping2 = mapping2Toggle.selectedProperty().get();
//            srSetting.mapping2SourceKey = mapping2SourceKeyBtn.keyProperty().get();
//            srSetting.mapping2TargetKey = mapping2TargetKeyBtn.keyProperty().get();
//
//            srSetting.enableMapping3 = mapping3Toggle.selectedProperty().get();
//            srSetting.mapping3SourceKey = mapping3SourceKeyBtn.keyProperty().get();
//            srSetting.mapping3TargetKey = mapping3TargetKeyBtn.keyProperty().get();
//
//            srSetting.enableMapping4 = mapping4Toggle.selectedProperty().get();
//            srSetting.mapping4SourceKey = mapping4SourceKeyBtn.keyProperty().get();
//            srSetting.mapping4TargetKey = mapping4TargetKeyBtn.keyProperty().get();
//
//            srSetting.enableMapping5 = mapping5Toggle.selectedProperty().get();
//            srSetting.mapping5SourceKey = mapping5SourceKeyBtn.keyProperty().get();
//            srSetting.mapping5TargetKey = mapping5TargetKeyBtn.keyProperty().get();

            srSetting.enableClearKey = enableClearKey.get();
            srSetting.clearKey = clearKey.get();
        }
    }
}
