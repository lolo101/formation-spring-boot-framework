package framework.annotation;

import java.lang.reflect.Method;

class CompositeProxyLifecycle extends ProxyLifecycleAdapter {
    private final ProxyLifecycle first;
    private final ProxyLifecycle second;

    public CompositeProxyLifecycle(ProxyLifecycle first, ProxyLifecycle second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public void before(Object instance, Method method, Object[] args) {
        first.before(instance, method, args);
        second.before(instance, method, args);
    }

    @Override
    public void after(Object instance, Method method, Object[] args, Object ret) {
        first.after(instance, method, args, ret);
        second.after(instance, method, args, ret);
    }
}
