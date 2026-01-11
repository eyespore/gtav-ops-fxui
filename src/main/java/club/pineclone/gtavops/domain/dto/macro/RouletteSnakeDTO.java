package club.pineclone.gtavops.domain.dto.macro;

import io.vproxy.vfx.entity.input.Key;

/**
 * 轮盘零食宏创建数据模型
 * @param snakeKey 零食键位
 * @param activateKey 触发键位
 * @param weaponWheelKey 武器轮盘键位
 * @param triggerInterval 触发间隔
 */
public record RouletteSnakeDTO(
        Key snakeKey,
        Key activateKey,
        Key weaponWheelKey,
        PercentageDTO triggerInterval
) {
}
