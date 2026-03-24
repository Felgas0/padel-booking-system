package pt.isel.ls.courts.unit.database.sections.users

import pt.isel.ls.storage.Storage
import kotlin.test.Test

interface UsersDBTests {
    val db: Storage

    @Test
    fun `create and retrieve a user`()

    @Test
    fun `getUserById returns null if not found`()

    @Test
    fun `isTaken returns true for existing email`()

    @Test
    fun `insertUser with known values`()

    @Test
    fun `password is stored as a BCrypt hash`()

}
