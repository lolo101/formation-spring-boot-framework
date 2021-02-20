package framework.annotation;

import java.lang.reflect.Method;

public interface ProxyLifecycle {
    void before(Object instance, Method method, Object[] args);
    void after(Object instance, Method method, Object[] args, Object ret);

    default Method findMethod(Object instance, Method method) {
        try {
            return instance.getClass().getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
