package club.pineclone.gtavops.macro;

import club.pineclone.gtavops.AppLifecycleAware;
import club.pineclone.gtavops.common.TriggerMode;
import club.pineclone.gtavops.domain.dto.macro.*;
import club.pineclone.gtavops.macro.action.Action;
import club.pineclone.gtavops.macro.action.impl.*;
import club.pineclone.gtavops.macro.action.impl.betterlbutton.HoldLButtonAction;
import club.pineclone.gtavops.macro.action.impl.betterlbutton.RapidlyClickLButtonAction;
import club.pineclone.gtavops.macro.action.impl.betterlbutton.RemapLButtonAction;
import club.pineclone.gtavops.macro.action.impl.bettermmenu.AutoSnakeAction;
import club.pineclone.gtavops.macro.action.impl.bettermmenu.StartEngineAction;
import club.pineclone.gtavops.macro.action.impl.betterpmenu.JoinABookmarkedJobAction;
import club.pineclone.gtavops.macro.action.impl.betterpmenu.JoinANewSessionAction;
import club.pineclone.gtavops.macro.action.impl.swapglitch.SwapGlitchAction;
import club.pineclone.gtavops.macro.action.impl.swapglitch.SwapMeleeAction;
import club.pineclone.gtavops.macro.action.impl.swapglitch.SwapRangedAction;
import club.pineclone.gtavops.macro.trigger.Trigger;
import club.pineclone.gtavops.macro.trigger.TriggerFactory;
import club.pineclone.gtavops.macro.trigger.TriggerIdentity;
import io.vproxy.vfx.entity.input.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MacroFactory {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /* 切枪偷速宏 */
    public final Strategy<SwapGlitchDTO> swapGlitch = dto -> {
        TriggerIdentity defaultIdentity = TriggerIdentity.of(dto.activateMethod(), dto.activateKey());
        Trigger trigger = TriggerFactory.simple(defaultIdentity);  /* 触发器 */

        Action action = new SwapGlitchAction(
                dto.weaponWheelKey(), dto.triggerInterval().parseLong());  /* 基础执行器 */

        if (dto.enableSwapMeleeWeapon()) {  /* 进入偷速切换近战武器 */
            action = new SwapMeleeAction(action,
                    dto.meleeWeaponKey(), dto.postSwapMeleeWeaponDelay().parseLong());
        }

        if (dto.enableSwapRangedWeapon() && !dto.keyMapping().isEmpty()) {  /* 结束偷速切换远程武器 */
            Map<Key, Key> sourceToTargetMap = new HashMap<>();
            action = new SwapRangedAction(action,
                    dto.defaultRangedWeaponKey(),
                    dto.swapDefaultRangedWeaponOnEmpty(),
                    dto.keyMapping());

            /* 映射表不为空，基于子动作实现武器切换 */
            log.debug("Register union trigger for swap glitch macro");
            trigger = TriggerFactory.union(
                    TriggerIdentity.of(TriggerMode.CLICK, sourceToTargetMap.keySet()),
                    defaultIdentity);
        }
//                /* 启用映射1 */
//                if (swapRangedSetting.enableMapping1)
//                    sourceToTargetMap.put(swapRangedSetting.mapping1SourceKey, swapRangedSetting.mapping1TargetKey);
//                /* 启用映射2 */
//                if (swapRangedSetting.enableMapping2)
//                    sourceToTargetMap.put(swapRangedSetting.mapping2SourceKey, swapRangedSetting.mapping2TargetKey);
//                /* 启用映射3 */
//                if (swapRangedSetting.enableMapping3)
//                    sourceToTargetMap.put(swapRangedSetting.mapping3SourceKey, swapRangedSetting.mapping3TargetKey);
//                /* 启用映射4 */
//                if (swapRangedSetting.enableMapping4)
//                    sourceToTargetMap.put(swapRangedSetting.mapping4SourceKey, swapRangedSetting.mapping4TargetKey);
//                /* 启用映射5 */
//                if (swapRangedSetting.enableMapping5)
//                    sourceToTargetMap.put(swapRangedSetting.mapping5SourceKey, swapRangedSetting.mapping5TargetKey);
//
//                /* 空值映射 */
//                if (swapRangedSetting.enableClearKey)
//                    sourceToTargetMap.put(swapRangedSetting.clearKey, null);
        return new SimpleMacro(trigger, action);
    };

    /* 轮盘零食宏 */
    public final Strategy<RouletteSnakeDTO> rouletteSnake = dto -> {
        Trigger trigger = TriggerFactory.composite(
                TriggerIdentity.of(TriggerMode.HOLD, dto.activateKey()),
                TriggerIdentity.of(TriggerMode.HOLD, dto.weaponWheelKey()));

        Action action = new RouletteSnakeAction(dto.triggerInterval().parseLong(), dto.snakeKey());
        return new SimpleMacro(trigger, action);
    };

    /* AD 摇宏 */
    public final Strategy<ADSwingDTO> adSwing = dto -> {
        TriggerMode mode = dto.activateMethod();  /* 激活模式 */
        Key activatekey = dto.activateKey();  /* 激活热键 */

        Trigger trigger;
        TriggerIdentity defaultIdentity = TriggerIdentity.of(mode, activatekey);
        if (dto.enableSafetyKey()) {  /* 启用保险键 */
            trigger = TriggerFactory.composite(defaultIdentity, TriggerIdentity.of(mode, dto.safetyKey()));
        } else trigger = TriggerFactory.simple(defaultIdentity);  /* 触发器 */

        Action action = new ADSwingAction(dto.triggerInterval().parseLong(),
                dto.moveLeftKey(), dto.moveRightKey());

        return new SimpleMacro(trigger, action);
    };

    /* 近战偷速宏 */
    public final Strategy<MeleeGlitchDTO> meleeGlitch = dto -> {
        TriggerMode mode = dto.activateMethod();  /* 激活模式 切换执行 or 按住执行 */
        Key activatekey = dto.activateKey();  /* 激活热键 */

        Trigger trigger;
        TriggerIdentity defaultIdentity = TriggerIdentity.of(mode, activatekey);
        if (dto.enableSafetyKey()) {  /* 启用保险键 */
            trigger = TriggerFactory.composite(defaultIdentity, TriggerIdentity.of(mode, dto.safetyKey()));
        } else trigger = TriggerFactory.simple(defaultIdentity);  /* 触发器 */

        long triggerInterval = dto.triggerInterval().parseLong();
        Action action = new MeleeGlitchAction(triggerInterval, dto.meleeSnakeScrollKey());

        return new SimpleMacro(trigger, action);
    };

    /* 更好 M 菜单宏 */
    /* 快速点火宏 */
    public final Strategy<StartEngineDTO> startEngine = dto -> {
        Key activateKey = dto.getActivateKey();
        boolean enableDoubleClickToOpenDoor = dto.getEnableDoubleClickToOpenDoor();
        long doubleClickInterval = dto.getDoubleClickDetectInterval().parseLong();

        Trigger trigger;
        if (enableDoubleClickToOpenDoor)
            trigger = TriggerFactory.simple(TriggerIdentity.ofDoubleClick(doubleClickInterval, activateKey));  // 启用双击触发
        else trigger = TriggerFactory.simple(TriggerIdentity.ofClick(activateKey));  // 仅启用单击触发

        Action action = new StartEngineAction(
                dto.getMenuKey(),
                dto.getMouseScrollInterval().parseLong(),
                dto.getKeyPressInterval().parseLong(),
                dto.getTimeUtilMMenuLoaded().parseLong(),
                enableDoubleClickToOpenDoor);

        return new SimpleMacro(trigger, action);
    };

    /* 自动 M 菜单零食宏 */
    public final Strategy<AutoSnakeDTO> autoSnake = dto -> {
        Trigger trigger = TriggerFactory.simple(TriggerIdentity.ofHold(dto.getActivateKey()));
        Action action = new AutoSnakeAction(
                dto.getMenuKey(),
                dto.getMouseScrollInterval().parseLong(),
                dto.getKeyPressInterval().parseLong(),
                dto.getTimeUtilMMenuLoaded().parseLong(),
                dto.getEnableRefillVest(),
                dto.getEnableKeepMMenu());

        return new SimpleMacro(trigger, action);
    };

    /* 更好左键宏 */
    /* 辅助按住鼠标左键宏 */
    public final Strategy<HoldLeftButtonDTO> holdLeftButton = dto ->
            new SimpleMacro(TriggerFactory.simple(TriggerIdentity.of(dto.activateMethod(), dto.activateKey())), new HoldLButtonAction());

    /* 辅助点按鼠标左键宏 */
    public final Strategy<RapidlyClickLeftButtonDTO> rapidlyClickLeftButton = dto ->
            new SimpleMacro(TriggerFactory.simple(TriggerIdentity.of(dto.activateMethod(), dto.activateKey())),
                    new RapidlyClickLButtonAction(dto.triggerInterval().parseLong()));

    /* 鼠标左键重映射宏 */
    public final Strategy<RemapLeftButtonDTO> remapLeftButton = dto ->
            new SimpleMacro(TriggerFactory.simple(TriggerIdentity.of(TriggerMode.HOLD, dto.mapKey())), new RemapLButtonAction());

    /* 切枪自动确认宏 */
    public final Strategy<QuickSwapDTO> quickSwap = dto -> {
        // TODO: 对Map判空处理，避免NPE
        Action action = new QuickSwapAction(dto.keyMapping(), dto.blockKey(), dto.blockDuration().parseLong());

        Trigger trigger;
        TriggerIdentity defaultIdentify = TriggerIdentity.of(TriggerMode.CLICK, dto.keyMapping().keySet());

        if (!dto.enableBlockKey()) {  /* 未启用屏蔽键 */
            trigger = TriggerFactory.simple(defaultIdentify);
        } else {  /* 启用屏蔽键 */
            trigger = TriggerFactory.union(
                    defaultIdentify,
                    TriggerIdentity.of(TriggerMode.HOLD, dto.blockKey()));
        }

        return new SimpleMacro(trigger, action);
    };

    /* 延迟攀宏 */
    public final Strategy<DelayClimbDTO> delayClimb = dto -> {
        Trigger trigger = TriggerFactory.simple(TriggerIdentity.of(TriggerMode.CLICK, dto.toggleDelayClimbKey()));
        Action action = new DelayClimbAction(
                dto.usePhoneKey(),
                dto.hideInCoverKey(),
                dto.triggerInterval().parseLong(),
                dto.timeUtilCameraExited().parseLong(),
                dto.timeUtilCameraLoaded1().parseLong(),
                dto.timeUtilCameraLoaded2().parseLong(),
                dto.hideInCoverOnExit());

        return new SimpleMacro(trigger, action);
    };

    /* 更好 P 菜单宏 */
    /* 加入新战局宏 */
    public final Strategy<JoinANewSessionDTO> joinANewSession = dto -> {
        Trigger trigger = TriggerFactory.simple(TriggerIdentity.of(TriggerMode.CLICK, dto.getActivateKey()));
        Action action = new JoinANewSessionAction(
                dto.getSessionType(),
                dto.getMouseScrollInterval().parseLong(),
                dto.getKeyPressInterval().parseLong(),
                dto.getTimeUtilPMenuLoaded().parseLong());
        return new SimpleMacro(trigger, action);
    };

    /* 加入已收藏差事宏 */
    public final Strategy<JoinABookmarkedJobDTO> joinABookmarkedJob = dto -> {
        Trigger trigger = TriggerFactory.simple(TriggerIdentity.of(TriggerMode.CLICK, dto.getActivateKey()));
        Action action = new JoinABookmarkedJobAction(
                dto.getMouseScrollInterval().parseLong(),
                dto.getKeyPressInterval().parseLong(),
                dto.getTimeUtilPMenuLoaded().parseLong(),
                dto.getTimeUtilPMenuLoaded().parseLong());
        return new SimpleMacro(trigger, action);
    };

    public final Strategy<AutoFireDTO> autoFire = dto -> {
        Trigger trigger = TriggerFactory.simple(TriggerIdentity.of(dto.activateMethod(), dto.activateKey()));
        //            Action action = new AutoFireAction(heavyWeaponKey, specialWeaponKey, triggerInterval, mousePressInterval);
        //            return createSimpleMacro(trigger, action);
        return null;
    };

    /* DTO 数据类到创建策略的具体映射 */
    private final Map<Class<?>, Strategy<?>> STRATEGY_MAP = new HashMap<>(){{
        put(SwapGlitchDTO.class, swapGlitch);
        put(RouletteSnakeDTO.class, rouletteSnake);
        put(ADSwingDTO.class, adSwing);
        put(MeleeGlitchDTO.class, meleeGlitch);
        put(StartEngineDTO.class, startEngine);
        put(AutoSnakeDTO.class, autoSnake);
        put(HoldLeftButtonDTO.class, holdLeftButton);
        put(RapidlyClickLeftButtonDTO.class, rapidlyClickLeftButton);
        put(RemapLeftButtonDTO.class, remapLeftButton);
        put(QuickSwapDTO.class, quickSwap);
        put(DelayClimbDTO.class, delayClimb);
        put(JoinANewSessionDTO.class, joinANewSession);
        put(JoinABookmarkedJobDTO.class, joinABookmarkedJob);
        put(AutoFireDTO.class, autoFire);
    }};

    @SuppressWarnings("unchecked")
    private <T> Strategy<T> getStrategy(Class<T> dtoClass) {
        Strategy<T> strategy = (Strategy<T>) STRATEGY_MAP.get(dtoClass);
        if (strategy == null) {
            throw new IllegalArgumentException("No MacroCreationStrategy registered for DTO: " + dtoClass);
        }
        return strategy;
    }

    @SuppressWarnings("unchecked")
    public <T> Strategy<T> getStrategy(MacroCreationDTO<T> creationDTO) {
        T dto = creationDTO.dto();
        if (dto == null) {
            throw new IllegalArgumentException("DTO inside MacroCreationDTO cannot be null");
        }
        Class<T> dtoClass = (Class<T>) dto.getClass();
        return getStrategy(dtoClass);
    }

    /**
     * 宏创建策略
     */
    public interface Strategy<T> extends Function<T, Macro> { }
}
