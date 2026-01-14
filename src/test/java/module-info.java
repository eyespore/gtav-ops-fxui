module club.pineclone.test {
    requires club.pineclone.gtavops;
    requires com.fasterxml.jackson.databind;
    requires io.vproxy.vfx;
    requires junit;
    requires com.github.kwhat.jnativehook;

    requires static lombok;
    requires io.vproxy.base;
    requires javafx.graphics;
    requires org.mapstruct;
    requires org.junit.jupiter.api;
    requires org.slf4j;

    opens club.pineclone.test.domain;
    opens club.pineclone.test.dao;
}