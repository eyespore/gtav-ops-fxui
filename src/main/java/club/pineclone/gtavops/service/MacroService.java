package club.pineclone.gtavops.service;

import club.pineclone.gtavops.domain.dto.macro.MacroCreationDTO;

import java.util.UUID;

public interface MacroService {

    /**
     * 创建宏实例
     * @param creationDTO 宏创建输入参数
     * @return 宏UUID，宏本身会被注册到MacroRegistry当中，外部通过该UUID继续调用MacroService来操作宏
     * @param <T> 宏实例类型
     */
    public <T> UUID createMacro(MacroCreationDTO<T> creationDTO);
}
