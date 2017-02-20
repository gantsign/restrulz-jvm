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
package com.gantsign.restrulz.json.reader

import java.io.InputStream

/**
 * Interface for reading objects from JSON serialized form.
 *
 * @param T the type of the object to read.
 */
interface JsonObjectReader<out T> {

    /**
     * Reads a single object.
     *
     * @param input the stream to read the object from.
     */
    fun readObject(input: InputStream): T

    /**
     * Reads an array of objects.
     *
     * @param input the stream to read the array of objects from.
     */
    fun readArray(input: InputStream): List<T>
}
