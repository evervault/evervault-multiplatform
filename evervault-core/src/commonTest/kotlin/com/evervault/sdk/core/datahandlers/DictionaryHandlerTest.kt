@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

package com.evervault.sdk.core.datahandlers

import org.mockito.kotlin.*
import kotlin.test.*

internal class DictionaryHandlerTest {

    lateinit var contextMock: DataHandlerContext
    lateinit var handler: DictionaryHandler

    @BeforeTest
    fun setUp() {
        contextMock = mock<DataHandlerContext> {
            onGeneric { encrypt(anyOrNull(), anyOrNull()) } doAnswer { it.arguments.first() }
        }
        handler = DictionaryHandler()
    }

    @Test
    fun testCanEncrypt() {
        assertTrue(handler.canEncrypt(emptyMap<String, String>()))
        assertTrue(handler.canEncrypt(mapOf("a" to "A", "b" to "B")))
        assertTrue(handler.canEncrypt(mapOf(1 to 10, 2 to 20)))
        assertTrue(handler.canEncrypt(mapOf("a" to 1, 2 to "b")))
        assertTrue(handler.canEncrypt(mapOf("z" to mapOf("a" to 1, "b" to "B"), 2 to true)))
    }

    @Test
    fun testCannotEncrypt() {
        assertFalse(handler.canEncrypt("String value"))
        assertFalse(handler.canEncrypt(1))
        assertFalse(handler.canEncrypt(true))
        assertFalse(handler.canEncrypt(false))
        assertFalse(handler.canEncrypt(emptyList<String>()))
    }

    @Test
    fun testEncryptEmptyStringDictionary() {
        assertEquals(emptyMap<String, String>(), handler.encrypt(emptyMap<String, String>(), contextMock, null))
        verify(contextMock, never()).encrypt(anyOrNull(), eq(null))
    }

    @Test
    fun testEncryptEmptyStringDictionaryDataRoles() {
        assertEquals(emptyMap<String, String>(), handler.encrypt(emptyMap<String, String>(), contextMock, "test-role"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"))
    }

    @Test
    fun testEncryptStringDictionary() {
        assertEquals(mapOf("a" to "A", "b" to "B"), handler.encrypt(mapOf("a" to "A", "b" to "B"), contextMock, null))
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq(null))
    }

    @Test
    fun testEncryptStringDictionaryDataRoles() {
        assertEquals(mapOf("a" to "A", "b" to "B"), handler.encrypt(mapOf("a" to "A", "b" to "B"), contextMock, "test-role"))
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq("test-role"))
    }

    @Test
    fun testEncryptNumbersDictionary() {
        assertEquals(mapOf(1 to 10, 2 to 20), handler.encrypt(mapOf(1 to 10, 2 to 20), contextMock, null))
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq(null))
    }

    @Test
    fun testEncryptNumbersDictionaryDataRoles() {
        assertEquals(mapOf(1 to 10, 2 to 20), handler.encrypt(mapOf(1 to 10, 2 to 20), contextMock, "test-role"))
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq("test-role"))
    }

    @Test
    fun testEncryptMixedDictionary() {
        val result = handler.encrypt(mapOf("a" to 1, 2 to "b"), contextMock, null) as Map<Any?, Any?>
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals(1, result["a"])
        assertEquals("b", result[2])
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq(null))
    }

    @Test
    fun testEncryptMixedDictionaryDataRoles() {
        val result = handler.encrypt(mapOf("a" to 1, 2 to "b"), contextMock, "test-role") as Map<Any?, Any?>
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals(1, result["a"])
        assertEquals("b", result[2])
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq("test-role"))
    }

    @Test
    fun testEncryptMixedMultidimensionalDictionary() {
        val result = handler.encrypt(mapOf("z" to mapOf("a" to 1, "b" to "B"), 2 to true), contextMock, null) as Map<Any?, Any?>
        assertNotNull(result)
        assertEquals(2, result.size)
        val inner = result["z"] as Map<String, Any>?
        assertNotNull(inner)
        assertEquals(1, inner!!["a"])
        assertEquals("B", inner["b"])
        assertEquals(true, result[2])
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq(null))
    }

    @Test
    fun testEncryptMixedMultidimensionalDictionaryDataRoles() {
        val result = handler.encrypt(mapOf("z" to mapOf("a" to 1, "b" to "B"), 2 to true), contextMock, "test-role") as Map<Any?, Any?>
        assertNotNull(result)
        assertEquals(2, result.size)
        val inner = result["z"] as Map<String, Any>?
        assertNotNull(inner)
        assertEquals(1, inner!!["a"])
        assertEquals("B", inner["b"])
        assertEquals(true, result[2])
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq("test-role"))
    }
}
