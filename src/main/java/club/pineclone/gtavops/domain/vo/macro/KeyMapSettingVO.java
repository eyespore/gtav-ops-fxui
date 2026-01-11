package club.pineclone.gtavops.domain.vo.macro;

import io.vproxy.vfx.entity.input.Key;

public record KeyMapSettingVO (
        Boolean enabled,
        Key sourceKey,
        Key targetKey
) {
}
