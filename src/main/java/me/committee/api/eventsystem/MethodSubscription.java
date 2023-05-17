package me.committee.api.eventsystem;

import java.lang.reflect.Method;

public class MethodSubscription {

    private final Method method;
    private final Class<?> event;

    public MethodSubscription(Method method, Class<?> event) {
        this.method = method;
        this.event = event;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getEvent() {
        return event;
    }
}
