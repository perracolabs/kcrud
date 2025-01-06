/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package krud.database.schema.base

import krud.database.column.kotlinUuid
import krud.database.schema.admin.actor.ActorTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import kotlin.uuid.Uuid

/**
 * Base class for database tables that tracks
 * the creation and modification metadata of records.
 */
public abstract class BaseTable(private val name: String) : TimestampedTable(name = name) {

    /**
     * Reference to actor who created the record.
     */
    public val createdBy: Column<Uuid> = kotlinUuid(
        name = "created_by"
    ).references(
        ref = ActorTable.id,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT,
        fkName = "fk_${name}__created_by"
    ).index(
        customIndexName = "ix_${name}__created_by"
    )

    /**
     * Reference to actor who last modified the record.
     */
    public val modifiedBy: Column<Uuid> = kotlinUuid(
        name = "modified_by"
    ).references(
        ref = ActorTable.id,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT,
        fkName = "fk_${name}__modified_by"
    ).index(
        customIndexName = "ix_${name}__modified_by"
    )
}
