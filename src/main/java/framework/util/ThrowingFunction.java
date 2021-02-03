package framework.util;

@FunctionalInterface
public interface ThrowingFunction<T, R> {
    R apply(T param) throws Exception;
}
