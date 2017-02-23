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
package com.gantsign.restrulz.spring.mvc

import io.reactivex.Single
import org.junit.Test
import kotlin.test.assertEquals

class SingleDeferredResultTest {

    @Suppress("UNCHECKED_CAST")
    @Test
    fun testOnSuccess() {
        val value = Any()
        val single = Single.just(value)
        val deferredResult = SingleDeferredResult(single = single)

        assertEquals(expected = value, actual = deferredResult.result)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun testOnError() {
        val error = RuntimeException()
        val single = Single.error<Any>(error)
        val deferredResult = SingleDeferredResult(single = single)

        assertEquals(expected = error, actual = deferredResult.result)
    }

}
