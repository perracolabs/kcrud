/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

-- noinspection SqlDialectInspectionForFile
-- https://www.red-gate.com/blog/database-devops/flyway-naming-patterns-matter

-------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS employment (
    employment_id UUID,
    employee_id UUID NOT NULL,
    status INTEGER NOT NULL,
    probation_end_date DATE NULL,
    work_modality INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NULL,
    comments VARCHAR(512) NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_employment_id PRIMARY KEY (employment_id),

    CONSTRAINT fk_employment__employee_id FOREIGN KEY (employee_id)
        REFERENCES employee(employee_id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE INDEX IF NOT EXISTS ix_employment__employee_id ON employment (employee_id);

-------------------------------------------------------------------------------------
