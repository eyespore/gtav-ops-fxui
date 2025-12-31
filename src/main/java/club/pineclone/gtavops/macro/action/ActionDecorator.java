package club.pineclone.gtavops.macro.action;

public class ActionDecorator extends Action {

    protected final Action delegate;

    public ActionDecorator(final Action delegate) {
        super(delegate.actionId);
        this.delegate = delegate;
    }

    @Override
    public void activate(ActionEvent event) {
        delegate.activate(event);
    }

    @Override
    public void deactivate(ActionEvent event) {
        delegate.deactivate(event);
    }
}
