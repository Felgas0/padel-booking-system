package pt.isel.ls.courts.unit.database

import pt.isel.ls.storage.Storage
import pt.isel.ls.storage.dataMem.DataMem
import kotlin.test.BeforeTest

abstract class AppMemoryDBTests {
    open lateinit var db: Storage

    @BeforeTest
    fun setupMemoryDB() {
        db = DataMem().apply { reset() }
    }
}
