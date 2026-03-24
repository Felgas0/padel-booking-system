package pt.isel.ls.domain

import kotlinx.serialization.Serializable

/**
 * Represents an email address with validation.
 *
 * @property value the email address as a string
 *
 * @throws IllegalArgumentException if the email format is invalid
 */
@Serializable
data class Email(val value: String) {
    init {
        require(value.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"))) {
            "Invalid email format: $value"
        }
    }
}
