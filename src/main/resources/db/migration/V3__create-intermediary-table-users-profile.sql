CREATE TABLE users_profiles (
    user_id BIGINT NOT NULL,
    profiles_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, profiles_id),
    CONSTRAINT fk_users_profiles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_users_profiles_profile FOREIGN KEY (profiles_id) REFERENCES profiles (id) ON DELETE CASCADE
);