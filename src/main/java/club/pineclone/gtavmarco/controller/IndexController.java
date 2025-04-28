package club.pineclone.gtavmarco.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import netscape.javascript.JSObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class IndexController {

    @FXML
    public BorderPane appPane;  // app主功能区域

    @FXML
    private AnchorPane rootPane;  // 根节点

    @FXML
    private Label closeButton;  // 关闭按钮

    @FXML
    private Label minimizeButton;  // 最小化按钮

    @FXML
    private Label windowTitle;  // app标题

    @FXML
    private WebView webView;  // 网页窗口

    private double xOffset = 0;
    private double yOffset = 0;

    private final Map<String, Timeline> hoverAnimations = new HashMap<>();

    private final FontFactory fontFactory = new FontFactory();
    private final Font largeText = fontFactory.getLargeFont();

    // 颜色过渡函数
    private String interpolateColor(String startColor, String endColor, double fraction) {
        int startR = Integer.parseInt(startColor.substring(1, 3), 16);
        int startG = Integer.parseInt(startColor.substring(3, 5), 16);
        int startB = Integer.parseInt(startColor.substring(5, 7), 16);

        int endR = Integer.parseInt(endColor.substring(1, 3), 16);
        int endG = Integer.parseInt(endColor.substring(3, 5), 16);
        int endB = Integer.parseInt(endColor.substring(5, 7), 16);

        int r = (int) (startR + (endR - startR) * fraction);
        int g = (int) (startG + (endG - startG) * fraction);
        int b = (int) (startB + (endB - startB) * fraction);

        return String.format("#%02X%02X%02X", r, g, b);
    }

    private void setBgColorTransition(Node node, String startColor, String endColor) {
        String id = node.getId();
        Timeline hoverAnimation = new Timeline();

        for (int i = 0; i <= 30; i++) {
            double fraction = i / 30.0;  // 创建 30 帧动画
            String color = interpolateColor(startColor, endColor, fraction);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(i * 0.0067), e -> node.setStyle("-fx-background-color: " + color));
            hoverAnimation.getKeyFrames().add(keyFrame);
        }

        hoverAnimations.put(id, hoverAnimation);
        node.setOnMouseEntered(this::handleMouseEnter);
        node.setOnMouseExited(this::handleMouseExit);
    }

    @FXML
    private void initialize() {
        initCss();  // 加载css样式
        initWebView();  // 初始化web视图
    }

    private void initCss() {
        URL resource = getClass().getResource("/css/main.css");  // 加载主css样式表
        assert resource != null;
        rootPane.getStylesheets().add(resource.toExternalForm());

        windowTitle.setFont(largeText);

        String startColor = "#8ca9aa";  // 初始背景色
        String endColor = "#7d9d9c";    // 悬停背景色
        setBgColorTransition(this.minimizeButton, startColor, endColor);
        setBgColorTransition(this.closeButton, startColor, endColor);
    }

    private static final BridgeController bridge = new BridgeController();

    private void initWebView() {
        WebEngine webEngine = webView.getEngine();
        URL htmlResource = getClass().getResource("/web/dist/index.html");
        assert htmlResource != null;
        webEngine.load(htmlResource.toExternalForm());
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("bridge", bridge);  // 注入控制器到window
            }
        });
    }

    @FXML
    private void handleCloseButtonClicked(MouseEvent event) {
        // 获取当前点击的控件的父节点（即 Scene），然后获取 Stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    // 最小化按钮的点击事件
    @FXML
    private void handleMinimizeButtonClicked(MouseEvent event) {
        // 获取当前点击的控件的父节点（即 Scene），然后获取 Stage
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    // 鼠标悬停，执行动画
    @FXML
    private void handleMouseEnter(MouseEvent event) {
        String id = ((Node) event.getSource()).getId();
        Timeline hoverAnimation = hoverAnimations.get(id);
        hoverAnimation.setRate(1);  // 设置动画正向播放
        hoverAnimation.play();  // 播放动画
    }

    // 鼠标离开，执行反向动画
    @FXML
    private void handleMouseExit(MouseEvent event) {
        Timeline hoverAnimation = hoverAnimations.get(((Node) event.getSource()).getId());
        hoverAnimation.setRate(-1);  // 设置动画反向播放
        hoverAnimation.play();  // 播放反向动画
    }

    @FXML
    public void handleNavbarDragged(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    public void handleNavbarPressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    private static class FontFactory {
        private final InputStream fontResource;

        public FontFactory() {
            this.fontResource = IndexController.class.getResourceAsStream("/static/meow.ttf");
        }

        public Font getLargeFont() {
            return Font.loadFont(fontResource, 28);
        }
    }
}
