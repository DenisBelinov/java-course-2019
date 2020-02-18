package bg.sofia.uni.fmi.mjt.splitwise.server.execution.action;

import bg.sofia.uni.fmi.mjt.splitwise.backend.data.Group;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.GroupService;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.UserService;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception.GroupAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception.InvalidGroupSizeException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AddFriendAction extends CreateGroupAction {
    private static final String INSUFFICIENT_ARGUMENTS_MESSSAGE = "Insufficient arguments provided for add-friend." +
            " Please provide <username>.";
    private static final String NOT_LOGGED_IN_MESSAGE = "Please log in before adding a friend.";
    private static final String FRIENDSHIP_EXISTS_MESSAGE = "You are already friends with this user.";
    private static final String INSUFFICIENT_UNIQUE_MEMBERS_MESSAGE = "You cannot be friends with yourself :(.";
    private static final String SUCCESSFUL_GROUP_CREATION_MESSAGE = "Successfully added friend!";

    private static final String UNREGISTERED_MEMBERS_TEMPLATE = "%s is not a registered user. Cannot add to friends.";

    public AddFriendAction(String currentUser, List<String> arguments, UserService userService, GroupService groupService) {
        super(currentUser, arguments, userService, groupService);
    }

    @Override
    public String call() {
        if (arguments.size() < 1) {
            return INSUFFICIENT_ARGUMENTS_MESSSAGE;
        }

        if (Objects.isNull(currentUser) || currentUser.length() == 0) {
            return NOT_LOGGED_IN_MESSAGE;
        }

        String username = arguments.get(0);
        List<String> friendshipMembers = new ArrayList<>();

        friendshipMembers.add(username);
        friendshipMembers.add(currentUser);

        List<String> unregisteredMembers = getUnregisteredMembers(friendshipMembers);
        if (!unregisteredMembers.isEmpty()) {
            return String.format(UNREGISTERED_MEMBERS_TEMPLATE, unregisteredMembers.toString());
        }

        try {
            groupService.registerFriendShip(currentUser, username);
        } catch (GroupAlreadyExistsException e) {
            return FRIENDSHIP_EXISTS_MESSAGE;
        } catch (InvalidGroupSizeException e) {
            return INSUFFICIENT_UNIQUE_MEMBERS_MESSAGE;
        }

        return SUCCESSFUL_GROUP_CREATION_MESSAGE;
    }
}
