package pt.isel.ls.utils

import org.http4k.core.Response
import org.http4k.core.Status

/**
 * Base exception that includes an HTTP status code.
 *
 * @property status the HTTP status to be returned with the error
 * @constructor creates an exception with a specific HTTP status and message
 */
open class ApiException(val status: Status, message: String) : RuntimeException(message)

/**
 * Represents an HTTP 401 Unauthorized error.
 */
class UnauthorizedException(message: String) : ApiException(Status.UNAUTHORIZED, message)

/**
 * Represents an HTTP 403 Forbidden error.
 */
class ForbiddenException(message: String) : ApiException(Status.FORBIDDEN, message)

/**
 * Represents an HTTP 404 Not Found error.
 */
class NotFoundException(message: String) : ApiException(Status.NOT_FOUND, message)

/**
 * Represents an HTTP 400 Bad Request error.
 */
class BadRequestException(message: String) : ApiException(Status.BAD_REQUEST, message)

/**
 * Represents an HTTP 409 Conflict error.
 */
class ConflictException(message: String) : ApiException(Status.CONFLICT, message)


/**
 * ===========================================================================================
 * ===========================================================================================
 * ===========================================================================================
 * ===========================================================================================
 */


/**
 * Helper function to throw a 403 Forbidden exception.
 */
fun forbidden(message: String): Nothing = throw ForbiddenException(message)

/**
 * Helper function to throw a 401 Unauthorized exception.
 */
fun unauthorized(message: String): Nothing = throw UnauthorizedException(message)

/**
 * Helper function to throw a 404 Not Found exception.
 */
fun notFound(message: String): Nothing = throw NotFoundException(message)

/**
 * Helper function to throw a 400 Bad Request exception.
 */
fun badRequest(message: String): Nothing = throw BadRequestException(message)

/**
 * Helper function to throw a 409 Conflict exception.
 */
fun conflict(message: String): Nothing = throw ConflictException(message)
