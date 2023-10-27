package persistence.sql.ddl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static persistence.sql.common.meta.MetaUtils.Columns을_생성함;
import static persistence.sql.common.meta.MetaUtils.TableName을_생성함;

import domain.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.exception.InvalidEntityException;
import domain.ExistTablePerson;
import domain.NonExistentEntityPerson;
import domain.NonExistentTablePerson;
import persistence.sql.common.meta.Columns;
import persistence.sql.common.meta.TableName;

class CreateQueryTest {

    private final CreateQuery createQuery = CreateQuery.create();

    @Test
    @DisplayName("Person Entity를 이용한 CREATE QUERY가 정확히 생성 되었는지 검증")
    void create() {
        //given
        final String expectedSql = "CREATE TABLE users ("
            + "id BIGINT generated by default as identity"
            + ", nick_name VARCHAR(255), old INTEGER"
            + ", email VARCHAR(255) not null"
            + ", PRIMARY KEY (id)"
            + ")";

        final Class<Person> clazz = Person.class;
        final TableName tableName = TableName을_생성함(clazz);
        final Columns columns = Columns을_생성함(clazz);

        //when
        final String result = createQuery.get(tableName, columns);

        //then
        assertThat(result).isEqualTo(expectedSql);
    }

    @Test
    @DisplayName("@Entity가 없는 객체의 경우 InvalidEntityException 발생")
    void notExistentEntity() {
        //given
        Class<NonExistentEntityPerson> personClass = NonExistentEntityPerson.class;

        //when & then
        assertThrows(InvalidEntityException.class,
            () -> createQuery.get(TableName을_생성함(personClass), Columns을_생성함(personClass)));
    }

    @Test
    @DisplayName("별도 @Table 이 없을 경우, 클래스 이름을 기반으로 테이블 생성")
    void notExistentTable() {
        //given
        final Class<NonExistentTablePerson> personClass = NonExistentTablePerson.class;
        final TableName tableName = TableName을_생성함(personClass);
        final Columns columns = Columns을_생성함(personClass);

        final String className = personClass.getSimpleName();

        final String expectedSql = String.format("CREATE TABLE %s ("
            + "id BIGINT generated by default as identity"
            + ", nick_name VARCHAR(255), old INTEGER"
            + ", email VARCHAR(255) not null"
            + ", PRIMARY KEY (id)"
            + ")", className);

        //when
        final String result = createQuery.get(tableName, columns);

        //then
        assertThat(result).isEqualTo(expectedSql);
    }

    @Test
    @DisplayName("@Table의 name이 존재하면 해당 값으로 테이블 생성 쿼리 생성")
    void existentTable() {
        //given
        final Class<ExistTablePerson> personClass = ExistTablePerson.class;
        final TableName tableName = TableName을_생성함(personClass);
        final Columns columns = Columns을_생성함(personClass);

        final String expectedSql = "CREATE TABLE users ("
            + "id BIGINT generated by default as identity"
            + ", nick_name VARCHAR(255), old INTEGER"
            + ", email VARCHAR(255) not null"
            + ", PRIMARY KEY (id)"
            + ")";

        //when
        final String result = createQuery.get(tableName, columns);

        //then
        assertThat(result).isEqualTo(expectedSql);
    }
}
