package pt.isel.ls.storage.dataPostgres.utils

import pt.isel.ls.domain.*
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*


/**
 * Provides utility functions for working with JDBC and PostgreSQL in a consistent and reusable way.
 *
 * Includes generic methods for executing SQL queries and updates (`query` and `execute`) using prepared statements,
 * which helps reduce boilerplate and improve safety against SQL injection. It also defines `ResultSet` extension functions
 * (`toRental`, `toUser`, `toClub`, `toCourt`) that map database rows to domain model objects, ensuring consistency
 * and readability when constructing entities from query results.
 *
 * This file serves as a centralized helper for the data access layer.
 */



/**
 * Executes a SQL query with parameter binding and maps each row to type T.
 */
@Suppress("SqlSourceToSinkFlow")
fun <T> query(
    conn: Connection,
    sql: String,
    binder: (PreparedStatement) -> Unit,
    extractor: (ResultSet) -> T
): List<T> {
    val stmt = conn.prepareStatement(sql)
    binder(stmt)
    val rs = stmt.executeQuery()
    val results = mutableListOf<T>()
    while (rs.next()) {
        results += extractor(rs)
    }
    return results
}

/**
 * Executes an update or delete SQL statement with parameter binding.
 */
@Suppress("SqlSourceToSinkFlow")
fun execute(
    conn: Connection,
    sql: String,
    binder: (PreparedStatement) -> Unit
): Boolean {
    val stmt = conn.prepareStatement(sql)
    binder(stmt)
    return stmt.executeUpdate() == 1
}

/**
 * Maps a ResultSet row to a Rental domain object.
 * Assumes the result set includes a joined club_id from the courts table.
 */
fun ResultSet.toRental(): Rental = Rental(
    id       = getInt("id"),
    date     = getTimestamp("date").toLocalDateTime(),
    duration = getInt("duration"),
    user     = getInt("user_id"),
    clubId   = getInt("club_id"),
    courtId  = getInt("court_id")
)

/**
 * Maps a ResultSet row to a User domain object.
 */
fun ResultSet.toUser(): User = User(
    id           = getInt("id"),
    name         = getString("name"),
    email        = Email(getString("email")),
    passwordHash = getString("password_hash"),
    token        = UUID.fromString(getString("token"))
)

/**
 * Maps a ResultSet row to a Club domain object.
 * Assumes the columns id, name, and owner_id are present.
 */
fun ResultSet.toClub(): Club = Club(
    id      = getInt("id"),
    name    = getString("name"),
    ownerId = getInt("owner_id"),
    courts  = mutableListOf()
)

/**
 * Maps a ResultSet row to a Court domain object.
 * Assumes the columns id, name, and club_id are present.
 */
fun ResultSet.toCourt(): Court = Court(
    id     = getInt("id"),
    name   = getString("name"),
    clubId = getInt("club_id")
)
