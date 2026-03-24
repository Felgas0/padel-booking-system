package pt.isel.ls.API.utils

import org.http4k.core.Request

/**
 * Extracts pagination parameters from an HTTP request query.
 *
 * @param request the HTTP request containing optional "limit" and "skip" query parameters
 * @return a pair where the first element is the limit (default 50) and the second is the skip (default 0)
 */
fun paging(request: Request): Pair<Int, Int> {
    val limit = request.query("limit")?.toIntOrNull() ?: 50
    val skip = request.query("skip")?.toIntOrNull() ?: 0
    return Pair(limit, skip)
}
