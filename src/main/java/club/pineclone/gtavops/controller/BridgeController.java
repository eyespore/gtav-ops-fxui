package club.pineclone.gtavops.controller;

import club.pineclone.gtavops.marco.ButtonListener;
import club.pineclone.gtavops.marco.MouseScrollDownMarco;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

public class BridgeController {

    private static final ButtonListener marco;

    static {

        try {
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
