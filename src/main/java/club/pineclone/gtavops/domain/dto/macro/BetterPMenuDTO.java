package club.pineclone.gtavops.domain.dto.macro;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BetterPMenuDTO {

    protected final PercentageDTO mouseScrollInterval;
    protected final PercentageDTO keyPressInterval;
    protected final PercentageDTO timeUtilPMenuLoaded;

}
