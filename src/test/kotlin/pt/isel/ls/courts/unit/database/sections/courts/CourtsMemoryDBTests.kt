package pt.isel.ls.courts.unit.database.sections.courts

import pt.isel.ls.courts.unit.database.AppMemoryDBTests
import pt.isel.ls.storage.dtosStorage.user.UserInsert
import kotlin.test.*

class CourtsMemoryDBTests : AppMemoryDBTests(), CourtsDBTests {

    private fun createTestClubAndUser(): Triple<Int, Int, Int> {
        val user = db.userStorage.createUser(
            UserInsert(
                name = "ClubOwner",
                email = "club+${System.currentTimeMillis()}@isel.pt",
                password = "passwordClubOwner"
            )
        )
        val club = db.clubStorage.createClub("TestClub", user.id)
        return Triple(club.id, user.id, user.id)
    }

    @Test
    override fun `create and retrieve court`() {
        val (clubId, _, userId) = createTestClubAndUser()
        val court = db.courtStorage.createCourt("MyCourt", clubId)
        val fetched = db.courtStorage.getCourtDetails(court.id)

        assertNotNull(fetched)
        assertEquals("MyCourt", fetched.name)
        assertEquals(clubId, fetched.clubId)

        db.courtStorage.deleteCourtById(court.id)
        db.clubStorage.deleteClubById(clubId)
        db.userStorage.deleteUserById(userId)
    }

    @Test
    override fun `getCourtDetails returns null for invalid id`() {
        val fetched = db.courtStorage.getCourtDetails(9999)
        assertNull(fetched)
    }

    @Test
    override fun `getCourts returns list of courts for a club`() {
        val (clubId, _, userId) = createTestClubAndUser()
        val court1 = db.courtStorage.createCourt("Court 1", clubId)
        val court2 = db.courtStorage.createCourt("Court 2", clubId)

        val courts = db.courtStorage.getCourts(clubId)

        assertTrue(courts.any { it.id == court1.id })
        assertTrue(courts.any { it.id == court2.id })

        db.courtStorage.deleteCourtById(court1.id)
        db.courtStorage.deleteCourtById(court2.id)
        db.clubStorage.deleteClubById(clubId)
        db.userStorage.deleteUserById(userId)
    }

    @Test
    override fun `deleteCourtById removes court`() {
        val (clubId, _, userId) = createTestClubAndUser()
        val court = db.courtStorage.createCourt("DeleteMe", clubId)

        db.courtStorage.deleteCourtById(court.id)
        val deleted = db.courtStorage.getCourtDetails(court.id)

        assertNull(deleted)

        db.clubStorage.deleteClubById(clubId)
        db.userStorage.deleteUserById(userId)
    }
}
