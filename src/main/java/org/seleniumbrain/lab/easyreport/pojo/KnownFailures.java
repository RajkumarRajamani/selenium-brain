package org.seleniumbrain.lab.easyreport.pojo;

import lombok.Data;

@Data
public class KnownFailures {
    private String label;
    private String trackingId;
    private String description;
}
