package com.classroom.db

import java.sql.Timestamp
import java.time.Instant
import java.time.OffsetDateTime
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class OffsetDateTimePersistenceConverter : AttributeConverter<OffsetDateTime?, Timestamp?> {
    override fun convertToDatabaseColumn(attribute: OffsetDateTime?): Timestamp? {
        return if (attribute == null) null else Timestamp.from(Instant.from(attribute))
    }

    override fun convertToEntityAttribute(dbData: Timestamp?): OffsetDateTime? {
        return if (dbData == null) null else OffsetDateTime.parse(dbData.toInstant().toString())
    }
}