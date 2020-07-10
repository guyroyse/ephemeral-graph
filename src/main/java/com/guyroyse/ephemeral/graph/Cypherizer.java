package com.guyroyse.ephemeral.graph;

import com.guyroyse.ephemeral.graph.annotations.GraphInt;
import com.guyroyse.ephemeral.graph.annotations.GraphString;
import com.guyroyse.ephemeral.graph.annotations.Graphable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Cypherizer {
    public String cypherize(Object object) {

        String label = buildQueryLabel(object);
        String sets = buildQuerySets(object);

        String query;
        if (sets.length() == 0) {
            query = String.format("CREATE (n:%s)", label);
        } else {
            query = String.format("CREATE (n:%s) SET %s", label, sets);
        }

        return query;
    }

    private String buildQueryLabel(Object object) {
        Graphable graphable = object.getClass().getAnnotation(Graphable.class);
        return graphable.value();
    }

    private String buildQuerySets(Object object) {
        return Arrays.stream(object.getClass().getMethods())
            .filter(m -> hasDesirableAnnotation(m))
            .map(m -> buildQuerySet(object, m))
            .collect(Collectors.joining(", "));
    }

    private boolean hasDesirableAnnotation(Method m) {
        return m.isAnnotationPresent(GraphString.class) || m.isAnnotationPresent(GraphInt.class);
    }

    private String buildQuerySet(Object o, Method m) {
        return buildQueryStringSet(o, m) + buildQueryIntegerSet(o, m);
    }

    private String buildQueryStringSet(Object object, Method method) {
        String set = "";

        GraphString graphString = method.getAnnotation(GraphString.class);
        if (graphString != null) {
            String value = (String) invoke(method, object);
            String name = graphString.value();
            set = String.format("n.%s = '%s'", name, value);
        }

        return set;
    }

    private String buildQueryIntegerSet(Object object, Method method) {
        String set = "";

        GraphInt graphInt = method.getAnnotation(GraphInt.class);
        if (graphInt != null) {
            int value = (int) invoke(method, object);
            String name = graphInt.value();
            set = String.format("n.%s = %d", name, value);
        }

        return set;
    }

    private Object invoke(Method m, Object o) {
        try {
            return m.invoke(o);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
