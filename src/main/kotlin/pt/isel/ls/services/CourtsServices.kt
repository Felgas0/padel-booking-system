package pt.isel.ls.services

import pt.isel.ls.API.dtosAPI.court.*
import pt.isel.ls.services.dtosServices.Paged
import pt.isel.ls.storage.Storage
import pt.isel.ls.utils.*
import java.util.UUID

/**
 * Service class responsible for handling business logic related to courts.
 * Provides functionality for court creation and retrieval operations.
 *
 * @param storage the data access layer dependency
 */
class CourtsServices(storage: Storage) : ServicesModel(storage) {

    /**
     * Creates a new court associated with a given club, if the authenticated user is the owner of that club.
     *
     * @param body the input containing the name and clubId of the new court
     * @param token the user's authorization token
     * @return a response with the ID of the newly created court
     */
    fun createCourt(body: CourtCreate, token: UUID): CourtResponse {
        requireName(body.name, "Court name")

        val club  = clubOr404(body.clubId)
        val owner = userByToken(token)

        if (club.ownerId != owner.id)
            forbidden("Only the club owner can add courts.")

        val court = storage.courtStorage.createCourt(body.name, body.clubId)
        storage.clubStorage.addCourtToClub(club, court)

        return CourtResponse(court.id)
    }

    /**
     * Retrieves detailed information about a court by its ID.
     *
     * @param id the court's unique identifier
     * @return the court's details (id, name, clubId)
     */
    fun getCourtDetails(id: Int): CourtDetails {
        requireId(id, "Court id")
        val court = courtOr404(id)
        return CourtDetails(court.id, court.name, court.clubId)
    }

    /**
     * Retrieves a paginated list of courts associated with a specific club.
     *
     * @param clubId the ID of the club to retrieve courts from
     * @param skip number of items to skip for pagination
     * @param limit number of items to return
     * @return a paged list of court details
     */
    fun getCourts(clubId: Int, skip: Int, limit: Int): Paged<CourtDetails> =
        storage.courtStorage.getCourts(clubId)
            .map { CourtDetails(it.id, it.name, it.clubId) }
            .page(skip, limit)
}
