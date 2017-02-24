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
import io.reactivex.observers.DisposableSingleObserver
import org.springframework.web.context.request.async.DeferredResult

/**
 * Single observer that passes the result/error of the [Single] to the [DeferredResult].
 *
 * @constructor Creates a new instance of this observer to connecting the specified [Single] to the
 * specified [DeferredResult].
 * @param single the source of the result/error.
 * @param deferredResult the receiver for the result/error.
 * @param T type of the result from the [Single].
 */
internal class DeferredResultSingleObserver<T>(single: Single<T>,
                                               private val deferredResult: DeferredResult<T>) :
        DisposableSingleObserver<T>(), Runnable {

    init {
        this.deferredResult.onTimeout(this)
        this.deferredResult.onCompletion(this)
        single.subscribe(this)
    }

    override fun onSuccess(value: T) {
        deferredResult.setResult(value)
    }

    override fun onError(error: Throwable) {
        deferredResult.setErrorResult(error)
    }

    override fun run() {
        this.dispose()
    }
}
