package pt.isel.ls.courts.unit.services.sections.users

import pt.isel.ls.API.dtosAPI.user.UserCreate
import pt.isel.ls.courts.unit.services.AbstractServicesTests
import pt.isel.ls.utils.*
import kotlin.test.*

class UsersServicesTests : AbstractServicesTests() {

    @Test
    fun `createUser with valid data returns correct response`() {
        val body   = UserCreate("Alice", "alice@isel.pt", "passwordAlice")
        val result = services.usersServices.createUser(body)

        assertNotNull(result.token)
        assertTrue(result.userId > 0)
    }

    @Test
    fun `createUser with blank email throws BadRequestException`() {
        val ex = assertFailsWith<BadRequestException> {
            services.usersServices.createUser(UserCreate("Blank", "", "pass123"))
        }
        assertTrue(ex.message!!.contains("email", ignoreCase = true))
    }

    @Test
    fun `createUser with duplicate email throws ConflictException`() {
        val body = UserCreate("Bob", "bob@isel.pt", "passwordBob")
        services.usersServices.createUser(body)

        val ex = assertFailsWith<ConflictException> {
            services.usersServices.createUser(body)
        }
        assertTrue(ex.message!!.contains("exists", ignoreCase = true))
    }

    @Test
    fun `getUser with valid ID returns correct details`() {
        val created = services.usersServices.createUser(
            UserCreate("Charlie", "charlie@isel.pt", "passwordCharlie")
        )

        val details = services.usersServices.getUser(created.userId)

        assertEquals("Charlie", details.name)
        assertEquals("charlie@isel.pt", details.email.value)
    }

    @Test
    fun `getUser with invalid ID throws NotFoundException`() {
        val ex = assertFailsWith<NotFoundException> {
            services.usersServices.getUser(999)
        }
        assertTrue(ex.message!!.contains("user", ignoreCase = true))
    }

    @Test
    fun `getAllUsers returns all created users`() {
        services.usersServices.createUser(UserCreate("Luis",  "luis@isel.pt",  "passwordLuis"))
        services.usersServices.createUser(UserCreate("Joana", "joana@isel.pt", "passwordJoana"))

        val page = services.usersServices.getAllUsers(skip = 0, limit = 10)

        assertEquals(2, page.totalCount)
        assertEquals(2, page.items.size)
        assertTrue(page.items.any { it.name == "Luis" })
        assertTrue(page.items.any { it.email.value == "joana@isel.pt" })
    }

    @Test
    fun `createUser with password too short throws BadRequestException`() {
        val ex = assertFailsWith<BadRequestException> {
            services.usersServices.createUser(UserCreate("Tiny", "tiny@isel.pt", "123"))
        }
        assertTrue(ex.message!!.contains("Password must have between", ignoreCase = true))
    }

    @Test
    fun `createUser with password too long throws BadRequestException`() {
        val longPassword = "a".repeat(26)
        val ex = assertFailsWith<BadRequestException> {
            services.usersServices.createUser(UserCreate("Verbose", "verbose@isel.pt", longPassword))
        }
        assertTrue(ex.message!!.contains("Password must have between", ignoreCase = true))
    }

    @Test
    fun `createUser with password length 5 and 25 should succeed`() {
        val min = services.usersServices.createUser(UserCreate("Min", "min@isel.pt", "12345"))
        val max = services.usersServices.createUser(UserCreate("Max", "max@isel.pt", "a".repeat(25)))

        assertTrue(min.userId > 0)
        assertTrue(max.userId > 0)
    }



}
