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
package com.gantsign.restrulz.util.string

import org.junit.Assert.assertSame
import org.junit.Test
import kotlin.test.assertEquals

class PackageTest {

    @Test
    fun testStringBlankOrNullToEmptyWithNonBlank() {
        val expected = "a"
        val actual = expected.blankOrNullToEmpty()

        assertSame(expected, actual)
    }

    @Test
    fun testStringBlankOrNullToEmptyWithBlank() {
        val expected = ""
        val actual = " ".blankOrNullToEmpty()

        assertEquals(expected, actual)
    }

    @Test
    fun testStringBlankOrNullToEmptyWithNull() {
        val expected = ""
        val actual = (null as String?).blankOrNullToEmpty()

        assertEquals(expected, actual)
    }

    @Test
    fun testListBlankOrNullToEmptyWithNonBlank() {
        val expected = listOf("a")
        val actual = expected.blankOrNullToEmpty()

        assertEquals(expected, actual)
    }

    @Test
    fun testListBlankOrNullToEmptyWithBlank() {
        val expected = listOf("")
        val actual = listOf(" ").blankOrNullToEmpty()

        assertEquals(expected, actual)
    }
}
