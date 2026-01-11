package club.pineclone.gtavops.domain.dto.macro;

import io.vproxy.vfx.entity.input.Key;

public record KeyMappingDTO (
        Boolean enabled,
        Key sourceKey,
        Key targetKey
) {
}
