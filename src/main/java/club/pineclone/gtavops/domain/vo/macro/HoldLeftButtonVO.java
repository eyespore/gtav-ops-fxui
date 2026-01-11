package club.pineclone.gtavops.domain.vo.macro;

import club.pineclone.gtavops.common.TriggerMode;
import io.vproxy.vfx.entity.input.Key;

/**
 * 辅助按住鼠标左键宏数据模型
 * @param activateMethod 触发模式
 * @param activateKey 触发键位
 */
public record HoldLeftButtonVO(
        TriggerMode activateMethod,
        Key activateKey
) {
}
