package club.pineclone.gtavops;

import club.pineclone.gtavops.common.JNativeHookManager;
import club.pineclone.gtavops.common.PathUtils;
import club.pineclone.gtavops.config.MacroConfigLoader;
import club.pineclone.gtavops.jni.PlatformFocusMonitor;
import club.pineclone.gtavops.macro.MacroFactory;
import club.pineclone.gtavops.macro.MacroRegistry;
import club.pineclone.gtavops.macro.MacroTaskScheduler;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class AppContext {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static volatile AppContext INSTANCE;

    private final MacroFactory macroFactory;  /* 宏工厂 */

    private final MacroRegistry macroRegistry;  /* 宏注册表 */

    private final PlatformFocusMonitor platformFocusMonitor;  /* 平台焦点监听 */

    private final List<AppLifecycleAware> listeners;

    @Getter private final MacroTaskScheduler macroTaskScheduler;  /* 宏任务调度 */

    public AppContext() {
        if (INSTANCE != null) throw new IllegalStateException("AppContext instance already exists.");

        this.listeners = new ArrayList<>();

        this.macroFactory = new MacroFactory();
        this.macroTaskScheduler = new MacroTaskScheduler();
        this.platformFocusMonitor = new PlatformFocusMonitor();

        this.macroRegistry = new MacroRegistry(platformFocusMonitor);

        this.listeners.add(macroRegistry);
        this.listeners.add(macroTaskScheduler);
        this.listeners.add(platformFocusMonitor);
    }

    public static AppContext getInstance() {
        if (INSTANCE == null) {
            synchronized (AppContext.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppContext();
                }
            }
        }
        return INSTANCE;
    }

    public void init() throws Exception {
        log.info("Initializing macro core app home directory");  /* 初始化宏后端应用家目录 */
        PathUtils.initCoreHome();

        /* 注册 jnativehook 全局钩子在应用启动阶段调用，注册jnativehook全局监听钩子，从而确保后续所有InputSource监听器能够正常工作，*/
        log.info("Register jnativehook global native hook for macro core");
        JNativeHookManager.register(AppContext.class);

        listeners.forEach(AppLifecycleAware::onAppInit);
    }

    public void start() throws Exception {
        listeners.forEach(AppLifecycleAware::onAppStart);
    }

    public void stop() throws Exception {
        JNativeHookManager.unregister(AppContext.class); /* 注销 jnativehook 全局钩子 */
        listeners.forEach(AppLifecycleAware::onAppStop);
//        MacroConfigLoader.save();  /* 保存配置 */
    }
}
