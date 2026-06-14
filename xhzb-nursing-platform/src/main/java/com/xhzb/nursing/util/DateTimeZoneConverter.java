package com.xhzb.nursing.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 日期时区转换工具类
 */
public class DateTimeZoneConverter {
    // 预定义常用时区
    public static final ZoneId UTC_ZONE = ZoneOffset.UTC;
    public static final ZoneId SHANGHAI_ZONE = ZoneId.of("Asia/Shanghai");
    
    // 私有构造防止实例化
    private DateTimeZoneConverter() {}

    /**
     * 转换LocalDateTime时区（明确时区上下文）
     * @param sourceTime 源时间（无时区信息）
     * @param sourceZone 源时间所在的时区
     * @param targetZone 目标时区
     * @return 转换后的LocalDateTime
     */
    public static LocalDateTime convert(LocalDateTime sourceTime, 
                                       ZoneId sourceZone,
                                       ZoneId targetZone) {
        return sourceTime.atZone(sourceZone)
                        .withZoneSameInstant(targetZone)
                        .toLocalDateTime();
    }

    /**
     * 安全转换方法（Optional包装）
     * @param sourceTime 可为null的源时间
     * @param sourceZone 源时区
     * @param targetZone 目标时区
     * @return Optional包装的转换结果
     */
    public static Optional<LocalDateTime> safeConvert(LocalDateTime sourceTime,
                                                     ZoneId sourceZone,
                                                     ZoneId targetZone) {
        return Optional.ofNullable(sourceTime)
                      .map(time -> convert(time, sourceZone, targetZone));
    }

    /**
     * UTC转上海时区的快捷方法
     * @param utcTime UTC时间的LocalDateTime
     * @return 上海时区本地时间
     */
    public static LocalDateTime utcToShanghai(LocalDateTime utcTime) {
        return convert(utcTime, UTC_ZONE, SHANGHAI_ZONE);
    }

    /**
     * 带格式解析的完整流程
     * @param timeStr 时间字符串
     * @param pattern 格式模式（需匹配timeStr）
     * @param sourceZone 字符串对应的时区
     * @param targetZone 目标时区
     * @return Optional包装的转换结果
     */
    public static Optional<LocalDateTime> parseAndConvert(String timeStr,
                                                         String pattern,
                                                         ZoneId sourceZone,
                                                         ZoneId targetZone) {
        try {
            LocalDateTime sourceTime = LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
            return Optional.of(convert(sourceTime, sourceZone, targetZone));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}