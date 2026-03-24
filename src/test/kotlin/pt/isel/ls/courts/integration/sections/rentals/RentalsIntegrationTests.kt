package pt.isel.ls.courts.integration.sections.rentals

import org.http4k.client.JavaHttpClient
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.Test
import pt.isel.ls.courts.integration.IntegrationTests
import pt.isel.ls.utils.seedInitialData
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests for the /rentals endpoint.
 * Verifies rental creation, validation, conflict handling, and user rental retrieval.
 */
class RentalsIntegrationTests : IntegrationTests() {
    private val client = JavaHttpClient()
    private val uriPrefix = "http://localhost:8080"
    private val token = "00000000-0000-0000-0000-000000000001"

    /**
     * Tests that a rental can be created with valid data and returns 201 Created.
     */
    @Test
    fun `create rental with valid data should return 201 and rentalId`() {
        seedInitialData(db)

        val rentalBody =
            """
            {
              "clubId": 1,
              "courtId": 1,
              "date": "2099-08-01T10:00:00",
              "duration": 2
            }
            """.trimIndent()

        val request =
            Request(Method.POST, "$uriPrefix/rentals")
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .body(rentalBody)

        val response = client(request)

        assertEquals(Status.CREATED, response.status)
        val body = response.bodyString()
        println("Response: $body")

        assertTrue(body.contains("rentalId"), "Response should contain rentalId")
    }

    /**
     * Tests that attempting to create a rental with a non-existent club returns 404 Not Found.
     */
    @Test
    fun `create rental with non-existent club should return 404`() {
        seedInitialData(db)

        val invalidClubRentalBody =
            """
            {
              "clubId": 99,
              "courtId": 1,
              "date": "2099-08-01T10:00:00",
              "duration": 2
            }
            """.trimIndent()

        val request =
            Request(Method.POST, "$uriPrefix/rentals")
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .body(invalidClubRentalBody)

        val response = client(request)

        assertEquals(Status.NOT_FOUND, response.status)
        val body = response.bodyString()
        println("Response: $body")

        assertTrue(
            body.contains("not found", ignoreCase = true),
            "Expected error message about missing club"
        )
    }

    /**
     * Tests that a user can retrieve their rentals after creating one.
     */
    @Test
    fun `get rentals for user should return one created rental`() {
        seedInitialData(db)

        val rentalBody =
            """
            {
              "clubId": 1,
              "courtId": 1,
              "date": "2099-08-01T10:00:00",
              "duration": 2
            }
            """.trimIndent()

        val createRequest =
            Request(Method.POST, "$uriPrefix/rentals")
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .body(rentalBody)

        val createResponse = client(createRequest)
        assertEquals(Status.CREATED, createResponse.status)

        val getRequest = Request(Method.GET, "$uriPrefix/users/1/rentals")
        val getResponse = client(getRequest)

        assertEquals(Status.OK, getResponse.status)
        val responseBody = getResponse.bodyString()
        println("Rental list response: $responseBody")

        val rentalCount = Regex("\"courtId\"\\s*:\\s*1").findAll(responseBody).count()
        assertEquals(
            1,
            rentalCount,
            "Expected exactly 1 rental for user with id 1"
        )
    }

    /**
     * Tests that creating a rental in an already occupied time slot returns 409 Conflict.
     */
    @Test
    fun `create duplicate rental on same slot should return 409`() {
        seedInitialData(db)

        val rentalBody =
            """
            {
              "clubId": 1,
              "courtId": 1,
              "date": "2099-08-01T10:00:00",
              "duration": 2
            }
            """.trimIndent()

        val firstRequest =
            Request(Method.POST, "$uriPrefix/rentals")
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .body(rentalBody)

        val firstResponse = client(firstRequest)
        assertEquals(Status.CREATED, firstResponse.status)

        val secondResponse = client(firstRequest)
        assertEquals(Status.CONFLICT, secondResponse.status)

        val body = secondResponse.bodyString()
        println("Duplicate rental response: $body")

        assertTrue(
            body.contains("Court is not available", ignoreCase = true),
            "Expected error message about court not being available"
        )
    }
}
