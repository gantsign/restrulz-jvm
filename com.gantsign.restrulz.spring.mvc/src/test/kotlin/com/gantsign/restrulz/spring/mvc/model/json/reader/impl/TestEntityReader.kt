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
package com.gantsign.restrulz.spring.mvc.model.json.reader.impl

import com.fasterxml.jackson.core.JsonToken
import com.gantsign.restrulz.jackson.reader.JacksonObjectReader
import com.gantsign.restrulz.jackson.reader.ValidationHandlingJsonParser
import com.gantsign.restrulz.spring.mvc.model.TestEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.BitSet

object TestEntityReader : JacksonObjectReader<TestEntity>() {

    private val log: Logger = LoggerFactory.getLogger(TestEntityReader::class.java)
    private val idIndex: Int = 0

    override fun readRequiredObject(parser: ValidationHandlingJsonParser): TestEntity? {
        val startObject = parser.currentToken()
        when (startObject) {
            JsonToken.START_OBJECT -> {
                // continue
            }
            else -> {
                parser.handleValidationFailure("Expected ${JsonToken.START_OBJECT} but was $startObject")
                return null
            }
        }

        val fieldNamesPresent = BitSet()

        var idValue: String = ""

        // Iterate over object fields
        while (parser.nextToken() !== JsonToken.END_OBJECT) {
            val fieldName = parser.currentName

            // Move to value
            val token = parser.nextToken()

            when (fieldName) {
                "id" -> {
                    if (fieldNamesPresent.get(idIndex)) {
                        parser.handleValidationFailure("Repeated field name: $fieldName")
                    }
                    fieldNamesPresent.set(idIndex)

                    when (token) {
                        JsonToken.VALUE_STRING -> {
                            val value = parser.text
                            if (!value.isEmpty() && value.isBlank()) {
                                parser.handleValidationFailure("Value must not be blank string: '$value'")
                            } else {
                                idValue = value
                            }
                        }
                        else -> {
                            parser.handleValidationFailure("Expected ${JsonToken.VALUE_STRING} but was $token")
                        }
                    }
                }
                else -> {
                    if (log.isTraceEnabled) {
                        val location = parser.tokenLocation
                        log.trace("[{}:{}] Ignoring unexpected field-name: {}",
                                location.lineNr, location.columnNr, fieldName)
                    }
                }
            }
        }
        if (!fieldNamesPresent.get(idIndex)) {
            parser.handleValidationFailure("Expected field name: id")
        }

        if (parser.hasValidationFailures) {
            return null
        }
        return TestEntity(
                id = idValue)
    }
}
