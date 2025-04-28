module club.pineclone.gtavmarco {
    requires javafx.web;
    requires javafx.fxml;
    requires javafx.controls;

    requires com.almasb.fxgl.all;
    requires jdk.jsobject;
    requires jnativehook;
    requires java.logging;
    requires java.desktop;

    opens club.pineclone.gtavmarco to javafx.fxml;
    opens club.pineclone.gtavmarco.controller to javafx.fxml;

    exports club.pineclone.gtavmarco;
    exports club.pineclone.gtavmarco.controller;
}