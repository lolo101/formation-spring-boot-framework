package framework.annotation;

import java.lang.reflect.Method;

public class ProxyLifecycleAdapter implements ProxyLifecycle {
    @Override public void before(Object instance, Method method, Object[] args) {}
    @Override public void after(Object instance, Method method, Object[] args, Object ret) {}
}
