package club.pineclone.gtavops.domain.mapper;

import club.pineclone.gtavops.domain.vo.macro.PercentageVO;
import club.pineclone.gtavops.domain.dto.macro.PercentageDTO;
import org.mapstruct.Mapper;

@Mapper
public interface PercentageMapper {

    PercentageVO toVO(PercentageDTO dto);

    PercentageDTO toDTO(PercentageVO vo);

}
