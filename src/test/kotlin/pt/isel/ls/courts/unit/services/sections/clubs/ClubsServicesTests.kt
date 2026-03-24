package pt.isel.ls.courts.unit.services.sections.clubs

import pt.isel.ls.API.dtosAPI.club.ClubCreate
import pt.isel.ls.courts.unit.services.AbstractServicesTests
import pt.isel.ls.domain.*
import pt.isel.ls.utils.*
import java.util.*
import kotlin.test.*
import org.springframework.security.crypto.bcrypt.BCrypt

class ClubsServicesTests : AbstractServicesTests() {

    private val token = UUID.fromString("00000000-0000-0000-0000-000000000001")

    @BeforeTest
    fun insertMockUser() {
        storage.userStorage.insertUser(
            User(
                id = 1,
                name = "António Pimentel",
                email = Email("antonio@isel.pt"),
                passwordHash = BCrypt.hashpw("passwordAntonio", BCrypt.gensalt()),
                token = token
            )
        )
    }

    @Test
    fun `createClub with valid data returns clubId`() {
        val out = services.clubsServices.createClub(ClubCreate("Padel Avengers"), token)
        assertTrue(out.clubId > 0)
    }

    @Test
    fun `createClub with blank name throws BadRequestException`() {
        val ex = assertFailsWith<BadRequestException> {
            services.clubsServices.createClub(ClubCreate(""), token)
        }
        assertTrue(ex.message!!.contains("name", ignoreCase = true))
    }

    @Test
    fun `createClub with duplicate name throws ConflictException`() {
        val dto = ClubCreate("Padel Club")
        services.clubsServices.createClub(dto, token)

        val ex = assertFailsWith<ConflictException> {
            services.clubsServices.createClub(dto, token)
        }
        assertTrue(ex.message!!.contains("exists", ignoreCase = true))
    }

    @Test
    fun `createClub with non-existent token throws UnauthorizedException`() {
        val ex = assertFailsWith<UnauthorizedException> {
            services.clubsServices.createClub(
                ClubCreate("Ghost Club"),
                UUID.fromString("99999999-0000-0000-0000-000000000999")
            )
        }
        assertTrue(ex.message!!.contains("Missing", ignoreCase = true))
    }

    @Test
    fun `getClub with valid ID returns correct club`() {
        val created = services.clubsServices.createClub(ClubCreate("Elite Padel"), token)
        val club    = services.clubsServices.getClub(created.clubId)

        assertEquals("Elite Padel", club.name)
        assertEquals(1, club.ownerId)
    }

    @Test
    fun `getClub with invalid ID throws NotFoundException`() {
        val ex = assertFailsWith<NotFoundException> {
            services.clubsServices.getClub(999)
        }
        assertTrue(ex.message!!.contains("club", ignoreCase = true))
    }

    @Test
    fun `getClubs returns all created clubs`() {
        services.clubsServices.createClub(ClubCreate("A Club"), token)
        services.clubsServices.createClub(ClubCreate("B Club"), token)

        val page = services.clubsServices.getClubs(skip = 0, limit = 10)

        assertEquals(2, page.totalCount)
        assertEquals(2, page.items.size)
        assertTrue(page.items.any { it.name == "A Club" })
        assertTrue(page.items.any { it.name == "B Club" })
    }

    @Test
    fun `searchClubs returns clubs that match partial name`() {
        services.clubsServices.createClub(ClubCreate("Padel Sinners"), token)
        services.clubsServices.createClub(ClubCreate("Thunderbolts Padel Sin"), token)
        services.clubsServices.createClub(ClubCreate("Trump sucks"), token)

        val page = services.clubsServices.searchClubs(
            name  = "padel",
            skip  = 0,
            limit = 10
        )

        assertEquals(1, page.totalCount)
        assertTrue(page.items.any { it.name == "Padel Sinners" })
        assertTrue(page.items.none { it.name == "Thunderbolts Padel Sin" })
        assertTrue(page.items.none { it.name == "Trump sucks" })
    }

    @Test
    fun `searchClubs applies pagination correctly`() {
        services.clubsServices.createClub(ClubCreate("Pa One"),   token)
        services.clubsServices.createClub(ClubCreate("Pa Two"),   token)
        services.clubsServices.createClub(ClubCreate("Pa Three"), token)

        val page1 = services.clubsServices.searchClubs(
            name  = "Pa",
            skip  = 0,
            limit = 2
        )

        assertEquals(3, page1.totalCount)
        assertEquals(2, page1.items.size)
        assertEquals(listOf("Pa One", "Pa Two"), page1.items.map { it.name })

        val page2 = services.clubsServices.searchClubs(
            name  = "Pa",
            skip  = 2,
            limit = 2
        )

        assertEquals(3, page2.totalCount)
        assertEquals(1, page2.items.size)
        assertEquals("Pa Three", page2.items.first().name)
    }
}
