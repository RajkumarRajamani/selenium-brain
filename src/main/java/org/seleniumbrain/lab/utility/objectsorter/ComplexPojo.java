package org.seleniumbrain.lab.utility.objectsorter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComplexPojo {
    private int id;
    private String name;
    private double salary;
    private Address address;
    private List<String> hobbies;
    private Set<Integer> luckyNumbers;
    private Map<String, Employer> employees;
}