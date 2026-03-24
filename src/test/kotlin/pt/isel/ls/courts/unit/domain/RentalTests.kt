package pt.isel.ls.courts.unit.domain

import pt.isel.ls.domain.Rental
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertFailsWith

class RentalTests {
    private val validDate =
        LocalDateTime.of(
            2099,
            1,
            1,
            10,
            0,
        )

    @Test
    fun `creating rental with valid values should succeed`() {
        Rental(
            id = 1,
            date = validDate,
            duration = 2,
            user = 1,
            clubId = 1,
            courtId = 1,
        )
    }

    @Test
    fun `creating rental with id zero should throw`() {
        assertFailsWith<IllegalArgumentException> {
            Rental(
                id = 0,
                date = validDate,
                duration = 2,
                user = 1,
                clubId = 1,
                courtId = 1,
            )
        }
    }

    @Test
    fun `creating rental with negative duration should throw`() {
        assertFailsWith<IllegalArgumentException> {
            Rental(
                id = 1,
                date = validDate,
                duration = -1,
                user = 1,
                clubId = 1,
                courtId = 1,
            )
        }
    }

    @Test
    fun `creating rental with clubId zero should throw`() {
        assertFailsWith<IllegalArgumentException> {
            Rental(
                id = 1,
                date = validDate,
                duration = 1,
                user = 1,
                clubId = 0,
                courtId = 1,
            )
        }
    }

    @Test
    fun `creating rental with courtId zero should throw`() {
        assertFailsWith<IllegalArgumentException> {
            Rental(
                id = 1,
                date = validDate,
                duration = 1,
                user = 1,
                clubId = 1,
                courtId = 0,
            )
        }
    }
}
