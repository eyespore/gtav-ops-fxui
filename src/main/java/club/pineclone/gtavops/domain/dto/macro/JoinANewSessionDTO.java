package club.pineclone.gtavops.domain.dto.macro;

import club.pineclone.gtavops.common.SessionType;
import io.vproxy.vfx.entity.input.Key;
import lombok.Getter;

@Getter
public class JoinANewSessionDTO extends BetterPMenuDTO {

    private final Key activateKey;
    private final SessionType sessionType;

    public JoinANewSessionDTO(PercentageDTO mouseScrollInterval,
                              PercentageDTO keyPressInterval,
                              PercentageDTO timeUtilPMenuLoaded,
                              Key activateKey,
                              SessionType sessionType) {
        super(mouseScrollInterval, keyPressInterval, timeUtilPMenuLoaded);
        this.activateKey = activateKey;
        this.sessionType = sessionType;
    }
}
