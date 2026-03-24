package pt.isel.ls.courts.unit.services

import pt.isel.ls.services.Services
import pt.isel.ls.storage.Storage
import pt.isel.ls.storage.dataMem.DataMem
import kotlin.test.BeforeTest

abstract class AbstractServicesTests {
    protected lateinit var storage: Storage
    protected lateinit var services: Services

    @BeforeTest
    fun setup() {
        storage = DataMem().apply { reset() }
        services = Services(storage)
    }
}
