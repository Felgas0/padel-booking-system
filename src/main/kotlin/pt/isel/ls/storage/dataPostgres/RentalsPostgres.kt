package pt.isel.ls.storage.dataPostgres

import pt.isel.ls.domain.Rental
import pt.isel.ls.storage.RentalStorage
import pt.isel.ls.storage.dtosStorage.rental.RentalEdit
import pt.isel.ls.storage.dtosStorage.rental.RentalInsert
import pt.isel.ls.storage.dataPostgres.utils.execute
import pt.isel.ls.storage.dataPostgres.utils.query
import pt.isel.ls.storage.dataPostgres.utils.toRental
import java.sql.Connection
import java.time.LocalDate
import java.time.LocalDateTime


/**
 * Implementation of [RentalStorage] backed by a PostgreSQL database.
 *
 * This class encapsulates all persistence logic for rentals using JDBC, and performs
 * necessary joins to reduce redundant queries (e.g., joins with courts to fetch clubId).
 * It uses prepared statements for safety and helper functions for common JDBC patterns.
 */


@Suppress("SqlSourceToSinkFlow")
class RentalsPostgres(
    private val conn: Connection
) : RentalStorage {

    override fun createRental(rental: RentalInsert, userId: Int): Rental {
        val stmt = conn.prepareStatement(
            """
            INSERT INTO rentals (date, duration, user_id, court_id) 
            VALUES (?, ?, ?, ?) RETURNING id
            """.trimIndent()
        )
        stmt.setObject(1, rental.date)
        stmt.setInt(2, rental.duration)
        stmt.setInt(3, userId)
        stmt.setInt(4, rental.courtId)

        stmt.executeQuery().use { rs ->
            rs.next()
            val id = rs.getInt("id")
            return Rental(id, rental.date, rental.duration, userId, rental.clubId, rental.courtId)
        }
    }

    override fun getRentalDetails(rentalId: Int): Rental? {
        val sql = """
            SELECT r.id, r.date, r.duration, r.user_id, r.court_id, c.club_id
            FROM rentals r
            JOIN courts c ON r.court_id = c.id
            WHERE r.id = ?
        """.trimIndent()
        return query(conn, sql, { it.setInt(1, rentalId) }) { it.toRental() }
            .firstOrNull()
    }

    override fun getRentals(clubId: Int, courtId: Int, date: LocalDate?): List<Rental> {
        val sb = StringBuilder(
            """
            SELECT r.id, r.date, r.duration, r.user_id, r.court_id, c.club_id
            FROM rentals r
            JOIN courts c ON r.court_id = c.id
            WHERE r.court_id = ?
            """.trimIndent()
        )
        if (date != null) sb.append(" AND DATE(r.date) = ?")

        val sql = sb.toString()
        return query(conn, sql, { stmt ->
            stmt.setInt(1, courtId)
            if (date != null) stmt.setDate(2, java.sql.Date.valueOf(date))
        }) { it.toRental() }
    }

    override fun getUserRentals(userId: Int): List<Rental> =
        query(conn,
            """
            SELECT r.id, r.date, r.duration, r.user_id, r.court_id, c.club_id
            FROM rentals r
            JOIN courts c ON r.court_id = c.id
            WHERE r.user_id = ?
            """.trimIndent(),
            { it.setInt(1, userId) }
        ) { it.toRental() }

    override fun getAvailableHours(
        clubId: Int,
        courtId: Int,
        date: LocalDateTime
    ): List<LocalDateTime> {
        val stmt = conn.prepareStatement(
            """
            SELECT date, duration
            FROM rentals
            WHERE court_id = ? AND DATE(date) = ?
            """.trimIndent()
        )
        stmt.setInt(1, courtId)
        stmt.setDate(2, java.sql.Date.valueOf(date.toLocalDate()))

        val unavailable = mutableSetOf<Int>()
        stmt.executeQuery().use { rs ->
            while (rs.next()) {
                val start = rs.getTimestamp("date").toLocalDateTime().hour
                repeat(rs.getInt("duration")) { unavailable.add(start + it) }
            }
        }

        return (0..23)
            .filterNot { it in unavailable }
            .map { date.toLocalDate().atTime(it, 0) }
    }

    override fun reset() {
        conn.prepareStatement("DELETE FROM rentals").executeUpdate()
        conn.prepareStatement("ALTER SEQUENCE rentals_id_seq RESTART WITH 1").executeUpdate()
    }

    override fun deleteRentalById(rentalId: Int): Boolean =
        execute(conn,
            "DELETE FROM rentals WHERE id = ?"
        ) { it.setInt(1, rentalId) }

    override fun updateRental(rentalId: Int, rentalEdit: RentalEdit): Boolean =
        execute(conn,
            "UPDATE rentals SET date = ?, duration = ? WHERE id = ?"
        ) {
            it.setObject(1, rentalEdit.date)
            it.setInt(2, rentalEdit.duration)
            it.setInt(3, rentalId)
        }

    override fun hasTimeConflict(
        courtId: Int,
        date: LocalDateTime,
        plusHours: LocalDateTime?,
        rentalId: Int
    ): Boolean {
        val sql = buildString {
            append("SELECT COUNT(*) FROM rentals WHERE court_id = ? ")
            if (rentalId > 0) append("AND id != ? ")
            append("AND date < ? AND (date + (duration * INTERVAL '1 hour')) > ?")
        }
        val stmt = conn.prepareStatement(sql)
        stmt.setInt(1, courtId)
        var idx = 2
        if (rentalId > 0) stmt.setInt(idx++, rentalId)
        stmt.setObject(idx++, plusHours)
        stmt.setObject(idx, date)
        stmt.executeQuery().use { rs ->
            return rs.next() && rs.getInt(1) > 0
        }
    }
}
