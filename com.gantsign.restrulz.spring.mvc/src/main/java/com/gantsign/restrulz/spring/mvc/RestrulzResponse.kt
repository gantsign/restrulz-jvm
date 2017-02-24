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

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * Base class for code REST API responses.
 *
 * @constructor Constructs a response instance with the specified response details.
 * @param statusCode the HTTP status code for this response.
 * @param headers the HTTP response headers.
 * @param body the body of the HTTP response.
 */
abstract class RestrulzResponse(val statusCode: HttpStatus,
                                val headers: HttpHeaders,
                                val body: Any?) : ResponseEntityConvertible<Any?> {

    override fun toResponseEntity(): ResponseEntity<Any?> {
        return ResponseEntity(body, headers, statusCode)
    }
}
