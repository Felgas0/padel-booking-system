package pt.isel.ls.courts.unit.database.sections.users

import pt.isel.ls.courts.unit.database.AppMemoryDBTests
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.User
import pt.isel.ls.storage.dtosStorage.user.UserInsert
import org.springframework.security.crypto.bcrypt.BCrypt
import kotlin.test.*

class UsersMemoryDBTests : AppMemoryDBTests(), UsersDBTests {

    @Test
    override fun `create and retrieve a user`() {
        val newUser = UserInsert("John", "john@isel.pt", "passwordJohn")
        val created = db.userStorage.createUser(newUser)

        val fetched = db.userStorage.getUserDetails(created.id)
        assertNotNull(fetched)
        assertEquals("John", fetched.name)
        assertEquals("john@isel.pt", fetched.email.value)

        db.userStorage.deleteUserById(created.id)
    }

    @Test
    override fun `getUserById returns null if not found`() {
        val user = db.userStorage.getUserDetails(999)
        assertNull(user)
    }

    @Test
    override fun `isTaken returns true for existing email`() {
        val user = UserInsert("Alice", "alice@isel.pt", "passwordAlice")
        val created = db.userStorage.createUser(user)
        val result = db.userStorage.isTaken("alice@isel.pt")
        assertTrue(result)

        db.userStorage.deleteUserById(created.id)
    }

    @Test
    override fun `insertUser with known values`() {
        val user = User(
            50,
            "Test User",
            Email("test@isel.pt"),
            BCrypt.hashpw("passwordTest", BCrypt.gensalt()),
            java.util.UUID.randomUUID(),
        )
        db.userStorage.insertUser(user)
        val fetched = db.userStorage.getUserDetails(50)
        assertEquals("test@isel.pt", fetched?.email?.value)

        db.userStorage.deleteUserById(50)
    }

    @Test
    override fun `password is stored as a BCrypt hash`() {
        val plainPassword = "securePass123"
        val user = UserInsert("HashUser", "hash@isel.pt", plainPassword)
        val created = db.userStorage.createUser(user)

        val stored = db.userStorage.getUserDetails(created.id)
        assertNotNull(stored)

        assertNotEquals(plainPassword, stored.passwordHash, "Password should not be stored in plain text")

        assertTrue(
            BCrypt.checkpw(plainPassword, stored.passwordHash),
            "BCrypt hash must match the original password"
        )

        db.userStorage.deleteUserById(created.id)
    }
}
