package pt.isel.ls.services

import pt.isel.ls.API.dtosAPI.rental.*
import pt.isel.ls.domain.Rental
import pt.isel.ls.services.dtosServices.Paged
import pt.isel.ls.storage.Storage
import pt.isel.ls.storage.dtosStorage.rental.*
import pt.isel.ls.utils.*
import java.time.LocalDate
import java.util.UUID

/**
 * Service class responsible for business logic related to rentals.
 *
 * Provides methods for creating, retrieving, updating, deleting rentals,
 * and checking availability of time slots.
 */
class RentalsServices(storage: Storage) : ServicesModel(storage) {

    /**
     * Creates a new rental if the court is available and date is valid.
     *
     * @param body the rental creation payload
     * @param token the user authentication token
     * @return a response containing the created rental ID
     */
    fun createRental(body: RentalCreate, token: UUID): RentalResponse {
        val user  = userByToken(token)
        val club  = clubOr404(body.clubId)
        val court = courtOr404(body.courtId)

        if (storage.courtStorage.getClubIdByCourtId(body.courtId) != body.clubId)
            badRequest("Court ${body.courtId} does not belong to club ${body.clubId}.")

        requireFuture(body.date)

        val inConflict = storage.rentalStorage.hasTimeConflict(
            court.id,
            body.date,
            body.date.plusHours(body.duration.toLong()),
            -1
        )
        if (inConflict) conflict("Court is not available for that slot.")

        val insert = RentalInsert(
            clubId = body.clubId,
            courtId = body.courtId,
            date = body.date,
            duration = body.duration
        )

        val rental = storage.rentalStorage.createRental(insert, user.id)
        return RentalResponse(rental.id)
    }

    /**
     * Retrieves rental details by ID.
     *
     * @param id the rental identifier
     * @return a DTO with full rental details
     */
    fun getRental(id: Int): RentalSearch.RentalDetails {
        requireId(id, "Rental id")
        val rental = rentalOr404(id)
        val club   = clubOr404(rental.clubId)
        val court  = courtOr404(rental.courtId)
        val user   = userOr404(rental.user)

        return RentalSearch.RentalDetails(
            rentalId  = rental.id,
            clubId    = club.id,
            clubName  = club.name,
            courtId   = court.id,
            courtName = court.name,
            userId    = user.id,
            userName  = user.name,
            date      = rental.date,
            duration  = rental.duration
        )
    }

    /**
     * Retrieves a paginated list of rentals for a specific court and optional date.
     */
    fun getRentals(
        clubId: Int,
        courtId: Int,
        dateStr: String?,
        skip: Int,
        limit: Int
    ): Paged<RentalSearch.RentalDetails> {
        val date = dateStr?.let {
            runCatching { LocalDate.parse(it) }
                .getOrElse { badRequest("Invalid date format. Must be yyyy-MM-dd.") }
        }

        val rentals = storage.rentalStorage.getRentals(clubId, courtId, date)
        return rentals.toDetails().page(skip, limit)
    }

    /**
     * Retrieves paginated list of rentals made by a specific user.
     */
    fun getUserRentals(userId: Int, skip: Int, limit: Int): Paged<RentalSearch.RentalDetails> =
        storage.rentalStorage.getUserRentals(userId)
            .toDetails()
            .page(skip, limit)

    /**
     * Returns available hourly slots for a court on a given date.
     */
    fun getAvailableHours(clubId: Int, courtId: Int, date: String): List<Map<String, String>> {
        clubOr404(clubId); courtOr404(courtId)

        if (storage.courtStorage.getClubIdByCourtId(courtId) != clubId)
            badRequest("Court $courtId does not belong to club $clubId.")

        val localDate = runCatching { LocalDate.parse(date) }
            .getOrElse { badRequest("Date must be yyyy-MM-dd.") }

        val taken = storage.rentalStorage
            .getAvailableHours(clubId, courtId, localDate.atStartOfDay())
            .map { it.hour }
            .toSet()

        return (0..23).map { h ->
            val label = "%02d:00".format(h)
            mapOf(
                "hour"   to label,
                "status" to if (h in taken) "OPEN" else "NO"
            )
        }
    }

    /**
     * Deletes a rental by ID if the user is the one who created it.
     */
    fun deleteRental(id: Int, token: UUID): Boolean {
        val user   = userByToken(token)
        val rental = rentalOr404(id)

        if (rental.user != user.id)
            forbidden("Not allowed to delete this rental.")

        return storage.rentalStorage.deleteRentalById(id)
    }

    /**
     * Updates an existing rental's date and duration if no conflict and user is owner.
     */
    fun updateRental(id: Int, upd: RentalUpdate, token: UUID): Boolean {
        val user   = userByToken(token)
        val rental = rentalOr404(id)

        if (rental.user != user.id)
            forbidden("Not allowed to update this rental.")

        requireFuture(upd.date)

        val conflict = storage.rentalStorage.hasTimeConflict(
            rental.courtId,
            upd.date,
            upd.date.plusHours(upd.duration.toLong()),
            id
        )
        if (conflict) conflict("Court is not available at that time.")

        val edit = RentalEdit(
            date = upd.date,
            duration = upd.duration
        )

        return storage.rentalStorage.updateRental(id, edit)
    }

    /**
     * Converts a list of Rental domain objects into API DTOs with full detail.
     */
    private fun List<Rental>.toDetails(): List<RentalSearch.RentalDetails> =
        map { r ->
            val club  = clubOr404(r.clubId)
            val court = courtOr404(r.courtId)
            val user  = userOr404(r.user)
            RentalSearch.RentalDetails(
                rentalId  = r.id,
                clubId    = club.id,
                clubName  = club.name,
                courtId   = court.id,
                courtName = court.name,
                userId    = user.id,
                userName  = user.name,
                date      = r.date,
                duration  = r.duration
            )
        }
}
