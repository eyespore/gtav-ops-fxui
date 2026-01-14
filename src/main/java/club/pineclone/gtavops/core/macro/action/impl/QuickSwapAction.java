package club.pineclone.gtavops.core.macro.action.impl;

import club.pineclone.gtavops.core.macro.trigger.TriggerEvent;
import club.pineclone.gtavops.core.macro.action.Action;
import club.pineclone.gtavops.core.macro.MacroEvent;
import club.pineclone.gtavops.core.macro.action.impl.actionext.BlockAction;
import club.pineclone.gtavops.core.macro.action.robot.RobotFactory;
import club.pineclone.gtavops.core.macro.action.robot.VCRobotAdapter;

import io.vproxy.base.util.LogType;
import io.vproxy.base.util.Logger;
import io.vproxy.vfx.entity.input.Key;
import javafx.scene.input.MouseButton;

import java.util.HashMap;
import java.util.Map;

/* 快速切枪 */
// TODO: 引入优先级
public class QuickSwapAction extends Action {

    private final VCRobotAdapter robot;
    private final Map<Key, Key> sourceToTargetMap = new HashMap<>();

    protected static final String ACTION_ID = "quick-swap";
    private final Key leftButtonKey = new Key(MouseButton.PRIMARY);

    private final Key blockKey;
    private final BlockAction blockAction;

    public QuickSwapAction(Map<Key, Key> sourceToTargetMap, Key blockKey ,long blockDuration) {
        super(ACTION_ID);
        this.sourceToTargetMap.putAll(sourceToTargetMap);
        this.robot = RobotFactory.getRobot(ACTION_ID);

        this.blockAction = new BlockAction(blockDuration);
        this.blockKey = blockKey;
    }

    @Override
    public void activate(MacroEvent event) {
        /* 触发屏蔽 */
        if (blockKey != null && blockKey.equals(event.getTriggerEvent().getInputSourceEvent().getKey())) {
            blockAction.activate(event);
            return;
        }

        if (blockAction.isBlocked()) return;

        try {
            Thread.sleep(20);
            Key key = event.getTriggerEvent().getInputSourceEvent().getKey();
            robot.simulate(sourceToTargetMap.get(key));  /* 切换到枪 */
            Thread.sleep(20);
            robot.simulate(leftButtonKey);  /* 点左键选择 */
        } catch (Exception e) {
            Logger.error(LogType.SYS_ERROR, e.getMessage());
        }
    }

    @Override
    public void deactivate(MacroEvent event) {
        TriggerEvent triggerEvent = event.getTriggerEvent();
        if (triggerEvent != null && blockKey != null) {
            if (blockKey.equals(triggerEvent.getInputSourceEvent().getKey())) {
                blockAction.deactivate(event);
            }
        }
    }
}
