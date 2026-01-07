package club.pineclone.gtavops;

import club.pineclone.gtavops.common.JLibLocator;
import club.pineclone.gtavops.common.JNativeHookManager;
import club.pineclone.gtavops.config.MacroConfigLoader;
import club.pineclone.gtavops.jni.PlatformFocusMonitor;
import club.pineclone.gtavops.macro.MacroRegistry;
import club.pineclone.gtavops.macro.MacroTaskScheduler;
import club.pineclone.gtavops.utils.PathUtils;
import com.github.kwhat.jnativehook.GlobalScreen;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Main.class);

    public static void start() throws Exception {
        log.info("Initializing macro core app home directory");  /* 初始化宏后端应用家目录 */
        PathUtils.initCoreHome();

        log.info("Loading native library for jnativehook and terminate jnativehook logging ");  /* 加载 jnativehook 依赖的本地库 */
        Class.forName(JLibLocator.class.getName());
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());  /* 停止 jnativehook 日志记录 */
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);

        /* 注册 jnativehook 全局钩子在应用启动阶段调用，注册jnativehook全局监听钩子，从而确保后续所有InputSource监听器能够正常工作，*/
        /* 以及Action能够正常模拟操作 */
        log.info("Register jnativehook global native hook for macro core");
        JNativeHookManager.register(Main.class);

        log.info("Register platform focus monitor, ensure the macro can only be activate while target app is focusing");
        Class.forName(PlatformFocusMonitor.class.getName());  /* 平台焦点监听 */
        PlatformFocusMonitor.addListener(MacroRegistry.getInstance());  /* 添加监听器 */

        log.info("Loading macro task scheduler for handling macro multiple-threads task");
        Class.forName(MacroTaskScheduler.class.getName());  /* 宏任务调度 */

        // TODO: 扫描并加载所有配置文件到列表，等待客户端调起配置
        log.info("Loading macro configuration for macro core");
        MacroConfigLoader.load();  /* 加载宏配置文件 */
    }

    public static void stop() throws Exception {
        JNativeHookManager.unregister(Main.class); /* 注销 jnativehook 全局钩子 */

        MacroTaskScheduler.shutdown();  /* 停止任务调度 */
        PlatformFocusMonitor.shutdown();  /* 停止焦点监听 */
        MacroConfigLoader.save();  /* 保存配置 */
    }
}
