package club.pineclone.gtavops.client;

import club.pineclone.gtavops.AppContext;
import club.pineclone.gtavops.client.config.AppConfig;
import club.pineclone.gtavops.client.config.ClientConfig;
import club.pineclone.gtavops.client.config.ClientConfigLoader;
import club.pineclone.gtavops.client.i18n.I18nContext;
import club.pineclone.gtavops.client.i18n.I18nText;
import club.pineclone.gtavops.client.theme.DimTheme;
import club.pineclone.gtavops.common.JNativeHookManager;
import club.pineclone.gtavops.client.utils.SingletonLock;
import club.pineclone.gtavops.client.scene.*;
import club.pineclone.gtavops.client.i18n.I18nLoader;
import club.pineclone.gtavops.client.utils.ImageUtils;
import club.pineclone.gtavops.common.PathUtils;
import io.vproxy.vfx.manager.task.TaskManager;
import io.vproxy.vfx.theme.Theme;
import io.vproxy.vfx.ui.alert.StackTraceAlert;
import io.vproxy.vfx.ui.button.FusionButton;
import io.vproxy.vfx.ui.button.FusionImageButton;
import io.vproxy.vfx.ui.layout.HPadding;
import io.vproxy.vfx.ui.layout.VPadding;
import io.vproxy.vfx.ui.pane.FusionPane;
import io.vproxy.vfx.ui.scene.*;
import io.vproxy.vfx.ui.stage.VStage;
import io.vproxy.vfx.ui.stage.VStageInitParams;
import io.vproxy.vfx.util.FXUtils;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.nio.file.Files;
import java.util.*;

/**
 * 主窗口
 */
@Slf4j
public class MainFX extends Application {

    private final AppContext appContext;  /* 应用上下文，宏内核 */
    private final AppConfig appConfig;  /* 前端配置 */
    private final I18nContext i18nContext;
    private final ObjectProperty<ClientConfig> clientConfig;  /* 客户端配置 */
    private final ClientConfigLoader clientConfigLoader;
    private final SingletonLock singletonLock;

    public static final String APPLICATION_TITLE = "GTAV Ops";  /* 应用基础信息 */
    private static final Set<UILifecycleAware> uiListeners = new HashSet<>();

    private Exception exception;  /* 初始化阶段异常 */

    private final List<SceneTemplate> mainScenes = new ArrayList<>();
    private VSceneGroup sceneGroup;

    private FusionPane navigatePane;
    private List<FusionButton> navigatorButtons;
    private volatile boolean isSwitch = false;
    private volatile int targetIndex;

    public MainFX() {
        appContext = AppContext.getInstance();  /* 宏内核 */

        appConfig = new AppConfig(PathUtils.getAppHomePath());  /* 前端配置 */
        i18nContext = new I18nContext();  /* I18n上下文 */
        clientConfig = new SimpleObjectProperty<>();  /* 配置实例 */

        singletonLock = new SingletonLock(appConfig.getSingletonLockPath());  /* 单例锁 */
        clientConfigLoader = new ClientConfigLoader(appConfig.getClientConfigPath());  /* 客户端配置加载器 */
    }

