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

import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@WebMvcTest(TestApi::class)
class SingleReturnValueHandlerMvcTest {

    @SpringBootConfiguration
    @ComponentScan
    open class TestConfig

    @Autowired
    lateinit var testApi: TestApi

    @Autowired
    lateinit var context: WebApplicationContext

    private lateinit var mvc: MockMvc

    @Before
    fun setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun testHandleReturnValueSingleWithResponseEntity() {
        val mvcResult = mvc.perform(get("/singleWithResponseEntity"))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(notNullValue()))
                .andReturn()

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isPartialContent)
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").value(testApi.singleWithResponseEntityBody))
    }

    @Test
    fun testHandleReturnValueSingleWithStringResponse() {
        val mvcResult = mvc.perform(get("/singleWithStringResponse"))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(notNullValue()))
                .andReturn()

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isPartialContent)
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").value(testApi.singleWithStringResponseBody))
    }

    @Test
    fun testHandleReturnValueSingleWithTestEntityResponse() {
        val request = post("/singleWithTestEntityResponse")
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"id\":\"${testApi.singleWithTestEntityResponseBody}\"}")

        val mvcResult = mvc.perform(request)
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(notNullValue()))
                .andReturn()

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isPartialContent)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(testApi.singleWithTestEntityResponseBody))
    }

    @Test
    fun testHandleReturnValueSingleWithTestEntityListResponse() {
        val request = post("/singleWithTestEntityListResponse")
                .contentType(APPLICATION_JSON_UTF8)
                .content("[{\"id\":\"${testApi.singleWithTestEntityListResponseBody}\"},{\"id\":\"${testApi.singleWithTestEntityListResponseBody2}\"}]")

        val mvcResult = mvc.perform(request)
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(notNullValue()))
                .andReturn()

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isPartialContent)
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(testApi.singleWithTestEntityListResponseBody))
                .andExpect(jsonPath("$[1].id").value(testApi.singleWithTestEntityListResponseBody2))
    }

    @Test
    fun testHandleReturnValueSingleWithAny() {
        val mvcResult = mvc.perform(get("/singleWithAny"))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(notNullValue()))
                .andReturn()

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk)
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(jsonPath("$").value(testApi.singleWithAnyBody))
    }

    @Test
    fun testHandleReturnValueResponseEntityWithSingle() {
        val mvcResult = mvc.perform(get("/responseEntityWithSingle"))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(notNullValue()))
                .andReturn()

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isPartialContent)
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").value(testApi.responseEntityWithSingleBody))
    }

    @Test
    fun testHandleReturnValueTestEntity() {
        val request = post("/testEntity")
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"id\":\"${testApi.testEntityBody}\"}")
        mvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(testApi.testEntityBody))
    }

    @Test
    fun testHandleReturnValueListOfTestEntity() {
        val request = post("/listOfTestEntity")
                .contentType(APPLICATION_JSON_UTF8)
                .content("[{\"id\":\"${testApi.listOfTestEntityBody}\"},{\"id\":\"${testApi.listOfTestEntityBody2}\"}]")
        mvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id").value(testApi.listOfTestEntityBody))
                .andExpect(jsonPath("$[1].id").value(testApi.listOfTestEntityBody2))
    }
}
