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
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Suppress("unused")
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(Single::class)
open class RestrulzAutoConfiguration {

    @Configuration
    @ConditionalOnClass(*arrayOf(AsyncHandlerMethodReturnValueHandler::class, WebMvcConfigurerAdapter::class))
    open class SingleReturnValueHandlerConfig {
        @Bean
        open fun singleReturnValueHandler(): SingleReturnValueHandler {
            return SingleReturnValueHandler()
        }

        @Bean
        open fun singleMvcConfiguration(singleReturnValueHandler: SingleReturnValueHandler): WebMvcConfigurerAdapter {
            return object : WebMvcConfigurerAdapter() {

                override fun addReturnValueHandlers(returnValueHandlers: MutableList<HandlerMethodReturnValueHandler>) {
                    returnValueHandlers.add(singleReturnValueHandler)
                }
            }
        }
    }
}
