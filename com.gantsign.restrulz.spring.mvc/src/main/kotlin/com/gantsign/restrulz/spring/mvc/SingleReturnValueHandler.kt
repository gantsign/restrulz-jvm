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
import org.springframework.core.MethodParameter
import org.springframework.core.ResolvableType
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.async.DeferredResult
import org.springframework.web.context.request.async.WebAsyncUtils
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * A [AsyncHandlerMethodReturnValueHandler] implementation for [Single].
 */
class SingleReturnValueHandler : AsyncHandlerMethodReturnValueHandler {

    private fun isSingle(returnType: MethodParameter): Boolean {
        return Single::class.java.isAssignableFrom(returnType.parameterType)
    }

    private fun isSingleInResponseEntity(returnType: MethodParameter): Boolean {
        if (ResponseEntity::class.java.isAssignableFrom(returnType.parameterType)) {
            val resolvableType = ResolvableType.forMethodParameter(returnType)
            val bodyType = resolvableType.getGeneric(0).resolve()
            return bodyType != null && Single::class.java.isAssignableFrom(bodyType)
        }
        return false
    }

    override fun supportsReturnType(returnType: MethodParameter): Boolean {
        return isSingle(returnType) || isSingleInResponseEntity(returnType)
    }

    override fun isAsyncReturnValue(returnValue: Any?, returnType: MethodParameter): Boolean {
        return returnValue != null && supportsReturnType(returnType)
    }

    @Throws(Exception::class)
    override fun handleReturnValue(returnValue: Any?,
                                   returnType: MethodParameter,
                                   mavContainer: ModelAndViewContainer,
                                   webRequest: NativeWebRequest) {

        // returnValue is either Single<ResponseEntity, Single<ResponseEntityConvertible>,
        // Single<*>, ResponseEntity<Single<*>> or null

        if (returnValue === null) {
            mavContainer.isRequestHandled = true
            return
        }

        val single: Single<*>
        val responseEntity: ResponseEntity<Single<*>>?
        if (returnValue is Single<*>) {
            single = returnValue

            responseEntity = null
        } else {
            if (returnValue !is ResponseEntity<*>) {
                throw AssertionError("Expected ${Single::class.java.name} or ${ResponseEntity::class.java.name} but was ${returnValue::class.java.name}")
            }
            val body = returnValue.body
            if (body === null) {
                mavContainer.isRequestHandled = true
                return
            }
            if (body !is Single<*>) {
                throw AssertionError("Expected ${Single::class.java.name} but was ${body::class.java.name}")
            }

            single = body

            @Suppress("UNCHECKED_CAST")
            responseEntity = returnValue as ResponseEntity<Single<*>>?
        }

        val asyncManager = WebAsyncUtils.getAsyncManager(webRequest)
        val deferredResult = convertToDeferredResult(responseEntity, single)
        asyncManager.startDeferredResultProcessing(deferredResult, mavContainer)
    }

    private fun ResponseEntity<*>?.copy(body: Any): ResponseEntity<*> {

        val statusCode: HttpStatus
        val headers: HttpHeaders
        if (this !== null) {
            statusCode = this.statusCode
            headers = this.headers
        } else {
            statusCode = HttpStatus.OK
            headers = HttpHeaders()
        }
        return ResponseEntity(body, headers, statusCode)
    }

    private fun convertToDeferredResult(responseEntity: ResponseEntity<Single<*>>?,
                                        single: Single<*>): DeferredResult<*> {

        val singleResponse = single.map { returnValue ->
            @Suppress("IfThenToElvis")
            if (returnValue is ResponseEntity<*>) {
                returnValue
            } else if (returnValue is ResponseEntityConvertible<*>) {
                returnValue.toResponseEntity()
            } else {
                responseEntity.copy(body = returnValue)
            }
        }

        return SingleDeferredResult(single = singleResponse)
    }
}
