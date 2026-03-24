package pt.isel.ls

import pt.isel.ls.storage.dataMem.DataMem
import pt.isel.ls.storage.dataPostgres.DataPostgres
import pt.isel.ls.utils.seedInitialData

/**
 * Entry point for the application.
 *
 * This function starts the HTTP server on a specified port, using a PostgreSQL database.
 * If no environment variable "PORT" is found, the server waits for user input before stopping.
 */
fun main() {
    val port = 8080
//    val database = DataPostgres()
    val database = DataMem()
//    seedInitialData(database) // For local tests

    val server = AppServer(port, database)
    server.start()

    // If running locally (no "PORT" environment variable), wait for user to press Enter before stopping
    if (System.getenv("PORT") == null) {
        readln()
        server.stop()
    }
}
