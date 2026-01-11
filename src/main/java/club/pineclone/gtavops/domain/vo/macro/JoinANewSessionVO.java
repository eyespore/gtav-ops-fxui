package club.pineclone.gtavops.domain.vo.macro;

import club.pineclone.gtavops.common.SessionType;
import io.vproxy.vfx.entity.input.Key;
import lombok.Getter;

@Getter
public class JoinANewSessionVO extends BetterPMenuVO {

    private final Key activateKey;
    private final SessionType sessionType;

    public JoinANewSessionVO(PercentageVO mouseScrollInterval,
                             PercentageVO keyPressInterval,
                             PercentageVO timeUtilPMenuLoaded,
                             Key activateKey,
                             SessionType sessionType) {
        super(mouseScrollInterval, keyPressInterval, timeUtilPMenuLoaded);
        this.activateKey = activateKey;
        this.sessionType = sessionType;
    }
}
