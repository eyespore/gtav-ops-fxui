package club.pineclone.gtavops.domain.vo.macro;

/**
 * 宏创建输入对象，基于name字段指定创建的宏类型，Controller会基于类型进行反序列化，封装到MacroCreationDTO，
 * 通过泛型传递宏类型供策略进行判断
 * @param <T>
 */
public record MacroCreationVO(String type, Object dto) {
}
