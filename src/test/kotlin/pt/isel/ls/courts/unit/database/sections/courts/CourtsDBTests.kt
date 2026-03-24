package pt.isel.ls.courts.unit.database.sections.courts

import pt.isel.ls.storage.Storage
import kotlin.test.Test

interface CourtsDBTests {
    val db: Storage

    @Test
    fun `create and retrieve court`()

    @Test
    fun `getCourtDetails returns null for invalid id`()

    @Test
    fun `getCourts returns list of courts for a club`()

    @Test
    fun `deleteCourtById removes court`()
}
