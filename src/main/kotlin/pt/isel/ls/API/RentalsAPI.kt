package pt.isel.ls.API

import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.path
import pt.isel.ls.API.dtosAPI.rental.*
import pt.isel.ls.API.utils.*
import pt.isel.ls.filters.token
import pt.isel.ls.services.RentalsServices
import pt.isel.ls.utils.*

/**
 * Handles HTTP requests related to rentals.
 *
 * @property services the service layer that provides business logic for rentals
 */
class RentalsAPI(
    private val services: RentalsServices,
) {

    /**
     * Handles POST /rentals
     * Creates a new rental using the provided data and authenticated user.
     *
     * @param req the HTTP request with rental creation data and token
     * @return a Response with status 201 Created and rental ID
     */
    fun createRental(req: Request): Response {
        val body  = Json.decodeFromString<RentalCreate>(req.bodyString())
        val resp  = services.createRental(body, req.token)
        return Response(Status.CREATED).json(resp)
    }

    /**
     * Handles GET /rentals/{rentalId}
     * Retrieves details of a rental by ID.
     *
     * @param req the HTTP request with path parameter "rentalId"
     * @return a Response with status 200 OK and rental details
     */
    fun getRentalDetails(req: Request): Response {
        val id = req.path("rentalId")?.toInt()
            ?: badRequest("rentalId must be provided")
        return Response(Status.OK).json(services.getRental(id))
    }

    /**
     * Handles GET /clubs/{clubId}/courts/{courtId}/rentals?date=&skip=&limit=
     * Retrieves a paginated list of rentals for a specific court and optional date filter.
     *
     * @param req the HTTP request with clubId, courtId, optional date, skip, and limit
     * @return a Response with status 200 OK and list of rentals
     */
    fun getRentals(req: Request): Response {
        val clubId  = req.path("clubId") ?.toInt() ?: badRequest("clubId must be provided")
        val courtId = req.path("courtId")?.toInt() ?: badRequest("courtId must be provided")
        val date            = req.query("date")
        val (limit, skip)   = paging(req)

        val page = services.getRentals(clubId, courtId, date, skip, limit)
        return Response(Status.OK).json(
            RentalsListResponse(page.items, page.totalCount)
        )
    }

    /**
     * Handles GET /users/{userId}/rentals?skip=&limit=
     * Retrieves a paginated list of rentals made by a specific user.
     *
     * @param req the HTTP request with path parameter "userId" and optional "skip"/"limit"
     * @return a Response with status 200 OK and user rental list
     */
    fun getRentalsByUser(req: Request): Response {
        val userId = req.path("userId")?.toInt()
            ?: badRequest("userId must be provided")

        val (limit, skip) = paging(req)
        val page = services.getUserRentals(userId, skip, limit)

        return Response(Status.OK).json(
            RentalsListResponse(page.items, page.totalCount)
        )
    }

    /**
     * Handles GET /clubs/{clubId}/courts/{courtId}/available-hours?date=yyyy-MM-dd
     * Retrieves available time slots for a court on a specific date.
     *
     * @param req the HTTP request with clubId, courtId, and date query parameter
     * @return a Response with status 200 OK and list of available hours
     */
    fun getAvailableHours(req: Request): Response {
        val clubId  = req.path("clubId") ?.toInt() ?: badRequest("clubId must be provided")
        val courtId = req.path("courtId")?.toInt() ?: badRequest("courtId must be provided")
        val date    = req.query("date") ?: badRequest("date must be provided")

        return Response(Status.OK).json(
            services.getAvailableHours(clubId, courtId, date)
        )
    }

    /**
     * Handles DELETE /rentals/{rentalId}
     * Deletes a rental by ID, if the requesting user is authorized.
     *
     * @param req the HTTP request with path parameter "rentalId" and token
     * @return a Response with status 200 OK and success message
     */
    fun deleteRental(req: Request): Response {
        val id = req.path("rentalId")?.toInt()
            ?: notFound("rentalId must be provided")

        val ok = services.deleteRental(id, req.token)
        return Response(Status.OK).json(
            DeleteRentalResponse("Rental $id successfully deleted", ok)
        )
    }

    /**
     * Handles PUT /rentals/{rentalId}
     * Updates a rental by ID, if the requesting user is authorized, with the provided new date and duration.
     *
     * @param req the HTTP request with path parameter "rentalId", request body, and token
     * @return a Response with status 200 OK and success message
     */
    fun updateRental(req: Request): Response {
        val id = req.path("rentalId")?.toInt()
            ?: badRequest("rentalId must be provided")

        val body = Json.decodeFromString<RentalUpdate>(req.bodyString())
        val ok   = services.updateRental(id, body, req.token)

        return Response(Status.OK).json(
            RentalUpdateResponse("Rental $id successfully updated", ok)
        )
    }
}
