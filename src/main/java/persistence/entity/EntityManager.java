package persistence.entity;

public interface EntityManager {
    <T> T find(Class<T> clazz, Long Id);

    Object persist(Object entity);

    Object update(Object entity);

    void remove(Object entity);
}
