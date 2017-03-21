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
package com.gantsign.restrulz.json

import com.gantsign.restrulz.json.model.BadEntity
import com.gantsign.restrulz.json.model.TestEntity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.lang.reflect.ParameterizedType
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class JsonObjectMapperTest {

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    private lateinit var objectMapper: JsonObjectMapper

    @Before
    fun setup() {
        objectMapper = JsonObjectMapper()
    }

    @Test
    fun testIsSupportedForReading() {
        assertTrue(objectMapper.isSupportedForReading(TestEntity::class.java))
    }

    @Test
    fun testIsSupportedForReadingParametrizedType() {
        val list = object : ArrayList<TestEntity>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType
        assertTrue(objectMapper.isSupportedForReading(typedListType))
    }

    @Test
    fun testIsSupportedForReadingList() {
        val list = object : ArrayList<TestEntity>() {}
        assertTrue(objectMapper.isSupportedForReading(list::class.java))
    }

    @Test
    fun testIsSupportedForReadingCached() {
        assertTrue(objectMapper.isSupportedForReading(TestEntity::class.java))
        assertTrue(objectMapper.isSupportedForReading(TestEntity::class.java))
    }

    @Test
    fun testIsSupportedForReadingParametrizedTypeCached() {
        val list = object : ArrayList<TestEntity>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType
        assertTrue(objectMapper.isSupportedForReading(typedListType))
        assertTrue(objectMapper.isSupportedForReading(typedListType))
    }

    @Test
    fun testIsSupportedForReadingListCached() {
        val list = object : ArrayList<TestEntity>() {}
        assertTrue(objectMapper.isSupportedForReading(list::class.java))
        assertTrue(objectMapper.isSupportedForReading(list::class.java))
    }

    @Test
    fun testIsSupportedForReadingUnsupportedType() {
        assertFalse(objectMapper.isSupportedForReading(String::class.java))
    }

    @Test
    fun testIsSupportedForReadingUnsupportedParametrizedType() {
        val list = object : ArrayList<String>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType
        assertFalse(objectMapper.isSupportedForReading(typedListType))
    }

    @Test
    fun testIsSupportedForReadingUnsupportedList() {
        val list = object : ArrayList<String>() {}
        assertFalse(objectMapper.isSupportedForReading(list::class.java))
    }

    @Test
    fun testIsSupportedForReadingUnsupportedTypeCached() {
        assertFalse(objectMapper.isSupportedForReading(String::class.java))
        assertFalse(objectMapper.isSupportedForReading(String::class.java))
    }

    @Test
    fun testIsSupportedForReadingUnsupportedParametrizedTypeCached() {
        val list = object : ArrayList<String>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType
        assertFalse(objectMapper.isSupportedForReading(typedListType))
        assertFalse(objectMapper.isSupportedForReading(typedListType))
    }

    @Test
    fun testIsSupportedForReadingUnsupportedListCached() {
        val list = object : ArrayList<String>() {}
        assertFalse(objectMapper.isSupportedForReading(list::class.java))
        assertFalse(objectMapper.isSupportedForReading(list::class.java))
    }

    @Test
    fun testIsSupportedForReadingBadFactory() {
        assertFalse(objectMapper.isSupportedForReading(BadEntity::class.java))
    }

    @Test
    fun testIsSupportedForReadingUnsupportedSet() {
        val set = object : HashSet<String>() {}
        val typedSetType = set::class.java.genericSuperclass as ParameterizedType
        assertFalse(objectMapper.isSupportedForReading(typedSetType))
    }

    @Test
    fun testRead() {
        val content: String = "{\"id\":\"test1\"}"
        val buf = ByteArrayInputStream(content.toByteArray(UTF_8))

        val result = objectMapper.read(TestEntity::class.java, buf) as TestEntity
        assertEquals(expected = "test1", actual = result.id)
    }

    @Test
    fun testReadParametrizedType() {
        val list = object : ArrayList<TestEntity>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType

        val content: String = "[{\"id\":\"test1\"},{\"id\":\"test2\"}]"
        val buf = ByteArrayInputStream(content.toByteArray(UTF_8))

        @Suppress("UNCHECKED_CAST")
        val result = objectMapper.read(typedListType, buf) as List<TestEntity>
        assertEquals(expected = 2, actual = result.size)
        assertEquals(expected = "test1", actual = result[0].id)
        assertEquals(expected = "test2", actual = result[1].id)
    }

    @Test
    fun testReadList() {
        val list = object : ArrayList<TestEntity>() {}
        val content: String = "[{\"id\":\"test1\"},{\"id\":\"test2\"}]"
        val buf = ByteArrayInputStream(content.toByteArray(UTF_8))

        @Suppress("UNCHECKED_CAST")
        val result = objectMapper.read(list::class.java, buf) as List<TestEntity>
        assertEquals(expected = 2, actual = result.size)
        assertEquals(expected = "test1", actual = result[0].id)
        assertEquals(expected = "test2", actual = result[1].id)
    }

    @Test
    fun testReadUnsupportedType() {
        val content: String = "{\"id\":\"test1\"}"
        val buf = ByteArrayInputStream(content.toByteArray(UTF_8))

        thrown.expect(IllegalArgumentException::class.java)
        thrown.expectMessage("Unsupported type java.lang.String")
        assertNull(objectMapper.read(String::class.java, buf))
    }

    @Test
    fun testIsSupportedForWriting() {
        assertTrue(objectMapper.isSupportedForWriting(TestEntity::class.java))
    }

    @Test
    fun testIsSupportedForWritingParametrizedType() {
        val list = object : ArrayList<TestEntity>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType
        assertTrue(objectMapper.isSupportedForWriting(typedListType))
    }

    @Test
    fun testIsSupportedForWritingList() {
        val list = object : ArrayList<TestEntity>() {}
        assertTrue(objectMapper.isSupportedForWriting(list::class.java))
    }

    @Test
    fun testIsSupportedForWritingCached() {
        assertTrue(objectMapper.isSupportedForWriting(TestEntity::class.java))
        assertTrue(objectMapper.isSupportedForWriting(TestEntity::class.java))
    }

    @Test
    fun testIsSupportedForWritingParametrizedTypeCached() {
        val list = object : ArrayList<TestEntity>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType
        assertTrue(objectMapper.isSupportedForWriting(typedListType))
        assertTrue(objectMapper.isSupportedForWriting(typedListType))
    }

    @Test
    fun testIsSupportedForWritingListCached() {
        val list = object : ArrayList<TestEntity>() {}
        assertTrue(objectMapper.isSupportedForWriting(list::class.java))
        assertTrue(objectMapper.isSupportedForWriting(list::class.java))
    }

    @Test
    fun testIsSupportedForUnsupportedWritingType() {
        assertFalse(objectMapper.isSupportedForWriting(String::class.java))
    }

    @Test
    fun testIsSupportedForWritingUnsupportedParametrizedType() {
        val list = object : ArrayList<String>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType
        assertFalse(objectMapper.isSupportedForWriting(typedListType))
    }

    @Test
    fun testIsSupportedForWritingUnsupportedList() {
        val list = object : ArrayList<String>() {}
        assertFalse(objectMapper.isSupportedForWriting(list::class.java))
    }

    @Test
    fun testIsSupportedForWritingUnsupportedTypeCached() {
        assertFalse(objectMapper.isSupportedForWriting(String::class.java))
        assertFalse(objectMapper.isSupportedForWriting(String::class.java))
    }

    @Test
    fun testIsSupportedForWritingUnsupportedParametrizedTypeCached() {
        val list = object : ArrayList<String>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType
        assertFalse(objectMapper.isSupportedForWriting(typedListType))
        assertFalse(objectMapper.isSupportedForWriting(typedListType))
    }

    @Test
    fun testIsSupportedForWritingUnsupportedListCached() {
        val list = object : ArrayList<String>() {}
        assertFalse(objectMapper.isSupportedForWriting(list::class.java))
        assertFalse(objectMapper.isSupportedForWriting(list::class.java))
    }

    @Test
    fun testIsSupportedForWritingBadFactory() {
        assertFalse(objectMapper.isSupportedForWriting(BadEntity::class.java))
    }

    @Test
    fun testIsSupportedForWritingUnsupportedSet() {
        val set = object : HashSet<String>() {}
        val typedSetType = set::class.java.genericSuperclass as ParameterizedType
        assertFalse(objectMapper.isSupportedForWriting(typedSetType))
    }

    @Test
    fun testWrite() {
        val buf = ByteArrayOutputStream()
        objectMapper.write(TestEntity("test1"), TestEntity::class.java, buf)

        assertEquals(
                expected = "{\"id\":\"test1\"}",
                actual = String(buf.toByteArray(), UTF_8))
    }

    @Test
    fun testWriteParametrizedType() {
        val list = object : ArrayList<TestEntity>() {}
        val typedListType = list::class.java.genericSuperclass as ParameterizedType

        val buf = ByteArrayOutputStream()
        objectMapper.write(listOf(TestEntity("test1"), TestEntity("test2")), typedListType, buf)

        assertEquals(
                expected = "[{\"id\":\"test1\"},{\"id\":\"test2\"}]",
                actual = String(buf.toByteArray(), UTF_8))
    }

    @Test
    fun testWriteList() {
        val values = listOf(TestEntity("test1"), TestEntity("test2"))
        val typedList = object : ArrayList<TestEntity>(values) {}

        val buf = ByteArrayOutputStream()
        objectMapper.write(typedList, null, buf)

        assertEquals(
                expected = "[{\"id\":\"test1\"},{\"id\":\"test2\"}]",
                actual = String(buf.toByteArray(), UTF_8))
    }

    @Test
    fun testWriteUnsupportedType() {
        val buf = ByteArrayOutputStream()

        thrown.expect(IllegalArgumentException::class.java)
        thrown.expectMessage("Unsupported type java.lang.String")
        objectMapper.write("test1", String::class.java, buf)
    }

    @Test
    fun testWriteUnsupportedSet() {
        val set = object : HashSet<TestEntity>() {}
        val typedSetType = set::class.java.genericSuperclass as ParameterizedType

        val buf = ByteArrayOutputStream()

        thrown.expect(IllegalArgumentException::class.java)
        thrown.expectMessage("Expected java.util.List but was java.util.LinkedHashSet")
        objectMapper.write(setOf(TestEntity("test1"), TestEntity("test2")), typedSetType, buf)
    }
}
