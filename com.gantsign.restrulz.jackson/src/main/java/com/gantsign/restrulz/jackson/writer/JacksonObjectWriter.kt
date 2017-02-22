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

import com.fasterxml.jackson.core.JsonEncoding
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.gantsign.restrulz.json.writer.JsonObjectWriter
import java.io.OutputStream
import java.io.StringWriter

/**
 * Base class for implementing writing objects as JSON using Jackson.
 *
 * @param T the type of object being written out.
 */
@Suppress("unused")
abstract class JacksonObjectWriter<in T> : JsonObjectWriter<T> {

    /**
     * Convenience method for outputting a field entry ("member")
     * that has the specified numeric value. Equivalent to:
     *
     * ```
     * writeFieldName(fieldName);
     * if (value == null) {
     *   writeNull();
     * } else {
     *   writeNumber(value);
     * }
     * ```
     *
     * @param fieldName the name of the field being written out.
     * @param value the value to write out.
     */
    protected fun JsonGenerator.writeNumberField(fieldName: String, value: Byte?) {
        if (value === null) {
            writeNullField(fieldName)
        } else {
            writeNumberField(fieldName, value.toInt())
        }
    }

    /**
     * Convenience method for outputting a field entry ("member")
     * that has the specified numeric value. Equivalent to:
     *
     * ```
     * writeFieldName(fieldName);
     * if (value == null) {
     *   writeNull();
     * } else {
     *   writeNumber(value);
     * }
     * ```
     *
     * @param fieldName the name of the field being written out.
     * @param value the value to write out.
     */
    protected fun JsonGenerator.writeNumberField(fieldName: String, value: Short?) {
        if (value === null) {
            writeNullField(fieldName)
        } else {
            writeNumberField(fieldName, value.toInt())
        }
    }

    /**
     * Convenience method for outputting a field entry ("member")
     * that has the specified numeric value. Equivalent to:
     *
     * ```
     * writeFieldName(fieldName);
     * if (value == null) {
     *   writeNull();
     * } else {
     *   writeNumber(value);
     * }
     * ```
     *
     * @param fieldName the name of the field being written out.
     * @param value the value to write out.
     */
    protected fun JsonGenerator.writeNumberField(fieldName: String, value: Int?) {
        if (value === null) {
            writeNullField(fieldName)
        } else {
            writeNumberField(fieldName, value)
        }
    }

    /**
     * Convenience method for outputting a field entry ("member")
     * that has the specified numeric value. Equivalent to:
     *
     * ```
     * writeFieldName(fieldName);
     * if (value == null) {
     *   writeNull();
     * } else {
     *   writeNumber(value);
     * }
     * ```
     *
     * @param fieldName the name of the field being written out.
     * @param value the value to write out.
     */
    protected fun JsonGenerator.writeNumberField(fieldName: String, value: Long?) {
        if (value === null) {
            writeNullField(fieldName)
        } else {
            writeNumberField(fieldName, value)
        }
    }

    /**
     * Convenience method for outputting a field entry ("member")
     * that has the specified array value. Equivalent to:
     *
     * ```
     * writeFieldName(fieldName)
     * writeStartArray(values.size)
     * for (value in values) {
     *   writeString(value)
     * }
     * writeEndArray()
     * ```
     *
     * @param fieldName the name of the field being written out.
     * @param values the values to write out.
     */
    protected fun JsonGenerator.writeStringArrayField(fieldName: String, values: List<String>) {
        writeFieldName(fieldName)
        writeStartArray(values.size)
        for (value in values) {
            writeString(value)
        }
        writeEndArray()
    }

    /**
     * Convenience method for outputting a field entry ("member")
     * that has the specified array value. Equivalent to:
     *
     * ```
     * writeFieldName(fieldName)
     * writeStartArray(values.size)
     * for (value in values) {
     *   writeBoolean(value)
     * }
     * writeEndArray()
     * ```
     *
     * @param fieldName the name of the field being written out.
     * @param values the values to write out.
     */
    protected fun JsonGenerator.writeBooleanArrayField(fieldName: String, values: List<Boolean>) {
        writeFieldName(fieldName)
        writeStartArray(values.size)
        for (value in values) {
            writeBoolean(value)
        }
        writeEndArray()
    }

    /**
     * Convenience method for outputting a field entry ("member")
     * that has the specified array value. Equivalent to:
     *
     * ```
     * writeFieldName(fieldName)
     * writeStartArray(values.size)
     * for (value in values) {
     *   writeNumber(value)
     * }
     * writeEndArray()
     * ```
     *
     * @param fieldName the name of the field being written out.
     * @param values the values to write out.
     */
    protected fun JsonGenerator.writeByteArrayField(fieldName: String, values: List<Byte>) {
        writeFieldName(fieldName)
        writeStartArray(values.size)
        for (value in values) {
            writeNumber(value.toInt())
        }
        writeEndArray()
    }

