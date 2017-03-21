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

import com.gantsign.restrulz.json.JsonObjectMapper
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.AbstractGenericHttpMessageConverter
import java.lang.reflect.Type

class RestrulzMessageConverter(
        private val objectMapper: JsonObjectMapper = JsonObjectMapper(),
        vararg supportedMediaTypes: MediaType = arrayOf(MediaType.APPLICATION_JSON_UTF8)) :
        AbstractGenericHttpMessageConverter<Any>(*supportedMediaTypes) {

    override fun canRead(clazz: Class<*>, mediaType: MediaType?): Boolean {
        return canRead(clazz, null, mediaType)
    }

    override fun canRead(type: Type, contextClass: Class<*>?, mediaType: MediaType?): Boolean {
        if (!canRead(mediaType)) {
            return false
        }
        return objectMapper.isSupportedForReading(type)
    }

    override fun canWrite(clazz: Class<*>, mediaType: MediaType?): Boolean {
        return canWrite(null, clazz, mediaType)
    }

    override fun canWrite(type: Type?, clazz: Class<*>, mediaType: MediaType?): Boolean {
        if (!canWrite(mediaType)) {
            return false
        }
        return this.objectMapper.isSupportedForWriting(type ?: clazz)
    }

    override fun supports(clazz: Class<*>): Boolean {
        throw UnsupportedOperationException("Use canWrite() / canWrite()")
    }

    override fun read(type: Type, contextClass: Class<*>?, inputMessage: HttpInputMessage): Any {
        return objectMapper.read(type, inputMessage.body)
    }

    override fun readInternal(clazz: Class<*>, inputMessage: HttpInputMessage): Any {
        return read(clazz, null, inputMessage)
    }

    override fun writeInternal(value: Any, type: Type?, outputMessage: HttpOutputMessage) {
        objectMapper.write(value, type, outputMessage.body)
    }
}
