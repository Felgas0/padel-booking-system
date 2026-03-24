package pt.isel.ls.storage

import pt.isel.ls.domain.Rental
import pt.isel.ls.storage.dtosStorage.rental.RentalEdit
import pt.isel.ls.storage.dtosStorage.rental.RentalInsert
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Storage contract for Rental persistence.
 * This interface abstracts access to the rental data, whether stored in memory,
 * a relational database, or any other (Postgres). It exposes only domain-related types
 * (like Rental), and uses DTOs specific to the storage layer for input mutations.
 */
interface RentalStorage {

    /**
     * Creates and stores a new rental.
     *
     * @param rental the rental data to insert
     * @param userId the ID of the user making the rental
     * @return the created Rental object
     */
    fun createRental(rental: RentalInsert, userId: Int): Rental

    /**
     * Retrieves a rental by its unique ID.
     *
     * @param rentalId the rental ID
     * @return a Rental if found, or null otherwise
     */
    fun getRentalDetails(rentalId: Int): Rental?

    /**
     * Retrieves rentals for a specific court and optionally a specific date.
     *
     * @param clubId the club to which the court belongs
     * @param courtId the court ID
     * @param date optional filter for rentals on a specific date
     * @return the list of matching rentals
     */
    fun getRentals(
        clubId: Int,
        courtId: Int,
        date: LocalDate? = null
    ): List<Rental>

    /**
     * Retrieves all rentals made by a given user.
     *
     * @param userId the user ID
     * @return the list of the user's rentals
     */
    fun getUserRentals(userId: Int): List<Rental>

    /**
     * Returns a list of available rental start times (hour precision) for a given court and date.
     *
     * @param clubId the club ID
     * @param courtId the court ID
     * @param date the day for which availability is checked
     * @return list of available LocalDateTime slots
     */
    fun getAvailableHours(
        clubId: Int,
        courtId: Int,
        date: LocalDateTime,
    ): List<LocalDateTime>

    /**
     * Deletes all stored data, typically used for resetting in-memory state.
     */
    fun reset()

    /**
     * Deletes a rental by its ID.
     *
     * @param rentalId the rental ID
     * @return true if the rental existed and was deleted, false otherwise
     */
    fun deleteRentalById(rentalId: Int): Boolean

    /**
     * Updates the date and duration of a rental.
     *
     * @param rentalId the rental ID to update
     * @param rentalEdit the new values for date and duration
     * @return true if update was successful, false otherwise
     */
    fun updateRental(rentalId: Int, rentalEdit: RentalEdit): Boolean

    /**
     * Checks whether a rental would overlap with existing rentals for a given court.
     *
     * @param courtId the court ID
     * @param date the start of the rental
     * @param plusHours the end of the rental (start + duration)
     * @param rentalId the ID of the current rental (0 if new)
     * @return true if there is a conflict, false if slot is free
     */
    fun hasTimeConflict(
        courtId: Int,
        date: LocalDateTime,
        plusHours: LocalDateTime?,
        rentalId: Int,
    ): Boolean
}
