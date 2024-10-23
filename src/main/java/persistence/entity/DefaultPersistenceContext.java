package persistence.entity;

import jdbc.InstanceFactory;
import persistence.sql.meta.EntityKey;
import persistence.sql.meta.EntityTable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultPersistenceContext implements PersistenceContext {
    private final Map<EntityKey, Object> entityRegistry = new ConcurrentHashMap<>();
    private final Map<EntityKey, Object> entitySnapshotRegistry = new ConcurrentHashMap<>();

    @Override
    public void addEntity(Object entity) {
        final EntityTable entityTable = new EntityTable(entity);
        entityRegistry.put(entityTable.toEntityKey(), entity);
        addSnapshot(entity, entityTable);
    }

    @Override
    public <T> T getEntity(Class<T> entityType, Object id) {
        final EntityKey entityKey = new EntityKey(entityType, id);
        return entityType.cast(entityRegistry.get(entityKey));
    }

    @Override
    public void removeEntity(Object entity) {
        final EntityTable entityTable = new EntityTable(entity);
        entityRegistry.remove(entityTable.toEntityKey());
        entitySnapshotRegistry.remove(entityTable.toEntityKey());
    }

    @Override
    public <T> T getSnapshot(Class<T> entityType, Object id) {
        final EntityKey entityKey = new EntityKey(entityType, id);
        return entityType.cast(entitySnapshotRegistry.get(entityKey));
    }

    private void addSnapshot(Object entity, EntityTable entityTable) {
        final Object snapshot = new InstanceFactory<>(entity.getClass()).copy(entity);
        entitySnapshotRegistry.put(entityTable.toEntityKey(), snapshot);
    }
}
