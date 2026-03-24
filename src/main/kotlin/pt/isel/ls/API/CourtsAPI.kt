package pt.isel.ls.API

import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.path
import pt.isel.ls.API.dtosAPI.court.*
import pt.isel.ls.API.utils.*
import pt.isel.ls.services.CourtsServices
import pt.isel.ls.utils.*

/**
 * Handles HTTP requests related to courts.
 *
 * @property services the service layer that provides business logic for courts
 */
class CourtsAPI(
    private val services: CourtsServices,
) : APIModel() {

    /**
     * Handles POST /courts
     * Creates a new court associated with a club and an authenticated user.
     *
     * @param req the HTTP request containing court data and user token
     * @return a Response with status 201 Created and the courtId
     */
    fun createCourt(req: Request): Response {
        val body  = Json.decodeFromString<CourtCreate>(req.bodyString())
        val token = req.requireToken()
        val resp  = services.createCourt(body, token)
        return Response(Status.CREATED).json(resp)
    }

    /**
     * Handles GET /courts/{courtId}
     * Retrieves details of a specific court by ID.
     *
     * @param req the HTTP request containing the path parameter "courtId"
     * @return a Response with status 200 OK and the court details
     */
    fun getCourtDetails(req: Request): Response {
        val id = req.path("courtId")?.toInt()
            ?: badRequest("Court ID must be provided")
        return Response(Status.OK).json(services.getCourtDetails(id))
    }

    /**
     * Handles GET /clubs/{clubId}/courts?skip=&limit=
     * Retrieves a paginated list of courts for a specific club.
     *
     * @param req the HTTP request containing path parameter "clubId" and optional "skip"/"limit"
     * @return a Response with status 200 OK and the list of courts
     */
    fun getCourts(req: Request): Response {
        val clubId = req.path("clubId")?.toInt()
            ?: badRequest("Club ID must be provided")

        val (limit, skip) = paging(req)
        val page = services.getCourts(clubId, skip, limit)

        return Response(Status.OK).json(
            CourtsListResponse(
                courts     = page.items,
                totalCount = page.totalCount
            )
        )
    }
}
