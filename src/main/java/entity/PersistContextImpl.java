package entity;

import java.util.Optional;

public class PersistContextImpl implements PersistContext{
    @Override
    public <T, ID> Optional<T> getEntity(ID id, Class<T> entityType) {
        return Optional.empty();
    }

    @Override
    public void addEntity(Object entity) {

    }
}
