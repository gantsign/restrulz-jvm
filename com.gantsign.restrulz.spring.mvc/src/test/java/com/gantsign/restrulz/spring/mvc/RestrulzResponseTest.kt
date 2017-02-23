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

import org.junit.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import kotlin.test.assertEquals

class RestrulzResponseTest {

    @Test
    fun testNewInstance() {
        val status = HttpStatus.PARTIAL_CONTENT
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val body = Any()

        val response = object : RestrulzResponse(status, headers, body) {}

        assertEquals(expected = status, actual = response.statusCode)
        assertEquals(expected = headers, actual = response.headers)
        assertEquals(expected = body, actual = response.body)
    }

    @Test
    fun testToResponseEntity() {
        val status = HttpStatus.PARTIAL_CONTENT
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val body = Any()

        val response = object : RestrulzResponse(status, headers, body) {}
        val responseEntity = response.toResponseEntity()

        assertEquals(expected = status, actual = responseEntity.statusCode)
        assertEquals(expected = headers, actual = responseEntity.headers)
        assertEquals(expected = body, actual = responseEntity.body)
    }

}
