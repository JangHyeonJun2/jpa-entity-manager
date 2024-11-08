package entity;

import common.ErrorCode;
import jakarta.persistence.Id;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public record EntityCash(Object key, Class<?> entityType) {
    public EntityCash(Object entity) {
        this(getKey(entity), entity.getClass());
    }

    private static Object getKey(Object entity) {
        Field idField = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_EXIST_ID_ANNOTATION.getErrorMsg()));
        try {
            idField.setAccessible(true);
            return idField.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntityCash entityCash)) {
            return false;
        }
        return Objects.equals(key, entityCash.key) && Objects.equals(entityType, entityCash.entityType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, entityType);
    }
}
