package pt.isel.ls.filters

import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.core.with
import org.http4k.lens.RequestKey
import pt.isel.ls.utils.unauthorized
import java.util.UUID

/**
 * In http4k, a "lens" is a bidirectional accessor that allows safely extracting or injecting values
 * from and into HTTP messages (requests or responses). Lenses handle validation and type-safety
 * when working with headers, query parameters, bodies, etc.
 */

/**
 * Lens that stores the user token directly in the Request for downstream use.
 */
val UserTokenKey = RequestKey.required<UUID>("user-token")

/**
 * Extension property to access the user token from the Request.
 */
val Request.token: UUID
    get() = UserTokenKey(this)

/**
 * Authentication filter that checks for a valid Bearer token in the "Authorization" header.
 * If the token is valid, it is injected into the request; otherwise, responds with 401 Unauthorized.
 *
 * @return a Filter to apply authentication validation to request handlers
 */
fun authFilter(): Filter =
    Filter { next ->
        { req: Request ->
            // valida header
            val token =
                req.header("Authorization")
                    ?.removePrefix("Bearer ")
                    ?.trim()
                    ?.let { runCatching { UUID.fromString(it) }.getOrNull() }

            if (token == null) {
                unauthorized("Missing or invalid Authorization header")
            } else {
                // inject token into the request and proceed to handler
                next(req.with(UserTokenKey of token))
            }
        }
    }
