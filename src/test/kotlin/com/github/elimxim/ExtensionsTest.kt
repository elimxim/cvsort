package com.github.elimxim

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class ExtensionsTest {
    @Test
    fun `snakeCaseToCamelCase test`() {
        assertEquals("TestTest", "test_test".snakeCaseToCamelCase())
        assertEquals("TestTest", "TEST_TEST".snakeCaseToCamelCase())
        assertEquals("TestTest", "Test_Test".snakeCaseToCamelCase())
        assertEquals("TestT", "test_t".snakeCaseToCamelCase())
        assertEquals("TestT", "test_T".snakeCaseToCamelCase())
        assertEquals("TestT", "TEST_t".snakeCaseToCamelCase())
        assertEquals("TestT", "TEST_T".snakeCaseToCamelCase())
        assertEquals("TTest", "t_test".snakeCaseToCamelCase())
        assertEquals("TTest", "T_test".snakeCaseToCamelCase())
        assertEquals("TTest", "t_TEST".snakeCaseToCamelCase())
        assertEquals("TTest", "T_TEST".snakeCaseToCamelCase())
        assertEquals("TT", "t_t".snakeCaseToCamelCase())
        assertEquals("TT", "T_T".snakeCaseToCamelCase())
        assertEquals("TestTest", "TestTest".snakeCaseToCamelCase())
        assertEquals("TEST", "TEST".snakeCaseToCamelCase())
        assertEquals("Test", "test".snakeCaseToCamelCase())
        assertEquals("TEST", "T_E_S_T".snakeCaseToCamelCase())
        assertEquals("TEST", "t_e_s_t".snakeCaseToCamelCase())
    }

    @Test
    fun `camelCaseToSnakeCase test`() {
        assertEquals("TEST_TEST", "TestTest".camelCaseToSnakeCase())
        assertEquals("TEST_T", "TestT".camelCaseToSnakeCase())
        assertEquals("T_TEST", "TTest".camelCaseToSnakeCase())
        assertEquals("T_T", "TT".camelCaseToSnakeCase())
        assertEquals("TEST_TEST", "test_test".camelCaseToSnakeCase())
        assertEquals("TEST", "test".camelCaseToSnakeCase())
        assertEquals("T", "t".camelCaseToSnakeCase())
        assertEquals("TEST_TEST", "TEST_TEST".camelCaseToSnakeCase())
        assertEquals("T_E_S_T", "TEST".camelCaseToSnakeCase())
        assertEquals("T", "T".camelCaseToSnakeCase())
        assertEquals("test_test", "TestTest".camelCaseToSnakeCase(upperCase = false))
        assertEquals("test_t", "TestT".camelCaseToSnakeCase(upperCase = false))
        assertEquals("t_test", "TTest".camelCaseToSnakeCase(upperCase = false))
        assertEquals("t_t", "TT".camelCaseToSnakeCase(upperCase = false))
        assertEquals("test_test", "test_test".camelCaseToSnakeCase(upperCase = false))
        assertEquals("test_test", "TEST_TEST".camelCaseToSnakeCase(upperCase = false))
        assertEquals("t_e_s_t", "TEST".camelCaseToSnakeCase(upperCase = false))
        assertEquals("t", "T".camelCaseToSnakeCase(upperCase = false))
        assertEquals("test_test", "test_test".camelCaseToSnakeCase(upperCase = false))
        assertEquals("test", "test".camelCaseToSnakeCase(upperCase = false))
        assertEquals("t", "t".camelCaseToSnakeCase(upperCase = false))

    }
}