package pt.isel.ls.storage.dataPostgres

import pt.isel.ls.domain.Club
import pt.isel.ls.domain.Court
import pt.isel.ls.storage.ClubStorage
import pt.isel.ls.storage.dataPostgres.utils.query
import pt.isel.ls.storage.dataPostgres.utils.execute
import pt.isel.ls.storage.dataPostgres.utils.toClub
import java.sql.Connection

/**
 * Implementation of [ClubStorage] backed by a PostgreSQL database.
 *
 * Encapsulates all persistence logic for clubs using JDBC, and delegates
 * common query and update operations to helper functions to reduce boilerplate.
 */
class ClubsPostgres(
    private val conn: Connection
) : ClubStorage {

    /**
     * Inserts a new club into the database and returns the created Club.
     * Uses RETURNING clause to fetch the generated id.
     */
    override fun createClub(clubName: String, ownerId: Int): Club {
        val sql = "INSERT INTO clubs (name, owner_id) VALUES (?, ?) RETURNING id"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, clubName)
            stmt.setInt(2, ownerId)
            stmt.executeQuery().use { rs ->
                rs.next()
                val id = rs.getInt("id")
                return Club(id, clubName, ownerId, mutableListOf())
            }
        }
    }

    /**
     * Retrieves a Club by its id, or null if not found.
     * Selects only basic club fields; courts can be added separately.
     */
    override fun getClubDetails(clubId: Int): Club? {
        val sql = "SELECT id, name, owner_id FROM clubs WHERE id = ?"
        return query(conn, sql, { it.setInt(1, clubId) }) { it.toClub() }
            .firstOrNull()
    }

    /**
     * Retrieves all clubs from the database, ordered by id.
     */
    override fun getClubs(): List<Club> {
        val sql = "SELECT id, name, owner_id FROM clubs ORDER BY id"
        return query(conn, sql, { /* no params */ }) { it.toClub() }
    }

    /**
     * Alias for getClubDetails.
     */
    override fun getClub(clubId: Int): Club? =
        getClubDetails(clubId)

    /**
     * Adds a Court to an in-memory Club object. No database update needed,
     * since courts are persisted independently in their own table.
     */
    override fun addCourtToClub(club: Club, court: Court) {
        club.courts.add(court)
    }

    /**
     * Inserts an existing Club (including id) into the database.
     */
    override fun insertClub(club: Club) {
        val sql = "INSERT INTO clubs (id, name, owner_id) VALUES (?, ?, ?)"
        execute(conn, sql) {
            it.setInt(1, club.id)
            it.setString(2, club.name)
            it.setInt(3, club.ownerId)
        }
    }

    /**
     * Deletes all clubs and resets the id sequence.
     */
    override fun reset() {
        conn.prepareStatement("DELETE FROM clubs").use { it.executeUpdate() }
        conn.prepareStatement("ALTER SEQUENCE clubs_id_seq RESTART WITH 1").use { it.executeUpdate() }
    }

    /**
     * Deletes a Club by its id. Returns true if one row was affected.
     */
    override fun deleteClubById(clubId: Int) {
        execute(conn, "DELETE FROM clubs WHERE id = ?") { it.setInt(1, clubId) }
    }

    /**
     * Searches for clubs whose name starts with the given fragment (case-insensitive).
     * Returns a list ordered by id.
     */
    override fun searchByName(fragment: String): List<Club> {
        val sql = """
            SELECT id, name, owner_id
              FROM clubs
             WHERE name ILIKE ? || '%'
             ORDER BY id
        """.trimIndent()
        return query(conn, sql, { it.setString(1, fragment) }) { it.toClub() }
    }
}
