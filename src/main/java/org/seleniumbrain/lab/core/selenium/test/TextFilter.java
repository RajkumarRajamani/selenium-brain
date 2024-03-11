package org.seleniumbrain.lab.core.selenium.test;

import lombok.Data;
import org.openqa.selenium.WebElement;

@Data
public class TextFilter implements Filter {
    private WebElement filterElement;
    private String filterOn;

    public TextFilter(WebElement filterElement, String filterOn) {
        this.filterElement = filterElement;
        this.filterOn = filterOn;
    }

    public Operator withOperator(Operator operator) {
        operator.setFilter(this);
        return operator;
    }

    public static void main(String[] args) {
        new TextFilter(null, "").withOperator(new Contains(null, null, "test")).filter();
    }
}
