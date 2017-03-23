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
package com.gantsign.restrulz.util.string

/**
 * Returns this String if the string is non-null and non-blank otherwise returns empty string.
 */
fun String?.blankToEmpty(): String {
    return if (this === null || this.isBlank()) "" else this
}

/**
 * Returns a new list where all the items in this list are mapped with [blankToEmpty].
 */
fun List<String>.blankToEmpty(): List<String> {
    return this.map(String::blankToEmpty)
}
