package org.seleniumbrain.lab.selenium.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;


public interface TextBoxValidator extends ElementValidator {

    Logger log = LogManager.getLogger(TextBoxValidator.class);

    static ElementValidator isEditable() {
        System.out.println("text box validator");
        return (driver, element, elementName) -> {
            try {
                String testText = "1";

                if(ElementValidator.checkField(element, testText).replaceAll("[^\\d]", "").trim().equals(testText)) {
                    log.info(MessageFormat.format("Element ({0}) is editable", elementName));
                    return true;
                } else {
                    log.info(MessageFormat.format("Element ({0}) is not editable", elementName));
                    return false;
                }
            } catch (Exception e) {
                log.error(MessageFormat.format("Element ({0}) is not intractable and hence not editable", elementName));
                return false;
            }
        };
    }
}
