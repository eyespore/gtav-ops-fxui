package club.pineclone.gtavops.client.i18n;

import io.vproxy.vfx.manager.internal_i18n.InternalI18n;
import lombok.Data;

@Data
public class ExtendedI18n implements InternalI18n {

    public static final ExtendedI18n EMPTY = new ExtendedI18n();

    public String name = "name";  /* 本地化名称 */
    public String lang = "lang";  /* 本地化语言描述 */

    public InGame inGame = new InGame();  /* 游戏内名词 */
    public Common common = new Common();  /* 通用 */
    public VFXComponent vfxComponent = new VFXComponent();  /* VFX组件 */

    public SwapGlitch swapGlitch = new SwapGlitch();  /* 切枪偷速 */
    public RouletteSnake rouletteSnake = new RouletteSnake();  /* 轮盘零食 */
    public ADSwing adSwing = new ADSwing();  /* AD摇 */
    public MeleeGlitch meleeGlitch = new MeleeGlitch();  /* 近战武器偷速 */
    public BetterMMenu betterMMenu = new BetterMMenu();  /* 更好的 M 菜单 */
    public BetterLButton betterLButton = new BetterLButton();  /* 更好的鼠标左键 */
    public QuickSwap quickSwap = new QuickSwap();  /* 快速切枪 */
    public DelayClimb delayClimb = new DelayClimb();  /* 延迟攀 */
    public BetterPMenu betterPMenu = new BetterPMenu();  /* 更好的 P 菜单 */
    public AutoFire autoFire = new AutoFire();  /* 连发 RPG */

    public MacroToggleScene macroToggleScene = new MacroToggleScene();  /* 功能特性 */
    public IntroScene introScene = new IntroScene();  /* 主页 */
    public ConfigScene configScene = new ConfigScene();  /* 应用配置 */

    /* 通用 */
    @Data
    public static class Common {
        public String toggle = "common.toggle";
        public String hold = "common.hold";
        public String click = "common.click";

        public String confirm = "common.confirm";
        public String cancel = "common.cancel";
        public String unknown = "common.unknown";
        public String enabled = "common.enabled";
        public String disabled = "common.disabled";
        public String keyboard = "common.keyboard";
        public String mouseButton = "common.mouseButton";
        public String mouseWheel = "common.mouseWheel";
    }

    /* 游戏内名词 */
    @Data
    public static class InGame {
        public String legacy = "inGame.legacy";  /* 传承版 */
        public String enhanced = "inGame.Enhanced";  /* 增强版 */

        public String publicSession = "inGame.publicSession";  /* 公开战局 */
        public String inviteOnlySession = "inGame.inviteOnlySession";  /* 邀请战局 */
        public String crewSession = "inGame.crewSession";  /* 帮会战局 */
        public String inviteOnlyCrewSession = "inGame.inviteOnlyCrewSession";  /* 非公开帮会战局 */
        public String inviteOnlyFriendsSession = "inGame.inviteOnlyFriendsSession";  /* 非公开好友战局 */
    }

    /* 主页 */
    @Data
    public static class IntroScene {
        public String title = "introScene.title";
        public String header = "introScene.header";
        public String coreVersionLabel = "introScene.versionLabel";
        public String clientVersionLabel = "introScene.clientVersionLabel";
        public String acknowledgement = "introScene.acknowledgement";

        public String introNavigate = "introScene.introNavigate";
        public String featureNavigate = "introScene.featureNavigate";
        public String fontpackNavigate = "introScene.fontpackNavigate";
        public String configNavigate = "introScene.configNavigate";
    }

    /* 功能选项 */
    @Data
    public static class MacroToggleScene {
        public String title = "macroToggleScene.title";
        public String header = "macroToggleScene.header";
        public String gameVersion = "macroToggleScene.gameVersion";
    }

    /* 应用配置 */
    @Data
    public static class ConfigScene {
        public String title = "configScene.title";
        public String header = "configScene.header";

        public AppearanceSetting appearanceSetting = new AppearanceSetting();

        /* 外观设置 */
        public static class AppearanceSetting {
            public String title = "configScene.appearanceSetting.title";
            public String language = "configScene.appearanceSetting.language";  /* 语言设置 */
        }
    }

    /* 切枪偷速 */
    @Data
    public static class SwapGlitch {
        public String title = "swapGlitch.title";
        public BaseSetting baseSetting = new BaseSetting();
        public SwapMeleeSetting swapMeleeSetting = new SwapMeleeSetting();
        public SwapRangedSetting swapRangedSetting = new SwapRangedSetting();

