This is the docs for the client's API, for starting out with it

Creating a module

Example code, don't worry if you don't understand all of it as will go through it completely later
```java
package me.committee.impl.modules.world;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.mixin.mixins.accessors.AccessorMinecraft;
import me.committee.api.mixin.mixins.accessors.AccessorTimer;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.player.PlayerUpdateEvent;

public class Timer extends Module {

    private final Setting<Float> speed = new Setting<>("Speed", new String[]{"Spd"},"The multiplier value.", 4.2f, 0.1f, 10f, 0.1f).setID(69); // Thank you to SpicyBigDaddy for correcting the 4.6 to 4.2

    public Timer() {
        super("Timer", new String[]{"TimerSpeed"}, "Speeds up client side tick.", Category.WORLD);
    }

    @EventSubscribe
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        setTimerValue(50 / speed.getValue());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        setTimerValue(50);
    }

    private void setTimerValue(float timerValue) {
        ((AccessorTimer) ((AccessorMinecraft) mc).getTimer()).setTickLength(timerValue);
    }
}
```

### Settings

First there is a speed setting. It does not matter if it is private or public as it gets set to be accessible when it
gets loaded but please prefer making them private. You can make settings for other common java types in a similar way.
Also, colours are supported via setting `me.committee.api.setting.Colour` as the common type. Please set these as final
as when the setting changes the value inside it will be changed and not the setting object.

If you want to get settings from another class you want to set an id for the setting and use getSettingById(int id) instead of getSettingByName(String name)
as it is a lot faster.

### Initializer

The args are (name, aliases[], desc, Category). You can probably figure this out by yourself but don't add 1 character
aliases and use your brain.

### Listening for events

When a class is being registered for events, it will first filter out all methods that don't have the
`me.committee.api.eventsystem.annotation.EventSubscribe` annotation, have a void return type and only one
parameter that is a subtype of `me.committee.api.eventsystem.event.Event`. You access the event bus from
`me.committee.Committee.EVENT_BUS`. All modules are scanned for listeners by default via the register
method.

