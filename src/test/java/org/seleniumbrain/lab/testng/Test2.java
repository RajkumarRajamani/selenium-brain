package org.seleniumbrain.lab.testng;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.seleniumbrain.lab.core.selenium.validator.Validator;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Objects;

public class Test2 {

    public static void main(String[] args) {
        method1();
    }

    public static void method1(String... values) {
//        System.out.println(calculatePlaceholderText("90000000000023", 4));
//        System.out.println(calculatePlaceholderText("90000011.000", 4));
//        System.out.println(calculatePlaceholderText("90000012.0000", 4));
//        System.out.println(calculatePlaceholderText("90000013.00000", 4));
//        System.out.println(calculatePlaceholderText("90000014.00001", 4));
//        System.out.println(calculatePlaceholderText("90000015.00035", 4));
//        System.out.println(calculatePlaceholderText("90000016.00036", 4));
//        System.out.println(calculatePlaceholderText("90000017.12956", 4));
//        System.out.println(calculatePlaceholderText("90000018.12952", 4));
//        System.out.println(calculatePlaceholderText("90000019.99962", 4));
//        System.out.println(calculatePlaceholderText("90000020.99965", 4));
//        System.out.println(calculatePlaceholderText("90000021.99996", 4));
//        System.out.println(calculatePlaceholderText("0.0", 4));
//        System.out.println(calculatePlaceholderText("0.00", 4));
//        System.out.println(calculatePlaceholderText("", 4));
//        System.out.println(calculatePlaceholderText("sd", 4));
//        System.out.println(calculatePlaceholderText(null, 4));

        System.out.println(calculatePlaceholderText("11", 2));
        System.out.println(calculatePlaceholderText("12.0", 2));
        System.out.println(calculatePlaceholderText("13.00", 2));
        System.out.println(calculatePlaceholderText("14.000", 2));
        System.out.println(calculatePlaceholderText("15.001", 2));
        System.out.println(calculatePlaceholderText("16.01", 2));
        System.out.println(calculatePlaceholderText("17.013", 2));
        System.out.println(calculatePlaceholderText("18.034", 2));
        System.out.println(calculatePlaceholderText("19.045", 2));
        System.out.println(calculatePlaceholderText("20.056", 2));
        System.out.println(calculatePlaceholderText("21.983", 2));
        System.out.println(calculatePlaceholderText("22.985", 2));
        System.out.println(calculatePlaceholderText("23.986", 2));
        System.out.println(calculatePlaceholderText("23.996", 2));
    }

    static String calculatePlaceholderText(Object value, int requiredDecimalDigits) {
        String text = calculatePlaceholderTexts(value, requiredDecimalDigits).replaceAll("[^0-9.]", "").trim();
        return calculatePlaceholderTexts(text, requiredDecimalDigits);
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

}
