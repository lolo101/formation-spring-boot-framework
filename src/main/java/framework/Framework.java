package framework;

import framework.annotation.*;
import framework.util.Try;
import framework.util.Try.Status;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class Framework {

    private static final Logger LOGGER = Logger.getLogger(Framework.class.getName());
    private final List<Class> descriptors;
    private final List<Proxyfier<?>> proxyfiers = Arrays.asList(
            new Proxyfier<>(Lunatic.class, new LunaticLifecycle()),
            new Proxyfier<>(Polite.class, new PoliteLifecycle())
    );

    public Framework(URL beansUrl) throws IOException, URISyntaxException {
        Path beans = Path.of(beansUrl.toURI());

        Map<Status, List<Try<Class>>> collect = Files.lines(beans)
                .filter(s -> !s.isBlank())
                .filter(line -> !line.startsWith("#"))
                .map(Try.to(className -> (Class) Class.forName(className)))
                .collect(Try.groupingByStatus());

        descriptors = collect.getOrDefault(Status.SUCCESS, emptyList()).stream()
                .map(Try::get)
                .collect(toList());

        collect.getOrDefault(Status.CAUGHT, emptyList()).stream()
                .map(Try::exception)
                .forEach(ex -> LOGGER.severe("Could not find bean class " + ex));
    }

    public Object instantiate(Class<?> clazz) throws Exception {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            try {
                return instantiate(constructor);
            } catch (Exception e) {
                System.out.println("Could not instantiate " + constructor + ": " + e);
            }
        }
        throw new Exception("Could not instantiate " + clazz);
    }

    private Object instantiate(Constructor<?> constructor) throws Exception {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] arguments = new Object[parameterTypes.length];
        for (int i = 0; i < arguments.length; ++i) {
            arguments[i] = proxyfy(parameterTypes[i]);
        }
        return constructor.newInstance(arguments);
    }

    private Object proxyfy(Class<?> interfaceClazz) throws Exception {
        Class<?> clazz = findClass(interfaceClazz);
        Object instance = instantiate(clazz);
        return proxyfiers.stream()
                .map(proxyfier -> proxyfier.proxify(interfaceClazz, instance))
                .reduce(FrameworkProxy.NoProxy(interfaceClazz, instance), FrameworkProxy::compose)
                .proxyInstance();
    }

    private Class<?> findClass(Class<?> type) throws Exception {
        List<Class> candidates = descriptors.stream()
                .filter(type::isAssignableFrom)
                .collect(toList());
        if (candidates.isEmpty()) {
            LOGGER.warning("Bean not found for type " + type + ": will try to instantiate type directly");
            return type;
        }
        if (candidates.size() > 1) {
            throw new Exception("More than 1 candidate for type " + type + ": " + candidates);
        }
        return candidates.get(0);
    }
}
