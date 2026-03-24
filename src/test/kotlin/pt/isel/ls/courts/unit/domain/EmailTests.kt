package pt.isel.ls.courts.unit.domain

import pt.isel.ls.domain.Email
import kotlin.test.Test
import kotlin.test.assertFailsWith

class EmailTests {
    @Test
    fun `creating Email with valid format does not throw`() {
        Email("test@example.com")
        Email("john.doe@isel.pt")
        Email("a@b.co") // minimum valid format
    }

    @Test
    fun `creating Email with missing at symbol throws`() {
        assertFailsWith<IllegalArgumentException> {
            Email("invalidemail.com")
        }
    }

    @Test
    fun `creating Email with missing domain throws`() {
        assertFailsWith<IllegalArgumentException> {
            Email("user@")
        }
    }

    @Test
    fun `creating Email with missing username throws`() {
        assertFailsWith<IllegalArgumentException> {
            Email("@example.com")
        }
    }

    @Test
    fun `creating Email with invalid characters throws`() {
        assertFailsWith<IllegalArgumentException> {
            Email("user@exa mple.com")
        }
    }
}
