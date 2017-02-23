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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST
import org.springframework.web.context.request.async.DeferredResult
import org.springframework.web.context.request.async.WebAsyncManager
import org.springframework.web.context.request.async.WebAsyncUtils.WEB_ASYNC_MANAGER_ATTRIBUTE
import org.springframework.web.method.support.ModelAndViewContainer
import kotlin.test.assertEquals


class SingleReturnValueHandlerTest {

    private fun getMethodParameter(methodName: String): MethodParameter {
        return MethodParameter(TestApi::class.java.getMethod(methodName), -1)
    }

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    private val testValue = "test1"

    private lateinit var headers: HttpHeaders

    private lateinit var singleWithResponseEntity: Single<ResponseEntity<*>>
    private lateinit var singleWithRestrulzResponse: Single<RestrulzResponse>
    private lateinit var singleWithAny: Single<Any>
    private lateinit var responseEntityWithSingle: ResponseEntity<Single<String>>
    private val responseEntityWithString = ResponseEntity(testValue, HttpStatus.PARTIAL_CONTENT)
    private val string = testValue

    private var singleWithResponseEntityMp = getMethodParameter("singleWithResponseEntity")
    private val singleWithRestrulzResponseMp = getMethodParameter("singleWithRestrulzResponse")
    private val singleWithAnyMp = getMethodParameter("singleWithAny")
    private val responseEntityWithSingleMp = getMethodParameter("responseEntityWithSingle")
    private val responseEntityWithStringMp = getMethodParameter("responseEntityWithString")
    private val stringMp = getMethodParameter("string")

    @Captor
    private lateinit var deferredResultCaptor: ArgumentCaptor<DeferredResult<*>>

