CREATE TABLE password_reset_tokens (
    id BIGINT NOT NULL AUTO_INCREMENT,

    user_id BIGINT NOT NULL,

    token VARCHAR(255) NOT NULL,

    expires_at DATETIME(6) NOT NULL,

    used BOOLEAN NOT NULL DEFAULT FALSE,

    created_at DATETIME(6) NOT NULL,

    CONSTRAINT pk_password_reset_tokens
        PRIMARY KEY (id),

    CONSTRAINT uk_password_reset_tokens_token
        UNIQUE (token),

    CONSTRAINT fk_password_reset_tokens_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);