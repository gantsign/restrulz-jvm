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
package com.gantsign.restrulz.spring.mvc.responses

import com.gantsign.restrulz.spring.mvc.ResponseEntityConvertible
import com.gantsign.restrulz.spring.mvc.model.TestEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

class TestEntityResponse private constructor(
        private val responseEntity: ResponseEntity<Any?>) : ResponseEntityConvertible<Any?> {

    override fun toResponseEntity(): ResponseEntity<Any?> {
        return responseEntity
    }

    companion object {
        fun partialContent(value: TestEntity): TestEntityResponse {
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON_UTF8

            return TestEntityResponse(
                    ResponseEntity<Any?>(value, headers, HttpStatus.PARTIAL_CONTENT))
        }
    }
}
