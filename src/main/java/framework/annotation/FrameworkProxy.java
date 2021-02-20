package framework.annotation;

import java.lang.reflect.Proxy;

public class FrameworkProxy {
    private final Class<?> interfaceClazz;
    private final ProxyLifecycle proxyLifecycle;
    private final Object instance;

    public FrameworkProxy(Class<?> interfaceClazz, ProxyLifecycle proxyLifecycle, Object instance) {
        this.interfaceClazz = interfaceClazz;
        this.proxyLifecycle = proxyLifecycle;
        this.instance = instance;
    }

    public Object proxyInstance() {
        return Proxy.newProxyInstance(
                FrameworkProxy.class.getClassLoader(),
                new Class[]{interfaceClazz},
                (proxy, method, args) -> {
                    proxyLifecycle.before(instance, method, args);
                    Object ret = method.invoke(instance, args);
                    proxyLifecycle.after(instance, method, args, ret);
                    return ret;
                });
    }

    public static FrameworkProxy NoProxy(Class<?> interfaceClazz, Object instance) {
        return new FrameworkProxy(
                interfaceClazz,
                new ProxyLifecycleAdapter(),
                instance);
    }

    public FrameworkProxy compose(FrameworkProxy other) {
        return new FrameworkProxy(interfaceClazz, new CompositeProxyLifecycle(proxyLifecycle, other.proxyLifecycle), instance);
    }
}
