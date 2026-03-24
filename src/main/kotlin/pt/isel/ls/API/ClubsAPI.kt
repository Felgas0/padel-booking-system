package pt.isel.ls.API

import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.path
import pt.isel.ls.API.dtosAPI.club.*
import pt.isel.ls.API.utils.*
import pt.isel.ls.services.ClubsServices
import pt.isel.ls.utils.*

/**
 * Handles HTTP requests related to clubs.
 *
 * @property services the service layer that provides business logic for clubs
 */
class ClubsAPI(
    private val services: ClubsServices,
) : APIModel() {

    /**
     * Handles POST /clubs
     * Creates a new club using the request body and authenticated user.
     *
     * @param req the HTTP request containing the club data and user token
     * @return a Response with status 201 Created and the clubId
     */
    fun createClub(req: Request): Response {
        val body  = Json.decodeFromString<ClubCreate>(req.bodyString())
        val token = req.requireToken()
        val out   = services.createClub(body, token)
        return Response(Status.CREATED).json(out)
    }

    /**
     * Handles GET /clubs/{clubId}
     * Retrieves details of a specific club by ID.
     *
     * @param req the HTTP request containing the path parameter "clubId"
     * @return a Response with status 200 OK and the club details
     */
    fun getClubDetails(req: Request): Response {
        val id = req.path("clubId")?.toInt()
            ?: badRequest("clubId must be a number")
        return Response(Status.OK).json(services.getClub(id))
    }

    /**
     * Handles GET /clubs?skip=&limit=
     * Retrieves a paginated list of all clubs.
     *
     * @param req the HTTP request with optional query parameters "skip" and "limit"
     * @return a Response with status 200 OK and the list of clubs
     */
    fun getAllClubs(req: Request): Response {
        val (limit, skip) = paging(req)
        val page = services.getClubs(skip, limit)
        return Response(Status.OK).json(
            ClubsListResponse(page.items, page.totalCount)
        )
    }

    /**
     * Handles GET /clubs/search?name=xxx&skip=&limit=
     * Searches for clubs by partial name with pagination support.
     *
     * @param req the HTTP request with required query parameter "name" and optional "skip"/"limit"
     * @return a Response with status 200 OK and the matching clubs
     */
    fun searchClubs(req: Request): Response {
        val name = req.query("name")
            ?: badRequest("name Query-Parameter is required")

        val (limit, skip) = paging(req)
        val page = services.searchClubs(name, skip, limit)

        return Response(Status.OK).json(
            ClubsListResponse(page.items, page.totalCount)
        )
    }
}
