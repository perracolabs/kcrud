/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

-- noinspection SqlDialectInspectionForFile
-- https://www.red-gate.com/blog/database-devops/flyway-naming-patterns-matter

-------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS employment (
    employment_id UUID,
    employee_id UUID NOT NULL,
    status_id INTEGER NOT NULL,
    probation_end_date DATE NULL,
    work_modality_id INTEGER NOT NULL,
    sensitive_data VARCHAR(512),
    is_active BOOLEAN NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NULL,
    comments VARCHAR(512) NULL,
    created_by UUID NOT NULL,
    modified_by UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_employment__created_by
        FOREIGN KEY (created_by) REFERENCES actor(actor_id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT,

    CONSTRAINT fk_employment__modified_by
        FOREIGN KEY (modified_by) REFERENCES actor(actor_id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT,

    CONSTRAINT pk_employment_id
        PRIMARY KEY (employment_id),

    CONSTRAINT fk_employment__employee_id
        FOREIGN KEY (employee_id) REFERENCES employee(employee_id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);

CREATE INDEX IF NOT EXISTS ix_employment__created_by
    ON employment (created_by);

CREATE INDEX IF NOT EXISTS ix_employment__modified_by
    ON employment (modified_by);

CREATE INDEX IF NOT EXISTS ix_employment__employee_id
    ON employment (employee_id);

CREATE TRIGGER IF NOT EXISTS tg_employment__updated_at
    BEFORE UPDATE ON employment
    FOR EACH ROW CALL 'krud.database.util.UpdateTimestampTrigger';
