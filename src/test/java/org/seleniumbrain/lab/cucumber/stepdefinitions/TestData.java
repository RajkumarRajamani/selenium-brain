package org.seleniumbrain.lab.cucumber.stepdefinitions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class TestData {
    private String eventType;
    private String code;
    private String umr;
    private String policy;
}