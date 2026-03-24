package pt.isel.ls.courts.integration.sections.clubs

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
 * Integration tests for the /clubs endpoints.
 * Covers club creation, retrieval, and search functionality.
 */
class ClubsIntegrationTests : IntegrationTests() {
    private val client = JavaHttpClient()
    private val uriPrefix = "http://localhost:8080"
    private val token = "00000000-0000-0000-0000-000000000001"

    /**
     * Test creating a club with a valid token should return 201 and a club ID.
     */
    @Test
    fun `create club with valid user token should return 201 and club ID`() {
        seedInitialData(db)

        val requestBody =
            """
            {
                "name": "New Club"
            }
            """.trimIndent()

        val request =
            Request(Method.POST, "$uriPrefix/clubs")
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .body(requestBody)

        val response = client(request)

        assertEquals(Status.CREATED, response.status)
        val body = response.bodyString()
        println("Response: $body")

        assertTrue(body.contains("clubId"))
    }

    /**
     * Test retrieving a club by ID returns its expected details.
     */
    @Test
    fun `get club by ID should return club details`() {
        seedInitialData(db)

        val request = Request(Method.GET, "$uriPrefix/clubs/1")
        val response = client(request)

        assertEquals(Status.OK, response.status)
        val body = response.bodyString()
        println("Response: $body")

        assertTrue(body.contains("Challengers is average"))
        assertTrue(body.contains("1")) // club id
    }

    /**
     * Test that all clubs are returned, including the seeded and newly created ones.
     */
    @Test
    fun `get all clubs should return seeded club and newly created one`() {
        seedInitialData(db)

        val requestBody =
            """
            {
                "name": "Antonio Club_2"
            }
            """.trimIndent()

        val createRequest =
            Request(Method.POST, "$uriPrefix/clubs")
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .body(requestBody)

        val createResponse = client(createRequest)
        assertEquals(Status.CREATED, createResponse.status, "Expected 201 Created")

        val getRequest = Request(Method.GET, "$uriPrefix/clubs")
        val getResponse = client(getRequest)

        assertEquals(Status.OK, getResponse.status)
        val body = getResponse.bodyString()
        println("Response: $body")

        assertTrue(body.contains("Challengers is average"), "Missing seeded club")
        assertTrue(body.contains("Antonio Club_2"), "Missing newly created club")
    }

    /**
     * Test searching for clubs by partial name returns matching results (case-insensitive).
     */
    @Test
    fun `search clubs by partial name returns matches`() {
        seedInitialData(db)

        listOf("Sporting Benfica", "Sporting Benfica Empate").forEach { name ->
            val body = """{ "name": "$name" }"""
            client(
                Request(Method.POST, "$uriPrefix/clubs")
                    .header("Authorization", "Bearer $token")
                    .header("Content-Type", "application/json")
                    .body(body)
            ).also { assertEquals(Status.CREATED, it.status) }
        }

        val resp = client(
            Request(Method.GET, "$uriPrefix/clubs/search")
                .query("name", "sporting")
                .query("skip", "0")
                .query("limit", "10")
        )

        assertEquals(Status.OK, resp.status)
        val body = resp.bodyString()
        println("SEARCH response → $body")

        assertTrue(body.contains("Sporting Benfica"))
        assertTrue(body.contains("Sporting Benfica Empate"))
        assertTrue(!body.contains("Challengers is average"))
    }

    /**
     * Test paginated search returns the correct number of items respecting skip & limit.
     */
    @Test
    fun `search clubs paginated respects skip and limit`() {
        seedInitialData(db)

        (1..3).forEach { idx ->
            val body = """{ "name": "Thunderbolts $idx" }"""
            client(
                Request(Method.POST, "$uriPrefix/clubs")
                    .header("Authorization", "Bearer $token")
                    .header("Content-Type", "application/json")
                    .body(body)
            ).also { assertEquals(Status.CREATED, it.status) }
        }

        val resp = client(
            Request(Method.GET, "$uriPrefix/clubs/search")
                .query("name", "thunderbolts")
                .query("skip", "1")
                .query("limit", "1")
        )

        assertEquals(Status.OK, resp.status)
        val body = resp.bodyString()
        println("PAGINATED response → $body")

        assertTrue(body.contains("\"totalCount\":"), "Expected totalCount field")
        assertTrue(body.contains("\"clubs\":"), "Expected clubs field")

        val nItems = "\"clubs\":\\s*\\[".toRegex()
            .split(body)[1]
            .takeWhile { it != ']' }
            .count { it == '{' }

        assertEquals(1, nItems, "limit=1 devia devolver só 1 item")
    }
}
