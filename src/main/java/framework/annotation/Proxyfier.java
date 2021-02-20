package framework.annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class Proxyfier<A extends Annotation> {
    private final Class<A> annotationClazz;
    private final ProxyLifecycle proxyLifecycle;

    public Proxyfier(Class<A> annotationClazz, ProxyLifecycle proxyLifecycle) {
        this.annotationClazz = annotationClazz;
        this.proxyLifecycle = proxyLifecycle;
    }

    public FrameworkProxy proxify(Class<?> interfaceClazz, Object instance) {
        if (containsAnnotation(instance)) {
            return new FrameworkProxy(interfaceClazz, proxyLifecycle, instance);
        }
        return FrameworkProxy.NoProxy(interfaceClazz, instance);
    }

    private boolean containsAnnotation(Object instance) {
        return instance.getClass().isAnnotationPresent(annotationClazz)
                || Arrays.stream(instance.getClass().getMethods()).anyMatch(m -> m.isAnnotationPresent(annotationClazz));
    }
}
