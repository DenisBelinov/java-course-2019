package bg.sofia.uni.fmi.mjt.splitwise.backend.persistence;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;

public interface Persistence {

    List<Serializable> readAll();

    void write(Serializable toPersist, Path file);

    void writeAll(List<Serializable> toPersist);

}
