@file:Suppress("ktlint:standard:no-wildcard-imports")

package pt.isel.ls

// http4k
import org.http4k.core.Filter
import org.http4k.core.Method.GET
import org.http4k.core.then
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import org.http4k.routing.ResourceLoader
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.singlePageApp
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer

// Padle
import pt.isel.ls.API.API
import pt.isel.ls.filters.authFilter
import pt.isel.ls.filters.errorFilter
import pt.isel.ls.API.routersAPI.clubsRouter
import pt.isel.ls.API.routersAPI.courtsRouter
import pt.isel.ls.API.routersAPI.rentalsRouter
import pt.isel.ls.API.routersAPI.usersRouter
import pt.isel.ls.services.Services
import pt.isel.ls.storage.Storage
import pt.isel.ls.utils.Logger

/**
 * Application server for the Padel backend.
 *
 * This class configures the HTTP server using http4k, initializes routes, applies filters,
 * and binds the API routers (users, clubs, courts, rentals).
 *
 * @property port The port where the server will listen.
 * @property Storage The storage backend to be used by services.
 */
class AppServer(
    private val port: Int,
    database: Storage,
) {

    private val server: Http4kServer

    init {
        // Set up CORS policy (unsafe, permissive for development)
        val cors: Filter = ServerFilters.Cors(CorsPolicy.UnsafeGlobalPermissive)

        // Filter for catching and formatting errors
        val errors = errorFilter()

        // Filter for extracting and validating authorization headers
        val auth: Filter = authFilter()

        // Instantiate services with access to the storage
        val services = Services(database)

        // Build the API layer using the services
        val api = API(services)

        // Configure routing with associated API handlers and filters
        val routing: RoutingHttpHandler = routes(
            usersRouter(api.userAPI),
            clubsRouter(api.clubsAPI, auth),
            courtsRouter(api.courtsAPI, auth),
            rentalsRouter(api.rentalsAPI, auth),

            docsRouter(),         // Serves OpenAPI YAML documentation
            swaggerRouter(),      // Serves Swagger UI static files
            singlePageApp(ResourceLoader.Directory("static-content")) // Fallback for SPA
        )

        // Build the HTTP server with middleware and routing
        server = cors
            .then(errors)   // Error handling comes first
            .then(routing)  // Then the routing logic
            .asServer(Jetty(port))
    }

    /**
     * Starts the HTTP server and logs the port.
     */
    fun start() {
        server.start()
        Logger.info("Server @ $port")
    }

    /**
     * Stops the HTTP server and logs the shutdown.
     */
    fun stop() {
        server.stop()
        Logger.info("Stopped")
    }
}


// === Helpers: return RoutingHttpHandler instances ===

/**
 * Serves the OpenAPI YAML documentation file for the API at `/docs`.
 * Returns 404 if the file is not found.
 */
private fun docsRouter(): RoutingHttpHandler =
    "/docs" bind GET to { _ ->
        val path = java.nio.file.Paths.get("docs/padel-API.yaml")
        if (java.nio.file.Files.exists(path))
            org.http4k.core.Response(org.http4k.core.Status.OK)
                .header("Content-Type", "text/plain")
                .body(java.nio.file.Files.readString(path))
        else
            org.http4k.core.Response(org.http4k.core.Status.NOT_FOUND)
                .body("padel-API.yaml not found")
    }

/**
 * Serves Swagger UI static files from the `/swagger` path.
 * Assumes the files are located in `public/swagger`.
 */
private fun swaggerRouter(): RoutingHttpHandler =
    "/swagger" bind org.http4k.routing.static(ResourceLoader
        .Directory("public/swagger"))
