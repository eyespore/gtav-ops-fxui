package club.pineclone.gtavmarco;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GTAVMarcoApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(GTAVMarcoApp.class.getResource("/fxml/index.fxml"));
        VBox root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setTitle("GTAV Marco By Pineclone!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}