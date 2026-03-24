package pt.isel.ls.domain

/**
 * Represents a court within a club.
 *
 * @property id the unique identifier of the court
 * @property name the name of the court
 * @property clubId the identifier of the club to which the court belongs
 *
 * @throws IllegalArgumentException if id or clubId are not positive, or if name is blank
 */
data class Court(
    val id: Int,
    val name: String,
    val clubId: Int,
) {
    init {
        require(id > 0) { "Id must be greater than zero." }
        require(name.isNotBlank()) { "Name must not be blank." }
        require(clubId > 0) { "ClubId must be greater than zero." }
    }
}
