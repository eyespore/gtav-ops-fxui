package club.pineclone.gtavops.macro.action.impl;

import club.pineclone.gtavops.macro.action.ActionEvent;
import club.pineclone.gtavops.macro.action.ScheduledAction;
import club.pineclone.gtavops.macro.action.robot.RobotFactory;
import club.pineclone.gtavops.macro.action.robot.VCRobotAdapter;
import io.vproxy.vfx.entity.input.Key;
import javafx.scene.input.MouseButton;

public class RapidlyClickLButtonAction extends ScheduledAction {

    private static final String ACTION_ID = "better-l-button";
    private static final Key leftButton = new Key(MouseButton.PRIMARY);
    private final VCRobotAdapter robot;

    public RapidlyClickLButtonAction(long interval) {
        super(ACTION_ID, interval);
        robot = RobotFactory.getRobot();
    }

    @Override
    public void schedule(ActionEvent event) throws Exception {
        robot.simulate(leftButton);
    }
}
