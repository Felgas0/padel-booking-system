package pt.isel.ls.storage.dataMem

import pt.isel.ls.domain.Rental
import pt.isel.ls.storage.RentalStorage
import pt.isel.ls.storage.dtosStorage.rental.RentalEdit
import pt.isel.ls.storage.dtosStorage.rental.RentalInsert
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * In-memory implementation of RentalStorage for testing or development purposes.
 * Stores data in a mutable map instead of a persistent database.
 */
class RentalsMem : RentalStorage {
    private val rentals = mutableMapOf<Int, Rental>()

    /**
     * Creates a new rental and stores it in memory.
     * Automatically assigns an incrementing ID based on the highest existing one.
     */
    override fun createRental(rental: RentalInsert, userId: Int): Rental {
        val id = rentals.keys.maxOfOrNull { it }?.plus(1) ?: 1
        val rentalInstance = Rental(
            id = id,
            date = rental.date,
            duration = rental.duration,
            user = userId,
            clubId = rental.clubId,
            courtId = rental.courtId
        )
        rentals[id] = rentalInstance
        return rentalInstance
    }

    /**
     * Retrieves a rental by its ID or null if not found.
     */
    override fun getRentalDetails(rentalId: Int): Rental? = rentals[rentalId]

    /**
     * Retrieves all rentals for a given club and court, optionally filtered by date.
     */
    override fun getRentals(clubId: Int, courtId: Int, date: LocalDate?): List<Rental> {
        return rentals.values.filter { it.matches(clubId, courtId, date) }
    }

    /**
     * Retrieves all rentals associated with a specific user.
     */
    override fun getUserRentals(userId: Int): List<Rental> =
        rentals.values.filter { it.user == userId }

    /**
     * Calculates all available hourly time slots (0 to 23) for a given court on a given day.
     * Filters out hours that are already occupied by existing rentals.
     */
    override fun getAvailableHours(clubId: Int, courtId: Int, date: LocalDateTime): List<LocalDateTime> {
        val rentalsOnDate = rentals.values.filter { it.matches(clubId, courtId, date.toLocalDate()) }

        val unavailable = mutableSetOf<Int>()
        rentalsOnDate.forEach { rental ->
            val startHour = rental.date.hour
            repeat(rental.duration) {
                unavailable.add(startHour + it)
            }
        }

        return (0..23)
            .filterNot { it in unavailable }
            .map { date.toLocalDate().atTime(it, 0) }
    }

    /**
     * Clears all stored rentals from memory. Typically used for test resets.
     */
    override fun reset() {
        rentals.clear()
    }

    /**
     * Deletes a rental by ID.
     * @return true if the rental existed and was removed, false otherwise
     */
    override fun deleteRentalById(rentalId: Int): Boolean =
        rentals.remove(rentalId) != null

    /**
     * Updates the date and duration of an existing rental.
     * @return true if the update succeeded, false if the rental was not found
     */
    override fun updateRental(rentalId: Int, rentalEdit: RentalEdit): Boolean {
        val rental = rentals[rentalId]
        return if (rental != null) {
            rentals[rentalId] = rental.copy(
                date = rentalEdit.date,
                duration = rentalEdit.duration
            )
            true
        } else false
    }

    /**
     * Checks whether a new rental conflicts in time with any existing ones.
     * Ignores a rental with the same ID if specified (used for updates).
     */
    override fun hasTimeConflict(
        courtId: Int,
        date: LocalDateTime,
        plusHours: LocalDateTime?,
        rentalId: Int
    ): Boolean =
        rentals.values.any { rental ->
            if (rentalId > 0 && rental.id == rentalId) return@any false
            rental.courtId == courtId &&
                    rental.date.isBefore(plusHours) &&
                    rental.date.plusHours(rental.duration.toLong()).isAfter(date)
        }

    /**
     * Checks whether this rental matches the given club, court, and optional date.
     *
     * This helper is used to reduce code duplication in in-memory filtering logic by centralizing
     * the predicate for selecting relevant rentals based on identifiers and optionally a date.
     *
     * @param clubId the expected club ID
     * @param courtId the expected court ID
     * @param date optional date to match against the rental's starting day
     * @return true if all criteria match, false otherwise
     */
    private fun Rental.matches(clubId: Int, courtId: Int, date: LocalDate?): Boolean =
        this.clubId == clubId &&
                this.courtId == courtId &&
                (date == null || this.date.toLocalDate().isEqual(date))
}
