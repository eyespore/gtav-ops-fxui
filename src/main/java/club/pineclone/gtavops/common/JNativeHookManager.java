package club.pineclone.gtavops.common;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class JNativeHookManager {

    private static final Object LOCK = new Object();
    @Getter private static volatile boolean registered = false;
    private static final Set<Object> owners = new HashSet<>();

    private JNativeHookManager() {}

    public static void register(Object owner) throws NativeHookException {
        synchronized (LOCK) {
            if (owners.add(owner)) {
                if (!registered) {
                    GlobalScreen.registerNativeHook();
                    registered = true;
                }
            }
        }
    }

    public static void unregister(Object owner) throws NativeHookException {
        synchronized (LOCK) {
            if (!owners.remove(owner)) return; // 非法 / 重复 unregister，直接忽略
            if (owners.isEmpty() && registered) {
                GlobalScreen.unregisterNativeHook();
                GlobalScreen.setEventDispatcher(null);
                registered = false;
            }
        }
    }
}
