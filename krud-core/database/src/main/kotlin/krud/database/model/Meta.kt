/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package krud.database.model

import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import kotlinx.serialization.Serializable
import krud.database.schema.base.BaseTable
import krud.database.schema.base.TimestampedTable
import org.jetbrains.exposed.sql.ResultRow
import kotlin.uuid.Uuid

/**
 * Represents the metadata of a record.
 *
 * @property createdAt The timestamp when the record was created, in UTC.
 * @property createdBy The actor who created the record.
 * @property updatedAt The timestamp when the record was last updated, in UTC.
 * @property updatedBy The actor who last modified the record.
 */
@Serializable
public data class Meta(
    val createdAt: Instant,
    val createdBy: Uuid? = null,
    val updatedAt: Instant,
    val updatedBy: Uuid? = null
) {
    public companion object {
        /**
         * Maps a [ResultRow] to a [Meta] instance, converting timestamps to UTC.
         * This conversion ensures that the timestamps are timezone-agnostic
         * and can be consistently interpreted in any geographical location.
         *
         * @param row The [ResultRow] to map.
         * @param table The [TimestampedTable] from which the [ResultRow] was obtained.
         * @return The mapped [Meta] instance with timestamps in UTC.
         */
        public fun from(row: ResultRow, table: TimestampedTable): Meta {
            return Meta(
                createdAt = row[table.createdAt].toInstant().toKotlinInstant(),
                updatedAt = row[table.updatedAt].toInstant().toKotlinInstant()
            )
        }

        /**
         * Maps a [ResultRow] to a [Meta] instance, converting timestamps to UTC.
         * This conversion ensures that the timestamps are timezone-agnostic
         * and can be consistently interpreted in any geographical location.
         *
         * @param row The [ResultRow] to map.
         * @param table The [BaseTable] from which the [ResultRow] was obtained.
         * @return The mapped [Meta] instance with timestamps in UTC.
         */
        public fun from(row: ResultRow, table: BaseTable): Meta {
            return Meta(
                createdAt = row[table.createdAt].toInstant().toKotlinInstant(),
                createdBy = row[table.createdBy],
                updatedAt = row[table.updatedAt].toInstant().toKotlinInstant(),
                updatedBy = row[table.updatedBy]
            )
        }
    }
}
