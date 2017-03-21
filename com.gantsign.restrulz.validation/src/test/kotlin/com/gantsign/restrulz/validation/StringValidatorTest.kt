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
import java.util.regex.PatternSyntaxException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StringValidatorTest {

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    @Test
    fun testNewStringValidatorInvalidMinimumLength() {
        thrown.expect(IllegalArgumentException::class.java)
        thrown.expectMessage("Parameter 'minimumLength' (0) must be greater than or equal to 1")

        TestStringValidator(minimumLength = 0, maximumLength = 5, pattern = "[a-z]+")
    }

    @Test
    fun testNewStringValidatorInvalidMaximumLength() {
        thrown.expect(IllegalArgumentException::class.java)
        thrown.expectMessage("Parameter 'minimumLength' (6) must be less than or equal to parameter 'maximumLength' (5)")

        TestStringValidator(minimumLength = 6, maximumLength = 5, pattern = "[a-z]+")
    }

    @Test
    fun testNewStringValidatorInvalidPattern() {
        thrown.expect(PatternSyntaxException::class.java)
        thrown.expectMessage("Unknown character property name {DoesNotExist}")

        TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "\\p{DoesNotExist}+")
    }

    @Test
    fun testRequireValidValue() {
        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        assertEquals(expected = "ab", actual = testValidator.requireValidValue("test1", "ab"))
        assertEquals(expected = "abc", actual = testValidator.requireValidValue("test1", "abc"))
        assertEquals(expected = "abcde", actual = testValidator.requireValidValue("test1", "abcde"))
    }

    @Test
    fun testRequireValidValueTooShort() {
        thrown.expect(InvalidArgumentException::class.java)
        thrown.expectMessage("'a' is shorter than the minimum permitted length of 2")

        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        testValidator.requireValidValue("test1", "a")
    }

    @Test
    fun testRequireValidValueTooLong() {
        thrown.expect(InvalidArgumentException::class.java)
        thrown.expectMessage("'abcdef' is longer than the maximum permitted length of 5")

        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        testValidator.requireValidValue("test1", "abcdef")
    }

    @Test
    fun testRequireValidValueNotMatched() {
        thrown.expect(InvalidArgumentException::class.java)
        thrown.expectMessage("'12' does not match pattern '[a-z]+'")

        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        testValidator.requireValidValue("test1", "12")
    }

    @Test
    fun testRequireValidValueOrEmpty() {
        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        assertEquals(expected = "ab", actual = testValidator.requireValidValueOrEmpty("test1", "ab"))
        assertEquals(expected = "abc", actual = testValidator.requireValidValueOrEmpty("test1", "abc"))
        assertEquals(expected = "abcde", actual = testValidator.requireValidValueOrEmpty("test1", "abcde"))
        assertEquals(expected = "", actual = testValidator.requireValidValueOrEmpty("test1", ""))
    }

    @Test
    fun testRequireValidValueOrEmptyTooShort() {
        thrown.expect(InvalidArgumentException::class.java)
        thrown.expectMessage("'a' is shorter than the minimum permitted length of 2")

        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        testValidator.requireValidValueOrEmpty("test1", "a")
    }

    @Test
    fun testRequireValidValueOrEmptyTooLong() {
        thrown.expect(InvalidArgumentException::class.java)
        thrown.expectMessage("'abcdef' is longer than the maximum permitted length of 5")

        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        testValidator.requireValidValueOrEmpty("test1", "abcdef")
    }

    @Test
    fun testRequireValidValueOrEmptyNotMatched() {
        thrown.expect(InvalidArgumentException::class.java)
        thrown.expectMessage("'12' does not match pattern '[a-z]+'")

        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        testValidator.requireValidValueOrEmpty("test1", "12")
    }

    @Test
    fun testValidateValue() {
        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        val validationHandler = mock(ValidationHandler::class.java)

        assertTrue(testValidator.validateValue("ab", validationHandler))
        assertTrue(testValidator.validateValue("abc", validationHandler))
        assertTrue(testValidator.validateValue("abcde", validationHandler))

        verifyZeroInteractions(validationHandler)
    }

    @Test
    fun testValidateValueTooShort() {
        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        val validationHandler = mock(ValidationHandler::class.java)

        assertFalse(testValidator.validateValue("a", validationHandler))

        verify(validationHandler).handleValidationFailure("'a' is shorter than the minimum permitted length of 2")
    }

    @Test
    fun testValidateValueTooLong() {
        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        val validationHandler = mock(ValidationHandler::class.java)

        assertFalse(testValidator.validateValue("abcdef", validationHandler))

        verify(validationHandler).handleValidationFailure("'abcdef' is longer than the maximum permitted length of 5")
    }

    @Test
    fun testValidateValueNotMatched() {
        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        val validationHandler = mock(ValidationHandler::class.java)

        assertFalse(testValidator.validateValue("12", validationHandler))

        verify(validationHandler).handleValidationFailure("'12' does not match pattern '[a-z]+'")
    }

    @Test
    fun testValidateValueOrEmpty() {
        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        val validationHandler = mock(ValidationHandler::class.java)

        assertTrue(testValidator.validateValue("ab", validationHandler))
        assertTrue(testValidator.validateValue("abc", validationHandler))
        assertTrue(testValidator.validateValue("abcde", validationHandler))
        assertTrue(testValidator.validateValueOrEmpty("", validationHandler))

        verifyZeroInteractions(validationHandler)
    }

    @Test
    fun testValidateValueOrEmptyTooShort() {
        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        val validationHandler = mock(ValidationHandler::class.java)

        assertFalse(testValidator.validateValueOrEmpty("a", validationHandler))

        verify(validationHandler).handleValidationFailure("'a' is shorter than the minimum permitted length of 2")
    }

    @Test
    fun testValidateValueOrEmptyTooLong() {
        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        val validationHandler = mock(ValidationHandler::class.java)

        assertFalse(testValidator.validateValueOrEmpty("abcdef", validationHandler))

        verify(validationHandler).handleValidationFailure("'abcdef' is longer than the maximum permitted length of 5")
    }

    @Test
    fun testValidateValueOrEmptyNotMatched() {
        val testValidator = TestStringValidator(minimumLength = 2, maximumLength = 5, pattern = "[a-z]+")
        val validationHandler = mock(ValidationHandler::class.java)

        assertFalse(testValidator.validateValueOrEmpty("12", validationHandler))

        verify(validationHandler).handleValidationFailure("'12' does not match pattern '[a-z]+'")
    }
}
