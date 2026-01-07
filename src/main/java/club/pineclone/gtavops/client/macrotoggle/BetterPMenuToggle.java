package club.pineclone.gtavops.client.macrotoggle;

import club.pineclone.gtavops.common.ResourceHolder;
import club.pineclone.gtavops.common.SessionType;
import club.pineclone.gtavops.config.MacroConfig;
import club.pineclone.gtavops.config.MacroConfigLoader;
import club.pineclone.gtavops.client.component.VOptionButton;
import club.pineclone.gtavops.client.component.I18nKeyChooseButton;
import club.pineclone.gtavops.client.forked.I18nKeyChooser;
import club.pineclone.gtavops.client.forked.ForkedSlider;
import club.pineclone.gtavops.client.i18n.ExtendedI18n;
import club.pineclone.gtavops.macro.MacroCreationStrategies;
import club.pineclone.gtavops.macro.MacroRegistry;
import io.vproxy.vfx.ui.toggle.ToggleSwitch;
import javafx.beans.property.ObjectProperty;

import java.util.List;
import java.util.UUID;

/* 额外功能，例如快速切换战局，快速进入某个夺取 */
public class BetterPMenuToggle extends MacroToggle {

    private UUID joinANewSessionMacroId;  /* 快速加入新战局 */
    private UUID joinABookmarkedJobMacroId;  /* 加入已收藏差事 */

    public BetterPMenuToggle(ObjectProperty<ExtendedI18n> i18n) {
        super(i18n, i -> i.betterPMenu.title, BPMSettingStage::new);
    }

