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
import com.fasterxml.jackson.core.JsonToken
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidationHandlingJsonParserTest {

    @Test
    fun testHasValidationFailures() {
        val jsonFactory = JsonFactory()
        val parser = ValidationHandlingJsonParser(jsonFactory.createParser(""))
        assertFalse(parser.hasValidationFailures)

        parser.handleValidationFailure("a")
        assertTrue(parser.hasValidationFailures)
    }

    @Test
    fun testHandleValidationFailureStart() {
        val jsonFactory = JsonFactory()
        val parser = ValidationHandlingJsonParser(jsonFactory.createParser(""))

        parser.handleValidationFailure("a")
        assertTrue(parser.hasValidationFailures)

        val validationFailureMessages = parser.validationFailureMessages
        assertEquals(expected = 1, actual = validationFailureMessages.size)
        assertEquals(expected = "[1:0] a", actual = validationFailureMessages[0])
    }

    @Test
    fun testHandleValidationFailureMiddle() {
        val jsonFactory = JsonFactory()
        val parser = ValidationHandlingJsonParser(jsonFactory.createParser("[\n    \"b\"]"))
        assertEquals(expected = JsonToken.START_ARRAY, actual = parser.nextToken())
        assertEquals(expected = JsonToken.VALUE_STRING, actual = parser.nextToken())

        parser.handleValidationFailure("c")
        assertTrue(parser.hasValidationFailures)

        val validationFailureMessages = parser.validationFailureMessages
        assertEquals(expected = 1, actual = validationFailureMessages.size)
        assertEquals(expected = "[2:5] c", actual = validationFailureMessages[0])
    }
}
