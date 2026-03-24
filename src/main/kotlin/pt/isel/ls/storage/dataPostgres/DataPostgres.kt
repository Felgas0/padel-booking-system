package pt.isel.ls.storage.dataPostgres

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.storage.*

/**
 * PostgreSQL-based implementation of the [Storage] interface.
 *
 * Initializes a connection to the PostgreSQL database using the JDBC URL provided
 * via the environment variable `JDBC_DATABASE_URL`, and sets up concrete storage
 * implementations for users, clubs, courts, and rentals.
 */
class DataPostgres : Storage {

    // Data source configured with JDBC URL from environment
    private val dataSource =
        PGSimpleDataSource().apply {
            setURL(System.getenv("JDBC_DATABASE_URL"))
        }

    // Single shared database connection
    private val conn = dataSource.connection

    // Storage implementations using the shared connection
    override val userStorage: UserStorage = UsersPostgres(conn)
    override val clubStorage: ClubStorage = ClubsPostgres(conn)
    override val courtStorage: CourtStorage = CourtsPostgres(conn)
    override val rentalStorage: RentalStorage = RentalsPostgres(conn)

    /**
     * Resets all storage components by clearing their contents.
     * Useful for test setups or development environments.
     */
    override fun reset() {
        rentalStorage.reset()
        courtStorage.reset()
        clubStorage.reset()
        userStorage.reset()
    }
}
