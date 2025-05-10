package club.pineclone.gtavops.macro.action.impl;

import club.pineclone.gtavops.macro.action.Action;
import club.pineclone.gtavops.macro.action.ActionEvent;
import club.pineclone.gtavops.macro.action.robot.RobotFactory;
import club.pineclone.gtavops.macro.action.robot.VCRobotAdapter;
import io.vproxy.vfx.entity.input.Key;
import javafx.scene.input.MouseButton;

public class HoldLButtonAction extends Action {

    private static final String ACTION_ID = "better-l-button";
    private boolean running = false;

    private final VCRobotAdapter robot;
    private final Key leftButton = new Key(MouseButton.PRIMARY);

    public HoldLButtonAction() {
        super(ACTION_ID);
        robot = RobotFactory.getRobot(ACTION_ID);
    }

    @Override
    public void activate(ActionEvent event) {
        if (running) return;
        running = true;
        robot.mousePress(leftButton);
    }

    @Override
    public void deactivate(ActionEvent event) {
        if (!running) return;
        robot.mouseRelease(leftButton);
        running = false;
    }
}
