package me.committee.api.eventsystem;

import com.google.common.collect.ImmutableMap;
import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.eventsystem.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CommitteeEventBus {

    private final HashMap<Object, ArrayList<MethodSubscription>> subscriptions;

    public CommitteeEventBus() {
        this.subscriptions = new HashMap<>();
    }

    public void post(Event event) {
        ImmutableMap.copyOf(this.subscriptions).forEach((obj, methodSubscriptions) -> {
            for (MethodSubscription methodSubscription : methodSubscriptions) {
                if (methodSubscription.getEvent().isInstance(event)) {
                    try {
                        methodSubscription.getMethod().invoke(obj, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void subscribe(Object obj) {
        this.subscriptions.put(obj, this.getListenersFromObject(obj));
    }

    public void unsubscribe(Object obj) {
        this.subscriptions.remove(obj);
    }

    public HashMap<Object, ArrayList<MethodSubscription>> getSubscribed() {
        return subscriptions;
    }

    public void clearSubscriptions() {
        this.subscriptions.clear();
    }

    private ArrayList<MethodSubscription> getListenersFromObject(Object object) {
        final ArrayList<MethodSubscription> methodSubscriptions = new ArrayList<>();

        Arrays.stream(object.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(EventSubscribe.class))
                .filter(method -> method.getParameterCount() == 1)
                .filter(method -> Event.class.isAssignableFrom(method.getParameterTypes()[0]))
                .filter(method -> method.getReturnType().equals(Void.TYPE))
                .forEach(method -> {
                    if (!method.isAccessible())
                        method.setAccessible(true);
                    methodSubscriptions.add(new MethodSubscription(method, method.getParameterTypes()[0]));
                });

        return methodSubscriptions;
    }

}
