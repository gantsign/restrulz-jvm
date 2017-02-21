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

/**
 * Base class for validating string values.
 *
 * @constructor creates an instance of the string validator with the specified constraints.
 * @param minimumLength the minimum permitted length.
 * @param maximumLength the maximum permitted length.
 * @param pattern a regular expression to validate values against.
 * @throws IllegalArgumentException if minimumLength < 1 or minimumLength > maximumLength.
 * @throws java.util.regex.PatternSyntaxException if the regular expression syntax is invalid.
 */
@Suppress("unused")
abstract class StringValidator protected constructor(private val minimumLength: Int,
                                                     private val maximumLength: Int,
                                                     pattern: String) {

    private val regex: Regex = Regex(pattern)

    init {
        if (minimumLength < 1) {
            throw IllegalArgumentException("Parameter 'minimumLength' ($minimumLength) must be greater than or equal to 1")
        }
        if (minimumLength > maximumLength) {
            throw IllegalArgumentException("Parameter 'minimumLength' ($minimumLength) must be less than or equal to parameter 'maximumLength' ($maximumLength)")
        }
    }

    /**
     * Checks that the specified parameter value is valid.
     *
     * @param parameterName the name of the parameter being validated.
     * @param value the value to validate.
     * @return the value if valid.
     * @throws InvalidArgumentException if the value is invalid.
     */
    fun requireValidValue(parameterName: String, value: String): String {
        if (value.length < minimumLength) {
            throw InvalidArgumentException(parameterName, "'$value' is shorter than the minimum permitted length of $minimumLength")
        }
        if (value.length > maximumLength) {
            throw InvalidArgumentException(parameterName, "'$value' is longer than the maximum permitted length of $maximumLength")
        }
        if (!value.matches(regex)) {
            throw InvalidArgumentException(parameterName, "'$value' does not match pattern '${regex.pattern}'")
        }
        return value
    }

    /**
     * Checks that the specified parameter value is valid or empty string.
     *
     * @param parameterName the name of the parameter being validated.
     * @param value the value to validate.
     * @return the value if valid or empty string.
     * @throws InvalidArgumentException if the value is non-empty and invalid.
     */
    fun requireValidValueOrEmpty(parameterName: String, value: String): String {
        if (value.isEmpty()) {
            return value
        }
        return requireValidValue(parameterName, value)
    }

    /**
     * Checks that the specified parameter value is valid.
     *
     * @param value the value to validate.
     * @param validationHandler the handler for validation failures.
     * @return `true` if the value is valid.
     */
    fun validateValue(value: String, validationHandler: ValidationHandler): Boolean {
        var valid = true

        if (value.length < minimumLength) {
            valid = false
            validationHandler.handleValidationFailure("'$value' is shorter than the minimum permitted length of $minimumLength")
        } else if (value.length > maximumLength) {
            valid = false
            validationHandler.handleValidationFailure("'$value' is longer than the maximum permitted length of $maximumLength")
        }

        if (!value.matches(regex)) {
            valid = false
            validationHandler.handleValidationFailure("'$value' does not match pattern '${regex.pattern}'")
        }
        return valid
    }

    /**
     * Checks that the specified parameter value is valid or empty-string.
     *
     * @param value the value to validate.
     * @param validationHandler the handler for validation failures.
     * @return `true` if the value is valid or empty-string.
     */
    fun validateValueOrEmpty(value: String, validationHandler: ValidationHandler): Boolean {
        if (value.isEmpty()) {
            return true
        }
        return validateValue(value, validationHandler)
    }

}
