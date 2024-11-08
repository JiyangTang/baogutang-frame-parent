package com.baogutang.frame.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author N1KO
 */
public class AmountUtils {


    public static final int scale = 0;

    private static final Pattern PATTERN = Pattern.compile("-?[0-9]+(.[0-9]+)?$");

    public static Boolean greaterZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public static Boolean lessZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean lessEqZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) <= 0;
    }

    public static boolean greaterEqZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static boolean greaterThan(BigDecimal amount, BigDecimal availableAmount) {
        return amount.compareTo(availableAmount) > 0;
    }

    public static boolean lessThan(BigDecimal amount, BigDecimal availableAmount) {
        return amount.compareTo(availableAmount) < 0;
    }

    public static boolean greaterThanEq(BigDecimal amount, BigDecimal availableAmount) {
        return amount.compareTo(availableAmount) >= 0;
    }

    public static boolean lessThanEq(BigDecimal amount, BigDecimal availableAmount) {
        return amount.compareTo(availableAmount) <= 0;
    }

    public static boolean greaterEqThan(BigDecimal amount, BigDecimal availableAmount) {
        return amount.compareTo(availableAmount) >= 0;
    }

    /**
     * 金额与范围值比较
     *
     * @param amount    金额
     * @param minAmount 金额范围最小值
     * @param maxAmount 金额范围最大值
     * @return 当金额在范围外左侧，则返回-1；当金额在范围内，则返回0；当金额在范围外右侧，则返回1
     */
    public static int compareToRangeEq(BigDecimal amount, BigDecimal minAmount, BigDecimal maxAmount) {
        if (lessThan(amount, minAmount)) {
            return -1;
        } else if (greaterThanEq(amount, minAmount) && lessThanEq(amount, maxAmount)) {
            return 0;
        } else {
            return 1;
        }
    }

    public static boolean eq(BigDecimal amount, BigDecimal amount2) {
        if (Objects.isNull(amount)) {
            return false;
        }
        if (Objects.isNull(amount2)) {
            return false;
        }
        return amount.compareTo(amount2) == 0;
    }

    public static boolean lessEqThan(BigDecimal amount, BigDecimal availableAmount) {
        return amount.compareTo(availableAmount) <= 0;
    }

    public static BigDecimal multiplyUp(BigDecimal a, BigDecimal b) {
        return a.multiply(b).setScale(0, RoundingMode.UP);
    }

    public static BigDecimal multiplyScale8(BigDecimal a, BigDecimal b) {
        return a.multiply(b).setScale(8, RoundingMode.UP);
    }

    public static BigDecimal multiplyUp(BigDecimal a, Double d) {
        return a.multiply(new BigDecimal(d)).setScale(0, RoundingMode.UP);
    }

    public static boolean eqZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 包括负数
     *
     * @param str str
     * @return res
     */
    public static boolean isNumeric(String str) {
        if (Objects.isNull(str)) {
            return false;
        }
        return PATTERN.matcher(str).matches();
    }

    public static BigDecimal halfUp(BigDecimal amount, BigDecimal serviceRate) {
        return amount.multiply(serviceRate).setScale(0, RoundingMode.HALF_UP);
    }

    public static BigDecimal upRound1000(BigDecimal amount) {
        BigDecimal indicator = new BigDecimal(1000);
        return amount.divide(indicator, 0, RoundingMode.UP).multiply(indicator);
    }

    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        if (greaterThan(a, b)) {
            return a;
        }
        return b;
    }

    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        if (greaterThan(a, b)) {
            return b;
        }
        return a;
    }

    public static BigDecimal halfDown(BigDecimal amount, BigDecimal serviceRate) {
        return amount.multiply(serviceRate).setScale(0, RoundingMode.HALF_DOWN);
    }

    public static BigDecimal remainder(BigDecimal amount, BigDecimal bigDecimal) {
        BigDecimal[] results = amount.divideAndRemainder(bigDecimal);
        return results[1];
    }

    public static boolean remainderGreatThanZero(BigDecimal amount, BigDecimal bigDecimal) {
        return greaterZero(remainder(amount, bigDecimal));
    }

    public static BigDecimal divideAmount(BigDecimal interestFeeAmount, int period) {
        return interestFeeAmount.divide(BigDecimal.valueOf(period), scale, RoundingMode.HALF_DOWN);
    }

    public static boolean between(BigDecimal amount, BigDecimal minimumAmount, BigDecimal maximumAmount) {
        return AmountUtils.greaterThan(amount, maximumAmount) || AmountUtils.greaterThan(minimumAmount, amount);
    }

    /**
     * 返回零，当金额小于零
     */
    public static BigDecimal getZeroIfLessThanZero(BigDecimal amount) {
        return lessZero(amount) ? BigDecimal.ZERO : amount;
    }

}
