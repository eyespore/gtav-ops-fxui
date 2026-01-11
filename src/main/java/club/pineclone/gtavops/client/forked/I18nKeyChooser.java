package club.pineclone.gtavops.client.forked;

import club.pineclone.gtavops.client.i18n.I18nContext;
import club.pineclone.gtavops.client.i18n.I18nText;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelListener;
import io.vproxy.vfx.entity.input.Key;
import io.vproxy.vfx.entity.input.KeyCode;
import io.vproxy.vfx.entity.input.MouseWheelScroll;
import io.vproxy.vfx.manager.internal_i18n.InternalI18n;
import javafx.application.Platform;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.kwhat.jnativehook.keyboard.NativeKeyEvent.*;
import static com.github.kwhat.jnativehook.keyboard.NativeKeyEvent.KEY_LOCATION_NUMPAD;


public class I18nKeyChooser extends ForkedDialog<Key> {

    private final int flags;

    private final KeyListener keyListener = new KeyListener();
    private final MouseListener mouseListener = new MouseListener();
    private final WheelScrollListener wheelScrollListener = new WheelScrollListener();

    public static final int FLAG_WITH_KEY = 0x0001;   // 1 << 0, 监听键盘
    public static final int FLAG_WITH_MOUSE = 0x0002;  // 1 << 1, 监听鼠标按键
    public static final int FLAG_WITH_WHEEL_SCROLL = 0x0004;  // 1 << 2, 监听鼠标滚轮

    public I18nKeyChooser(I18nContext i18nContext) {
        this(i18nContext, FLAG_WITH_KEY);  // 默认监听键盘
    }

    public I18nKeyChooser(I18nContext i18nContext, int flags) {
        this.flags = flags;
        List<ForkedDialogButton<Key>> buttons = new ArrayList<>();
        if ((flags & FLAG_WITH_MOUSE) == FLAG_WITH_MOUSE) {
            buttons.add(new ForkedDialogButton.I18nDialogButton<>(new Key(MouseButton.PRIMARY), i18nContext, I18nText.of(i -> i.vfxComponent.keyChooser.primaryMouseButton)));
            buttons.add(new ForkedDialogButton.I18nDialogButton<>(new Key(MouseButton.MIDDLE), i18nContext, I18nText.of(i -> i.vfxComponent.keyChooser.middleMouseButton)));
            buttons.add(new ForkedDialogButton.I18nDialogButton<>(new Key(MouseButton.SECONDARY), i18nContext, I18nText.of(i -> i.vfxComponent.keyChooser.secondaryMouseButton)));
            buttons.add(new ForkedDialogButton.I18nDialogButton<>(new Key(MouseButton.FORWARD), i18nContext, I18nText.of(i -> i.vfxComponent.keyChooser.forwardMouseButton)));
            buttons.add(new ForkedDialogButton.I18nDialogButton<>(new Key(MouseButton.BACK), i18nContext, I18nText.of(i -> i.vfxComponent.keyChooser.backwardMouseButton)));
        }
        buttons.add(new ForkedDialogButton.I18nDialogButton<>(null, i18nContext, I18nText.of(i -> i.vfxComponent.keyChooser.cancel)));

        setButtons(buttons);  /* 调用 setButtons 之后，VDialogButton中的FusionButton button字段初始化完毕 */

        /*  */
//        buttons.forEach(b -> b.button.setPrefHeight(35)); // TODO: 修改按钮默认高度到35，并移交到Builder
//        getStage().getInitialScene().getContentPane().setPrefHeight(35);

        boolean withMouse = (flags & (FLAG_WITH_WHEEL_SCROLL | FLAG_WITH_MOUSE)) != 0;
        getMessageNode().setText(withMouse ? InternalI18n.get().keyChooserDesc() : InternalI18n.get().keyChooserDescWithoutMouse());

//        if (buttons.size() > 4) {
//            getStage().getStage().setWidth(1200);
//        }

        getStage().getStage().setWidth(600);

        List<String> listenedComponents = new ArrayList<>();

        if ((flags & FLAG_WITH_KEY) != 0) {
            listenedComponents.add(i18nContext.get().common.keyboard);
        }

        if ((flags & FLAG_WITH_MOUSE) != 0) {
            listenedComponents.add(i18nContext.get().common.mouseButton);
        }
        if ((flags & FLAG_WITH_WHEEL_SCROLL) != 0) {
            listenedComponents.add(i18nContext.get().common.mouseWheel);
        }

        getMessageNode().textProperty().bind(I18nText.of(i -> i.vfxComponent.keyChooser.description, String.join(",", listenedComponents)).binding(i18nContext));
    }

