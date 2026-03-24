package pt.isel.ls.API.dtosAPI.rental

import kotlinx.serialization.Serializable
import pt.isel.ls.utils.LocalDateTimeSerializer
import java.time.LocalDateTime

/**
 * Represents a request to update an existing rental.
 *
 * @property date the new starting date and time of the rental
 * @property duration the new duration of the rental in hours
 */
@Serializable
class RentalUpdate(
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime,
    val duration: Int,
)
