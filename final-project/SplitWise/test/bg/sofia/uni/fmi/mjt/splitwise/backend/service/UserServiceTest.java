package bg.sofia.uni.fmi.mjt.splitwise.backend.service;

import bg.sofia.uni.fmi.mjt.splitwise.backend.data.User;
import bg.sofia.uni.fmi.mjt.splitwise.backend.persistence.Persistence;

import bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception.UsernameTakenException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.util.Set;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    private static final Path TEST_PATH = Path.of("");

    @Mock
    private Persistence persistenceMock;

    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserService(persistenceMock, TEST_PATH);
    }

    @Test
    public void testConstructorLoadsFromPersistence() {
        //when(persistenceMock.readAll()).thenReturn(new ArrayList<>());

        verify(persistenceMock).readAll();
    }

    @Test
    public void testRegisterUser() throws UsernameTakenException {
        User testUser = new User("Test", "test", "test test");
        userService.registerUser(testUser);

        Set<User> registerdUsers = userService.getUsers();
        assertTrue(registerdUsers.contains(testUser));
    }

    @Test
    public void verifyPersistNewUserOnce() throws UsernameTakenException {
        User testUser = new User("Test", "test", "test test");
        userService.registerUser(testUser);

        verify(persistenceMock, times(1)).write(testUser, TEST_PATH);
    }

    @Test(expected = UsernameTakenException.class)
    public void testRegisterUserWithTakenUsername() throws UsernameTakenException {
        User testUser = new User("Test", "test", "test test");
        User testUser2 = new User("Test", "test2", "test test2");
        try {
            userService.registerUser(testUser);
        } catch (UsernameTakenException e) {
            fail("registerUser shouldn't have thrown exception during first registration.");
        }

        userService.registerUser(testUser2);
    }
}