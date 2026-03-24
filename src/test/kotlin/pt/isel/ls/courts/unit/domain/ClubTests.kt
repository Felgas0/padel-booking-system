package pt.isel.ls.courts.unit.domain

import pt.isel.ls.domain.Club
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ClubTests {
    @Test
    fun `creating club with valid data does not throw`() {
        Club(
            id = 1,
            name = "My Club",
            ownerId = 1,
            courts = mutableListOf(),
        )
    }

    @Test
    fun `creating club with id less than or equal to 0 throws`() {
        assertFailsWith<IllegalArgumentException> {
            Club(
                id = 0,
                name = "Club",
                ownerId = 1,
                courts = mutableListOf(),
            )
        }
    }

    @Test
    fun `creating club with blank name throws`() {
        assertFailsWith<IllegalArgumentException> {
            Club(
                id = 1,
                name = "   ",
                ownerId = 1,
                courts = mutableListOf(),
            )
        }
    }

    @Test
    fun `creating club with ownerId less than or equal to 0 throws`() {
        assertFailsWith<IllegalArgumentException> {
            Club(
                id = 1,
                name = "Club",
                ownerId = 0,
                courts = mutableListOf(),
            )
        }
    }
}
