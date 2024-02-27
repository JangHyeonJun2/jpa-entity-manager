package persistence.entity;

import database.sql.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static persistence.entity.EntityTestUtils.assertSamePerson;

class EntityLoaderTest extends H2DatabaseTest {
    private EntityManagerImpl entityManager;
    private EntityLoader entityLoader;

    @BeforeEach
    void setUp() {
        entityManager = new EntityManagerImpl(jdbcTemplate);
        entityLoader = new EntityLoader(jdbcTemplate, Person.class);
    }

    @Test
    void loadMissingRecord() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                entityLoader.load(1L)
        );
        assertThat(exception.getMessage()).isEqualTo("Expected 1 result, got 0");
    }

    @Test
    void load2() {
        Person person = new Person(null, "abc123", 14, "c123@d.com");
        entityManager.persist(person);

        Person found = (Person) entityLoader.load(1L);

        assertSamePerson(found, person, false);
    }

    @Test
    void loadMany() {
        Person p1 = new Person(null, "abc123", 14, "c123@d.com");
        Person p2 = new Person(null, "def567", 30, "z999@d.com");
        entityManager.persist(p1);
        entityManager.persist(p2);

        List<Object> result = entityLoader.load(List.of(1L, 2L));

        assertSamePerson((Person) result.get(0), p1, false);
        assertSamePerson((Person) result.get(1), p2, false);
    }
}