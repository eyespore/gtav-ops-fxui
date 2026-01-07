package club.pineclone.gtavops.macro;

import lombok.Getter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 宏任务调度
 */
public class MacroTaskScheduler {

    @Getter
    private static final ScheduledExecutorService SCHEDULER =
            Executors.newScheduledThreadPool(
                    Runtime.getRuntime().availableProcessors(),
                    new NamedThreadFactory("macro-task-scheduler-%d")
            );

    private MacroTaskScheduler() {}

    public static void shutdown() {
        SCHEDULER.shutdownNow();
    }

    private static class NamedThreadFactory implements ThreadFactory {
        private final AtomicInteger count = new AtomicInteger();
        private final String pattern;
        public NamedThreadFactory(String pattern) { this.pattern = pattern; }
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, String.format(pattern, count.incrementAndGet()));
            t.setDaemon(true);
            return t;
        }
    }
}
