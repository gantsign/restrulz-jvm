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
package com.gantsign.restrulz.validation

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ByteValidatorTest {

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    @Test
    fun testNewByteValidatorInvalidArgs() {
        thrown.expect(IllegalArgumentException::class.java)
        thrown.expectMessage("Parameter 'minimumValue' (41) must be less than or equal to parameter 'maximumValue' (40)")

        TestByteValidator(minimumValue = 41, maximumValue = 40)
    }

    @Test
    fun testRequireValidValue() {
        val testValidator = TestByteValidator(minimumValue = 20, maximumValue = 40)
        assertEquals(expected = 20, actual = testValidator.requireValidValue("test1", 20))
        assertEquals(expected = 21, actual = testValidator.requireValidValue("test1", 21))
        assertEquals(expected = 40, actual = testValidator.requireValidValue("test1", 40))
    }

    @Test
    fun testRequireValidValueTooLow() {
        thrown.expect(InvalidArgumentException::class.java)
        thrown.expectMessage("19 is less than the minimum permitted value of 20")

        val testValidator = TestByteValidator(minimumValue = 20, maximumValue = 40)
        testValidator.requireValidValue("test1", 19)
    }

    @Test
    fun testRequireValidValueTooHigh() {
        thrown.expect(InvalidArgumentException::class.java)
        thrown.expectMessage("41 is greater than the maximum permitted value of 40")

        val testValidator = TestByteValidator(minimumValue = 20, maximumValue = 40)
        testValidator.requireValidValue("test1", 41)
    }

    @Test
    fun testRequireValidValueOrNull() {
        val testValidator = TestByteValidator(minimumValue = 20, maximumValue = 40)
        assertEquals(expected = 20, actual = testValidator.requireValidValueOrNull("test1", 20))
        assertEquals(expected = 21, actual = testValidator.requireValidValueOrNull("test1", 21))
        assertEquals(expected = 40, actual = testValidator.requireValidValueOrNull("test1", 40))
        assertNull(testValidator.requireValidValueOrNull("test1", null))
    }

    @Test
    fun testRequireValidValueOrNullTooLow() {
        thrown.expect(InvalidArgumentException::class.java)
        thrown.expectMessage("19 is less than the minimum permitted value of 20")

        val testValidator = TestByteValidator(minimumValue = 20, maximumValue = 40)
        testValidator.requireValidValueOrNull("test1", 19)
    }

    @Test
    fun testRequireValidValueOrNullTooHigh() {
        thrown.expect(InvalidArgumentException::class.java)
        thrown.expectMessage("41 is greater than the maximum permitted value of 40")

        val testValidator = TestByteValidator(minimumValue = 20, maximumValue = 40)
        testValidator.requireValidValueOrNull("test1", 41)
    }

    @Test
    fun testValidateValue() {
        val testValidator = TestByteValidator(minimumValue = 20, maximumValue = 40)
        val validationHandler = mock(ValidationHandler::class.java)

        assertTrue(testValidator.validateValue(20, validationHandler))
        assertTrue(testValidator.validateValue(21, validationHandler))
        assertTrue(testValidator.validateValue(40, validationHandler))

        verifyZeroInteractions(validationHandler)
    }

    @Test
    fun testValidateValueTooLow() {
        val testValidator = TestByteValidator(minimumValue = 20, maximumValue = 40)
        val validationHandler = mock(ValidationHandler::class.java)

        assertFalse(testValidator.validateValue(19, validationHandler))

        verify(validationHandler).handleValidationFailure("19 is less than the minimum permitted value of 20")
    }

    @Test
    fun testValidateValueTooHigh() {
        val testValidator = TestByteValidator(minimumValue = 20, maximumValue = 40)
        val validationHandler = mock(ValidationHandler::class.java)

        assertFalse(testValidator.validateValue(41, validationHandler))

        verify(validationHandler).handleValidationFailure("41 is greater than the maximum permitted value of 40")
    }

    @Test
    fun testValidateValueOrNull() {
        val testValidator = TestByteValidator(minimumValue = 20, maximumValue = 40)
        val validationHandler = mock(ValidationHandler::class.java)

        assertTrue(testValidator.validateValueOrNull(20, validationHandler))
        assertTrue(testValidator.validateValueOrNull(21, validationHandler))
        assertTrue(testValidator.validateValueOrNull(40, validationHandler))
        assertTrue(testValidator.validateValueOrNull(null, validationHandler))

        verifyZeroInteractions(validationHandler)
    }

    @Test
    fun testValidateValueOrNullTooLow() {
        val testValidator = TestByteValidator(minimumValue = 20, maximumValue = 40)
        val validationHandler = mock(ValidationHandler::class.java)

        assertFalse(testValidator.validateValueOrNull(19, validationHandler))

        verify(validationHandler).handleValidationFailure("19 is less than the minimum permitted value of 20")
    }

    @Test
    fun testValidateValueOrNullTooHigh() {
        val testValidator = TestByteValidator(minimumValue = 20, maximumValue = 40)
        val validationHandler = mock(ValidationHandler::class.java)

        assertFalse(testValidator.validateValueOrNull(41, validationHandler))

        verify(validationHandler).handleValidationFailure("41 is greater than the maximum permitted value of 40")
    }
}
