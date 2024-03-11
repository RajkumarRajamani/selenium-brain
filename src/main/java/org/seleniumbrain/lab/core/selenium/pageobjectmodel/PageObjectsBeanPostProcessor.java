package org.seleniumbrain.lab.core.selenium.pageobjectmodel;

import org.openqa.selenium.support.PageFactory;
import org.seleniumbrain.lab.core.selenium.driver.factory.WebDriverUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class PageObjectsBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private WebDriverUtils webDriverUtils;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(PageObjects.class))
            PageFactory.initElements(webDriverUtils.getDriver(), bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
