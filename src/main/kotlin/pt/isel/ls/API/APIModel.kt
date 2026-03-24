package pt.isel.ls.API

import org.http4k.core.Request
import java.util.UUID
import pt.isel.ls.filters.token
import pt.isel.ls.utils.UnauthorizedException

/**
 * Base class for all API handlers, providing common request utilities.
 */
abstract class APIModel {

    /**
     * Extracts the authorization token from the request or throws an UnauthorizedException if missing or invalid.
     *
     * @receiver the HTTP request
     * @return the UUID token extracted from the request
     * @throws UnauthorizedException if the token is missing or invalid
     */
    protected fun Request.requireToken(): UUID =
        try { this.token }
        catch (t: Throwable) {
            throw UnauthorizedException("Missing or invalid Authorization header")
        }
}
