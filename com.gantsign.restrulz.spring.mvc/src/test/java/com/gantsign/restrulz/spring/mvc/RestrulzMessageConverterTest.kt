/*-
 * #%L
 * Restrulz
 * %%
 * Copyright (C) 2017 GantSign Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.gantsign.restrulz.spring.mvc

import com.gantsign.restrulz.spring.mvc.model.TestEntity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.http.MediaType.TEXT_MARKDOWN
import org.springframework.mock.http.MockHttpInputMessage
import org.springframework.mock.http.MockHttpOutputMessage
import java.lang.reflect.ParameterizedType
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RestrulzMessageConverterTest {

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    private lateinit var messageConverter: RestrulzMessageConverter

    @Before
    fun setup() {
        messageConverter = RestrulzMessageConverter()
    }

    @Test
    fun testCanRead() {
        assertTrue(messageConverter.canRead(TestEntity::class.java, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanReadList() {
        val list = object : ArrayList<TestEntity>() {}
        assertTrue(messageConverter.canRead(list::class.java, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanReadUnsupportedType() {
        assertFalse(messageConverter.canRead(String::class.java, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanReadUnsupportedList() {
        val list = object : ArrayList<String>() {}
        assertFalse(messageConverter.canRead(list::class.java, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanReadUnsupportedSet() {
        val set = object : HashSet<TestEntity>() {}
        assertFalse(messageConverter.canRead(set::class.java, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanReadUnsupportedMediaType() {
        assertFalse(messageConverter.canRead(TestEntity::class.java, TEXT_MARKDOWN))
    }

    @Test
    fun testCanReadGeneric() {
        assertTrue(messageConverter.canRead(TestEntity::class.java, null, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanReadGenericParametrizedType() {
        val list = object : ArrayList<TestEntity>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType
        assertTrue(messageConverter.canRead(typedListType, null, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanReadGenericList() {
        val list = object : ArrayList<TestEntity>() {}
        assertTrue(messageConverter.canRead(list::class.java, null, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanReadGenericUnsupportedType() {
        assertFalse(messageConverter.canRead(String::class.java, null, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanReadGenericUnsupportedParametrizedType() {
        val list = object : ArrayList<String>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType
        assertFalse(messageConverter.canRead(typedListType, null, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanReadGenericUnsupportedList() {
        val list = object : ArrayList<String>() {}
        assertFalse(messageConverter.canRead(list::class.java, null, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanReadGenericUnsupportedParametrizedTypeSet() {
        val set = object : HashSet<String>() {}
        val typedSetType = set::class.java.genericSuperclass as ParameterizedType
        assertFalse(messageConverter.canRead(typedSetType, null, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanReadGenericUnsupportedSet() {
        val set = object : HashSet<TestEntity>() {}
        assertFalse(messageConverter.canRead(set::class.java, null, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanReadGenericUnsupportedMediaType() {
        assertFalse(messageConverter.canRead(TestEntity::class.java, null, TEXT_MARKDOWN))
    }

    @Test
    fun testCanWrite() {
        assertTrue(messageConverter.canWrite(TestEntity::class.java, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanWriteList() {
        val list = object : ArrayList<TestEntity>() {}
        assertTrue(messageConverter.canWrite(list::class.java, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanWriteUnsupportedType() {
        assertFalse(messageConverter.canWrite(String::class.java, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanWriteUnsupportedList() {
        val list = object : ArrayList<String>() {}
        assertFalse(messageConverter.canWrite(list::class.java, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanWriteListUnsupportedSet() {
        val set = object : HashSet<String>() {}
        assertFalse(messageConverter.canWrite(set::class.java, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanWriteUnsupportedMediaType() {
        assertFalse(messageConverter.canWrite(TestEntity::class.java, TEXT_MARKDOWN))
    }

    @Test
    fun testCanWriteGeneric() {
        val type = TestEntity::class.java
        assertTrue(messageConverter.canWrite(type, type, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanWriteGenericParametrizedType() {
        val list = object : ArrayList<TestEntity>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType
        assertTrue(messageConverter.canWrite(typedListType, list::class.java, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanWriteGenericList() {
        val list = object : ArrayList<TestEntity>() {}
        val type = list::class.java
        assertTrue(messageConverter.canWrite(type, type, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanWriteGenericUnsupportedType() {
        val type = String::class.java
        assertFalse(messageConverter.canWrite(type, type, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanWriteGenericUnsupportedParametrizedType() {
        val list = object : ArrayList<String>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType
        assertFalse(messageConverter.canWrite(typedListType, list::class.java, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanWriteGenericUnsupportedList() {
        val list = object : ArrayList<String>() {}
        val type = list::class.java
        assertFalse(messageConverter.canWrite(type, type, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanWriteGenericUnsupportedSet() {
        val set = object : HashSet<String>() {}
        val type = set::class.java
        assertFalse(messageConverter.canWrite(type, type, APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCanWriteGenericUnsupportedMediaType() {
        val type = TestEntity::class.java
        assertFalse(messageConverter.canWrite(type, type, TEXT_MARKDOWN))
    }

    @Test
    fun testRead() {
        val content: String = "{\"id\":\"test1\"}"
        val msg = MockHttpInputMessage(content.toByteArray(UTF_8))

        val result = messageConverter.read(TestEntity::class.java, msg) as TestEntity
        assertEquals(expected = "test1", actual = result.id)
    }

    @Test
    fun testReadList() {
        val list = object : ArrayList<TestEntity>() {}
        val content: String = "[{\"id\":\"test1\"},{\"id\":\"test2\"}]"
        val msg = MockHttpInputMessage(content.toByteArray(UTF_8))

        @Suppress("UNCHECKED_CAST")
        val result = messageConverter.read(list::class.java, msg) as List<TestEntity>
        assertEquals(expected = 2, actual = result.size)
        assertEquals(expected = "test1", actual = result[0].id)
        assertEquals(expected = "test2", actual = result[1].id)
    }

    @Test
    fun testReadUnsupportedType() {
        val content: String = "{\"id\":\"test1\"}"
        val msg = MockHttpInputMessage(content.toByteArray(UTF_8))

        thrown.expect(IllegalArgumentException::class.java)
        thrown.expectMessage("Unsupported type java.lang.String")
        assertNull(messageConverter.read(String::class.java, msg))
    }

    @Test
    fun testReadInternal() {
        val content: String = "{\"id\":\"test1\"}"
        val msg = MockHttpInputMessage(content.toByteArray(UTF_8))

        val result = messageConverter.read(TestEntity::class.java, null, msg) as TestEntity
        assertEquals(expected = "test1", actual = result.id)
    }

    @Test
    fun testReadParametrizedType() {
        val list = object : ArrayList<TestEntity>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType

        val content: String = "[{\"id\":\"test1\"},{\"id\":\"test2\"}]"
        val msg = MockHttpInputMessage(content.toByteArray(UTF_8))

        @Suppress("UNCHECKED_CAST")
        val result = messageConverter.read(typedListType, null, msg) as List<TestEntity>
        assertEquals(expected = 2, actual = result.size)
        assertEquals(expected = "test1", actual = result[0].id)
        assertEquals(expected = "test2", actual = result[1].id)
    }

    @Test
    fun testReadInternalList() {
        val list = object : ArrayList<TestEntity>() {}
        val content: String = "[{\"id\":\"test1\"},{\"id\":\"test2\"}]"
        val msg = MockHttpInputMessage(content.toByteArray(UTF_8))

        @Suppress("UNCHECKED_CAST")
        val result = messageConverter.read(list::class.java, null, msg) as List<TestEntity>
        assertEquals(expected = 2, actual = result.size)
        assertEquals(expected = "test1", actual = result[0].id)
        assertEquals(expected = "test2", actual = result[1].id)
    }

    @Test
    fun testReadInternalUnsupportedType() {
        val content: String = "{\"id\":\"test1\"}"
        val msg = MockHttpInputMessage(content.toByteArray(UTF_8))

        thrown.expect(IllegalArgumentException::class.java)
        thrown.expectMessage("Unsupported type java.lang.String")
        assertNull(messageConverter.read(String::class.java, null, msg))
    }

    @Test
    fun testWriteInternal() {
        val msg = MockHttpOutputMessage()
        messageConverter.write(TestEntity("test1"), TestEntity::class.java, APPLICATION_JSON_UTF8, msg)

        assertEquals(expected = "{\"id\":\"test1\"}", actual = msg.bodyAsString)
        assertEquals(expected = APPLICATION_JSON_UTF8, actual = msg.headers.contentType)
    }

    @Test
    fun testWriteInternalParametrizedType() {
        val list = object : ArrayList<TestEntity>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType
        val msg = MockHttpOutputMessage()

        val values = listOf(TestEntity("test1"), TestEntity("test2"))
        messageConverter.write(values, typedListType, APPLICATION_JSON_UTF8, msg)

        assertEquals(expected = "[{\"id\":\"test1\"},{\"id\":\"test2\"}]", actual = msg.bodyAsString)
        assertEquals(expected = APPLICATION_JSON_UTF8, actual = msg.headers.contentType)
    }

    @Test
    fun testWriteInternalList() {
        val msg = MockHttpOutputMessage()

        val values = listOf(TestEntity("test1"), TestEntity("test2"))
        val typedList = object : ArrayList<TestEntity>(values) {}

        messageConverter.write(typedList, null, APPLICATION_JSON_UTF8, msg)

        assertEquals(expected = "[{\"id\":\"test1\"},{\"id\":\"test2\"}]", actual = msg.bodyAsString)
        assertEquals(expected = APPLICATION_JSON_UTF8, actual = msg.headers.contentType)
    }

    @Test
    fun testWriteUnsupportedType() {
        val msg = MockHttpOutputMessage()

        thrown.expect(IllegalArgumentException::class.java)
        thrown.expectMessage("Unsupported type java.lang.String")
        messageConverter.write("test1", String::class.java, APPLICATION_JSON_UTF8, msg)
    }

    @Test
    fun testWriteUnsupportedSet() {
        val set = object : HashSet<TestEntity>() {}
        val typedSetType = set::class.java.genericSuperclass as ParameterizedType

        val msg = MockHttpOutputMessage()

        thrown.expect(IllegalArgumentException::class.java)
        thrown.expectMessage("Expected java.util.List but was java.util.LinkedHashSet")
        val values = setOf(TestEntity("test1"), TestEntity("test2"))
        messageConverter.write(values, typedSetType, APPLICATION_JSON_UTF8, msg)
    }

}
