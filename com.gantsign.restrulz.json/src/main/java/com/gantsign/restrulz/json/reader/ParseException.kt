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

/**
 * Exception thrown when one or more errors have occurred during parsing.
 *
 * @constructor Creates a new instance of this exception based on the specified errors.
 * @param errors the errors that occurred during parsing.
 */
class ParseException(errors: List<String>) : RuntimeException(formatErrorMessage(errors)) {

    companion object {
        private fun formatErrorMessage(errors: List<String>): String {
            return errors.joinToString(
                    prefix = "The following error(s) were found in the content being parsed:\n\t",
                    separator = "\n\t"
            )
        }
    }
}
