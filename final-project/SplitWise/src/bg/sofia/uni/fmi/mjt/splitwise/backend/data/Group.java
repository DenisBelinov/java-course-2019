package bg.sofia.uni.fmi.mjt.splitwise.backend.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Group implements Serializable {
    private final String name;

    private Set<String> members;

    public Group(String name, Set<String> members) {
        this.name = name;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public Set<String> getMembers() {
        // I don't want anyone to be able to modify these. That's why I return a copy
        return new HashSet<>(members);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return Objects.equals(getName(), group.getName()) &&
                Objects.equals(getMembers(), group.getMembers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getMembers());
    }
}
