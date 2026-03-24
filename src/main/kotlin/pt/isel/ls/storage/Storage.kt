package pt.isel.ls.storage

/**
 * Aggregates all storage interfaces for the application.
 *
 * Provides centralized access to different domain-specific storage layers (users, clubs, courts, rentals),
 * allowing implementations to provide either in-memory or persistent storage as needed.
 *
 * @property userStorage handles user-related persistence operations
 * @property clubStorage handles club-related persistence operations
 * @property courtStorage handles court-related persistence operations
 * @property rentalStorage handles rental-related persistence operations
 */
interface Storage {
    val userStorage: UserStorage
    val clubStorage: ClubStorage
    val courtStorage: CourtStorage
    val rentalStorage: RentalStorage

    /**
     * Clears all stored data across all storage components.
     * Intended primarily for testing or resetting application state.
     */
    fun reset()
}

