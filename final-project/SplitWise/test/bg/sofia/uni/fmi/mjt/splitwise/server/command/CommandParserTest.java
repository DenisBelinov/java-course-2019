package bg.sofia.uni.fmi.mjt.splitwise.server.command;

import bg.sofia.uni.fmi.mjt.splitwise.server.command.exception.InvalidCommandException;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandParserTest {
    private static final String UNPRUNED_USER = "<user123>";
    private static final String PRUNED_USER = "user123";
    private static final String VALID_ARGUMENTS = "arg1 arg2";

    private CommandParser commandParser = new CommandParser();

    @Test(expected = InvalidCommandException.class)
    public void testTooShortCommand() throws InvalidCommandException {
        commandParser.parse("faulty");
    }

    @Test(expected = InvalidCommandException.class)
    public void testNoUserProvidedInCommand() throws InvalidCommandException {
        commandParser.parse("get-status");
    }

    @Test(expected = InvalidCommandException.class)
    public void testInvalidUserFormat() throws InvalidCommandException {
        commandParser.parse(PRUNED_USER + " get-status");
    }

    @Test
    public void testUserIsPrunedCorrectly() throws InvalidCommandException {
        String fullCommand = UNPRUNED_USER + " get-status";
        Command resultCommand = commandParser.parse(fullCommand);

        assertEquals(PRUNED_USER, resultCommand.getUser());
    }

    @Test
    public void testEmptyUserIsHandled() throws InvalidCommandException {
        String fullCommand = "<> get-status";
        Command resultCommand = commandParser.parse(fullCommand);

        assertEquals("", resultCommand.getUser());
    }

    @Test
    public void testActionsAreConvertedCorrectly() throws InvalidCommandException {

        for (CommandType commandType : CommandType.values()) {
            String fullCommand = UNPRUNED_USER + " " + commandType.getCliCommand() + " " + VALID_ARGUMENTS;
            Command resultCommand = commandParser.parse(fullCommand);

            assertEquals(commandType, resultCommand.getCommandType());
        }
    }

    @Test
    public void testArgumentsAreParsedCorrectly() throws InvalidCommandException {
        String fullCommand = UNPRUNED_USER + " split " + VALID_ARGUMENTS;
        Command resultCommand = commandParser.parse(fullCommand);

        int expectedArgumentsCount = VALID_ARGUMENTS.split(" ").length;
        assertEquals(expectedArgumentsCount, resultCommand.getArguments().size());
    }
}