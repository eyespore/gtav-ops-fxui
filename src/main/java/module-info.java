module club.pineclone.gtavops {
    requires javafx.web;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires jdk.jsobject;
    requires io.vproxy.vfx;
    requires com.github.kwhat.jnativehook;
    requires io.vproxy.base;

    opens club.pineclone.gtavops;
    opens club.pineclone.gtavops.utils;
    opens club.pineclone.gtavops.controller;

    exports club.pineclone.gtavops;
    exports club.pineclone.gtavops.utils;
    exports club.pineclone.gtavops.controller;
}
