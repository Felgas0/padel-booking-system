package pt.isel.ls.courts.integration

import org.http4k.client.JavaHttpClient
import org.junit.AfterClass
import org.junit.BeforeClass
import pt.isel.ls.AppServer
import pt.isel.ls.storage.Storage
import pt.isel.ls.storage.dataMem.DataMem
import kotlin.test.BeforeTest

/**
 * Abstract base class for integration tests.
 *
 * This class ensures:
 * - A shared test server is started once before all tests.
 * - The server is stopped after all tests complete.
 * - A shared HTTP client and base URL are available to all tests.
 * - In-memory data is reset between tests when using DataMem.
 */
abstract class IntegrationTests {
    companion object {
        private const val DEFAULT_PORT = 8080
        private val port = DEFAULT_PORT
        private val jdbcDatabaseURL: String? = System.getenv("JDBC_DATABASE_URL")

//        val db: Storage = if (jdbcDatabaseURL != null)
//            DataPostgres()
//        else
//            DataMem()

        // In-memory database used for tests
        val db: Storage = DataMem() // Force local memory

        // Shared HTTP client for sending test requests
        val send = JavaHttpClient()

        // Base URL for test requests
        val uriPrefix = "http://localhost:$port"

        // Server instance used in tests
        private val server = AppServer(port, db)

        /**
         * Starts the HTTP server once before any test runs.
         */
        @BeforeClass
        @JvmStatic
        fun start() {
            server.start()
        }

        /**
         * Stops the HTTP server after all tests complete.
         */
        @AfterClass
        @JvmStatic
        fun stop() {
            server.stop()
        }
    }

    /**
     * Resets the in-memory database before each test
     * to ensure test isolation and consistency.
     */
    @BeforeTest
    fun resetDataIfNeeded() {
        if (db is DataMem) {
            db.reset()
        }
    }
}
