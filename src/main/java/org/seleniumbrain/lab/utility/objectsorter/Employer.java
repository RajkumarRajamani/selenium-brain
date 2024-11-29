package org.seleniumbrain.lab.utility.objectsorter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employer {
        private int employeeId;
        private String employeeName;
        private double employeeSalary;
    }