package club.pineclone.gtavops.client;

import club.pineclone.gtavops.Main;
import club.pineclone.gtavops.client.config.ClientConfig;
import club.pineclone.gtavops.client.config.ClientConfigLoader;
import club.pineclone.gtavops.client.theme.BaseTheme;
import club.pineclone.gtavops.common.JNativeHookManager;
import club.pineclone.gtavops.common.SingletonLock;
import club.pineclone.gtavops.client.scene.*;
import club.pineclone.gtavops.client.i18n.ExtendedI18n;
import club.pineclone.gtavops.client.i18n.I18nLoader;
import club.pineclone.gtavops.utils.ImageUtils;
import club.pineclone.gtavops.utils.PathUtils;
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
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * 主窗口
 */
@Slf4j
public class MainFX extends Application {

    public static final String APPLICATION_TITLE = "GTAV Ops";  /* 应用基础信息 */
    private static final Set<UILifecycleAware> uiListeners = new HashSet<>();

    private Exception exception;  /* 初始化阶段异常 */

    private final List<SceneTemplate> mainScenes = new ArrayList<>();
    private VSceneGroup sceneGroup;

    private FusionPane navigatePane;
    private List<FusionButton> navigatorButtons;
    private boolean isSwitch = false;

    private final ObjectProperty<ExtendedI18n> i18n = new SimpleObjectProperty<>();  /* 客户端本地化 */
    private final ObjectProperty<ClientConfig> config = new SimpleObjectProperty<>();  /* 客户端配置 */

    @Override
    public void init() throws Exception {
        MDC.put("module", "[Client]");  /* 初始化日志 */
        try {

            log.info("Launching macro core");
            Main.start();  /* 后端启动 - 启动宏核心 */

            log.info("Initializing fxui app home directory");
            PathUtils.initCoreHome();  /* 初始化客户端应用家目录 */

            log.info("Obtaining app singleton lock");
            if (!SingletonLock.lockInstance()) {  /* 程序已经在运行，抛出异常并返回 */
                exception = new RuntimeException("Duplicated GTAV Ops Instance");
                return;
            }

            log.info("Loading client configuration");
            config.set(ClientConfigLoader.getInstance().load(PathUtils.getClientConfigPath()));  /* 加载客户端配置 */

            log.info("Scanning and loading i18n configuration");
            I18nLoader.getInstance().init();  /* 扫描本地化，并基于客户端配置初始化本地化 */
            i18n.set(I18nLoader.getInstance().load(config.get().lang));  /* 加载默认本地化 */

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

        VStage vStage = new VStage(new VStageInitParams().setResizable(false).setMaximizeAndResetButton(false)) {
            @Override
            public void close() {
                super.close();
                TaskManager.get().terminate();
            }
        };
        vStage.getInitialScene().enableAutoContentWidthHeight();
        vStage.setTitle(APPLICATION_TITLE);

        /* 注册所有场景 */
        mainScenes.add(new IntroScene(i18n));
        mainScenes.add(new MacroToggleScene(i18n));
        mainScenes.add(new ConfigScene(i18n, config));

//        mainScenes.add(new FontPackScene(oldI18n));

        mainScenes.forEach(MainFX::addUIListener);
        uiListeners.forEach(UILifecycleAware::onUIInit);  /* 调用初始化逻辑 */

        var initialScene = mainScenes.get(0);

        /* 场景导航栏 */
        sceneGroup = new VSceneGroup(initialScene);
        for (var s : mainScenes) {
            if (s == initialScene) continue;
            sceneGroup.addScene(s);
        }

        navigatePane = new FusionPane();
        navigatePane.getNode().setPrefHeight(60);
        FXUtils.observeHeight(vStage.getInitialScene().getContentPane(), sceneGroup.getNode(), -10 - 60 - 5 - 10);
        FXUtils.observeWidth(vStage.getInitialScene().getContentPane(), sceneGroup.getNode(), -20);
        FXUtils.observeWidth(vStage.getInitialScene().getContentPane(), navigatePane.getNode(), -20);

        navigatorButtons = new ArrayList<>() {{
            add(createNavigateButton(i -> i.introScene.introNavigate, 0));
            add(createNavigateButton(i -> i.introScene.featureNavigate, 1));
            add(createNavigateButton(i -> i.introScene.configNavigate, 2));
//            add(createNavigateButton(iI18n.fontpackNavigate, 3));
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

        var menuBtn = new FusionImageButton(ImageUtils.loadImage("/img/check.png")) {{
            setPrefWidth(40);
            setPrefHeight(VStage.TITLE_BAR_HEIGHT + 1);
            getImageView().setFitHeight(15);
            setLayoutX(-2);
            setLayoutY(-1);
        }};

//        menuBtn.setOnAction(e -> vStage.getRootSceneGroup().show(menuScene, VSceneShowMethod.FROM_LEFT));
        menuBtn.setOnAction(e -> {});

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

    private FusionButton createNavigateButton(Function<ExtendedI18n, String> textProvider, int index) {
        return new FusionButton() {{
            setPrefWidth(150);
            setPrefHeight(navigatePane.getNode().getPrefHeight() - FusionPane.PADDING_V * 2);  /* 40 */
            setDisableAnimation(true);
            getTextNode().textProperty().bind(Bindings.createStringBinding(() -> textProvider.apply(i18n.get()), i18n));

            setOnAction(e -> {
                if (isSwitch) return;  /* Scene 正在切换，禁用事件点击 */

                var current = sceneGroup.getCurrentMainScene();
                //noinspection SuspiciousMethodCalls
                int currentIndex = mainScenes.indexOf(current);
                if (currentIndex == index) return;

                VSceneShowMethod method = index > currentIndex ?
                        VSceneShowMethod.FROM_RIGHT : VSceneShowMethod.FROM_LEFT;

                isSwitch = true;
                navigatorButtons.forEach(b -> b.setDisable(true));

                SceneTemplate newScene = mainScenes.get(index);
                sceneGroup.show(newScene, method);

                PauseTransition pause = new PauseTransition(Duration.millis(400));
                pause.setOnFinished(e2 -> {
                    isSwitch = false;
                    for (int i = 0; i < navigatorButtons.size(); i++) {
                        navigatorButtons.get(i).setDisable(i == index);
                    }
                });
                pause.play();
            });
        }};
    }

    @Override
    public void stop() throws Exception {
        uiListeners.forEach(UILifecycleAware::onUIDispose);  /* 调用销毁逻辑，需要先调用UI销毁逻辑，再调用生命周期销毁逻辑 */
        JNativeHookManager.unregister(MainFX.class);  /* 注销全局钩子 */
        ClientConfigLoader.getInstance().save(config.get(), PathUtils.getClientConfigPath());  /* 保存客户端本地配置 */

        Main.stop();  /* 停止宏核心 */
    }

    public static void main(String[] args) {
        Theme.setTheme(new BaseTheme());
        Application.launch(MainFX.class, args);
    }
}