        /* 基础设置 */
        @Data
        public static class BaseSetting {
            public String title = "swapGlitch.baseSetting.title";  /* 基础设置 */
            public String activateMethod = "swapGlitch.baseSetting.activateMethod";
            public String targetWeaponWheelKey = "swapGlitch.baseSetting.targetWeaponWheelKey";  /* 目标武器轮盘 */
            public String activateKey = "swapGlitch.baseSetting.activateKey";
            public String triggerInterval = "swapGlitch.baseSetting.triggerInterval";
        }

        /* 切换近战武器设置 */
        @Data
        public static class SwapMeleeSetting {
            public String title = "swapGlitch.swapMeleeSetting.title";
            public String enable = "swapGlitch.swapMeleeSetting.enable";
            public String postSwapMeleeDelay = "swapGlitch.swapMeleeSetting.postSwapMeleeDelay";
            public String meleeKey = "swapGlitch.swapMeleeSetting.meleeKey";  /* 近战武器键 */
        }

        /* 切换远程武器设置 */
        @Data
        public static class SwapRangedSetting {
            public String title = "swapGlitch.swapRangedSetting.title";
            public String enable = "swapGlitch.swapRangedSetting.enable";
            public String defaultRangedWeaponKey = "swapGlitch.swapRangedSetting.defaultRangedWeaponKey";
            public String listenRangedWeaponMapping = "swapGlitch.swapRangedSetting.listenRangedWeaponMapping";
            public String enableClearKey = "swapGlitch.swapRangedSetting.enableClearKey";  /* 启用清除键 */
            public String clearKey = "swapGlitch.swapRangedSetting.clearKey";  /* 清除键 */
        }
    }

    /* 近战武器偷速 */
    @Data
    public static class MeleeGlitch {
        public String title = "meleeGlitch.title";
        public BaseSetting baseSetting = new BaseSetting();

        public static class BaseSetting {
            public String title = "meleeGlitch.baseSetting.title";
            public String activateKey = "meleeGlitch.baseSetting.activateKey";
            public String activateMethod = "meleeGlitch.baseSetting.activateMethod";  /* 激活方式 */
            public String meleeSnakeScrollKey = "meleeGlitch.baseSetting.meleeSnakeScrollKey";  /* 近战零食滚轮键 */
            public String triggerInterval = "meleeGlitch.baseSetting.triggerInterval";
            public String safetyKey = "meleeGlitch.baseSetting.safetyKey";
            public String enableSafetyKey = "meleeGlitch.baseSetting.enableSafetyKey";
        }
    }

    /* 快速零食 */
    @Data
    public static class RouletteSnake {
        public String title = "rouletteSnake.title";
        public BaseSetting baseSetting = new BaseSetting();

        @Data
        public static class BaseSetting {
            public String title = "rouletteSnake.title";
            public String activateKey = "rouletteSnake.activateKey";
            public String triggerInterval = "rouletteSnake.triggerInterval";
            public String snakeKey = "rouletteSnake.snakeKey";
            public String weaponWheelKey = "rouletteSnake.weaponWheelKey";
        }
    }

    /* AD摇 */
    @Data
    public static class ADSwing {
        public String title = "adSwing.title";
        public BaseSetting baseSetting = new BaseSetting();

        @Data
        public static class BaseSetting {
            public String title = "adSwing.baseSettings.title";
            public String activateKey = "adSwing.baseSettings.activateKey";
            public String activateMethod = "adSwing.baseSettings.activateMethod";
            public String triggerInterval = "adSwing.baseSettings.triggerInterval";
            public String moveLeftKey = "adSwing.baseSettings.moveLeftKey";
            public String moveRightKey = "adSwing.baseSettings.moveRightKey";
            public String safetyKey = "adSwing.baseSettings.safetyKey";
            public String enableSafetyKey = "adSwing.baseSettings.enableSafetyKey";
        }
    }

    /* 更好的 M 菜单 */
    @Data
    public static class BetterMMenu {
        public String title = "betterMMenu.title";
        public BaseSetting baseSetting = new BaseSetting();
        public StartEngine startEngine = new StartEngine();
        public AutoSnake autoSnake = new AutoSnake();

        public static class BaseSetting {
            public String title = "betterMMenu.baseSetting.title";
            public String menuKey = "betterMMenu.baseSetting.menukey";  /* 互动菜单键 */
            public String mouseScrollInterval = "betterMMenu.baseSetting.mouseScrollInterval";  /* 方向键之后的等待时间 */
            public String keyPressInterval = "betterMMenu.baseSetting.keyPressInterval";  /* 回车之后的等待时间 */
            public String timeUtilMMenuLoaded = "betterMMenu.baseSetting.timeUtilMMenuLoaded";  /* 等待M菜单加载时间 */
        }

