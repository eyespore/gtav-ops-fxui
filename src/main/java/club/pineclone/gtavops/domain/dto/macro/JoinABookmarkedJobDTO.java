package club.pineclone.gtavops.domain.dto.macro;

import io.vproxy.vfx.entity.input.Key;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class JoinABookmarkedJobDTO extends BetterPMenuDTO {

    private final Key activateKey;
    private final PercentageDTO timeUtilJobListLoaded;

    public JoinABookmarkedJobDTO(PercentageDTO mouseScrollInterval,
                                 PercentageDTO keyPressInterval,
                                 PercentageDTO timeUtilPMenuLoaded,
                                 Key activateKey,
                                 PercentageDTO timeUtilJobListLoaded) {
        super(mouseScrollInterval, keyPressInterval, timeUtilPMenuLoaded);
        this.activateKey = activateKey;
        this.timeUtilJobListLoaded = timeUtilJobListLoaded;
    }
}
