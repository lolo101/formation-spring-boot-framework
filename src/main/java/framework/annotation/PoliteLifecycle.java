package framework.annotation;

import java.lang.reflect.Method;

public class PoliteLifecycle implements ProxyLifecycle {
    @Override
    public void before(Object instance, Method method, Object[] args) {
        String value = findMethod(instance, method).getAnnotation(Polite.class).before();
        System.out.println(value);
    }

    @Override
    public void after(Object instance, Method method, Object[] args, Object ret) {
        String value = findMethod(instance, method).getAnnotation(Polite.class).after();
        System.out.println(value);
    }
}