    /**
     * Convenience method for outputting a field entry ("member")
     * that has the specified array value. Equivalent to:
     *
     * ```
     * writeFieldName(fieldName)
     * writeStartArray(values.size)
     * for (value in values) {
     *   writeNumber(value)
     * }
     * writeEndArray()
     * ```
     *
     * @param fieldName the name of the field being written out.
     * @param values the values to write out.
     */
    protected fun JsonGenerator.writeShortArrayField(fieldName: String, values: List<Short>) {
        writeFieldName(fieldName)
        writeStartArray(values.size)
        for (value in values) {
            writeNumber(value)
        }
        writeEndArray()
    }

    /**
     * Convenience method for outputting a field entry ("member")
     * that has the specified array value. Equivalent to:
     *
     * ```
     * writeFieldName(fieldName)
     * writeStartArray(values.size)
     * for (value in values) {
     *   writeNumber(value)
     * }
     * writeEndArray()
     * ```
     *
     * @param fieldName the name of the field being written out.
     * @param values the values to write out.
     */
    protected fun JsonGenerator.writeIntArrayField(fieldName: String, values: List<Int>) {
        writeFieldName(fieldName)
        writeStartArray(values.size)
        for (value in values) {
            writeNumber(value)
        }
        writeEndArray()
    }

    /**
     * Convenience method for outputting a field entry ("member")
     * that has the specified array value. Equivalent to:
     *
     * ```
     * writeFieldName(fieldName)
     * writeStartArray(values.size)
     * for (value in values) {
     *   writeNumber(value)
     * }
     * writeEndArray()
     * ```
     *
     * @param fieldName the name of the field being written out.
     * @param values the values to write out.
     */
    protected fun JsonGenerator.writeLongArrayField(fieldName: String, values: List<Long>) {
        writeFieldName(fieldName)
        writeStartArray(values.size)
        for (value in values) {
            writeNumber(value)
        }
        writeEndArray()
    }

    /**
     * Writes the specified object to the specified [JsonGenerator].
     *
     * @param generator the low level API for writing JSON.
     * @param value the object to write out.
     */
    abstract fun writeObject(generator: JsonGenerator, value: T?)

    /**
     * Convenience method for outputting a field entry ("member")
     * that has contents of specific Java object as its value.
     * Equivalent to:
     *
     * ```
     * writeFieldName(fieldName);
     * writeObject(value);
     * ```
     *
     * @param fieldName the name of the field being written out.
     * @param value the value to write out.
     */
    fun writeObjectField(generator: JsonGenerator, fieldName: String, value: T?) {
        generator.writeFieldName(fieldName)
        writeObject(generator, value)
    }

    /**
     * Writes the specified list of objects to the specified [JsonGenerator].
     *
     * @param generator the low level API for writing JSON.
     * @param values the list of objects to write out.
     */
    fun writeArray(generator: JsonGenerator, values: List<T>) {
        generator.writeStartArray()
        for (value in values) {
            writeObject(generator, value)
        }
        generator.writeEndArray()
    }

    /**
     * Convenience method for outputting a field entry ("member")
     * that has contents of specific Java object as its value.
     * Equivalent to:
     *
     * ```
     * writeFieldName(fieldName);
     * writeArray(values);
     * ```
     *
     * @param fieldName the name of the field being written out.
     * @param values the values to write out.
     */
    fun writeArrayField(generator: JsonGenerator, fieldName: String, values: List<T>) {
        generator.writeFieldName(fieldName)
        writeArray(generator, values)
    }

    override fun writeAsJson(output: OutputStream, value: T) {
        val factory = JsonFactory()
        factory.createGenerator(output, JsonEncoding.UTF8).use { generator ->
            writeObject(generator, value)
        }
    }

    override fun toJsonString(value: T): String {
        val writer = StringWriter()
        val factory = JsonFactory()
        factory.createGenerator(writer).use { generator ->
            generator.prettyPrinter = DefaultPrettyPrinter()
            writeObject(generator, value)
        }
        return writer.toString()
    }

    override fun writeAsJsonArray(output: OutputStream, values: List<T>) {
        val factory = JsonFactory()
        factory.createGenerator(output, JsonEncoding.UTF8).use { generator ->
            writeArray(generator, values)
        }
    }

    override fun toJsonArrayString(values: List<T>): String {
        val writer = StringWriter()
        val factory = JsonFactory()
        factory.createGenerator(writer).use { generator ->
            generator.prettyPrinter = DefaultPrettyPrinter()
            writeArray(generator, values)
        }
        return writer.toString()
    }
}
