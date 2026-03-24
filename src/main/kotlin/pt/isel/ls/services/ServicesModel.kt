package pt.isel.ls.services

import pt.isel.ls.storage.Storage
import pt.isel.ls.utils.*
import java.time.LocalDateTime
import java.util.UUID

/**
 * Base class for service-layer classes, providing shared validation and helper logic.
 *
 * This class encapsulates common access patterns such as authentication, entity resolution,
 * validation checks, and pagination logic.
 *
 * @property storage the shared data access layer used by all service classes
 */
abstract class ServicesModel(protected val storage: Storage) {

    /**
     * Resolves the user from the provided authentication token.
     * Throws UnauthorizedException if no user is found.
     *
     * @param token the UUID-based authentication token
     * @return the authenticated user
     */
    protected fun userByToken(token: UUID) =
        storage.userStorage.getUserByToken(token)
            ?: unauthorized("Missing or invalid Authorization header")

    // ────── Validation Helpers ─────────────────────────────────────────────

    /**
     * Ensures that a given integer ID is valid (positive).
     */
    protected fun requireId(id: Int, label: String = "Id") {
        if (id <= 0) badRequest("$label must be a positive integer")
    }

    /**
     * Ensures that a name string is not blank.
     */
    protected fun requireName(name: String, label: String = "Name") {
        if (name.isBlank()) badRequest("$label is required")
    }

    /**
     * Ensures that a given date-time is in the future.
     */
    protected fun requireFuture(dateTime: LocalDateTime) {
        if (dateTime.isBefore(LocalDateTime.now()))
            badRequest("Date-time must be in the future")
    }

    /**
     * Ensures that the requested limit is within the accepted range.
     */
    protected fun requireLimit(limit: Int, range: IntRange = 1..100) {
        if (limit !in range) badRequest("Limit must be in $range.")
    }

    /**
     * Ensures that skip is non-negative.
     */
    protected fun requireSkip(skip: Int) {
        if (skip < 0) badRequest("Skip must be ≥ 0.")
    }

    // ────── Entity Resolution (or 404) ─────────────────────────────────────

    /**
     * Resolves a club by ID or throws a NotFound exception.
     */
    protected fun clubOr404(id: Int) =
        storage.clubStorage.getClub(id) ?: notFound("Club $id not found.")

    /**
     * Resolves a court by ID or throws a NotFound exception.
     */
    protected fun courtOr404(id: Int) =
        storage.courtStorage.getCourtDetails(id) ?: notFound("Court $id not found.")

    /**
     * Resolves a rental by ID or throws a NotFound exception.
     */
    protected fun rentalOr404(id: Int) =
        storage.rentalStorage.getRentalDetails(id) ?: notFound("Rental $id not found.")

    /**
     * Resolves a user by ID or throws a NotFound exception.
     */
    protected fun userOr404(id: Int) =
        storage.userStorage.getUserDetails(id) ?: notFound("User $id not found.")

    // ────── Pagination Utility ─────────────────────────────────────────────

    /**
     * Returns a sublist of this list representing a paginated view.
     *
     * @param skip the number of items to skip
     * @param limit the maximum number of items to include in the page
     * @return a Paged<T> containing the selected items and total count
     */
    protected fun <T> List<T>.page(skip: Int, limit: Int) = run {
        requireSkip(skip); requireLimit(limit)
        val from = skip.coerceAtMost(size)
        val to   = (from + limit).coerceAtMost(size)
        pt.isel.ls.services.dtosServices.Paged(subList(from, to), size)
    }
}
