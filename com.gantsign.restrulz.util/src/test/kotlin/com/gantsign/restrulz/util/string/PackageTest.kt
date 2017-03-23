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
    fun testStringBlankToEmptyWithNonBlank() {
        val expected = "a"
        val actual = expected.blankToEmpty()

        assertSame(expected, actual)
    }

    @Test
    fun testStringBlankToEmptyWithBlank() {
        val expected = ""
        val actual = " ".blankToEmpty()

        assertEquals(expected, actual)
    }

    @Test
    fun testStringBlankToEmptyWithNull() {
        val expected = ""
        val actual = (null as String?).blankToEmpty()

        assertEquals(expected, actual)
    }

    @Test
    fun testListBlankToEmptyWithNonBlank() {
        val expected = listOf("a")
        val actual = expected.blankToEmpty()

        assertEquals(expected, actual)
    }

    @Test
    fun testListBlankToEmptyWithBlank() {
        val expected = listOf("")
        val actual = listOf(" ").blankToEmpty()

        assertEquals(expected, actual)
    }
}
