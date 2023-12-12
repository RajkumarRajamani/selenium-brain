package org.seleniumbrain.lab.utility;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class Model {
    @CsvBindByName(column = "Make")
    private String make;
    @CsvBindByName(column = "Model")
    private String model;
    @CsvBindByName(column = "Description")
    private String description;
    @CsvBindByName(column = "Price")
    private String price;
}
