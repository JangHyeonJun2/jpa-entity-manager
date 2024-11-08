package entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PersistContextImpl implements PersistContext{
    private final Map<EntityCash, Object> context = new HashMap<>();

    @Override
    public <T, ID> Optional<T> getEntity(ID id, Class<T> entityClass) {
        EntityCash entityCash = new EntityCash(id, entityClass);
        return Optional.ofNullable(entityClass.cast(context.get(entityCash)));
    }

    @Override
    public void addEntity(Object entity) {
        EntityCash entityCash = new EntityCash(entity);
        context.put(entityCash, entity);
    }
}
