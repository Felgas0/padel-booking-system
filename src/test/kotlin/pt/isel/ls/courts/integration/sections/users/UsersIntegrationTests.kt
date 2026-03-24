package pt.isel.ls.courts.integration.sections.users

import kotlinx.serialization.json.*
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
 * Integration tests for the /users endpoint.
 * Covers user creation, login, retrieval, listing, and input validation.
 */
class UsersIntegrationTests : IntegrationTests() {
    private val client = JavaHttpClient()
    private val uriPrefix = "http://localhost:8080"

    /**
     * Tests that creating a user with valid data returns 201 Created and includes a token.
     */
    @Test
    fun `create user with valid data should return 201 and a valid token`() {
        val requestBody =
            """
            {
                "name": "Bob Doe",
                "email": "bob.doe@isel.pt",
                "password": "passwordBob"
            }
            """.trimIndent()

        val request =
            Request(Method.POST, "$uriPrefix/users")
                .header("Content-Type", "application/json")
                .body(requestBody)
        val response = client(request)

        assertEquals(Status.CREATED, response.status, "Expected 201 Created response")

        val responseBody = response.bodyString()
        println("Response: $responseBody")

        assertTrue(responseBody.contains("token"), "Response should contain token")
        assertTrue(responseBody.contains("userId"), "Response should contain user ID")
    }

    /**
     * Tests fetching a seeded user by ID. Should return correct name and email.
     */
    @Test
    fun `get user by id should return seeded user`() {
        seedInitialData(db)

        val request = Request(Method.GET, "$uriPrefix/users/1")
        val response = client(request)

        assertEquals(Status.OK, response.status)
        val body = response.bodyString()
        println("Response: $body")

        assertTrue(body.contains("António Pimentel"))
        assertTrue(body.contains("A42146@alunos.isel.pt"))
    }

    /**
     * Tests retrieving a user by a valid ID and checking returned details.
     */
    @Test
    fun `get user by valid id should return user details`() {
        seedInitialData(db)

        val userId = 1
        val request = Request(Method.GET, "$uriPrefix/users/$userId")
        val response = client(request)

        assertEquals(Status.OK, response.status, "Expected 200 OK")

        val body = response.bodyString()
        println("Response: $body")

        assertTrue(body.contains("António Pimentel"))
        assertTrue(body.contains("A42146@alunos.isel.pt"))
    }

    /**
     * Tests listing users after inserting one. Should include mock and new user.
     */
    @Test
    fun `get all users should return mock + created user`() {
        seedInitialData(db)

        val createRequestBody =
            """
            {
                "name": "Bob Doe",
                "email": "bob.doe@isel.pt",
                "password": "passwordBob"
            }
            """.trimIndent()

        val createRequest = Request(Method.POST, "$uriPrefix/users")
            .header("Content-Type", "application/json")
            .body(createRequestBody)

        val createResponse = client(createRequest)
        assertEquals(Status.CREATED, createResponse.status)

        val getRequest = Request(Method.GET, "$uriPrefix/users")
        val getResponse = client(getRequest)

        assertEquals(Status.OK, getResponse.status)

        val body = getResponse.bodyString()
        println("Response: $body")

        assertTrue(body.contains("António Pimentel"))
        assertTrue(body.contains("bob.doe@isel.pt"))
    }

    /**
     * Tests structure of user list response and validates expected fields.
     */
    @Test
    fun `get all users and check JSON structure`() {
        seedInitialData(db)

        val createRequestBody =
            """
            {
                "name": "Bob Doe",
                "email": "bob.doe@isel.pt",
                "password": "passwordBob"
            }
            """.trimIndent()

        val createRequest = Request(Method.POST, "$uriPrefix/users")
            .header("Content-Type", "application/json")
            .body(createRequestBody)

        val createResponse = client(createRequest)
        assertEquals(Status.CREATED, createResponse.status)

        val getRequest = Request(Method.GET, "$uriPrefix/users")
        val getResponse = client(getRequest)
        assertEquals(Status.OK, getResponse.status)

        val body = getResponse.bodyString()
        println("Response: $body")

        val root = Json.parseToJsonElement(body).jsonObject
        val usersJsonArray = root["users"]?.jsonArray
        val totalCount = root["totalCount"]?.jsonPrimitive?.int

        assertEquals(2, totalCount)

        val names = usersJsonArray?.map { it.jsonObject["name"]?.jsonPrimitive?.content }
        val emails = usersJsonArray?.map { it.jsonObject["email"]?.jsonObject?.get("value")?.jsonPrimitive?.content }

        assertTrue(names?.contains("António Pimentel") == true)
        assertTrue(emails?.contains("A42146@alunos.isel.pt") == true)
        assertTrue(names?.contains("Bob Doe") == true)
        assertTrue(emails?.contains("bob.doe@isel.pt") == true)
    }

