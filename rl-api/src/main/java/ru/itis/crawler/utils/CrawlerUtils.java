package ru.itis.crawler.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Optional;

@Slf4j
@UtilityClass
public class CrawlerUtils {

    public static final String BASE_URL = "https://market.yandex.ru";
    public static final String SOURCE = "Яндекс Маркет";
    public static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            log.error("Exception when thread sleep: ", e);
        }
    }

    public static Double strToDouble(String str) {
        try {
            return Optional.ofNullable(str)
                    .map(CrawlerUtils::clearNumberStr)
                    .map(Double::parseDouble)
                    .orElse(NumberUtils.DOUBLE_MINUS_ONE);
        } catch (Exception e) {
            log.error("Failed parse {} to double: {}", str, e.getMessage());
            return null;
        }
    }

    public static Integer strToInteger(String str) {
        try {
            return Optional.ofNullable(str)
                    .map(CrawlerUtils::clearNumberStr)
                    .map(Integer::parseInt)
                    .orElse(NumberUtils.INTEGER_MINUS_ONE);
        } catch (Exception e) {
            log.error("Failed parse {} to integer: {}", str, e.getMessage());
            return null;
        }
    }

    private static String clearNumberStr(String str) {
        return StringUtils.strip(str.replaceAll("[^\\d.]+",""), ".");
    }
}
