package pt.isel.ls.storage.dataMem

import pt.isel.ls.storage.*

/**
 * In-memory implementation of the [Storage] interface.
 *
 * This class provides temporary, non-persistent storage for users, clubs,
 * courts, and rentals. Useful for testing or development environments
 * where a real database is not needed.
 */
class DataMem : Storage {

    // In-memory implementations for each storage type
    override val userStorage = UsersMem()
    override val clubStorage = ClubsMem()
    override val courtStorage = CourtsMem()
    override val rentalStorage = RentalsMem()

    /**
     * Resets all in-memory storage components by clearing their data.
     */
    override fun reset() {
        userStorage.reset()
        clubStorage.reset()
        courtStorage.reset()
        rentalStorage.reset()
    }
}
