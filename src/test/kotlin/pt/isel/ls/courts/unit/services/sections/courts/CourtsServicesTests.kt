package pt.isel.ls.courts.unit.services.sections.courts

import pt.isel.ls.API.dtosAPI.court.CourtCreate
import pt.isel.ls.courts.unit.services.AbstractServicesTests
import pt.isel.ls.domain.*
import pt.isel.ls.utils.*
import java.util.*
import kotlin.test.*
import org.springframework.security.crypto.bcrypt.BCrypt

class CourtsServicesTests : AbstractServicesTests() {

    private val userId  = 1
    private val clubId  = 1
    private val token   = UUID.fromString("00000000-0000-0000-0000-000000000001")

    @BeforeTest
    fun setupTestData() {
        storage.userStorage.insertUser(
            User(
                id = userId,
                name = "António",
                email = Email("antonio@isel.pt"),
                passwordHash = BCrypt.hashpw("passwordAntonio", BCrypt.gensalt()),
                token = token
            )
        )
        storage.clubStorage.insertClub(Club(clubId, "ISEL Club", userId, mutableListOf()))
    }

    @Test
    fun `create court with valid data returns CourtResponse`() {
        val out = services.courtsServices.createCourt(
            CourtCreate("ISEL's new logo SUCKS", clubId), token
        )
        assertTrue(out.courtId > 0)
    }

    @Test
    fun `create court with blank name throws BadRequestException`() {
        val ex = assertFailsWith<BadRequestException> {
            services.courtsServices.createCourt(CourtCreate("", clubId), token)
        }
        assertTrue(ex.message!!.contains("name", ignoreCase = true))
    }

    @Test
    fun `create court with non-existent club throws NotFoundException`() {
        val ex = assertFailsWith<NotFoundException> {
            services.courtsServices.createCourt(CourtCreate("Ghost Court", 999), token)
        }
        assertTrue(ex.message!!.contains("club", ignoreCase = true))
    }

    @Test
    fun `create court with wrong user throws ForbiddenException`() {
        val strangerToken = UUID.fromString("11111111-1111-1111-1111-111111111111")
        storage.userStorage.insertUser(
            User(
                id = 2,
                name = "Stranger",
                email = Email("stranger@isel.pt"),
                passwordHash = BCrypt.hashpw("passwordStranger", BCrypt.gensalt()),
                token = strangerToken
            )
        )

        val ex = assertFailsWith<ForbiddenException> {
            services.courtsServices.createCourt(CourtCreate("Locked Court", clubId), strangerToken)
        }
        assertTrue(ex.message!!.contains("Only", ignoreCase = true))
    }

    @Test
    fun `get court details returns correct data`() {
        val created = services.courtsServices.createCourt(CourtCreate("Detail Court", clubId), token)
        val court   = services.courtsServices.getCourtDetails(created.courtId)

        assertEquals("Detail Court", court.name)
        assertEquals(clubId, court.clubId)
    }

    @Test
    fun `get courts for a club returns all its courts`() {
        services.courtsServices.createCourt(CourtCreate("Court A", clubId), token)
        services.courtsServices.createCourt(CourtCreate("Court B", clubId), token)

        val page = services.courtsServices.getCourts(clubId, skip = 0, limit = 10)

        assertEquals(2, page.totalCount)
        assertEquals(2, page.items.size)
        assertTrue(page.items.any { it.name == "Court A" })
        assertTrue(page.items.any { it.name == "Court B" })
    }

    @Test
    fun `get court details for non-existent court throws NotFoundException`() {
        val ex = assertFailsWith<NotFoundException> {
            services.courtsServices.getCourtDetails(999)
        }
        assertTrue(ex.message!!.contains("court", ignoreCase = true))
    }
}
