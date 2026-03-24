package pt.isel.ls.courts.unit.services.sections.rentals

import pt.isel.ls.API.dtosAPI.rental.RentalCreate
import pt.isel.ls.courts.unit.services.AbstractServicesTests
import pt.isel.ls.domain.*
import pt.isel.ls.utils.*
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.*
import org.springframework.security.crypto.bcrypt.BCrypt

class RentalsServicesTests : AbstractServicesTests() {

    private val userId  = 1
    private val clubId  = 1
    private val courtId = 1
    private val token   = UUID.fromString("00000000-0000-0000-0000-000000000001")

    @BeforeTest
    fun seed() {
        storage.userStorage.insertUser(
            User(
                id = userId,
                name = "Luis",
                email = Email("luis@isel.pt"),
                passwordHash = BCrypt.hashpw("passwordLuis", BCrypt.gensalt()),
                token = token
            )
        )

        val club  = Club(clubId, "Padel Kings", userId, mutableListOf())
        storage.clubStorage.insertClub(club)

        val court = Court(courtId, "Center Court", clubId)
        storage.courtStorage.insertCourt(court)
        club.courts += court
    }

    @Test
    fun `create rental with valid data returns rentalId`() {
        val dto  = RentalCreate(clubId, courtId, LocalDateTime.now().plusDays(1).withHour(10), 2)
        val resp = services.rentalsServices.createRental(dto, token)

        assertTrue(resp.rentalId > 0)
    }

    @Test
    fun `create rental with non-existent court throws NotFoundException`() {
        val dto = RentalCreate(clubId, 999, LocalDateTime.now().plusDays(1).withHour(10), 1)

        val ex = assertFailsWith<NotFoundException> {
            services.rentalsServices.createRental(dto, token)
        }
        assertTrue(ex.message!!.contains("court", ignoreCase = true))
    }

    @Test
    fun `create rental with past date throws BadRequestException`() {
        val dto = RentalCreate(clubId, courtId, LocalDateTime.now().minusDays(1), 1)

        val ex = assertFailsWith<BadRequestException> {
            services.rentalsServices.createRental(dto, token)
        }
        assertTrue(ex.message!!.contains("future", ignoreCase = true))
    }

    @Test
    fun `creating overlapping rental returns ConflictException`() {
        val start = LocalDateTime.now().plusDays(1).withHour(10)
        services.rentalsServices.createRental(RentalCreate(clubId, courtId, start, 2), token)

        val overlap = RentalCreate(clubId, courtId, start.withHour(11), 2)

        val ex = assertFailsWith<ConflictException> {
            services.rentalsServices.createRental(overlap, token)
        }
        assertTrue(ex.message!!.contains("available", ignoreCase = true))
    }

    @Test
    fun `get available hours includes open slots`() {
        val date = LocalDateTime.now().plusDays(1).toLocalDate().toString()

        val hours = services.rentalsServices.getAvailableHours(clubId, courtId, date)
        val tenAM = hours.find { it["hour"] == "10:00" }

        assertEquals("OPEN", tenAM?.get("status"))
    }

    @Test
    fun `get rental by id returns expected data`() {
        val dto      = RentalCreate(clubId, courtId, LocalDateTime.now().plusDays(1).withHour(15), 1)
        val created  = services.rentalsServices.createRental(dto, token)

        val result = services.rentalsServices.getRental(created.rentalId)

        assertEquals(dto.date.hour, result.date.hour)
        assertEquals(dto.duration , result.duration)
    }

    @Test
    fun `get rental with invalid id throws NotFoundException`() {
        val ex = assertFailsWith<NotFoundException> {
            services.rentalsServices.getRental(999)
        }
        assertTrue(ex.message!!.contains("rental", ignoreCase = true))
    }

    @Test
    fun `get rentals for user returns all user rentals`() {
        services.rentalsServices.createRental(
            RentalCreate(clubId, courtId, LocalDateTime.now().plusDays(1).withHour(10), 1), token
        )
        services.rentalsServices.createRental(
            RentalCreate(clubId, courtId, LocalDateTime.now().plusDays(1).withHour(15), 1), token
        )

        val page = services.rentalsServices.getUserRentals(userId, skip = 0, limit = 10)

        assertEquals(2, page.totalCount)
        assertEquals(2, page.items.size)
    }
}
