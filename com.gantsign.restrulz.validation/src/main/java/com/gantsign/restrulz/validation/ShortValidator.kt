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
 * Base class for validating short values.
 *
 * @constructor creates an instance of the short validator with the specified constraints.
 * @param minimumValue the minimum permitted value.
 * @param maximumValue the maximum permitted value.
 * @throws IllegalArgumentException if minimumValue > maximumValue.
 */
@Suppress("unused")
abstract class ShortValidator protected constructor(private val minimumValue: Short,
                                                    private val maximumValue: Short) {

    init {
        if (minimumValue > maximumValue) {
            throw IllegalArgumentException("Parameter 'minimumValue' ($minimumValue) must be less than or equal to parameter 'maximumValue' ($maximumValue)")
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
    fun requireValidValue(parameterName: String, value: Short): Short {
        if (value < minimumValue) {
            throw InvalidArgumentException(parameterName, "$value is less than the minimum permitted value of $minimumValue")
        }
        if (value > maximumValue) {
            throw InvalidArgumentException(parameterName, "$value is greater than the maximum permitted value of $maximumValue")
        }
        return value
    }

    /**
     * Checks that the specified parameter value is valid or null.
     *
     * @param parameterName the name of the parameter being validated.
     * @param value the value to validate.
     * @return the value if valid or null.
     * @throws InvalidArgumentException if the value is non-null and invalid.
     */
    fun requireValidValueOrNull(parameterName: String, value: Short?): Short? {
        if (value === null) {
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
    fun validateValue(value: Short, validationHandler: ValidationHandler): Boolean {
        if (value < minimumValue) {
            validationHandler.handleValidationFailure("$value is less than the minimum permitted value of $minimumValue")
            return false
        }
        if (value > maximumValue) {
            validationHandler.handleValidationFailure("$value is greater than the maximum permitted value of $maximumValue")
            return false
        }

        return true
    }

    /**
     * Checks that the specified parameter value is valid or null.
     *
     * @param value the value to validate.
     * @param validationHandler the handler for validation failures.
     * @return `true` if the value is valid or null.
     */
    fun validateValueOrNull(value: Short?, validationHandler: ValidationHandler): Boolean {
        if (value === null) {
            return true
        }
        return validateValue(value, validationHandler)
    }

}
