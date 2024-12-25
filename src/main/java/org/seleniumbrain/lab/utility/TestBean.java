package org.seleniumbrain.lab.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.seleniumbrain.lab.utils.date.DateTimeFormat;
import org.seleniumbrain.lab.utils.date.DateTimeUtils;

import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestBean {

    private String name;
    private String age;
    private String address;

    public static void main(String[] args) {

        DateTimeUtils util = new DateTimeUtils();
        System.out.println(util.formatTo(util.toLocalDateTime("2021-01-01").toString(), DateTimeFormat.FORMAT_RFC_1123_DATE_TIME));

    }
}
