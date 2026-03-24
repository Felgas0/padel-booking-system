package pt.isel.ls.services.dtosServices

/**
 * Generic data transfer object (DTO) representing a paginated response.
 *
 * @param T The type of items in the page.
 * @property items The list of items on the current page.
 * @property totalCount The total number of items available (across all pages).
 */
data class Paged<T>(val items: List<T>, val totalCount: Int)
