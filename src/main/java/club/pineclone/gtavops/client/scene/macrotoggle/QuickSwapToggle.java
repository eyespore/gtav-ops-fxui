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
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;

import java.util.UUID;

/* 快速切枪 */
public class QuickSwapToggle extends MacroToggle {

    private UUID macroId;

    public QuickSwapToggle(I18nContext i18nContext) {
        super(i18nContext, I18nText.of(i -> i.quickSwap.title), QSSettingStage::new);
    }

    @Override
    protected void onFeatureEnable() {
        macroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.QUICK_SWAP_MACRO_CREATION_STRATEGY);
        MacroRegistry.getInstance().launchMacro(macroId);
    }

    @Override
    protected void onFeatureDisable() {
        MacroRegistry.getInstance().terminateMacro(macroId);
    }

    @Override
    public void onUIInit() {
        selectedProperty().set(MacroConfigLoader.get().quickSwap.baseSetting.enable);
    }

    @Override
    public void onUIDispose() {
        MacroConfigLoader.get().quickSwap.baseSetting.enable = selectedProperty().get();
    }

    private static class QSSettingStage extends MacroSettingStage implements ResourceHolder {

        private final MacroConfig config = getConfig();
        private final MacroConfig.QuickSwap qsConfig = config.getQuickSwap();
        private final MacroConfig.SwapGlitch sgConfig = config.getSwapGlitch();

//        private static final int FLAG_WITH_KEY_AND_MOUSE = I18nKeyChooser.FLAG_WITH_KEY  | I18nKeyChooser.FLAG_WITH_MOUSE;
//        private static final int FLAG_WITH_ALL = FLAG_WITH_KEY_AND_MOUSE | I18nKeyChooser.FLAG_WITH_WHEEL_SCROLL;

//        private final ToggleSwitch mapping1Toggle = new ToggleSwitch();  /* 监听远程武器映射1 */
//        private final I18nKeyChooseButton mapping1SourceKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射1主键 */
//        private final I18nKeyChooseButton mapping1TargetKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射1目标键 */
//
//        private final ToggleSwitch mapping2Toggle = new ToggleSwitch();  /* 监听远程武器映射1 */
//        private final I18nKeyChooseButton mapping2SourceKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射1主键 */
//        private final I18nKeyChooseButton mapping2TargetKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射1目标键 */
//
//        private final ToggleSwitch mapping3Toggle = new ToggleSwitch();  /* 监听远程武器映射1 */
//        private final I18nKeyChooseButton mapping3SourceKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射1主键 */
//        private final I18nKeyChooseButton mapping3TargetKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射1目标键 */
//
//        private final ToggleSwitch mapping4Toggle = new ToggleSwitch();  /* 监听远程武器映射4 */
//        private final I18nKeyChooseButton mapping4SourceKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射1主键 */
//        private final I18nKeyChooseButton mapping4TargetKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射1目标键 */
//
//        private final ToggleSwitch mapping5Toggle = new ToggleSwitch();  /* 监听远程武器映射5 */
//        private final I18nKeyChooseButton mapping5SourceKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射5主键 */
//        private final I18nKeyChooseButton mapping5TargetKeyBtn = new I18nKeyChooseButton(i18nContext, I18nKeyChooser.FLAG_WITH_KEY);  /* 映射5目标键 */

//        private final ToggleSwitch enableBlockKeyToggle = new ToggleSwitch();  /* 启用屏蔽键 */

        private final BooleanProperty enableBlockKey = new SimpleBooleanProperty();  /* 启用屏蔽键 */
        private final ObjectProperty<Key> blockKey = new SimpleObjectProperty<>();  /* 屏蔽键键位 */
        private final DoubleProperty blockDuration = new SimpleDoubleProperty();

//        private final I18nKeyChooseButton blockKeyBtn = new I18nKeyChooseButton(i18nContext, FLAG_WITH_ALL);  /* 屏蔽键 */
//        private final ForkedSlider blockDurationSlider = new ForkedSlider() {{  /* 屏蔽有效时间 */
//            setLength(300);
//            setRange(0, 1000);
//        }};

        /* 按键映射设置 */
        private static class KeyMapSetting {
            public final BooleanProperty enabled = new SimpleBooleanProperty();  /* 是否启用 */
            public final ObjectProperty<Key> sourceKey = new SimpleObjectProperty<>();  /* 源键 */
            public final ObjectProperty<Key> targetKey = new SimpleObjectProperty<>();  /* 映射键 */
        }

        private final ObservableList<KeyMapSetting> keyMapSettings = FXCollections.observableArrayList();

        public QSSettingStage(I18nContext i18nContext, I18nText introTitle) {
            super(i18nContext, introTitle);
            this.content = ConfigContentBuilder.defaultLine(i18nContext)
                    .dividerRow(i -> i.quickSwap.baseSetting.title)
                    .listConfigEditorRow(i -> i.quickSwap.baseSetting.keyMapListEditor, i -> i.quickSwap.baseSetting.keyMapNotFount,
                            keyMapSettings,
                            k -> ConfigContentBuilder.defaultRow(i18nContext).spacing(10)
                                    .line().alignment(Pos.CENTER)
                                    .themeLabel().text(I18nText.of(i -> i.quickSwap.baseSetting.keyMapIntro, keyMapSettings.indexOf(k) + 1)).buildForContent()
                                    .buildForParent()
                                    .hSpacer()
                                    .keyChooseButton(k.sourceKey, true)
                                    .keyChooseButton(k.targetKey, true)
                                    .toggleButton().bindSelected(k.enabled).buildForContent()
                                    .build(), KeyMapSetting::new)
//                    .buttonToggle(i -> MessageFormat.format(i.quickSwap.baseSetting.quickSwapMapping, 1), mapping1Toggle, mapping1SourceKeyBtn, mapping1TargetKeyBtn)
//                    .buttonToggle(i -> MessageFormat.format(i.quickSwap.baseSetting.quickSwapMapping, 2), mapping2Toggle, mapping2SourceKeyBtn, mapping2TargetKeyBtn)
//                    .buttonToggle(i -> MessageFormat.format(i.quickSwap.baseSetting.quickSwapMapping, 3), mapping3Toggle, mapping3SourceKeyBtn, mapping3TargetKeyBtn)
//                    .buttonToggle(i -> MessageFormat.format(i.quickSwap.baseSetting.quickSwapMapping, 4), mapping4Toggle, mapping4SourceKeyBtn, mapping4TargetKeyBtn)
//                    .buttonToggle(i -> MessageFormat.format(i.quickSwap.baseSetting.quickSwapMapping, 5), mapping5Toggle, mapping5SourceKeyBtn, mapping5TargetKeyBtn)
                    .toggleButtonRow(i -> i.quickSwap.baseSetting.enableBlockKey, enableBlockKey)
                    .keyChooseButtonRow(i -> i.quickSwap.baseSetting.blockKey, blockKey)
                    .sliderRow(i -> i.quickSwap.baseSetting.blockDuration, 300, 0, 1000, blockDuration)
                    .build();
        }

        @Override
        public void onVSettingStageInit() {
//            mapping1Toggle.selectedProperty().set(qsConfig.baseSetting.enableMapping1);
//            mapping1SourceKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping1SourceKey);
//            mapping1TargetKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping1TargetKey);
//
//            mapping2Toggle.selectedProperty().set(qsConfig.baseSetting.enableMapping2);
//            mapping2SourceKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping2SourceKey);
//            mapping2TargetKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping2TargetKey);
//
//            mapping3Toggle.selectedProperty().set(qsConfig.baseSetting.enableMapping3);
//            mapping3SourceKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping3SourceKey);
//            mapping3TargetKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping3TargetKey);
//
//            mapping4Toggle.selectedProperty().set(qsConfig.baseSetting.enableMapping4);
//            mapping4SourceKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping4SourceKey);
//            mapping4TargetKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping4TargetKey);
//
//            mapping5Toggle.selectedProperty().set(qsConfig.baseSetting.enableMapping5);
//            mapping5SourceKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping5SourceKey);
//            mapping5TargetKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.mapping5TargetKey);

            enableBlockKey.set(qsConfig.baseSetting.enableBlockKey);
            blockKey.set(qsConfig.baseSetting.blockKey);
            blockDuration.set(qsConfig.baseSetting.blockDuration);
        }

        @Override
        public void onVSettingStageExit() {
//            MacroConfig.SwapGlitch.SwapRangedSetting srSetting = sgConfig.swapRangedSetting;
            MacroConfig.QuickSwap.BaseSetting bSetting = qsConfig.baseSetting;

//            bSetting.enableMapping1 = mapping1Toggle.selectedProperty().get();
//            srSetting.mapping1SourceKey = mapping1SourceKeyBtn.keyProperty().get();
//            srSetting.mapping1TargetKey = mapping1TargetKeyBtn.keyProperty().get();
//
//            bSetting.enableMapping2 = mapping2Toggle.selectedProperty().get();
//            srSetting.mapping2SourceKey = mapping2SourceKeyBtn.keyProperty().get();
//            srSetting.mapping2TargetKey = mapping2TargetKeyBtn.keyProperty().get();
//
//            bSetting.enableMapping3 = mapping3Toggle.selectedProperty().get();
//            srSetting.mapping3SourceKey = mapping3SourceKeyBtn.keyProperty().get();
//            srSetting.mapping3TargetKey = mapping3TargetKeyBtn.keyProperty().get();
//
//            bSetting.enableMapping4 = mapping4Toggle.selectedProperty().get();
//            srSetting.mapping4SourceKey = mapping4SourceKeyBtn.keyProperty().get();
//            srSetting.mapping4TargetKey = mapping4TargetKeyBtn.keyProperty().get();
//
//            bSetting.enableMapping5 = mapping5Toggle.selectedProperty().get();
//            srSetting.mapping5SourceKey = mapping5SourceKeyBtn.keyProperty().get();
//            srSetting.mapping5TargetKey = mapping5TargetKeyBtn.keyProperty().get();

            bSetting.enableBlockKey = enableBlockKey.get();
            bSetting.blockKey = blockKey.get();
            bSetting.blockDuration = blockDuration.get();
        }
    }
}
