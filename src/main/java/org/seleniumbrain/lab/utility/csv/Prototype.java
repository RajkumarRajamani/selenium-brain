package org.seleniumbrain.lab.utility.csv;

import com.opencsv.bean.*;
import lombok.Data;

@Data
public class Prototype {

    //        @CsvBindByName(column = "Make")
    @CsvBindByPosition(position = 0)
    private String make;

    //        @CsvBindByName(column = "Model")
    @CsvBindByPosition(position = 1)
    private String model;

    //        @CsvCustomBindByName(column = "Description", converter = UnicodeConverter.class)
    @CsvBindByPosition(position = 2)
    private String description;

    //        @CsvBindByName(column = "Price")
    @CsvBindByPosition(position = 3)
    private String price;
}