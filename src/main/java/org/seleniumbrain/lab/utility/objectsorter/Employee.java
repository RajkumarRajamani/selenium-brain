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
public class Employee {
    private List<String> names;
    private List<Person> itEmployees;
    private List<Person> managementEmployees;
    private Set<String> locations;
    private Map<String, Person> empGroups;
}


