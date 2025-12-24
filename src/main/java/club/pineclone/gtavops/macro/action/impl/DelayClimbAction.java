package club.pineclone.gtavops.macro.action.impl;

import club.pineclone.gtavops.macro.action.Action;
import club.pineclone.gtavops.macro.action.ActionEvent;
import club.pineclone.gtavops.macro.action.ScheduledAction;
import club.pineclone.gtavops.macro.action.robot.RobotFactory;
import club.pineclone.gtavops.macro.action.robot.VCRobotAdapter;
import io.vproxy.base.util.Logger;
import io.vproxy.vfx.entity.input.Key;
import io.vproxy.vfx.entity.input.KeyCode;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class DelayClimbAction extends Action {

    private final VCRobotAdapter robot;
    private final Key usePhoneKey;
    private final Key hideInCoverKey;

    private final long timeUtilCameraExited;
    private final long timeUtilCameraLoaded1;
    private final long timeUtilCameraLoaded2;

    protected static final String ACTION_ID = "delay-climb";

    private final AtomicBoolean isPhase2Running = new AtomicBoolean(false);  /* 标记当前阶段2是否正在运行的状态位 */
    private final AtomicBoolean isPhase1Running = new AtomicBoolean(false);  /* 标记当前阶段1是否正在运行的状态位 */
    private final AtomicBoolean shouldStopPhase1 = new AtomicBoolean(false);

    private final Action cameraAction;

    /**
     * 子动作，用于实现延迟攀阶段二
     */
    private class CameraAction extends ScheduledAction {

        public CameraAction(String macroId, long interval) {
            super(macroId, interval, 200);
        }

        @Override
        public void schedule(ActionEvent event) throws Exception {
            /* 进入CameraAction时相机应当处关闭状态，因此首先启用相机 */
            if (!isPhase2Running.get()) return;
            enterCamera();
            awaitTimeUtilCameraLoaded2();

            /* 等待相机加载完毕后关闭相机 */
            if (!isPhase2Running.get()) return;
            exitCamera();  /* 退出相机 */
            awaitTimeUtilCameraExited();
        }
    }

    /**
     * 延迟攀爬宏，宏的执行逻辑分为两个阶段：
     * <li> 阶段1：通过躲进掩体键位让小哑巴进入掩体，同时连续快速点击两次使用手机按键掏出相机；等待一段延时(stage1)，确保相机被自动收起
     * 此时点击前进方向键和空格触发攀爬动作，等待微小延迟确定攀爬生效后，二次掏出相机，此时小哑巴处于站立状态；随后取消相机，点击
     * 两次右方向键，确保手机应用处于相机处
     *
     * <li> 阶段2：进入循环，点击鼠标左键进入相机，点击鼠标右键退出相机，退出相机后等待一段时间（约1.5s供小哑巴移动），然后再次点击
     * 左键进入相机，保持该循环，直到用户点击“停止延迟攀爬宏”，此处使用子宏来实现阶段2
     *
     * @param usePhoneKey           使用手机按键
     * @param hideInCoverKey        躲入掩体键位
     * @param triggerInterval       每次关闭相机-重新打开相机循环间隔，决定了延迟攀是否失败和小哑巴能够自由移动的时间
     * @param timeUtilCameraExited  在阶段1当中等待相机退出的时间
     * @param timeUtilCameraLoaded1 在阶段2当中等待相机退出的时间
     */
    public DelayClimbAction(Key usePhoneKey,
                            Key hideInCoverKey,
                            long triggerInterval,
                            long timeUtilCameraExited,
                            long timeUtilCameraLoaded1,
                            long timeUtilCameraLoaded2) {
        super(ACTION_ID);
        this.usePhoneKey = usePhoneKey;
        this.hideInCoverKey = hideInCoverKey;
        this.timeUtilCameraExited = timeUtilCameraExited;
        this.timeUtilCameraLoaded1 = timeUtilCameraLoaded1;
        this.timeUtilCameraLoaded2 = timeUtilCameraLoaded2;

        /* 子动作，通过传入相同的ACTION_ID来获取同一个robot对象 */
        this.cameraAction = new CameraAction(ACTION_ID, triggerInterval);

        robot = RobotFactory.getRobot(ACTION_ID);
    }

    /* 在进入循环之前的逻辑，该方法被用于执行宏的阶段1 */
    @Override
    public void activate(ActionEvent event) throws Exception {
        /* 阶段一处于未运行状态，将阶段一置为运行状态 */
        if (isPhase1Running.compareAndSet(false, true)) {

            /* 如果阶段二此时正处于运行状态，那么打断阶段二，同时直接退出 */
            if (isPhase2Running.compareAndSet(true, false)) {
                this.cameraAction.deactivate(event);
                return;
            }

            /* 当前阶段二未运行，那么执行阶段一并尝试切入阶段二 */

            /* 进入掩体并打开相机 */
            try {
                pressHideInCoverKey();
                setupCamera();  /* 相机会自动消失 */
                awaitTimeUtilCameraLoaded1();  /* 此处为第一次打开相机，因此等待时间应当较长一些 */

                if (shouldStopPhase1.get()) return;

                /* 按住W键位并点击空格 */
                holdWAndPressSpace();
                setupCamera();  /* 掏出手机并打开相机 */  // TODO: 第一次启动游戏时会遇到该行无法执行的情况，推测解决方法是增加延时
                awaitTimeUtilCameraLoaded2();  /* 此后打开相机使用延迟2 */

                if (shouldStopPhase1.get()) return;

                exitCamera();  /* 点击右键关闭相机并进入循环 */
                awaitTimeUtilCameraExited();
                selectCamera();  /* 选择相机 */

                if (shouldStopPhase1.get()) return;
                if (isPhase2Running.compareAndSet(false, true)) {
                    cameraAction.activate(event);
                }

            } finally {
                isPhase1Running.set(false);
                shouldStopPhase1.set(false);
            }

        } else {
            Logger.lowLevelDebug("Phase1 is running, try to stop");
            shouldStopPhase1.set(true);
        }
    }


    @Override
    public void deactivate(ActionEvent event) throws Exception {
        /* 由于延迟攀Action采用子时间，而不是子宏，子时间并不会被纳入MacroRegistry中得到挂起信号，因此
         *  需要由父动作管理，当挂起时deactivate会被调用，此时由父动作停止子动作 */
        if (isPhase2Running.compareAndSet(true, false)) {
            this.cameraAction.deactivate(event);
        }
    }

    private void holdWAndPressSpace() throws Exception {
        try {
            robot.keyPress(new Key(KeyCode.W));
            Thread.sleep(30);
            robot.simulate(new Key(KeyCode.SPACE));
            Thread.sleep(30);
        } finally {
            robot.keyRelease(new Key(KeyCode.W));
            Thread.sleep(30);
        }
    }

    private void selectCamera() throws Exception {
        robot.simulate(new Key(KeyCode.LEFT));
        Thread.sleep(50);
        robot.simulate(new Key(KeyCode.DOWN));
        Thread.sleep(50);
    }

    private void pressHideInCoverKey() throws Exception {
        robot.simulate(this.hideInCoverKey);
        Thread.sleep(30);
    }

    /* 连续点击两次使用手机键来打开相机 */
    private void setupCamera() throws Exception {
        robot.simulate(this.usePhoneKey);
        Thread.sleep(30);
        robot.simulate(this.usePhoneKey);
        Thread.sleep(30);
    }

    private void exitCamera() throws Exception {
        robot.simulate(new Key(KeyCode.ESCAPE));
        Thread.sleep(30);
    }

    private void enterCamera() throws Exception {
        robot.simulate(new Key(KeyCode.ENTER));
        Thread.sleep(30);
    }

    private void awaitTimeUtilCameraExited() throws InterruptedException {
        Thread.sleep(timeUtilCameraExited);
    }

    private void awaitTimeUtilCameraLoaded1() throws InterruptedException {
        Thread.sleep(timeUtilCameraLoaded1);
    }

    private void awaitTimeUtilCameraLoaded2() throws InterruptedException {
        Thread.sleep(timeUtilCameraLoaded2);
    }
}
