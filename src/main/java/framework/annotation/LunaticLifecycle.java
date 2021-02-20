package framework.annotation;

import java.lang.reflect.Method;

public class LunaticLifecycle extends ProxyLifecycleAdapter {
    @Override
    public void before(Object instance, Method method, Object[] args) {
        double random = Math.random();
        if (random > 0.5) {
            String value = instance.getClass().getAnnotation(Lunatic.class).value();
            throw new RuntimeException(value);
        }
    }
}