    @Override
    public void init() throws Exception {
        MDC.put("module", "[Client]");  /* 初始化日志 */
        try {
            log.info("Launching macro core");
            appContext.init();  /* 后端启动 - 初始化宏内核 */
            appContext.start();  /* 启动宏内核 */

            log.info("Initializing fxui app home directory");
            Files.createDirectories(appConfig.getClientHomePath());  /* 初始化客户端应用家目录 */

            log.info("Obtaining app singleton lock");
            if (!singletonLock.lockInstance()) {  /* 程序已经在运行，抛出异常并返回 */
                exception = new RuntimeException("Duplicated GTAV Ops Instance");
                return;
            }

            log.info("Loading client configuration");
            clientConfig.set(clientConfigLoader.load());  /* 加载客户端配置 */

            log.info("Scanning and loading i18n configuration");
            I18nLoader.getInstance().init();  /* 扫描本地化，并基于客户端配置初始化本地化 */
            i18nContext.set(I18nLoader.getInstance().load(clientConfig.get().lang));  /* 加载默认本地化到上下文 */

            log.info("Register jnativehook global native hook for fxui");
            JNativeHookManager.register(MainFX.class);  /* 注册全局钩子 */

        } catch (Exception e) {
            exception = e;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (exception != null) {  /* start 阶段 UI 可以被正确渲染，处理先前的异常 */
            StackTraceAlert.showAndWait(exception);  /* 异常存在，向用户打印警告信息后退出程序 */
            System.exit(1);
            return;
        }

        VStage vStage = new VStage(new VStageInitParams().setResizable(true).setMaximizeAndResetButton(false)) {
            @Override
            public void close() {
                super.close();
                TaskManager.get().terminate();
            }
        };
        vStage.getInitialScene().enableAutoContentWidthHeight();
        vStage.setTitle(APPLICATION_TITLE);

        /* 初始化导航栏按钮 */
        navigatePane = new FusionPane();
        navigatePane.getNode().setPrefHeight(60);
        FXUtils.observeWidth(vStage.getInitialScene().getContentPane(), navigatePane.getNode(), -20);
        navigatorButtons = new ArrayList<>() {{
            add(createNavigateButton(I18nText.of(i -> i.introScene.introNavigate), 0));
            add(createNavigateButton(I18nText.of(i -> i.introScene.featureNavigate), 1));
            add(createNavigateButton(I18nText.of(i -> i.introScene.configNavigate), 2));
//            add(createNavigateButton(i -> i.introScene.fontpackNavigate, 3));
        }};

        navigatorButtons.get(0).setDisable(true);  /* 默认页禁用 */
        navigatePane.getContentPane().getChildren().addAll(navigatorButtons);
        navigatePane.getContentPane().widthProperty().addListener((ob, old, now) -> {
            if (now == null) return;
            double totalWidth = 0;
            double spacing = FusionPane.PADDING_H;

            // 计算总宽度：按钮总宽度 + 间隔总宽度
            for (FusionButton btn : navigatorButtons) {
                totalWidth += btn.getPrefWidth();
            }
            totalWidth += spacing * (navigatorButtons.size() - 1);

            double x = now.doubleValue() / 2 - totalWidth / 2;

            for (FusionButton btn : navigatorButtons) {
                btn.setLayoutX(x);
                x += btn.getPrefWidth() + spacing;
            }
        });

        /* 注册所有场景 */
        mainScenes.add(new IntroScene(i18nContext));
        mainScenes.add(new MacroToggleScene(i18nContext));
        mainScenes.add(new ConfigScene(i18nContext, clientConfig));
//        mainScenes.add(new FontPackScene(i18n));
//        mainScenes.add(new FontPackScene(oldI18n));

        mainScenes.forEach(s -> {
            addUIListener(s);  /* 正向注册 */
            s.readyProperty().addListener((obs, oldVal, newVal) -> {
                isSwitch = false;
                for (int i = 0; i < navigatorButtons.size(); i++) {
                    navigatorButtons.get(i).setDisable(i == targetIndex);
                }
            });  /* 反向注册 */
        });
        uiListeners.forEach(UILifecycleAware::onUIInit);  /* 调用初始化逻辑 */

        /* 初始场景 */
        var initialScene = mainScenes.get(0);
        sceneGroup = new VSceneGroup(initialScene);
        for (var s : mainScenes) {
            if (s == initialScene) continue;
            sceneGroup.addScene(s);
        }

        /* 场景位置修正 */
        FXUtils.observeHeight(vStage.getInitialScene().getContentPane(), sceneGroup.getNode(), -10 - 60 - 5 - 10);
        FXUtils.observeWidth(vStage.getInitialScene().getContentPane(), sceneGroup.getNode(), -20);

        var box = new HBox(
                new HPadding(10),
                new VBox(
                        new VPadding(10),
                        sceneGroup.getNode(),
                        new VPadding(5),
                        navigatePane.getNode()
                )
        );
        vStage.getInitialScene().getContentPane().getChildren().add(box);

//        var menuScene = new VScene(VSceneRole.DRAWER_VERTICAL);
//        menuScene.getNode().setPrefWidth(300);
//        menuScene.enableAutoContentWidth();
//        menuScene.getNode().setBackground(new Background(new BackgroundFill(
//                Theme.current().subSceneBackgroundColor(),
//                CornerRadii.EMPTY,
//                Insets.EMPTY
//        )));

//        vStage.getRootSceneGroup().addScene(menuScene, VSceneHideMethod.TO_LEFT);
//        var menuVBox = new VBox() {{
//            setPadding(new Insets(0, 0, 0, 24));
//            getChildren().add(new VPadding(20));
//        }};
//
//        menuScene.getContentPane().getChildren().add(menuVBox);
//        for (int i = 0; i < mainScenes.size(); ++i) {
//            final var targetIndex = i;
//            var scene = mainScenes.get(i);
//            var title = scene.getTitle();
//            var button = new FusionButton(title);
//            button.setDisableAnimation(true);
//            button.setOnAction(e -> {
//                //noinspection SuspiciousMethodCalls
//                var currentIndex = mainScenes.indexOf(sceneGroup.getCurrentMainScene());
//                if (currentIndex != targetIndex) {
//                    sceneGroup.show(scene, currentIndex < targetIndex ? VSceneShowMethod.FROM_RIGHT : VSceneShowMethod.FROM_LEFT);
//
//                    /* 修复通过左侧菜单栏切换页面导航按钮不变化的问题 */
//                    for (int naviBtnIndex = 0; naviBtnIndex < navigatorButtons.size(); naviBtnIndex++) {
//                        navigatorButtons.get(naviBtnIndex).setDisable(naviBtnIndex == targetIndex);
//                    }
//                }
//                vStage.getRootSceneGroup().hide(menuScene, VSceneHideMethod.TO_LEFT);
//                prevButton.setDisable(targetIndex == 0);
//                nextButton.setDisable(targetIndex == mainScenes.size() - 1);
//            });
//            button.setPrefWidth(250);
//            button.setPrefHeight(40);
//            if (i != 0) {
//                menuVBox.getChildren().add(new VPadding(20));
//            }
//            menuVBox.getChildren().add(button);
//        }
//        menuVBox.getChildren().add(new VPadding(20));

        /* 左上角帮助按钮 */
        var menuBtn = new FusionImageButton(ImageUtils.loadImage("/img/check.png")) {{
            setPrefWidth(40);
            setPrefHeight(VStage.TITLE_BAR_HEIGHT + 1);
            getImageView().setFitHeight(15);
            setLayoutX(-2);
            setLayoutY(-1);
        }};

//        menuBtn.setOnAction(e -> vStage.getRootSceneGroup().show(menuScene, VSceneShowMethod.FROM_LEFT));
//        menuBtn.setOnAction(e -> {
//            var scene = new VScene(VSceneRole.POPUP);
//            scene.enableAutoContentWidthHeight();
//            scene.getNode().setPrefWidth(500);
//            scene.getNode().setPrefHeight(300);
//            scene.getNode().setBackground(new Background(new BackgroundFill(
//                    new Color(0x8f / 255d, 0xb8 / 255d, 0xd8 / 255d, 1), //
//                    CornerRadii.EMPTY,
//                    Insets.EMPTY
//            )));
//            var closeBtn = new FusionButton("hide") {{
//                setPrefWidth(300);
//                setPrefHeight(150);
//            }};
//            closeBtn.setOnAction(ee -> {
//                sceneGroup.hide(scene, VSceneHideMethod.FADE_OUT);
//                FXUtils.runDelay(VScene.ANIMATION_DURATION_MILLIS, () -> sceneGroup.removeScene(scene));
//            });
//            scene.getContentPane().getChildren().add(closeBtn);
//            FXUtils.observeWidthHeightCenter(scene.getContentPane(), closeBtn);
//            sceneGroup.addScene(scene, VSceneHideMethod.FADE_OUT);
//            FXUtils.runDelay(0, () -> sceneGroup.show(scene, VSceneShowMethod.FADE_IN));
//        });

        vStage.getRoot().getContentPane().getChildren().add(menuBtn);

        vStage.getStage().setWidth(1000);
        vStage.getStage().setHeight(600);
        vStage.getStage().getIcons().add(ImageUtils.loadImage("/img/favicon.png"));
        vStage.getStage().centerOnScreen();
        vStage.getStage().show();
    }

    public static void addUIListener(UILifecycleAware listener) {
        uiListeners.add(listener);
    }

    private FusionButton createNavigateButton(I18nText labelText, int index) {
        return new FusionButton() {{
            setPrefWidth(150);
            setPrefHeight(navigatePane.getNode().getPrefHeight() - FusionPane.PADDING_V * 2);  /* 40 */
            setDisableAnimation(true);
            getTextNode().textProperty().bind(labelText.binding(i18nContext));

            setOnAction(e -> {
                if (isSwitch) return;  /* Scene 正在切换，禁用事件点击 */

                var current = sceneGroup.getCurrentMainScene();
                //noinspection SuspiciousMethodCalls
                int currentIndex = mainScenes.indexOf(current);
                if (currentIndex == index) return;

                VSceneShowMethod method = index > currentIndex ?
                        VSceneShowMethod.FROM_RIGHT : VSceneShowMethod.FROM_LEFT;

                targetIndex = index;
                isSwitch = true;
                navigatorButtons.forEach(b -> b.setDisable(true));
                SceneTemplate newScene = mainScenes.get(index);
                sceneGroup.show(newScene, VSceneShowMethod.FADE_IN);
            });
        }};
    }

    @Override
    public void stop() throws Exception {
        uiListeners.forEach(UILifecycleAware::onUIDispose);  /* 调用销毁逻辑，需要先调用UI销毁逻辑，再调用生命周期销毁逻辑 */
        JNativeHookManager.unregister(MainFX.class);  /* 注销全局钩子 */
        clientConfigLoader.save(clientConfig.get());  /* 保存客户端本地配置 */

        appContext.stop();  /* 停止宏核心 */
    }

    public static void main(String[] args) {
        Theme.setTheme(new DimTheme());
//        Theme.setTheme(new LightTheme());
        Application.launch(MainFX.class, args);
    }
}
