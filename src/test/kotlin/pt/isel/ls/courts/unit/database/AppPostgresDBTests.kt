package pt.isel.ls.courts.unit.database

import org.junit.Assume.assumeNotNull
import org.junit.Before
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.storage.Storage
import pt.isel.ls.storage.dataPostgres.DataPostgres
import kotlin.test.BeforeTest

abstract class AppPostgresDBTests {
    open lateinit var db: Storage

    companion object {
        private val jdbcUrl: String? = System.getenv("JDBC_DATABASE_URL")
        val dataSource =
            PGSimpleDataSource().apply {
                jdbcUrl?.let { setURL(it) }
            }
    }

    @Before
    fun assumeDatabaseEnv() {
        assumeNotNull(jdbcUrl)
    }

    @BeforeTest
    fun setupPostgresDB() {
        if (jdbcUrl != null) {
            db = DataPostgres()
        }
    }
}
