package pt.isel.ls.courts.unit.database.sections.rental

import pt.isel.ls.storage.Storage
import kotlin.test.Test

interface RentalDBTests {
    val db: Storage

    @Test
    fun `create and fetch rental`()

    @Test
    fun `getRentalDetails returns null for invalid id`()

    @Test
    fun `getUserRentals returns rentals for user`()
}
