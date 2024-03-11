package org.seleniumbrain.lab.core.cucumber.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * @param context the ApplicationContext object to be used by this object
     * @throws BeansException <br>ApplicationContextException – in case of context initialization errors
     *                        <br>BeansException – if thrown by application context methods
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ApplicationContextUtil.applicationContext = context;
    }

    public static <T> T getBean(Class<T> beanType) {
        return (T) applicationContext.getBean(beanType);
    }
}
