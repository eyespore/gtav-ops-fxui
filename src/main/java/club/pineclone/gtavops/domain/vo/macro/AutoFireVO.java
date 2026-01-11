package club.pineclone.gtavops.domain.vo.macro;

import club.pineclone.gtavops.common.TriggerMode;
import io.vproxy.vfx.entity.input.Key;

public record AutoFireVO(
        Key activateKey,
        TriggerMode activateMethod,
        Key heavyWeaponKey,
        Key specialWeaponKey,
        PercentageVO triggerInterval,
        PercentageVO mousePressInterval
) {
}
