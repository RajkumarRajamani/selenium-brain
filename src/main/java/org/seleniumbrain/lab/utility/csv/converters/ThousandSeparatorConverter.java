package org.seleniumbrain.lab.utility.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.commons.lang3.StringUtils;
import org.seleniumbrain.lab.core.selenium.validator.Validator;

import java.util.Locale;
import java.util.Optional;

public class ThousandSeparatorConverter<T, I> extends AbstractBeanField<T, I> {
    /**
     * @param beanFieldValue raw value of the field to be converted
     * @return resultant string after converting number string values to a thousand separated string value along with round off
     * @throws CsvDataTypeMismatchException
     * @throws CsvConstraintViolationException
     */
    @Override
    protected String convert(String beanFieldValue) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        final String value = Optional.ofNullable(beanFieldValue).orElse(StringUtils.EMPTY);
        return Validator.getNumberWithThousandSeparator(value, 2, Locale.UK);
    }
}
