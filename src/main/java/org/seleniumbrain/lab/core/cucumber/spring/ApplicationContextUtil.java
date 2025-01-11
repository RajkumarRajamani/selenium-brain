package org.seleniumbrain.lab.core.cucumber.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Utility class to provide access to the Spring ApplicationContext.
 * This class implements {@link ApplicationContextAware} to automatically
 * set the ApplicationContext when the Spring context is initialized.
 *
 * <p>This class provides a static method to retrieve beans from the
 * ApplicationContext.</p>
 *
 * @author Rajkumar Rajamani
 * @version 1.0
 * @since 2024-03-01
 */
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
