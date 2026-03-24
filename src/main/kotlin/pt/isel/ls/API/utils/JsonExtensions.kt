package pt.isel.ls.API.utils

import kotlinx.serialization.json.Json
import org.http4k.core.Response

/**
 * Extension function to convert a response into a JSON response with the given body.
 *
 * @param T the type of the body object to be serialized
 * @param body the object to serialize into the response body
 * @return the original response with JSON content-type and serialized body
 */
inline fun <reified T> Response.json(body: T): Response =
    this.header("Content-Type", "application/json")
        .body(Json.encodeToString(body))