    @Captor
    private lateinit var processingContextCaptor: ArgumentCaptor<Any>

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        singleWithResponseEntity = Single.just(ResponseEntity(
                testValue, headers, HttpStatus.PARTIAL_CONTENT))
        singleWithRestrulzResponse = Single.just(object : RestrulzResponse(
                HttpStatus.PARTIAL_CONTENT, headers, testValue) {})
        singleWithAny = Single.just(testValue)
        responseEntityWithSingle = ResponseEntity(
                Single.just(testValue), headers, HttpStatus.PARTIAL_CONTENT)
    }

    @Test
    fun testSupportsReturnType() {
        val returnValueHandler = SingleReturnValueHandler()
        assertTrue(returnValueHandler.supportsReturnType(singleWithRestrulzResponseMp))
        assertTrue(returnValueHandler.supportsReturnType(singleWithAnyMp))
        assertTrue(returnValueHandler.supportsReturnType(responseEntityWithSingleMp))

        assertFalse(returnValueHandler.supportsReturnType(responseEntityWithStringMp))
        assertFalse(returnValueHandler.supportsReturnType(stringMp))
    }

    @Test
    fun testIsAsyncReturnValue() {
        val returnValueHandler = SingleReturnValueHandler()
        assertTrue(returnValueHandler.isAsyncReturnValue(singleWithRestrulzResponse, singleWithRestrulzResponseMp))
        assertTrue(returnValueHandler.isAsyncReturnValue(singleWithAny, singleWithAnyMp))
        assertTrue(returnValueHandler.isAsyncReturnValue(responseEntityWithSingle, responseEntityWithSingleMp))

        assertFalse(returnValueHandler.isAsyncReturnValue(responseEntityWithString, responseEntityWithStringMp))
        assertFalse(returnValueHandler.isAsyncReturnValue(string, stringMp))
        assertFalse(returnValueHandler.isAsyncReturnValue(null, stringMp))
    }

    @Test
    fun testHandleReturnValueSingleWithResponseEntity() {
        val returnValueHandler = SingleReturnValueHandler()
        val mavContainer = mock(ModelAndViewContainer::class.java)
        val webRequest = mock(NativeWebRequest::class.java)
        val webAsyncManager = mock(WebAsyncManager::class.java)
        `when`(webRequest.getAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, SCOPE_REQUEST))
                .thenReturn(webAsyncManager)

        returnValueHandler.handleReturnValue(
                singleWithResponseEntity,
                singleWithResponseEntityMp,
                mavContainer,
                webRequest)

        verify(webAsyncManager).startDeferredResultProcessing(
                deferredResultCaptor.capture(),
                processingContextCaptor.capture())

        val deferredResult = deferredResultCaptor.value
        val responseEntity = deferredResult.result as ResponseEntity<*>

        assertEquals(expected = HttpStatus.PARTIAL_CONTENT, actual = responseEntity.statusCode)
        assertEquals(expected = headers, actual = responseEntity.headers)
        assertEquals(expected = testValue, actual = responseEntity.body)

        assertEquals(expected = mavContainer, actual = processingContextCaptor.value)
    }

    @Test
    fun testHandleReturnValueSingleWithRestrulzResponse() {
        val returnValueHandler = SingleReturnValueHandler()
        val mavContainer = mock(ModelAndViewContainer::class.java)
        val webRequest = mock(NativeWebRequest::class.java)
        val webAsyncManager = mock(WebAsyncManager::class.java)
        `when`(webRequest.getAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, SCOPE_REQUEST))
                .thenReturn(webAsyncManager)

        returnValueHandler.handleReturnValue(
                singleWithRestrulzResponse,
                singleWithRestrulzResponseMp,
                mavContainer,
                webRequest)

        verify(webAsyncManager).startDeferredResultProcessing(
                deferredResultCaptor.capture(),
                processingContextCaptor.capture())

        val deferredResult = deferredResultCaptor.value
        val responseEntity = deferredResult.result as ResponseEntity<*>

        assertEquals(expected = HttpStatus.PARTIAL_CONTENT, actual = responseEntity.statusCode)
        assertEquals(expected = headers, actual = responseEntity.headers)
        assertEquals(expected = testValue, actual = responseEntity.body)

        assertEquals(expected = mavContainer, actual = processingContextCaptor.value)
    }

    @Test
    fun testHandleReturnValueSingleWithAny() {
        val returnValueHandler = SingleReturnValueHandler()
        val mavContainer = mock(ModelAndViewContainer::class.java)
        val webRequest = mock(NativeWebRequest::class.java)
        val webAsyncManager = mock(WebAsyncManager::class.java)
        `when`(webRequest.getAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, SCOPE_REQUEST))
                .thenReturn(webAsyncManager)

        returnValueHandler.handleReturnValue(
                singleWithAny,
                singleWithAnyMp,
                mavContainer,
                webRequest)

        verify(webAsyncManager).startDeferredResultProcessing(
                deferredResultCaptor.capture(),
                processingContextCaptor.capture())

        val deferredResult = deferredResultCaptor.value
        val responseEntity = deferredResult.result as ResponseEntity<*>

        assertEquals(expected = HttpStatus.OK, actual = responseEntity.statusCode)
        assertEquals(expected = HttpHeaders(), actual = responseEntity.headers)
        assertEquals(expected = testValue, actual = responseEntity.body)

        assertEquals(expected = mavContainer, actual = processingContextCaptor.value)
    }

    @Test
    fun testHandleReturnValueResponseEntityWithSingle() {
        val returnValueHandler = SingleReturnValueHandler()
        val mavContainer = mock(ModelAndViewContainer::class.java)
        val webRequest = mock(NativeWebRequest::class.java)
        val webAsyncManager = mock(WebAsyncManager::class.java)
        `when`(webRequest.getAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, SCOPE_REQUEST))
                .thenReturn(webAsyncManager)

        returnValueHandler.handleReturnValue(
                responseEntityWithSingle,
                responseEntityWithSingleMp,
                mavContainer,
                webRequest)

        verify(webAsyncManager).startDeferredResultProcessing(
                deferredResultCaptor.capture(),
                processingContextCaptor.capture())

        val deferredResult = deferredResultCaptor.value
        val responseEntity = deferredResult.result as ResponseEntity<*>

        assertEquals(expected = HttpStatus.PARTIAL_CONTENT, actual = responseEntity.statusCode)
        assertEquals(expected = headers, actual = responseEntity.headers)
        assertEquals(expected = testValue, actual = responseEntity.body)

        assertEquals(expected = mavContainer, actual = processingContextCaptor.value)
    }

    @Test
    fun testHandleReturnValueNull() {
        val returnValueHandler = SingleReturnValueHandler()
        val mavContainer = mock(ModelAndViewContainer::class.java)
        val webRequest = mock(NativeWebRequest::class.java)
        val webAsyncManager = mock(WebAsyncManager::class.java)
        `when`(webRequest.getAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, SCOPE_REQUEST))
                .thenReturn(webAsyncManager)

        returnValueHandler.handleReturnValue(
                null,
                stringMp,
                mavContainer,
                webRequest)

        verify(mavContainer).isRequestHandled = true
    }

    @Test
    fun testHandleReturnValueResponseEntityWithNull() {
        val returnValueHandler = SingleReturnValueHandler()
        val mavContainer = mock(ModelAndViewContainer::class.java)
        val webRequest = mock(NativeWebRequest::class.java)
        val webAsyncManager = mock(WebAsyncManager::class.java)
        `when`(webRequest.getAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, SCOPE_REQUEST))
                .thenReturn(webAsyncManager)

        val responseEntityWithNull = ResponseEntity<String>(null, HttpStatus.PARTIAL_CONTENT)

        returnValueHandler.handleReturnValue(
                responseEntityWithNull,
                responseEntityWithSingleMp,
                mavContainer,
                webRequest)

        verify(mavContainer).isRequestHandled = true
    }

    @Test
    fun testHandleReturnValueUnexpectedtype() {
        val returnValueHandler = SingleReturnValueHandler()
        val mavContainer = mock(ModelAndViewContainer::class.java)
        val webRequest = mock(NativeWebRequest::class.java)
        val webAsyncManager = mock(WebAsyncManager::class.java)
        `when`(webRequest.getAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, SCOPE_REQUEST))
                .thenReturn(webAsyncManager)

        thrown.expect(AssertionError::class.java)
        thrown.expectMessage("Expected io.reactivex.Single or org.springframework.http.ResponseEntity but was java.lang.String")

        returnValueHandler.handleReturnValue(
                "unexpected1",
                stringMp,
                mavContainer,
                webRequest)
    }

    @Test
    fun testHandleReturnValueResponseEntityWithUnexpectedType() {
        val returnValueHandler = SingleReturnValueHandler()
        val mavContainer = mock(ModelAndViewContainer::class.java)
        val webRequest = mock(NativeWebRequest::class.java)
        val webAsyncManager = mock(WebAsyncManager::class.java)
        `when`(webRequest.getAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, SCOPE_REQUEST))
                .thenReturn(webAsyncManager)

        val responseEntityWithUnexpectedType = ResponseEntity<String>("unexpected2", HttpStatus.PARTIAL_CONTENT)

        thrown.expect(AssertionError::class.java)
        thrown.expectMessage("Expected io.reactivex.Single but was java.lang.String")

        returnValueHandler.handleReturnValue(
                responseEntityWithUnexpectedType,
                responseEntityWithSingleMp,
                mavContainer,
                webRequest)

        verify(mavContainer).isRequestHandled = true
    }
}
