package pt.isel.ls.storage.dataMem

import pt.isel.ls.domain.Court
import pt.isel.ls.storage.CourtStorage

/**
 * In-memory implementation of [CourtStorage] for testing or development purposes.
 *
 * Uses a mutable map to simulate persistent storage of courts and supports
 * basic CRUD operations with filtering based on club identifiers.
 */
class CourtsMem : CourtStorage {

    private val courts = mutableMapOf<Int, Court>()

    /**
     * Retrieves all courts associated with the given club ID.
     */
    override fun getCourts(clubId: Int): List<Court> =
        courts.values.filter { it.clubId == clubId }.toList()

    /**
     * Retrieves a court by its ID, or null if it doesn't exist.
     */
    override fun getCourtDetails(courtId: Int): Court? {
        return courts[courtId]
    }

    /**
     * Creates a new court associated with a given club and stores it in memory.
     * Generates an incremental ID based on the current maximum.
     */
    override fun createCourt(courtName: String, clubId: Int): Court {
        val id = if (courts.isEmpty()) 1 else courts.keys.maxOrNull()!! + 1
        val court = Court(id, courtName, clubId)
        courts[id] = court
        return court
    }

    /**
     * Inserts an existing Court into memory (used for testing).
     */
    override fun insertCourt(court: Court) {
        courts[court.id] = court
    }

    /**
     * Clears all courts from memory.
     */
    override fun reset() {
        courts.clear()
    }

    /**
     * Returns the club ID associated with a given court ID.
     * Throws if the court does not exist.
     */
    override fun getClubIdByCourtId(courtId: Int): Int {
        return courts[courtId]?.clubId
            ?: throw IllegalStateException("Court with id $courtId not found")
    }

    /**
     * Removes a court by its ID from the in-memory store.
     */
    override fun deleteCourtById(courtId: Int) {
        courts.remove(courtId)
    }
}
