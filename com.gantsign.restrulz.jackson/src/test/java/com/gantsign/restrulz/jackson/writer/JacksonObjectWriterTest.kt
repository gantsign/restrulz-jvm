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
package com.gantsign.restrulz.jackson.writer

import com.fasterxml.jackson.core.JsonGenerator
import com.gantsign.restrulz.jackson.Empty
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.test.AssertionError
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JacksonObjectWriterTest {

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    fun ByteArrayOutputStream.utf8ToString(): String {
        return String(this.toByteArray(), UTF_8)
    }

    @Test
    fun testWriteNumberFieldByte() {
        val generator = mock(JsonGenerator::class.java)
        val testWriter = object : JacksonObjectWriter<String>() {
            override fun writeObject(generator: JsonGenerator, value: String?) {
                var number: Byte? = 12
                generator.writeNumberField("test1", number)
                // Kotlin will call the wrong method if it's certain the value is null
                number = if (System.currentTimeMillis() > 0) null else 12
                generator.writeNumberField("test2", number)
            }
        }
        testWriter.writeObject(generator, null)
        verify(generator).writeNumberField("test1", 12)
        verify(generator).writeNullField("test2")
    }

    @Test
    fun testWriteNumberFieldShort() {
        val generator = mock(JsonGenerator::class.java)
        val testWriter = object : JacksonObjectWriter<String>() {
            override fun writeObject(generator: JsonGenerator, value: String?) {
                var number: Short? = 12
                generator.writeNumberField("test1", number)
                // Kotlin will call the wrong method if it's certain the value is null
                number = if (System.currentTimeMillis() > 0) null else 12
                generator.writeNumberField("test2", number)
            }
        }
        testWriter.writeObject(generator, null)
        verify(generator).writeNumberField("test1", 12)
        verify(generator).writeNullField("test2")
    }

    @Test
    fun testWriteNumberFieldInt() {
        val generator = mock(JsonGenerator::class.java)
        val testWriter = object : JacksonObjectWriter<String>() {
            override fun writeObject(generator: JsonGenerator, value: String?) {
                var number: Int? = 12
                generator.writeNumberField("test1", number)
                // Kotlin will call the wrong method if it's certain the value is null
                number = if (System.currentTimeMillis() > 0) null else 12
                generator.writeNumberField("test2", number)
            }
        }
        testWriter.writeObject(generator, null)
        verify(generator).writeNumberField("test1", 12)
        verify(generator).writeNullField("test2")
    }

    @Test
    fun testWriteNumberFieldLong() {
        val generator = mock(JsonGenerator::class.java)
        val testWriter = object : JacksonObjectWriter<String>() {
            override fun writeObject(generator: JsonGenerator, value: String?) {
                var number: Long? = 12
                generator.writeNumberField("test1", number)
                // Kotlin will call the wrong method if it's certain the value is null
                number = if (System.currentTimeMillis() > 0) null else 12
                generator.writeNumberField("test2", number)
            }
        }
        testWriter.writeObject(generator, null)
        verify(generator).writeNumberField("test1", 12L)
        verify(generator).writeNullField("test2")
    }

    @Test
    fun testWriteStringArrayField() {
        val generator = mock(JsonGenerator::class.java)
        val testWriter = object : JacksonObjectWriter<String>() {
            override fun writeObject(generator: JsonGenerator, value: String?) {
                generator.writeStringArrayField("test1", listOf("test2", "test3"))
            }
        }
        testWriter.writeObject(generator, null)
        verify(generator).writeFieldName("test1")
        verify(generator).writeStartArray(2)
        verify(generator).writeString("test2")
        verify(generator).writeString("test3")
        verify(generator).writeEndArray()
    }

    @Test
    fun testWriteBooleanArrayField() {
        val generator = mock(JsonGenerator::class.java)
        val testWriter = object : JacksonObjectWriter<String>() {
            override fun writeObject(generator: JsonGenerator, value: String?) {
                generator.writeBooleanArrayField("test1", listOf(true, false))
            }
        }
        testWriter.writeObject(generator, null)
        verify(generator).writeFieldName("test1")
        verify(generator).writeStartArray(2)
        verify(generator).writeBoolean(true)
        verify(generator).writeBoolean(false)
        verify(generator).writeEndArray()
    }

    @Test
    fun testWriteByteArrayField() {
        val generator = mock(JsonGenerator::class.java)
        val testWriter = object : JacksonObjectWriter<String>() {
            override fun writeObject(generator: JsonGenerator, value: String?) {
                generator.writeByteArrayField("test1", listOf(1, 2))
            }
        }
        testWriter.writeObject(generator, null)
        verify(generator).writeFieldName("test1")
        verify(generator).writeStartArray(2)
        verify(generator).writeNumber(1)
        verify(generator).writeNumber(2)
        verify(generator).writeEndArray()
    }

    @Test
    fun testWriteShortArrayField() {
        val generator = mock(JsonGenerator::class.java)
        val testWriter = object : JacksonObjectWriter<String>() {
            override fun writeObject(generator: JsonGenerator, value: String?) {
                generator.writeShortArrayField("test1", listOf(1, 2))
            }
        }
        testWriter.writeObject(generator, null)
        verify(generator).writeFieldName("test1")
        verify(generator).writeStartArray(2)
        verify(generator).writeNumber(1.toShort())
        verify(generator).writeNumber(2.toShort())
        verify(generator).writeEndArray()
    }

    @Test
    fun testWriteIntArrayField() {
        val generator = mock(JsonGenerator::class.java)
        val testWriter = object : JacksonObjectWriter<String>() {
            override fun writeObject(generator: JsonGenerator, value: String?) {
                generator.writeIntArrayField("test1", listOf(1, 2))
            }
        }
        testWriter.writeObject(generator, null)
        verify(generator).writeFieldName("test1")
        verify(generator).writeStartArray(2)
        verify(generator).writeNumber(1)
        verify(generator).writeNumber(2)
        verify(generator).writeEndArray()
    }

    @Test
    fun testWriteLongArrayField() {
        val generator = mock(JsonGenerator::class.java)
        val testWriter = object : JacksonObjectWriter<String>() {
            override fun writeObject(generator: JsonGenerator, value: String?) {
                generator.writeLongArrayField("test1", listOf(1, 2))
            }
        }
        testWriter.writeObject(generator, null)
        verify(generator).writeFieldName("test1")
        verify(generator).writeStartArray(2)
        verify(generator).writeNumber(1L)
        verify(generator).writeNumber(2L)
        verify(generator).writeEndArray()
    }

    @Test
    fun testWriteObjectField() {
        val generator = mock(JsonGenerator::class.java)
        var called = false
        val testWriter = object : JacksonObjectWriter<String>() {
            override fun writeObject(generator: JsonGenerator, value: String?) {
                called = true
                assertEquals(expected = "test2", actual = value)
            }
        }
        testWriter.writeObjectField(generator, "test1", "test2")
        assertTrue(called)
        verify(generator).writeFieldName("test1")
    }

    @Test
    fun testWriteArray() {
        val generator = mock(JsonGenerator::class.java)
        val values = listOf("test2", "test3")
        val recordedValues = mutableListOf<String>()
        val testWriter = object : JacksonObjectWriter<String>() {
            override fun writeObject(generator: JsonGenerator, value: String?) {
                if (value === null) {
                    throw AssertionError()
                }
                recordedValues.add(value)
            }
        }
        testWriter.writeArray(generator, values)
        assertEquals(expected = values, actual = recordedValues)
    }

    @Test
    fun testWriteArrayField() {
        val generator = mock(JsonGenerator::class.java)
        val values = listOf("test2", "test3")
        val recordedValues = mutableListOf<String>()
        val testWriter = object : JacksonObjectWriter<String>() {
            override fun writeObject(generator: JsonGenerator, value: String?) {
                if (value === null) {
                    throw AssertionError()
                }
                recordedValues.add(value)
            }
        }
        testWriter.writeArrayField(generator, "test1", values)
        assertEquals(expected = values, actual = recordedValues)
        verify(generator).writeFieldName("test1")
    }

    @Test
    fun testWriteAsJson() {
        val buf = ByteArrayOutputStream()
        EmptyWriter.writeAsJson(buf, Empty())
        assertEquals(expected = "{}", actual = buf.utf8ToString())
    }

    @Test
    fun testWriteAsJsonException() {
        thrown.expect(RuntimeException::class.java)

        val buf = ByteArrayOutputStream()
        ExceptionWriter.writeAsJson(buf, Empty())
    }

    @Test
    fun testToJsonString() {
        assertEquals(expected = "{ }", actual = EmptyWriter.toJsonString(Empty()))
    }

    @Test
    fun testToJsonStringException() {
        thrown.expect(RuntimeException::class.java)

        ExceptionWriter.toJsonString(Empty())
    }

    @Test
    fun testWriteAsJsonArray() {
        val buf = ByteArrayOutputStream()
        EmptyWriter.writeAsJsonArray(buf, listOf(Empty(), Empty()))
        assertEquals(expected = "[{},{}]", actual = buf.utf8ToString())
    }

    @Test
    fun testWriteAsJsonArrayException() {
        thrown.expect(RuntimeException::class.java)

        val buf = ByteArrayOutputStream()
        ExceptionWriter.writeAsJsonArray(buf, listOf(Empty(), Empty()))
    }

    @Test
    fun testToJsonArrayString() {
        assertEquals(
                expected = "[ { }, { } ]",
                actual = EmptyWriter.toJsonArrayString(listOf(Empty(), Empty())))
    }

    @Test
    fun testToJsonArrayStringException() {
        thrown.expect(RuntimeException::class.java)

        ExceptionWriter.toJsonArrayString(listOf(Empty(), Empty()))
    }

}
