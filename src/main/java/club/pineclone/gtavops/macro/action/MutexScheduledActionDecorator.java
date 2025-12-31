package club.pineclone.gtavops.macro.action;

/* todo: 引入动作优先级 */
/* 优先级互斥定时动作装饰器 */
@Deprecated
public class MutexScheduledActionDecorator extends ScheduledAction  {

    protected final ScheduledAction delegate;

    public MutexScheduledActionDecorator(final ScheduledAction delegate) {
        super(delegate.getActionId(), delegate.getInterval());
        this.delegate = delegate;
    }

    @Override
    public void schedule(ActionEvent event) {
        delegate.schedule(event);
    }

    @Override
    public boolean beforeSchedule(ActionEvent event) {
        return delegate.beforeSchedule(event);
    }

    @Override
    public void afterSchedule(ActionEvent event) {
        delegate.afterSchedule(event);
    }


}
