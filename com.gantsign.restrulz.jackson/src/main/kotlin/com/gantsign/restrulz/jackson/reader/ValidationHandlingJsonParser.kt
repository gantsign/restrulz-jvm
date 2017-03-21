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

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.util.JsonParserDelegate
import com.gantsign.restrulz.validation.ValidationHandler

/**
 * A Jackson [JsonParser] with the additional feature of being able to record validation failures.
 *
 * @constructor Creates a validation handling JSON parser by wrapping the specified [JsonParser].
 * @param jsonParser the parser to delegate the actual JSON parsing to.
 */
class ValidationHandlingJsonParser(jsonParser: JsonParser) : JsonParserDelegate(jsonParser), ValidationHandler {

    /**
     * The validation failures that have occurred.
     */
    val validationFailureMessages: MutableList<String> = mutableListOf()

    /**
     * Whether validation failures have occurred.
     */
    val hasValidationFailures: Boolean
        get() = !validationFailureMessages.isEmpty()

    override fun handleValidationFailure(failureMessage: String) {
        val location = tokenLocation
        validationFailureMessages.add("[${location.lineNr}:${location.columnNr}] $failureMessage")
    }

}
