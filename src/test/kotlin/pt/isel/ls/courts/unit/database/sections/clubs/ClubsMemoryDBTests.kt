package pt.isel.ls.courts.unit.database.sections.clubs

import pt.isel.ls.storage.dtosStorage.user.UserInsert
import pt.isel.ls.courts.unit.database.AppMemoryDBTests
import kotlin.test.*

class ClubsMemoryDBTests : AppMemoryDBTests(), ClubsDBTests {

    @Test
    override fun `create and retrieve club`() {
        val user = db.userStorage.createUser(
            UserInsert(
                name = "TestUser",
                email = "user+${System.currentTimeMillis()}@isel.pt",
                password = "passwordTestUser"
            )
        )
        val club = db.clubStorage.createClub("Test Club", user.id)
        val retrieved = db.clubStorage.getClub(club.id)

        assertNotNull(retrieved)
        assertEquals("Test Club", retrieved.name)
        assertEquals(user.id, retrieved.ownerId)

        db.clubStorage.deleteClubById(club.id)
        db.userStorage.deleteUserById(user.id)
    }

    @Test
    override fun `getClub returns null for non-existent id`() {
        val club = db.clubStorage.getClub(9999)
        assertNull(club)
    }

    @Test
    override fun `getClubs returns all clubs`() {
        val user = db.userStorage.createUser(
            UserInsert(
                name = "MultiClubUser",
                email = "multi+${System.currentTimeMillis()}@isel.pt",
                password = "passwordMulti"
            )
        )
        val c1 = db.clubStorage.createClub("Club One", user.id)
        val c2 = db.clubStorage.createClub("Club Two", user.id)

        val allClubs = db.clubStorage.getClubs()
        assertTrue(allClubs.any { it.name == "Club One" })
        assertTrue(allClubs.any { it.name == "Club Two" })

        db.clubStorage.deleteClubById(c1.id)
        db.clubStorage.deleteClubById(c2.id)
        db.userStorage.deleteUserById(user.id)
    }

    @Test
    override fun `add and remove court to club`() {
        val user = db.userStorage.createUser(
            UserInsert(
                name = "CourtOwner",
                email = "court+${System.currentTimeMillis()}@isel.pt",
                password = "passwordCourt"
            )
        )
        val club = db.clubStorage.createClub("New Club", user.id)
        val court = db.courtStorage.createCourt("Court X", club.id)

        db.clubStorage.addCourtToClub(club, court)

        val courtsAfterAdd = db.courtStorage.getCourts(club.id)
        assertTrue(courtsAfterAdd.any { it.id == court.id })

        db.courtStorage.deleteCourtById(court.id)

        val courtsAfterDelete = db.courtStorage.getCourts(club.id)
        assertTrue(courtsAfterDelete.none { it.id == court.id })

        db.clubStorage.deleteClubById(club.id)
        db.userStorage.deleteUserById(user.id)
    }

    @Test
    override fun `searchByName returns clubs containing fragment`() {
        val user = db.userStorage.createUser(
            UserInsert(
                name = "Searcher",
                email = "search+${System.currentTimeMillis()}@isel.pt",
                password = "passwordSearcher"
            )
        )

        db.clubStorage.createClub("Padel Alpha", user.id)
        db.clubStorage.createClub("Beta Club", user.id)
        db.clubStorage.createClub("PadZone", user.id)

        val result = db.clubStorage.searchByName("Pad")

        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "Padel Alpha" })
        assertTrue(result.any { it.name == "PadZone" })
    }
}
