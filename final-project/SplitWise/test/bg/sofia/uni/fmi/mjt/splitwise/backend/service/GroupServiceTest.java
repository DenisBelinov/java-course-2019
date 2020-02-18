package bg.sofia.uni.fmi.mjt.splitwise.backend.service;

import bg.sofia.uni.fmi.mjt.splitwise.backend.data.Group;
import bg.sofia.uni.fmi.mjt.splitwise.backend.persistence.Persistence;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception.GroupAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception.InvalidGroupSizeException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {
    private static final Path TEST_PATH = Path.of("");

    @Mock
    private Persistence persistenceMock;

    private GroupService groupService;

    @Before
    public void setUp() {
        groupService = new GroupService(persistenceMock, TEST_PATH);
    }

    @Test
    public void testConstructorLoadsFromPersistence() {
        verify(persistenceMock).readAll();
    }

    @Test
    public void testRegisterGroup() throws GroupAlreadyExistsException, InvalidGroupSizeException {
        Group testGroup = getTestGroup();

        groupService.registerGroup(testGroup);

        Set<Group> registeredGroups = groupService.getGroups();
        assertTrue(registeredGroups.contains(testGroup));
    }

    @Test
    public void verifyPersistNewUserOnce() throws GroupAlreadyExistsException, InvalidGroupSizeException {
        Group testGroup = getTestGroup();
        groupService.registerGroup(testGroup);

        verify(persistenceMock, times(1)).write(testGroup, TEST_PATH);
    }

    @Test(expected = GroupAlreadyExistsException.class)
    public void testRegisterExistingGroup() throws GroupAlreadyExistsException, InvalidGroupSizeException {
        Group testGroup = getTestGroup();
        try {
            groupService.registerGroup(testGroup);
        } catch (GroupAlreadyExistsException e) {
            fail("registerGroup shouldn't have thrown exception during first registration.");
        }

        groupService.registerGroup(testGroup);
    }

    @Test(expected = InvalidGroupSizeException.class)
    public void testRegisterGroupWithOneMember() throws GroupAlreadyExistsException, InvalidGroupSizeException {
        String username1 = "a";

        Set<String> groupMembers = new HashSet<>();
        groupMembers.add(username1);

        groupService.registerGroup(new Group("test", groupMembers));
    }

    @Test
    public void testVerifyGroupMembership() throws GroupAlreadyExistsException, InvalidGroupSizeException {
        Group testGroup = getTestGroup();

        groupService.registerGroup(testGroup);

        assertTrue(groupService.getGroup(testGroup.getName(), "User1").isEmpty());
        assertTrue(groupService.getGroup(testGroup.getName(), "a").isPresent());
    }

    @Test
    public void testRegisterFriendship() throws InvalidGroupSizeException, GroupAlreadyExistsException {
        String user1 = "User1";
        String user2 = "User2";

        Set<String> groupMembers = new HashSet<>();
        groupMembers.add(user1);
        groupMembers.add(user2);

        groupService.registerFriendShip(user1, user2);
        Set<Group> registeredGroups = groupService.getGroups();

        Group expectedGroup = new Group("friendship", groupMembers);
        assertTrue(registeredGroups.contains(expectedGroup));
    }

    @Test(expected = GroupAlreadyExistsException.class)
    public void testRegisterExistingFriendship() throws GroupAlreadyExistsException, InvalidGroupSizeException {
        String user1 = "User1";
        String user2 = "User2";

        try {
            groupService.registerFriendShip(user1, user2);
        } catch (GroupAlreadyExistsException e) {
            fail("registerGroup shouldn't have thrown exception during first registration.");
        }

        groupService.registerFriendShip(user2, user1);
    }

    @Test(expected = InvalidGroupSizeException.class)
    public void testRegisterFriendShipWithOneMember() throws GroupAlreadyExistsException, InvalidGroupSizeException {
        String user1 = "User1";

        groupService.registerFriendShip(user1, user1);
    }

    @Test
    public void testVerifyFriendShip() throws GroupAlreadyExistsException, InvalidGroupSizeException {
        String user1 = "User1";
        String user2 = "User2";

        groupService.registerFriendShip(user2, user1);

        assertTrue(groupService.verifyFriendship(user2, user1));
    }

    private Group getTestGroup() {
        String username1 = "a";
        String username2 = "b";
        String username3 = "c";

        Set<String> groupMembers = new HashSet<>();
        groupMembers.add(username1);
        groupMembers.add(username2);
        groupMembers.add(username3);

        return new Group("test", groupMembers);
    }
}