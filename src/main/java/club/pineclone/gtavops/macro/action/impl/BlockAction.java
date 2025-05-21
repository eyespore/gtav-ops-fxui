package club.pineclone.gtavops.macro.action.impl;

import club.pineclone.gtavops.macro.action.Action;
import club.pineclone.gtavops.macro.action.ActionEvent;

import java.util.HashMap;
import java.util.Map;

public class BlockAction extends Action {
    private final static String ACTION_ID = "action-ext";
    private final long blockDuration;

    private boolean blocked = false;
    private long blockStartTime;

    private AbstractBlockState state;
    private final Map<State, AbstractBlockState> map = new HashMap<>();

    public BlockAction() {
        this(0);
    }

    public BlockAction(long blockDuration) {
        super(ACTION_ID);
        this.map.put(State.ACTIVE, new ActiveState(this));
        this.map.put(State.INACTIVE, new InactiveState(this));

        this.blockDuration = blockDuration;
        this.setState(State.INACTIVE);
    }

    private void setState(State state) {
        this.state = map.get(state);
    }

    public void enable() {
        this.setState(State.ACTIVE);
    }

    public void disable() {
        this.setState(State.INACTIVE);
    }

    @Override
    public void activate(ActionEvent event) {
        this.state.activate();
    }

    @Override
    public void deactivate(ActionEvent event) {
        this.state.deactivate();
    }

    public boolean isBlocked() {
        return this.state.isBlocked();
    }

    public interface BlockState {
        default void activate() {}
        default void deactivate() {}
        default boolean isBlocked() { return false; }
    }

    public abstract static class AbstractBlockState implements BlockState {
        protected BlockAction context;

        public AbstractBlockState(BlockAction context) {
            this.context = context;
        }
    }

    public static class ActiveState extends AbstractBlockState {
        public ActiveState(BlockAction context) {
            super(context);
        }

        @Override
        public void activate() {
            context.blocked = true;
        }

        @Override
        public void deactivate() {
            context.blocked = false;
            context.blockStartTime = System.currentTimeMillis();
        }

        @Override
        public boolean isBlocked() {
            if (context.blocked) return true;
            return (System.currentTimeMillis() - context.blockStartTime) < context.blockDuration;
        }
    }

    public static class InactiveState extends AbstractBlockState {
        public InactiveState(BlockAction context) {
            super(context);
            context.blocked = false;  // 重置状态
        }
    }

    public enum State {
        ACTIVE,
        INACTIVE,
    }
}
