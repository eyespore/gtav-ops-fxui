module club.pineclone.gtavops {
    requires javafx.web;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires jdk.jsobject;
    requires io.vproxy.vfx;
    requires com.github.kwhat.jnativehook;
    requires io.vproxy.base;
    requires com.fasterxml.jackson.databind;
    requires vjson;
    requires static lombok;
    requires javafx.graphics;
    requires org.slf4j;
//    requires annotations;  /* jetbrains.annotations 不支持 Java9+ 模块化，会引发异常 */
    requires com.fasterxml.jackson.datatype.jsr310;

    opens club.pineclone.gtavops;
    exports club.pineclone.gtavops;

    exports club.pineclone.gtavops.common;
    opens club.pineclone.gtavops.common;
    exports club.pineclone.gtavops.config;

    opens club.pineclone.gtavops.utils;
    exports club.pineclone.gtavops.i18n;
    exports club.pineclone.gtavops.component;
    exports club.pineclone.gtavops.forked;
    exports club.pineclone.gtavops.utils;
}
