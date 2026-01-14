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
    requires org.mapstruct;
    requires annotations;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens club.pineclone.gtavops;
    opens club.pineclone.gtavops.domain;

    exports club.pineclone.gtavops;

    exports club.pineclone.gtavops.common;
    opens club.pineclone.gtavops.common;
    exports club.pineclone.gtavops.config;

    exports club.pineclone.gtavops.core.jni;
    exports club.pineclone.gtavops.core.macro;
    exports club.pineclone.gtavops.core.macro.trigger;
    exports club.pineclone.gtavops.core.macro.action;
    exports club.pineclone.gtavops.core.macro.action.robot;
    exports club.pineclone.gtavops.core.macro.trigger.source;
    exports club.pineclone.gtavops.core.macro.trigger.policy;
    exports club.pineclone.gtavops.core.macro.action.impl;

    exports club.pineclone.gtavops.core.macro.action.impl.betterlbutton;
    exports club.pineclone.gtavops.core.macro.action.impl.betterpmenu;
    exports club.pineclone.gtavops.core.macro.action.impl.bettermmenu;
    exports club.pineclone.gtavops.core.macro.action.impl.actionext;
    exports club.pineclone.gtavops.core.macro.action.impl.swapglitch;

    opens club.pineclone.gtavops.domain.vo.macro;

    exports club.pineclone.gtavops.service;
    exports club.pineclone.gtavops.controller;
    exports club.pineclone.gtavops.domain;
    exports club.pineclone.gtavops.domain.vo.macro;
    exports club.pineclone.gtavops.domain.dto.macro;
    exports club.pineclone.gtavops.domain.mapper;

    opens club.pineclone.gtavops.client;
    opens club.pineclone.gtavops.client.utils;

    exports club.pineclone.gtavops.client;
    exports club.pineclone.gtavops.client.i18n;
    exports club.pineclone.gtavops.client.config;
    exports club.pineclone.gtavops.client.component;
    exports club.pineclone.gtavops.client.forked;
    exports club.pineclone.gtavops.client.utils;
    exports club.pineclone.gtavops.dao;

    exports club.pineclone.gtavops.domain.entity;
    exports club.pineclone.gtavops.dao.impl;
    opens club.pineclone.gtavops.core.macro;
    opens club.pineclone.gtavops.service;
    exports club.pineclone.gtavops.service.impl;
    opens club.pineclone.gtavops.service.impl;
}
