package pt.isel.ls.courts.unit.database.sections.rental

import pt.isel.ls.courts.unit.database.AppMemoryDBTests
import pt.isel.ls.storage.dtosStorage.rental.RentalInsert
import pt.isel.ls.storage.dtosStorage.user.UserInsert
import java.time.LocalDateTime
import kotlin.test.*

class RentalsMemoryDBTests : AppMemoryDBTests(), RentalDBTests {

    private fun setupTestEntities(): Triple<Int, Int, Int> {
        val user = db.userStorage.createUser(
            UserInsert(
                name = "RentalUser",
                email = "rental+${System.currentTimeMillis()}@isel.pt",
                password = "passwordRental"
            )
        )
        val club = db.clubStorage.createClub("Rental Club", user.id)
        val court = db.courtStorage.createCourt("Rental Court", club.id)
        return Triple(user.id, club.id, court.id)
    }

    @Test
    override fun `create and fetch rental`() {
        val (userId, clubId, courtId) = setupTestEntities()
        val date = LocalDateTime.of(2099, 8, 1, 10, 0)

        val rental = db.rentalStorage.createRental(
            RentalInsert(
                clubId = clubId,
                courtId = courtId,
                date = date,
                duration = 2
            ),
            userId
        )

        val fetched = db.rentalStorage.getRentalDetails(rental.id)
        assertNotNull(fetched)
        assertEquals(clubId, fetched.clubId)
        assertEquals(courtId, fetched.courtId)
        assertEquals(userId, fetched.user)

        db.rentalStorage.deleteRentalById(rental.id)
        db.courtStorage.deleteCourtById(courtId)
        db.clubStorage.deleteClubById(clubId)
        db.userStorage.deleteUserById(userId)
    }

    @Test
    override fun `getRentalDetails returns null for invalid id`() {
        val result = db.rentalStorage.getRentalDetails(99999)
        assertNull(result)
    }

    @Test
    override fun `getUserRentals returns rentals for user`() {
        val (userId, clubId, courtId) = setupTestEntities()
        val date = LocalDateTime.of(2099, 8, 2, 14, 0)

        val rental = db.rentalStorage.createRental(
            RentalInsert(
                clubId = clubId,
                courtId = courtId,
                date = date,
                duration = 1
            ),
            userId
        )

        val rentals = db.rentalStorage.getUserRentals(userId)
        assertTrue(rentals.isNotEmpty())
        assertEquals(userId, rentals.first().user)

        db.rentalStorage.deleteRentalById(rental.id)
        db.courtStorage.deleteCourtById(courtId)
        db.clubStorage.deleteClubById(clubId)
        db.userStorage.deleteUserById(userId)
    }
}
