package club.pineclone.gtavops.core.macro;

import club.pineclone.gtavops.core.macro.trigger.TriggerEvent;
import io.vproxy.base.util.anno.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MacroEvent {

    @Nullable private final TriggerEvent triggerEvent;
    private final MacroContext macroContext;  /* 宏上下文 */

    public MacroEvent(TriggerEvent triggerEvent, MacroContext macroContext) {
        this.triggerEvent = triggerEvent;
        this.macroContext = macroContext;
    }

    /* TriggerEvent 派生 ActionEvent */
    public static MacroEvent of(TriggerEvent event, Macro macro) {
        MacroContext context = new MacroContext(macro.getStatus(), macro.getExecutionStatus());
        return new MacroEvent(event, context);
    }

    /* 宏上下文 */
    @AllArgsConstructor
    @Getter
    public static class MacroContext {
        private Macro.MacroStatus status;  /* 是否被停止 */
        private Macro.MacroExecutionStatus executionStatus;
    }
}