        public static class StartEngine {
            public String title = "betterMMenu.startEngine.title";
            public String enableStartEngine = "betterMMenu.startEngine.enableStartEngine";
            public String activateKey = "betterMMenu.startEngine.activateKey";  /* 快速点火 */
            public String enableDoubleClickToOpenDoor = "betterMMenu.startEngine.enableDoubleClickToOpenDoor";  /* 是否打开车门 */
            public String doubleClickInterval = "betterMMenu.startEngine.doubleClickInterval";  /* 双击间隔 */
        }

        /* 自动零食 */
        public static class AutoSnake {
            public String title = "betterMMenu.autoSnake.title";
            public String enableAutoSnake = "betterMMenu.autoSnake.enableAutoSnake";
            public String activateKey = "betterMMenu.autoSnake.activateKey"; /* 激活键位 */
            public String refillVest = "betterMMenu.autoSnake.refillVest";
            public String keepMMenu = "betterMMenu.autoSnake.keepMMenu";  /* 是否保留 M 菜单 */
        }
    }

    @Data
    public static class BetterLButton {
        public String title = "betterLButton.title";
        public HoldLButtonSetting holdLButtonSetting = new HoldLButtonSetting();
        public RapidlyClickLButtonSetting rapidlyClickLButtonSetting = new RapidlyClickLButtonSetting();
        public RemapLButtonSetting remapLButtonSetting = new RemapLButtonSetting();

        public static class HoldLButtonSetting {
            public String title = "betterLButton.holdLButtonSetting.title";
            public String enable = "betterLButton.holdLButtonSetting.enable";
            public String activateMethod = "betterLButton.holdLButtonSetting.activateMethod";
            public String activateKey =  "betterLButton.holdLButtonSetting.activateKey";
        }

        public static class RapidlyClickLButtonSetting {
            public String title = "betterLButton.rapidlyClickLButtonSetting.title";
            public String enable = "betterLButton.rapidlyClickLButtonSetting.enable";
            public String activateMethod = "betterLButton.rapidlyClickLButtonSetting.activateMethod";
            public String activateKey =  "betterLButton.rapidlyClickLButtonSetting.activateKey";
            public String triggerInterval = "betterLButton.rapidlyClickLButtonSetting.triggerInterval";
        }

        public static class RemapLButtonSetting {
            public String title = "betterLButton.remapLButtonSetting.title";
            public String enable = "betterLButton.remapLButtonSetting.enable";
            public String activateKey = "betterLButton.remapLButtonSetting.activateKey";
        }
    }

    /* 快速切枪 */
    public static class QuickSwap {
        public String title = "quickSwap.title";
        public BaseSetting baseSetting = new BaseSetting();

        public static class BaseSetting {
            public String title = "quickSwap.baseSetting.title";
            public String quickSwapMapping = "quickSwap.baseSetting.quickSwapMapping";
            public String enableBlockKey = "quickSwap.baseSetting.enableBlockKey";
            public String blockKey = "quickSwap.baseSetting.blockKey";
            public String blockDuration = "quickSwap.baseSetting.blockDuration";
        }
    }

    /* 延迟攀爬 */
    @Data
    public static class DelayClimb {
        public String title = "delayClimb.title";
        public BaseSetting baseSetting = new BaseSetting();

        public static class BaseSetting {
            public String title = "delayClimb.baseSetting.title";
            public String toggleDelayClimbKey = "delayClimb.baseSetting.toggleDelayClimbKey";
            public String hideInCoverKey = "delayClimb.baseSetting.hideInCoverKey";  /* 躲入掩体按键 */
            public String triggerInterval = "delayClimb.baseSetting.triggerInterval";  /* 启用相机-关闭相机之间的间隔 */
            public String timeUtilCameraExited = "delayClimb.baseSetting.timeUtilCameraExited";  /* 相机退出等待时间1 */
            public String timeUtilCameraLoaded1 = "delayClimb.baseSetting.timeUtilCameraLoaded1";  /* 相机退出等待时间2 */
            public String timeUtilCameraLoaded2 = "delayClimb.baseSetting.timeUtilCameraLoaded2";  /* 相机退出等待时间2 */
            public String usePhoneKey = "delayClimb.baseSetting.usePhoneKey";  /* 使用手机键 */
            public String hideInCoverOnExit = "delayClimb.baseSetting.hideInCoverOnExit";  /* 在结束时躲入掩体 */
        }
    }