    @Override
    protected void onFeatureEnable() {
        MacroConfig.BetterPMenu bpmconfig = MacroConfigLoader.get().betterPMenu;
        /* 快速加入新战局 */
        if (bpmconfig.joinANewSession.enable) {
            joinANewSessionMacroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.JOIN_A_NEW_SESSION_MACRO_CREATION_STRATEGY);
            MacroRegistry.getInstance().launchMacro(joinANewSessionMacroId);
        }
        /* 加入已收藏差事 */
        if (bpmconfig.joinABookmarkedJob.enable) {
            joinABookmarkedJobMacroId = MacroRegistry.getInstance().register(MacroConfigLoader.get(), MacroCreationStrategies.JOIN_A_BOOKMARKED_JOB_MACRO_CREATION_STRATEGY);;
            MacroRegistry.getInstance().launchMacro(joinABookmarkedJobMacroId);
        }
    }

    @Override
    protected void onFeatureDisable() {
        MacroRegistry.getInstance().terminateMacro(joinANewSessionMacroId);
        MacroRegistry.getInstance().terminateMacro(joinABookmarkedJobMacroId);
    }

    @Override
    public void onUIInit() {
        selectedProperty().set(MacroConfigLoader.get().betterPMenu.baseSetting.enable);
    }

    @Override
    public void onUIDispose() {
        MacroConfigLoader.get().betterPMenu.baseSetting.enable = selectedProperty().get();
    }

    private static class BPMSettingStage
            extends MacroSettingStage
            implements ResourceHolder {

        private final MacroConfig config = getConfig();
        private final MacroConfig.BetterPMenu bpmConfig = config.betterPMenu;

        private final ForkedSlider mouseScrollIntervalSlider = new ForkedSlider() {{
            setLength(200);
            setRange(10, 200);
        }};
        private final ForkedSlider enterKeyIntervalSlider = new ForkedSlider() {{
            setLength(300);
            setRange(10, 500);
        }};

        private final ForkedSlider timeUtilPMenuLoadedSlider = new ForkedSlider() {{
            setLength(300);
            setRange(10, 3000);
        }};

        /* Join A New Session */
        private final ToggleSwitch enableJoinANewSessionToggle = new ToggleSwitch();
        private final I18nKeyChooseButton joinANewSessionActivateKey = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);

        private final VOptionButton<SessionType> joinANewSessionTypeOption = new VOptionButton<>(List.of(
                new VOptionButton.I18nOptionItem<>(SessionType.PUBLIC_SESSION, i18n, i -> i.inGame.publicSession),
                new VOptionButton.I18nOptionItem<>(SessionType.INVITE_ONLY_SESSION, i18n, i -> i.inGame.inviteOnlySession),
                new VOptionButton.I18nOptionItem<>(SessionType.CREW_SESSION, i18n, i -> i.inGame.crewSession),
                new VOptionButton.I18nOptionItem<>(SessionType.INVITE_ONLY_CREW_SESSION, i18n, i -> i.inGame.inviteOnlyCrewSession),
                new VOptionButton.I18nOptionItem<>(SessionType.INVITE_ONLY_FRIENDS_SESSION, i18n, i -> i.inGame.inviteOnlyFriendsSession)
        ));

        /* Join A Bookmarked Job */
        private final ToggleSwitch enableJoinABookmarkedJobToggle = new ToggleSwitch();
        private final I18nKeyChooseButton joinABookmarkedJobActivateKey = new I18nKeyChooseButton(i18n, I18nKeyChooser.FLAG_WITH_KEY);
        private final ForkedSlider timeUtilJobsLoadedSlider = new ForkedSlider() {{
            setLength(300);
            setRange(500, 4000);
        }};

        public BPMSettingStage(ObjectProperty<ExtendedI18n> i18n) {
            super(i18n, i -> i.betterPMenu.title);
            getContent().getChildren().addAll(contentBuilder()
                    .divide(i -> i.betterPMenu.baseSetting.title)
                    .slider(i -> i.betterPMenu.baseSetting.mouseScrollInterval, mouseScrollIntervalSlider)
                    .slider(i -> i.betterPMenu.baseSetting.enterKeyInterval, enterKeyIntervalSlider)
                    .slider(i -> i.betterPMenu.baseSetting.timeUtilPMenuLoaded, timeUtilPMenuLoadedSlider)
                    .divide(i -> i.betterPMenu.joinANewSession.title)
                    .toggle(i -> i.betterPMenu.joinANewSession.enable, enableJoinANewSessionToggle)
                    .button(i -> i.betterPMenu.joinANewSession.activateKey, joinANewSessionActivateKey)
                    .button(i -> i.betterPMenu.joinANewSession.sessionType, joinANewSessionTypeOption)
                    .divide(i -> i.betterPMenu.joinABookmarkedJob.title)
                    .toggle(i -> i.betterPMenu.joinABookmarkedJob.enable, enableJoinABookmarkedJobToggle)
                    .button(i -> i.betterPMenu.joinABookmarkedJob.activateKey, joinABookmarkedJobActivateKey)
                    .slider(i -> i.betterPMenu.joinABookmarkedJob.timeUtilJobsLoaded, timeUtilJobsLoadedSlider)
                    .build());
        }

        @Override
        public void onVSettingStageInit() {
            mouseScrollIntervalSlider.setValue(bpmConfig.baseSetting.mouseScrollInterval);
            enterKeyIntervalSlider.setValue(bpmConfig.baseSetting.enterKeyInterval);
            timeUtilPMenuLoadedSlider.setValue(bpmConfig.baseSetting.timeUtilPMenuLoaded);

            enableJoinANewSessionToggle.selectedProperty().set(bpmConfig.joinANewSession.enable);
            joinANewSessionActivateKey.keyProperty().set(bpmConfig.joinANewSession.activateKey);
            joinANewSessionTypeOption.setOption(bpmConfig.joinANewSession.sessionType);

            enableJoinABookmarkedJobToggle.selectedProperty().set(bpmConfig.joinABookmarkedJob.enable);
            joinABookmarkedJobActivateKey.keyProperty().set(bpmConfig.joinABookmarkedJob.activateKey);
            timeUtilJobsLoadedSlider.setValue(bpmConfig.joinABookmarkedJob.timeUtilJobsLoaded);
        }

        @Override
        public void onVSettingStageExit() {
            bpmConfig.baseSetting.mouseScrollInterval = mouseScrollIntervalSlider.valueProperty().get();
            bpmConfig.baseSetting.enterKeyInterval = enterKeyIntervalSlider.valueProperty().get();
            bpmConfig.baseSetting.timeUtilPMenuLoaded = timeUtilPMenuLoadedSlider.valueProperty().get();

            bpmConfig.joinANewSession.enable = enableJoinANewSessionToggle.selectedProperty().get();
            bpmConfig.joinANewSession.activateKey = joinANewSessionActivateKey.keyProperty().get();
            bpmConfig.joinANewSession.sessionType = joinANewSessionTypeOption.getOption();

            bpmConfig.joinABookmarkedJob.enable = enableJoinABookmarkedJobToggle.selectedProperty().get();
            bpmConfig.joinABookmarkedJob.activateKey = joinABookmarkedJobActivateKey.keyProperty().get();
            bpmConfig.joinABookmarkedJob.timeUtilJobsLoaded = timeUtilJobsLoadedSlider.valueProperty().get();
        }
    }
}
