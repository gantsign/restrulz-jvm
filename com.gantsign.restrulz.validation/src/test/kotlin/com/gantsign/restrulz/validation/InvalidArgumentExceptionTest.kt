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
package com.gantsign.restrulz.validation

import org.junit.Test
import kotlin.test.assertEquals

class InvalidArgumentExceptionTest {

    @Test
    fun testNewInstance() {
        val exception = InvalidArgumentException(parameterName = "test1", message = "test2")

        assertEquals(
                expected = "Invalid argument for parameter test1: test2",
                actual = exception.message)
    }
}
