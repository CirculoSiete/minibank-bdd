
CREATE TABLE IF NOT EXISTS account (
    id UUID NOT NULL PRIMARY KEY,
    owner_id UUID NOT NULL,
    status VARCHAR(32) NOT NULL,
    currency VARCHAR(16) NOT NULL,
    amount NUMERIC(19, 4) NOT NULL
);

ALTER TABLE account
    ADD CONSTRAINT ck_account_amount_non_negative
        CHECK (amount >= 0);

CREATE INDEX IF NOT EXISTS idx_account_owner_id ON account(owner_id);
