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
import com.fasterxml.jackson.core.io.JsonEOFException
import com.gantsign.restrulz.json.reader.JsonObjectReader
import com.gantsign.restrulz.json.reader.ParseException
import java.io.InputStream

/**
 * Jackson based implementation for parsing JSON.
 *
 * @param T the type of object to be parsed.
 */
@Suppress("unused")
abstract class JacksonObjectReader<out T> : JsonObjectReader<T> {

    /**
     * Returns an instance of the object using the property values in the JSON object literal.
     *
     * @param parser the parser to read properties from and record validation errors to.
     * @return An instance of the object or `null` if an error occurs.
     */
    abstract fun readRequiredObject(parser: ValidationHandlingJsonParser): T?

    /**
     * Returns an instance of the object using the property values in the JSON object literal.
     *
     * @param parser the parser to read properties from and record validation errors to.
     * @return An instance of the object or `null` if the value is `null` or an error occurs.
     */
    fun readOptionalObject(parser: ValidationHandlingJsonParser): T? {
        if (parser.currentToken() == JsonToken.VALUE_NULL) {
            return null
        }
        return readRequiredObject(parser)
    }

    override fun readObject(input: InputStream): T {
        val jsonFactory = JsonFactory()
        val parser = ValidationHandlingJsonParser(jsonFactory.createParser(input))

        parser.nextToken()

        val result = readRequiredObject(parser)
        if (result === null) {
            throw ParseException(parser.validationFailureMessages)
        }
        return result
    }

    /**
     * Returns a list of instance of the object using the object literals contained in a JSON array
     * literal.
     *
     * @param parser the parser to read properties from and record validation errors to.
     * @return A list of objects or `null` if an error occurs.
     */
    fun readRequiredArray(parser: ValidationHandlingJsonParser): List<T>? {
        // Sanity check: verify that we got "Json Array":
        when (parser.currentToken()) {
            null -> {
                parser.handleValidationFailure("Expected ${JsonToken.START_ARRAY} but was EOF")
                return null
            }
            JsonToken.START_ARRAY -> {
                // continue
            }
            else -> {
                parser.handleValidationFailure("Expected ${JsonToken.START_ARRAY} but was ${parser.currentToken()}")
                return null
            }
        }

        val mutableList: MutableList<T> = mutableListOf()
        try {
            while (parser.nextToken() !== JsonToken.END_ARRAY) {

                val element = readRequiredObject(parser)

                if (element === null) {
                    continue
                }

                mutableList.add(element)
            }
        } catch(e: JsonEOFException) {
            parser.handleValidationFailure("Expected ${JsonToken.END_ARRAY} but was EOF")
        }

        if (parser.hasValidationFailures) {
            return null
        }

        return mutableList.toList()
    }

    /**
     * Returns a list of instance of the object using the object literals contained in a JSON array
     * literal.
     *
     * @param parser the parser to read properties from and record validation errors to.
     * @return A list of objects or `null` if the value is `null` or an error occurs.
     */
    fun readOptionalArray(parser: ValidationHandlingJsonParser): List<T>? {
        if (parser.currentToken() == JsonToken.VALUE_NULL) {
            return null
        }
        return readRequiredArray(parser)
    }

    override fun readArray(input: InputStream): List<T> {
        val jsonFactory = JsonFactory()
        val parser = ValidationHandlingJsonParser(jsonFactory.createParser(input))

        parser.nextToken()

        val result = readRequiredArray(parser)
        if (result === null) {
            throw ParseException(parser.validationFailureMessages)
        }
        return result
    }

}
