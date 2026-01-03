package club.pineclone.gtavops.gui;

import club.pineclone.gtavops.config.ConfigHolder;
import club.pineclone.gtavops.gui.scene.SceneLifecycleAware;
import club.pineclone.gtavops.gui.scene.SceneRegistry;
import club.pineclone.gtavops.gui.scene.SceneTemplate;
import club.pineclone.gtavops.i18n.ExtendedI18n;
import club.pineclone.gtavops.i18n.I18nHolder;
import club.pineclone.gtavops.utils.ImageUtils;
import io.vproxy.vfx.control.globalscreen.GlobalScreenUtils;
import io.vproxy.vfx.manager.task.TaskManager;
import io.vproxy.vfx.theme.Theme;
import io.vproxy.vfx.ui.button.FusionButton;
import io.vproxy.vfx.ui.button.FusionImageButton;
import io.vproxy.vfx.ui.layout.HPadding;
import io.vproxy.vfx.ui.layout.VPadding;
import io.vproxy.vfx.ui.pane.FusionPane;
import io.vproxy.vfx.ui.scene.*;
import io.vproxy.vfx.ui.stage.VStage;
import io.vproxy.vfx.util.FXUtils;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/* 主窗口 */
public class MainFX
        extends Application
        implements SceneLifecycleAware {

    @Setter private static FXLifecycleAware listener;

    private final List<SceneTemplate> mainScenes = new ArrayList<>();
    private VSceneGroup sceneGroup;

    private FusionPane navigatePane;
    private List<FusionButton> navigatorButtons;
    private boolean isSwitch = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (listener != null) listener.onFXStart();

        ExtendedI18n i18n = I18nHolder.get();
        ExtendedI18n.Intro iI18n = i18n.intro;

        VStage vStage = new VStage(primaryStage) {
            @Override
            public void close() {
                super.close();
                TaskManager.get().terminate();
                GlobalScreenUtils.unregister();
            }
        };
        vStage.getInitialScene().enableAutoContentWidthHeight();
        vStage.setTitle(ConfigHolder.APPLICATION_TITLE);

        // 添加所有注册的场景
        mainScenes.addAll(SceneRegistry.getInstance().values());

        /* 将主窗口注册到各个场景作为监听器 */
        mainScenes.forEach(s -> s.setListener(this));

        /* 设定引导页作为初始场景 */
        // var initialScene = mainScenes.stream().filter(e -> e instanceof ).findAny().get();
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
            add(createNavigateButton(iI18n.introNavigate, 0));
            add(createNavigateButton(iI18n.featureNavigate, 1));
            add(createNavigateButton(iI18n.fontpackNavigate, 2));
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

        var menuScene = new VScene(VSceneRole.DRAWER_VERTICAL);
        menuScene.getNode().setPrefWidth(300);
        menuScene.enableAutoContentWidth();
        menuScene.getNode().setBackground(new Background(new BackgroundFill(
                Theme.current().subSceneBackgroundColor(),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));

        vStage.getRootSceneGroup().addScene(menuScene, VSceneHideMethod.TO_LEFT);
        var menuVBox = new VBox() {{
            setPadding(new Insets(0, 0, 0, 24));
            getChildren().add(new VPadding(20));
        }};

        menuScene.getContentPane().getChildren().add(menuVBox);
        for (int i = 0; i < mainScenes.size(); ++i) {
            final var targetIndex = i;
            var scene = mainScenes.get(i);
            var title = scene.getTitle();
            var button = new FusionButton(title);
            button.setDisableAnimation(true);
            button.setOnAction(e -> {
                //noinspection SuspiciousMethodCalls
                var currentIndex = mainScenes.indexOf(sceneGroup.getCurrentMainScene());
                if (currentIndex != targetIndex) {
                    sceneGroup.show(scene, currentIndex < targetIndex ? VSceneShowMethod.FROM_RIGHT : VSceneShowMethod.FROM_LEFT);

                    /* 修复通过左侧菜单栏切换页面导航按钮不变化的问题 */
                    for (int naviBtnIndex = 0; naviBtnIndex < navigatorButtons.size(); naviBtnIndex++) {
                        navigatorButtons.get(naviBtnIndex).setDisable(naviBtnIndex == targetIndex);
                    }
                }
                vStage.getRootSceneGroup().hide(menuScene, VSceneHideMethod.TO_LEFT);
//                prevButton.setDisable(targetIndex == 0);
//                nextButton.setDisable(targetIndex == mainScenes.size() - 1);
            });
            button.setPrefWidth(250);
            button.setPrefHeight(40);
            if (i != 0) {
                menuVBox.getChildren().add(new VPadding(20));
            }
            menuVBox.getChildren().add(button);
        }
        menuVBox.getChildren().add(new VPadding(20));

        var menuBtn = new FusionImageButton(ImageUtils.loadImage("/img/menu.png")) {{
            setPrefWidth(40);
            setPrefHeight(VStage.TITLE_BAR_HEIGHT + 1);
            getImageView().setFitHeight(15);
            setLayoutX(-2);
            setLayoutY(-1);
        }};

        menuBtn.setOnAction(e -> vStage.getRootSceneGroup().show(menuScene, VSceneShowMethod.FROM_LEFT));
        vStage.getRoot().getContentPane().getChildren().add(menuBtn);

        vStage.getStage().setWidth(1200);
        vStage.getStage().setHeight(700);
        vStage.getStage().getIcons().add(ImageUtils.loadImage("/img/favicon.png"));
        vStage.getStage().centerOnScreen();
        vStage.getStage().show();
    }



    private FusionButton createNavigateButton(String text, int index) {
        return new FusionButton(text) {{
            setPrefWidth(150);
            setPrefHeight(navigatePane.getNode().getPrefHeight() - FusionPane.PADDING_V * 2);  /* 40 */
            setDisableAnimation(true);

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
    public void init() throws Exception {
        if (listener != null) listener.onFXInit();
    }

    @Override
    public void stop() throws Exception {
        if (listener != null) listener.onFXStop();
    }
}
