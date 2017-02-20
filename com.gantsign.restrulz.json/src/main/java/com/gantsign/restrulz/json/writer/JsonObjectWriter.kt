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
package com.gantsign.restrulz.json.writer

import java.io.OutputStream

/**
 * Interface for writing objects to JSON serialized form.
 *
 * @param T the type of the object being written out.
 */
interface JsonObjectWriter<in T> {

    /**
     * Writes the specified object to the output as JSON.
     *
     * @param output the stream to write the object to.
     * @param value the object to write out.
     */
    fun writeAsJson(output: OutputStream, value: T)

    /**
     * Writes the specified list of objects to the output as JSON.
     *
     * @param output the stream to write the object to.
     * @param values the objects to write out.
     */
    fun writeAsJsonArray(output: OutputStream, values: List<T>)

    /**
     * Returns the specified object as a JSON object literal.
     *
     * @param value the object to convert to a JSON object literal.
     * @return The JSON object literal as a string.
     */
    fun toJsonString(value: T): String

    /**
     * Returns the specified list of objects as a JSON array literal.
     *
     * @param value the object to convert to a JSON array literal.
     * @return The JSON array literal as a string.
     */
    fun toJsonArrayString(values: List<T>): String
}
