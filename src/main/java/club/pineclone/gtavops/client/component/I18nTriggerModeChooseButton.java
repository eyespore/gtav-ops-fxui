package club.pineclone.gtavops.client.component;

import club.pineclone.gtavops.client.i18n.I18nContext;
import club.pineclone.gtavops.client.i18n.I18nText;
import club.pineclone.gtavops.common.TriggerMode;

import java.util.LinkedHashMap;

/* 触发模式选择器，供选择触发模式为单击触发、切换出发或按住触发 */
public class I18nTriggerModeChooseButton extends I18nOptionButton<TriggerMode> {

    public static final int FLAG_WITH_TOGGLE = 0x0001;   // 1 << 0, 切换触发
    public static final int FLAG_WITH_HOLD = 0x0002;  // 1 << 1, 按住触发
    public static final int FLAG_WITH_CLICK = 0x0004;  // 1 << 2, 点击触发

    public I18nTriggerModeChooseButton(I18nContext i18nContext, int flags) {
        super(i18nContext, new LinkedHashMap<>() {{
            if ((flags & FLAG_WITH_TOGGLE) != 0) {
                this.put(TriggerMode.TOGGLE, I18nText.of(i -> i.common.toggle));
            }

            if ((flags & FLAG_WITH_HOLD) != 0) {
                this.put(TriggerMode.HOLD, I18nText.of(i -> i.common.hold));
            }

            if ((flags & FLAG_WITH_CLICK) != 0) {
                this.put(TriggerMode.CLICK, I18nText.of(i -> i.common.click));
            }
        }});
    }
}
