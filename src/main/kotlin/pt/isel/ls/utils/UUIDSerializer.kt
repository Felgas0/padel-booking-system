package pt.isel.ls.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

/**
 * Custom Kotlinx serializer for java.util.UUID.
 *
 * Serializes UUID objects into their standard string representation
 * (e.g. "123e4567-e89b-12d3-a456-426614174000") and deserializes
 * those strings back into UUID instances.
 *
 * This is useful when working with JSON serialization/deserialization
 * of UUIDs in data classes.
 */
object UUIDSerializer : KSerializer<UUID> {

    // Descriptor defines the type as a primitive string
    override val descriptor =
        PrimitiveSerialDescriptor(
            "UUID",
            PrimitiveKind.STRING,
        )

    /**
     * Deserializes a UUID from its string representation.
     */
    override fun deserialize(decoder: Decoder): UUID =
        UUID.fromString(decoder.decodeString())

    /**
     * Serializes a UUID into its string representation.
     */
    override fun serialize(
        encoder: Encoder,
        value: UUID,
    ) = encoder.encodeString(value.toString())
}
