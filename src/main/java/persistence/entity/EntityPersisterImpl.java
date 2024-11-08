package persistence.entity;

import jdbc.JdbcTemplate;
import persistence.sql.dml.builder.InsertQueryBuilder;
import persistence.sql.dml.query.InsertQuery;

public class EntityPersisterImpl implements EntityPersister{
    @Override
    public <T> void insert(T entity, JdbcTemplate jdbcTemplate) {
        InsertQuery query = new InsertQuery(entity);
        jdbcTemplate.execute(InsertQueryBuilder.builder()
                .insert(query.tableName(), query.columns())
                .values(query.columns())
                .build());
    }

    @Override
    public <T> void update(T entity, JdbcTemplate jdbcTemplate) {

    }

    @Override
    public <T> void delete(T entity, JdbcTemplate jdbcTemplate) {

    }
}
