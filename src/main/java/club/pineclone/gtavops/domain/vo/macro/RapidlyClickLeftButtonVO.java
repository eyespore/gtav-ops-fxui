package club.pineclone.gtavops.domain.vo.macro;

import club.pineclone.gtavops.common.TriggerMode;
import io.vproxy.vfx.entity.input.Key;

/**
 * 辅助快速点击鼠标左键宏创建数据模型
 * @param activateMethod 触发模式
 * @param activateKey 触发按键
 * @param triggerInterval 触发间隔
 */
public record RapidlyClickLeftButtonVO(
        TriggerMode activateMethod,
        Key activateKey,
        PercentageVO triggerInterval
) {
}
