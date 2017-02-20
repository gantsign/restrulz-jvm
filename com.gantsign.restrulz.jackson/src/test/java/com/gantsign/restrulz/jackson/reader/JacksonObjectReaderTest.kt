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
package com.gantsign.restrulz.jackson.reader

import com.fasterxml.jackson.core.JsonFactory
import com.gantsign.restrulz.jackson.Empty
import com.gantsign.restrulz.json.reader.ParseException
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class JacksonObjectReaderTest {

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    fun String.toInputStream(): InputStream {
        return ByteArrayInputStream(this.toByteArray(UTF_8))
    }

    @Test
    fun testReadObjectFromStream() {
        val empty: Empty = EmptyReader.readObject("{}".toInputStream())
        assertNotNull(empty)
    }

    @Test
    fun testReadObjectFromStreamBadStart() {
        thrown.expect(ParseException::class.java)
        thrown.expectMessage("Expected START_OBJECT but was START_ARRAY")

        EmptyReader.readObject("[]".toInputStream())
    }

    @Test
    fun testReadObjectFromStreamNull() {
        thrown.expect(ParseException::class.java)
        thrown.expectMessage("Expected START_OBJECT but was VALUE_NULL")

        EmptyReader.readObject("null".toInputStream())
    }

    @Test
    fun testReadArrayFromStream() {
        val values: List<Empty> = EmptyReader.readArray("[{},{}]".toInputStream())
        assertEquals(expected = 2, actual = values.size)
    }

    @Test
    fun testReadArrayFromEOF() {
        val jsonFactory = JsonFactory()
        val parser = ValidationHandlingJsonParser(jsonFactory.createParser(""))

        parser.nextToken()

        EmptyReader.readArray(parser)

        assertTrue(parser.hasValidationFailures)
        assertEquals(
                expected = 1,
                actual = parser.validationFailureMessages.size)
        assertEquals(
                expected = "[1:0] Expected START_ARRAY but was EOF",
                actual = parser.validationFailureMessages[0])
    }

    @Test
    fun testReadArrayFromStreamBadStart() {
        thrown.expect(ParseException::class.java)
        thrown.expectMessage("Expected START_ARRAY but was START_OBJECT")

        EmptyReader.readArray("{{},{}}".toInputStream())
    }

    @Test
    fun testReadArrayFromStreamUnterminated() {
        thrown.expect(ParseException::class.java)
        thrown.expectMessage("Expected END_ARRAY but was EOF")

        EmptyReader.readArray("[{},{}".toInputStream())
    }

    @Test
    fun testReadArrayFromStreamNull() {
        thrown.expect(ParseException::class.java)
        thrown.expectMessage("Expected START_ARRAY but was VALUE_NULL")

        EmptyReader.readArray("null".toInputStream())
    }

    @Test
    fun testReadArrayFromStreamNullElement() {
        thrown.expect(ParseException::class.java)
        thrown.expectMessage("Expected START_OBJECT but was VALUE_NULL")

        EmptyReader.readArray("[null,{}]".toInputStream())
    }
}
