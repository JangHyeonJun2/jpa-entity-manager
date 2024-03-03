package persistence.sql.column;

import java.util.Arrays;

public enum MysqlColumnType implements ColumnType {
    BIGINT(Long.class, "bigint", ""),
    VARCHAR(String.class, "varchar", "255"),
    INTEGER(Integer.class, "integer", ""),
    ;

    private final Class<?> javaType;
    private final String sqlType;
    private final String defaultValue;

    MysqlColumnType(Class<?> javaType, String sqlType, String defaultValue) {
        this.javaType = javaType;
        this.sqlType = sqlType;
        this.defaultValue = defaultValue;
    }

    public static MysqlColumnType convertToSqlColumnType(Class<?> javaType) {
        return Arrays.stream(values())
                .filter(columnType -> columnType.javaType.equals(javaType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[INFO] No supported javaType: " + javaType.getName() + " found"));
    }

    @Override
    public String getColumnDefinition() {
        StringBuilder sb = new StringBuilder();
        sb.append(sqlType);
        if (!defaultValue.isBlank()) {
            sb.append("(").append(defaultValue).append(")");
        }
        return sb.toString();
    }

}
