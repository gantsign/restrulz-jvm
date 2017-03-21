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
package com.gantsign.restrulz.json.model.json.reader.impl

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.gantsign.restrulz.json.model.TestEntity
import com.gantsign.restrulz.json.reader.JsonObjectReader
import java.io.InputStream
import kotlin.test.assertEquals

object TestEntityReader : JsonObjectReader<TestEntity> {

    private fun readObject(parser: JsonParser): TestEntity {
        assertEquals(expected = JsonToken.START_OBJECT, actual = parser.currentToken())
        assertEquals(expected = JsonToken.FIELD_NAME, actual = parser.nextToken())
        assertEquals(expected = JsonToken.VALUE_STRING, actual = parser.nextToken())
        val id = parser.text
        assertEquals(expected = JsonToken.END_OBJECT, actual = parser.nextToken())
        return TestEntity(id)
    }

    override fun readObject(input: InputStream): TestEntity {
        val jsonFactory = JsonFactory()
        val parser = jsonFactory.createParser(input)

        parser.nextToken()
        return readObject(parser)
    }

    override fun readArray(input: InputStream): List<TestEntity> {
        val jsonFactory = JsonFactory()
        val parser = jsonFactory.createParser(input)

        val results: MutableList<TestEntity> = mutableListOf()

        assertEquals(expected = JsonToken.START_ARRAY, actual = parser.nextToken())
        while (JsonToken.END_ARRAY !== parser.nextToken()) {
            results.add(readObject(parser))
        }
        return results.toList()
    }

}
