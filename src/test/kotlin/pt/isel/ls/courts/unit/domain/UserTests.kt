package pt.isel.ls.courts.unit.domain

import pt.isel.ls.domain.Email
import pt.isel.ls.domain.User
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertFailsWith

class UserTests {
    @Test
    fun `creating user with valid data does not throw`() {
        User(
            id = 1,
            name = "Antonio",
            email = Email("a@isel.pt"),
            passwordHash = "someHashedPassword",
            token = UUID.randomUUID(),
        )
    }

    @Test
    fun `creating user with id less than or equal to zero throws`() {
        assertFailsWith<IllegalArgumentException> {
            User(
                id = 0,
                name = "Antonio",
                email = Email("a@isel.pt"),
                passwordHash = "someHashedPassword",
                token = UUID.randomUUID(),
            )
        }
    }

    @Test
    fun `creating user with blank name throws`() {
        assertFailsWith<IllegalArgumentException> {
            User(
                id = 1,
                name = "   ",
                email = Email("a@isel.pt"),
                passwordHash = "someHashedPassword",
                token = UUID.randomUUID(),
            )
        }
    }

    @Test
    fun `creating user with blank password hash throws`() {
        assertFailsWith<IllegalArgumentException> {
            User(
                id = 1,
                name = "Antonio",
                email = Email("a@isel.pt"),
                passwordHash = "   ",
                token = UUID.randomUUID(),
            )
        }
    }
}
