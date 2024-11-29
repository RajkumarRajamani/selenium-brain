package org.seleniumbrain.lab.utility.json.validator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequiredField {
    private String root;
    private List<Member> members;

    private List<String> paths; // not required for validator method
}