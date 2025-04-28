package club.pineclone.gtavmarco.controller;

import club.pineclone.gtavmarco.marco.ButtonListener;
import club.pineclone.gtavmarco.marco.MouseScrollDownMarco;
import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BridgeController {

    private static final ButtonListener marco;

    static {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
        try {
            GlobalScreen.registerNativeHook();
            marco = new MouseScrollDownMarco(NativeMouseEvent.BUTTON4);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onFunctionToggle(String name, boolean enabled) {
        if ("切枪偷速".equals(name) && enabled) {
            enableSwitchWeaponSpeedHack();
        } else if ("切枪偷速".equals(name)) {
            disableSwitchWeaponSpeedHack();
        }
    }

    private void enableSwitchWeaponSpeedHack() {
        GlobalScreen.addNativeMouseListener(marco);
    }

    private void disableSwitchWeaponSpeedHack() {
        GlobalScreen.removeNativeMouseListener(marco);
    }

}
