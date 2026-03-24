package pt.isel.ls.storage.dataMem

import pt.isel.ls.domain.Club
import pt.isel.ls.domain.Court
import pt.isel.ls.storage.ClubStorage

/**
 * In-memory implementation of [ClubStorage] for testing or development purposes.
 *
 * Uses a mutable map to simulate persistent storage of clubs and supports
 * basic CRUD operations. Courts are managed separately and linked manually.
 */
class ClubsMem : ClubStorage {
    private val clubs = mutableMapOf<Int, Club>()

    /**
     * Creates a new club and adds it to the in-memory map.
     * Generates an incremental ID based on the current maximum.
     */
    override fun createClub(clubName: String, ownerId: Int): Club {
        val id = if (clubs.isEmpty()) 1 else clubs.keys.maxOrNull()!! + 1
        val club = Club(id, clubName, ownerId, mutableListOf())
        clubs[id] = club
        return club
    }

    /**
     * Retrieves a club by ID, or null if not found.
     */
    override fun getClubDetails(clubId: Int): Club? {
        return clubs[clubId]
    }

    /**
     * Retrieves all clubs stored in memory.
     */
    override fun getClubs(): List<Club> {
        return clubs.values.toList()
    }

    /**
     * Alias for getClubDetails.
     */
    override fun getClub(clubId: Int): Club? = clubs[clubId]

    /**
     * Adds a Court to the given Club instance in memory.
     * Assumes the court is already stored separately.
     */
    override fun addCourtToClub(club: Club, court: Court) {
        val clubCurr = clubs[club.id]
        clubCurr?.courts?.add(court)
    }

    /**
     * Inserts an existing Club into memory (used for testing).
     */
    override fun insertClub(club: Club) {
        clubs[club.id] = club
    }

    /**
     * Clears all clubs from memory.
     */
    override fun reset() {
        clubs.clear()
    }

    /**
     * Removes a club by ID from the in-memory store.
     */
    override fun deleteClubById(clubId: Int) {
        clubs.remove(clubId)
    }

    /**
     * Searches for clubs whose name starts with the given fragment (case-insensitive).
     */
    override fun searchByName(fragment: String): List<Club> =
        clubs.values.filter { it.name.startsWith(fragment, ignoreCase = true) }
}
