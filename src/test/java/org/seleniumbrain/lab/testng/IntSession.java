package org.seleniumbrain.lab.testng;

import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.seleniumbrain.lab.utility.TestBean;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static com.jayway.jsonpath.JsonPath.*;
import static com.jayway.jsonpath.Criteria.*;
import static com.jayway.jsonpath.Filter.*;

public class IntSession {

    public static void main(String[] args) {

        Filter cheapFictionFilter = filter(
                where("category.name").is("fiction").and("price").lte(10D)
        );

        System.out.println(StringUtils.removeStart(StringUtils.removeEnd(cheapFictionFilter.toString(), "]"), "["));

        Filter filter2 = filter(
                where("category.name").in("fiction", "chennai")
        );

        System.out.println(filter2);

        System.out.println(where("category.name").ne("fiction").and("price").lte(10D));
        System.out.println(where("category.name").in("fiction", "chennai"));

        Predicate predicate = predicateContext -> predicateContext.item(String.class).startsWith("Raj");
        String val = "QA_";
        Filter filter3 = filter(
                where("category[*].name").regex(Pattern.compile("/^" + val + ".*$/"))
        );
        System.out.println(filter3.toString());

        System.out.println(

                where("sections[*].contractParties[*].party.roleCode")
                        .is("insured")
                        .and("sections[*].contractParties[*].party.name.fullName")
                        .is(val)

        );


    }

}
