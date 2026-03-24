package pt.isel.ls.API.dtosAPI.rental

import kotlinx.serialization.Serializable
import pt.isel.ls.utils.LocalDateTimeSerializer
import java.time.LocalDateTime

/**
 * Represents a sealed hierarchy for rental-related request and response models.
 * Future subtypes may include minimal summaries, error variants, etc.
 */
@Serializable
sealed class RentalSearch {

    /**
     * Represents detailed information about a rental.
     *
     * @property rentalId the unique identifier of the rental
     * @property clubId the identifier of the club where the rental occurs
     * @property clubName the name of the club
     * @property courtId the identifier of the rented court
     * @property courtName the name of the rented court
     * @property userId the identifier of the user who made the rental
     * @property userName the name of the user
     * @property date the starting date and time of the rental
     * @property duration the duration of the rental in hours
     */
    @Serializable
    data class RentalDetails(
        val rentalId: Int,
        val clubId: Int,
        val clubName: String,
        val courtId: Int,
        val courtName: String,
        val userId: Int,
        val userName: String,
        @Serializable(with = LocalDateTimeSerializer::class)
        val date: LocalDateTime,
        val duration: Int,
    ) : RentalSearch()


}
