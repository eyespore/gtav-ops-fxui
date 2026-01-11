package club.pineclone.gtavops.jni;

import club.pineclone.gtavops.AppLifecycleAware;
import club.pineclone.gtavops.utils.JNIUtils;
import io.vproxy.base.util.LogType;
import io.vproxy.base.util.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/* 平台焦点监听 */
public class PlatformFocusMonitor implements AppLifecycleAware {

    private final ScheduledExecutorService scheduler;
    private String lastTitle;
    private final Set<WindowTitleListener> listeners = new HashSet<>();
    private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

    public PlatformFocusMonitor() {
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "platform-focus-scheduler");
            t.setDaemon(true);
            return t;
        });
    }

    public void onAppStart() {  /* 启动平台焦点监听 */
        log.info("Register platform focus monitor, ensure the macro can only be activate while target app is focusing");
        scheduler.scheduleAtFixedRate(this::poll, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void onAppStop() {
        scheduler.shutdownNow();
    }

    private void poll() {
        try {
            String title = JNIUtils.getForegroundWindowTitle();
            if (title == null) return;
            if (!title.equals(lastTitle)) {
                lastTitle = title;
                listeners.forEach(listener -> listener.accept(title));
            }
        } catch (UnsatisfiedLinkError e) {
            Logger.error(LogType.SYS_ERROR, e.getMessage());
        } catch (Throwable t) {
            Logger.warn(LogType.SYS_ERROR, t.getMessage());
        }
    }

    public void addListener(WindowTitleListener listener) {
        listeners.add(listener);
    }

    public void removeListener(WindowTitleListener listener) {
        listeners.remove(listener);
    }
}
