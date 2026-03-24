package pt.isel.ls.filters

import org.http4k.core.Filter
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.utils.ApiException
import kotlinx.serialization.SerializationException

/**
 * In http4k, a filter is a middleware component that can process a request before it reaches the handler,
 * and/or process the response after it leaves the handler. Filters are used for cross-cutting concerns
 * such as logging, authentication, or error handling.
 */

/**
 * Global error-handling filter.
 *
 * Catches known application and serialization exceptions and returns appropriate HTTP responses,
 * ensuring consistent error formatting across the API.
 * Falls back to a generic 500 Internal Server Error for unexpected exceptions.
 *
 * @return a Filter that wraps request handling in a try-catch block for structured error responses
 */
fun errorFilter(): Filter = Filter { next ->
    { req ->
        try {
            next(req)
        } catch (e: ApiException) {
            Response(e.status)
                .header("Content-Type", "application/json")
                .body("""{ "message": "${e.message}" }""")

        } catch (e: SerializationException) {
            Response(Status.BAD_REQUEST)
                .header("Content-Type", "application/json")
                .body("""{ "message": "Invalid request body: ${e.message}" }""")

        } catch (t: Throwable) {
            t.printStackTrace()
            Response(Status.INTERNAL_SERVER_ERROR)
                .header("Content-Type", "application/json")
                .body("""{ "message": "Internal server error" }""")
        }
    }
}
