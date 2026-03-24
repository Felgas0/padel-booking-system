package pt.isel.ls.courts.unit.domain

import pt.isel.ls.domain.Court
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CourtTests {
    @Test
    fun `creating court with valid data does not throw`() {
        Court(
            id = 1,
            name = "Central Court",
            clubId = 1,
        )
    }

    @Test
    fun `creating court with id less than or equal to 0 throws`() {
        assertFailsWith<IllegalArgumentException> {
            Court(
                id = 0,
                name = "Court A",
                clubId = 1,
            )
        }
    }

    @Test
    fun `creating court with blank name throws`() {
        assertFailsWith<IllegalArgumentException> {
            Court(
                id = 1,
                name = "   ",
                clubId = 1,
            )
        }
    }

    @Test
    fun `creating court with clubId less than or equal to 0 throws`() {
        assertFailsWith<IllegalArgumentException> {
            Court(
                id = 1,
                name = "Court B",
                clubId = 0,
            )
        }
    }
}
