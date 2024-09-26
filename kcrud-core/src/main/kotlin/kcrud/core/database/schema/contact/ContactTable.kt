/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package kcrud.core.database.schema.contact

import kcrud.core.database.columns.autoGenerate
import kcrud.core.database.columns.kotlinUuid
import kcrud.core.database.columns.references
import kcrud.core.database.columns.validVarchar
import kcrud.core.database.schema.base.TimestampedTable
import kcrud.core.database.schema.employee.EmployeeTable
import kcrud.core.errors.validators.EmailValidator
import kcrud.core.errors.validators.PhoneValidator
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import kotlin.uuid.Uuid

/**
 * Database table definition for employee contact details.
 * Demonstrates custom columns validators (see email and phone).
 */
public object ContactTable : TimestampedTable(name = "contact") {
    /**
     * The unique id of the contact record.
     */
    public val id: Column<Uuid> = kotlinUuid(
        name = "contact_id"
    ).autoGenerate()

    /**
     * The id of the employee to which the contact details belong.
     */
    public val employeeId: Column<Uuid> = kotlinUuid(
        name = "employee_id"
    ).references(
        ref = EmployeeTable.id,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT,
        fkName = "fk_contact__employee_id"
    ).index(
        customIndexName = "ix_contact__employee_id"
    )

    /**
     * The contact's personal email.
     */
    public val email: Column<String> = validVarchar(
        name = "email",
        length = EmailValidator.MAX_EMAIL_LENGTH,
        validator = EmailValidator
    ).index(
        customIndexName = "ix_contact__email"
    )

    /**
     * The contact's personal phone.
     */
    public val phone: Column<String> = validVarchar(
        name = "phone",
        length = PhoneValidator.MAX_PHONE_LENGTH,
        validator = PhoneValidator
    ).index(
        customIndexName = "ix_contact__phone"
    )

    /**
     * The table's primary key.
     */
    override val primaryKey: Table.PrimaryKey = PrimaryKey(
        firstColumn = id,
        name = "pk_contact_id"
    )
}
