package io.github.leopard.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 方便获取容器bean
 *
 * @author <a href="mailto:fuwei@daojia-inc.com">pleuvoir</a>
 */
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public final static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public final static <T> T getBean(Class<T> classs) {
        return context.getBean(classs);
    }

    public final static Object getBean(String beanName, Class<?> requiredType) {
        return context.getBean(beanName, requiredType);
    }
}
