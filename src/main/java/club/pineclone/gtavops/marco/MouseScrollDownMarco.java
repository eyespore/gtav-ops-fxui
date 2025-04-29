package club.pineclone.gtavops.marco;

public class MouseScrollDownMarco extends ButtonListener {

    public MouseScrollDownMarco(int listenedButton) {
        super(listenedButton);
    }

    @Override
    protected void execute() {
        try {
            robot.mouseWheel(1);
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
