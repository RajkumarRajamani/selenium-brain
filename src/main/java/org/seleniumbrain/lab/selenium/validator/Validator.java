package org.seleniumbrain.lab.selenium.validator;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
            return format.format(BigDecimal.valueOf(Double.parseDouble(objValue)));
        }
        return objValue;
    }

    static String getNumberWithoutThousandSeparator(Object value, int decimalPrecision) {
        String objValue = Objects.isNull(value) ? StringUtils.EMPTY : value.toString();
        if (!objValue.isBlank() && NumberUtils.isCreatable(objValue)) {
            DecimalFormat df = new DecimalFormat("0");
            df.setMaximumFractionDigits(decimalPrecision);
            return df.format(Double.parseDouble(objValue));
        }
        return objValue;
    }

    static int getDecimalPlaces(@NonNull String value) {
        if(value.contains(".") && StringUtils.countMatches(value, ".") == 1)
            return value.replaceAll("[^\\d.]]", "").trim().split("\\.")[1].length();
        else return 0;
    }
}
