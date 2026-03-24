package pt.isel.ls.storage

import pt.isel.ls.domain.Club
import pt.isel.ls.domain.Court

interface ClubStorage {
    /**
     * Retrieves the details of a specific club by its ID.
     *
     * @param clubId the club ID
     * @return the details of the club or null if not found
     */
    fun getClubDetails(clubId: Int): Club?

    /**
     * Retrieves all clubs stored in the database.
     *
     * @return a list with all the clubs in the database
     */
    fun getClubs(): List<Club>

    /**
     * Creates a new club with the specified name and owner.
     *
     * @param clubName the name to give to the new club
     * @param ownerId the ID of the user creating the club
     * @return the created Club instance
     */
    fun createClub(
        clubName: String,
        ownerId: Int,
    ): Club

    /**
     * Gets a club by its ID.
     *
     * @param clubId the club ID
     * @return the club if found, or null otherwise
     */
    fun getClub(clubId: Int): Club?

    /**
     * Associates a court with a club.
     *
     * @param club the club to which the court will be added
     * @param court the court to associate with the club
     */
    fun addCourtToClub(
        club: Club,
        court: Court,
    )

    /**
     * Inserts a club directly into the storage.
     * Used for testing or internal operations.
     *
     * @param club the club to insert
     */
    fun insertClub(club: Club)

    /**
     * Removes all clubs from the storage.
     * Useful for testing or reset operations.
     */
    fun reset()

    /**
     * Deletes a specific club by its ID.
     *
     * @param clubId the ID of the club to delete
     */
    fun deleteClubById(clubId: Int)

    /**
     * Searches for clubs by a partial name match.
     *
     * @param fragment the name fragment to search for
     * @return a list of clubs whose names contain the given fragment
     */
    fun searchByName(fragment: String): List<Club>
}
