package pt.isel.ls.services

import pt.isel.ls.API.dtosAPI.club.*
import pt.isel.ls.services.dtosServices.Paged
import pt.isel.ls.storage.Storage
import pt.isel.ls.utils.*
import java.util.UUID

/**
 * Service class responsible for business logic related to clubs,
 * including creation, retrieval, and name-based searching.
 *
 * @param storage the shared data access layer
 */
class ClubsServices(storage: Storage) : ServicesModel(storage) {

    /**
     * Creates a new club with the specified name and sets the authenticated user as the owner.
     * Validates name uniqueness and required input.
     *
     * @param body the input data containing the club name
     * @param token the user's authorization token
     * @return a response containing the newly created club's ID
     */
    fun createClub(body: ClubCreate, token: UUID): ClubResponse {
        requireName(body.name)

        if (storage.clubStorage.getClubs().any { it.name == body.name })
            conflict("Name already exists!")

        val owner = userByToken(token)
        val club  = storage.clubStorage.createClub(body.name, owner.id)
        return ClubResponse(club.id)
    }

    /**
     * Retrieves detailed information about a club by its ID.
     *
     * @param id the unique identifier of the club
     * @return the club's details (id, name, ownerId)
     */
    fun getClub(id: Int): ClubDetails {
        requireId(id, "Club id")
        val club = clubOr404(id)
        return ClubDetails(club.id, club.name, club.ownerId)
    }

    /**
     * Retrieves a paginated list of all clubs.
     *
     * @param skip number of items to skip
     * @param limit number of items to return
     * @return a paged list of club details
     */
    fun getClubs(skip: Int, limit: Int): Paged<ClubDetails> =
        storage.clubStorage.getClubs()
            .map { ClubDetails(it.id, it.name, it.ownerId) }
            .page(skip, limit)

    /**
     * Searches for clubs whose name starts with the given string (case-insensitive).
     * Results are returned in a paginated format.
     *
     * @param name the name fragment to search for
     * @param skip number of items to skip
     * @param limit number of items to return
     * @return a paged list of club details matching the query
     */
    fun searchClubs(name: String, skip: Int, limit: Int): Paged<ClubDetails> {
        requireName(name, "name query parameter")
        return storage.clubStorage.searchByName(name)
            .map { ClubDetails(it.id, it.name, it.ownerId) }
            .page(skip, limit)
    }
}
