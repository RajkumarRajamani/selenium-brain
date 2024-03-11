package org.seleniumbrain.lab.utility.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.commons.text.StringEscapeUtils;

public class UnicodeConverter<T, I> extends AbstractBeanField<T, I> {
    /**
     * @param beanFieldValue raw value of the field to be converted
     * @return resultant string after converting unicode values to a normal string
     * @throws CsvDataTypeMismatchException
     * @throws CsvConstraintViolationException
     */
    @Override
    protected String convert(String beanFieldValue) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        return StringEscapeUtils.unescapeJava(beanFieldValue);
    }
}
