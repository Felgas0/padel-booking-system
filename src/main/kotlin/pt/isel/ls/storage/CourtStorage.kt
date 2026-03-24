package pt.isel.ls.storage

import pt.isel.ls.domain.Court

interface CourtStorage {
    /**
     * Retrieves all courts associated with a specific club.
     *
     * @param clubId the club ID to get the courts from
     * @return a list with all the courts of the specified club
     */
    fun getCourts(clubId: Int): List<Court>

    /**
     * Retrieves the details of a specific court by its ID.
     *
     * @param courtId the court ID to get the details
     * @return the court if found, or null if not found
     */
    fun getCourtDetails(courtId: Int): Court?

    /**
     * Creates a new court associated with a specific club.
     *
     * @param courtName the name to assign to the new court
     * @param clubId the club ID the new court belongs to
     * @return the created Court instance
     */
    fun createCourt(
        courtName: String,
        clubId: Int,
    ): Court

    /**
     * Inserts a court directly into the storage.
     * Typically used for testing or internal setup.
     *
     * @param court the court instance to insert
     */
    fun insertCourt(court: Court)

    /**
     * Clears all courts from the storage.
     * Useful for testing or resetting state.
     */
    fun reset()

    /**
     * Retrieves the club ID that a given court belongs to.
     *
     * @param courtId the court ID
     * @return the ID of the club that owns the specified court
     */
    fun getClubIdByCourtId(courtId: Int): Int

    /**
     * Deletes a court from storage by its ID.
     *
     * @param courtId the ID of the court to delete
     */
    fun deleteCourtById(courtId: Int)
}
