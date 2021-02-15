package framework;

import java.net.URL;

public class Main {

    public static void main(String... args) throws Exception {
        URL beansUrl = Framework.class.getResource("/beans.txt");
        Framework framework = new Framework(beansUrl);
        Class<?> clazz = Class.forName(args[0]);
        Object instance = framework.instantiate(clazz);
        System.out.println(clazz.getMethod(args[1]).invoke(instance));
    }
}
