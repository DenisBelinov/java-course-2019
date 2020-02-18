package bg.sofia.uni.fmi.mjt.splitwise.backend.service;

import bg.sofia.uni.fmi.mjt.splitwise.backend.data.Group;
import bg.sofia.uni.fmi.mjt.splitwise.backend.persistence.Persistence;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception.GroupAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception.InvalidGroupSizeException;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service responsible for managing groups.
 * TODO: Add a DebtService for each group in a map. That way each debt will be for a given group instead of being global.
 */
public class GroupService {
    private final Persistence persistence;

    private Path persistanceFile;
    private Set<Group> groups;

    public GroupService(Persistence persistence, Path persistanceFile) {
        this.persistence = persistence;
        this.persistanceFile = persistanceFile;

        groups = ConcurrentHashMap.newKeySet(); // concurrent set

        List<Serializable> savedGroups = persistence.readAll();

        // load the persisted Groups
        savedGroups.stream()
                .filter(s -> s instanceof Group)
                .map(s -> (Group) s)
                .forEach(groups::add);
    }

    public Optional<Group> getGroup(String groupName, String username) {
        return groups.stream()
                     .filter(g -> g.getName().equals(groupName) && g.getMembers().contains(username))
                     .findFirst();
    }

    /**
     * Get all registered groups
     * @return copy of the registered groups
     */
    public Set<Group> getGroups() {
        return new HashSet<>(groups);
    }

    /**
     * Register a group in the system
     * @param group to be registered
     * @throws GroupAlreadyExistsException if a group with the same name and members exists
     */
    public void registerGroup(Group group) throws InvalidGroupSizeException,
                                                  GroupAlreadyExistsException {
        if (Objects.isNull(group)) {
            throw new IllegalArgumentException("Null value passed for a group.");
        }

        if (Objects.isNull(group.getMembers()) || group.getMembers().size() < 3) {
            throw new InvalidGroupSizeException("Attempt to create a group with invalid member size.");
        }

        if (!groups.add(group)) {
            String errMsg = String.format("Group with name: %s and these members: [%s] already exists",
                                          group.getName(), group.getMembers().toString());

            throw new GroupAlreadyExistsException(errMsg);
        }

        persistence.write(group, persistanceFile);
    }

    public void registerFriendShip(String user1, String user2) throws InvalidGroupSizeException,
                                                                      GroupAlreadyExistsException {
        if (Objects.isNull(user1) || Objects.isNull(user2)) {
            throw new IllegalArgumentException("Null value passed for a user.");
        }

        if (user1.equals(user2)) {
            String errMsg = String.format("Attempt to create a group with one member: %s", user1);
            throw new InvalidGroupSizeException(errMsg);
        }

        Set<String> groupMembers = new HashSet<>();
        groupMembers.add(user1);
        groupMembers.add(user2);

        Group friendshipGroup = new Group("friendship", groupMembers);

        if (!groups.add(friendshipGroup)) {
            String errMsg = String.format("Friendship between: [%s] already exists",
                    friendshipGroup.getMembers().toString());

            throw new GroupAlreadyExistsException(errMsg);
        }
    }

    public boolean verifyFriendship(String user1, String user2) {
        if (Objects.isNull(user1) || Objects.isNull(user2)) {
            throw new IllegalArgumentException("Null value passed for a user.");
        }

        Set<String> groupMembers = new HashSet<>();
        groupMembers.add(user1);
        groupMembers.add(user2);

        Group friendshipGroup = new Group("friendship", groupMembers);

        return groups.contains(friendshipGroup);
    }
}
