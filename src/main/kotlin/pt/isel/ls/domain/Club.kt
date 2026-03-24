package pt.isel.ls.domain

/**
 * Represents a padel club.
 *
 * @property id the unique identifier of the club
 * @property name the name of the club
 * @property ownerId the identifier of the user who owns the club
 * @property courts the list of courts that belong to the club
 *
 * @throws IllegalArgumentException if id or ownerId are not positive, or if name is blank
 */
data class Club(
    val id: Int,
    val name: String,
    val ownerId: Int,
    val courts: MutableList<Court>,
) {
    init {
        require(id > 0) { "Club id must be greater than or equal to zero." }
        require(name.isNotBlank()) { "Club name must not be blank." }
        require(ownerId > 0) { "Owner id must be greater than zero." }
    }
}
