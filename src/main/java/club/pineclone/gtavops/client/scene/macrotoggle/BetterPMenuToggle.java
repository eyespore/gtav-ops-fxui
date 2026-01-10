package club.pineclone.gtavops.client.scene.macrotoggle;

import club.pineclone.gtavops.client.i18n.I18nContext;
import club.pineclone.gtavops.client.i18n.I18nText;
import club.pineclone.gtavops.client.utils.ConfigContentBuilder;
import club.pineclone.gtavops.common.ResourceHolder;
import club.pineclone.gtavops.common.SessionType;
import club.pineclone.gtavops.config.MacroConfig;
import club.pineclone.gtavops.config.MacroConfigLoader;
import club.pineclone.gtavops.macro.MacroCreationStrategies;
import club.pineclone.gtavops.macro.MacroRegistry;
import io.vproxy.vfx.entity.input.Key;
import javafx.beans.property.*;

import java.util.LinkedHashMap;
import java.util.UUID;

/* 额外功能，例如快速切换战局，快速进入某个夺取 */
public class BetterPMenuToggle extends MacroToggle {

    private UUID joinANewSessionMacroId;  /* 快速加入新战局 */
    private UUID joinABookmarkedJobMacroId;  /* 加入已收藏差事 */

    public BetterPMenuToggle(I18nContext i18nContext) {
        super(i18nContext, I18nText.of(i -> i.betterPMenu.title), BPMSettingStage::new);
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

    private static class BPMSettingStage extends MacroSettingStage implements ResourceHolder {

        private final MacroConfig config = getConfig();
        private final MacroConfig.BetterPMenu bpmConfig = config.betterPMenu;

        private final DoubleProperty mouseScrollInterval = new SimpleDoubleProperty();
        private final DoubleProperty enterKeyInterval = new SimpleDoubleProperty();
        private final DoubleProperty timeUtilPMenuLoaded = new SimpleDoubleProperty();

        /* Join A New Session */
        private final BooleanProperty enableJoinANewSession = new SimpleBooleanProperty();
        private final ObjectProperty<Key> joinANewSessionActivateKey = new SimpleObjectProperty<>();
        private final ObjectProperty<SessionType> joinANewSessionType = new SimpleObjectProperty<>();

        /* Join A Bookmarked Job */
        private final BooleanProperty enableJoinABookmarkedJob = new SimpleBooleanProperty();
        private final ObjectProperty<Key> joinABookmarkedJobActivateKey = new SimpleObjectProperty<>();
        private final DoubleProperty timeUtilJobsLoaded = new SimpleDoubleProperty();

        public BPMSettingStage(I18nContext i18nContext, I18nText introTitle) {
            super(i18nContext, introTitle);
            this.content = ConfigContentBuilder.defaultLine(i18nContext)
                    .dividerRow(i -> i.betterPMenu.baseSetting.title)
                    .sliderRow(i -> i.betterPMenu.baseSetting.mouseScrollInterval, 200, 10, 200, mouseScrollInterval)
                    .sliderRow(i -> i.betterPMenu.baseSetting.enterKeyInterval, 300, 10, 500, enterKeyInterval)
                    .sliderRow(i -> i.betterPMenu.baseSetting.timeUtilPMenuLoaded, 300, 10, 3000, timeUtilPMenuLoaded)
                    .dividerRow(i -> i.betterPMenu.joinANewSession.title)
                    .toggleButtonRow(i -> i.betterPMenu.joinANewSession.enable, enableJoinANewSession)
                    .keyChooseButtonRow(i -> i.betterPMenu.joinANewSession.activateKey, joinANewSessionActivateKey)
                    .optionButtonRow(i -> i.betterPMenu.joinANewSession.sessionType, new LinkedHashMap<>() {{
                        put(SessionType.PUBLIC_SESSION, I18nText.of(i -> i.inGame.publicSession));
                        put(SessionType.INVITE_ONLY_SESSION, I18nText.of(i -> i.inGame.inviteOnlySession));
                        put(SessionType.CREW_SESSION, I18nText.of(i -> i.inGame.crewSession));
                        put(SessionType.INVITE_ONLY_CREW_SESSION, I18nText.of(i -> i.inGame.inviteOnlyCrewSession));
                        put(SessionType.INVITE_ONLY_FRIENDS_SESSION, I18nText.of(i -> i.inGame.inviteOnlyFriendsSession));
                    }}, joinANewSessionType)
                    .dividerRow(i -> i.betterPMenu.joinABookmarkedJob.title)
                    .toggleButtonRow(i -> i.betterPMenu.joinABookmarkedJob.enable, enableJoinABookmarkedJob)
                    .keyChooseButtonRow(i -> i.betterPMenu.joinABookmarkedJob.activateKey, joinABookmarkedJobActivateKey)
                    .sliderRow(i -> i.betterPMenu.joinABookmarkedJob.timeUtilJobsLoaded, 300, 500, 4000, timeUtilJobsLoaded)
                    .build();
        }

        @Override
        public void onVSettingStageInit() {
            mouseScrollInterval.set(bpmConfig.baseSetting.mouseScrollInterval);
            enterKeyInterval.set(bpmConfig.baseSetting.enterKeyInterval);
            timeUtilPMenuLoaded.set(bpmConfig.baseSetting.timeUtilPMenuLoaded);

            enableJoinANewSession.set(bpmConfig.joinANewSession.enable);
            joinANewSessionActivateKey.set(bpmConfig.joinANewSession.activateKey);
            joinANewSessionType.set(bpmConfig.joinANewSession.sessionType);

            enableJoinABookmarkedJob.set(bpmConfig.joinABookmarkedJob.enable);
            joinABookmarkedJobActivateKey.set(bpmConfig.joinABookmarkedJob.activateKey);
            timeUtilJobsLoaded.set(bpmConfig.joinABookmarkedJob.timeUtilJobsLoaded);
        }

        @Override
        public void onVSettingStageExit() {
            bpmConfig.baseSetting.mouseScrollInterval = mouseScrollInterval.get();
            bpmConfig.baseSetting.enterKeyInterval = enterKeyInterval.get();
            bpmConfig.baseSetting.timeUtilPMenuLoaded = timeUtilPMenuLoaded.get();

            bpmConfig.joinANewSession.enable = enableJoinANewSession.get();
            bpmConfig.joinANewSession.activateKey = joinANewSessionActivateKey.get();
            bpmConfig.joinANewSession.sessionType = joinANewSessionType.get();

            bpmConfig.joinABookmarkedJob.enable = enableJoinABookmarkedJob.get();
            bpmConfig.joinABookmarkedJob.activateKey = joinABookmarkedJobActivateKey.get();
            bpmConfig.joinABookmarkedJob.timeUtilJobsLoaded = timeUtilJobsLoaded.get();
        }
    }
}
