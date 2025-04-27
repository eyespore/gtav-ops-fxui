module club.pineclone.gtavmarco {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens club.pineclone.gtavmarco to javafx.fxml;
    exports club.pineclone.gtavmarco;
}