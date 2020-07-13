package com.guyroyse.ephemeral.graph.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflector {

    public static Object invoke(Method m, Object o) {
        try {
            return m.invoke(o);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
