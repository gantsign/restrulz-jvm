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

import com.fasterxml.jackson.core.JsonToken
import com.gantsign.restrulz.jackson.Empty

object EmptyReader : JacksonObjectReader<Empty>() {

    override fun readObject(parser: ValidationHandlingJsonParser): Empty? {
        // Sanity check: verify that we got "Json Object":
        val startObject = parser.currentToken()
        when (startObject) {
            JsonToken.VALUE_NULL -> {
                return null
            }
            JsonToken.START_OBJECT -> {
                // continue
            }
            else -> {
                parser.handleValidationFailure("Expected ${JsonToken.START_OBJECT} but was $startObject")
                return null
            }
        }

        // Iterate over object fields
        while (parser.nextToken() !== JsonToken.END_OBJECT) {
            // continue
        }

        if (parser.hasValidationFailures) {
            return null
        }
        return Empty()
    }
}
