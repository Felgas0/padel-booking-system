package pt.isel.ls.API.dtosAPI.rental

import kotlinx.serialization.Serializable
import pt.isel.ls.utils.LocalDateTimeSerializer
import java.time.LocalDateTime

/**
 * Represents a request to create a new rental.
 *
 * @property clubId the identifier of the club where the rental takes place
 * @property courtId the identifier of the court being rented
 * @property date the starting date and time of the rental
 * @property duration the duration of the rental in hours
 */
@Serializable
data class RentalCreate(
    val clubId: Int,
    val courtId: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime,
    val duration: Int,
)