    /* 额外功能 */
    @Data
    public static class BetterPMenu {
        public String title = "betterPMenu.title";
        public BaseSetting baseSetting = new BaseSetting();
        public JoinANewSession joinANewSession = new JoinANewSession();
        public JoinABookmarkedJob joinABookmarkedJob = new JoinABookmarkedJob();

        public static class BaseSetting {
            public String title = "betterPMenu.baseSetting.title";
            public String mouseScrollInterval = "betterPMenu.baseSetting.mouseScrollInterval";  /* 方向键之后的等待时间 */
            public String enterKeyInterval = "betterPMenu.baseSetting.enterKeyInterval";  /* 回车之后的等待时间 */
            public String timeUtilPMenuLoaded = "betterPMenu.baseSetting.timeUtilPMenuLoaded";  /* 等待P菜单加载时间 */
        }

        public static class JoinANewSession {
            public String title = "betterPMenu.joinANewSession.title";
            public String enable = "betterPMenu.joinANewSession.enable";
            public String activateKey = "betterPMenu.joinANewSession.activateKey";
            public String sessionType = "betterPMenu.joinANewSession.sessionType";
        }

        /* 加入一个已收藏的差事 */
        public static class JoinABookmarkedJob {
            public String title = "betterPMenu.joinABookmarkedJob.title";
            public String enable = "betterPMenu.joinABookmarkedJob.enable";
            public String activateKey = "betterPMenu.joinABookmarkedJob.activateKey";
            public String timeUtilJobsLoaded = "betterPMenu.joinABookmarkedJob.timeUtilJobsLoaded";
        }
    }

    /* 武器连发 */
    @Data
    public static class AutoFire {
        public String title = "autoFire.title";
        public BaseSetting baseSetting = new BaseSetting();

        public static class BaseSetting {
            public String title = "autoFire.baseSetting.title";
            public String activateMethod = "autoFire.baseSetting.activateMethod";
            public String activateKey = "autoFire.baseSetting.activateKey";
            public String heavyWeaponKey = "autoFire.baseSetting.heavyWeaponKey";  // 重型武器键
            public String specialWeaponKey = "autoFire.baseSetting.specialWeaponKey";  // 特殊武器键
            public String triggerInterval = "autoFire.baseSetting.triggerInterval";  /* 触发间隔 */
            public String mousePressInterval = "autoFire.baseSetting.mousePressInterval";  /* 鼠标按住间隔 */
        }
    }

    /* VFX UI 组件 */
    public static class VFXComponent {
        public KeyChooser keyChooser = new KeyChooser();

        public static class KeyChooser {
            public String description = "vfxComponent.keyChooser.description";
            public String forwardMouseButton = "vfxComponent.keyChooser.forwardMouseButton";  /* 鼠标前侧键 */
            public String backwardMouseButton = "vfxComponent.keyChooser.backwardMouseButton";  /* 鼠标后侧键 */
            public String primaryMouseButton = "vfxComponent.keyChooser.primaryMouseButton";  /* 鼠标左键 */
            public String secondaryMouseButton = "vfxComponent.keyChooser.secondaryMouseButton";  /* 鼠标右键 */
            public String middleMouseButton = "vfxComponent.keyChooser.middleMouseButton";  /* 鼠标中键 */
            public String cancel = "vfxComponent.keyChooser.cancel";
            public String unset = "vfxComponent.keyChooser.unset";  /* 未设置 */
            public String mouseScrollUp = "vfxComponent.keyChooser.mouseScrollUp";  /* 上滚轮 */
            public String mouseScrollDown = "vfxComponent.keyChooser.mouseScrollDown";  /* 下滚轮 */
        }
    }

    public String configFileLoadFailed = "configFileLoadFailed";
    public String configStillLoadFailed = "configStillLoadFailed";
    public String duplicatedAppInstanceRunning = "duplicatedAppInstanceRunning";

    public String stacktraceAlertHeaderText = "stacktraceAlertHeaderText";
    public String stacktraceAlertLabel = "stacktraceAlertLabel";
    public String stacktraceAlertTitle = "stacktraceAlertTitle";

    @Override
    public String keyChooserLeftMouseButton() {
        return "keyChooserLeftMouseButton";
    }

    @Override
    public String keyChooserMiddleMouseButton() {
        return "keyChooserMiddleMouseButton";
    }

    @Override
    public String keyChooserRightMouseButton() {
        return "keyChooserRightMouseButton";
    }

    @Override
    public String stacktraceAlertHeaderText() {
        return stacktraceAlertHeaderText;
    }

    @Override
    public String stacktraceAlertTitle() {
        return stacktraceAlertTitle;
    }

    @Override
    public String stacktraceAlertLabel() {
        return stacktraceAlertLabel;
    }
}
