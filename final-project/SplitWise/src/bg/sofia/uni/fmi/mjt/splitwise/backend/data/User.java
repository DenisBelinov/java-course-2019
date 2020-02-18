package bg.sofia.uni.fmi.mjt.splitwise.backend.data;

import java.io.Serializable;

public class User implements Serializable {
    private final String username;
    private final String password;

    private String fullName;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }
}
