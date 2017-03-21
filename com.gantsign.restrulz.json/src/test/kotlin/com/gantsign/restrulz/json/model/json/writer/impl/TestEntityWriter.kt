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
package com.gantsign.restrulz.json.model.json.writer.impl

import com.fasterxml.jackson.core.JsonEncoding
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.gantsign.restrulz.json.model.TestEntity
import com.gantsign.restrulz.json.writer.JsonObjectWriter
import java.io.OutputStream
import java.io.StringWriter

object TestEntityWriter : JsonObjectWriter<TestEntity> {

    fun writeObject(generator: JsonGenerator,
                    value: TestEntity?) {
        generator.writeStartObject()
        generator.writeStringField("id", value!!.id)
        generator.writeEndObject()
    }

    override fun writeAsJson(output: OutputStream, value: TestEntity) {
        val factory = JsonFactory()
        factory.createGenerator(output, JsonEncoding.UTF8).use { generator ->
            writeObject(generator, value)
        }
    }

    override fun toJsonString(value: TestEntity): String {
        val writer = StringWriter()
        val factory = JsonFactory()
        factory.createGenerator(writer).use { generator ->
            generator.prettyPrinter = DefaultPrettyPrinter()
            writeObject(generator, value)
        }
        return writer.toString()
    }

    private fun writeArray(generator: JsonGenerator, values: List<TestEntity>) {
        generator.writeStartArray()
        for (value in values) {
            writeObject(generator, value)
        }
        generator.writeEndArray()
    }

    override fun writeAsJsonArray(output: OutputStream, values: List<TestEntity>) {
        val factory = JsonFactory()
        factory.createGenerator(output, JsonEncoding.UTF8).use { generator ->
            writeArray(generator, values)
        }
    }

    override fun toJsonArrayString(values: List<TestEntity>): String {
        val writer = StringWriter()
        val factory = JsonFactory()
        factory.createGenerator(writer).use { generator ->
            generator.prettyPrinter = DefaultPrettyPrinter()
            writeArray(generator, values)
        }
        return writer.toString()
    }

}
