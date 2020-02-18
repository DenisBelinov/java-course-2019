package bg.sofia.uni.fmi.mjt.splitwise.backend.persistence;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MockPersistence implements Persistence {
    @Override
    public List<Serializable> readAll() {
        return new ArrayList<>();
    }

    @Override
    public void write(Serializable toPersist, Path file) {

    }

    @Override
    public void writeAll(List<Serializable> toPersist) {

    }
}
