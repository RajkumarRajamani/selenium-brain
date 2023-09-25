package org.seleniumbrain.lab.selenium.pageobjectmodel.spring;

import io.cucumber.spring.ScenarioScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Lazy
@Component
@ScenarioScope
public @interface PageObjects {
    String value() default "";
}
