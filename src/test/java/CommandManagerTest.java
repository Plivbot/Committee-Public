import me.committee.api.managers.CommandManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CommandManagerTest {

    @Test
    public void test() {
        CommandManager commandManager = new CommandManager();
        commandManager.init();

        assertNotEquals(0, commandManager.commands.size());
    }
}
