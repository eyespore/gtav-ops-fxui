package club.pineclone.gtavops.domain.mapper;

import club.pineclone.gtavops.domain.entity.MacroConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MacroConfigMapper {

    /**
     * 为一份MacroConfig创建拷贝，面向Dao层
     * @param source 拷贝数据源
     * @return 拷贝结果
     */
    MacroConfig copy(MacroConfig source);

}
