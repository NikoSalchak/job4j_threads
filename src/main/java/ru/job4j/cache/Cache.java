package ru.job4j.cache;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) throws OptimisticException {
        return memory.putIfAbsent(model.id(), model) == null;
    }

    public boolean update(Base model) throws OptimisticException {
        return memory.computeIfPresent(model.id(), (key, value) -> {
            if (value.version() != model.version()) {
                throw new OptimisticException("Versions are not equal");
            }
            return new Base(model.id(), model.name(), model.version() + 1);
        }) != null;
    }

    public boolean delete(int id) throws OptimisticException {
        return memory.remove(id) != null;
    }

    public Optional<Base> findById(int id) {
        return Stream.of(memory.get(id))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
