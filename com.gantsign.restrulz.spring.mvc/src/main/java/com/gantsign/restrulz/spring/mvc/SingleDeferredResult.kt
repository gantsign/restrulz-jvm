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
import org.springframework.web.context.request.async.DeferredResult

internal class SingleDeferredResult<T>(timeout: Long? = null,
                                       timeoutResult: Any = EMPTY_RESULT,
                                       single: Single<T>) :
        DeferredResult<T>(timeout, timeoutResult) {

    @Suppress("unused")
    private val observer = DeferredResultSingleObserver(single, this)

    companion object {
        @Suppress("unused")
        private val EMPTY_RESULT = Any()
    }
}
