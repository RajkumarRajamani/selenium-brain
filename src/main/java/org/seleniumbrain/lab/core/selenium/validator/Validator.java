package org.seleniumbrain.lab.core.selenium.validator;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiFunction;

@FunctionalInterface
public interface Validator extends BiFunction<WebElement, String, String> {

    /**
     * It converts the given numeric value (Given in string representation)
     * with a thousand separator and returns in string format
     *
     * @param value            String format of numeric value to be converted with a thousand separator
     * @param decimalPrecision number of decimal places to keep
     * @param locale           Locale of thousand separator format
     * @return
     */
    static String getNumberWithThousandSeparator(Object value, int decimalPrecision, Locale locale) {
        String objValue = Objects.isNull(value) ? StringUtils.EMPTY : value.toString();
        if (!objValue.isBlank() && NumberUtils.isCreatable(objValue)) {
            NumberFormat format = NumberFormat.getInstance(locale);
            format.setMaximumFractionDigits(decimalPrecision);
            format.setMinimumFractionDigits(decimalPrecision);
            format.setRoundingMode(RoundingMode.HALF_UP);
            return format.format(BigDecimal.valueOf(Double.parseDouble(objValue)));
        }
        return objValue;
    }

    static String getNumberWithoutThousandSeparator(Object value, int decimalPrecision) {
        String objValue = Objects.isNull(value) ? StringUtils.EMPTY : value.toString();
        if (!objValue.isBlank() && NumberUtils.isCreatable(objValue)) {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(decimalPrecision);
            df.setMinimumFractionDigits(decimalPrecision);
            df.setRoundingMode(RoundingMode.HALF_UP);
            df.setGroupingUsed(false);
            return df.format(Double.parseDouble(objValue));
        }
        return objValue;
    }

    static String calculatePlaceholderText(Object value, int decimalPrecision) {
        String text = calculatePlaceholderTexts(value, decimalPrecision).replaceAll("[^0-9.]", "").trim();
        return calculatePlaceholderTexts(text, decimalPrecision);
    }

    static String calculatePlaceholderTexts(Object value, int requiredDecimalDigits) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(requiredDecimalDigits);
        df.setMinimumFractionDigits(requiredDecimalDigits);
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setGroupingUsed(false);

        String objValue = Objects.isNull(value) ? StringUtils.EMPTY : value.toString();
        if (!objValue.isBlank() && NumberUtils.isCreatable(objValue)) {

            double doubleValue = Double.parseDouble(objValue);
            String textFormatOfDoubleValue = new DecimalFormat("0.000000000000000000000000000000").format(doubleValue);
            String characteristic = textFormatOfDoubleValue.split("\\.")[0];
            BigInteger characteristicValue = new BigInteger(characteristic);

            String mantissa = textFormatOfDoubleValue.split("\\.")[1].substring(0, requiredDecimalDigits + 1);
            double mantissaValue = Double.parseDouble("0." + mantissa.substring(0, requiredDecimalDigits));

            String digitChr = String.valueOf(mantissa.charAt((requiredDecimalDigits <= 0 ? 1 : requiredDecimalDigits)));
            int digitChrVal = Integer.parseInt(digitChr);
            if (digitChrVal >= 5) {
                StringBuilder builderText = new StringBuilder();
                for (int i = 0; i < requiredDecimalDigits; i++) {
                    if (i == requiredDecimalDigits - 1)
                        builderText.append("1");
                    else
                        builderText.append("0");
                }
                String precisionTextToAdd = "0." + builderText;
                double precisionValueToAdd = Double.parseDouble(precisionTextToAdd);
                double doubleValueFormatted = characteristicValue.intValue() + mantissaValue + precisionValueToAdd;
                df.setGroupingUsed(true);
                return df.format(doubleValueFormatted);
            } else {
                String doubleValueTobeFormatted = characteristicValue + "." + mantissa.substring(0, requiredDecimalDigits);
                df.setGroupingUsed(true);
                if (mantissaValue > 0) {
                    return df.format(Double.parseDouble(doubleValueTobeFormatted));
                } else {
                    df.setMaximumFractionDigits(0);
                    df.setMinimumFractionDigits(0);
                    return df.format(Double.parseDouble(doubleValueTobeFormatted));
                }
            }
        }
        return objValue;
    }

    static int getDecimalPlaces(@NonNull String value) {
        if (value.contains(".") && StringUtils.countMatches(value, ".") == 1)
            return value.replaceAll("[^\\d.]]", "").trim().split("\\.")[1].length();
        else return 0;
    }
}
