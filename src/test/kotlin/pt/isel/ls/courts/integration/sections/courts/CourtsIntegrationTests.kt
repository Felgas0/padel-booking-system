package pt.isel.ls.courts.integration.sections.courts

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
 * Integration tests for the /courts endpoints.
 * Covers court creation, retrieval, and list structure validation.
 */
class CourtsIntegrationTests : IntegrationTests() {
    private val client = JavaHttpClient()
    private val uriPrefix = "http://localhost:8080"
    private val token = "00000000-0000-0000-0000-000000000001" // Token for António

    /**
     * Test that creating a court returns 201 Created and includes the court ID.
     */
    @Test
    fun `create a court for a club returns 201 and court id`() {
        seedInitialData(db)

        val requestBody =
            """
            {
              "name": "Antonio Court",
              "clubId": 1
            }
            """.trimIndent()

        val request =
            Request(Method.POST, "$uriPrefix/courts")
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .body(requestBody)

        val response = client(request)

        assertEquals(Status.CREATED, response.status)
        val body = response.bodyString()
        println("Response: $body")

        assertTrue(body.contains("courtId"), "Expected courtId in response")
    }

    /**
     * Test that getting a court by ID returns the correct court information.
     */
    @Test
    fun `get court details by ID returns correct court`() {
        seedInitialData(db)

        val createRequestBody =
            """
            {
              "name": "Antonio Court",
              "clubId": 1
            }
            """.trimIndent()

        val createRequest =
            Request(Method.POST, "$uriPrefix/courts")
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .body(createRequestBody)

        val createResponse = client(createRequest)
        assertEquals(Status.CREATED, createResponse.status)

        val createdBody = createResponse.bodyString()
        println("Create Response: $createdBody")

        val courtId =
            Regex("\"courtId\"\\s*:\\s*(\\d+)")
                .find(createdBody)
                ?.groupValues?.get(1)
                ?.toIntOrNull()
                ?: throw AssertionError("courtId not found in response")

        val getRequest = Request(Method.GET, "$uriPrefix/courts/$courtId")
        val getResponse = client(getRequest)

        assertEquals(Status.OK, getResponse.status)

        val body = getResponse.bodyString()
        println("Response: $body")

        assertTrue(body.contains("Antonio Court"), "Expected 'Antonio Court' in response")
        assertTrue(body.contains("\"clubId\":1"), "Expected clubId 1 in response")
    }

    /**
     * Test that retrieving all courts for a club returns all expected courts.
     */
    @Test
    fun `get all courts for a club returns created courts`() {
        seedInitialData(db)

        val createRequestBody =
            """
            {
              "name": "Antonio Court",
              "clubId": 1
            }
            """.trimIndent()

        val createRequest =
            Request(Method.POST, "$uriPrefix/courts")
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .body(createRequestBody)

        val createResponse = client(createRequest)
        assertEquals(Status.CREATED, createResponse.status)

        val getRequest = Request(Method.GET, "$uriPrefix/clubs/1/courts")
        val getResponse = client(getRequest)

        assertEquals(Status.OK, getResponse.status)
        val body = getResponse.bodyString()
        println("Response: $body")

        val courtCount = Regex("\"clubId\"\\s*:\\s*1").findAll(body).count()
        assertEquals(2, courtCount, "Expected 2 courts for club 1")

        assertTrue(body.contains("Main Court"), "Expected 'Main Court' from seed")
        assertTrue(body.contains("Antonio Court"), "Expected 'Antonio Court' from POST")
    }

    /**
     * Test that the court list endpoint returns the expected JSON structure.
     */
    @Test
    fun `get courts list structure returns correct format`() {
        seedInitialData(db)

        val request = Request(Method.GET, "$uriPrefix/clubs/1/courts")
        val response = client(request)

        assertEquals(Status.OK, response.status)

        val body = response.bodyString()
        println("Response: $body")

        assertTrue(body.contains("\"courts\":"), "Expected 'courts' field in response")
        assertTrue(body.contains("\"totalCount\":"), "Expected 'totalCount' field in response")

        val courtMatch =
            Regex("\\{[^}]*\"id\"\\s*:\\s*\\d+[^}]*\"name\"\\s*:\\s*\"[^\"]+\"[^}]*\"clubId\"\\s*:\\s*\\d+[^}]*}")
                .find(body)

        assertTrue(courtMatch != null, "Expected at least one court with id, name and clubId")
    }
}
