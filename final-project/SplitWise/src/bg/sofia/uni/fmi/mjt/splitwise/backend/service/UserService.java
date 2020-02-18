package bg.sofia.uni.fmi.mjt.splitwise.backend.service;

import bg.sofia.uni.fmi.mjt.splitwise.backend.data.User;
import bg.sofia.uni.fmi.mjt.splitwise.backend.persistence.Persistence;
import bg.sofia.uni.fmi.mjt.splitwise.backend.service.exception.UsernameTakenException;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service responsible for managing users.
 */
public class UserService {
    private final Persistence persistence;

    private Path persistanceFile;
    private Map<String, User> users;

    public UserService(Persistence persistence, Path persistanceFile) {
        this.persistence = persistence;
        this.persistanceFile = persistanceFile;

        users = new ConcurrentHashMap<>();

        List<Serializable> savedUsers = persistence.readAll();

        // load the persisted Users
        savedUsers.stream()
                  .filter(s -> s instanceof User)
                  .map(s -> (User) s)
                  .forEach(u -> users.put(u.getUsername(), u));
    }

    /**
     * Get user by username
     */
    public Optional<User> getUser(String username) {
        return Optional.ofNullable(users.get(username));
    }

    /**
     * Get all registered users
     * @return a copy of the Users
     */
    public Set<User> getUsers() {
        return new HashSet<>(users.values());
    }

    /**
     * Registed a user in the system
     * @param user user to be registered
     * @throws UsernameTakenException if the username is already present
     */
    public void registerUser(User user) throws UsernameTakenException {
        User existingUser = users.putIfAbsent(user.getUsername(), user);

        if (!Objects.isNull(existingUser)) {
            // we have a user with that username already
            String errMsg = String.format("Username %s is already taken.", user.getUsername());
            throw new UsernameTakenException(errMsg);
        }

        persistence.write(user, persistanceFile);
    }
}