    /**
     * Tests login with correct credentials should succeed and return token.
     */
    @Test
    fun `login with valid credentials should return 200 and token`() {
        val createRequest = Request(Method.POST, "$uriPrefix/users")
            .header("Content-Type", "application/json")
            .body(
                """
                {
                    "name": "Alice Login",
                    "email": "alice.login@isel.pt",
                    "password": "securePassword"
                }
                """.trimIndent()
            )
        val createResponse = client(createRequest)
        assertEquals(Status.CREATED, createResponse.status)

        val loginRequest = Request(Method.POST, "$uriPrefix/users/login")
            .header("Content-Type", "application/json")
            .body(
                """
                {
                    "name": "Alice Login",
                    "password": "securePassword"
                }
                """.trimIndent()
            )
        val loginResponse = client(loginRequest)
        val loginBody = loginResponse.bodyString()

        println("Login response: $loginBody")
        assertEquals(Status.OK, loginResponse.status)
        assertTrue(loginBody.contains("token"))
    }

    /**
     * Tests login attempt with incorrect password. Should return 401 Unauthorized.
     */
    @Test
    fun `login with incorrect password should return 401`() {
        val createResponse = client(
            Request(Method.POST, "$uriPrefix/users")
                .header("Content-Type", "application/json")
                .body(
                    """
                    {
                        "name": "WrongPassUser",
                        "email": "wrong.pass@isel.pt",
                        "password": "rightPassword"
                    }
                    """.trimIndent()
                )
        )
        assertEquals(Status.CREATED, createResponse.status)

        val loginRequest = Request(Method.POST, "$uriPrefix/users/login")
            .header("Content-Type", "application/json")
            .body(
                """
                {
                    "name": "WrongPassUser",
                    "password": "wrongPassword"
                }
                """.trimIndent()
            )
        val loginResponse = client(loginRequest)

        assertEquals(Status.UNAUTHORIZED, loginResponse.status)
        assertTrue(loginResponse.bodyString().contains("invalid", ignoreCase = true))
    }

    /**
     * Tests login with non-existent username. Should return 401 Unauthorized.
     */
    @Test
    fun `login with non-existent username should return 401`() {
        val loginRequest = Request(Method.POST, "$uriPrefix/users/login")
            .header("Content-Type", "application/json")
            .body(
                """
                {
                    "name": "GhostUser",
                    "password": "doesNotMatter"
                }
                """.trimIndent()
            )
        val loginResponse = client(loginRequest)

        assertEquals(Status.UNAUTHORIZED, loginResponse.status)
        assertTrue(loginResponse.bodyString().contains("invalid", ignoreCase = true))
    }

    /**
     * Tests that creating a user with an empty password returns 400 BadRequest.
     */
    @Test
    fun `create user with empty password should return 400 BadRequest`() {
        val requestBody =
            """
            {
                "name": "EmptyPassUser",
                "email": "empty.pass@isel.pt",
                "password": ""
            }
            """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/users")
            .header("Content-Type", "application/json")
            .body(requestBody)

        val response = client(request)

        assertEquals(Status.BAD_REQUEST, response.status)
        val body = response.bodyString()
        println("Response: $body")

        assertTrue(body.contains("password", ignoreCase = true))
    }

    /**
     * Tests that password length is enforced: too short or too long returns 400.
     */
    @Test
    fun `create user with password too short or too long should return 400`() {
        val shortPassRequestBody =
            """
            {
                "name": "Shorty",
                "email": "short@isel.pt",
                "password": "123"
            }
            """.trimIndent()

        val longPassRequestBody =
            """
            {
                "name": "Longy",
                "email": "long@isel.pt",
                "password": "${"a".repeat(26)}"
            }
            """.trimIndent()

        val shortRequest = Request(Method.POST, "$uriPrefix/users")
            .header("Content-Type", "application/json")
            .body(shortPassRequestBody)

        val longRequest = Request(Method.POST, "$uriPrefix/users")
            .header("Content-Type", "application/json")
            .body(longPassRequestBody)

        val shortResponse = client(shortRequest)
        val longResponse = client(longRequest)

        assertEquals(Status.BAD_REQUEST, shortResponse.status)
        assertTrue(shortResponse.bodyString().contains("Password must have between 5 and 25", ignoreCase = true))

        assertEquals(Status.BAD_REQUEST, longResponse.status)
        assertTrue(longResponse.bodyString().contains("Password must have between 5 and 25", ignoreCase = true))
    }
}
