import me.committee.Committee;
import me.committee.api.command.Command;
import me.committee.api.command.CommandCompletion;
import me.committee.api.managers.ModuleManager;
import me.committee.api.module.Module;
import net.minecraft.init.Bootstrap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandCompletionTest {

    @BeforeAll
    static void setup() {
        Bootstrap.register();
    }

    void testCommandCompletion() {
        Command command = new Command("test", new String[]{}, "test") {
            @Override
            public String[][] getCompletions() {
                return new String[][]{
                        {"hello", "world"},
                        {"hello", "human"},
                        {"hello", "huzz"},
                        {"eeee"}, // this needs to be above the below line or it will not work correctly
                        {"e", "human"},
                        {"e"},
                        {"poof"},
                        {"let's", "try", "even", "more", "args"}
                };
            }

            @Override
            public void onExec(String[] args) { // never gets called because tests = no user input
            }
        };

        CommandCompletion completion = new CommandCompletion(command);

        assertEquals("uman", completion.getNext("e h", 0));

        assertEquals("orld", completion.getNext("hello w", 0));

        assertEquals("uman", completion.getNext("hello h", 0));
        assertEquals("uzz", completion.getNext("hello h", 1));

        for (int i = 0; i < 100; i++) {
            assertEquals("human", completion.getNext("e ", i));
        }


        assertEquals("of", completion.getNext("po", 0));
        assertEquals("eee", completion.getNext("e", 0));
        assertEquals("rgs", completion.getNext("let's try even more a", 0));
    }

    @Test
    void testCommandPlaceholderCompletion() {
        Command command = new Command("test", new String[]{}, "test") {
            @Override
            public String[][] getCompletions() {
                return new String[][]{
                        {"string", "[STRING]"},
                        {"number", "[NUMBER]"},
                        {"module", "[MODULE]"}
                };
            }

            @Override
            public void onExec(String[] args) {
            }
        };

        CommandCompletion completion = new CommandCompletion(command);

        Committee.moduleManager = new ModuleManager();

        Committee.moduleManager.getModules().add(new Module("TestModule", new String[]{}, "A test module", Module.Category.WORLD) {
            @Override
            public void onEnable() {
                super.onEnable();
            }

            @Override
            public Category getCategory() {
                return super.getCategory();
            }
        });

        assertEquals("tModule", completion.getNext("module Tes", 0));
        assertEquals("", completion.getNext("string wasd", 0));
    }
}