    private void registerListeners(int flags) {
        if ((flags & FLAG_WITH_KEY) == FLAG_WITH_KEY) {
            GlobalScreen.addNativeKeyListener(keyListener);
        }
        /* 采用点击的方式选择鼠标按键，而不是监听，监听容易造成误触 */
//        if ((flags & FLAG_WITH_MOUSE) == FLAG_WITH_MOUSE) {
//            GlobalScreen.addNativeMouseListener(mouseListener);
//        }
        if ((flags & FLAG_WITH_WHEEL_SCROLL) == FLAG_WITH_WHEEL_SCROLL) {
            GlobalScreen.addNativeMouseWheelListener(wheelScrollListener);
        }
    }

    private void unregisterListeners(int flags) {
        if ((flags & FLAG_WITH_KEY) == FLAG_WITH_KEY) {
            GlobalScreen.removeNativeKeyListener(keyListener);
        }
//        if ((flags & FLAG_WITH_MOUSE) == FLAG_WITH_MOUSE) {
//            GlobalScreen.removeNativeMouseListener(mouseListener);
//        }
        if ((flags & FLAG_WITH_WHEEL_SCROLL) == FLAG_WITH_WHEEL_SCROLL) {
            GlobalScreen.removeNativeMouseWheelListener(wheelScrollListener);
        }
    }

    public Optional<Key> choose() {
        registerListeners(flags);
        var ret = showAndWait();
        unregisterListeners(flags);
        return ret;
    }

    private class KeyListener implements NativeKeyListener {
        @Override
        public void nativeKeyPressed(NativeKeyEvent e) {
            Key key;
            if (e.getKeyCode() == VC_CONTROL || e.getKeyCode() == VC_ALT || e.getKeyCode() == VC_SHIFT || e.getKeyCode() == 0x0e36 /*right shift*/) {
                boolean isLeft;
                if (e.getKeyLocation() == KEY_LOCATION_LEFT) {
                    isLeft = true;  // 左侧键修饰
                } else if (e.getKeyLocation() == KEY_LOCATION_RIGHT) {
                    isLeft = false;  // 右侧键修饰
                } else {
                    return; // should not happen, but if happens, we ignore this event
                }
                key = new Key(KeyCode.valueOf(e.getKeyCode()), isLeft);
            } else {
                if (e.getKeyLocation() == KEY_LOCATION_NUMPAD) {
                    return; // ignore numpad
                }
                key = new Key(KeyCode.valueOf(e.getKeyCode()));
                if (!key.isValid()) key = null;
            }

            Key finalKey = key;
            Platform.runLater(() -> {
                I18nKeyChooser.this.returnValue = finalKey;
                I18nKeyChooser.this.getStage().close();
            });
        }
    }

    private class MouseListener implements NativeMouseListener {
        @Override
        public void nativeMouseClicked(NativeMouseEvent e) {
            Key key;
            switch (e.getButton()) {
                case NativeMouseEvent.BUTTON1: {
                    key = new Key(MouseButton.PRIMARY); // 左键
                    break;
                }
                case NativeMouseEvent.BUTTON2: {
                    key = new Key(MouseButton.SECONDARY);  // 右键
                    break;
                }
                case NativeMouseEvent.BUTTON3: {
                    key = new Key(MouseButton.MIDDLE); // 中键
                    break;
                }
                case NativeMouseEvent.BUTTON4: {
                    key = new Key(MouseButton.BACK);  // 后退（侧键）
                    break;
                }
                case NativeMouseEvent.BUTTON5: {
                    key = new Key(MouseButton.FORWARD);  // 前进（侧键）
                    break;
                }
                default: return;  // 如果是其他鼠标按键，忽略
            }

            Platform.runLater(() -> {
                I18nKeyChooser.this.returnValue = key;
                I18nKeyChooser.this.getStage().close();
            });
        }
    }

    private class WheelScrollListener implements NativeMouseWheelListener {
        @Override
        public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
            Key key;
            int rotation = e.getWheelRotation();
            if (rotation < 0) {
                /* 鼠标向上滚动 */
                key = new Key(new MouseWheelScroll(MouseWheelScroll.Direction.UP));
            } else if (rotation > 0) {
                /* 鼠标向下滚动 */
                key = new Key(new MouseWheelScroll(MouseWheelScroll.Direction.DOWN));
            } else {
                key = null;
            }

            Platform.runLater(() -> {
                I18nKeyChooser.this.returnValue = key;
                I18nKeyChooser.this.getStage().close();
            });
        }
    }

}
