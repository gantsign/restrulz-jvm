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

import com.gantsign.restrulz.spring.mvc.model.TestEntity
import com.gantsign.restrulz.spring.mvc.responses.StringResponse
import com.gantsign.restrulz.spring.mvc.responses.TestEntityListResponse
import com.gantsign.restrulz.spring.mvc.responses.TestEntityResponse
import io.reactivex.Single
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
@Component
open class TestApi {
    val testValue = "test1"

    val headers = HttpHeaders()

    val singleWithResponseEntityBody = "singleWithResponseEntity"
    val singleWithStringResponseBody = "singleWithStringResponse"
    val singleWithTestEntityResponseBody = "singleWithTestEntityResponse"
    val singleWithTestEntityListResponseBody
            = "singleWithTestEntityListResponse"
    val singleWithTestEntityListResponseBody2
            = "singleWithTestEntityListResponse2"
    val singleWithAnyBody = "singleWithAny"
    val responseEntityWithSingleBody = "responseEntityWithSingle"
    val responseEntityWithStringBody = "responseEntityWithString"
    val stringBody = "string"
    val testEntityBody = "testEntity"
    val listOfTestEntityBody = "listOfTestEntity"
    val listOfTestEntityBody2 = "listOfTestEntity2"

    val singleWithResponseEntity: Single<ResponseEntity<*>>
    val singleWithStringResponse: Single<StringResponse>
    val singleWithAny: Single<Any>
    val responseEntityWithSingle: ResponseEntity<Single<String>>
    val responseEntityWithString = ResponseEntity(
            responseEntityWithStringBody, HttpStatus.PARTIAL_CONTENT)
    val string = stringBody

    init {
        headers.contentType = MediaType.APPLICATION_JSON

        singleWithResponseEntity = Single.just(ResponseEntity(
                singleWithResponseEntityBody, headers, HttpStatus.PARTIAL_CONTENT))
        singleWithStringResponse = Single.just(
                StringResponse.partialContent(singleWithStringResponseBody))
        singleWithAny = Single.just(singleWithAnyBody)
        responseEntityWithSingle = ResponseEntity(
                Single.just(responseEntityWithSingleBody), headers, HttpStatus.PARTIAL_CONTENT)
    }

    @RequestMapping(value = "/singleWithResponseEntity", method = arrayOf(RequestMethod.GET))
    fun singleWithResponseEntity(): Single<ResponseEntity<*>> {
        return singleWithResponseEntity
    }

    @RequestMapping(value = "/singleWithStringResponse", method = arrayOf(RequestMethod.GET))
    fun singleWithStringResponse(): Single<StringResponse> {
        return singleWithStringResponse
    }

    @RequestMapping(value = "/singleWithTestEntityResponse", method = arrayOf(RequestMethod.POST))
    fun singleWithTestEntityResponse(
            @RequestBody testEntity: TestEntity): Single<TestEntityResponse> {
        return Single.just(TestEntityResponse.partialContent(testEntity))
    }

    @RequestMapping(value = "/singleWithTestEntityListResponse", method = arrayOf(RequestMethod.POST))
    fun singleWithTestEntityListResponse(
            @RequestBody testEntities: List<TestEntity>): Single<TestEntityListResponse> {
        return Single.just(TestEntityListResponse.partialContent(testEntities))
    }

    @RequestMapping(value = "/singleWithAny", method = arrayOf(RequestMethod.GET))
    fun singleWithAny(): Single<Any> {
        return singleWithAny
    }

    @RequestMapping(value = "/responseEntityWithSingle", method = arrayOf(RequestMethod.GET))
    fun responseEntityWithSingle(): ResponseEntity<Single<String>> {
        return responseEntityWithSingle
    }

    @RequestMapping(value = "/responseEntityWithString", method = arrayOf(RequestMethod.GET))
    fun responseEntityWithString(): ResponseEntity<String> {
        return responseEntityWithString
    }

    @RequestMapping(value = "/string", method = arrayOf(RequestMethod.GET))
    fun string(): String {
        return string
    }

    @RequestMapping(value = "/testEntity", method = arrayOf(RequestMethod.POST))
    fun testEntity(@RequestBody testEntity: TestEntity): TestEntity {
        return testEntity
    }

    @RequestMapping(value = "/listOfTestEntity", method = arrayOf(RequestMethod.POST))
    fun listOfTestEntity(@RequestBody testEntities: List<TestEntity>): List<TestEntity> {
        return testEntities
    }
}
