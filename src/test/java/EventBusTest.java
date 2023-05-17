import me.committee.api.eventsystem.CommitteeEventBus;
import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.eventsystem.event.Event;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventBusTest {

    boolean ran = false;

    @Test
    void test() {
        CommitteeEventBus eventBus = new CommitteeEventBus();

        eventBus.subscribe(this);
        eventBus.post(new TestEvent());

        assertTrue(ran);
    }

    @EventSubscribe
    public void onEvent(TestEvent event) {
        ran = true;
    }
}

class TestEvent extends Event {

}