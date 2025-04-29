package club.pineclone.gtavops.marco;


import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ButtonListener implements NativeMouseInputListener {

    protected final int listenedButton;
    protected final AtomicBoolean running = new AtomicBoolean(false);
    protected Thread macroThread;
    protected Robot robot;

    public ButtonListener(int listenedButton) {
        this.listenedButton = listenedButton;
        try {
            this.robot = new Robot();
        } catch (Exception ignored) {

        }
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        if (e.getButton() == listenedButton && !running.get()) {
            running.set(true);
            startMacro();
        }
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        if (e.getButton() == listenedButton && running.get()) {
            running.set(false);
        }
    }

    protected abstract void execute();

    private void doExecute() {
        this.enter();
        while (running.get()) {
            this.execute();
        }
        this.fadeout();
    }

    private void startMacro() {
        macroThread = new Thread(this::doExecute);
        macroThread.setDaemon(true);  // 设置为守护进程
        macroThread.start();
    }

    protected void enter() {}
    protected void fadeout() {}
}
