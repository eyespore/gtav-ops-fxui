package club.pineclone.gtavmarco;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;

public class GTAVMarcoApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(GTAVMarcoApp.class.getResource("/fxml/index.fxml"));  // 加载fxml模板
        fxmlLoader.load();

        AnchorPane root = fxmlLoader.getRoot();  // 根节点
        Rectangle clip = new Rectangle(800, 500);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        root.setClip(clip);

        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);

        InputStream resource = GTAVMarcoApp.class.getResourceAsStream("/static/favicon.png"); // 加载icon
        assert resource != null;
        stage.getIcons().add(new Image(resource));

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}