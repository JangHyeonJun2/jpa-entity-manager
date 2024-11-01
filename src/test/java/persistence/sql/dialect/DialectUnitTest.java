package persistence.sql.dialect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.meta.DataType;
import persistence.sql.dialect.type.DataTypeRegistry;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DialectUnitTest {
    private Dialect dialect;
    private DataTypeRegistry dataTypeRegistry;

    @BeforeEach
    void setUp() {
        dataTypeRegistry = mock(DataTypeRegistry.class);

        dialect = new Dialect(dataTypeRegistry) {
            @Override
            public String getNullDefinitionPhrase(Boolean isNull) {
                return isNull ? "DEFAULT NULL" : "NOT NULL";
            }

            @Override
            public String getAutoGeneratedIdentityPhrase() {
                return "AUTO_INCREMENT";
            }

            @Override
            public Boolean shouldSpecifyNotNullOnIdentity() {
                return true;
            }

            @Override
            public Boolean checkIfExitsBeforeDropTable() {
                return true;
            }
        };
    }

    @Test
    @DisplayName("dataTypeRegistry를 이용해 데이터 타입의 이름을 추출해낸다.")
    void testGetDataTypeFullName() {
        // given
        DataType mockDataType = mock(DataType.class);
        when(dataTypeRegistry.getDataType(String.class)).thenReturn(mockDataType);
        when(mockDataType.getFullName(255)).thenReturn("varchar(255)");

        // when
        String result = dialect.getDataTypeFullName(String.class, 255);

        // then
        assertEquals("varchar(255)", result);
        verify(dataTypeRegistry).getDataType(String.class);
        verify(mockDataType).getFullName(255);
    }

    @Test
    @DisplayName("테이블 명이나 컬럼 명에 구분자를 추가한다.")
    void testGetIdentifierQuoted() {
        String result = dialect.getIdentifierQuoted("table_name");

        assertEquals("\"table_name\"", result);
    }

    @Test
    @DisplayName("문자열인 값에 구분자를 적용한다.")
    void testGetValueQuotedOfString() {
        String result = dialect.getValueQuoted("hello");

        assertEquals("'hello'", result);
    }

    @Test
    @DisplayName("null인 값은 NULL 문자열로 표기된다.")
    void testGetValueQuotedOfNull() {
        String result = dialect.getValueQuoted(null);

        assertEquals("NULL", result);
    }

    @Test
    @DisplayName("여러 컬럼의 값을 각자 타입의 SQL문법에 맞는 문자열로 변환한다.")
    void testGetValuesQuoted() {
        List<Object> values = new ArrayList<>();
        values.add("test");
        values.add(123);
        values.add(null);

        String result = dialect.getValuesQuoted(values);

        assertEquals("'test', 123, NULL", result);
    }
}