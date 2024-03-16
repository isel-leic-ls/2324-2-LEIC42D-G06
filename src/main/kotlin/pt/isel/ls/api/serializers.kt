package pt.isel.ls.api

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import pt.isel.ls.utils.dateFormatter
import java.time.LocalDateTime

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(dateFormatter))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), dateFormatter)
    }
}