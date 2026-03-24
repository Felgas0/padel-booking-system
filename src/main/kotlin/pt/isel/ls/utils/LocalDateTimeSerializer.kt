package pt.isel.ls.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Custom Kotlinx serializer for java.time.LocalDateTime.
 *
 * Serializes LocalDateTime objects into ISO-8601 formatted strings (e.g. "2024-06-11T15:30:00")
 * and deserializes those strings back into LocalDateTime instances.
 *
 * This serializer is typically used for JSON encoding/decoding of LocalDateTime values.
 */
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {

    // Formatter using ISO_LOCAL_DATE_TIME (e.g. "yyyy-MM-dd'T'HH:mm:ss")
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // Descriptor defining the type as a primitive string
    override val descriptor =
        PrimitiveSerialDescriptor(
            "LocalDateTime",
            PrimitiveKind.STRING,
        )

    /**
     * Serializes a LocalDateTime into a string using the specified formatter.
     */
    override fun serialize(
        encoder: Encoder,
        value: LocalDateTime,
    ) {
        encoder.encodeString(formatter.format(value))
    }

    /**
     * Deserializes a string into a LocalDateTime using the specified formatter.
     */
    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}
