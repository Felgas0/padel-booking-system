package pt.isel.ls.storage.dataPostgres

import pt.isel.ls.domain.Court
import pt.isel.ls.storage.CourtStorage
import pt.isel.ls.storage.dataPostgres.utils.query
import pt.isel.ls.storage.dataPostgres.utils.execute
import pt.isel.ls.storage.dataPostgres.utils.toCourt
import java.sql.Connection

/**
 * Implementation of [CourtStorage] backed by a PostgreSQL database.
 *
 * Encapsulates all persistence logic for courts using JDBC, and delegates
 * common query and update operations to helper functions to reduce boilerplate.
 */
class CourtsPostgres(
    private val conn: Connection
) : CourtStorage {

    /**
     * Retrieves all courts for a given club, ordered by id.
     */
    override fun getCourts(clubId: Int): List<Court> {
        val sql = "SELECT id, name, club_id FROM courts WHERE club_id = ? ORDER BY id"
        return query(conn, sql, { it.setInt(1, clubId) }) { it.toCourt() }
    }

    /**
     * Retrieves a Court by its id, or null if not found.
     */
    override fun getCourtDetails(courtId: Int): Court? {
        val sql = "SELECT id, name, club_id FROM courts WHERE id = ?"
        return query(conn, sql, { it.setInt(1, courtId) }) { it.toCourt() }
            .firstOrNull()
    }

    /**
     * Inserts a new court into the database and returns the created Court.
     * Uses RETURNING clause to fetch the generated id.
     */
    override fun createCourt(courtName: String, clubId: Int): Court {
        val sql = "INSERT INTO courts (name, club_id) VALUES (?, ?) RETURNING id"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, courtName)
            stmt.setInt(2, clubId)
            stmt.executeQuery().use { rs ->
                rs.next()
                val id = rs.getInt("id")
                return Court(id, courtName, clubId)
            }
        }
    }

    /**
     * Inserts an existing Court (including id) into the database.
     */
    override fun insertCourt(court: Court) {
        val sql = "INSERT INTO courts (id, name, club_id) VALUES (?, ?, ?)"
        execute(conn, sql) {
            it.setInt(1, court.id)
            it.setString(2, court.name)
            it.setInt(3, court.clubId)
        }
    }

    /**
     * Deletes all courts and resets the id sequence.
     */
    override fun reset() {
        conn.prepareStatement("DELETE FROM courts").use { it.executeUpdate() }
        conn.prepareStatement("ALTER SEQUENCE courts_id_seq RESTART WITH 1").use { it.executeUpdate() }
    }

    /**
     * Retrieves the club id associated with the given court id.
     * Throws [IllegalStateException] if the court is not found.
     */
    override fun getClubIdByCourtId(courtId: Int): Int {
        val sql = "SELECT club_id FROM courts WHERE id = ?"
        return query(conn, sql, { it.setInt(1, courtId) }) { rs -> rs.getInt("club_id") }
            .firstOrNull() ?: throw IllegalStateException("Court with id $courtId not found")
    }

    /**
     * Deletes a Court by its id. Returns true if one row was affected.
     */
    override fun deleteCourtById(courtId: Int) {
        execute(conn, "DELETE FROM courts WHERE id = ?") { it.setInt(1, courtId) }
    }
}
