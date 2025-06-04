package ru.job4j.cache;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CacheTest {

    @Test
    public void whenAddFind() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        var find = cache.findById(base.id());
        assertThat(find.get().name()).isEqualTo("Base");
    }

    @Test
    public void whenAddUpdateFind() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(new Base(1, "Base updated", 1));
        var find = cache.findById(base.id());
        assertThat(find.get().name())
                .isEqualTo("Base updated");
    }

    @Test
    public void whenAddDeleteFind() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.delete(1);
        var find = cache.findById(base.id());
        assertThat(find.isEmpty()).isTrue();
    }

    @Test
    public void whenMultiUpdateThrowException() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(base);
        assertThatThrownBy(() -> cache.update(base))
                .isInstanceOf(OptimisticException.class);
    }

    @Test
    public void whenAddAndUpdateThenDelete() {
        var base1 = new Base(1, "Base 1", 1);
        var cache = new Cache();
        cache.add(base1);
        cache.update(base1);
        assertThat(cache.findById(1).get()).isEqualTo(new Base(1, "Base 1", 2));
        cache.delete(1);
        assertThat(cache.findById(1).isEmpty()).isTrue();
    }

    @Test
    public void whenAddThenUpdate() {
        var base1 = new Base(1, "Base 1", 1);
        var base2 = new Base(1, "updated Base", 1);
        var cache = new Cache();
        cache.add(base1);
        cache.update(base2);
        assertThat(cache.findById(1).get().name()).isEqualTo("updated Base");
    }
}
