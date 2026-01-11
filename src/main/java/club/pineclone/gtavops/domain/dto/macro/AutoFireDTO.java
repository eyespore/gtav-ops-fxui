package club.pineclone.gtavops.domain.dto.macro;

import club.pineclone.gtavops.common.TriggerMode;
import io.vproxy.vfx.entity.input.Key;

public record AutoFireDTO(
        Key activateKey,
        TriggerMode activateMethod,
        Key heavyWeaponKey,
        Key specialWeaponKey,
        PercentageDTO triggerInterval,
        PercentageDTO mousePressInterval
) {
}
